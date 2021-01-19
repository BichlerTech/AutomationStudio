package com.bichler.astudio.opcua.properties.complex;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.bichler.astudio.opcua.widget.model.AdvancedConfigurationNode;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.opcua.OPCUAActivator;

public class OPCUADevicePropertySource implements IPropertySource {
	private static final String PROPERTY_DEVICE = "p_dev";
	// private static final String PROPERTY_DEVICE_NAME = "p_devname";
	private AdvancedConfigurationNode adapter;

	public OPCUADevicePropertySource(AdvancedConfigurationNode adapter) {
		this.adapter = adapter;
	}

	@Override
	public Object getEditableValue() {
		return this;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] pd = null;
		pd = new IPropertyDescriptor[] { new PropertyDescriptor(PROPERTY_DEVICE,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.id"))
				// ,new PropertyDescriptor(PROPERTY_DEVICE_NAME, "Name")
		};
		return pd;
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (PROPERTY_DEVICE.equals(id)) {
			return getAdapter().getDeviceId();
		}
		// if (PROPERTY_DEVICE_NAME.equals(id)) {
		// return getAdapter().getDeviceName();
		// }
		return null;
	}

	@Override
	public boolean isPropertySet(Object id) {
		return false;
	}

	@Override
	public void resetPropertyValue(Object id) {
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
	}

	public AdvancedConfigurationNode getAdapter() {
		return this.adapter;
	}
}
