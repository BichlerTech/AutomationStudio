package com.bichler.astudio.opcua.opcmodeler.wizards.opc.reference.model;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.NodeClass;

public class ReferenceModel {
	private ExpandedNodeId id = null;
	private LocalizedText name = null;
	private ReferenceModel parent = null;
	private List<ReferenceModel> children = new ArrayList<>();
	private NodeClass nodeClass;

	public ReferenceModel(ExpandedNodeId nodeId, LocalizedText name, NodeClass nodeClass) {
		this.id = nodeId;
		this.name = name;
		this.nodeClass = nodeClass;
	}

	public ReferenceModel find(NamespaceTable nsTable, NodeId lookup) {
		if (lookup.equals(getId(nsTable))) {
			return this;
		}
		for (ReferenceModel child : this.children) {
			ReferenceModel found = child.find(nsTable, lookup);
			if (found != null) {
				return found;
			}
		}
		return null;
	}

	public NodeId getId(NamespaceTable nsTable) {
		try {
			return nsTable.toNodeId(this.id);
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
		return NodeId.NULL;
	}

	public void setParent(ReferenceModel parent) {
		this.parent = parent;
	}

	public ReferenceModel getParent() {
		return this.parent;
	}

	public void addChild(ReferenceModel child) {
		this.children.add(child);
		child.setParent(this);
	}

	public ReferenceModel[] getChildren() {
		return this.children.toArray(new ReferenceModel[0]);
	}

	public String getName() {
		return this.name.toString();
	}

	public ExpandedNodeId getId() {
		return this.id;
	}

	public NodeClass getNodeClass() {
		return this.nodeClass;
	}
}
