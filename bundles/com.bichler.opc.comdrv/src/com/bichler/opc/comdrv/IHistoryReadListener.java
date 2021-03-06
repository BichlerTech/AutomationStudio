package com.bichler.opc.comdrv;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.core.HistoryReadDetails;
import org.opcfoundation.ua.core.HistoryReadResult;

public interface IHistoryReadListener {
	/**
	 * Prepares a node for a read request. We set flags to the node if it should be
	 * red or not.
	 * 
	 * @param nodeId Nodeid to prepare for read.
	 * @return true if the nodeid was found and could be set otherwise false.
	 */
	boolean prepareRead(NodeId nodeId);

	/**
	 * Handles an asyncron history read request for a given nodeid. This function
	 * must be implemented non blocking.
	 * 
	 * @param nodeId      Nodeid which should be written.
	 * @param senderState State of the sender for further use.
	 * @return Response of this read request.
	 */
	StatusCode asyncHistoryReadValue(NodeId nodeId, long senderState);

	/**
	 * Handles an syncron history read request for a given nodeid.
	 * 
	 * @param nodeId      Nodeid which should be written.
	 * @param senderState State of the sender for further use.
	 * @return Response of this read request.
	 */
	HistoryReadResult syncHistoryReadValue(NodeId nodeId, HistoryReadDetails details, long senderState);

	/**
	 * Handles an syncron history read request for a given nodeid.
	 * 
	 * @param nodeId      Nodeid which should be written.
	 * @param senderState State of the sender for further use.
	 * @return Response of this read request.
	 */
	HistoryReadResult syncHistoryReadEventValue(NodeId nodeId, HistoryReadDetails details, long senderState);
}
