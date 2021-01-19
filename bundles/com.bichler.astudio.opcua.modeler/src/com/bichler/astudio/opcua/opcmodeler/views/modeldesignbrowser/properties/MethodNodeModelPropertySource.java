package com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.properties;

import opc.sdk.core.node.MethodNode;

import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;
import com.bichler.astudio.utils.internationalization.CustomString;

public class MethodNodeModelPropertySource extends BrowserModelNodeModelPropertySource {
	protected static final String PROPERTY_EXECUTEABLE = "p_executeable";
	protected static final String PROPERTY_USEREXECUTEABLE = "p_userexecuteable";

	public MethodNodeModelPropertySource(BrowserModelNode adapter) {
		super(adapter);
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (PROPERTY_EXECUTEABLE.equals(id)) {
			return ((MethodNode) getAdapter().getNode()).getExecutable();
		}
		if (PROPERTY_USEREXECUTEABLE.equals(id)) {
			return ((MethodNode) getAdapter().getNode()).getUserExecutable();
		}
		return super.getPropertyValue(id);
	}

	@Override
	protected void setDescriptors() {
		super.setDescriptors();
		this.descriptors.add(new PropertyDescriptor(PROPERTY_EXECUTEABLE, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorMethodPart.lbl_executeable.text")));
		this.descriptors.add(new PropertyDescriptor(PROPERTY_USEREXECUTEABLE, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorMethodPart.lbl_userExecuteable.text")));
	}
}
