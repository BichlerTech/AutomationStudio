package com.bichler.astudio.opcua.components.ui.dialogs.providers;

import org.opcfoundation.ua.core.Identifiers;

import com.bichler.astudio.opcua.components.ui.dialogs.OPCRemoteTreeViewerItem;
import com.bichler.astudio.opcua.components.ui.serverbrowser.providers.UAServerModelNode;

public class OPCUARemoteBrowserProvider {

	/**
	 * 
	 */
	private OPCRemoteTreeViewerItem root = null;

	/**
	 * 
	 */
	public OPCUARemoteBrowserProvider(UAServerModelNode server) {
		this.root = new OPCRemoteTreeViewerItem("Objects");

		this.root.setNodeId(Identifiers.ObjectsFolder);
		this.root.setTypeDefinition(Identifiers.FolderType);
		this.root.setServer(server);
	}

	/**
	 * 
	 * @return root input object of the tree.
	 */
	public OPCRemoteTreeViewerItem getBrowser() {
		return this.root;
	}
}
