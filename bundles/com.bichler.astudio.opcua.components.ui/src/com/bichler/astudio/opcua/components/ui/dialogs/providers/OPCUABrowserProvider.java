package com.bichler.astudio.opcua.components.ui.dialogs.providers;

import opc.sdk.core.node.Node;
import opc.sdk.server.core.OPCInternalServer;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Identifiers;

import com.bichler.astudio.opcua.components.ui.dialogs.OPCTreeViewerItem;


public class OPCUABrowserProvider {

	/**
	 * 
	 */
	private OPCTreeViewerItem root = null;

	/**
	 * @param startId
	 * 
	 */
	public OPCUABrowserProvider(OPCInternalServer server, NodeId startId) {
		Node node = server.getAddressSpaceManager().getNodeById(startId);
		String displayname = node.getDisplayName().getText();
		ExpandedNodeId typeDefiniition = node.findTarget(Identifiers.HasTypeDefinition, true);

		this.root = new OPCTreeViewerItem(displayname);
		this.root.setNodeId(startId);
		try {
			this.root.setTypeDefinition(server.getNamespaceUris().toNodeId(typeDefiniition));
		} catch (ServiceResultException sre) {
			sre.printStackTrace();
		}
		this.root.setServer(server);
	}

	/**
	 * 
	 * @return root input object of the tree.
	 */
	public OPCTreeViewerItem getBrowser() {
		return this.root;
	}
}
