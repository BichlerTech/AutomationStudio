package com.bichler.astudio.opcua.opcmodeler.wizards.opc.validation.model;

import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceDescription;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;

public class ObjectValidationModel extends AbstractContentValidationModel {
	public ObjectValidationModel(NodeId nodeId, NodeClass nodeClass, LocalizedText name) {
		super(nodeId, nodeClass, name);
	}

	@Override
	public void buildModel() {
		buildChildren(this);
	}

	private void buildChildren(ObjectValidationModel parent) {
		ReferenceDescription[] results = ValidationModelContentFactory.browse(parent.getNodeId(),
				BrowseDirection.Forward, Identifiers.HierarchicalReferences);
		for (ReferenceDescription result : results) {
			try {
				NodeId id = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
						.toNodeId(result.getNodeId());
				ObjectValidationModel model = new ObjectValidationModel(id, result.getNodeClass(),
						result.getDisplayName());
				parent.addChild(model);
				buildChildren(model);
			} catch (ServiceResultException e) {
				e.printStackTrace();
			}
		}
	}
}
