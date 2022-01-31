package com.bichler.astudio.editor.calculation.properties;

import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.opc.driver.calculation.CalculationNode;

public class CalculationNodePropertySource extends
		AbstractCalculationNodePropertySource {

	private CalculationNode item;

	public CalculationNodePropertySource(CalculationNode item) {
		super(item.getContent() == null ? NodeId.NULL : NodeId.parseNodeId(item
				.getContent()));
		this.item = item;
	}

	@Override
	public Object getCalculationIndex() {
		return this.item.getArrayIndex();
	}



}
