package com.bichler.astudio.opcua.adapter;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import com.bichler.astudio.opcua.properties.OPCUATriggerNodesPropertySource;
import com.bichler.astudio.opcua.widget.NodeToTrigger;

public class TriggerNodePropertyAdapterFactory implements IAdapterFactory {

	@Override
	public Object getAdapter(Object adaptableObject, @SuppressWarnings("rawtypes") Class adapterType) {
		if (adapterType == IPropertySource.class && adaptableObject != null
				&& adaptableObject.getClass() == NodeToTrigger.class) {
			return new OPCUATriggerNodesPropertySource((NodeToTrigger) adaptableObject);
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class[] getAdapterList() {
		return new Class[] { IPropertyDescriptor.class };
	}

}
