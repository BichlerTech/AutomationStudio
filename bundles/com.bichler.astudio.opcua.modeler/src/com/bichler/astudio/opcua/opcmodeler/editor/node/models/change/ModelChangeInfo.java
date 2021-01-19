package com.bichler.astudio.opcua.opcmodeler.editor.node.models.change;

import java.util.List;
import java.util.Map;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.core.AddNodesItem;
import org.opcfoundation.ua.core.AddReferencesItem;
import org.opcfoundation.ua.core.NodeAttributes;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceNode;

import opc.sdk.core.node.NamingRuleType;
import opc.sdk.core.node.Node;

public class ModelChangeInfo {
	private NodeId nodeId;
	private Map<Node, Boolean> additionalNodes;
	private Boolean hasRuleItself;
	private NamingRuleType selfRule;
	private NodeAttributes nodeAttributes;
	private QualifiedName browseName;
	private NodeClass nodeClass;
	private NodeId type;
	private ExpandedNodeId parentNodeId;
	private NodeId referenceTypeId;
	private List<ReferenceNode> additionalReferences;
	private Map<ExpandedNodeId, AddNodesItem> additionalPasteNodes;
	// create / paste
	private boolean isCreate;
	private List<AddReferencesItem> additionalPasteReferences;

	// public ModelChangeInfo(){
	//
	// }
	public ModelChangeInfo(boolean isCreate) {
		this.isCreate = isCreate;
	}

	public NodeId getNodeId() {
		return nodeId;
	}

	public Map<Node, Boolean> getAdditionalCreateNodes() {
		return additionalNodes;
	}

	public boolean hasRuleItself() {
		return hasRuleItself;
	}

	public NamingRuleType getNamingRule() {
		return selfRule;
	}

	public NodeAttributes getNodeAttributes() {
		return nodeAttributes;
	}

	public QualifiedName getBrowseName() {
		return browseName;
	}

	public NodeClass getNodeClass() {
		return nodeClass;
	}

	public NodeId getType() {
		return type;
	}

	public void setNodeId(NodeId nodeId) {
		this.nodeId = nodeId;
	}

	public void setAdditionalNodesMap(Map<Node, Boolean> additionalNodes) {
		this.additionalNodes = additionalNodes;
	}

	public void setHasRuleItself(Boolean hasRuleItself) {
		this.hasRuleItself = hasRuleItself;
	}

	public void setNamingRuleTyp(NamingRuleType selfRule) {
		this.selfRule = selfRule;
	}

	public void setNodeAttributes(NodeAttributes nodeAttributes) {
		this.nodeAttributes = nodeAttributes;
	}

	public void setBrowseName(QualifiedName browseName) {
		this.browseName = browseName;
	}

	public void setNodeClass(NodeClass nodeClass) {
		this.nodeClass = nodeClass;
	}

	public void setType(NodeId type) {
		this.type = type;
	}

	public void setParentNodeId(ExpandedNodeId parentNodeId) {
		this.parentNodeId = parentNodeId;
	}

	public ExpandedNodeId getParentNodeId() {
		return this.parentNodeId;
	}

	public NodeId getReferenceTypeId() {
		return this.referenceTypeId;
	}

	public void setReferenceTypeId(NodeId referenceTypeId) {
		this.referenceTypeId = referenceTypeId;
	}

	public void setAdditionalRefernces(List<ReferenceNode> additionalReferences) {
		this.additionalReferences = additionalReferences;
	}

	public List<ReferenceNode> getAdditionalReferences() {
		return this.additionalReferences;
	}

	public void setAdditionalPasteNodes(Map<ExpandedNodeId, AddNodesItem> pasteNodes) {
		this.additionalPasteNodes = pasteNodes;
	}

	public Map<ExpandedNodeId, AddNodesItem> getAdditionalPasteNodes() {
		return this.additionalPasteNodes;
	}

	public boolean isCreate() {
		return this.isCreate;
	}

	public void setCreate(boolean isCreate) {
		this.isCreate = isCreate;
	}

	public void setAdditionalPasteReferences(List<AddReferencesItem> references2add) {
		this.additionalPasteReferences = references2add;
	}

	public List<AddReferencesItem> getAdditionalPasteReferences() {
		return this.additionalPasteReferences;
	}
}
