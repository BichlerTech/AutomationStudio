package com.bichler.opc.comdrv.importer;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.NodeId;

public class Com_CounterConfigGroup {
	private boolean active = false;
	private String confignodeidNS = "";
	private String confignodeID = "";
	private NodeId configNodeId = null;
	// private int index = 0;
	private String value = "";
	private String confignodeName = "";
	private List<Com_CounterConfigNode> counterlist = null;

	public Com_CounterConfigGroup() {
		counterlist = new ArrayList<Com_CounterConfigNode>();
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

	// public int getIndex() {
	// return index;
	// }
	// public void setIndex(int index) {
	// this.index = index;
	// }
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

	public NodeId getConfigNodeId() {
		return configNodeId;
	}

	public void setConfigNodeId(NodeId configNodeId) {
		this.configNodeId = configNodeId;
	}

	public List<Com_CounterConfigNode> getCounterlist() {
		return counterlist;
	}

	public void setCounterlist(List<Com_CounterConfigNode> counterlist) {
		this.counterlist = counterlist;
	}
}
