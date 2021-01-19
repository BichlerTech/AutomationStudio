package com.bichler.astudio.editor.aggregated.model;

import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.editor.aggregated.dp.AggregatedDPItem;
import com.bichler.astudio.opcua.properties.driver.IDriverNode;

public class AggregatedDpModelNode implements IDriverNode {

	private AggregatedDPItem dp = null;
	private boolean valid = true;

	public AggregatedDpModelNode(AggregatedDPItem item) {
		this.dp = item;
	}

	public AggregatedDPItem getDPItem() {
		return this.dp;
	}

	@Override
	public String getDname() {
		return this.dp.getDisplayName();
	}

	@Override
	public String getDesc() {
		return "";
	}

	@Override
	public String getDtype() {
		return "";
	}

	@Override
	public NodeId getNId() {
		return this.dp != null ? this.dp.getTargetNodeId() : NodeId.NULL;
	}

	@Override
	public boolean isValid() {
		return this.valid;
	}

	@Override
	public String getBrowsepath() {
		return this.dp.getServerBrowsePath();
	}

}
