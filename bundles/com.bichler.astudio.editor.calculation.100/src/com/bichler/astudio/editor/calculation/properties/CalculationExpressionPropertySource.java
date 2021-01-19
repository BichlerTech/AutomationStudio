package com.bichler.astudio.editor.calculation.properties;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.hbsoft.driver.calculation.CalculationExpression;

public class CalculationExpressionPropertySource extends
		AbstractCalculationPropertySource {

	private static final String PROPERTY_TARGET_EXPRESSION = "target_expression";

	private CalculationExpression item;

	public CalculationExpressionPropertySource(CalculationExpression item) {
		this.item = item;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return new IPropertyDescriptor[] { new PropertyDescriptor(
				PROPERTY_TARGET_EXPRESSION, "Expression") };
	}

	public Object getPropertyValue(Object id) {
		if (this.item instanceof CalculationExpression) {
			if (PROPERTY_TARGET_EXPRESSION.equals(id)) {
				return item.getContent();
			}
		}

		return null;
	}
}
