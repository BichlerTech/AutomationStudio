package com.bichler.astudio.opcua.properties.complex;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.bichler.astudio.opcua.widget.model.AdvancedConfigurationNode;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.opcua.OPCUAActivator;

public class OPCUAAddonPropertySource implements IPropertySource {

	private static final String PROPERTY_ADDON = "p_addon";
//	private static final String PROPERTY_ADDON_NAME = "p_addonname";

	private AdvancedConfigurationNode adapter;

	public OPCUAAddonPropertySource(AdvancedConfigurationNode adapter) {
		this.adapter = adapter;
	}

	@Override
	public Object getEditableValue() {
		return this;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] pd = null;

		pd = new IPropertyDescriptor[] {
//				new PropertyDescriptor(PROPERTY_ADDON_NAME, "Name"),
				new PropertyDescriptor(PROPERTY_ADDON,
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.id")) };

		return pd;
	}

	@Override
	public Object getPropertyValue(Object id) {
//		if (PROPERTY_ADDON_NAME.equals(id)) {
//			return getAdapter().getAddonName();
//		}

		if (PROPERTY_ADDON.equals(id)) {
			return getAdapter().getAddonId();
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
