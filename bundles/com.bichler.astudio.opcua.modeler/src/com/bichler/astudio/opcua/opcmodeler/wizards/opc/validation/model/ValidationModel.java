package com.bichler.astudio.opcua.opcmodeler.wizards.opc.validation.model;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.NodeClass;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.utils.extern.DesignerUtils;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.validation.ModelValidationWizard;

import opc.sdk.core.node.NodeIdMode;

public class ValidationModel {
	private LocalizedText name;
	private NodeId nodeId;
	private NodeClass nodeClass;
	private NodeId typeDefinitionId;
	private List<ValidationModel> children = new ArrayList<>();
	private ValidationModel parent;
	private ExpandedNodeId mappingId;
	private ExpandedNodeId originMappingId;

	public ValidationModel(NodeId nodeId, NodeClass nodeClass, LocalizedText name) {
		this.nodeId = nodeId;
		this.nodeClass = nodeClass;
		this.name = name;
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		this.mappingId = ServerInstance.getTypeModel().getTypeIdFromObject(
				new ExpandedNodeId(nsTable.getUri(nodeId.getNamespaceIndex()), nodeId.getValue(), nsTable));
		// this.mappingId = ServerInstance.getTypeModel().getTypeIdFromObject(
		// ServerInstance.getInstance().getServerInstance()
		// .getNamespaceUris().toExpandedNodeId(nodeId));
		this.originMappingId = this.mappingId;
	}

	public ExpandedNodeId getMappingId() {
		return this.mappingId;
	}

	public void addChild(int index, ValidationModel child) {
		this.children.add(index, child);
		child.setParent(this);
	}

	public void addChild(ValidationModel child) {
		this.children.add(child);
		child.setParent(this);
	}

	public ValidationModel[] getChildren() {
		return this.children.toArray(new ValidationModel[0]);
	}

	public List<ValidationModel> getChildrenList() {
		return this.children;
	}

	public LocalizedText getName() {
		return this.name;
	}

	public boolean hasChild(NodeId nodeId) {
		for (ValidationModel child : this.children) {
			if (nodeId.equals(child.getNodeId())) {
				return true;
			}
		}
		return false;
	}

	public NodeClass getNodeClass() {
		return this.nodeClass;
	}

	public NodeId getNodeId() {
		return this.nodeId;
	}

	public ValidationModel getParent() {
		return this.parent;
	}

	public void setMappingId(ExpandedNodeId mappingId) {
		this.mappingId = mappingId;
	}

	protected void resetMappingId() {
		this.mappingId = this.originMappingId;
	}

	private void setParent(ValidationModel parent) {
		this.parent = parent;
	}

	public ValidationModel findChild(NodeId id) {
		for (ValidationModel child : this.children) {
			if (child.getNodeId().equals(id)) {
				return child;
			}
		}
		return null;
	}

	public ValidationModel findDeep(NodeId nodeId) {
		if (this.nodeId.equals(nodeId)) {
			return this;
		}
		ValidationModel found = null;
		for (ValidationModel child : this.children) {
			found = child.findDeep(nodeId);
			if (found != null) {
				break;
			}
		}
		return found;
	}

	public ObjectValidationModel redrawMissing(MissingValidationModel objModel, ModelValidationWizard wizard) {
		// new id for new node
		NodeIdMode ccNodeId = DesignerUtils.getPreferenceContinueCreateNodeIds();
		NodeId newId = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager().getNodeFactory()
				.getNextNodeId(objModel.getParent().getNodeId().getNamespaceIndex(),
						objModel.getParent().getNodeId().getValue(), objModel.getParent().getNodeId().getIdType(),
						ccNodeId);

		// create wrapped instance
		ObjectValidationModel wrappedValidation = new ObjectValidationModel(newId, getNodeClass(), getName());
		// clone children from instance
		cloneChildren(wrappedValidation, wizard);
		return wrappedValidation;
	}

	public void setTypeDefinitionId(NodeId nodeId) {
		this.typeDefinitionId = nodeId;
	}

	private void cloneChildren(ObjectValidationModel objModelParent, ModelValidationWizard wizard) {
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		// add as new node
		wizard.getNodesToAdd().add(objModelParent);
		ExpandedNodeId mapping = new ExpandedNodeId(nsTable.getUri(getNodeId().getNamespaceIndex()),
				getNodeId().getValue(), nsTable);
		// ExpandedNodeId mapping = ServerInstance.getInstance()
		// .getServerInstance().getNamespaceUris()
		// .toExpandedNodeId(getNodeId());
		wizard.getRuntimeMapping()
				.put(new ExpandedNodeId(nsTable.getUri(objModelParent.getNodeId().getNamespaceIndex()),
						objModelParent.getNodeId().getValue(), nsTable), mapping);
		// wizard.getRuntimeMapping().put(
		// ServerInstance.getInstance().getServerInstance()
		// .getNamespaceUris()
		// .toExpandedNodeId(objModelParent.getNodeId()), mapping);
		objModelParent.setMappingId(mapping);
		ValidationModel[] c = this.getChildren();
		for (ValidationModel child : c) {
			NodeIdMode ccNodeId = DesignerUtils.getPreferenceContinueCreateNodeIds();
			NodeId newId = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager().getNodeFactory()
					.getNextNodeId(child.getParent().getNodeId().getNamespaceIndex(),
							child.getParent().getNodeId().getValue(), child.getParent().getNodeId().getIdType(),
							ccNodeId);

			ObjectValidationModel objValModel = new ObjectValidationModel(newId, child.getNodeClass(), child.getName());
			// objValModel.setMappingId(ServerInstance.getInstance()
			// .getServerInstance().getNamespaceUris()
			// .toExpandedNodeId(child.getNodeId()));
			objModelParent.addChild(objValModel);
			child.cloneChildren(objValModel, wizard);
		}
	}
}
