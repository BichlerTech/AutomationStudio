package com.bichler.astudio.opcua.properties.complex;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.bichler.astudio.opcua.widget.model.AdvancedConfigurationNode;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.opcua.OPCUAActivator;

public class OPCUAMeterPropertySource implements IPropertySource {

	private static final String PROPERTY_METER = "p_meter";
//	private static final String PROPERTY_METER_NAME = "p_metername";

	private AdvancedConfigurationNode adapter;

	public OPCUAMeterPropertySource(AdvancedConfigurationNode adapter) {
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
//				new PropertyDescriptor(PROPERTY_METER_NAME, "Name"),
				new PropertyDescriptor(PROPERTY_METER,
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.id")) };

		return pd;
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (PROPERTY_METER.equals(id)) {
			return getAdapter().getMeterId();
		}
//		if (PROPERTY_METER_NAME.equals(id)) {
//			return getAdapter().getMeterName();
//		}

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
