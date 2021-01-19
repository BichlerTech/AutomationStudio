package com.bichler.astudio.opcua.opcmodeler.editor.node.models.change;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceDescription;

public class ModelTreeDef {
	private ExpandedNodeId nodeId;
	private List<ModelTreeDef> children = new ArrayList<>();
	private ModelTreeDef parent;
	private ModelChangeInfo update;
	private ReferenceDescription reference;
	private boolean isChanged = false;
	private NodeClass nodeClass;

	public void setNodeId(ExpandedNodeId nodeId) {
		this.nodeId = nodeId;
	}

	public NodeId getNodeId(NamespaceTable nsTable) {
		try {
			return nsTable.toNodeId(this.nodeId);
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void addChild(ModelTreeDef def) {
		boolean found = false;
		for (ModelTreeDef child : this.children) {
			if (child.getNodeId().equals(def.getNodeId())) {
				found = true;
				break;
			}
		}
		if (!found) {
			this.children.add(def);
			def.setParent(this);
		}
	}

	private void setParent(ModelTreeDef parent) {
		this.parent = parent;
	}

	public ModelTreeDef[] getChildren() {
		return this.children.toArray(new ModelTreeDef[0]);
	}

	public void setReference(ReferenceDescription reference) {
		this.reference = reference;
	}

	public void setChanged(boolean isNewCreated) {
		this.isChanged = isNewCreated;
	}

	public boolean hasChanged() {
		return this.isChanged;
	}

	public void setUpdate(ModelChangeInfo update) {
		this.update = update;
	}

	public ModelChangeInfo getUpdateInfo() {
		return this.update;
	}

	// TODO COMMENT
	public ReferenceDescription getNodeInfo() {
		return this.reference;
	}

	public ExpandedNodeId getNodeId() {
		return this.nodeId;
	}

	public ModelTreeDef getParent() {
		return this.parent;
	}

	public void setNodeClass(NodeClass typeClass) {
		this.nodeClass = typeClass;
	}

	public NodeClass getTypeClass() {
		return this.nodeClass;
	}

	public ModelTreeDef findTypedModel(ExpandedNodeId typeId) {
		if (this.nodeId.equals(typeId)) {
			return this;
		}
		for (ModelTreeDef def : this.children) {
			ModelTreeDef found = def.findTypedModel(typeId);
			if (found != null) {
				return found;
			}
		}
		return null;
	}

	public List<ExpandedNodeId> getAllChildrenIds() {
		List<ExpandedNodeId> ids = new ArrayList<>();
		getAllChildrenIds(ids);
		return ids;
	}

	private void getAllChildrenIds(List<ExpandedNodeId> ids) {
		for (ModelTreeDef child : this.children) {
			ids.add(child.nodeId);
			child.getAllChildrenIds(ids);
		}
	}
}
