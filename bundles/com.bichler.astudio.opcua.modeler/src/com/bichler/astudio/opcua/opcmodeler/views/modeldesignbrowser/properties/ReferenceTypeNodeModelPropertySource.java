package com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.properties;

import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;
import com.bichler.astudio.utils.internationalization.CustomString;

import opc.sdk.core.node.ReferenceTypeNode;

public class ReferenceTypeNodeModelPropertySource extends BrowserModelNodeModelPropertySource {
	protected static final String PROPERTY_ISABSTRACT = "p_isabstract";
	protected static final String PROPERTY_SYMMETRIC = "p_symmetric";
	protected static final String PROPERTY_INVERSENAME = "p_inversename";

	public ReferenceTypeNodeModelPropertySource(BrowserModelNode adapter) {
		super(adapter);
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (PROPERTY_ISABSTRACT.equals(id)) {
			return ((ReferenceTypeNode) getAdapter().getNode()).getIsAbstract();
		}
		if (PROPERTY_SYMMETRIC.equals(id)) {
			return ((ReferenceTypeNode) getAdapter().getNode()).getSymmetric();
		}
		if (PROPERTY_SYMMETRIC.equals(id)) {
			return ((ReferenceTypeNode) getAdapter().getNode()).getInverseName();
		}
		return super.getPropertyValue(id);
	}

	@Override
	protected void setDescriptors() {
		super.setDescriptors();
		this.descriptors.add(new PropertyDescriptor(PROPERTY_ISABSTRACT, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorDataTypePart.lbl_isAbstract.text")));
		this.descriptors.add(new PropertyDescriptor(PROPERTY_SYMMETRIC, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorReferenceTypePart.lbl_symmetric.text")));
		this.descriptors.add(new PropertyDescriptor(PROPERTY_INVERSENAME, CustomString.getString(
				Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorReferenceTypePart.lbl_inverseName.text")));
	}
}
