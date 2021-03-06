package com.bichler.opc.comdrv.importer;

import java.util.List;

import org.opcfoundation.ua.builtintypes.NodeId;

public class Com_TriggerDpItem {
	private String triggerID = "";
	private NodeId nodeId = NodeId.parseNodeId("ns=1;i=3");
	private boolean active = true;
	private int index = -1;
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

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getTriggerID() {
		return triggerID;
	}

	public void setTriggerID(String triggerID) {
		this.triggerID = triggerID;
	}
}
