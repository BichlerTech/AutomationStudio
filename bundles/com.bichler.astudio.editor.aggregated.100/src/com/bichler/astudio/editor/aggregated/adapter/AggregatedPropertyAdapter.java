package com.bichler.astudio.editor.aggregated.adapter;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;

import com.bichler.astudio.editor.aggregated.model.AggregatedDpModelNode;
import com.bichler.astudio.editor.aggregated.properties.AggregatedPropertySource;

public class AggregatedPropertyAdapter implements IAdapterFactory {

	@Override
	public Object getAdapter(Object adaptableObject, @SuppressWarnings("rawtypes") Class adapterType) {
		// aggregated datapoint node
		if (adapterType == IPropertySource.class && adaptableObject != null
				&& adaptableObject.getClass() == AggregatedDpModelNode.class) {
			return new AggregatedPropertySource(
					(AggregatedDpModelNode) adaptableObject);
		}

		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class[] getAdapterList() {
		return new Class[] { IPropertySource.class };
	}

}
