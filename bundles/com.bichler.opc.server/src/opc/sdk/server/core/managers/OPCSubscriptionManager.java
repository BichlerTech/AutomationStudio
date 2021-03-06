package opc.sdk.server.core.managers;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.HistoryEvent;
import org.opcfoundation.ua.core.HistoryEventFieldList;
import org.opcfoundation.ua.core.HistoryReadResult;
import org.opcfoundation.ua.core.HistoryReadValueId;
import org.opcfoundation.ua.core.ModifySubscriptionResponse;
import org.opcfoundation.ua.core.MonitoredItemCreateRequest;
import org.opcfoundation.ua.core.MonitoredItemCreateResult;
import org.opcfoundation.ua.core.MonitoredItemModifyRequest;
import org.opcfoundation.ua.core.MonitoredItemModifyResult;
import org.opcfoundation.ua.core.MonitoringMode;
import org.opcfoundation.ua.core.NotificationMessage;
import org.opcfoundation.ua.core.RepublishResponse;
import org.opcfoundation.ua.core.SetTriggeringResponse;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.core.SubscriptionAcknowledgement;
import org.opcfoundation.ua.core.TimestampsToReturn;
import org.opcfoundation.ua.core.TransferSubscriptionsResponse;
import org.opcfoundation.ua.transport.tcp.io.SequenceNumber;
import org.opcfoundation.ua.utils.MultiDimensionArrayUtils;

import com.bichler.opc.comdrv.ComDRVManager;

import opc.sdk.core.enums.BuiltinType;
import opc.sdk.core.enums.MonitoredItemTypeMask;
import opc.sdk.core.enums.RequestType;
import opc.sdk.core.enums.ValueRanks;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.user.AuthorityRule;
import opc.sdk.core.subscription.IMonitoredItem;
import opc.sdk.core.types.TypeInfo;
import opc.sdk.core.types.TypeTable;
import opc.sdk.core.utils.NumericRange;
import opc.sdk.server.core.OPCInternalServer;
import opc.sdk.server.service.session.OPCServerSession;
import opc.sdk.server.service.subscribe.OPCMonitoredItem;
import opc.sdk.server.service.subscribe.OPCPublishOperation;
import opc.sdk.server.service.subscribe.OPCSessionPublishQueue;
import opc.sdk.server.service.subscribe.OPCSubscription;
import opc.sdk.server.service.subscribe.OPCSubscriptionConfiguratior;
import opc.sdk.server.service.subscribe.StatusMessage;
import opc.sdk.ua.classes.BaseEventType;

public class OPCSubscriptionManager implements IOPCManager {
	private Object lock = new Object();
	private OPCInternalServer server = null;
	private OPCSubscriptionConfiguratior configuration = null;
	/** Subscription id sequence number */
	private SequenceNumber seqNumber = new SequenceNumber();
	/** subscription set of opc server */
	private Map<Long, OPCSubscription> subscriptions;
	/** subscriptions to remove */
	/** publishing queues */
	private Map<NodeId, OPCSessionPublishQueue> publishQueues;
	/** driver manager */
	private OPCUserAuthentificationManager userAuthentification = null;
	private LinkedList<StatusMessage> statusMessages;
	private PublishingTimerTask task;

	public OPCSubscriptionManager(OPCInternalServer server) {
		this.server = server;
		this.configuration = server.getSubscriptionConfigurator();
		this.subscriptions = new HashMap<>();
		this.publishQueues = new HashMap<>();
		this.statusMessages = new LinkedList<>();
	}

	protected MonitoredItemCreateResult[] createMonitoredItems(UnsignedInteger subscriptionId,
			MonitoredItemCreateRequest[] itemsToCreate, TimestampsToReturn timestampsToReturn, OPCServerSession session)
			throws ServiceResultException {
		// nothing to do
		if (itemsToCreate == null || itemsToCreate.length == 0) {
			throw new ServiceResultException(StatusCodes.Bad_NothingToDo);
		}
		// valid tiemstamp
		if (timestampsToReturn == null || timestampsToReturn.getValue() < TimestampsToReturn.Source.getValue()
				|| timestampsToReturn.getValue() > TimestampsToReturn.Neither.getValue()) {
			throw new ServiceResultException(StatusCodes.Bad_TimestampsToReturnInvalid);
		}
		synchronized (this.lock) {
			// find subscription to add items
			OPCSubscription subscription = null;
			if ((subscription = this.subscriptions.get(subscriptionId.longValue())) == null) {
				throw new ServiceResultException(StatusCodes.Bad_SubscriptionIdInvalid);
			}
			MonitoredItemCreateResult[] results = new MonitoredItemCreateResult[itemsToCreate.length];
			for (int i = 0; i < itemsToCreate.length; i++) {
				MonitoredItemCreateRequest itemToCreate = itemsToCreate[i];
				MonitoredItemCreateResult result = new MonitoredItemCreateResult();
				results[i] = result;
				// validate and create monitored items
				StatusCode error = OPCValidationFramework.validateMonitoredItem(itemToCreate);
				if (error != null && error.isBad()) {
					result.setStatusCode(error);
					continue;
				}
				// find the node
				Node node = this.server.getAddressSpaceManager()
						.getNodeById(itemToCreate.getItemToMonitor().getNodeId());
				if (node == null) {
					result.setStatusCode(new StatusCode(StatusCodes.Bad_NodeIdUnknown));
					continue;
				}
				// check user authority
				AuthorityRule rule = null;
				MonitoredItemTypeMask typeMask = null;
				if (Attributes.Value.equals(itemToCreate.getItemToMonitor().getAttributeId())) {
					typeMask = MonitoredItemTypeMask.DataChange;
					rule = AuthorityRule.Datachange;
					error = OPCValidationFramework.validateDataEncoding(getServer().getTypeTable(),
							node.read(itemToCreate.getItemToMonitor().getAttributeId(), null),
							itemToCreate.getItemToMonitor().getDataEncoding());
					if (error != null && error.isBad()) {
						result.setStatusCode(error);
						continue;
					}
				} else if (Attributes.EventNotifier.equals(itemToCreate.getItemToMonitor().getAttributeId())) {
					typeMask = MonitoredItemTypeMask.Events;
					rule = AuthorityRule.Events;
				} else {
					typeMask = MonitoredItemTypeMask.Undefined;
				}
				error = this.userAuthentification.checkAuthorityFromNode(rule, session, node).getCode();
				if (error != null && error.isBad()) {
					result.setStatusCode(error);
					continue;
				}
				// create monitor item
				OPCMonitoredItem item = new OPCMonitoredItem(node, subscriptionId, itemToCreate, timestampsToReturn,
						typeMask);
				// add monitor item to subscription
				error = subscription.addMonitoredItem(item, this.server);
				server.logService(RequestType.CreateMonitoredItems,
						error + " MonitoredItem " + item.getMonitoredItemId() + " added on subscription "
								+ subscription.getSubscriptionId() + " watching node " + item.getNode().getNodeId());
				if (error.isBad()) {
					result.setStatusCode(error);
					continue;
				}
				// register on driver framework
				switch (item.getTypeMask()) {
				case DataChange:
					error = ComDRVManager.getDRVManager().registerNotification(node, itemToCreate);
					break;
				case Events:
				case AllEvents:
					error = ComDRVManager.getDRVManager().registerEvent(node, itemToCreate, session);
					break;
				case Undefined:
					// Non-Attribute Values for Non-Variables
					break;
				}
				// fill in results
				result.setFilterResult(item.getFilterResult());
				result.setMonitoredItemId(new UnsignedInteger(item.getMonitoredItemId()));
				result.setRevisedQueueSize(item.getQueueSize());
				result.setRevisedSamplingInterval(item.getSamplingInterval());
				result.setStatusCode(error);
			}
			return results;
		}
	}

	public HistoryReadResult[] historyEventRead(HistoryReadValueId[] nodesToRead, ExtensionObject historyReadDetails,
			Boolean releaseContinuationPoints, TimestampsToReturn timestampsToReturn, Long[] driverStates,
			OPCServerSession session) {
		return null;
	}

	protected OPCSubscription createSubscription(Double publishingInterval, UnsignedInteger maxLifetimeCount,
			UnsignedInteger maxKeepAliveCount, Boolean publishingEnabled, UnsignedInteger maxNotificationsPerPublish,
			UnsignedByte priority, OPCServerSession session) throws ServiceResultException {
		synchronized (this.lock) {
			// publishing interval
			Double revisedPublishingInterval = calculatePublishingInterval(publishingInterval);
			// keep alive count
			UnsignedInteger revisedMaxKeepAliveCount = calculateKeepAliveCount(revisedPublishingInterval,
					maxKeepAliveCount);
			// lifetime count
			UnsignedInteger revisedLifetimeCount = calculateLifetimeCount(revisedPublishingInterval,
					revisedMaxKeepAliveCount, maxLifetimeCount);
			// max notification per publish
			UnsignedInteger revisedMaxNotificationsPerPublish = calculateMaxNotificationPerPublish(
					maxNotificationsPerPublish);
			int id = this.seqNumber.getCurrentSendSequenceNumber();
			this.seqNumber.getNextSendSequencenumber();
			// too many subscriptions
			if (this.subscriptions.size() >= this.configuration.getMaxSubscriptionsCount()) {
				throw new ServiceResultException(StatusCodes.Bad_TooManySubscriptions);
			}
			OPCSubscription subscription = new OPCSubscription(session, id, revisedPublishingInterval,
					revisedLifetimeCount, revisedMaxKeepAliveCount, revisedMaxNotificationsPerPublish, priority,
					publishingEnabled, this.configuration.getMaxMessageCount(),
					this.configuration.getMaxPublishRequest());
			addSubscription(session, subscription);
			// TODO: subscription diagnsotics
			return subscription;
		}
	}

	protected StatusCode[] deleteMonitoredItems(UnsignedInteger subscriptionId, UnsignedInteger[] monitoredItemIds,
			OPCServerSession session) throws ServiceResultException {
		if (subscriptionId == null) {
			throw new ServiceResultException(StatusCodes.Bad_SubscriptionIdInvalid);
		}
		if (monitoredItemIds == null || monitoredItemIds.length == 0) {
			throw new ServiceResultException(StatusCodes.Bad_NothingToDo);
		}
		synchronized (this.lock) {
			OPCSubscription subscription = this.subscriptions.get(subscriptionId.longValue());
			if (subscription == null) {
				throw new ServiceResultException(StatusCodes.Bad_SubscriptionIdInvalid);
			}
			// int currentMonitoredItemCount = subscription
			// .getMonitoredItemCount();
			StatusCode[] results = subscription.deleteMonitoredItems(monitoredItemIds);
			// int monitoredItemIncrement = subscription.getMonitoredItemCount()
			// - currentMonitoredItemCount;
			/** session diagnostics */
			// SessionDiagnostics[] diagnostics = {
			// SessionDiagnostics.CurrentMonitoredItemsCount };
			//
			// UnsignedInteger value = subscription.getSession()
			// .getSessionDiagnostics()
			// .getCurrentMonitoredItemsCount()
			// .add(monitoredItemIncrement);
			return results;
		}
	}

	/**
	 * Delete all subscriptions from a session.
	 * 
	 * @param sessionId
	 * @throws ServiceResultException
	 */
	protected void deleteAllSubscriptionsFromSession(OPCServerSession session) throws ServiceResultException {
		synchronized (this.lock) {
			// delete the queue needs to be synchronized
			OPCSessionPublishQueue queue = this.publishQueues.get(session.getAuthentifikationToken());
			if (queue == null) {
				return;
			}
			// get subscriptionids from session queue
			UnsignedInteger[] subscriptionIds = queue.getSubscriptionIds();
			// delte subscriptions
			deleteSubscriptions(subscriptionIds);
		}
	}

	/**
	 * Delete subscriptions with its IDs.
	 * 
	 * @param subscriptionIds
	 * @return
	 * @throws ServiceResultException
	 */
	protected StatusCode[] deleteSubscriptions(UnsignedInteger[] subscriptionIds) throws ServiceResultException {
		if (subscriptionIds == null || subscriptionIds.length == 0) {
			throw new ServiceResultException(StatusCodes.Bad_NothingToDo);
		}
		synchronized (this.lock) {
			StatusCode[] results = new StatusCode[subscriptionIds.length];
			for (int i = 0; i < subscriptionIds.length; i++) {
				/** subscription */
				long subscriptionId = subscriptionIds[i].longValue();
				OPCSubscription subscription = this.subscriptions.get(subscriptionId);
				if (subscription == null) {
					/** no subscription to delete */
					results[i] = new StatusCode(StatusCodes.Bad_SubscriptionIdInvalid);
					continue;
				}
				NodeId sessionId = subscription.getSessionId();
				/** delete the queue needs to be synchronized */
				OPCSessionPublishQueue queue = this.publishQueues.get(sessionId);
				if (queue == null) {
					results[i] = new StatusCode(StatusCodes.Bad_SubscriptionIdInvalid);
					continue;
				}
				queue.removeSubscription(subscriptionId);
				// int monitoredItemCount =
				// subscription.getMonitoredItemCount();
				/** frees all resources from the subscription */
				this.subscriptions.remove(subscriptionId);
				subscription.dispose();
				if (queue.isEmpty()) {
					this.publishQueues.remove(sessionId);
				}
				// maybe TODO: cancle publishing task
				results[i] = StatusCode.GOOD;
			}
			server.logService(RequestType.DeleteSubscriptions, "Subscriptions " + Arrays.toString(subscriptionIds)
					+ " with results " + Arrays.toString(results) + " are removed");
			return results;
		}
	}

	protected MonitoredItemModifyResult[] modifyMonitoredItems(UnsignedInteger subscriptionId,
			MonitoredItemModifyRequest[] itemsToModify, TimestampsToReturn timestampsToReturn, OPCServerSession session)
			throws ServiceResultException {
		// invalid subscription id
		if (subscriptionId == null) {
			throw new ServiceResultException(StatusCodes.Bad_SubscriptionIdInvalid);
		}
		// nothing to do
		if (itemsToModify == null || itemsToModify.length == 0) {
			throw new ServiceResultException(StatusCodes.Bad_NothingToDo);
		}
		// valid tiemstamp
		if (timestampsToReturn == null || timestampsToReturn.getValue() < TimestampsToReturn.Source.getValue()
				|| timestampsToReturn.getValue() > TimestampsToReturn.Neither.getValue()) {
			throw new ServiceResultException(StatusCodes.Bad_TimestampsToReturnInvalid);
		}
		synchronized (this.lock) {
			OPCSubscription subscription = this.subscriptions.get(subscriptionId.longValue());
			if (subscription == null) {
				throw new ServiceResultException(StatusCodes.Bad_SubscriptionIdInvalid);
			}
			MonitoredItemModifyResult[] results = subscription.modifyMonitoredItems(itemsToModify, timestampsToReturn,
					this.server.getTypeTable());
			return results;
		}
	}

	protected ModifySubscriptionResponse modifySubscription(UnsignedInteger subscriptionId,
			Double requestedPublishingInterval, UnsignedInteger requestedMaxKeepAliveCount,
			UnsignedInteger requestedLifetimeCount, UnsignedInteger maxNotificationsPerPublish, UnsignedByte priority,
			OPCServerSession session) throws ServiceResultException {
		synchronized (this.lock) {
			// find the subscription */
			OPCSubscription subscription = this.subscriptions.get(subscriptionId.longValue());
			if (subscription == null) {
				throw new ServiceResultException(StatusCodes.Bad_SubscriptionIdInvalid);
			}
			// calculate publishing interval
			Double revisedPublishingInterval = calculatePublishingInterval(requestedPublishingInterval);
			// calculate the keepalive counter
			UnsignedInteger revisedMaxKeepAliveCounter = calculateKeepAliveCount(revisedPublishingInterval,
					requestedMaxKeepAliveCount);
			// calculate the lifetime count
			UnsignedInteger revisedLifetimeCount = calculateLifetimeCount(revisedPublishingInterval,
					revisedMaxKeepAliveCounter, requestedLifetimeCount);
			// calculate the max notification count
			UnsignedInteger revisedMaxNotificationsPerPublish = calculateMaxNotificationPerPublish(
					maxNotificationsPerPublish);
			// update the subscription
			subscription.modifySubscription(revisedPublishingInterval, revisedLifetimeCount, revisedMaxKeepAliveCounter,
					revisedMaxNotificationsPerPublish, priority);
			// response
			ModifySubscriptionResponse response = new ModifySubscriptionResponse();
			response.setRevisedLifetimeCount(revisedLifetimeCount);
			response.setRevisedMaxKeepAliveCount(revisedMaxKeepAliveCounter);
			response.setRevisedPublishingInterval(revisedPublishingInterval);
			return response;
		}
	}

	protected void publish(SubscriptionAcknowledgement[] acks, OPCPublishOperation operation, boolean isDif)
			throws ServiceResultException {
		synchronized (this.lock) {
			OPCSessionPublishQueue queue = null;
			queue = this.publishQueues.get(operation.getSessionId());
			if (queue == null) {
				throw new ServiceResultException(StatusCodes.Bad_NoSubscription);
			}
			queue.storePublishRequest(operation);
		}
	}

	protected RepublishResponse republish(UnsignedInteger subscriptionId, UnsignedInteger retransmitSequenceNumber)
			throws ServiceResultException {
		OPCSubscription subscription = null;
		// Array.newInstance(java.lang.Class.forName(className), size);
		synchronized (this.lock) {
			if ((subscription = this.subscriptions.get(subscriptionId.longValue())) == null) {
				throw new ServiceResultException(StatusCodes.Bad_SubscriptionIdInvalid);
			}
			NotificationMessage message = subscription.republish(retransmitSequenceNumber);
			RepublishResponse response = new RepublishResponse();
			response.setNotificationMessage(message);
			return response;
		}
	}

	protected StatusCode[] setPublishingMode(UnsignedInteger[] subscriptionIds, Boolean publishingEnabled,
			OPCServerSession session) {
		synchronized (this.lock) {
			StatusCode[] results = new StatusCode[subscriptionIds.length];
			for (int i = 0; i < subscriptionIds.length; i++) {
				OPCSubscription subscription = this.subscriptions.get(subscriptionIds[i].longValue());
				if (subscription == null) {
					results[i] = new StatusCode(StatusCodes.Bad_SubscriptionIdInvalid);
					continue;
				}
				results[i] = subscription.setPublishingMode(publishingEnabled);
			}
			return results;
		}
	}

	protected SetTriggeringResponse setTriggering(UnsignedInteger subscriptionId, UnsignedInteger triggeringItemId,
			UnsignedInteger[] linksToAdd, UnsignedInteger[] linksToRemove, OPCServerSession session)
			throws ServiceResultException {
		if (subscriptionId == null) {
			throw new ServiceResultException(StatusCodes.Bad_SubscriptionIdInvalid);
		}
		if (triggeringItemId == null) {
			throw new ServiceResultException(StatusCodes.Bad_NothingToDo);
		}
		synchronized (this.lock) {
			List<StatusCode> resultAdd = new ArrayList<>();
			List<StatusCode> resultRemove = new ArrayList<>();
			OPCSubscription subscription = this.subscriptions.get(subscriptionId.longValue());
			if (subscription == null) {
				throw new ServiceResultException(StatusCodes.Bad_SubscriptionIdInvalid);
			}
			subscription.setTriggering(session, triggeringItemId, linksToAdd, linksToRemove, resultAdd, resultRemove);
			SetTriggeringResponse response = new SetTriggeringResponse();
			response.setAddResults(resultAdd.toArray(new StatusCode[0]));
			response.setRemoveResults(resultRemove.toArray(new StatusCode[0]));
			return response;
		}
	}

	protected StatusCode[] setMonitoringMode(UnsignedInteger subscriptionId, UnsignedInteger[] monitoredItemIds,
			MonitoringMode monitoringMode, OPCServerSession session) throws ServiceResultException {
		if (subscriptionId == null) {
			throw new ServiceResultException(StatusCodes.Bad_SubscriptionIdInvalid);
		}
		if (monitoredItemIds == null || monitoredItemIds.length == 0) {
			throw new ServiceResultException(StatusCodes.Bad_NothingToDo);
		}
		synchronized (this.lock) {
			OPCSubscription subscription = null;
			if ((subscription = this.subscriptions.get(subscriptionId.longValue())) == null) {
				throw new ServiceResultException(StatusCodes.Bad_SubscriptionIdInvalid);
			}
			StatusCode[] results = subscription.setMonitoringMode(monitoredItemIds, monitoringMode);
			return results;
		}
	}

	protected TransferSubscriptionsResponse transferSubscription(UnsignedInteger[] subscriptionIds,
			Boolean sendInitialValues, OPCServerSession session) {
		return new TransferSubscriptionsResponse();
	}

	/**
	 * Publish subscriptions
	 */
	void publishSubscriptions() {
		UnsignedInteger[] subscriptionIds = null;
		Set<OPCSubscription> subscriptions2remove = new HashSet<>();
		// collect invalid subscriptions
		for (OPCSessionPublishQueue queue : this.publishQueues.values()) {
			List<OPCSubscription> expired = queue.publishTimerExpired();
			subscriptions2remove.addAll(expired);
		}
		if (!subscriptions2remove.isEmpty()) {
			subscriptionIds = new UnsignedInteger[subscriptions2remove.size()];
			int i = 0;
			for (OPCSubscription subscription : subscriptions2remove) {
				subscriptionIds[i] = subscription.getSubscriptionId();
				i++;
			}
		}
		// remove invalid subscriptions
		try {
			if (subscriptionIds != null) {
				deleteSubscriptions(subscriptionIds);
			}
		} catch (ServiceResultException e) {
			// BAD_NOTING_TO_DO
			e.printStackTrace();
		}
	}

	/**
	 * Adds a subscription to the server
	 * 
	 * @param sessionAuthentificationToken
	 * 
	 * @param Session                      Session to use.
	 * @param Subscription                 Subscription to add.
	 */
	private void addSubscription(OPCServerSession session, OPCSubscription subscription) {
		OPCSessionPublishQueue queue = this.publishQueues.get(session.getAuthentifikationToken());
		// create/update publish queue
		if (queue == null) {
			queue = new OPCSessionPublishQueue(this.server, session, this.configuration.getMaxPublishRequest());
			this.publishQueues.put(session.getAuthentifikationToken(), queue);
		}
		// save subscription on manager
		this.subscriptions.put(subscription.getSubscriptionId().longValue(), subscription);
		server.logService(RequestType.CreateSubscription, "Subscription " + subscription.getSubscriptionId()
				+ " on session " + session.getSessionname() + " " + session.getSessionId() + " has been created");
		// add subscription to publish
		queue.addSubscription(subscription);
	}

	/**
	 * Calculates the keep alive count.
	 * 
	 * @param PublishingInterval Current publish interval.
	 * @param KeepAliveCount     Requested new keepalive count.
	 * @return Calculated new keepalive count.
	 */
	private UnsignedInteger calculateKeepAliveCount(Double publishingInterval, UnsignedInteger keepAliveCount) {
		/** set default */
		if (keepAliveCount.getValue() == 0) {
			keepAliveCount = new UnsignedInteger(3);
		}
		double keepAliveInternal = keepAliveCount.longValue() * publishingInterval;
		/**
		 * keep alive interval cannot be longer than the max subscription lifetime
		 */
		if (keepAliveInternal > this.configuration.getMaxSubscriptionLifetime().longValue()) {
			keepAliveCount = new UnsignedInteger(
					this.configuration.getMaxSubscriptionLifetime().longValue() / publishingInterval.longValue());
		}
		if (keepAliveCount.getValue() < UnsignedInteger.MAX_VALUE.getValue()) {
			if (this.configuration.getMaxPublishingInterval() % publishingInterval != 0) {
				keepAliveCount = keepAliveCount.inc();
			}
		}
		return keepAliveCount;
	}

	/**
	 * Calculates the lifetime count.
	 * 
	 * @param PublishingInterval Current publishing interval.
	 * @param KeepAliveCount     Current keepalive count.
	 * @param LifetimeCount      Requested new lifetime count
	 * @return Calculated lifetime count.
	 */
	private UnsignedInteger calculateLifetimeCount(Double publishingInterval, UnsignedInteger keepAliveCount,
			UnsignedInteger lifetimeCount) {
		double lifetimeInterval = lifetimeCount.longValue() * publishingInterval;
		// lifetime cannot be longer than the max subscription lifetime
		if (lifetimeInterval > this.configuration.getMaxSubscriptionLifetime().longValue()) {
			lifetimeCount = new UnsignedInteger(
					this.configuration.getMaxSubscriptionLifetime().longValue() / publishingInterval.longValue());
			if (lifetimeCount.longValue() < UnsignedInteger.MAX_VALUE.longValue()) {
				if (this.configuration.getMaxSubscriptionLifetime().longValue() % publishingInterval != 0) {
					lifetimeCount = lifetimeCount.inc();
				}
			}
		}
		// lifetime must be greater than the keepalive
		if (keepAliveCount.longValue() < (UnsignedInteger.L_MAX_VALUE / 3)) {
			if ((keepAliveCount.longValue() * 3) > lifetimeCount.longValue()) {
				lifetimeCount = new UnsignedInteger(keepAliveCount.longValue() * 3);
			}
			lifetimeInterval = lifetimeCount.longValue() * publishingInterval;
		} else {
			lifetimeCount = UnsignedInteger.MAX_VALUE;
			lifetimeInterval = Double.MAX_VALUE;
		}
		// apply the minimum
		if (this.configuration.getMinSubscriptionLifetime().longValue() > publishingInterval
				&& this.configuration.getMinSubscriptionLifetime().longValue() > lifetimeInterval) {
			lifetimeCount = new UnsignedInteger(
					((Double) (this.configuration.getMinSubscriptionLifetime().longValue() / publishingInterval))
							.longValue());
			if (lifetimeCount.longValue() < UnsignedInteger.MAX_VALUE.longValue()) {
				if (this.configuration.getMinSubscriptionLifetime().longValue() % publishingInterval != 0) {
					lifetimeCount = lifetimeCount.inc();
				}
			}
		}
		return lifetimeCount;
	}

	/**
	 * Calculates the publishing interval.
	 * 
	 * @param PublishingInterval Requested new publish interval.
	 * @return Calculated publishing interval.
	 */
	private Double calculatePublishingInterval(Double publishingInterval) {
		/** NaN values get wrawpped to -1 <- not valid but less than zero */
		if (publishingInterval.compareTo(Double.NaN) == 0) {
			publishingInterval = -1.0;
		}
		/** bigger than max */
		if (publishingInterval > this.configuration.getMaxPublishingInterval()) {
			publishingInterval = this.configuration.getMaxPublishingInterval();
		}
		/** smaller than min */
		if (publishingInterval < this.configuration.getMinPublishingInterval()) {
			publishingInterval = this.configuration.getMinPublishingInterval();
		}
		/** even smaller than update resolution */
		if (publishingInterval < this.configuration.getPublishingResolution()) {
			publishingInterval = (double) this.configuration.getPublishingResolution();
		}
		/** recalculate resolution with interval */
		double resolution = 0;
		if ((resolution = publishingInterval % this.configuration.getPublishingResolution()) != 0) {
			// set next value
			publishingInterval = publishingInterval - resolution + this.configuration.getPublishingResolution();
			// publishingInterval = (double) (publishingInterval
			// / (this.configuration.getPublishingResolution()) + 1)
			// * this.configuration.getPublishingResolution();
		}
		return publishingInterval;
	}

	/**
	 * Calculates the maximum number of notifications per publish.
	 * 
	 * @param MaxNotificationsPerPublish Maximum notifications allowed to send per
	 *                                   session.
	 * @return Calculated MaxNotifiactionsPerPublish value.
	 */
	private UnsignedInteger calculateMaxNotificationPerPublish(UnsignedInteger maxNotificationsPerPublish) {
		if (maxNotificationsPerPublish.intValue() == 0 || maxNotificationsPerPublish.longValue() > this.configuration
				.getMaxNotificationPerPublish().longValue()) {
			return this.configuration.getMaxNotificationPerPublish();
		}
		return maxNotificationsPerPublish;
	}

	/**
	 * Updates the value for the index range on a monitored item.
	 * 
	 * @param node
	 * @param indexRange
	 * @param Value      Updated destination value
	 * @param typeTable
	 */
	private void updateIndexRangeValue(NumericRange indexRange, DataValue value, TypeTable typeTable) {
		// opc.sdk.core.utils.NumericRange indexRange =
		// (opc.sdk.core.utils.NumericRange) indexR;
		// DataValue updatedValue = new DataValue(value.getValue(),
		// value.getStatusCode(), value.getSourceTimestamp(),
		// value.getServerTimestamp());
		// no index range
		if (indexRange == null || indexRange.isEmpty()) {
			return;
		}
		if (value == null || value.isNull() || value.getValue().getValue() == null && indexRange != null
				&& !NumericRange.getEmpty().equals(indexRange)) {
			if (value != null)
				value.setStatusCode(StatusCodes.Bad_IndexRangeNoData);
			return;
		}
		TypeInfo typeInfo = TypeInfo.construct(value, typeTable);
		// check for string or subset of strings (valid scalars to write an
		// index)
		if (typeInfo.getValueRank() == ValueRanks.Scalar.getValue()) {
			// check for subset of string
			if (typeInfo.getBuiltInsType() == BuiltinType.String) {
				// string to index range
				String srcString = (String) value.getValue().getValue();
				if ((srcString == null || srcString.isEmpty()) || srcString.length() < indexRange.count()) {
					value.setStatusCode(StatusCodes.Bad_IndexRangeInvalid);
					return;
				}
				if (indexRange.getBegin() >= srcString.length()
						|| (indexRange.getEnd() > 0 && indexRange.getEnd() >= srcString.length())) {
					value.setStatusCode((StatusCodes.Bad_IndexRangeNoData));
					return;
				}
				int count = indexRange.count();
				String dstValue = "";
				for (int i = 0; i < count; i++) {
					dstValue += String.valueOf(srcString.charAt(indexRange.getBegin() + i));
					// dstString[indexRange.getBegin() + i] =
					// srcString.charAt(i);
				}
				value.setValue(new Variant(dstValue));
				// value.setStatusCode(StatusCode.GOOD);
				return;
			}
			// update elements within a bytestring
			else if (typeInfo.getBuiltInsType() == BuiltinType.ByteString) {
				byte[] srcString = (byte[]) value.getValue().getValue();
				if (srcString == null || srcString.length < indexRange.count()) {
					value.setStatusCode(StatusCodes.Bad_IndexRangeInvalid);
					return;
				}
				if (indexRange.getBegin() >= srcString.length
						|| ((indexRange.getEnd() > 0 && indexRange.getEnd() >= srcString.length))) {
					value.setStatusCode(StatusCodes.Bad_IndexRangeNoData);
					return;
				}
				int count = indexRange.count();
				byte[] dstValue = new byte[count];
				for (int i = 0; i < count; i++) {
					dstValue[i] = srcString[indexRange.getBegin() + i];
				}
				value.setValue(new Variant(dstValue));
				return;
			}
			// valid indexrange for scalar value
			return;
		}
		// check invalid array target
		Object srcArray = value.getValue().getValue();
		Object dstArray = value.getValue().getValue();
		int srcDim = value.getValue().getDimension();
		// handle only matrix (> 0)
		if (srcDim <= 0) {
			value.setStatusCode(StatusCodes.Bad_IndexRangeInvalid);
			return;
			// indexRange.getSubranges();
			//
			// int dims = MultiDimensionArrayUtils.getDimension(value.getValue()
			// .getValue());
			// if (dims != srcDim) {
			// return new StatusCode(StatusCodes.Bad_IndexRangeInvalid);
			// }
		}
		// TypeInfo srcTypeInfo = TypeInfo.construct(value, typeTable);
		// if (typeInfo.getBuiltInsType() != BuiltinType.Null &&
		// typeInfo.getBuiltInsType() != BuiltinType.Variant) {
		// value.setStatusCode(StatusCodes.Bad_IndexRangeInvalid);
		// return;
		// }
		// check value ranks is dimension
		if (typeInfo.getValueRank() < 0) {
			value.setStatusCode(StatusCodes.Bad_IndexRangeInvalid);
			return;
		}
		// handle one dimension
		if (srcDim == 1) {
			int count = indexRange.count();
			if (typeInfo.getValueRank() > 1) {
				value.setStatusCode(StatusCodes.Bad_IndexRangeInvalid);
				return;
			}
			if (((Object[]) srcArray).length < indexRange.count()) {
				value.setStatusCode(StatusCodes.Bad_IndexRangeInvalid);
				return;
			}
			if (indexRange.getBegin() >= ((Object[]) dstArray).length
					|| ((indexRange.getEnd() > 0 && indexRange.getEnd() >= ((Object[]) dstArray).length))) {
				value.setStatusCode(StatusCodes.Bad_IndexRangeNoData);
				return;
			}
			Class<? extends Object> arrayClass = value.getValue().getCompositeClass();
			Object destination = Array.newInstance(arrayClass, count);
			for (int i = 0; i < count; i++) {
				Object indexvalue = ((Object[]) srcArray)[indexRange.getBegin() + i];
				// TODO: same reference
				// ((Object[]) dstArray)[i] = indexvalue;
				((Object[]) destination)[i] = indexvalue;
			}
			// value.setValue(new Variant(dstArray));
			value.setValue(new Variant(destination));
			return;
		}
		NumericRange finalRange = null;
		// check for matching dimensions
		if (indexRange.getSubranges() != null && indexRange.getSubranges().length > typeInfo.getValueRank()) {
			if (typeInfo.getBuiltInsType() == BuiltinType.ByteString
					|| typeInfo.getBuiltInsType() == BuiltinType.String) {
				if (indexRange.getSubranges().length == typeInfo.getValueRank() + 1) {
					finalRange = indexRange.getSubranges()[indexRange.getSubranges().length - 1];
				}
			}
			if (finalRange == null) {
				value.setStatusCode(StatusCodes.Bad_IndexRangeNoData);
				return;
			}
		}
		// getting the arrays being copied
		int srcCount = 1;
		int[] dimensions = new int[typeInfo.getValueRank()];
		for (int i = 0; i < dimensions.length; i++) {
			if (indexRange.getSubranges().length < i) {
				if (indexRange.getSubranges()[i].count() != MultiDimensionArrayUtils
						.getLength(value.getValue().getArrayDimensions())) {
					value.setStatusCode(StatusCodes.Bad_IndexRangeInvalid);
					return;
				}
			}
			dimensions[i] = MultiDimensionArrayUtils.getLength(value.getValue().getArrayDimensions());
			srcCount *= dimensions[i];
		}
		// check that the index range fails with the target array
		int[] dstIndexes = new int[dimensions.length];
		for (int i = 0; i < srcCount; i++) {
			int divisor = srcCount;
			for (int j = 0; j < dimensions.length; j++) {
				divisor /= dimensions[i];
				int index = (i / divisor) % dimensions[j];
				int start = 0;
				if (indexRange.getSubranges().length > j) {
					start = indexRange.getSubranges()[j].getBegin();
				}
				if (start + index >= MultiDimensionArrayUtils.getLength(value.getValue().getArrayDimensions())) {
					value.setStatusCode(StatusCodes.Bad_IndexRangeNoData);
					return;
				}
				dstIndexes[j] = start + index;
			}
			if (finalRange == null) {
				continue;
			}
			int last = finalRange.getBegin();
			if (finalRange.getEnd() > 0) {
				last = finalRange.getEnd();
			}
			// set element
			Object element = null;
			// subset of string
			if (typeInfo.getBuiltInsType() == BuiltinType.String) {
				String str = (String) element;
				if (str == null || last >= str.length()) {
					value.setStatusCode(StatusCodes.Bad_IndexRangeNoData);
					return;
				}
			}
			// subset of bytestring
			else if (typeInfo.getBuiltInsType() == BuiltinType.ByteString) {
				byte[] str = (byte[]) element;
				if (str == null || last >= str.length) {
					value.setStatusCode(StatusCodes.Bad_IndexRangeNoData);
					return;
				}
			}
		}
		// copy data
		int[] srcIndexes = new int[dimensions.length];
		for (int i = 0; i < srcCount; i++) {
			int divisor = srcCount;
			for (int j = 0; j < dimensions.length; j++) {
				divisor /= dimensions[j];
				int index = (i / divisor) & dimensions[j];
				int start = 0;
				if (indexRange.getSubranges().length > j) {
					start = indexRange.getSubranges()[j].getBegin();
				}
				if (start + index >= MultiDimensionArrayUtils.getArrayLengths(dstArray)[j]) {
					value.setStatusCode(StatusCodes.Bad_IndexRangeNoData);
					return;
				}
				srcIndexes[j] = index;
				dstIndexes[j] = start + index;
			}
			// get the element to copy
			Object element1 = MultiDimensionArrayUtils.muxArray(srcArray, srcIndexes);
			if (finalRange == null) {
				// setvalue on dstarray with element 1 on dstindexes
				continue;
			}
			Object element2 = MultiDimensionArrayUtils.muxArray(dstArray, dstIndexes);
			// update elements within a string
			if (typeInfo.getBuiltInsType() == BuiltinType.String) {
				String srcString = (String) element1;
				char[] dstString = ((String) element2).toCharArray();
				if (srcString != null) {
					for (int j = 0; j < srcString.length(); j++) {
						dstString[finalRange.getBegin() + j] = srcString.charAt(j);
					}
				}
				// dstArray.setValue(new String(dstString), dstIndexes);
			}
			// update elemens within a byte string
			else if (typeInfo.getBuiltInsType() == BuiltinType.ByteString) {
				byte[] srcString = (byte[]) element1;
				byte[] dstString = (byte[]) element2;
				if (srcString != null) {
					for (int j = 0; j < srcString.length; j++) {
						dstString[finalRange.getBegin() + j] = srcString[j];
					}
				}
			}
		}
		value.setValue(new Variant(dstArray));
		return;
	}

	@Override
	public boolean start() {
		this.task = new PublishingTimerTask();
		this.server.scheduleTask(this.task, 0, (long) this.configuration.getPublishingResolution());
		this.hasInitialized = true;
		return this.hasInitialized;
	}

	@Override
	public boolean stop() {
		/** runnable is closed by ExecutorService */
		this.hasInitialized = false;
		return this.hasInitialized;
	}

	/**
	 * Task to publish subscriptions on the server
	 * 
	 * @author Thomas Z&ouml;chbauer
	 */
	class PublishingTimerTask implements Runnable {
		@Override
		public void run() {
			// do {
			// try {
			// if (OPCSubscriptionManager.this.lock.tryLock(
			// LOCK_TIME_2_WAIT, TimeUnit.MILLISECONDS)) {
			synchronized (OPCSubscriptionManager.this.lock) {
				long nanoStart = System.nanoTime();
				publishSubscriptions();
				long nanoEnd = System.nanoTime();
				long delay = (OPCSubscriptionManager.this.configuration.getPublishingResolution() * 1000000)
						- (nanoEnd - nanoStart);
				if (delay < 0) {
					try {
						Thread.sleep(OPCSubscriptionManager.this.configuration.getPublishingResolution());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void subscriptionExpired(OPCSubscription subscription) {
		synchronized (this.statusMessages) {
			StatusMessage message = new StatusMessage();
			message.setSubscriptionId(subscription.getSubscriptionId());
			message.setMessage(subscription.publishTimeout());
			this.statusMessages.addLast(message);
		}
	}

	final long LOCK_TIME_2_WAIT = 50;

	/**
	 * Updates referenced monitored items after a write service!
	 * 
	 * @param node
	 * @param attributeId
	 */
	protected void updateMonitoredItems(IMonitoredItem[] monitoredItems, UnsignedInteger attributeId,
			DataValue value/* ,DataValue lastValue */) {
		// find monitored items watching opc ua node
		if (monitoredItems == null) {
			return;
		}
		synchronized (this.lock) {
			// update if needed
			for (IMonitoredItem item : monitoredItems) {
				MonitoredItemTypeMask mask = item.getTypeMask();
				UnsignedInteger itemAttrId = item.getAttributeId();
				switch (mask) {
				// events
				case Events:
				case AllEvents:
					// TODO:
					break;
				// data change
				default:
					// write attribute id is matching with data
					// change item
					// attribute id
					if (itemAttrId.equals(attributeId)) {
						// reads current value in node
						// validate index range
						NumericRange indexRange = item.getIndexRange();
						DataValue dvValue = new DataValue(value.getValue(), value.getStatusCode(),
								value.getSourceTimestamp(), value.getServerTimestamp());
						updateIndexRangeValue(indexRange, dvValue, server.getTypeTable());
						DataValue lastValue = null;
						if (((OPCMonitoredItem) item).getQueue() != null) {
							lastValue = ((OPCMonitoredItem) item).getQueue().getLastValue();
						}
						item.queueValueChange(dvValue, lastValue);
					}
					break;
				}
			}
		}
	}

	public void reportEvent(IMonitoredItem[] monitoredItems, Node node, BaseEventType event) {
		// find monitored items watching opc ua node
		if (monitoredItems == null) {
			return;
		}
		synchronized (this.lock) {
			// update if needed
			for (IMonitoredItem item : monitoredItems) {
				MonitoredItemTypeMask mask = item.getTypeMask();
				// UnsignedInteger itemAttrId = item.getAttributeId();
				switch (mask) {
				// events
				case Events:
				case AllEvents:
					item.queueEvent(event);
					break;
				// data change
				default:
					// write attribute id is matching with data
					// change item
					// attribute id
					// if (itemAttrId.equals(attributeId)) {
					// // reads current value in node
					// // validate index range
					// NumericRange indexRange = item.getIndexRange();
					// updateIndexRangeValue(indexRange, value,
					// server.getTypeTable());
					// item.queueValueChange(value, lastValue);
					// }
					break;
				}
			}
		}
	}

	protected void setUserAuthorityManager(OPCUserAuthentificationManager userAuthentifiationManager) {
		this.userAuthentification = userAuthentifiationManager;
	}

	@Override
	public OPCInternalServer getServer() {
		return this.server;
	}

	boolean hasInitialized = false;

	@Override
	public boolean isInitialized() {
		return this.hasInitialized;
	}
}
