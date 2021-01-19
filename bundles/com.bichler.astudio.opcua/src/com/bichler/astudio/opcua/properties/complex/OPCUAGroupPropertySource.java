package com.bichler.astudio.opcua.properties.complex;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.widget.model.AdvancedConfigurationNode;
import com.bichler.astudio.utils.internationalization.CustomString;

public class OPCUAGroupPropertySource implements IPropertySource {
	private static final String PROPERTY_GROUP = "p_group";
	// private static final String PROPERTY_GROUP_NAME = "p_groupname";
	private AdvancedConfigurationNode adapter;

	public OPCUAGroupPropertySource(AdvancedConfigurationNode adapter) {
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
				// new PropertyDescriptor(PROPERTY_GROUP_NAME, "Name"),
				new PropertyDescriptor(PROPERTY_GROUP,
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.id")) };
		return pd;
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (PROPERTY_GROUP.equals(id)) {
			return getAdapter().getGroupId();
		}
		// if (PROPERTY_GROUP_NAME.equals(id)) {
		// return getAdapter().getGroupName();
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
