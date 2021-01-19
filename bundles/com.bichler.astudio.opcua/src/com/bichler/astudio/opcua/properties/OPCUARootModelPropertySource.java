package com.bichler.astudio.opcua.properties;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.opcua.nodes.OPCUARootModelNode;

public class OPCUARootModelPropertySource extends AbstractOPCPropertySource {

	public OPCUARootModelPropertySource(StudioModelNode adapter) {
		super(adapter);
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return new IPropertyDescriptor[] {};
	}

	@Override
	public Object getPropertyValue(Object id) {
		return null;
	}

	@Override
	public OPCUARootModelNode getAdapter() {
		return (OPCUARootModelNode) super.getAdapter();
	}

}
