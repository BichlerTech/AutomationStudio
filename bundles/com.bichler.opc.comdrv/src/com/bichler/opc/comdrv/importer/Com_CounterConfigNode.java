package com.bichler.opc.comdrv.importer;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.NodeId;

public class Com_CounterConfigNode {
	private List<NodeId> counterlist = null;

	public Com_CounterConfigNode() {
		counterlist = new ArrayList<NodeId>();
	}

	public List<NodeId> getCounterlist() {
		return counterlist;
	}

	public void setCounterlist(List<NodeId> counterlist) {
		this.counterlist = counterlist;
	}
}
