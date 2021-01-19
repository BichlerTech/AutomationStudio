package com.bichler.astudio.opcua.opcmodeler.wizards.opc.validation.model;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.ReferenceDescription;

public class ValidationModelContentProvider implements ITreeContentProvider {
	@Override
	public void dispose() {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof ValidationModel) {
			ValidationModelContentFactory.updateValidationModelChildren((ValidationModel) inputElement);
			return ((ValidationModel) inputElement).getChildren();
		}
		return new ValidationModel[0];
	}

	@Override
	public Object[] getChildren(Object parent) {
		if (parent instanceof ValidationModel) {
			ValidationModelContentFactory.updateValidationModelChildren((ValidationModel) parent);
			return ((ValidationModel) parent).getChildren();
		}
		return new ValidationModel[0];
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof ValidationModel) {
			return ((ValidationModel) element).getParent();
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		NodeId nodeId = ((ValidationModel) element).getNodeId();
		ReferenceDescription[] results = ValidationModelContentFactory.browse(nodeId);
		return results.length > 0;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
	}
}
