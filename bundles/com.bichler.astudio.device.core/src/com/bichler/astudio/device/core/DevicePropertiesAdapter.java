package com.bichler.astudio.device.core;


import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;
import org.osgi.service.prefs.Preferences;

import com.bichler.astudio.device.core.property.DevicePropertySource;

public class DevicePropertiesAdapter implements IAdapterFactory {

	
	@Override
	public Object getAdapter(Object adaptableObject, @SuppressWarnings("rawtypes") Class adapterType) {
		if (adapterType == IPropertySource.class && adaptableObject != null
				&& adaptableObject instanceof Preferences) {
			return new DevicePropertySource(
					(Preferences) adaptableObject);
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class[] getAdapterList() {
		return new Class[] { IPropertySource.class };
	}

}
