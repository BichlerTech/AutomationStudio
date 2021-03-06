package com.bichler.opc.comdrv;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;

public interface IReadListener {
	/**
	 * Prepares a node for a read request. We set flags to the node if it should be
	 * red or not.
	 * 
	 * @param nodeId Nodeid to prepare for read.
	 * @return true if the nodeid was found and could be set otherwise false.
	 */
	boolean prepareRead(NodeId nodeId);

	/**
	 * Handles an asyncron read request for a given nodeid. This function must be
	 * implemented non blocking.
	 * 
	 * @param nodeId      Nodeid which should be written.
	 * @param senderState State of the sender for further use.
	 * @return Response of this read request.
	 */
	StatusCode asyncReadValue(NodeId nodeId, long senderState);

	/**
	 * Handles an syncron read request for a given nodeid.
	 * 
	 * @param nodeId      Nodeid which should be written.
	 * @param senderState State of the sender for further use.
	 * @return Response of this read request.
	 */
	DataValue syncReadValue(NodeId nodeId, long senderState);
}
