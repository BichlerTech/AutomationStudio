package opc.sdk.server.service.subscribe;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.ServiceRequest;
import org.opcfoundation.ua.builtintypes.ServiceResponse;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.NotificationMessage;
import org.opcfoundation.ua.core.PublishRequest;
import org.opcfoundation.ua.core.PublishResponse;
import org.opcfoundation.ua.core.SubscriptionAcknowledgement;
import org.opcfoundation.ua.transport.endpoint.EndpointServiceRequest;

public class OPCPublishOperation {
	/** request to send response */
	private EndpointServiceRequest<PublishRequest, PublishResponse> request = null;
	/** response to send */
	private PublishResponse response = null;
	/** queued subscription */
	private OPCQueuedSubscription subscription = null;
	/** statuscode */
	private StatusCode error = StatusCode.GOOD;

	protected OPCPublishOperation() {
		this.response = new PublishResponse();
	}

	public OPCPublishOperation(EndpointServiceRequest<PublishRequest, PublishResponse> request) {
		this();
		this.request = request;
	}

	public Boolean hasMoreNotifications() {
		return this.response.getMoreNotifications();
	}

	public void setResponseMoreNotifications(boolean moreNotifications) {
		this.response.setMoreNotifications(moreNotifications);
	}

	public NodeId getSessionId() {
		return this.request.getRequest().getRequestHeader().getAuthenticationToken();
	}

	public SubscriptionAcknowledgement[] getRequestPublishAcknowledge() {
		return this.request.getRequest().getSubscriptionAcknowledgements();
	}

	public UnsignedInteger getDeadline() {
		UnsignedInteger timeout = this.request.getRequest().getRequestHeader().getTimeoutHint();
		return timeout != null ? timeout : UnsignedInteger.getFromBits(60000);
	}

	public long getTimestamp() {
		return this.request.getRequest().getRequestHeader().getTimestamp() != null
				? this.request.getRequest().getRequestHeader().getTimestamp().getTimeInMillis()
				: 0;
	}

	public int getSecureChannelId() {
		return this.request.getChannel().getSecureChannelId();
	}

	// public void setError(StatusCode error) {
	// this.error = error;
	// }
	//
	// public StatusCode getError() {
	// return this.error;
	// }
	protected NotificationMessage execute(OPCSessionPublishQueue publishQueue) {
		// finish publish
		return publishQueue.completePublish(this);
	}

	// public void execute(ServiceResultException e) {
	// this.request.sendFault(ServiceFault.createServiceFault(e
	// .getStatusCode().getValue()));
	// }
	public void setSubscription(OPCQueuedSubscription subscription) {
		this.subscription = subscription;
	}

	public OPCQueuedSubscription getSubscription() {
		return this.subscription;
	}

	// /**
	// * Cleans up operation
	// */
	// public void flush() {
	// setSubscription(null);
	// // setError(StatusCode.GOOD);
	// ServerSecureChannel channel = this.request.getChannel();
	// ServerConnection connection = channel.getConnection();
	// // OpcTcpServerSecureChannel
	// }
	public void setError(StatusCode error) {
		this.error = error;
	}

	public StatusCode getError() {
		return this.error;
	}

	public PublishResponse getResponse() {
		return this.response;
	}

	public PublishRequest getRequest() {
		return this.request.getRequest();
	}

	public EndpointServiceRequest<? extends ServiceRequest, ? extends ServiceResponse> getEndpointService() {
		return this.request;
	}

	public void sendResponse() {
		// Integer reqLength =
		// this.request.getRequest().getSubscriptionAcknowledgements() != null
		// ? this.request.getRequest().getSubscriptionAcknowledgements().length
		// : 0;
		// Integer resLength = this.response.getResults() != null ?
		// this.response.getResults().length : 0;
		//
		// if (reqLength != resLength) {
		// System.out.println("difference");
		// }
		this.request.sendResponse(this.response);
		// this.response.setResults(null);
	}

	public void addResponseAcknowledgeResults(StatusCode[] results) {
		List<StatusCode> acknowledgedResults = new ArrayList<>();
		StatusCode[] currentResults = this.response.getResults();
		if (currentResults != null) {
			for (StatusCode result : currentResults) {
				acknowledgedResults.add(result);
			}
		}
		if (results != null) {
			for (StatusCode result : results) {
				acknowledgedResults.add(result);
			}
		}
		this.response.setResults(acknowledgedResults.toArray(new StatusCode[0]));
	}

	// public void setResponseAvailableSequenceNumbers(List<UnsignedInteger>
	// availableSequenceNumbers) {
	// this.response.setAvailableSequenceNumbers(availableSequenceNumbers.toArray(new
	// UnsignedInteger[0]));
	// }
	public void setResponseAvailableSequenceNumbers(UnsignedInteger[] sequenceNumbers) {
		this.response.setAvailableSequenceNumbers(sequenceNumbers);
	}

	public void setResponseNotificationMessage(NotificationMessage message) {
		this.response.setNotificationMessage(message);
	}

	public void setResponseSubscriptionId(UnsignedInteger subscriptionId) {
		this.response.setSubscriptionId(subscriptionId);
	}
}
