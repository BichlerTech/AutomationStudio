package com.bichler.opc.comdrv.importer;

import java.util.HashMap;
import java.util.Map;

import org.opcfoundation.ua.builtintypes.NodeId;

public class Com_DeviceConfigNode {
	private boolean active = false;
	private String confignodeidNS = "";
	private String confignodeID = "";
	private NodeId configNodeId = null;
	private String confignodeName = "";
	private Map<Integer, DeviceNode> devices = new HashMap<Integer, Com_DeviceConfigNode.DeviceNode>();

	public class DeviceNode {
		private String deviceNS = "";
		private String deviceID = "";
		private NodeId deviceNodeId = null;
		private int value = 0;
		private String deviceName = "";
		private String filename = "";
		private String mappingFile = "";

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

		public NodeId getDeviceNodeId() {
			return deviceNodeId;
		}

		public void setDeviceNodeId(NodeId deviceNodeId) {
			this.deviceNodeId = deviceNodeId;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

		public String getDeviceName() {
			return deviceName;
		}

		public void setDeviceName(String deviceName) {
			this.deviceName = deviceName;
		}

		public String getFilename() {
			return filename;
		}

		public void setFilename(String filename) {
			this.filename = filename;
		}

		public String getMappingFile() {
			return mappingFile;
		}

		public void setMappingFile(String mappingFile) {
			this.mappingFile = mappingFile;
		}
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
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

	public String getConfignodeName() {
		return confignodeName;
	}

	public void setConfignodeName(String confignodeName) {
		this.confignodeName = confignodeName;
	}

	public NodeId getConfigNodeId() {
		return configNodeId;
	}

	public void setConfigNodeId(NodeId configNodeId) {
		this.configNodeId = configNodeId;
	}

	public Map<Integer, DeviceNode> getDevices() {
		return devices;
	}

	public void setDevices(Map<Integer, DeviceNode> devices) {
		this.devices = devices;
	}
}
