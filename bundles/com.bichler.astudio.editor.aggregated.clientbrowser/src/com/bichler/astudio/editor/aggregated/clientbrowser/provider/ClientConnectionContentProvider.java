package com.bichler.astudio.editor.aggregated.clientbrowser.provider;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.bichler.astudio.editor.aggregated.clientbrowser.model.AbstractCCModel;

public class ClientConnectionContentProvider implements ITreeContentProvider {

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

		if (parentElement instanceof AbstractCCModel) {
			((AbstractCCModel) parentElement).prepareFetchChildren();
			((AbstractCCModel) parentElement).fetchChildren();
			return ((AbstractCCModel) parentElement).getChildren();
		}

		return new Object[0];
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof AbstractCCModel) {
			return ((AbstractCCModel) element).getParent();
		}

		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}

}
