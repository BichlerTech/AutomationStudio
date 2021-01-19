package com.bichler.astudio.opcua.adapter;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;

import com.bichler.astudio.opcua.properties.OPCUAAdvancedConfigurationModelPropertySource;
import com.bichler.astudio.opcua.properties.OPCUAAdvancedRootConfigurationModelPropertySource;
import com.bichler.astudio.opcua.widget.model.AdvancedConfigurationNode;
import com.bichler.astudio.opcua.widget.model.AdvancedRootConfigurationNode;

public class OPCAdvancedConfigPropertyAdapterFactory implements IAdapterFactory {

	@Override
	public Object getAdapter(Object adaptableObject, @SuppressWarnings("rawtypes") Class adapterType) {
		if (adapterType == IPropertySource.class && adaptableObject != null
				&& adaptableObject.getClass() == AdvancedConfigurationNode.class) {
			return new OPCUAAdvancedConfigurationModelPropertySource((AdvancedConfigurationNode) adaptableObject);
		}
		if (adapterType == IPropertySource.class && adaptableObject != null
				&& adaptableObject.getClass() == AdvancedRootConfigurationNode.class) {
			return new OPCUAAdvancedRootConfigurationModelPropertySource(
					(AdvancedRootConfigurationNode) adaptableObject);
		}

		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class[] getAdapterList() {
		return new Class[] { IPropertySource.class };
	}

}
