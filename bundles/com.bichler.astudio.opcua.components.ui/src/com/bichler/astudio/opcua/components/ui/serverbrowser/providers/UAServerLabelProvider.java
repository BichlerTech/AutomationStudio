package com.bichler.astudio.opcua.components.ui.serverbrowser.providers;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.bichler.astudio.components.ui.ComponentsSharedImages;
import com.bichler.astudio.opcua.components.ui.serverbrowser.OPCUAServerModelNode;

public class UAServerLabelProvider extends LabelProvider {

	private Image newServer = null;
	private Image disconnectedServer = null;
	private Image connectedServer = null;

	/**
	 * constructor, create label images
	 */
	public UAServerLabelProvider() {
		
		//this.newServer = ComponentsSharedImages.getImage(ComponentsSharedImages.ICON_ADD);

		this.disconnectedServer = ComponentsSharedImages.getImage(ComponentsSharedImages.ICON_HOSTCONNECTION);

		this.connectedServer = ComponentsSharedImages.getImage(ComponentsSharedImages.ICON_HOSTCONNECTION_GREEN);
	}

	@Override
	public Image getImage(Object element) {
		return getImageForLabel(element);
	}

	private Image getImageForLabel(Object element) {
		if (element instanceof UAServerModelNode) {
			if (((UAServerModelNode) element).isConnected()) {
				return this.connectedServer;
			} else {
				return this.disconnectedServer;
			}

		} else if (element instanceof String) {
			return this.newServer;
		} else if (element instanceof OPCUAServerModelNode) {
			return this.newServer;
		}

		return null;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof UAServerModelNode) {
			return ((UAServerModelNode) element).getDisplayName();
		} else if (element instanceof OPCUAServerModelNode) {
			return ((OPCUAServerModelNode) element).getDisplayName();
		} else if (element instanceof String) {
			return (String) element;
		}
		return null;
	}
}
