package com.bichler.astudio.editor.siemens;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.bichler.astudio.editor.siemens.model.AbstractSiemensNode;

public class SiemensModelTreeContentProvider implements ITreeContentProvider {
	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof AbstractSiemensNode) {
			return ((AbstractSiemensNode) parentElement).getChildren();
		}
		return new Object[0];
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return this.getChildren(element).length > 0;
	}
}
