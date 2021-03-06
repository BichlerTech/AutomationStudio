package com.bichler.opc.driver.base;

import java.util.HashMap;
import java.util.Map;

import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.opc.comdrv.ComResourceManager;

import opc.sdk.ua.classes.BaseNode;

public class BaseResourceManager {
	private ComResourceManager manager = null;
	private Map<NodeId, BaseNode> nodes = null;

	public BaseResourceManager() {
		this.nodes = new HashMap<>();
	}

	public ComResourceManager getManager() {
		return manager;
	}

	public void setManager(ComResourceManager manager) {
		this.manager = manager;
	}

	public void registerNode(BaseNode node) {
		this.nodes.put(node.getNodeId(), node);
	}

	public BaseNode getNode(NodeId nodeId) {
		return this.nodes.get(nodeId);
	}
}
