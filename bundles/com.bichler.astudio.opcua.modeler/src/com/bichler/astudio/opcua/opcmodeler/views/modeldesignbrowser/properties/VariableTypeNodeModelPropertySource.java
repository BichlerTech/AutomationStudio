package com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.properties;

import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;
import com.bichler.astudio.utils.internationalization.CustomString;

import opc.sdk.core.node.VariableTypeNode;

public class VariableTypeNodeModelPropertySource extends BrowserModelNodeModelPropertySource {
	protected static final String PROPERTY_VALUE = "p_value";
	protected static final String PROPERTY_VALUERANK = "p_valuerank";
	protected static final String PROPERTY_DATATYPE = "p_datatype";
	protected static final String PROPERTY_ARRAYDIMENSION = "p_arraydimension";
	protected static final String PROPERTY_ISABSTRACT = "p_accesslevel";

	public VariableTypeNodeModelPropertySource(BrowserModelNode adapter) {
		super(adapter);
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (PROPERTY_DATATYPE.equals(id)) {
			return ((VariableTypeNode) getAdapter().getNode()).getDataType();
		}
		if (PROPERTY_VALUE.equals(id)) {
			return ((VariableTypeNode) getAdapter().getNode()).getValue();
		}
		if (PROPERTY_VALUERANK.equals(id)) {
			return ((VariableTypeNode) getAdapter().getNode()).getValueRank();
		}
		if (PROPERTY_ARRAYDIMENSION.equals(id)) {
			StringBuffer ret = new StringBuffer();
			String del = "";
			UnsignedInteger[] uints = ((VariableTypeNode) getAdapter().getNode()).getArrayDimensions();
			if (uints != null) {
				ret.append("[");
				for (UnsignedInteger uint : uints) {
					ret.append(del);
					ret.append(uint);
					del = ",";
				}
				ret.append("]");
				return ret;
			}

			return "";
		}
		if (PROPERTY_ISABSTRACT.equals(id)) {
			return ((VariableTypeNode) getAdapter().getNode()).getIsAbstract();
		}
		return super.getPropertyValue(id);
	}

	@Override
	protected void setDescriptors() {
		super.setDescriptors();
		this.descriptors.add(new PropertyDescriptor(PROPERTY_DATATYPE, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorVariableTypePart.lbl_dataType.text")));
		this.descriptors.add(new PropertyDescriptor(PROPERTY_VALUE, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorVariableTypePart.lbl_value.text")));
		this.descriptors.add(new PropertyDescriptor(PROPERTY_VALUERANK, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorVariableTypePart.lbl_valueRank.text")));
		this.descriptors.add(new PropertyDescriptor(PROPERTY_ARRAYDIMENSION, CustomString.getString(
				Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorVariableTypePart.lbl_arrayDimensions.text")));
		this.descriptors.add(new PropertyDescriptor(PROPERTY_ISABSTRACT, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorDataTypePart.lbl_isAbstract.text")));
	}
}
