package com.bichler.astudio.editor.siemens;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class SiemensModelContentProvider implements IStructuredContentProvider {
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof List) {
			return ((List) inputElement).toArray();
		}
		return new Object[0];
	}
}
