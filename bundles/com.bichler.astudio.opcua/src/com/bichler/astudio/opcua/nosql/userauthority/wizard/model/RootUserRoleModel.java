package com.bichler.astudio.opcua.nosql.userauthority.wizard.model;

import opc.sdk.server.core.OPCInternalServer;

import org.eclipse.jface.viewers.TreePath;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;

public class RootUserRoleModel extends AbstractOPCUserModel {

	private TreePath[] expandedElements = new TreePath[0];

	public RootUserRoleModel(String displayname, NodeClass nodeClass, OPCInternalServer opcInternalServer) {
		super(displayname, nodeClass, Identifiers.RootFolder);
		setServer(opcInternalServer);
	}

	public void setExpandedElements(TreePath[] expandedElements) {
		this.expandedElements = expandedElements;
	}

	public TreePath[] getExpandedElements() {
		return this.expandedElements;
	}

}
