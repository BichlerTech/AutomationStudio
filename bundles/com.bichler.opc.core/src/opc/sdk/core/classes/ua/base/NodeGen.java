package opc.sdk.core.classes.ua.base;

import org.opcfoundation.ua.builtintypes.NodeId;

import opc.sdk.core.node.Node;

/**
 * The base class for custom nodes.
 * 
 * @author Arbeit
 * 
 */
public abstract class NodeGen {
	private NodeGen parent = null;
	/** OPC UA handle */
	private Node addressSpaceNode = null;

	public NodeGen() {
	}

	public Node getAddressSpaceNode() {
		return this.addressSpaceNode;
	}

	public void setAddressSpaceNode(Node handle) {
		this.addressSpaceNode = handle;
	}

	public NodeGen getParent() {
		return this.parent;
	}

	public void setParent(NodeGen parent) {
		this.parent = parent;
	}

	public void reportEvent() {
	}

	public abstract NodeId getTypeId();
}
