package com.bichler.astudio.opcua.dnd;

import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.opcua.widget.AdvancedDriverConfigWidget;

public class OPCUADropInDeviceMappingTextNodeAdapter extends OPCUADropTarget {

	private AdvancedDriverConfigWidget widget;

	public OPCUADropInDeviceMappingTextNodeAdapter(AdvancedDriverConfigWidget widget) {
		super();
		this.widget = widget;
	}

	@Override
	public void setDropValues(NodeId nodeId, String name) {
		widget.setCounterConfigNode(nodeId, name);
	}

}
