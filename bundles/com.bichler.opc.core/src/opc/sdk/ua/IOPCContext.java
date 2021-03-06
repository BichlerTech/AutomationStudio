package opc.sdk.ua;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.ReferenceDescription;

import opc.sdk.core.node.Node;

public interface IOPCContext {
	public ReferenceDescription[] browse(NodeId node2browse, BrowseDirection direction);

	public NodeId expandedNodeIdToNodeId(ExpandedNodeId expandedNodeId);

	public void syncValueFromDriver(NodeId nodeId, Variant variant, long state);

	public long getDriverId();

	public NodeId getSessionId();

	public AtomicInteger getSeqNrFilehandles();

	public List<FileHandle> getFileHandles();

	public Node getNode(NodeId startId);

	public NamespaceTable getNamespaceUris();
	// public void logError(String msg);
	//
	// public void logError(String msg, Throwable e);
	//
	// public void logInfo(String msg);
	//
	// public void logInfo(String msg, Throwable e);
}
