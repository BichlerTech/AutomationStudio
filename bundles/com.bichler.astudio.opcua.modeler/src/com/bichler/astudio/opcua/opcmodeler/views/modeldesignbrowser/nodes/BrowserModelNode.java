package com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes;

import opc.sdk.core.node.Node;

public class BrowserModelNode {
	private Node node = null;
	private BrowserModelNode parent;
	private BrowserModelNode[] children = new BrowserModelNode[0];

	public BrowserModelNode(BrowserModelNode parent) {
		this.parent = parent;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public BrowserModelNode getParent() {
		return this.parent;
	}

	public BrowserModelNode[] getChildren() {
		return this.children;
	}

	public void setChildren(BrowserModelNode[] children) {
		// List<BrowserModelNode> newChildren = new ArrayList<>();
		//
		// for (BrowserModelNode newChild : children) {
		// boolean found = false;
		//
		// for (BrowserModelNode currentChild : this.children) {
		// if (currentChild.getNode().getNodeId()
		// .equals(newChild.getNode().getNodeId())) {
		// found = true;
		// newChildren.add(currentChild);
		// break;
		// }
		// }
		//
		// if (!found) {
		// newChildren.add(newChild);
		// }
		// }
		// this.children = newChildren.toArray(new BrowserModelNode[0]);
		this.children = children;
		// TODO: ONLY CHANGED CHILDREN
	}
}
