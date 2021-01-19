package com.bichler.astudio.opcua.opcmodeler.wizards.opc.validation.model;

import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.NodeClass;

public class MissingValidationModel extends AbstractContentValidationModel {
	public MissingValidationModel(NodeId nodeId, NodeClass nodeClass, LocalizedText name) {
		super(nodeId, nodeClass, name);
	}

	@Override
	public void buildModel() {
	}
}
