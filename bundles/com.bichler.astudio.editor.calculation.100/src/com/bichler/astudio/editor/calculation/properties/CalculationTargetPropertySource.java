package com.bichler.astudio.editor.calculation.properties;

import com.hbsoft.driver.calculation.CalculationDP;

public class CalculationTargetPropertySource extends
		AbstractCalculationNodePropertySource {

	private CalculationDP item;


	public CalculationTargetPropertySource(CalculationDP item) {
		super(item.getTarget().getTargetId());
		this.item = item;
	}


	@Override
	public Object getCalculationIndex() {
		return this.item.getArrayindex();
	}
	
}
