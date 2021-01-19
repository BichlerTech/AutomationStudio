package com.bichler.astudio.log.view.provider;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.bichler.astudio.log.server.core.ASLogCollection;


public class ASLogContentProvider implements IStructuredContentProvider {

	@Override
	public void dispose() {
		// no code
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// no code
	}

	@Override
	public Object[] getElements(Object inputElement) {
	  ASLogCollection logs = (ASLogCollection) inputElement;
		return logs.toArray();
	}
}
