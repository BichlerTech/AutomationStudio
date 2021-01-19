package com.bichler.astudio.opcua.opcmodeler.commands;

import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.opcua.components.ui.handler.update.IOPCUAUpdateable;

public class OPCUAUpdateNodeIdEvent implements IOPCUAUpdateable {
	private NodeId oldId = NodeId.NULL;
	private NodeId newId = NodeId.NULL;
	private String driverName;

	public OPCUAUpdateNodeIdEvent() {
		super();
	}

	public NodeId getOldId() {
		return this.oldId;
	}

	public void setOldId(NodeId oldId) {
		this.oldId = oldId;
	}

	public NodeId getNewId() {
		return this.newId;
	}

	public void setNewId(NodeId newId) {
		this.newId = newId;
	}

	@Override
	public String getDrvName() {
		return this.driverName;
	}

	public void setDrivername(String driverName) {
		this.driverName = driverName;
	}
}
