package opc.sdk.server.service.subscribe;

import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.core.NotificationMessage;

/**
 * Stores a {@link Subscription} that {@link NotificationMessage} ready to be
 * sent back to an UA Client.
 * 
 * @author Thomas Z�chbauer
 */
public class OPCQueuedSubscription {
	/** priority of a queued subscription */
	// private UnsignedByte priority = null;
	/** ready to publish flag */
	private boolean readyToPublish = false;
	/** timestamp of the publish service? */
	private long timestamp = Long.MIN_VALUE;
	/** subscription reference */
	private OPCSubscription subscription = null;
	/** Publishing flag */
	private boolean publishing = false;

	// /**
	// * Set the priority of the queued subscription.
	// *
	// * @param priority
	// * of the queued subscription
	// */
	// public void setPriority(UnsignedByte priority) {
	// this.priority = priority;
	// }
	/**
	 * Set the readytopublish flag of the queued subscription.
	 * 
	 * @param readyToPublish of the queued subscription
	 */
	public void setReadyToPublish(boolean readyToPublish) {
		this.readyToPublish = readyToPublish;
	}

	/**
	 * Set the publishing flag of the queued subscription.
	 * 
	 * @param publishing of the queued subscription
	 */
	public void setPublishing(boolean publishing) {
		this.publishing = publishing;
	}

	/**
	 * Set the timestamp to publish of the queued subscription.
	 * 
	 * @param timestamp Timestamp of the QueuedSubscription to compare if more than
	 *                  1 Subscriptions with the same priority have to publish.
	 * 
	 */
	public void setTimestamp(long currentTime) {
		this.timestamp = currentTime;
	}

	/**
	 * Set a subscription to the queued subscription.
	 * 
	 * @param SubscriptionToAdd Set the subscription reference to the queued
	 *                          subscription.
	 */
	public void setSubscription(OPCSubscription subscriptionToAdd) {
		this.subscription = subscriptionToAdd;
	}

	/**
	 * Get the subscription.
	 * 
	 * @return subscription
	 */
	public OPCSubscription getSubscription() {
		return this.subscription;
	}

	/**
	 * Get the priority of the queued subscription.
	 * 
	 * @return priority
	 */
	public UnsignedByte getPriority() {
		return this.subscription.getPriority();
	}

	/**
	 * Get the ready to publish flag of the queued subscription.
	 * 
	 * @return readyToPublish
	 */
	public boolean isReadyToPublish() {
		return this.readyToPublish;
	}

	/**
	 * Get the timestamp of the queued subscription to publish.
	 * 
	 * @return timestamp
	 */
	public long getTimestamp() {
		return this.timestamp;
	}

	/**
	 * Get the publishing flag of the queued subscription.
	 * 
	 * @return publishing
	 */
	public boolean isPublishing() {
		return this.publishing;
	}
}
