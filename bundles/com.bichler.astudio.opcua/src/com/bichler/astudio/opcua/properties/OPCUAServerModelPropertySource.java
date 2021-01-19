package com.bichler.astudio.opcua.properties;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerModelNode;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.opcua.OPCUAActivator;

public class OPCUAServerModelPropertySource extends AbstractOPCPropertySource {

	private static final String PROPERTY_NAME = "p_name";
	private static final String PROPERTY_ROOTPATH = "p_rpath";
	private static final String PROPERTY_VERSION = "p_vers";

	public OPCUAServerModelPropertySource(StudioModelNode adapter) {
		super(adapter);
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {

		return new IPropertyDescriptor[] {
				new PropertyDescriptor(PROPERTY_NAME,
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.name")),
				new PropertyDescriptor(PROPERTY_ROOTPATH,
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.path")),
				new PropertyDescriptor(PROPERTY_VERSION,
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.version")) };
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (PROPERTY_NAME.equals(id)) {
			return getAdapter().getServerName();
		}
		if (PROPERTY_ROOTPATH.equals(id)) {
			return getAdapter().getFilesystem().getRootPath();
		}
		if (PROPERTY_VERSION.equals(id)) {
			return getAdapter().getVersion();
		}
		return null;
	}

	@Override
	public OPCUAServerModelNode getAdapter() {
		return (OPCUAServerModelNode) super.getAdapter();
	}

}
