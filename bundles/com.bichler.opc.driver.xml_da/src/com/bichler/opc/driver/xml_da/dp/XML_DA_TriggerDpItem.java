package com.bichler.opc.driver.xml_da.dp;

import java.util.List;

import org.opcfoundation.ua.builtintypes.NodeId;

public class XML_DA_TriggerDpItem {
	private NodeId nodeId = NodeId.parseNodeId("ns=1;i=3");
	private boolean active = true;
	private List<NodeId> nodesToRead = null;

	public NodeId getNodeId() {
		return nodeId;
	}

	public void setNodeId(NodeId nodeId) {
		this.nodeId = nodeId;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<NodeId> getNodesToRead() {
		return nodesToRead;
	}

	public void setNodesToRead(List<NodeId> nodesToRead) {
		this.nodesToRead = nodesToRead;
	}
}
