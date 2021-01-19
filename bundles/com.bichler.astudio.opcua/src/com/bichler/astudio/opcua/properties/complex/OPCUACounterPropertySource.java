package com.bichler.astudio.opcua.properties.complex;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.bichler.astudio.opcua.widget.model.AdvancedConfigurationNode;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.opcua.OPCUAActivator;

public class OPCUACounterPropertySource implements IPropertySource {

	private static final String PROPERTY_CONFIG = "p_conf";
//	private static final String PROPERTY_CONFIG_NAME = "p_confname";

	private AdvancedConfigurationNode adapter;

	public OPCUACounterPropertySource(AdvancedConfigurationNode adapter) {
		this.adapter = adapter;
	}

	@Override
	public Object getEditableValue() {
		return this;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] pd = null;

		if (getAdapter().isState()) {
			pd = new IPropertyDescriptor[] {
//					new PropertyDescriptor(PROPERTY_CONFIG_NAME, "Countername"),
					new PropertyDescriptor(PROPERTY_CONFIG,
							CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.counter")) };
		}
		return pd;
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (getAdapter().isState()) {
			if (PROPERTY_CONFIG.equals(id)) {
				return getAdapter().getDeviceId();
			}
//			if (PROPERTY_CONFIG_NAME.equals(id)) {
//				return getAdapter().getCounter();
//			}
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

	public AdvancedConfigurationNode getAdapter() {
		return this.adapter;
	}

}
