package com.bichler.astudio.opcua.properties;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.properties.driver.AbstractDriverPropertySource;
import com.bichler.astudio.opcua.widget.NodeToTrigger;
import com.bichler.astudio.utils.internationalization.CustomString;

public class OPCUATriggerNodesPropertySource extends AbstractDriverPropertySource {
	private static final String PROPERTY_ACTIVE = "p_active";
	private static final String PROPERTY_DATATYPE = "p_datatype";
	private static final String PROPERTY_INDEX = "p_index";
	private static final String PROPERTY_NAME = "p_name";
	private static final String PROPERTY_NODEID = "p_nodeid";
	private NodeToTrigger adaptable;

	public OPCUATriggerNodesPropertySource(NodeToTrigger adaptable) {
		this.adaptable = adaptable;
	}

	@Override
	public Object getEditableValue() {
		return null;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] descriptors = new IPropertyDescriptor[] {
				new PropertyDescriptor(PROPERTY_ACTIVE,
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.active")),
				new PropertyDescriptor(PROPERTY_NODEID,
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.nodeid")),
				new PropertyDescriptor(PROPERTY_NAME,
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.name")),
				new PropertyDescriptor(PROPERTY_INDEX,
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.index")),
				new PropertyDescriptor(PROPERTY_DATATYPE,
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "property.datatype")) };
		return descriptors;
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (PROPERTY_ACTIVE.equals(id)) {
			return this.adaptable.active;
		}
		if (PROPERTY_NODEID.equals(id)) {
			return this.adaptable.nodeId;
		}
		if (PROPERTY_NAME.equals(id)) {
			return this.adaptable.displayname;
		}
		if (PROPERTY_INDEX.equals(id)) {
			return this.adaptable.index;
		}
		if (PROPERTY_DATATYPE.equals(id)) {
			return getDataTypeText(this.adaptable.nodeId);
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
