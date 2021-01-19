package com.bichler.astudio.opcua.components.ui.serverbrowser.providers;

import java.util.ArrayList;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class UAServerContentProvider implements IStructuredContentProvider {

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	@SuppressWarnings("unchecked")
	@Override
	public Object[] getElements(Object inputElement) {
		if(inputElement instanceof ArrayList<?>) {
			return ((ArrayList<UAServerModelNode>)inputElement).toArray();
		}
		return new Object[0];
	}

}
