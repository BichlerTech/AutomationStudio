package opc.sdk.core.node;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.IdType;

public interface NodeIdGenerator {
	/**
	 * Returns the next available nodeId and stores it as the last one
	 * 
	 * @param namespaceIndex
	 * @return
	 */
	public NodeId getNextNodeId(int namespaceIndex, Object value, IdType typ, NodeIdMode mode);

	/**
	 * Shows the next available nodeId
	 * 
	 * @param namespaceIndex
	 * @return
	 */
	public NodeId showNextNodeId(int namespaceIndex, IdType typ, NodeIdMode mode);
}
