package com.bichler.astudio.opcua.addressspace.xml.exporter.elements;

import org.opcfoundation.ua.builtintypes.NodeId;

public class UAAlias {

	private NodeId nodeId = null;
	private String aliasName = null;
	
	public UAAlias(NodeId nodeId, String aliasName) {
		this.nodeId = nodeId;
		this.aliasName = aliasName;
	}

	public NodeId getNodeId() {
		return nodeId;
	}

	public String getAliasName() {
		return aliasName;
	}

	@Override
	public String toString() {
		return getClass().getName()+" "+getAliasName()+" "+getNodeId();
	}
	
	
}
