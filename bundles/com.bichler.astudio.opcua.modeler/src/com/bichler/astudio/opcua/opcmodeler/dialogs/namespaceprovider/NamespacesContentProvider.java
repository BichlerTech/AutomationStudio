package com.bichler.astudio.opcua.opcmodeler.dialogs.namespaceprovider;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class NamespacesContentProvider implements IStructuredContentProvider {
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
	}

	@Override
	public Object[] getElements(Object inputElement) {
		String[] namespaces = (String[]) inputElement;
		List<String[]> elements = new ArrayList<String[]>();
		for (int i = 0; i < namespaces.length; i++) {
			elements.add(new String[] { i + "", namespaces[i] });
		}
		return elements.toArray();
	}
}
