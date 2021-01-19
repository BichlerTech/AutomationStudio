package com.bichler.astudio.editor.aggregated.properties;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.bichler.astudio.opcua.properties.driver.AbstractDriverPropertySource;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.editor.aggregated.AggregatedActivator;
import com.bichler.astudio.editor.aggregated.model.AggregatedDpModelNode;

public class AggregatedPropertySource extends AbstractDriverPropertySource {

	private static final String PROPERTY_TARGET_ACCESSLEVEL = "p_t_access";
	private static final String PROPERTY_TARGET_USERACCESSLEVEL = "p_t_useraccess";
	private static final String PROPERTY_TARGET_DATATYPE = "p_t_datatype";
	private static final String PROPERTY_TARGET_DISPLAYNAME = "p_t_disp";
	private static final String PROPERTY_TARGET_NODEID = "p_t_id";
	private static final String PROPERTY_TARGET_DESCRIPTION = "p_t_desc";

	private AggregatedDpModelNode adaptable = null;

	public AggregatedPropertySource(AggregatedDpModelNode adaptable) {
		super();
		this.adaptable = adaptable;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return new IPropertyDescriptor[] {
				new PropertyDescriptor(PROPERTY_DISPLAYNAME,
						CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
								"com.bichler.astudio.editor.aggregated.propertyview.displayname")),
				new PropertyDescriptor(PROPERTY_NODEID,
						CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
								"com.bichler.astudio.editor.aggregated.dpeditor.viewer.columnname.servernodeid")),
				new PropertyDescriptor(PROPERTY_DESCRIPTION,
						CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
								"com.bichler.astudio.editor.aggregated.propertyview.description")),
				new PropertyDescriptor(PROPERTY_DATATYPE,
						CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
								"com.bichler.astudio.editor.aggregated.propertyview.datatype")),
				new PropertyDescriptor(PROPERTY_ACCESSLEVEL,
						CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
								"com.bichler.astudio.editor.aggregated.propertyview.accesslevel")),
				new PropertyDescriptor(PROPERTY_USERACCESSLEVEL,
						CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
								"com.bichler.astudio.editor.aggregated.propertyview.useraccesslevel")),
				new PropertyDescriptor(PROPERTY_TARGET_DISPLAYNAME,
						CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
								"com.bichler.astudio.editor.aggregated.propertyview.remotename")),
				new PropertyDescriptor(PROPERTY_TARGET_NODEID,
						CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
								"com.bichler.astudio.editor.aggregated.propertyview.remotenodeid")),
				new PropertyDescriptor(PROPERTY_TARGET_DESCRIPTION,
						CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
								"com.bichler.astudio.editor.aggregated.propertyview.remotedescription")),
				new PropertyDescriptor(PROPERTY_TARGET_DATATYPE,
						CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
								"com.bichler.astudio.editor.aggregated.propertyview.remotedatatype")),
				new PropertyDescriptor(PROPERTY_TARGET_USERACCESSLEVEL,
						CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
								"com.bichler.astudio.editor.aggregated.propertyview.remoteuseraccesslevel")),
				new PropertyDescriptor(PROPERTY_TARGET_ACCESSLEVEL,
						CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
								"com.bichler.astudio.editor.aggregated.propertyview.remoteaccesslevel")) };
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (PROPERTY_DISPLAYNAME.equals(id)) {
			return this.adaptable.getDPItem().getServerDisplayName();
		}
		if (PROPERTY_DESCRIPTION.equals(id)) {
			return this.adaptable.getDPItem().getServerBrowsePath();
		}

		if (PROPERTY_DATATYPE.equals(id)) {
			return getDataTypeText(this.adaptable.getDPItem().getServerNodeId());
		}

		if (PROPERTY_ACCESSLEVEL.equals(id)) {
			return getAccessLevel(this.adaptable.getDPItem().getServerNodeId());
		}

		if (PROPERTY_USERACCESSLEVEL.equals(id)) {
			return getUserAccessLevel(this.adaptable.getDPItem().getServerNodeId());
		}

		if (PROPERTY_NODEID.equals(id)) {
			return this.adaptable.getDPItem().getServerNodeId();
		}

		if (PROPERTY_TARGET_DISPLAYNAME.equals(id)) {
			return this.adaptable.getDPItem().getTargetDisplayName();
		}

		if (PROPERTY_TARGET_NODEID.equals(id)) {
			return this.adaptable.getDPItem().getTargetNodeId();
		}

		if (PROPERTY_TARGET_DESCRIPTION.equals(id)) {
			return this.adaptable.getDPItem().getTargetBrowsePath();
		}

		if (PROPERTY_TARGET_DATATYPE.equals(id)) {
			return getDataTypeText(this.adaptable.getDPItem().getTargetNodeId());
		}

		if (PROPERTY_TARGET_ACCESSLEVEL.equals(id)) {
			return getAccessLevel(this.adaptable.getDPItem().getTargetNodeId());
		}

		if (PROPERTY_TARGET_USERACCESSLEVEL.equals(id)) {
			return getUserAccessLevel(this.adaptable.getDPItem().getTargetNodeId());
		}

		return null;
	}

}
