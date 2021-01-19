package com.bichler.astudio.opcua.properties;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerEcmaScriptsModelNode;

public class ServerEcmaScriptsModelPropertySource extends AbstractOPCPropertySource {

	private static final String PROPERTY_DESCRIPTION = "p_desc";

	public ServerEcmaScriptsModelPropertySource(StudioModelNode adapter) {
		super(adapter);
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {

		return new IPropertyDescriptor[] { new PropertyDescriptor(PROPERTY_DESCRIPTION, "Beschreibung") };
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (PROPERTY_DESCRIPTION.equals(id)) {
			return "";
		}
		return null;
	}

	@Override
	public OPCUAServerEcmaScriptsModelNode getAdapter() {
		return (OPCUAServerEcmaScriptsModelNode) super.getAdapter();
	}
}
