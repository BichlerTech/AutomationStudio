package com.bichler.astudio.opcua.properties;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.widget.model.AbstractConfigNode;
import com.bichler.astudio.opcua.widget.model.AdvancedRootConfigurationNode;
import com.bichler.astudio.utils.internationalization.CustomString;

public class OPCUAAdvancedRootConfigurationModelPropertySource extends AbstractAdvancedPropertySource {

	private static final String PROPERTY_CONFIG = "p_config";
	private static final String PROPERTY_CONFIG_NAME = "p_configname";

	public OPCUAAdvancedRootConfigurationModelPropertySource(AbstractConfigNode adapter) {
		super(adapter);
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return new IPropertyDescriptor[] {
				new PropertyDescriptor(PROPERTY_CONFIG,
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.id")),
				new PropertyDescriptor(PROPERTY_CONFIG_NAME,
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.name")) };
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (PROPERTY_CONFIG.equals(id)) {
			return getAdapter().getRefNodeId();
		}
		if (PROPERTY_CONFIG_NAME.equals(id)) {
			return getAdapter().getRefNodeName();
		}
		return null;
	}

	@Override
	public AdvancedRootConfigurationNode getAdapter() {
		return (AdvancedRootConfigurationNode) super.getAdapter();
	}

}
