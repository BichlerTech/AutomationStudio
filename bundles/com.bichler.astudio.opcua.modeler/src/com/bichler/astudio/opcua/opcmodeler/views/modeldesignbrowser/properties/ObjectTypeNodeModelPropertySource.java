package com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.properties;

import opc.sdk.core.node.ObjectTypeNode;

import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;
import com.bichler.astudio.utils.internationalization.CustomString;

public class ObjectTypeNodeModelPropertySource extends BrowserModelNodeModelPropertySource {
	protected static final String PROPERTY_ISABSTRACT = "p_isabstract";

	public ObjectTypeNodeModelPropertySource(BrowserModelNode adapter) {
		super(adapter);
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (PROPERTY_ISABSTRACT.equals(id)) {
			return ((ObjectTypeNode) getAdapter().getNode()).getIsAbstract();
		}
		return super.getPropertyValue(id);
	}

	@Override
	protected void setDescriptors() {
		super.setDescriptors();
		this.descriptors.add(new PropertyDescriptor(PROPERTY_ISABSTRACT, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorDataTypePart.lbl_isAbstract.text")));
	}
}
