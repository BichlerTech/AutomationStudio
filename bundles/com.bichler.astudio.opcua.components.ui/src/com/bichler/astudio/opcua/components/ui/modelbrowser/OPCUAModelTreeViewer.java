package com.bichler.astudio.opcua.components.ui.modelbrowser;

import opc.sdk.server.core.OPCInternalServer;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.opcua.components.ui.modelbrowser.nodes.OPCUABrowserModelNode;


public class OPCUAModelTreeViewer extends TreeViewer {

	public static final String ID = "com.hbsoft.comet.components.modelbrowser.CometUAModelTreeViewer";
	
	private OPCInternalServer internalServer = null;
	
	private NodeId startId = null;
	
	public OPCUAModelTreeViewer(Composite parent, int style) {
		super(parent, style);
	}
	
	public void fillTree() {
		
		OPCUABrowserModelNode node = new OPCUABrowserModelNode();
		
		node.setNode(internalServer.getAddressSpaceManager()
				.getNodeById(startId));
		this.setInput(node);
	}
	
	public OPCInternalServer getInternalServer() {
		return internalServer;
	}

	public void setInternalServer(OPCInternalServer internalServer) {
		this.internalServer = internalServer;
	}

	public NodeId getStartId() {
		return startId;
	}

	public void setStartId(NodeId startId) {
		this.startId = startId;
	}

	
	
}
