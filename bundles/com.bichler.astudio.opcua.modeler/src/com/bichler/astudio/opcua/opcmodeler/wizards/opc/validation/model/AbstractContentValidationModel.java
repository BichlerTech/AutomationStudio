package com.bichler.astudio.opcua.opcmodeler.wizards.opc.validation.model;

import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.NodeClass;

public abstract class AbstractContentValidationModel extends ValidationModel {
	public AbstractContentValidationModel(NodeId nodeId, NodeClass nodeClass, LocalizedText name) {
		super(nodeId, nodeClass, name);
	}

	public abstract void buildModel();
}
