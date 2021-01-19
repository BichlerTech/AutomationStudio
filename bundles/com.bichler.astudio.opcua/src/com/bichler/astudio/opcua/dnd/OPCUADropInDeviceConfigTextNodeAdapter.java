package com.bichler.astudio.opcua.dnd;

import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.opcua.widget.AdvancedDriverConfigWidget;

public class OPCUADropInDeviceConfigTextNodeAdapter extends OPCUADropTarget {

	private AdvancedDriverConfigWidget widget;

	public OPCUADropInDeviceConfigTextNodeAdapter(AdvancedDriverConfigWidget widget) {
		super();
		this.widget = widget;
	}

	@Override
	public void setDropValues(NodeId nodeId, String name) {
		this.widget.setConfigNode(nodeId, name);
	}

}
