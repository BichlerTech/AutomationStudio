package com.bichler.astudio.opcua.opcmodeler.wizards.opc.validation.model;

import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceDescription;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;

public class TypeValidationModel extends AbstractContentValidationModel {
	public TypeValidationModel(NodeId nodeId, NodeClass nodeClass, LocalizedText name) {
		super(nodeId, nodeClass, name);
	}

	@Override
	public void buildModel() {
		build(this, this);
	}

	private void build(TypeValidationModel type, TypeValidationModel typeObject) {
		buildChildren(type, typeObject);
		ReferenceDescription[] superTypeRef = ValidationModelContentFactory.browse(type.getNodeId(),
				BrowseDirection.Inverse, Identifiers.HasSubtype, NodeClass.getMask(NodeClass.ObjectType,
						NodeClass.VariableType, NodeClass.ReferenceType, NodeClass.DataType));
		if (superTypeRef != null && superTypeRef.length > 0) {
			try {
				TypeValidationModel superType = new TypeValidationModel(
						ServerInstance.getInstance().getServerInstance().getNamespaceUris()
								.toNodeId(superTypeRef[0].getNodeId()),
						superTypeRef[0].getNodeClass(), superTypeRef[0].getDisplayName());
				build(superType, typeObject);
			} catch (ServiceResultException e) {
				e.printStackTrace();
			}
		}
	}

	private void buildChildren(TypeValidationModel parent, TypeValidationModel typeObject) {
		ReferenceDescription[] results = ValidationModelContentFactory.browse(parent.getNodeId(),
				BrowseDirection.Forward, Identifiers.HierarchicalReferences,
				NodeClass.getMask(NodeClass.Object, NodeClass.Variable, NodeClass.Method));
		for (ReferenceDescription result : results) {
			try {
				TypeValidationModel model = new TypeValidationModel(ServerInstance.getInstance().getServerInstance()
						.getNamespaceUris().toNodeId(result.getNodeId()), result.getNodeClass(),
						result.getDisplayName());
				typeObject.addChild(model);
				buildChildren(model, model);
			} catch (ServiceResultException e) {
				e.printStackTrace();
			}
		}
	}
}
