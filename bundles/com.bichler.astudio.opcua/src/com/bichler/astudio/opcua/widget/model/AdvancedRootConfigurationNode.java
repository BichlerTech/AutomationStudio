package com.bichler.astudio.opcua.widget.model;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.NodeId;

public class AdvancedRootConfigurationNode extends AbstractConfigNode {

	private List<AdvancedConfigurationNode> children = new ArrayList<AdvancedConfigurationNode>();
	// default, section is not active
	private boolean isActive = false;
	// no nodeid
	private NodeId refNodeId = null;
	// empty attributes
	private String refNodeName = "";
	private String startAddress = "";
	private String datablock = "";
	private String groupRange = "";
	private String addonRange = "";
	private String metaId = "";

	public AdvancedRootConfigurationNode(AdvancedSectionType type) {
		super(type);
	}

	@Override
	public AdvancedConfigurationNode[] getChildren() {
		return children.toArray(new AdvancedConfigurationNode[0]);
	}

	public void addChild(AdvancedConfigurationNode child) {
		child.setParent(this);
		this.children.add(child);
	}

	public Boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public void removeChild(AdvancedConfigurationNode node) {
		this.children.remove(node);
	}

	public String getRefNodeName() {
		return refNodeName;
	}

	public void setRefNodeName(String refNode) {
		this.refNodeName = refNode;
	}

	public NodeId getRefNodeId() {
		return refNodeId;
	}

	public void setRefNodeId(NodeId refNodeId) {
		this.refNodeId = refNodeId;
	}

	public void setStartAddress(String text) {
		this.startAddress = text;
	}

	public void setDatablock(String text) {
		this.datablock = text;
	}

	public String getDatablock() {
		return this.datablock;
	}

	public void setGroupRange(String text) {
		this.groupRange = text;
	}

	public void setAddonRange(String text) {
		this.addonRange = text;
	}

	public String getStartAddress() {
		return this.startAddress;
	}

	public String getRangeAddon() {
		return this.addonRange;
	}

	public String getRangeGroup() {
		return this.groupRange;
	}

	public void setMetaId(String metaId) {
		this.metaId = metaId;
	}

	public String getMeterId() {
		return this.metaId;
	}

	@Override
	public AbstractConfigNode getParent() {
		return null;
	}

	public void setChildren(List<AdvancedConfigurationNode> children) {
		this.children = children;
	}

	// @Override
	// public void moveChild(IConfigNode child, int indizes2move) {
	// int index = this.children.indexOf(child);
	//
	// // IConfigNode[] nodes = this.children.toArray(new IConfigNode[0]);
	//
	// // this.children.
	// }
}
