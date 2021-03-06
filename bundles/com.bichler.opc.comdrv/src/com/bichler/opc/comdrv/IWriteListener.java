package com.bichler.opc.comdrv;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;

public interface IWriteListener {
	/**
	 * Prepares a node for a write request. We set flags to the node if it should be
	 * written or not.
	 * 
	 * @param nodeId Nodeid to prepare for write.
	 * @return true if the nodeid was found and could be set otherwise false.
	 */
	boolean prepareWrite(NodeId nodeId);

	/**
	 * Handles an asyncron write request for a given nodeid. This function must be
	 * implemented non blocking.
	 * 
	 * @param nodeId      Nodeid which should be written.
	 * @param value       Value which should be written.
	 * @param oldVald     oldValue from address space.
	 * @param senderState State of the sender for further use.
	 * @return Response of this write request.
	 */
	StatusCode asyncWriteValue(NodeId nodeId, DataValue value, DataValue oldValue, long senderState);

	/**
	 * Handles an asyncron write request for a given nodeid. This function must be
	 * implemented non blocking.
	 * 
	 * @param nodeId      Nodeid which should be written.
	 * @param value       Value which should be written.
	 * @param oldVald     oldValue from address space.
	 * @param indexRange  index range to write value to.
	 * @param senderState State of the sender for further use.
	 * @return Response of this write request.
	 */
	StatusCode asyncWriteValue(NodeId nodeId, DataValue value, DataValue oldValue, String indexRange, long senderState);

	/**
	 * Handles an syncron write request for a given nodeid.
	 * 
	 * @param nodeId      Nodeid which should be written.
	 * @param value       Value which should be written.
	 * @param oldVald     oldValue from address space.
	 * @param senderState State of the sender for further use.
	 * @return Response of this write request.
	 */
	StatusCode syncWriteValue(NodeId nodeId, DataValue value, DataValue oldValue, long senderState);

	/**
	 * Handles an syncron write request for a given nodeid.
	 * 
	 * @param nodeId      Nodeid which should be written.
	 * @param value       Value which should be written.
	 * @param oldVald     oldValue from address space.
	 * @param indexRange  index range to write value to.
	 * @param senderState State of the sender for further use.
	 * @return Response of this write request.
	 */
	StatusCode syncWriteValue(NodeId nodeId, DataValue value, DataValue oldValue, String indexRange, long senderState);
}
