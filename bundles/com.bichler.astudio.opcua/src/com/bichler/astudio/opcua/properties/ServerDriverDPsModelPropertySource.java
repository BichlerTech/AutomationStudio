package com.bichler.astudio.opcua.properties;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerDriverDPsModelNode;
import com.bichler.astudio.utils.internationalization.CustomString;

public class ServerDriverDPsModelPropertySource extends AbstractOPCPropertySource {
	private static final String PROPERTY_DESCRIPTION = "p_desc";
	private static final String PROPERTY_DRIVERTYPE = "p_drvtype";
	private static final String PROPERTY_DRIVERVERSION = "p_drvversion";
	private static final String PROPERTY_DRIVERNAME = "p_drvname";

	public ServerDriverDPsModelPropertySource(StudioModelNode adapter) {
		super(adapter);
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return new IPropertyDescriptor[] {
				new PropertyDescriptor(PROPERTY_DESCRIPTION,
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.description")),
				new PropertyDescriptor(PROPERTY_DRIVERNAME,
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.name")),
				new PropertyDescriptor(PROPERTY_DRIVERTYPE,
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.drivertype")),
				new PropertyDescriptor(PROPERTY_DRIVERVERSION, CustomString
						.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.driverversion")) };
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (PROPERTY_DESCRIPTION.equals(id)) {
			return "";
		}
		if (PROPERTY_DRIVERNAME.equals(id)) {
			return getAdapter().getDriverName();
		}
		if (PROPERTY_DRIVERTYPE.equals(id)) {
			return getAdapter().getDriverType();
		}
		if (PROPERTY_DRIVERVERSION.equals(id)) {
			return getAdapter().getDriverVersion();
		}
		return null;
	}

	@Override
	public OPCUAServerDriverDPsModelNode getAdapter() {
		return (OPCUAServerDriverDPsModelNode) super.getAdapter();
	}
}
