package opc.sdk.server.service.subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Set;

import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.NotificationMessage;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.core.SubscriptionAcknowledgement;

import opc.sdk.core.enums.RequestType;
import opc.sdk.server.core.OPCInternalServer;
import opc.sdk.server.service.session.OPCServerSession;

public class OPCSessionPublishQueue {
	final long LOCK_TIME_WAIT = 10;
	/** publish queue lock */
	// private Object lock2 = new Object();
	private Object lock = new Object();
	/** outstanding queued requests */
	/** flag to execute */
	private boolean publishing = false;
	/** subscriptions used to publish [PublishTask] */
	private Map<Long, OPCQueuedSubscription> queuedSubscriptions = new LinkedHashMap<>();
	/** incoming requests for publishing */
	private LinkedList<OPCPublishOperation> queuedRequests = new LinkedList<>();
	private boolean subscriptionsWaiting;
	private OPCInternalServer server;

	// private ExecutorService threadPool = Executors.newSingleThreadExecutor();
	public OPCSessionPublishQueue(OPCInternalServer server, OPCServerSession session,
			UnsignedInteger maxPublishRequest) {
		this.server = server;
	}

	public void storePublishRequest(OPCPublishOperation operation) {
		// add request to wait for publish
		synchronized (this.lock) {
			this.queuedRequests.addLast(operation);
		}
	}

	/**
	 * Adds a subscription to its session publish queue.
	 * 
	 * @param subscription
	 */
	public void addSubscription(OPCSubscription subscription) {
		synchronized (this.lock) {
			OPCQueuedSubscription queuedSubscription = new OPCQueuedSubscription();
			// queuedSubscription.setPriority(subscription.getPriority());
			queuedSubscription.setReadyToPublish(false);
			queuedSubscription.setTimestamp(System.nanoTime());
			queuedSubscription.setSubscription(subscription);
			this.queuedSubscriptions.put(subscription.getSubscriptionId().longValue(), queuedSubscription);
			if (!this.publishing && this.queuedSubscriptions.size() > 0) {
				this.publishing = true;
			}
		}
	}

	/**
	 * get next message
	 * 
	 * @param operation
	 * @return
	 */
	protected NotificationMessage completePublish(OPCPublishOperation operation) {
		NotificationMessage message = null;
		try {
			acknowledge(operation);
			// service results
			message = getNextMessage(operation);
			// return if no new message exists
			if (message == null) {
				return null;
			}
			operation.setResponseNotificationMessage(message);
			// send operati<on response
			server.getServiceManager().doResponseHeader(operation.getResponse(), operation.getRequest(),
					new ServiceResultException(operation.getError()));
			// log send publish
			// server.logService(RequestType.Publish, (message != null) ?
			// operation.getSubscription() + " -- Message SeqNr.: 1 "
			// + message.getSequenceNumber() + " " + message.getPublishTime() :
			// operation.getSubscription() + " -- [null]");
			operation.sendResponse();
		} catch (ServiceResultException e) {
			server.getServiceManager().onErrorRequest(RequestType.Publish, operation.getEndpointService(), e);
		}
		return message;
	}

	/**
	 * Returns weather the subscription queue is empty or not
	 * 
	 * @return TRUE if the queue is empty of subscriptions to publish, otherwise
	 *         FALSE
	 */
	public boolean isEmpty() {
		synchronized (this.lock) {
			return this.queuedSubscriptions.isEmpty();
		}
	}

	public List<OPCSubscription> publishTimerExpired() {
		// check the available subscriptions
		synchronized (this.lock) {
			List<OPCSubscription> subscriptions2delete = new ArrayList<>();
			try {
				// TODO: keep in sorted state
				List<OPCQueuedSubscription> subscriptions = new ArrayList<>(this.queuedSubscriptions.values());
				Collections.sort(subscriptions, new Comparator<OPCQueuedSubscription>() {
					@Override
					public int compare(OPCQueuedSubscription o1, OPCQueuedSubscription o2) {
						return (-1) * (o1.getPriority().compareTo(o2.getPriority()));
					}
				});
				Map<Long, OPCQueuedSubscription> aliveSubscriptions = new HashMap<>();
				for (OPCQueuedSubscription queuedSubscription : subscriptions) {
					PublishingState state = queuedSubscription.getSubscription().publishTimerExpired();
					if (PublishingState.Expired == state) {
						subscriptions2delete.add(queuedSubscription.getSubscription());
						continue;
					}
					aliveSubscriptions.put(queuedSubscription.getSubscription().getSubscriptionId().longValue(),
							queuedSubscription);
					// check for nothing to do (IDLE)
					if (PublishingState.Idle == state) {
						queuedSubscription.setReadyToPublish(false);
						continue;
					}
					// do nothing if subscription has already been flagged
					// as available
					if (queuedSubscription.isReadyToPublish()) {
						if (queuedSubscription.isReadyToPublish() && this.queuedRequests.size() == 0) {
							if (!subscriptionsWaiting) {
								subscriptionsWaiting = true;
							}
							continue;
						}
					}
					// assign subscription to request if one is available
					if (!queuedSubscription.isPublishing()) {
						assignSubscriptionToRequest(queuedSubscription, null);
					}
				}
				this.queuedSubscriptions = aliveSubscriptions;
			} catch (Exception e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			}
			return subscriptions2delete;
		}
	}

	/**
	 * Removes a subscription from its session publish queue.
	 * 
	 * @param subscriptionId
	 */
	public void removeSubscription(long subscriptionId) {
		synchronized (this.lock) {
			/** remove the subscription from the queue */
			// List<OPCQueuedSubscription> subscriptionToDelete = new
			// ArrayList<>();
			// OPCQueuedSubscription removed =
			this.queuedSubscriptions.remove(subscriptionId);
			// remove any outstanding publishes
			if (this.queuedSubscriptions.size() == 0) {
				List<OPCPublishOperation> requestsToDelete = new ArrayList<>();
				Iterator<OPCPublishOperation> iterator = this.queuedRequests.iterator();
				while (iterator.hasNext()) {
					OPCPublishOperation next = iterator.next();
					requestsToDelete.add(next);
					server.getServiceManager().onErrorRequest(RequestType.Publish, next.getEndpointService(),
							new ServiceResultException(StatusCodes.Bad_NoSubscription));
				}
				this.queuedRequests.removeAll(requestsToDelete);
			}
			if (this.publishing && this.queuedSubscriptions.size() <= 0) {
				this.publishing = false;
			}
		}
	}

	public UnsignedInteger[] getSubscriptionIds() {
		synchronized (this.lock) {
			Set<Long> keys = this.queuedSubscriptions.keySet();
			UnsignedInteger[] ids = new UnsignedInteger[keys.size()];
			int index = 0;
			for (Long value : keys) {
				ids[index] = new UnsignedInteger(value);
				index++;
			}
			return ids;
		}
	}

	private void acknowledge(OPCPublishOperation operation) {
		SubscriptionAcknowledgement[] subscriptionAcknowledgements = operation.getRequestPublishAcknowledge();
		// System.out.print("Operation acknowledge: " + operation);
		StatusCode[] results = acknowledge(subscriptionAcknowledgements);
		operation.addResponseAcknowledgeResults(results);
		// set acknowledged
		operation.getRequest().setSubscriptionAcknowledgements(new SubscriptionAcknowledgement[0]);
	}

	/**
	 * Receives an incoming publish request and acknowledge
	 * 
	 * @param subscriptionAcknowledgements
	 * @return
	 */
	private StatusCode[] acknowledge(SubscriptionAcknowledgement[] subscriptionAcknowledgements) {
		if (subscriptionAcknowledgements == null) {
			return new StatusCode[0];
		}
		StatusCode[] results = new StatusCode[subscriptionAcknowledgements.length];
		for (int i = 0; i < subscriptionAcknowledgements.length; i++) {
			SubscriptionAcknowledgement ack = subscriptionAcknowledgements[i];
			OPCQueuedSubscription subscription = this.queuedSubscriptions.get(ack.getSubscriptionId().longValue());
			// check if found
			if (subscription == null) {
				results[i] = new StatusCode(StatusCodes.Bad_SubscriptionIdInvalid);
				continue;
			}
			results[i] = subscription.getSubscription().acknowledge(ack.getSequenceNumber());
			// TODO: Diagnostics
		}
		// TODO: DIAGNOSTICS
		return results;
	}

	private void assignSubscriptionToRequest(OPCQueuedSubscription subscription,
			OPCPublishOperation moreNotificationOperation) {
		List<OPCPublishOperation> requests2remove = new ArrayList<>();
		OPCPublishOperation operation = null;
		for (final OPCPublishOperation request : this.queuedRequests) {
			StatusCode status = StatusCode.GOOD;
			if (status.isBad()) {
				requests2remove.add(request);
				request.setError(status);
				request.execute(OPCSessionPublishQueue.this);
				continue;
			}
			if (request == moreNotificationOperation) {
				continue;
			}
			// requests2remove.add(request);
			request.setError(StatusCode.GOOD);
			request.setSubscription(subscription);
			request.getSubscription().setPublishing(true);
			operation = request;
			break;
		}
		if (!requests2remove.isEmpty()) {
			this.queuedRequests.removeAll(requests2remove);
		}
		if (operation != null) {
			// OPCPublishOperation operation2execute = operation;
			NotificationMessage message = operation.execute(OPCSessionPublishQueue.this);
			// an valid message to send
			if (message != null) {
				this.queuedRequests.remove(operation);
				// requests2remove.add(operation);
				// if (!requests2remove.isEmpty()) {
				// this.queuedRequests.removeAll(requests2remove);
				// }
			}
			return;
		}
		subscription.setReadyToPublish(true);
		subscription.setTimestamp(System.nanoTime());
	}

	private OPCSubscription completePublish(boolean requeue, OPCPublishOperation operation)
			throws ServiceResultException {
		// this.lock.lock();
		// if (requeue) {
		// operation.setSubscription(null);
		// operation.setError(StatusCode.GOOD);
		// this.queuedRequests.add(operation);
		// return null;
		// }
		// try {
		if (operation.getError().isBad()) {
			if (operation.getSubscription() != null) {
				operation.getSubscription().setPublishing(false);
				assignSubscriptionToRequest(operation.getSubscription(), operation);
			}
		}
		if (operation.getSubscription() == null) {
			throw new ServiceResultException(StatusCodes.Bad_NoSubscription);
		}
		return operation.getSubscription().getSubscription();
		// } finally {
		// this.lock.unlock();
		// }
	}

	/**
	 * 
	 * @param operation
	 * @return
	 * @throws ServiceResultException
	 * 
	 */
	private NotificationMessage getNextMessage(OPCPublishOperation operation) throws ServiceResultException {
		// service results
		NotificationMessage message = null;
		// status messages
		// synchronized (this.statusMessages) {
		// if(statusMessages.size() > 0){
		// StatusMessage statusMessage = this.statusMessages.removeFirst();
		// operation.setSubscriptionId(statusMessage.getSubscriptionId());
		// return statusMessage.getMessage();
		// }
		// }
		// requeue flag
		boolean requeue = false;
		// do {
		// find subscription to publish
		// OPCSubscription subscription = publish(requeue, operation);
		OPCSubscription subscription = completePublish(requeue, operation);
		if (subscription == null) {
			return null;
		}
		operation.setResponseSubscriptionId(subscription.getSubscriptionId());
		operation.setResponseMoreNotifications(false);
		try {
			requeue = false;
			// set available sequencenumber, more notification
			message = subscription.publish(operation);
			if (message != null) {
				return message;
			}
			requeue = true;
		} finally {
			publishCompleted(subscription, operation, operation.hasMoreNotifications());
		}
		// } while (requeue);
		return message;
	}

	/**
	 * Completes a publish or requeue a request if there are more notifications
	 * available.
	 * 
	 * @param operation
	 * 
	 * @param operation
	 * 
	 * @param Subscription     Subscription to publish.
	 * @param MoreNotification Boolean if there are more notifications available to
	 *                         send in a new response.
	 */
	private void publishCompleted(OPCSubscription subscription, OPCPublishOperation operation,
			Boolean moreNotification) {
		for (Entry<Long, OPCQueuedSubscription> entry : this.queuedSubscriptions.entrySet()) {
			OPCQueuedSubscription queuedSubscription = entry.getValue();
			if (queuedSubscription.getSubscription() == subscription) {
				queuedSubscription.setPublishing(false);
				if (moreNotification) {
					assignSubscriptionToRequest(queuedSubscription, operation);
				} else {
					queuedSubscription.setReadyToPublish(false);
					queuedSubscription.setTimestamp(System.nanoTime());
				}
				break;
			}
		}
	}
}
