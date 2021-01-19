package com.bichler.astudio.device.opcua.property;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.osgi.service.prefs.Preferences;

import com.bichler.astudio.device.core.preference.DevicePreferenceManager;
import com.bichler.astudio.device.opcua.DeviceActivator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class DevicePropertySource implements IPropertySource {

	private Preferences adaptable;

	private static final String PROPERTY_NAME = "p_name";
	private static final String PROPERTY_HOST = "p_host";
	private static final String PROPERTY_USER = "p_user";
	private static final String PROPERTY_TIMEOUT = "p_timeout";
	private static final String PROPERTY_ROOTPATH = "p_rootpath";

	public DevicePropertySource(Preferences adaptable) {
		this.adaptable = adaptable;
	}

	@Override
	public Object getEditableValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {

		IPropertyDescriptor[] descriptors = new IPropertyDescriptor[] {
				new PropertyDescriptor(PROPERTY_NAME,
						CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
								"com.bichler.astudio.device.opcua.property.device")),
				new PropertyDescriptor(PROPERTY_HOST,
						CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
								"com.bichler.astudio.device.opcua.property.host")),
				new PropertyDescriptor(PROPERTY_USER,
						CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
								"com.bichler.astudio.device.opcua.property.user")),
				new PropertyDescriptor(PROPERTY_TIMEOUT,
						CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
								"com.bichler.astudio.device.opcua.property.timeout")),
				new PropertyDescriptor(PROPERTY_ROOTPATH,
						CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
								"com.bichler.astudio.device.opcua.property.path")) };
		return descriptors;
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (PROPERTY_NAME.equals(id)) {
			return DevicePreferenceManager.getPreferenceDeviceName(this.adaptable);
		}

		if (PROPERTY_HOST.equals(id)) {
			return DevicePreferenceManager.getPreferenceDeviceHost(this.adaptable);
		}

		if (PROPERTY_USER.equals(id)) {
			return DevicePreferenceManager.getPreferenceDeviceUser(this.adaptable);
		}

		if (PROPERTY_TIMEOUT.equals(id)) {
			return DevicePreferenceManager.getPreferenceDeviceTimeout(this.adaptable);
		}

		if (PROPERTY_ROOTPATH.equals(id)) {
			return DevicePreferenceManager.getPreferenceDeviceRootpath(this.adaptable);
		}

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

}
