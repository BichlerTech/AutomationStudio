package com.bichler.astudio.opcua.opcmodeler.views.modeldesignedit;

import opc.sdk.core.application.OPCEntry;
import opc.sdk.core.enums.NodeAttributeName;

import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.Variant;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class TableLabelProvider implements ITableLabelProvider {
	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getColumnText(Object element, int columnIndex) {
		switch (columnIndex) {
		case 0:
			if (element != null) {
				return ((OPCEntry<String, Object>) element).getKey();
			}
			break;
		case 1:
			if (((OPCEntry<String, Object>) element).getValue() != null) {
				// value attribute of a variable (type) node
				if (NodeAttributeName.Value.name().equals(((OPCEntry<String, Object>) element).getKey())) {
					// value is null
					if (((Variant) ((OPCEntry<String, Object>) element).getValue()).isEmpty()) {
						return "<null>";
					}
					// check if it is an array then it should be its class
					// displayed
					else if (((Variant) ((OPCEntry<String, Object>) element).getValue()).isArray()) {
						return ((Variant) ((OPCEntry<String, Object>) element).getValue()).getCompositeClass() + "["
								+ ((Object[]) ((Variant) ((OPCEntry<String, Object>) element).getValue())
										.getValue()).length
								+ "]";
					}
					// extension object (complex item)
					else if (((Variant) ((OPCEntry<String, Object>) element).getValue())
							.getValue() instanceof ExtensionObject) {
						return "ExtensionObject";
					}
					// otherwise display the value
					else {
						return ((Variant) ((OPCEntry<String, Object>) element).getValue()).getValue().toString();
					}
				} else if (NodeAttributeName.ArrayDimensions.name()
						.equals(((OPCEntry<String, Object>) element).getKey())) {
					Integer arrayDimension = ((Object[]) ((OPCEntry<String, Object>) element).getValue()).length;
					return arrayDimension.toString();
				}
				// else just display the toString representation of the value
				return ((OPCEntry<String, Object>) element).getValue().toString();
			}
			return "<null>";
		default:
			return null;
		}
		return null;
	}
}
