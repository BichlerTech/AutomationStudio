package com.bichler.astudio.opcua.properties;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.bichler.astudio.opcua.properties.complex.OPCUAAddonPropertySource;
import com.bichler.astudio.opcua.properties.complex.OPCUAConfigPropertySource;
import com.bichler.astudio.opcua.properties.complex.OPCUACounterPropertySource;
import com.bichler.astudio.opcua.properties.complex.OPCUADevicePropertySource;
import com.bichler.astudio.opcua.properties.complex.OPCUAGroupPropertySource;
import com.bichler.astudio.opcua.properties.complex.OPCUAMeterPropertySource;
import com.bichler.astudio.opcua.widget.model.AdvancedConfigurationNode;
import com.bichler.astudio.opcua.widget.model.AdvancedSectionType;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.opcua.OPCUAActivator;

public class OPCUAAdvancedConfigurationModelPropertySource extends AbstractAdvancedPropertySource {

	private static final String PROPERTY_CONFIG_NAME = "p_confname";
	private static final String PROPERTY_CMPLX_ADDON = "p_cmplx_addon";
	private static final String PROPERTY_CMPLX_CONFIG = "p_cmplx_config";
	private static final String PROPERTY_CMPLX_DEVICE = "p_cmplx_device";
	private static final String PROPERTY_CMPLX_GROUP = "p_cmplx_group";
	private static final String PROPERTY_CMPLX_METER = "p_cmplx_meter";
	private static final String PROPERTY_ACTIVE = "p_active";

	private static final String PROPERTY_INDEX = "p_index";
	private static final String PROPERTY_VALUE = "p_value";

	public OPCUAAdvancedConfigurationModelPropertySource(AdvancedConfigurationNode adapter) {
		super(adapter);
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] pd = new IPropertyDescriptor[0];

		AdvancedSectionType type = getAdapter().getType();
		switch (type) {
		// counter
		case Counter:
			if (getAdapter().isDevice()) {
				return new IPropertyDescriptor[] { new PropertyDescriptor(PROPERTY_CONFIG_NAME,
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.device")) };
			}

			if (getAdapter().isGroup()) {
				return new IPropertyDescriptor[] { new PropertyDescriptor(PROPERTY_CONFIG_NAME,
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.group")) };
			}

			PropertyDescriptor counterPD = new PropertyDescriptor(PROPERTY_CMPLX_CONFIG,
					CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.counter"));
			counterPD.setLabelProvider(new LabelProvider() {

				@Override
				public String getText(Object element) {
					if (element instanceof OPCUACounterPropertySource) {

						return getAdapter().getCounter();
					}
					return super.getText(element);
				}

			});

			pd = new IPropertyDescriptor[] { counterPD };
			break;
		// device configuration
		case DeviceConfig:
			PropertyDescriptor cmplx_dev = new PropertyDescriptor(PROPERTY_CMPLX_DEVICE,
					CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.device"));
			cmplx_dev.setLabelProvider(new LabelProvider() {

				@Override
				public String getText(Object element) {
					if (getAdapter() instanceof AdvancedConfigurationNode) {
						return getAdapter().getDeviceName();
					}
					return super.getText(element);
				}

			});

			pd = new IPropertyDescriptor[] { cmplx_dev,
					new PropertyDescriptor(PROPERTY_VALUE,
							CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.index")),
					new PropertyDescriptor(PROPERTY_ACTIVE,
							CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.active")) };
			break;
		// device mapping
		case DeviceMapping:
			PropertyDescriptor cmplx_addon = new PropertyDescriptor(PROPERTY_CMPLX_ADDON,
					CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.addon"));
			cmplx_addon.setLabelProvider(new LabelProvider() {

				@Override
				public String getText(Object element) {
					if (getAdapter() instanceof AdvancedConfigurationNode) {
						return getAdapter().getAddonName();
					}

					return super.getText(element);
				}

			});
			PropertyDescriptor cmplx_device = new PropertyDescriptor(PROPERTY_CMPLX_DEVICE,
					CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.device"));
			cmplx_device.setLabelProvider(new LabelProvider() {

				@Override
				public String getText(Object element) {
					if (getAdapter() instanceof AdvancedConfigurationNode) {
						return getAdapter().getDeviceName();
					}
					return super.getText(element);
				}

			});
			PropertyDescriptor cmplx_group = new PropertyDescriptor(PROPERTY_CMPLX_GROUP,
					CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.group"));
			cmplx_group.setLabelProvider(new LabelProvider() {

				@Override
				public String getText(Object element) {
					if (getAdapter() instanceof AdvancedConfigurationNode) {
						return getAdapter().getGroupName();
					}
					return super.getText(element);
				}

			});
			PropertyDescriptor cmplx_meter = new PropertyDescriptor(PROPERTY_CMPLX_METER,
					CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.meter"));
			cmplx_meter.setLabelProvider(new LabelProvider() {

				@Override
				public String getText(Object element) {
					if (getAdapter() instanceof AdvancedConfigurationNode) {
						return getAdapter().getMeterName();
					}
					return super.getText(element);
				}

			});

			pd = new IPropertyDescriptor[] { cmplx_addon, cmplx_device, cmplx_group, cmplx_meter,
					new PropertyDescriptor(PROPERTY_VALUE,
							CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.value")) };
			break;
		// start configuration
		case StartConfig:
			PropertyDescriptor cmplx_startconfig = new PropertyDescriptor(PROPERTY_CMPLX_CONFIG,
					CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.configuration"));
			cmplx_startconfig.setLabelProvider(new LabelProvider() {

				@Override
				public String getText(Object element) {
					if (getAdapter() instanceof AdvancedConfigurationNode) {
						return getAdapter().getConfigNodeName();
					}
					return super.getText(element);
				}

			});

			PropertyDescriptor cmplx_deviceconfig = new PropertyDescriptor(PROPERTY_CMPLX_DEVICE,
					CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.device"));
			cmplx_deviceconfig.setLabelProvider(new LabelProvider() {

				@Override
				public String getText(Object element) {
					if (getAdapter() instanceof AdvancedConfigurationNode) {
						return getAdapter().getDeviceName();
					}
					return super.getText(element);
				}

			});

			pd = new IPropertyDescriptor[] { cmplx_startconfig, cmplx_deviceconfig,
					new PropertyDescriptor(PROPERTY_INDEX,
							CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.index")),
					new PropertyDescriptor(PROPERTY_VALUE,
							CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.value")),
					new PropertyDescriptor(PROPERTY_ACTIVE,
							CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.active")) };
			break;
		}

		return pd;
	}

	@Override
	public Object getPropertyValue(Object id) {
		AdvancedSectionType type = getAdapter().getType();
		switch (type) {
		case Counter:
			if (getAdapter().isState()) {
				OPCUACounterPropertySource cps = new OPCUACounterPropertySource(getAdapter());
				return cps;
			} else {
				if (PROPERTY_CONFIG_NAME.equals(id)) {
					if (getAdapter().isGroup()) {
						return getAdapter().getGroupName();
					}

					if (getAdapter().isDevice()) {
						return getAdapter().getDeviceName();
					}
				}
			}

		case DeviceConfig:
			// if (PROPERTY_DEVICE.equals(id)) {
			// return getAdapter().getDeviceId();
			// }
			// if (PROPERTY_DEVICE_NAME.equals(id)) {
			// return getAdapter().getDeviceName();
			// }
			if (PROPERTY_CMPLX_DEVICE.equals(id)) {
				return new OPCUADevicePropertySource(getAdapter());
			}

			if (PROPERTY_VALUE.equals(id)) {
				return getAdapter().getValue();
			}
			if (PROPERTY_ACTIVE.equals(id)) {
				return getAdapter().isActive();
			}
			break;
		case DeviceMapping:
			if (PROPERTY_VALUE.equals(id)) {
				return getAdapter().getValue();
			}

			if (PROPERTY_CMPLX_ADDON.equals(id)) {
				return new OPCUAAddonPropertySource(getAdapter());
			}

			if (PROPERTY_CMPLX_DEVICE.equals(id)) {
				return new OPCUADevicePropertySource(getAdapter());
			}

			if (PROPERTY_CMPLX_GROUP.equals(id)) {
				return new OPCUAGroupPropertySource(getAdapter());
			}

			if (PROPERTY_CMPLX_METER.equals(id)) {
				return new OPCUAMeterPropertySource(getAdapter());
			}
			break;
		case StartConfig:
			if (PROPERTY_CMPLX_CONFIG.equals(id)) {
				return new OPCUAConfigPropertySource(getAdapter());
			}
			if (PROPERTY_CMPLX_DEVICE.equals(id)) {
				return new OPCUADevicePropertySource(getAdapter());
			}
			if (PROPERTY_INDEX.equals(id)) {
				return getAdapter().getIndex();
			}
			if (PROPERTY_VALUE.equals(id)) {
				return getAdapter().getValue();
			}
			if (PROPERTY_ACTIVE.equals(id)) {
				return getAdapter().isActive();
			}
			break;
		}

		return null;
	}

	@Override
	public AdvancedConfigurationNode getAdapter() {
		return (AdvancedConfigurationNode) super.getAdapter();
	}
}
