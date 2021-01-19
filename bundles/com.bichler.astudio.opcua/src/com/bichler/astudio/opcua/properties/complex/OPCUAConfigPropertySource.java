package com.bichler.astudio.opcua.properties.complex;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.widget.model.AdvancedConfigurationNode;
import com.bichler.astudio.utils.internationalization.CustomString;

public class OPCUAConfigPropertySource implements IPropertySource {
	private static final String PROPERTY_CONFIG = "p_configuration";
	// private static final String PROPERTY_CONFIG_NAME = "p_configurationname";
	private AdvancedConfigurationNode adapter;

	public OPCUAConfigPropertySource(AdvancedConfigurationNode adapter) {
		this.adapter = adapter;
	}

	@Override
	public Object getEditableValue() {
		return this;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] pd = null;
		pd = new IPropertyDescriptor[] { new PropertyDescriptor(PROPERTY_CONFIG,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.id"))
				// ,new PropertyDescriptor(PROPERTY_CONFIG_NAME, "Name")
		};
		return pd;
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (PROPERTY_CONFIG.equals(id)) {
			return getAdapter().getConfigNodeId();
		}
		// if (PROPERTY_CONFIG_NAME.equals(id)) {
		// return getAdapter().getConfigNodeName();
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
