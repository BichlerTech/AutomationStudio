package com.bichler.astudio.opcua.properties;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
//import com.bichler.astudio.opcua.nodes.OPCUAServerInfoModelNode;
import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.nodes.OPCUAServerInfoModelNode;
import com.bichler.astudio.utils.internationalization.CustomString;

public class InfoModelPropertySource extends AbstractOPCPropertySource {

	private static final String PROPERTY_DESCRIPTION = "p_desc";
	private static final String PROPERTY_NAME = "p_name";

	public InfoModelPropertySource(StudioModelNode adapter) {
		super(adapter);
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {

		return new IPropertyDescriptor[] {
				new PropertyDescriptor(PROPERTY_DESCRIPTION,
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.description")),
				new PropertyDescriptor(PROPERTY_NAME,
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.namespace")) };
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (PROPERTY_DESCRIPTION.equals(id)) {
			return "";
		}
		if (PROPERTY_NAME.equals(id)) {
			return getAdapter().getInfModelName();
		}
		return null;
	}

	@Override
	public OPCUAServerInfoModelNode getAdapter() {
		return (OPCUAServerInfoModelNode) super.getAdapter();
	}

}
