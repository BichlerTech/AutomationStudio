package com.bichler.astudio.opcua.opcmodeler.dialogs;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class NodeValueContentProvider implements IStructuredContentProvider {
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement == null) {
			return null;
		}
		String[] items = (String[]) inputElement;
		return items;
	}
}
