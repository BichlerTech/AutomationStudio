package com.bichler.astudio.opcua.opcmodeler.dialogs.utils;

import opc.sdk.core.node.ReferenceTypeNode;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.opcfoundation.ua.core.ReferenceNode;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;

public class ReferenceTableLabelProvider extends LabelProvider implements ITableLabelProvider {
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		switch (columnIndex) {
		// reference type
		case 0:
			ReferenceTypeNode referenceTypeNode = (ReferenceTypeNode) ServerInstance
					.getNode(((ReferenceNode) element).getReferenceTypeId());
			String display = null;
			if (((ReferenceNode) element).getIsInverse() && !referenceTypeNode.getSymmetric()) {
				display = referenceTypeNode.getInverseName().getText();
			} else {
				display = referenceTypeNode.getBrowseName().getName();
			}
			return display;
		// is Inverse
		case 1:
			return ((ReferenceNode) element).getIsInverse().toString();
		// target name
		case 2:
			try {
				return ServerInstance.getNode(((ReferenceNode) element).getTargetId()).getDisplayName().getText();
			} catch (NullPointerException npe) {
				return "";
			}
		default:
			break;
		}
		return null;
	}
}
