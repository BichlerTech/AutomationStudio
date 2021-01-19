package com.bichler.astudio.opcua.opcmodeler.dialogs.namespaceprovider;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.opcfoundation.ua.common.NamespaceTable;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;

public class NamespacesIndexContentProvider implements IStructuredContentProvider {
	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		NamespaceTable table = NamespaceTable
				.createFromArray(ServerInstance.getInstance().getServerInstance().getNamespaceUris().toArray());
		String[] namespaces = (String[]) inputElement;
		List<String[]> elements = new ArrayList<String[]>();
		for (int i = 0; i < namespaces.length; i++) {
			int index4ns = table.getIndex(namespaces[i]);
			elements.add(new String[] { index4ns + "", namespaces[i] });
		}
		return elements.toArray();
	}
}
