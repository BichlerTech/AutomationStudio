package com.bichler.astudio.connections;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.connections.utils.ConnectionsSharedImages;

public class ConnectionsLabelProvider extends LabelProvider {

	@Override
	public Image getImage(Object element) {
		if(element instanceof IFileSystem) {
			if(((IFileSystem)element).isconnected()) {
				return ConnectionsSharedImages.getImage(ConnectionsSharedImages.ICON_HOSTCONNECTION_GREEN);
			}
			else {
				return ConnectionsSharedImages.getImage(ConnectionsSharedImages.ICON_HOSTCONNECTION);
			}
		}
		return null;
	}

	@Override
	public String getText(Object element) {
		if(element instanceof IFileSystem) {
			return ((IFileSystem)element).getHostName();
		}
		return super.getText(element);
	}
	
}
