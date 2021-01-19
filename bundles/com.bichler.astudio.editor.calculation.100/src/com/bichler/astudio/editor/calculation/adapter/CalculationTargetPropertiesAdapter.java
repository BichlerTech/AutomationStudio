package com.bichler.astudio.editor.calculation.adapter;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;

import com.hbsoft.driver.calculation.CalculationDP;
import com.bichler.astudio.editor.calculation.properties.CalculationTargetPropertySource;

public class CalculationTargetPropertiesAdapter implements IAdapterFactory {

	@Override
	public Object getAdapter(Object adaptableObject, @SuppressWarnings("rawtypes") Class adapterType) {
		// target
		if (adapterType == IPropertySource.class && adaptableObject != null
				&& adaptableObject instanceof CalculationDP) {
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
