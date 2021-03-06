package opc.sdk.server.service.subscribe;

/**
 * Publishing States for a subscription.
 * 
 * @author Thomas Z�chbauer
 */
public enum PublishingState {
	/** Idle -> nothing to do */
	Idle,
	/** Expired -> delete */
	Expired,
	/** Waiting for publish -> send publish */
	WaitingForPublish,
	/** NotificationsAvailable -> send publish */
	NotificationsAvailable;
}
