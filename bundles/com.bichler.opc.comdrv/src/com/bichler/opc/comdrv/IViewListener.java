package com.bichler.opc.comdrv;

import opc.sdk.core.node.Node;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.ViewDescription;

public interface IViewListener {
	/**
	 * Handles a browse for a given nodeid.
	 * 
	 * @param description   Description of the actual browse.
	 * @param maxReferences Maximal amount of references in the result.
	 * @param view          View in which the browse should be performed.
	 * @return Result of browse of a starting nodeid.
	 */
	BrowseResult browse(BrowseDescription description, UnsignedInteger maxReferences, ViewDescription view);

	/**
	 * Checks if the nodeid is present in underlying system.
	 * 
	 * @param nodeId Nodeid of node to check.
	 * @return Return true if nodeid could be found, otherwise false.
	 */
	boolean isNodeInUnderlyingSystem(NodeId nodeId);

	/**
	 * Gets the required node by nodeid from underlying system, if node is not in
	 * the underlying system, it returns null.
	 * 
	 * @param nodeId Nodeid of required node.
	 * @return Node The node if it could be found, otherwise null.
	 */
	Node getNodeFromUnderlyingSystem(NodeId nodeId);
}
