package com.bichler.astudio.connections;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.bichler.astudio.filesystem.IFileSystem;

public class ConnectionsContentProvider implements ITreeContentProvider {

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
		
		return null;// ConnectionsActivator.hostManager.getStudioOPCUAConnections().getConnections().values().toArray();

	}

	@Override
	public Object[] getChildren(Object parentElement) {
		
		if(!(parentElement instanceof IFileSystem)) {
			
			return null; // return ConnectionsActivator.hostManager.getStudioOPCUAConnections().getConnections().values().toArray();
		}
		
		return new Object[0];
	}

	@Override
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return this.getChildren(element).length > 0;
	}

}
