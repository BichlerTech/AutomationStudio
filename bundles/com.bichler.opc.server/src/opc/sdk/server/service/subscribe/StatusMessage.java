package opc.sdk.server.service.subscribe;

import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.NotificationMessage;

public class StatusMessage {
	private UnsignedInteger subscriptionId;
	private NotificationMessage message;

	public StatusMessage() {
	}

	public UnsignedInteger getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(UnsignedInteger subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public NotificationMessage getMessage() {
		return message;
	}

	public void setMessage(NotificationMessage message) {
		this.message = message;
	}
}
