package opc.client.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.ServiceResponse;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.ServiceFaultException;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.CreateMonitoredItemsRequest;
import org.opcfoundation.ua.core.CreateMonitoredItemsResponse;
import org.opcfoundation.ua.core.CreateSubscriptionRequest;
import org.opcfoundation.ua.core.CreateSubscriptionResponse;
import org.opcfoundation.ua.core.DeleteMonitoredItemsRequest;
import org.opcfoundation.ua.core.DeleteMonitoredItemsResponse;
import org.opcfoundation.ua.core.DeleteSubscriptionsRequest;
import org.opcfoundation.ua.core.DeleteSubscriptionsResponse;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.ModifyMonitoredItemsRequest;
import org.opcfoundation.ua.core.ModifyMonitoredItemsResponse;
import org.opcfoundation.ua.core.ModifySubscriptionRequest;
import org.opcfoundation.ua.core.ModifySubscriptionResponse;
import org.opcfoundation.ua.core.MonitoredItemCreateRequest;
import org.opcfoundation.ua.core.MonitoredItemCreateResult;
import org.opcfoundation.ua.core.MonitoredItemModifyRequest;
import org.opcfoundation.ua.core.MonitoredItemModifyResult;
import org.opcfoundation.ua.core.MonitoringParameters;
import org.opcfoundation.ua.core.ReadValueId;
import org.opcfoundation.ua.core.RepublishRequest;
import org.opcfoundation.ua.core.RepublishResponse;
import org.opcfoundation.ua.core.SetMonitoringModeRequest;
import org.opcfoundation.ua.core.SetMonitoringModeResponse;
import org.opcfoundation.ua.core.SetPublishingModeRequest;
import org.opcfoundation.ua.core.SetPublishingModeResponse;
import org.opcfoundation.ua.core.SetTriggeringRequest;
import org.opcfoundation.ua.core.SetTriggeringResponse;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.core.TransferResult;
import org.opcfoundation.ua.core.TransferSubscriptionsRequest;
import org.opcfoundation.ua.core.TransferSubscriptionsResponse;
import org.opcfoundation.ua.transport.AsyncResult;
import org.opcfoundation.ua.utils.MultiDimensionArrayUtils;

import opc.sdk.core.enums.RequestType;

/**
 * The subscription manager manages all subscriptions that has been created.
 *
 * @author Thomas Z&ouml;chbauer
 * @since 23.05.2012, HB-Softsolution e.U.
 */
public class SubscriptionManager {
	private Object lock = new Object();
	private Map<ClientSession, Map<Subscription, UnsignedInteger>> subscriptions = null;

	public SubscriptionManager() {
		this.subscriptions = new HashMap<>();
	}

	/**
	 * Getter of a {@link Subscription} from a Session and its Id.
	 *
	 * @param SessionId      SessionId to locate the Subscription.
	 * @param SubscriptionId Server-assigned Id to locate the Subscription on the
	 *                       Session.
	 * @return Subscription given by the parameters.
	 */
	public Subscription getSubscription(ClientSession session, UnsignedInteger subscriptionId) {
		synchronized (this.lock) {
			Subscription[] subscriptionsPersession = getSubscriptionsPerSession(session);
			return findSubscription(subscriptionId, subscriptionsPersession);
		}
	}

	/**
	 * Getter of all {@link Subscription} per Session
	 *
	 * @param SessionId SessionId to locate the Subscription.
	 * @return Map<SubscriptionId, Subscription> with all Subscriptions from the
	 *         given SessionId.
	 */
	public Subscription[] getSubscriptionsPerSession(ClientSession session) {
		if (session == null) {
			return new Subscription[0];
		}
		Map<Subscription, UnsignedInteger> tmpSubscriptions;
		synchronized (this.lock) {
			tmpSubscriptions = this.subscriptions.get(session);
		}
		if (tmpSubscriptions == null) {
			return new Subscription[0];
		}
		return tmpSubscriptions.keySet().toArray(new Subscription[tmpSubscriptions.size()]);
	}

	/**
	 * Begins an Asynchronous Create-MonitoredItem Service. After sending the
	 * Request to the Server, a Listener is used to acknowledge, when the service
	 * finished.
	 *
	 * @param Session Session used to send the Request.
	 * @param Request Create and add one or more MonitoredItems to a Subscription
	 * @return List of {@link MonitoredItem} created by the Service.
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected MonitoredItem[] beginCreateMonitoredItems(ClientSession session, CreateMonitoredItemsRequest request,
			String[] key) throws ServiceResultException {
		NodeId requestId = Identifiers.CreateMonitoredItemsRequest;
		CreateMonitoredItemsResponse response = session.beginCreateMonitoredItems(request, requestId);
		return endCreateMonitoredItem(request, session, response, key);
	}

	/**
	 * Begins an Asynchronous Create-Subscription Service. After sending the Request
	 * to the Server, a Listener is used to acknowledge, when the service finished.
	 *
	 * @param Session Session used to send the Request.
	 * @param Request Create a Subscription.
	 * @return {@link Subscription} created by the Service.
	 * @throws ServiceResultException
	 */
	protected Subscription beginCreateSubscription(ClientSession session, CreateSubscriptionRequest request)
			throws ServiceResultException {
		NodeId requestId = Identifiers.CreateSubscriptionRequest;
		CreateSubscriptionResponse response = session.beginCreateSubscription(request, requestId);
		return endCreateSubscription(request, session, response);
	}

	/**
	 * Begins an Asynchronous Delete-Subscription Service. After sending the Request
	 * to the Server, a Listener is used to acknowledge, when the service finished.
	 *
	 * @param Session Session used to send the Request.
	 * @param Request Delete one or more Subscriptions
	 * @return {@link DeleteSubscriptionsResponse}
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected DeleteSubscriptionsResponse beginDeleteSubscription(ClientSession session,
			DeleteSubscriptionsRequest request, boolean continueRemovingSubscription) throws ServiceResultException {
		NodeId requestId = Identifiers.DeleteSubscriptionsRequest;
		DeleteSubscriptionsResponse response = session.beginDeleteSubscription(request, requestId);
		endDeleteSubscriptions(request, session, response, continueRemovingSubscription);
		return response;
	}

	/**
	 * Begins an Asynchronous Delete-MonitoredItem Service. After sending the
	 * Request to the Server, a Listener is used to acknowledge, when the service
	 * finished.
	 *
	 * @param Session Session used to send the Request.
	 * @param Request Remove one or more {@link MonitoredItem} of a
	 *                {@link Subscription}
	 * @return {@link DeleteMonitoredItemsResponse}
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected DeleteMonitoredItemsResponse beginDeleteMonitoredItems(ClientSession session,
			DeleteMonitoredItemsRequest request) throws ServiceResultException {
		NodeId requestId = Identifiers.DeleteMonitoredItemsRequest;
		DeleteMonitoredItemsResponse response = session.beginDeleteMonitoredItems(request, requestId);
		endDeleteMonitoredItems(request, session, response);
		return response;
	}

	/**
	 * Begins an Asynchronous Modify-MonitoredItem Service. After sending the
	 * Request to the Server, a Listener is used to acknowledge, when the service
	 * finished.
	 *
	 * @param Session Session used to send the Request.
	 * @param Request Modify MonitoredItems of a Subscription
	 * @return {@link ModifyMonitoredItemsResponse}
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected ModifyMonitoredItemsResponse beginModifyMonitoredItems(ClientSession session,
			ModifyMonitoredItemsRequest request) throws ServiceResultException {
		NodeId requestId = Identifiers.ModifyMonitoredItemsRequest;
		ModifyMonitoredItemsResponse response = session.beginModifyMonitoredItems(request, requestId);
		endModifyMonitoredItems(request, session, response);
		return response;
	}

	/**
	 * Begins an Asynchronous Modify-Subscriptions Service. After sending the
	 * Request to the Server, a Listener is used to acknowledge, when the service
	 * finished.
	 *
	 * @param Session Session used to send the Request.
	 * @param Request Modify a Subscription.
	 * @return {@link ModifySubscriptionResponse}
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected ModifySubscriptionResponse beginModifySubscription(ClientSession session,
			ModifySubscriptionRequest request) throws ServiceResultException {
		NodeId requestId = Identifiers.ModifySubscriptionRequest;
		ModifySubscriptionResponse response = session.beginModifySubscription(request, requestId);
		endModifySubscription(request, session, response);
		return response;
	}

	/**
	 * Begins an Asynchronous Set-Monitoring-Mode Service. After sending the Request
	 * to the Server, a Listener is used to acknowledge, when the service finished.
	 *
	 * @param Session Session used to send the Request.
	 * @param Request Set the monitoring mode for one or more MonitoredItems of a
	 *                Subscription
	 * @return {@link SetMonitoringModeResponse}
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected SetMonitoringModeResponse beginSetMonitoringMode(ClientSession session, SetMonitoringModeRequest request)
			throws ServiceResultException {
		NodeId requestId = Identifiers.SetMonitoringModeRequest;
		SetMonitoringModeResponse response = session.beginSetMonitoringMode(request, requestId);
		endSetMonitoringMode(request, session, response);
		return response;
	}

	/**
	 * Begins an Asynchronous Set-Publishing Service. After sending the Request to
	 * the Server, a Listener is used to acknowledge, when the service finished.
	 *
	 * @param Session Session used to send the Request.
	 * @param Request Enable sending of Notifications on one or more Subscriptions.
	 * @return {@link SetPublishingModeRequest}
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected SetPublishingModeResponse beginSetPublishing(ClientSession session, SetPublishingModeRequest request)
			throws ServiceResultException {
		NodeId requestId = Identifiers.SetPublishingModeRequest;
		SetPublishingModeResponse response = session.beginSetPublishingMode(request, requestId);
		endSetPublishingMode(request, session, response);
		return response;
	}

	/**
	 * Begins an Asynchronous Set-Triggering Service. After sending the Request to
	 * the Server, a Listener is used to acknowledge, when the service finished.
	 *
	 * @param Session Session used to send the Request.
	 * @param Request Create or Remove triggering links for a triggering item.
	 * @return {@link SetTriggeringResponse}
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected SetTriggeringResponse beginSetTriggering(ClientSession session, SetTriggeringRequest request)
			throws ServiceResultException {
		NodeId requestId = Identifiers.SetTriggeringRequest;
		preSetTriggering(session, request);
		SetTriggeringResponse response = session.beginSetTriggering(request, requestId);
		endSetTriggering(request, session, response);
		return response;
	}

	/**
	 * Begins an Asynchronous Transfer-Subscriptions Service. After sending the
	 * Request to the Server, a Listener is used to acknowledge, when the service
	 * finished.
	 *
	 * @param targetSessionId
	 *
	 * @param Session         Session used to send the Request.
	 * @param Request         Transfer a Subscription and its MonitoredItems from
	 *                        one Session to another.
	 * @return {@link TransferSubscriptionsResponse}
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected TransferSubscriptionsResponse beginTransferSubscriptions(ClientSession session,
			TransferSubscriptionsRequest request, NodeId targetSessionId) throws ServiceResultException {
		NodeId requestId = Identifiers.TransferSubscriptionsRequest;
		TransferSubscriptionsResponse response = session.beginTransferSubscriptions(request, requestId);
		endTransfereSubscriptions(request, session, response, targetSessionId);
		return response;
	}

	/**
	 * Sends the {@link CreateMonitoredItemsRequest} to the server.
	 *
	 * @param Session Session used to send the Request.
	 * @param Request {@link CreateMonitoredItemsRequest} to create
	 *                {@link MonitoredItem} to monitor DataChanges or Events.
	 * @return {@link CreateMonitoredItemsResponse} of the service.
	 * @throws ServiceResultException
	 */
	protected MonitoredItem[] createMonitoredItems(ClientSession session, CreateMonitoredItemsRequest request,
			String[] key) throws ServiceResultException {
		CreateMonitoredItemsResponse response = session.createMonitoredItems(request);
		return endCreateMonitoredItem(request, session, response, key);
	}

	/**
	 * Sends the {@link CreateSubscriptionRequest} to the server.
	 *
	 * @param Session Session used to send the Request.
	 * @param Request {@link CreateSubscriptionRequest} to create a
	 *                {@link Subscription} to hold {@link MonitoredItem}.
	 * @return
	 * @throws ServiceResultException
	 * @throws ServiceFaultException
	 * @throws IllegalArgumentException
	 */
	protected Subscription createSubscription(ClientSession session, CreateSubscriptionRequest request)
			throws ServiceResultException {
		CreateSubscriptionResponse response = session.createSubscription(request);
		return endCreateSubscription(request, session, response);
	}

	/**
	 * Sends the {@link DeleteMonitoredItemsRequest} to the server.
	 *
	 * @param Session Session used to send the Request.
	 * @param Request {@link DeleteMonitoredItemsRequest} to stop monitoring an
	 *                Event or DataChange.
	 * @return
	 * @throws ServiceResultException
	 * @throws ServiceFaultException
	 * @throws IllegalArgumentException
	 */
	protected DeleteMonitoredItemsResponse deleteMonitoredItems(ClientSession session,
			DeleteMonitoredItemsRequest request) throws ServiceResultException {
		DeleteMonitoredItemsResponse response = session.deleteMonitoredItems(request);
		endDeleteMonitoredItems(request, session, response);
		return response;
	}

	/**
	 * Sends the {@link DeleteSubscriptionsRequest} to the server.
	 *
	 * @param Session Session used to send the Request.
	 * @param Request {@link DeleteSubscriptionsRequest} to delete a
	 *                {@link Subscription} on the server with all its containing
	 *                {@link MonitoredItem}.
	 * @return {@link DeleteSubscriptionsResponse} of the Service.
	 * @throws ServiceResultException
	 * @throws ServiceFaultException
	 * @throws IllegalArgumentException
	 */
	protected DeleteSubscriptionsResponse deleteSubscription(ClientSession session, DeleteSubscriptionsRequest request,
			boolean continueRemovingSubscription) throws ServiceResultException {
		DeleteSubscriptionsResponse response = session.deleteSubscription(request);
		endDeleteSubscriptions(request, session, response, continueRemovingSubscription);
		return response;
	}

	/**
	 * Sends the {@link ModifyMonitoredItemsRequest} to the server.
	 *
	 * @param Session Session used to send the Request.
	 * @param Request {@link ModifyMonitoredItemsRequest} to modify its attributes.
	 * @return {@link ModifyMonitoredItemsResponse} of the Service.
	 * @throws ServiceResultException
	 * @throws ServiceFaultException
	 * @throws IllegalArgumentException
	 */
	protected ModifyMonitoredItemsResponse modifyMonitoredItems(ClientSession session,
			ModifyMonitoredItemsRequest request) throws ServiceResultException {
		ModifyMonitoredItemsResponse response = session.modifyMonitoredItems(request);
		endModifyMonitoredItems(request, session, response);
		return response;
	}

	/**
	 * Sends the {@link ModifySubscriptionRequest} to the server.
	 *
	 * @param Session Session used to send the Request.
	 * @param Request {@link ModifySubscriptionRequest} to modify its attributes.
	 * @return {@link ModifySubscriptionResponse} of the Service.
	 * @throws ServiceResultException
	 * @throws ServiceFaultException
	 * @throws IllegalArgumentException
	 */
	protected ModifySubscriptionResponse modifySubscriptions(ClientSession session, ModifySubscriptionRequest request)
			throws ServiceResultException {
		ModifySubscriptionResponse response = session.modifySubscription(request);
		endModifySubscription(request, session, response);
		return response;
	}

	/**
	 * Sends the {@link SetMonitoringModeRequest} to the server.
	 *
	 * @param Session Session used to send the Request.
	 * @param Request {@link SetMonitoringModeRequest} to set the monitoring mode
	 *                for one or more MonitoredItems of a Subscription.
	 * @return {@link SetMonitoringModeResponse} of the Service
	 * @throws ServiceResultException
	 * @throws ServiceFaultException
	 * @throws IllegalArgumentException
	 */
	protected SetMonitoringModeResponse setMonitoringMode(ClientSession session, SetMonitoringModeRequest request)
			throws ServiceResultException {
		SetMonitoringModeResponse response = session.setMonitoringMode(request);
		endSetMonitoringMode(request, session, response);
		return response;
	}

	/**
	 * Sends the {@link SetPublishingModeRequest} to the server.
	 *
	 * @param Session Session used to send the Request.
	 * @param Request {@link SetPublishingModeRequest} to enable sending of
	 *                Notifications on one or more Subscriptions.
	 * @return {@link SetPublishingModeResponse} of the Service.
	 * @throws ServiceResultException
	 * @throws ServiceFaultException
	 * @throws IllegalArgumentException
	 */
	protected SetPublishingModeResponse setPublishingMode(ClientSession session, SetPublishingModeRequest request)
			throws ServiceResultException {
		SetPublishingModeResponse response = session.setPublishingModegMode(request);
		endSetPublishingMode(request, session, response);
		return response;
	}

	/**
	 * Sends the {@link SetTriggeringRequest} to the server.
	 *
	 * @param Session Session used to send the Request.
	 * @param Request {@link SetTriggeringRequest} to create triggering links
	 *                between {@link MonitoredItem}
	 * @return {@link SetTriggeringResponse} of the Service
	 * @throws ServiceResultException
	 * @throws ServiceFaultException
	 * @throws IllegalArgumentException
	 */
	protected SetTriggeringResponse setTriggering(ClientSession session, SetTriggeringRequest request)
			throws ServiceResultException {
		preSetTriggering(session, request);
		SetTriggeringResponse response = session.setTriggering(request);
		endSetTriggering(request, session, response);
		return response;
	}

	/**
	 * Sends the {@link RepublishRequest} to the server.
	 *
	 * @param Session Session used to send the Request.
	 * @param Request {@link RepublishRequest} to receive an older Notification
	 *                Message from an Subscription.
	 * @return {@link RepublishResponse} of the Service.
	 * @throws ServiceResultException
	 * @throws ServiceFaultException
	 * @throws IllegalArgumentException
	 */
	protected RepublishResponse republish(ClientSession session, RepublishRequest request)
			throws ServiceResultException {
		return session.republish(request);
	}

	/**
	 * Sends the {@link TransferSubscriptionsRequest} to the server.
	 *
	 * @param targetSessionId
	 *
	 * @param Session         Session used to send the Request.
	 * @param Request         {@link TransferSubscriptionsRequest} to transfer a
	 *                        Subscription and its MonitoredItems from one Session
	 *                        to another.
	 * @return {@link TransferSubscriptionsResponse} of the Service.
	 * @throws ServiceResultException
	 * @throws ServiceFaultException
	 * @throws IllegalArgumentException
	 */
	protected TransferSubscriptionsResponse transfereSubscriptions(ClientSession session,
			TransferSubscriptionsRequest request, NodeId targetSessionId) throws ServiceResultException {
		TransferSubscriptionsResponse response = session.transfereSubscriptions(request);
		endTransfereSubscriptions(request, session, response, targetSessionId);
		return response;
	}

	/**
	 * Recreates a subscription on the server.
	 *
	 * @param Session  Client Session for the Subscriptions.
	 * @param Template Subscription to recreate.
	 *
	 * @throws ServiceResultException
	 */
	protected void recreateSubscription(ClientSession session, Subscription subscription)
			throws ServiceResultException {
		// max monitored item per create should not be 0
		if (session.getMaxMonitoredItemsPerCreate() <= 0)
			throw new ServiceResultException(StatusCodes.Bad_ConfigurationError);
		CreateSubscriptionRequest subscriptionRequest = new CreateSubscriptionRequest();
		subscriptionRequest.setMaxNotificationsPerPublish(subscription.getMaxNotificationsPerPublish());
		subscriptionRequest.setPriority(subscription.getPriority());
		subscriptionRequest.setPublishingEnabled(subscription.getPublishEnabled());
		subscriptionRequest.setRequestedLifetimeCount(subscription.getLifetimeCount());
		subscriptionRequest.setRequestedMaxKeepAliveCount(subscription.getKeepAliveCount());
		subscriptionRequest.setRequestedPublishingInterval(subscription.getPublishingInterval());
		// tries to create a subscription
		CreateSubscriptionResponse subscriptionResponse = session.beginCreateSubscription(subscriptionRequest,
				Identifiers.CreateSubscriptionRequest);
		/**
		 * Reinitialize subscription ******************************************
		 */
		if (subscriptionResponse != null) {
			synchronized (subscription.getLock()) {
				subscription.reInitialize(session);
				subscription.setSubscriptionId(subscriptionResponse.getSubscriptionId());
				subscription.setKeepAliveCount(subscriptionResponse.getRevisedMaxKeepAliveCount());
				subscription.setCurrentKeepAliveCount(subscriptionResponse.getRevisedMaxKeepAliveCount());
				subscription.setLifetimeCount(subscriptionResponse.getRevisedLifetimeCount());
				subscription.setPublishingInterval(subscriptionResponse.getRevisedPublishingInterval(), true);
				subscription.setMaxNotificationPerPublish(subscriptionRequest.getMaxNotificationsPerPublish());
				subscription.setPriority(subscriptionRequest.getPriority());
				subscription.setPublishEnabled(subscription.getPublishEnabled());
			}
			/**
			 * ******************************************
			 */
			endCreateSubscription(session, subscription, true);
			/**
			 * ******************************************
			 */
			// use handler from the template subscription
			MonitoredItem[] items = subscription.getMonitoredItems();
			// here we should delete monitored items from subscription
			subscription.clearMonitoredItems();
			if (items.length > 0) {
				CreateMonitoredItemsResponse response;
				List<MonitoredItem> tmpItems = new ArrayList<>();
				// create the request for the items
				CreateMonitoredItemsRequest monitoredItemRequest = new CreateMonitoredItemsRequest();
				List<MonitoredItemCreateRequest> itemsToCreate = new ArrayList<>();
				MonitoredItemCreateRequest itemToCreate;
				int count = 0;
				for (MonitoredItem item : items) {
					itemToCreate = new MonitoredItemCreateRequest();
					itemsToCreate.add(itemToCreate);
					// node to monitor
					ReadValueId itemToMonitor = new ReadValueId();
					itemToMonitor.setAttributeId(item.getAttributeId());
					itemToMonitor.setDataEncoding(item.getEnconding());
					itemToMonitor.setIndexRange(item.getIndexRange());
					itemToMonitor.setNodeId(item.getNodeId());
					itemToCreate.setItemToMonitor(itemToMonitor);
					// parameters of the monitoring
					MonitoringParameters parameters = new MonitoringParameters();
					parameters.setClientHandle(item.getClientHandle());
					parameters.setDiscardOldest(item.getDiscardOldest());
					parameters.setFilter(item.getFilter());
					parameters.setQueueSize(item.getQueueSize());
					parameters.setSamplingInterval(item.getSamplingInterval());
					itemToCreate.setRequestedParameters(parameters);
					// monitoring mode
					itemToCreate.setMonitoringMode(item.getMonitoringMode());
					count++;
					tmpItems.add(item);
					// if we reached the max count
					if (count % session.getMaxMonitoredItemsPerCreate() == 0 || count == items.length) {
						monitoredItemRequest.setItemsToCreate(
								itemsToCreate.toArray(new MonitoredItemCreateRequest[itemsToCreate.size()]));
						monitoredItemRequest.setSubscriptionId(subscription.getSubscriptionId());
						monitoredItemRequest.setTimestampsToReturn(subscription.getTimestampsToReturn());
						AsyncResult<?> resultMonItems = session.beginCreateMonitoredItem(monitoredItemRequest);
						response = (CreateMonitoredItemsResponse) resultMonItems.waitForResult();
						endCreateMonitoredItem(monitoredItemRequest, session, response,
								tmpItems.toArray(new MonitoredItem[tmpItems.size()]));
						itemsToCreate.clear();
						tmpItems.clear();
					}
				}
			}
		}
	}

	/**
	 * Called when receiving the {@link ServiceResponse} of the
	 * Create-Monitored-Item Service.
	 *
	 * @param Request               {@link CreateMonitoredItemsRequest} Request,
	 *                              which was sent to the server.
	 * @param Session               Session used to send the Request.
	 * @param Response              {@link CreateMonitoredItemsResponse} Response,
	 *                              which has been received.
	 * @param monitoredItemsCreated {@link MonitoredItem} to add.
	 * @return
	 */
	private MonitoredItem[] endCreateMonitoredItem(CreateMonitoredItemsRequest request, ClientSession session,
			CreateMonitoredItemsResponse response, String[] key) {
		/** Create the items */
		MonitoredItem[] monitoredItems = new MonitoredItem[request.getItemsToCreate().length];
		MonitoredItemCreateResult[] results = response.getResults();
		for (int i = 0; i < request.getItemsToCreate().length; i++) {
			MonitoredItemCreateRequest itemToCreate = request.getItemsToCreate()[i];
			if (results != null && results[i] != null) {
				MonitoredItem monitoredItem = new MonitoredItem();
				monitoredItem.create(itemToCreate.getItemToMonitor().getNodeId(),
						itemToCreate.getItemToMonitor().getAttributeId(),
						itemToCreate.getItemToMonitor().getDataEncoding(),
						itemToCreate.getItemToMonitor().getIndexRange(), itemToCreate.getRequestedParameters(), key[i]);
				monitoredItems[i] = monitoredItem;
			}
		}
		endCreateMonitoredItem(request, session, response, monitoredItems);
		return monitoredItems;
	}

	private void endCreateMonitoredItem(CreateMonitoredItemsRequest request, ClientSession session,
			CreateMonitoredItemsResponse response, MonitoredItem[] monitoredItems) {
		/** Subscription to store the monitored items */
		Subscription subscription = getSubscription(session, request.getSubscriptionId());
		if (subscription == null) {
			try {
				throw new ServiceResultException(StatusCodes.Bad_SubscriptionIdInvalid);
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			}
			return;
		}
		MonitoredItemCreateResult[] results = response.getResults();
		int resultLength;
		if (results == null) {
			resultLength = 0;
		} else {
			resultLength = results.length;
		}
		// check requested items less than result items
		if (resultLength > monitoredItems.length) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					"{0} The response returns more results than monitored items were expected!",
					new String[] { RequestType.CreateMonitoredItems.name() });
		}
		// check requested items more than result items
		else if (resultLength < monitoredItems.length) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					"{0} The response returns less results than monitored items were expected!",
					new String[] { RequestType.CreateMonitoredItems.name() });
		}
		for (int i = 0; i < monitoredItems.length; i++) {
			if (monitoredItems[i] == null) {
				continue;
			}
			if (results == null) {
				throw new NullPointerException("Monitored item result is null!");
			}
			boolean isAdded = monitoredItems[i].setResult(results[i],
					request.getItemsToCreate()[i].getRequestedParameters(), i);
			if (isAdded) {
				subscription.addItem(monitoredItems[i]);
			}
		}
	}

	/**
	 * Called when receiving the {@link ServiceResponse} of the Create-Subscription
	 * Service.
	 *
	 * Creates a new Instance of the Subscription.
	 *
	 * @param Request       {@link CreateSubscriptionRequest} Request, which was
	 *                      sent to the server.
	 * @param Session       Sender of the Service
	 * @param Response      {@link CreateSubscriptionResponse} Response, which has
	 *                      been received.
	 * @param subscriptions {@link Subscription} that have to add in the Client�s
	 *                      {@link SubscriptionManager}
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	private Subscription endCreateSubscription(CreateSubscriptionRequest request, ClientSession session,
			CreateSubscriptionResponse response) throws ServiceResultException {
		if (session == null) {
			throw new IllegalArgumentException("Session");
		}
		UnsignedInteger rlcount = request.getRequestedLifetimeCount();
		UnsignedInteger rkacount = request.getRequestedMaxKeepAliveCount();
		Double rpinterval = request.getRequestedPublishingInterval();
		UnsignedInteger relcount = response.getRevisedLifetimeCount();
		UnsignedInteger rekacount = response.getRevisedMaxKeepAliveCount();
		Double rerpinterval = response.getRevisedPublishingInterval();
		if (rlcount.compareTo(relcount) != 0) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING,
					"{0} Parameter LifetimeCount in subscription {1} has changed from {2} to {3}!",
					new Object[] { RequestType.CreateSubscription.name(), response.getSubscriptionId(),
							request.getRequestedLifetimeCount(), response.getRevisedLifetimeCount() });
		} else if (rkacount.compareTo(rekacount) != 0) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING,
					"{0} Parameter KeepAliveCount in subscription {1} has changed from {2} to {3}!",
					new Object[] { RequestType.CreateSubscription.name(), response.getSubscriptionId(),
							request.getRequestedMaxKeepAliveCount(), response.getRevisedMaxKeepAliveCount() });
		} else if (rpinterval.compareTo(rerpinterval) != 0) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING,
					"{0} Parameter KeepAliveCount in subscription {1} has changed from {2} to {3}!",
					new Object[] { RequestType.CreateSubscription.name(), response.getSubscriptionId(),
							request.getRequestedPublishingInterval(), response.getRevisedPublishingInterval() });
		}
		// create subscription object
		Subscription subscription = new Subscription(session, response.getSubscriptionId(),
				response.getRevisedMaxKeepAliveCount(), response.getRevisedPublishingInterval(),
				request.getMaxNotificationsPerPublish(), request.getPriority(), request.getPublishingEnabled(),
				response.getRevisedLifetimeCount());
		// subscription.setKeepAliveTimer(session.getKeepAliveTimer());
		// subscription.setKeepAliveTimer(new Timer());
		endCreateSubscription(session, subscription, false);
		return subscription;
	}

	/**
	 * Stores the subscription on the parameter session.
	 *
	 * @param Session
	 * @param Subscription
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	private void endCreateSubscription(ClientSession session, Subscription subscription, boolean isReconnection)
			throws ServiceResultException {
		// save subscription
		synchronized (this.lock) {
			Map<Subscription, UnsignedInteger> subscriptionPerSession = this.subscriptions.get(session);
			/** no subscription for the session */
			if (subscriptionPerSession == null) {
				subscriptionPerSession = new HashMap<>();
				subscriptionPerSession.put(subscription, subscription.getSubscriptionId());
				this.subscriptions.put(session, subscriptionPerSession);
			}
			/** existing subscriptions for the session */
			else {
				Subscription existingSubscription = getSubscription(session, subscription.getSubscriptionId());
				if (existingSubscription != null && !isReconnection) {
					// add if id is unique
					Logger.getLogger(getClass().getName()).log(Level.SEVERE,
							"{0} SubscriptionId {1} already exists on this session! Maybe errors occure when using it!",
							new Object[] { RequestType.CreateSubscription.name(), subscription.getSubscriptionId() });
				}
				subscriptionPerSession.put(subscription, subscription.getSubscriptionId());
			}
		}
		// start keep alive timer
		subscription.startKeepAliveTimer(false);
	}

	/**
	 * * Called when receiving the {@link ServiceResponse} of the
	 * Delete-Monitored-Item Service.
	 *
	 * @param Request  {@link DeleteMonitoredItemsRequest} Request, which was sent
	 *                 to the server.
	 * @param Session  Session used to send the Request.
	 * @param Response {@link DeleteMonitoredItemsResponse} Response, which has been
	 *                 received.
	 */
	private void endDeleteMonitoredItems(DeleteMonitoredItemsRequest request, final ClientSession session,
			DeleteMonitoredItemsResponse response) {
		Subscription subscription;
		if ((subscription = getSubscription(session, request.getSubscriptionId())) == null) {
			ServiceResultException sre = new ServiceResultException(StatusCodes.Bad_SubscriptionIdInvalid,
					"No subscription with ID - " + request.getSubscriptionId()
							+ " available to remove monitoredItems!");
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "{0} {1}",
					new String[] { RequestType.DeleteMonitoredItems.name(), sre.getAdditionalTextField() });
			return;
		}
		for (int i = 0; i < request.getMonitoredItemIds().length; i++) {
			if (response.getResults()[i].isGood()) {
				UnsignedInteger itemId = request.getMonitoredItemIds()[i];
				subscription.deleteMonitoredItem(itemId);
			}
			// CTT 10.5- 004-005
			else {
				if (response.getResults() != null && response.getResults().length > 0
						&& response.getResults()[i] != null) {
					UnsignedInteger itemId = request.getMonitoredItemIds()[i];
					subscription.deleteMonitoredItem(itemId);
					Logger.getLogger(getClass().getName()).log(Level.SEVERE,
							"{0} {1}, cannot remove monitored item {2} from server!",
							new Object[] { RequestType.DeleteMonitoredItems.name(),
									response.getResults()[i].getDescription(), request.getMonitoredItemIds()[i] });
				}
			}
		}
	}

	/**
	 * Called when receiving the {@link ServiceResponse} of the Delete-Subscriptions
	 * Service.
	 *
	 * @param Request  {@link DeleteSubscriptionsRequest} Request, which was sent to
	 *                 the server.
	 * @param Session  Session used to send the Request.
	 * @param Response {@link DeleteSubscriptionsResponse} Response, which has been
	 *                 received.
	 */
	private void endDeleteSubscriptions(DeleteSubscriptionsRequest request, final ClientSession session,
			DeleteSubscriptionsResponse response, final boolean continueRemovingSubscription) {
		if (response != null && request.getSubscriptionIds().length != response.getResults().length) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING,
					"{0} There is an inconsistence between the subscriptions to delete in the request and its results!",
					new String[] { RequestType.DeleteSubscriptions.name() });
		}
		List<UnsignedInteger> subscriptions2removeAgain = null;
		synchronized (this.lock) {
			for (int i = 0; i < request.getSubscriptionIds().length; i++) {
				Subscription subscription = getSubscription(session, request.getSubscriptionIds()[i]);
				if (subscription != null) {
					// stop keepalive timer if running
					subscription.stopKeepAliveTimer();
					StatusCode resultCode;
					// get result code
					if (response != null && response.getResults().length > i) {
						resultCode = response.getResults()[i];
					}
					// there is an entry missing in the response
					else {
						if (subscriptions2removeAgain == null) {
							subscriptions2removeAgain = new ArrayList<>();
						}
						subscriptions2removeAgain.add(request.getSubscriptionIds()[i]);
						continue;
					}
					if (resultCode.isGood() && subscription != null) {
						invokeSubscriptionDelete(session, subscription);
					}
				}
			}
		}
		if (subscriptions2removeAgain != null && !subscriptions2removeAgain.isEmpty()) {
			final UnsignedInteger[] sub2remove = subscriptions2removeAgain.toArray(new UnsignedInteger[0]);
			if (continueRemovingSubscription) {
				DeleteSubscriptionsRequest subscriptionRemove = new DeleteSubscriptionsRequest();
				subscriptionRemove.setSubscriptionIds(sub2remove);
				try {
					beginDeleteSubscription(session, subscriptionRemove, false);
				} catch (ServiceResultException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				}
			} else {
				Logger.getLogger(getClass().getName()).log(Level.WARNING,
						"{0} Could not remove subscription(s) from server- {1}!",
						new String[] { RequestType.DeleteSubscriptions.name(),
								MultiDimensionArrayUtils.toString(sub2remove) });
				synchronized (lock) {
					for (int i = 0; i < sub2remove.length; i++) {
						Subscription subscription = getSubscription(session, sub2remove[i]);
						if (subscription != null) {
							invokeSubscriptionDelete(session, subscription);
							subscriptions.get(session).remove(subscription);
							Logger.getLogger(getClass().getName()).log(Level.WARNING,
									"{0} Subscription removed from the client application - {1}!", new Object[] {
											RequestType.DeleteSubscriptions.name(), subscription.getSubscriptionId() });
						}
					}
				}
			}
		}
	}

	private void invokeSubscriptionDelete(ClientSession session, Subscription... subscriptions) {
		for (Subscription s : subscriptions) {
			s.onDelete(session, s);
			this.subscriptions.get(session).remove(s);
			s.clear();
		}
	}

	/**
	 * Called when receiving the {@link ServiceResponse} of the
	 * Modify-Monitored-Item Service.
	 *
	 * @param Request  {@link ModifyMonitoredItemsRequest} Request, which was sent
	 *                 to the server.
	 * @param Session  Session used to send the Request.
	 * @param Response {@link ModifyMonitoredItemsResponse} Response, which has been
	 *                 received.
	 */
	private void endModifyMonitoredItems(ModifyMonitoredItemsRequest request, ClientSession session,
			ModifyMonitoredItemsResponse response) {
		// modify the items on the subscriptionmanager
		Subscription subscription;
		if ((subscription = getSubscription(session, request.getSubscriptionId())) == null) {
			return;
		}
		int index = 0;
		for (MonitoredItemModifyRequest itemToModify : request.getItemsToModify()) {
			MonitoredItem item = subscription
					.getMonitoredItemByClientHandle(itemToModify.getRequestedParameters().getClientHandle());
			MonitoredItemModifyResult result = response.getResults()[index];
			if (result.getStatusCode().isGood()) {
				item.modify(result, request.getItemsToModify()[index].getRequestedParameters());
				index++;
			} else {
				String destination = (item.getSubscription() != null)
						? " Subscription " + item.getSubscription().getSubscriptionId() + " "
						: "";
				Logger.getLogger(getClass().getName()).log(Level.WARNING,
						"{0} Error modifying monitored item {1} {2} because of result {3}",
						new Object[] { RequestType.ModifyMonitoredItems.name(), item.getMonitoredItemId(), destination,
								result.getStatusCode() });
			}
		}
	}

	/**
	 * Called when receiving the {@link ServiceResponse} of the Set-Monitoring-Mode
	 * Service.
	 *
	 * @param Request  {@link SetMonitoringModeRequest} Request, which was sent to
	 *                 the server.
	 * @param Session  Session used to send the Request.
	 * @param Response {@link SetMonitoringModeResponse} Response, which has been
	 *                 received.
	 */
	private void endSetMonitoringMode(SetMonitoringModeRequest request, ClientSession session,
			SetMonitoringModeResponse response) {
		StatusCode[] result = response.getResults();
		Subscription subscription;
		UnsignedInteger[] monitoredItemIds = request.getMonitoredItemIds();
		synchronized (this.lock) {
			subscription = getSubscription(session, request.getSubscriptionId());
			if (subscription == null) {
				return;
			}
		}
		for (int i = 0; i < monitoredItemIds.length; i++) {
			MonitoredItem item = subscription.getMonitoredItemById(monitoredItemIds[i]);
			if (result != null && result[i] != null && result[i].isGood()) {
				item.setMonitoringMode(request.getMonitoringMode());
			} else if (result == null || result[i] == null || result[i].isBad()) {
				StatusCode error = result[i];
				// CTT 10.3- 005-008
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"{0} Error result {1} for item {2} when changing the monitoring mode to {3}!",
						new Object[] { RequestType.SetMonitoringMode.name(),
								error == null ? "null" : error.getDescription(), item.getMonitoredItemId(),
								request.getMonitoringMode() });
			}
		}
	}

	/**
	 * Called when receiving the {@link ServiceResponse} of the Set-Publishing-Mode
	 * Service.
	 *
	 * @param Request  {@link SetPublishingModeRequest} Request, which was sent to
	 *                 the server.
	 * @param Session  Session used to send the Request.
	 * @param Response {@link SetPublishingModeResponse} Response, which has been
	 *                 received.
	 */
	private void endSetPublishingMode(SetPublishingModeRequest request, ClientSession session,
			SetPublishingModeResponse response) {
		// empty
		for (int i = 0; i < request.getSubscriptionIds().length; i++) {
			Subscription subscription = getSubscription(session, request.getSubscriptionIds()[i]);
			if (subscription == null) {
				continue;
			}
			if (response.getResults() != null && response.getResults().length > 0 && response.getResults().length > i
					&& StatusCode.GOOD.equals(response.getResults()[i])) {
				subscription.setPublishEnabled(request.getPublishingEnabled());
			}
		}
	}

	/**
	 * Called when receiving the {@link ServiceResponse} of the Set-Triggering
	 * Service.
	 *
	 * @param Request  {@link SetTriggeringRequest} Request, which was sent to the
	 *                 server.
	 * @param Session  Session used to send the Request.
	 * @param Response {@link SetTriggeringResponse} Response, which has been
	 *                 received.
	 */
	private void endSetTriggering(SetTriggeringRequest request, ClientSession session, SetTriggeringResponse response) {
		Subscription subscription = getSubscription(session, request.getSubscriptionId());
		MonitoredItem triggeringItem = subscription.getMonitoredItemById(request.getTriggeringItemId());
		// empty
		StatusCode[] addResults = response.getAddResults();
		if (addResults != null && addResults.length > 0) {
			for (int i = 0; i < addResults.length; i++) {
				if (addResults[i].isGood()) {
					UnsignedInteger id = request.getLinksToAdd()[i];
					triggeringItem.addTrigger(id);
				}
			}
		}
		StatusCode[] removeResults = response.getRemoveResults();
		if (removeResults != null && removeResults.length > 0) {
			for (int i = 0; i < removeResults.length; i++) {
				if (removeResults[i].isGood()) {
					UnsignedInteger id = request.getLinksToRemove()[i];
					triggeringItem.removeTrigger(id);
				}
			}
		}
	}

	/**
	 * Called when receiving the {@link ServiceResponse} of the Modify-Subscription
	 * Service.
	 *
	 * @param Request  {@link ModifySubscriptionRequest} Request, which was sent to
	 *                 the server.
	 * @param Session  Session used to send the Request.
	 * @param Response {@link ModifySubscriptionResponse} Response, which has been
	 *                 received.
	 */
	private void endModifySubscription(ModifySubscriptionRequest request, final ClientSession session,
			ModifySubscriptionResponse response) {
		final Subscription subscription = getSubscription(session, request.getSubscriptionId());
		synchronized (this.lock) {
			if (subscription == null) {
				return;
			}
		}
		if (response.getRevisedPublishingInterval() != null && response.getRevisedPublishingInterval().isNaN()) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "{0} Received an invalid publish interval {1}!",
					new Object[] { RequestType.ModifySubscription.name(), response.getRevisedPublishingInterval() });
			try {
				session.removeSubscription(subscription);
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			}
			return;
		}
		// set the modified values
		synchronized (subscription.getLock()) {
			subscription.setLifetimeCount(response.getRevisedLifetimeCount());
			subscription.setKeepAliveCount(response.getRevisedMaxKeepAliveCount());
			subscription.setPublishingInterval(response.getRevisedPublishingInterval(), false);
			subscription.setPriority(request.getPriority());
			subscription.setMaxNotificationPerPublish(request.getMaxNotificationsPerPublish());
		}
	}

	/**
	 * Called when receiving the {@link ServiceResponse} of the
	 * Transfer-Subscription Service.
	 *
	 * @param targetSessionId Target Session Id to transfer the subscription.
	 * @param Request         {@link TransferSubscriptionsRequest} Request, which
	 *                        was sent to the server.
	 * @param Session         Session used to send the Request.
	 * @param Response        {@link TransferSubscriptionsResponse} Response, which
	 *                        has been received.
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	private void endTransfereSubscriptions(TransferSubscriptionsRequest request, ClientSession session,
			TransferSubscriptionsResponse response, NodeId targetSessionId) throws ServiceResultException {
		UnsignedInteger[] subscriptionIds = request.getSubscriptionIds();
		if (subscriptionIds == null) {
			return;
		}
		synchronized (this.lock) {
			// check for the subscriptionId in the current session value of
			// the map
			TransferResult[] results = response.getResults();
			for (int i = 0; i < subscriptionIds.length; i++) {
				Subscription subscription = getSubscription(session, request.getSubscriptionIds()[i]);
				TransferResult result = results[i];
				// not successful
				if (subscription == null || result.getStatusCode().isBad()) {
					continue;
				}
				/**
				 * transfer in subscription manager
				 */
				// remove
				this.subscriptions.get(session).remove(subscription);
				// add
				ClientSession transferSession = null;
				for (ClientSession oppositeSession : this.subscriptions.keySet()) {
					if (targetSessionId.equals(oppositeSession.getSessionId())) {
						transferSession = oppositeSession;
						break;
					}
				}
				if (transferSession == null) {
					continue;
				}
				Map<Subscription, UnsignedInteger> subscriptionPerSession = this.subscriptions.get(transferSession);
				/** no subscription */
				if (subscriptionPerSession == null) {
					subscriptionPerSession = new HashMap<>();
					// Add as first
					this.subscriptions.put(transferSession, subscriptionPerSession);
					// Add subscription
					subscriptionPerSession.put(subscription, subscription.getSubscriptionId());
				}
				/** existing subscriptions */
				else {
					subscriptionPerSession.put(subscription, subscription.getSubscriptionId());
				}
				// start keep alive timer
				subscription.startKeepAliveTimer(false);
			}
		}
	}

	private void preSetTriggering(ClientSession session, SetTriggeringRequest request) throws ServiceResultException {
		if (request.getSubscriptionId() == null || UnsignedInteger.ZERO.equals(request.getSubscriptionId())) {
			throw new ServiceResultException(new StatusCode(StatusCodes.Bad_InternalError),
					"SubscriptionId is invalid");
		}
		if ((request.getLinksToAdd() == null && request.getLinksToRemove() == null)
				|| (request.getLinksToAdd().length == 0 && request.getLinksToRemove().length == 0)) {
			throw new ServiceResultException(new StatusCode(StatusCodes.Bad_InternalError), "Nothing to do!");
		}
		boolean isFalse = false;
		Subscription subscription = getSubscription(session, request.getSubscriptionId());
		if (subscription == null) {
			isFalse = true;
		}
		if ((request.getLinksToAdd() != null && request.getLinksToAdd().length > 0) && !isFalse) {
			for (int i = 0; i < request.getLinksToAdd().length; i++) {
				MonitoredItem item = subscription.getMonitoredItemById(request.getLinksToAdd()[i]);
				if (item == null) {
					isFalse = true;
					break;
				}
			}
		}
		if ((request.getLinksToRemove() != null && request.getLinksToRemove().length > 0) && !isFalse) {
			for (int i = 0; i < request.getLinksToRemove().length; i++) {
				MonitoredItem item = subscription.getMonitoredItemById(request.getLinksToRemove()[i]);
				if (item == null) {
					isFalse = true;
					break;
				}
			}
		}
		if (isFalse) {
			throw new ServiceResultException(StatusCodes.Bad_RequestCancelledByClient,
					"SetTriggering parameters are invalid!");
		}
	}

	private Subscription findSubscription(UnsignedInteger subscriptionId, Subscription[] subscriptions) {
		if (subscriptionId == null || subscriptions == null) {
			return null;
		}
		for (Subscription subscription : subscriptions) {
			if (subscriptionId.equals(subscription.getSubscriptionId())) {
				return subscription;
			}
		}
		return null;
	}

	public boolean deleteSubscriptionsInternal(ClientSession clientSession) {
		if (this.subscriptions.containsKey(clientSession)) {
			Map<Subscription, UnsignedInteger> subs = this.subscriptions.remove(clientSession);
			for (Subscription sub : subs.keySet()) {
				sub.stopKeepAliveTimer();
			}
			return true;
		}
		return false;
	}
}
