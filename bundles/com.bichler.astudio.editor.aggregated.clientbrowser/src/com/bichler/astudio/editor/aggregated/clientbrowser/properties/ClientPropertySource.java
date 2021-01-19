package com.bichler.astudio.editor.aggregated.clientbrowser.properties;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.bichler.astudio.editor.aggregated.clientbrowser.Activator;
import com.bichler.astudio.editor.aggregated.clientbrowser.model.AbstractCCModel;
import com.bichler.astudio.utils.internationalization.CustomString;

public class ClientPropertySource implements IPropertySource {

	private AbstractCCModel adaptable;
	private String PROPERTY_DISPLAYNAME = "p_display";
	private String PROPERTY_NODEID = "p_id";
	private String PROPERTY_DESCRIPTION = "p_desc";
	private String PROPERTY_NODECLASS = "p_nodeclass";

	public ClientPropertySource(AbstractCCModel adaptable) {
		this.adaptable = adaptable;
	}

	@Override
	public Object getEditableValue() {
		return null;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] descriptors = new IPropertyDescriptor[] {
				new PropertyDescriptor(PROPERTY_DISPLAYNAME,
						CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
								"aggregated.clientbrowser.property.displayname")),
				new PropertyDescriptor(PROPERTY_NODEID,
						CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
								"aggregated.clientbrowser.property.nodeid")),
				new PropertyDescriptor(PROPERTY_DESCRIPTION,
						CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
								"aggregated.clientbrowser.property.description")),
				new PropertyDescriptor(PROPERTY_NODECLASS, CustomString.getString(
						Activator.getDefault().RESOURCE_BUNDLE, "aggregated.clientbrowser.property.nodeclass")),
				// new PropertyDescriptor(PROPERTY_DATATYPE, "Lokal Datatype"),
				// new PropertyDescriptor(PROPERTY_ACCESSLEVEL, "Lokal
				// AccessLevel"),
				// new PropertyDescriptor(PROPERTY_USERACCESSLEVEL,
				// "Lokal UserAccessLevel"),
				// new PropertyDescriptor(PROPERTY_TARGET_DISPLAYNAME,
				// "Remote Name"),
				// new PropertyDescriptor(PROPERTY_TARGET_NODEID, "Remote
				// NodeId"),
				// new PropertyDescriptor(PROPERTY_TARGET_DESCRIPTION,
				// "Remote Description"),
				// new PropertyDescriptor(PROPERTY_TARGET_DATATYPE,
				// "Remote Datentyp"),
				// new PropertyDescriptor(PROPERTY_TARGET_USERACCESSLEVEL,
				// "Remote UserAccessLevel"),
				// new PropertyDescriptor(PROPERTY_TARGET_ACCESSLEVEL,
				// "Remote Beschreibung")
		};
		return descriptors;
	}

	@Override
	public Object getPropertyValue(Object id) {

		if (PROPERTY_DISPLAYNAME.equals(id)) {
			return this.adaptable.getDisplayname();
		}
		if (PROPERTY_NODEID.equals(id)) {
			return this.adaptable.getNodeId();
		}
		if (PROPERTY_DESCRIPTION.equals(id)) {
			return "] desc { ";
		}
		if (PROPERTY_NODECLASS.equals(id)) {
			return this.adaptable.getNodeClass();
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
