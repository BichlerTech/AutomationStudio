package com.bichler.astudio.editor.allenbradley.model;

import org.opcfoundation.ua.builtintypes.NodeId;

public class AllenBradleyStartConfigurationNode {

	private boolean isActive = false;
	private NodeId deviceId = NodeId.NULL;
	private NodeId configNodeId = NodeId.NULL;
	private int index = -1;
	private int value = -1;
	private String configNodeName = "";
	private String deviceName = "";

	public void setActive(boolean active) {
		this.isActive = active;
	}

	public Boolean isActive() {
		return this.isActive;
	}

	public NodeId getDeviceId() {
		return this.deviceId;
	}

	public NodeId getConfigNodeId() {
		return this.configNodeId;
	}

	public Integer getIndex() {
		return this.index;
	}

	public Integer getValue() {
		return this.value;
	}

	public void setConfigId(NodeId configId) {
		this.configNodeId = configId;
	}

	public void setDeviceId(NodeId deviceId) {
		this.deviceId = deviceId;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getDeviceName() {
		return this.deviceName;
	}

	public String getConfigNodeName() {
		return this.configNodeName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public void setConfigNodeName(String configNodeName) {
		this.configNodeName = configNodeName;
	}
}
