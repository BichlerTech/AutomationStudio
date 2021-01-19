package com.bichler.astudio.editor.calculation.properties;

import com.bichler.astudio.opcua.properties.driver.AbstractDriverPropertySource;
import com.bichler.astudio.opcua.properties.driver.IDriverNode;

public abstract class AbstractCalculationPropertySource extends
		AbstractDriverPropertySource {
	
	public AbstractCalculationPropertySource() {
		super();
	}

	public AbstractCalculationPropertySource(IDriverNode adaptable) {
		super(adaptable);
	}

	@Override
	public Object getEditableValue() {
		return this;
	}

	@Override
	public void resetPropertyValue(Object id) {

	}

	@Override
	public void setPropertyValue(Object id, Object value) {
	
	}

	@Override
	public boolean isPropertySet(Object id) {
		return false;
	}
}
