package com.bichler.astudio.editor.calculation.dnd;

import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.editor.calculation.CalculationComposite;
import com.bichler.astudio.opcua.dnd.OPCUADropTarget;

public class CalculationDnDTargetAdapter extends OPCUADropTarget {

	private CalculationComposite widget;

	public CalculationDnDTargetAdapter(CalculationComposite widget) {
		super();
		this.widget = widget;
	}

	@Override
	public void setDropValues(NodeId nodeId, String name) {
		widget.setCalculationComposite(nodeId, name);
	}

}
