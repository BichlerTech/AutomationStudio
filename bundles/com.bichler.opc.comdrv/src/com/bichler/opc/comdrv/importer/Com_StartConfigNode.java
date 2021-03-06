package com.bichler.opc.comdrv.importer;

import org.opcfoundation.ua.builtintypes.NodeId;

public class Com_StartConfigNode {
	private boolean active = false;
	private String deviceNS = "";
	private String deviceID = "";
	private NodeId deviceNodeId = null;
	private String confignodeidNS = "";
	private String confignodeID = "";
	private NodeId configNodeId = null;
	private int index = 0;
	private String value = "";
	private String confignodeName = "";
	private String deviceName = "";

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getDeviceNS() {
		return deviceNS;
	}

	public void setDeviceNS(String deviceNS) {
		this.deviceNS = deviceNS;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public String getConfignodeidNS() {
		return confignodeidNS;
	}

	public void setConfignodeidNS(String confignodeidNS) {
		this.confignodeidNS = confignodeidNS;
	}

	public String getConfignodeID() {
		return confignodeID;
	}

	public void setConfignodeID(String confignodeID) {
		this.confignodeID = confignodeID;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getConfignodeName() {
		return confignodeName;
	}

	public void setConfignodeName(String confignodeName) {
		this.confignodeName = confignodeName;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public NodeId getDeviceNodeId() {
		return deviceNodeId;
	}

	public void setDeviceNodeId(NodeId deviceNodeId) {
		this.deviceNodeId = deviceNodeId;
	}

	public NodeId getConfigNodeId() {
		return configNodeId;
	}

	public void setConfigNodeId(NodeId configNodeId) {
		this.configNodeId = configNodeId;
	}
}
