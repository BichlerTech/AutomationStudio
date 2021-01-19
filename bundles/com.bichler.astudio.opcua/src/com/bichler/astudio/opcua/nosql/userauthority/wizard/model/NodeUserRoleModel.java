package com.bichler.astudio.opcua.nosql.userauthority.wizard.model;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.NodeClass;

public class NodeUserRoleModel extends AbstractOPCUserModel {

	public NodeUserRoleModel(String displayname, NodeClass nodeClass, NodeId nodeId) {
		super(displayname, nodeClass, nodeId);
	}

}
