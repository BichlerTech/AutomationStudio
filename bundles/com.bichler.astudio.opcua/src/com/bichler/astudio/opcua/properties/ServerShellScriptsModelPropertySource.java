package com.bichler.astudio.opcua.properties;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerShellScriptsModelNode;
import com.bichler.astudio.utils.internationalization.CustomString;

public class ServerShellScriptsModelPropertySource extends AbstractOPCPropertySource {
	private static final String PROPERTY_DESCRIPTION = "p_desc";

	public ServerShellScriptsModelPropertySource(StudioModelNode adapter) {
		super(adapter);
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return new IPropertyDescriptor[] { new PropertyDescriptor(PROPERTY_DESCRIPTION,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.description")) };
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (PROPERTY_DESCRIPTION.equals(id)) {
			return "";
		}
		return null;
	}

	@Override
	public OPCUAServerShellScriptsModelNode getAdapter() {
		return (OPCUAServerShellScriptsModelNode) super.getAdapter();
	}
}
