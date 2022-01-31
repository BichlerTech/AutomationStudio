package com.bichler.astudio.editor.calculation.adapter;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;

import com.hbsoft.driver.calculation.CalculationDP;
import com.hbsoft.driver.calculation.CalculationExpression;
import com.hbsoft.driver.calculation.CalculationNode;
import com.bichler.astudio.editor.calculation.properties.CalculationExpressionPropertySource;
import com.bichler.astudio.editor.calculation.properties.CalculationNodePropertySource;
import com.bichler.astudio.editor.calculation.properties.CalculationTargetPropertySource;

public class CalculationPropertiesAdapter implements IAdapterFactory {

	@Override
	public Object getAdapter(Object adaptableObject, @SuppressWarnings("rawtypes") Class adapterType) {
		// calculation node
		if (adapterType == IPropertySource.class && adaptableObject != null
				&& adaptableObject.getClass() == CalculationNode.class) {
			return new CalculationNodePropertySource(
					(CalculationNode) adaptableObject);
		}
		// calculation expression
		if (adapterType == IPropertySource.class && adaptableObject != null
				&& adaptableObject.getClass() == CalculationExpression.class) {
			return new CalculationExpressionPropertySource(
					(CalculationExpression) adaptableObject);
		}
		// target
		if (adapterType == IPropertySource.class && adaptableObject != null
				&& adaptableObject.getClass() == CalculationDP.class) {
			return new CalculationTargetPropertySource(
					(CalculationDP) adaptableObject);
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class[] getAdapterList() {
		return new Class[] { IPropertySource.class };
	}

}
