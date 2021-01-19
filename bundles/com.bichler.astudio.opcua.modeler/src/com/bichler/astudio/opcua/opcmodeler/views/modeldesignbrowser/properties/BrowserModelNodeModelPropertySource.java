package com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.properties;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;
import com.bichler.astudio.utils.internationalization.CustomString;

public abstract class BrowserModelNodeModelPropertySource extends AbstractDesignerPropertySource {
	public BrowserModelNodeModelPropertySource(BrowserModelNode adapter) {
		super(adapter);
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return this.descriptors.toArray(new IPropertyDescriptor[0]);
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (PROPERTY_BROWSENAME.equals(id)) {
			return getAdapter().getNode().getBrowseName().toString();
		}
		if (PROPERTY_DISPLAYNAME.equals(id)) {
			return getAdapter().getNode().getDisplayName().toString();
		}
		if (PROPERTY_NODECLASS.equals(id)) {
			return getAdapter().getNode().getNodeClass().name();
		}
		if (PROPERTY_NODEID.equals(id)) {
			return getAdapter().getNode().getNodeId();
		}
		if (PROPERTY_USERWRITEMASK.equals(id)) {
			return getAdapter().getNode().getUserWriteMask();
		}
		if (PROPERTY_WRITEMASK.equals(id)) {
			return getAdapter().getNode().getWriteMask();
		}
		return null;
	}

	@Override
	protected void setDescriptors() {
		this.descriptors.add(new PropertyDescriptor(PROPERTY_BROWSENAME,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "CREATEVARIABLEDIALOG_")));
		this.descriptors.add(new PropertyDescriptor(PROPERTY_DISPLAYNAME, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "CreateVariableDialog.lbl_displayName.text")));
		this.descriptors.add(new PropertyDescriptor(PROPERTY_NODECLASS,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.nodeclass")));
		this.descriptors.add(new PropertyDescriptor(PROPERTY_NODEID,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.id")));
		this.descriptors.add(new PropertyDescriptor(PROPERTY_USERWRITEMASK, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "CreateVariableDialog.lbl_userWriteMask.text")));
		this.descriptors.add(new PropertyDescriptor(PROPERTY_WRITEMASK, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "CreateVariableDialog.lbl_writeMask.text")));
	}
}
