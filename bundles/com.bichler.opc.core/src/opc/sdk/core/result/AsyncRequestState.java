package opc.sdk.core.result;

import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.transport.AsyncResult;

/**
 * State of an asnc request.
 * 
 * @author Thomas Z&ouml;bauer
 *
 */
public class AsyncRequestState {
	private NodeId requestTypeId = null;
	private UnsignedInteger requestId = null;
	private DateTime timestamp = null;
	private AsyncResult<?> result = null;
	private Boolean defunct = null;

	public NodeId getRequestTypeId() {
		return requestTypeId;
	}

	public void setRequestTypeId(NodeId requestTypeId) {
		this.requestTypeId = requestTypeId;
	}

	public UnsignedInteger getRequestId() {
		return requestId;
	}

	public void setRequestId(UnsignedInteger requestId) {
		this.requestId = requestId;
	}

	public DateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(DateTime timestamp) {
		this.timestamp = timestamp;
	}

	public AsyncResult<?> getResult() {
		return result;
	}

	public void setResult(AsyncResult<?> result) {
		this.result = result;
	}

	public Boolean getDefunct() {
		return defunct;
	}

	public void setDefunct(Boolean defunct) {
		this.defunct = defunct;
	}
}
