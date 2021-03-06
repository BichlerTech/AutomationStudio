package opc.sdk.server.service.subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.ServiceResult;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.DataChangeNotification;
import org.opcfoundation.ua.core.EventFieldList;
import org.opcfoundation.ua.core.EventNotificationList;
import org.opcfoundation.ua.core.MonitoredItemModifyRequest;
import org.opcfoundation.ua.core.MonitoredItemModifyResult;
import org.opcfoundation.ua.core.MonitoredItemNotification;
import org.opcfoundation.ua.core.MonitoringMode;
import org.opcfoundation.ua.core.MonitoringParameters;
import org.opcfoundation.ua.core.NotificationMessage;
import org.opcfoundation.ua.core.ReadValueId;
import org.opcfoundation.ua.core.StatusChangeNotification;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.core.TimestampsToReturn;
import org.opcfoundation.ua.encoding.EncoderContext;
import org.opcfoundation.ua.encoding.EncodingException;
import org.opcfoundation.ua.transport.tcp.io.SequenceNumber;

import com.bichler.opc.comdrv.ComDRVManager;

import opc.sdk.core.enums.EventNotifiers;
import opc.sdk.core.enums.MonitoredItemTypeMask;
import opc.sdk.core.enums.RequestType;
import opc.sdk.core.types.TypeTable;
import opc.sdk.server.core.OPCInternalServer;
import opc.sdk.server.core.managers.OPCValidationFramework;
import opc.sdk.server.service.session.OPCServerSession;

public class OPCSubscription {
	/** internal */
	private Object lock = new Object();
	private OPCServerSession session;
	/** monitored item sequence numbers */
	private SequenceNumber itemSeqNr = new SequenceNumber();
	/** notification message sequence number */
	private SequenceNumber notificationSequenceNumber = new SequenceNumber();
	/** subscription state to publish */
	private boolean waitingForPublish = false;
	/** timeout flag */
	private boolean expired = false;
	/** opc subscription defined parameters */
	private UnsignedInteger maxKeepAliveCount;
	private UnsignedInteger maxLifetimeCount;
	private UnsignedInteger maxNotificationsPerPublish;
	private UnsignedByte priority;
	private Boolean publishingEnabled;
	/** nano seconds */
	private Double publishingInterval;
	/** nano seconds */
	private long publishTimerExpiry;
	private int subscriptionId;
	private int maxMessageCount;
	private int keepAliveCounter;
	private int lifetimeCounter;
	private UnsignedInteger maxItemQueueSize;
	/** monitored items stored in the subscription */
	private LinkedList<OPCMonitoredItem> items2check;
	private List<OPCMonitoredItem> items2publish;
	private Map<Long, OPCMonitoredItem> monitoredItems;
	private Map<Long, List<ITriggeredMonitorItem>> items2trigger = new HashMap<>();
	/** messages */
	private LinkedList<NotificationMessage> messages2send;
	/** index of last sent message */
	private int lastSentMessage;
	private boolean init = false;

	public OPCSubscription(OPCServerSession session, int subscriptionId, Double publishingInterval,
			UnsignedInteger maxLifetimeCount, UnsignedInteger maxKeepAliveCount,
			UnsignedInteger maxNotificationsPerPublish, UnsignedByte priority, Boolean publishingEnabled,
			int maxMessageCount, UnsignedInteger maxNotificationQueueSize) {
		this.session = session;
		this.keepAliveCounter = 0;
		this.lifetimeCounter = 0;
		this.maxKeepAliveCount = maxKeepAliveCount;
		this.maxLifetimeCount = maxLifetimeCount;
		this.maxNotificationsPerPublish = maxNotificationsPerPublish;
		if (priority == null) {
			priority = UnsignedByte.ZERO;
		}
		this.priority = priority;
		this.publishingEnabled = publishingEnabled;
		this.publishingInterval = publishingInterval;
		this.publishTimerExpiry = System.nanoTime() + (publishingInterval.longValue() * 1000000);
		this.messages2send = new LinkedList<>();
		this.subscriptionId = subscriptionId;
		this.maxMessageCount = maxMessageCount;
		this.items2check = new LinkedList<>();
		this.items2publish = new ArrayList<>();
		this.monitoredItems = new HashMap<>();
		this.maxItemQueueSize = maxNotificationQueueSize;
	}

	public void dispose() {
		synchronized (this.lock) {
			Set<Long> keys = this.monitoredItems.keySet();
			UnsignedInteger[] itemIds = new UnsignedInteger[keys.size()];
			int index = 0;
			for (Long key : keys) {
				itemIds[index] = new UnsignedInteger(key);
				index++;
			}
			deleteMonitoredItems(itemIds);
			this.messages2send.clear();
		}
	}

	public StatusCode addMonitoredItem(OPCMonitoredItem item, OPCInternalServer server) {
		// resets subscription lifetime count
		synchronized (this.lock) {
			resetLifetimeCount();
			// next monitor item identifier
			long monitoredItemId = (long) this.itemSeqNr.getCurrentSendSequenceNumber();
			this.itemSeqNr.getNextSendSequencenumber();
			// recalculate monitor item create parameters
			UnsignedInteger revisedQueueSize = item.calculateQueueSize(item.getQueueSize(), this.maxItemQueueSize);
			Double samplingInterval = item.calculateSamplingInterval(this.publishingInterval);
			item.setMonitoredItemId(monitoredItemId);
			item.setQueueSize(revisedQueueSize, item.getDiscardOldest());
			item.setSamplingInterval(samplingInterval);
			// node must support the attribute
			UnsignedInteger attributeId = item.getAttributeId();
			if (!item.getNode().supportsAttribute(attributeId)) {
				return new StatusCode(StatusCodes.Bad_AttributeIdInvalid);
			}
			// init Event or Datachange item
			MonitoredItemTypeMask mask = item.getTypeMask();
			switch (mask) {
			case AllEvents:
			case Events:
				// evaluates if the opc ua node is able to subscribe to events
				try {
					Variant value = item.getNode().read(Attributes.EventNotifier, null);
					EnumSet<EventNotifiers> eventNotifiers = EventNotifiers.getSet(value.byteValue());
					if (!eventNotifiers.contains(EventNotifiers.SubscribeToEvents)) {
						return new StatusCode(StatusCodes.Bad_AttributeIdInvalid);
					}
					// register event to driver framework
				} catch (ClassCastException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
					return new StatusCode(StatusCodes.Bad_UnexpectedError);
				}
				// initialize event
				// TODO: EVENT
				break;
			case DataChange:
				// initialize data change
				ServiceResult error = OPCValidationFramework.validateFilter(item.getAttributeId(), item.getNode(),
						item.getFilter(), server.getTypeTable());
				if (error != null) {
					return error.getCode();
				}
				// read value
				try {
					ReadValueId readValue = new ReadValueId();
					readValue.setAttributeId(item.getAttributeId());
					readValue.setDataEncoding(item.getDataEncoding());
					readValue.setIndexRange(item.getIndexRangeAsString());
					readValue.setNodeId(item.getNode().getNodeId());
					DataValue[] values = server.getMaster().read(new ReadValueId[] { readValue }, 0.0,
							item.getTimestampsToReturn(), null, null);
					if (values != null && values.length > 0
							&& !values[0].getStatusCode().getValue().equals(StatusCodes.Bad_WaitingForInitialData)) {
						// initialize queue
						item.queueValueChange(values[0], null, true);
					}
				} catch (ServiceResultException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
				}
				break;
			default:
				break;
			}
			// add monitored item to subscription
			this.monitoredItems.put(monitoredItemId, item);
			this.items2check.add(item);
			// add monitored item to node
			item.register();
			return StatusCode.GOOD;
		}
	}

	public StatusCode[] deleteMonitoredItems(UnsignedInteger[] monitoredItemIds) {
		synchronized (this.lock) {
			resetLifetimeCount();
			StatusCode[] results = new StatusCode[monitoredItemIds.length];
			for (int i = 0; i < monitoredItemIds.length; i++) {
				OPCMonitoredItem item2remove = null;
				if ((item2remove = this.monitoredItems.get(monitoredItemIds[i].longValue())) == null) {
					results[i] = new StatusCode(StatusCodes.Bad_MonitoredItemIdInvalid);
					continue;
				}
				// unregisters from node
				item2remove.unRegister();
				// delete from subscription
				this.monitoredItems.remove(monitoredItemIds[i].longValue());
				this.items2publish.remove(item2remove);
				// this.items2publishtrigger.remove(item2remove);
				this.items2check.remove(item2remove);
				// remove trigger item
				this.items2trigger.remove(monitoredItemIds[i].longValue());
				// remove triggering links
				for (List<ITriggeredMonitorItem> items : this.items2trigger.values()) {
					List<ITriggeredMonitorItem> items2remove = new ArrayList<>();
					for (ITriggeredMonitorItem item : items) {
						if (item.getItemId().longValue() == item2remove.getMonitoredItemId()) {
							items2remove.add(item);
						}
					}
					items.removeAll(items2remove);
				}
				MonitoredItemTypeMask type = item2remove.getTypeMask();
				switch (type) {
				case DataChange:
					// unregister from driver
					ComDRVManager.getDRVManager().unregisterNotification(item2remove.getNode().getNodeId());
					break;
				case AllEvents:
				case Events:
					// unregister from driver
					ComDRVManager.getDRVManager().unregisterEvent(item2remove.getNode().getNodeId(), session);
					break;
				case Undefined:
					break;
				}
				results[i] = StatusCode.GOOD;
			}
			this.session.getServer().logService(RequestType.DeleteMonitoredItems,
					" MonitoredItems " + Arrays.toString(monitoredItemIds) + " with result " + Arrays.toString(results)
							+ "removed from subscription " + getSubscriptionId());
			return results;
		}
	}

	public MonitoredItemModifyResult[] modifyMonitoredItems(MonitoredItemModifyRequest[] itemsToModify,
			TimestampsToReturn timestampsToReturn, TypeTable typeTable) {
		synchronized (this.lock) {
			// resets subscription lifetime count
			resetLifetimeCount();
			MonitoredItemModifyResult[] results = new MonitoredItemModifyResult[itemsToModify.length];
			for (int i = 0; i < itemsToModify.length; i++) {
				MonitoredItemModifyRequest itemToModify = itemsToModify[i];
				MonitoredItemModifyResult result = new MonitoredItemModifyResult();
				results[i] = result;
				if (itemToModify.getMonitoredItemId() == null) {
					result.setStatusCode(new StatusCode(StatusCodes.Bad_MonitoredItemIdInvalid));
					continue;
				}
				OPCMonitoredItem monitoredItem = null;
				if ((monitoredItem = this.monitoredItems.get(itemToModify.getMonitoredItemId().longValue())) == null) {
					result.setStatusCode(new StatusCode(StatusCodes.Bad_MonitoredItemIdInvalid));
					continue;
				}
				MonitoringParameters params = itemToModify.getRequestedParameters();
				monitoredItem.modifyMonitoredItem(params, timestampsToReturn, result, typeTable, this.maxItemQueueSize);
				/**
				 * call right function of driver framework
				 */
				// register on driver framework
				switch (monitoredItem.getTypeMask()) {
				case DataChange:
					ComDRVManager.getDRVManager().changeNotification(monitoredItem.getNode(), itemToModify);
					break;
				case Events:
				case AllEvents:
					ComDRVManager.getDRVManager().changeEvent(monitoredItem.getNode(), itemToModify);
					break;
				case Undefined:
					// Non-Attribute Values for Non-Variables
					break;
				}
			}
			this.session.getServer().logService(RequestType.ModifyMonitoredItems,
					"Modified monitored items with result " + Arrays.toString(results));
			return results;
		}
	}

	public void modifySubscription(Double revisedPublishingInterval, UnsignedInteger revisedLifetimeCount,
			UnsignedInteger revisedMaxKeepAliveCounter, UnsignedInteger revisedMaxNotificationsPerPublish,
			UnsignedByte priority) {
		synchronized (this.lock) {
			resetLifetimeCount();
			/** modifies the lifetime count */
			updateLifeTimeCount(revisedLifetimeCount);
			/** modifies the publishing interval */
			updatePublishingInterval(revisedPublishingInterval);
			/** modifies the max keep alive counter */
			updateMaxKeepAliveCount(revisedMaxKeepAliveCounter);
			/** modifies the max notification per publish */
			updateMaxNotificationPerPublish(revisedMaxNotificationsPerPublish);
			/** modifies the priority */
			updatePriority(priority);
		}
		this.session.getServer().logService(RequestType.ModifySubscription,
				"Subscription " + getSubscriptionId() + " is modified");
	}

	public NotificationMessage republish(UnsignedInteger retransmitSequenceNumber) throws ServiceResultException {
		// SubscriptionDiagnostics[] sarray1 = {
		// SubscriptionDiagnostics.RepublishMessageRequestCount };
		// Object[] varray1 = {
		// this.diagnostics.getRepublishMessageRequestCount()
		// .inc() };
		// this.server.getDiagnosticsNodeManager()
		// .updateSubscriptionDiagnosticsArray(this.session,
		// this.subscriptionId, sarray1, varray1);
		synchronized (this.lock) {
			/** subscription diagnostics */
			// SubscriptionDiagnostics[] sarray2 = {
			// SubscriptionDiagnostics.RepublishMessageRequestCount,
			// SubscriptionDiagnostics.RepublishRequestCount };
			// Object[] varray2 = {
			// this.diagnostics.getRepublishMessageRequestCount().inc(),
			// this.diagnostics.getRepublishRequestCount().inc() };
			// this.server.getDiagnosticsNodeManager()
			// .updateSubscriptionDiagnosticsArray(this.session,
			// this.subscriptionId, sarray2, varray2);
			resetLifetimeCount();
			NotificationMessage message2republish = null;
			// find message
			for (NotificationMessage message : this.messages2send) {
				if (message.getSequenceNumber().compareTo(retransmitSequenceNumber) == 0) {
					/** subscription diagnostics */
					// SubscriptionDiagnostics[] categories = {
					// SubscriptionDiagnostics.RepublishMessageCount };
					// Object[] values = { this.diagnostics
					// .getRepublishMessageCount().inc() };
					// this.server.getDiagnosticsNodeManager()
					// .updateSubscriptionDiagnosticsArray(this.session,
					// this.subscriptionId, categories, values);
					message2republish = message;
					break;
				}
			}
			if (message2republish == null) {
				throw new ServiceResultException(new StatusCode(StatusCodes.Bad_MessageNotAvailable));
			}
			session.getServer().logService(RequestType.Republish,
					"Notificationmessage " + message2republish.getSequenceNumber() + " "
							+ message2republish.getPublishTime() + " is retransmitted");
			return message2republish;
		}
	}

	/**
	 * Set monitoring mode of monitored items in this subscription.
	 * 
	 * @param monitoredItemIds
	 * @param monitoringMode
	 * @return
	 */
	public StatusCode[] setMonitoringMode(UnsignedInteger[] monitoredItemIds, MonitoringMode monitoringMode) {
		synchronized (this.lock) {
			StatusCode[] results = new StatusCode[monitoredItemIds.length];
			for (int i = 0; i < monitoredItemIds.length; i++) {
				OPCMonitoredItem monitoredItem = null;
				if ((monitoredItem = this.monitoredItems.get(monitoredItemIds[i].longValue())) == null) {
					results[i] = new StatusCode(StatusCodes.Bad_MonitoredItemIdInvalid);
					continue;
				}
				StatusCode error = monitoredItem.setMonitoringMode(monitoringMode);
				results[i] = error;
			}
			session.getServer().logService(RequestType.SetMonitoringMode,
					"Subscription " + getSubscriptionId() + " set monitoring mode on items "
							+ Arrays.toString(monitoredItemIds) + " with result " + Arrays.toString(results));
			return results;
		}
	}

	public StatusCode setPublishingMode(Boolean publishingEnabled) {
		synchronized (this.lock) {
			resetLifetimeCount();
			if (publishingEnabled != this.publishingEnabled) {
				this.publishingEnabled = publishingEnabled;
				/** subscription diagnostics */
				// SubscriptionDiagnostics[] categories = new
				// SubscriptionDiagnostics[2];
				// Object[] values = new Object[2];
				// categories[0] = SubscriptionDiagnostics.PublishingEnabled;
				// values[0] = this.publishingEnabled;
				//
				// if (this.publishingEnabled) {
				// categories[1] = SubscriptionDiagnostics.EnableCount;
				// values[1] = this.diagnostics.getEnableCount().inc();
				// } else {
				// categories[1] = SubscriptionDiagnostics.DisableCount;
				// values[1] = this.publishingEnabled;
				// values[1] = this.diagnostics.getDisableCount().inc();
				// }
				// this.server.getDiagnosticsNodeManager()
				// .updateSubscriptionDiagnosticsArray(this.session,
				// this.subscriptionId, categories, values);
			}
			this.session.getServer().logService(RequestType.SetPublishingMode,
					"Subscription " + getSubscriptionId() + " set publishmode to " + publishingEnabled);
			return StatusCode.GOOD;
		}
	}

	public void setTriggering(OPCServerSession session, UnsignedInteger triggeringItemId, UnsignedInteger[] linksToAdd,
			UnsignedInteger[] linksToRemove, List<StatusCode> resultAdd, List<StatusCode> resultRemove)
			throws ServiceResultException {
		if (linksToAdd == null) {
			throw new ServiceResultException(StatusCodes.Bad_InvalidArgument);
		}
		if (linksToRemove == null) {
			throw new ServiceResultException(StatusCodes.Bad_InvalidArgument);
		}
		if (linksToAdd.length == 0 && linksToRemove.length == 0) {
			throw new ServiceResultException(StatusCodes.Bad_NothingToDo);
		}
		synchronized (this.lock) {
			resetLifetimeCount();
			// find triggering item
			OPCMonitoredItem trigger = this.monitoredItems.get(triggeringItemId.longValue());
			if (trigger == null) {
				throw new ServiceResultException(StatusCodes.Bad_MonitoredItemIdInvalid);
			}
			// look up existing triggering items
			List<ITriggeredMonitorItem> triggerItems = this.items2trigger.get(triggeringItemId.longValue());
			if (triggerItems == null) {
				triggerItems = new ArrayList<ITriggeredMonitorItem>();
				this.items2trigger.put(triggeringItemId.longValue(), triggerItems);
			}
			// remove old links
			for (int i = 0; i < linksToRemove.length; i++) {
				resultRemove.add(StatusCode.GOOD);
				boolean found = false;
				for (int j = 0; j < triggerItems.size(); j++) {
					if (triggerItems.get(j).getItemId().longValue() == linksToRemove[i].longValue()) {
						found = true;
						triggerItems.remove(j);
						break;
					}
				}
				if (!found) {
					resultRemove.set(i, new StatusCode(StatusCodes.Bad_MonitoredItemIdInvalid));
					// TODO: Diagnostics
					continue;
				}
			}
			for (int i = 0; i < linksToAdd.length; i++) {
				resultAdd.add(StatusCode.GOOD);
				OPCMonitoredItem link = this.monitoredItems.get(linksToAdd[i].longValue());
				if (link == null) {
					resultAdd.set(i, new StatusCode(StatusCodes.Bad_MonitoredItemIdInvalid));
					// TODO: diagnostics
					continue;
				}
				if (!(link instanceof ITriggeredMonitorItem)) {
					resultAdd.set(i, new StatusCode(StatusCodes.Bad_NotSupported));
					// TODO: diagnostics
					continue;
				}
				boolean found = false;
				for (int j = 0; j < triggerItems.size(); j++) {
					if (triggerItems.get(j).getItemId().longValue() == link.getMonitoredItemId()) {
						found = true;
						break;
					}
				}
				if (!found) {
					triggerItems.add((ITriggeredMonitorItem) link);
				}
				// TODO: update diagnostics
			}
		}
	}

	protected StatusCode acknowledge(UnsignedInteger messageNumber) {
		// System.out.println(this.subscriptionId + " Subscription acknowledge:
		// " + messageNumber);
		synchronized (this.lock) {
			// reset liftime counter
			resetLifetimeCount();
			// changed from sentmessage queue to acknowledge messages
			NotificationMessage message2acknowledge = null;
			for (int i = 0; i < this.messages2send.size(); i++) {
				NotificationMessage notification = this.messages2send.get(i);
				if (notification.getSequenceNumber().equals(messageNumber)) {
					if (this.lastSentMessage > i) {
						this.lastSentMessage--;
					}
					message2acknowledge = notification;
					break;
				}
			}
			// remove acknowledged message
			if (message2acknowledge != null) {
				this.messages2send.remove(message2acknowledge);
				return StatusCode.GOOD;
			}
			if (UnsignedInteger.ZERO.equals(messageNumber)) {
				return new StatusCode(StatusCodes.Bad_SequenceNumberUnknown);
				// return StatusCode.GOOD;
			}
			// message not found
			return new StatusCode(StatusCodes.Bad_SequenceNumberUnknown);
		}
	}

	protected NotificationMessage publish(OPCPublishOperation operation) {
		synchronized (this.lock) {
			NotificationMessage message = null;
			operation.setResponseMoreNotifications(false);
			// operation.setAvailableSequenceNumbers(null);
			if (this.expired) {
				return null;
			}
			try {
				// publish notification message from subscription
				message = innerPublish(operation);
			} finally {
				if (message != null) {
					resetKeepAliveCounter();
					this.waitingForPublish = operation.hasMoreNotifications();
					resetLifetimeCount();
				}
			}
			return message;
		}
	}

	/**
	 * Checks if the subscription is ready to publish, expired, waitingforpublish or
	 * idle. The boolean availableToPublish returns the result, if monitored items
	 * are able to publish datachanges.
	 * 
	 * @param Timestamp Timestamp to check {@link MonitoredItem} are ready to
	 *                  publish.
	 * 
	 * @return PublishingState of the subscription.
	 */
	protected PublishingState publishTimerExpired() {
		synchronized (this.lock) {
			long timestamp = System.nanoTime();
			// check if publish interval has elapsed
			if (timestamp - this.publishTimerExpiry <= 0) {
				// if (this.publishTimerExpiry >= timestamp.getTimeInMillis()) {
				// check if waiting for publish
				if (this.waitingForPublish) {
					return PublishingState.WaitingForPublish;
				}
				return PublishingState.Idle;
			}
			// set next expiry time
			while (timestamp - this.publishTimerExpiry > 0) {
				// this.publishTimerExpiry < timestamp.getTimeInMillis()) {
				this.publishTimerExpiry += (this.publishingInterval.longValue() * 1000000);
			}
			// check if lifetime has elapsed
			if (this.waitingForPublish) {
				this.lifetimeCounter++;
				/** subscription diagnostics */
				// SubscriptionDiagnostics[] categories = {
				// SubscriptionDiagnostics.LatePublishRequestCount,
				// SubscriptionDiagnostics.CurrentLifetimeCount };
				// Object[] values = {
				// this.diagnostics.getLatePublishRequestCount().inc(),
				// new UnsignedInteger(this.lifetimeCounter) };
				// this.server.getDiagnosticsNodeManager()
				// .updateSubscriptionDiagnosticsArray(this.session,
				// this.subscriptionId, categories, values);
				if (this.lifetimeCounter >= this.maxLifetimeCount.longValue()) {
					return PublishingState.Expired;
				}
			}
			// increment keep alive counter
			this.keepAliveCounter++;
			// subscription diagnostics
			// SubscriptionDiagnostics[] categories = {
			// SubscriptionDiagnostics.CurrentKeepAliveCount };
			// Object[] values = { new UnsignedInteger(this.keepAliveCounter) };
			// this.server.getDiagnosticsNodeManager()
			// .updateSubscriptionDiagnosticsArray(this.session,
			// this.subscriptionId, categories, values);
			if (this.publishingEnabled && this.session != null) {
				/** check for monitored items that are ready to publish */
				// boolean itemsTriggerd = false;
				List<OPCMonitoredItem> items2pickout = new ArrayList<>();
				// List<OPCMonitoredItem> items2trigger = new ArrayList<>();
				for (OPCMonitoredItem item : this.items2check) {
					if (!item.getReadyToPublish()) {
						continue;
					}
					// check if monitoring is disabled
					if (MonitoringMode.Disabled == item.getMonitoringMode()) {
						continue;
					}
					// if (items2trigger.contains(item)) {
					// continue;
					// }
					if (!item.getTriggered()) {
						if (item.getNextSamplingTime() - timestamp > 0) {
							continue;
						}
						// report item
						if (MonitoringMode.Reporting == item.getMonitoringMode()) {
							items2pickout.add(item);
						}
					}
					// update any trigger items
					if (item.getReadyToTrigger()) {
						/** update any triggered items */
						List<ITriggeredMonitorItem> triggeredItems = this.items2trigger.get(item.getMonitoredItemId());
						if (triggeredItems == null) {
							continue;
						}
						for (ITriggeredMonitorItem triggerdItem : triggeredItems) {
							if (triggerdItem.isTriggered()) {
								items2pickout.add((OPCMonitoredItem) triggerdItem);
								// items2trigger.add((OPCMonitoredItem)
								// triggerdItem);
								// itemsTriggerd = true;
							}
						}
						// trigger items are published
						item.setReadyToTrigger(false);
					}
				}
				// set monitoreditems to publish
				this.items2publish.addAll(items2pickout);
				// remove from waiting queue
				this.items2check.removeAll(items2pickout);
				items2pickout.clear();
				// include trigger items
				// if (itemsTriggerd) {
				// for (OPCMonitoredItem item : items2trigger) {
				//
				// // already added to publish
				// // if (this.items2publish.contains(item)) {
				// // continue;
				// // }
				//
				// this.items2publish.add(item);
				// }
				//
				// this.items2check.removeAll(items2trigger);
				// }
				// items are ready to publish
				if (this.items2publish.size() > 0) {
					this.waitingForPublish = true;
					return PublishingState.NotificationsAvailable;
				}
			}
			/** check if keep alive expired */
			if (this.keepAliveCounter >= this.maxKeepAliveCount.longValue()) {
				this.waitingForPublish = true;
				return PublishingState.NotificationsAvailable;
			}
			if (!this.init) {
				// init whatever
				this.init = true;
				if (this.monitoredItems.isEmpty()) {
					// System.out.println("init empty!");
					this.waitingForPublish = true;
					return PublishingState.NotificationsAvailable;
				}
				// else {
				// System.out.println("init monitoreditem!");
				// }
			} /** do nothing */
			return PublishingState.Idle;
		}
	}

	private int constructMessage(List<EventFieldList> events, List<MonitoredItemNotification> datachanges,
			NotificationMessage message) {
		List<ExtensionObject> data = new ArrayList<ExtensionObject>();
		int notificationCount = 0;
		int seqNr = this.notificationSequenceNumber.getCurrentSendSequenceNumber();
		this.notificationSequenceNumber.getNextSendSequencenumber();
		message.setSequenceNumber(new UnsignedInteger(seqNr));
		message.setPublishTime(DateTime.currentTime());
		/** subscription diagnostics */
		// SubscriptionDiagnostics[] categories = {
		// SubscriptionDiagnostics.NextSequenceNumber };
		// Object[] values = { new UnsignedInteger(
		// this.notificationSequenceNumber.getCurrentSendSequenceNumber()) };
		// this.server.getDiagnosticsNodeManager()
		// .updateSubscriptionDiagnosticsArray(this.session,
		// this.subscriptionId, categories, values);
		if (events.size() > 0 && notificationCount < this.maxNotificationsPerPublish.longValue()) {
			EventNotificationList notification = new EventNotificationList();
			ListIterator<EventFieldList> iterator = events.listIterator();
			List<EventFieldList> events2notify = new ArrayList<EventFieldList>(events.size());
			List<EventFieldList> events2remove = new ArrayList<EventFieldList>();
			while (iterator.hasNext() && notificationCount < this.maxNotificationsPerPublish.longValue()) {
				EventFieldList event = iterator.next();
				events2remove.add(event);
				events2notify.add(event);
				notificationCount++;
			}
			events.removeAll(events2remove);
			notification.setEvents(events2notify.toArray(new EventFieldList[0]));
			try {
				data.add(ExtensionObject.binaryEncode(notification, EncoderContext.getDefaultInstance()));
			} catch (EncodingException e) {
				// SHOULD NOT CALLED
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
				data.add(null);
			}
		}
		// add data changes
		if (datachanges.size() > 0 && notificationCount < this.maxNotificationsPerPublish.longValue()) {
			DataChangeNotification notification = new DataChangeNotification();
			// boolean diagnosticsExist = false;
			List<MonitoredItemNotification> dataChanges2notify = new ArrayList<MonitoredItemNotification>();
			// List<DiagnosticInfo> diagnostics2notify = new
			// ArrayList<DiagnosticInfo>();
			ListIterator<MonitoredItemNotification> iterator = datachanges.listIterator();
			// ListIterator<DiagnosticInfo> diagnosticsIterator =
			// datachangeDiagnostics
			// .listIterator();
			List<MonitoredItemNotification> datachanges2remove = new ArrayList<MonitoredItemNotification>();
			while (iterator.hasNext() && notificationCount < this.maxNotificationsPerPublish.longValue()) {
				MonitoredItemNotification datachange = iterator.next();
				datachanges2remove.add(datachange);
				dataChanges2notify.add(datachange);
				notificationCount++;
				// if (diagnosticsIterator.hasNext()) {
				// DiagnosticInfo diagnostic = diagnosticsIterator.next();
				// iterator.remove();
				// diagnostics2notify.add(diagnostic);
				// diagnosticsExist = true;
				// }
			}
			datachanges.removeAll(datachanges2remove);
			// if (!diagnosticsExist) {
			// diagnostics2notify.clear();
			// }
			// notification.setDiagnosticInfos(diagnostics2notify
			// .toArray(new DiagnosticInfo[0]));
			notification.setMonitoredItems(dataChanges2notify.toArray(new MonitoredItemNotification[0]));
			try {
				data.add(ExtensionObject.binaryEncode(notification, EncoderContext.getDefaultInstance()));
			} catch (EncodingException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
				data.add(null);
			}
		}
		message.setNotificationData(data.toArray(new ExtensionObject[data.size()]));
		return notificationCount;
	}

	/**
	 * Publishes the next notification message for the subscription. Weather the
	 * message is NEWLY created, or it is QUEUED and waiting to publish.
	 * 
	 * @param operation
	 * @return
	 */
	private NotificationMessage innerPublish(OPCPublishOperation operation) {
		// check if a keep alive should be sent if there is no data
		boolean keepAliveIfNoData = this.keepAliveCounter >= this.maxKeepAliveCount.longValue();
		boolean hasMoreNotifications = false;
		List<UnsignedInteger> availableSequenceNumbers = new ArrayList<>();
		// sends a waiting message
		if (this.lastSentMessage < this.messages2send.size()) {
			// return available sequence numbers
			for (int i = 0; i <= this.lastSentMessage && i < this.messages2send.size(); i++) {
				availableSequenceNumbers.add(this.messages2send.get(i).getSequenceNumber());
			}
			hasMoreNotifications = this.waitingForPublish = this.lastSentMessage < this.messages2send.size() - 1;
			// set operation response
			operation.setResponseMoreNotifications(hasMoreNotifications);
			operation.setResponseAvailableSequenceNumbers(availableSequenceNumbers.toArray(new UnsignedInteger[0]));
			return this.messages2send.get(this.lastSentMessage++);
		}
		List<NotificationMessage> messages = new ArrayList<>();
		if (this.publishingEnabled) {
			List<EventFieldList> events = new LinkedList<>();
			List<MonitoredItemNotification> datachanges = new LinkedList<>();
			List<OPCMonitoredItem> items2remove = new ArrayList<>();
			// publish monitored items
			for (OPCMonitoredItem item : this.items2publish) {
				switch (item.getTypeMask()) {
				// events
				case Events:
				case AllEvents:
					item.publishEvent(events);
					break;
				// datachange
				default:
					item.publishDataChange(datachanges);
					break;
				}
				items2remove.add(item);
				// if (!triggeredItem) {
				this.items2check.addLast(item);
				// }
				if (this.maxNotificationsPerPublish.longValue() > 0
						&& events.size() + datachanges.size() > this.maxNotificationsPerPublish.longValue()) {
					NotificationMessage message = new NotificationMessage();
					/** construct message */
					// int eventCount = events.size();
					// int datachangeCount = datachanges.size();
					// int notificationCount =
					constructMessage(events, datachanges, message);
					/** add to list of messages to send */
					messages.add(message);
					// SubscriptionDiagnostics[] categories = {
					// SubscriptionDiagnostics.DataChangeNotificagtionsCount,
					// SubscriptionDiagnostics.EventNotificationsCount,
					// SubscriptionDiagnostics.NotificationsCount };
					// Object[] values = {
					// new UnsignedInteger(datachangeCount),
					// new UnsignedInteger(eventCount),
					// this.diagnostics.getNotificationsCount().add(
					// notificationCount) };
					// this.server.getDiagnosticsNodeManager()
					// .updateSubscriptionDiagnosticsArray(this.session,
					// this.subscriptionId, categories, values);
				}
			}
			this.items2publish.removeAll(items2remove);
			// publish remaining notifications
			while (events.size() + datachanges.size() > 0) {
				// int eventCount = events.size();
				// int datachangeCount = datachanges.size();
				NotificationMessage message = new NotificationMessage();
				// int notificationCount =
				constructMessage(events, datachanges, message);
				/** add to list of messages to send */
				messages.add(message);
				/** subscription diagnostics */
				// SubscriptionDiagnostics[] categories = {
				// SubscriptionDiagnostics.DataChangeNotificagtionsCount,
				// SubscriptionDiagnostics.EventNotificationsCount,
				// SubscriptionDiagnostics.NotificationsCount };
				// Object[] values = {
				// new UnsignedInteger(datachangeCount),
				// new UnsignedInteger(eventCount),
				// this.diagnostics.getNotificationsCount().add(
				// notificationCount) };
				// this.server.getDiagnosticsNodeManager()
				// .updateSubscriptionDiagnosticsArray(this.session,
				// this.subscriptionId, categories, values);
			}
			// check for missing notifications
			if (!keepAliveIfNoData && messages.size() == 0) {
				this.waitingForPublish = false;
				return null;
			}
		}
		// create a keep alive message
		if (messages.size() == 0) {
			NotificationMessage message = new NotificationMessage();
			// use the sequence number for the next message
			int seqNr = this.notificationSequenceNumber.getCurrentSendSequenceNumber();
			message.setSequenceNumber(new UnsignedInteger(seqNr));
			message.setPublishTime(DateTime.currentTime());
			// return available sequence numbers
			for (int i = 0; i <= this.lastSentMessage && i < this.messages2send.size(); i++) {
				availableSequenceNumbers.add(this.messages2send.get(i).getSequenceNumber());
			}
			operation.setResponseAvailableSequenceNumbers(availableSequenceNumbers.toArray(new UnsignedInteger[0]));
			return message;
		}
		// drop unsent messages if out of queue size
		if (messages.size() > this.maxMessageCount) {
			messages.removeAll(messages.subList(0, messages.size() - this.maxMessageCount));
		}
		// remove old messages if queue is full
		if (this.messages2send.size() >= this.maxMessageCount - messages.size()) {
			/** subscription diagnostics */
			// SubscriptionDiagnostics[] categories = {
			// SubscriptionDiagnostics.UnacknowledgedMessageCount };
			// Object[] values = { this.diagnostics
			// .getUnacknowledgedMessageCount().add(messages.size()) };
			// this.server.getDiagnosticsNodeManager()
			// .updateSubscriptionDiagnosticsArray(this.session,
			// this.subscriptionId, categories, values);
			if (this.maxMessageCount <= messages.size()) {
				this.messages2send.clear();
			} else {
				messages.removeAll(messages.subList(0, messages.size()));
			}
		}
		// save new message
		this.lastSentMessage = this.messages2send.size();
		this.messages2send.addAll(messages);
		// / check if there are more notifications to send
		hasMoreNotifications = this.waitingForPublish = this.messages2send.size() > 1;
		operation.setResponseMoreNotifications(hasMoreNotifications);
		// return available sequence numbers
		for (int i = 0; i <= this.lastSentMessage && i < this.messages2send.size(); i++) {
			availableSequenceNumbers.add(this.messages2send.get(i).getSequenceNumber());
		}
		operation.setResponseAvailableSequenceNumbers(availableSequenceNumbers.toArray(new UnsignedInteger[0]));
		// HACK set last index
		try {
			return this.messages2send.get(this.lastSentMessage++);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	private void resetKeepAliveCounter() {
		// do nothing
		if (this.keepAliveCounter == 0) {
			return;
		}
		this.keepAliveCounter = 0;/** subscription diagnostics */
		// SubscriptionDiagnostics[] categories = {
		// SubscriptionDiagnostics.CurrentKeepAliveCount };
		// Object[] values = { UnsignedInteger.ZERO };
		// this.server.getDiagnosticsNodeManager()
		// .updateSubscriptionDiagnosticsArray(this.session,
		// this.subscriptionId, categories, values);
	}

	private void resetLifetimeCount() {
		if (this.lifetimeCounter == 0) {
			return;
		}
		this.lifetimeCounter = 0;/** subscription diagnostics */
		// SubscriptionDiagnostics[] categories = {
		// SubscriptionDiagnostics.CurrentLifetimeCount };
		// Object[] values = { UnsignedInteger.ZERO };
		// this.server.getDiagnosticsNodeManager()
		// .updateSubscriptionDiagnosticsArray(this.session,
		// this.subscriptionId, categories, values);
	}

	private void updateLifeTimeCount(UnsignedInteger maxLifetimeCount) {
		this.maxLifetimeCount = maxLifetimeCount;
	}

	private void updateMaxKeepAliveCount(UnsignedInteger maxKeepAliveCounter) {
		if (!maxKeepAliveCounter.equals(this.maxKeepAliveCount)) {
			this.maxKeepAliveCount = maxKeepAliveCounter;
		}
	}

	private void updateMaxNotificationPerPublish(UnsignedInteger maxNotificationsPerPublish) {
		this.maxNotificationsPerPublish = maxNotificationsPerPublish;
	}

	private void updatePriority(UnsignedByte priority) {
		if (priority == null) {
			priority = UnsignedByte.ZERO;
		}
		this.priority = priority;
	}

	private void updatePublishingInterval(Double publishingInterval) {
		// update publishing interval
		boolean hasToUpdate = publishingInterval != this.publishingInterval;
		if (hasToUpdate) {
			// nano
			this.publishingInterval = publishingInterval;
			this.publishTimerExpiry = System.nanoTime() + (this.publishingInterval.longValue() * 1000000);
		}
		if (hasToUpdate) {
			resetKeepAliveCounter();
		}
	}

	public UnsignedInteger getSubscriptionId() {
		return new UnsignedInteger(this.subscriptionId);
	}

	public UnsignedInteger getMaxLifeTimeCount() {
		return this.maxLifetimeCount;
	}

	public UnsignedInteger getMaxKeepAliveCount() {
		return this.maxKeepAliveCount;
	}

	public Double getPublishingInterval() {
		return this.publishingInterval;
	}

	public UnsignedByte getPriority() {
		return this.priority;
	}

	// public NotificationMessage publish(OPCPublishOperation operation) {
	// NotificationMessage message = null;
	// synchronized (this.lock) {
	// operation.setMoreNotifications(false);
	// operation.setAvailableSequenceNumbers(null);
	// if (this.expired) {
	// return null;
	// }
	//
	// try {
	// /** subscription diagnostics */
	// // SubscriptionDiagnostics[] categories1 = {
	// // SubscriptionDiagnostics.PublishRequestCount };
	// // Object[] values1 = {
	// // this.diagnostics.getPublishRequestCount()
	// // .inc() };
	// // this.server.getDiagnosticsNodeManager()
	// // .updateSubscriptionDiagnosticsArray(this.session,
	// // this.subscriptionId, categories1, values1);
	//
	// /** publish the subscription */
	// message = innerPublish(operation);
	// /** subscription diagnstics */
	// // SubscriptionDiagnostics[] categories2 = {
	// // SubscriptionDiagnostics.UnacknowledgedMessageCount };
	// //
	// // Integer v = 0;
	// // if (operation.getResponse().getAvailableSequenceNumbers() !=
	// // null) {
	// // v =
	// // operation.getResponse().getAvailableSequenceNumbers().length;
	// // }
	// //
	// // Object[] values2 = { v };
	// // this.server.getDiagnosticsNodeManager()
	// // .updateSubscriptionDiagnosticsArray(this.session,
	// // this.subscriptionId, categories2, values2);
	//
	// } finally {
	// if (message != null) {
	// resetKeepAliveCounter();
	// this.waitingForPublish = operation.hasMoreNotifications();
	// resetLifetimeCount();
	// }
	// }
	// }
	// return message;
	// }
	public NodeId getSessionId() {
		return this.session.getAuthentifikationToken();
	}

	public NotificationMessage publishTimeout() {
		NotificationMessage message = null;
		synchronized (this.lock) {
			this.expired = true;
			message = new NotificationMessage();
			int seqNr = this.notificationSequenceNumber.getCurrentSendSequenceNumber();
			this.notificationSequenceNumber.getNextSendSequencenumber();
			message.setSequenceNumber(new UnsignedInteger(seqNr));
			message.setPublishTime(DateTime.currentTime());
			// TODO: diagnostics
			StatusChangeNotification notification = new StatusChangeNotification();
			notification.setStatus(new StatusCode(StatusCodes.Bad_Timeout));
			try {
				ExtensionObject encoded = ExtensionObject.binaryEncode(notification,
						EncoderContext.getDefaultInstance());
				message.setNotificationData(new ExtensionObject[] { encoded });
			} catch (EncodingException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			}
		}
		return message;
	}
}
