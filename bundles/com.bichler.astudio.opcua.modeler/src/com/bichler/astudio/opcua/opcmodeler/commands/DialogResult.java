package com.bichler.astudio.opcua.opcmodeler.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import opc.sdk.core.node.NamingRuleType;
import opc.sdk.core.node.Node;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.NodeAttributes;
import org.opcfoundation.ua.core.ReferenceNode;

public class DialogResult {
	private Node nodeResult = null;
	private NodeAttributes nodeAttributes = null;
	private NodeId type = null;
	private ExpandedNodeId parentNodeId = null;
	private NodeId referenceTypeId = null;
	private List<ReferenceNode> additionalReferences = null;
	private List<Node> children = null;
	private Map<Node, Boolean> additionalNodesMap;
	private NamingRuleType namingRuleTyp;
	private boolean hasRuleItself = false;

	public DialogResult() {
		this.additionalReferences = new ArrayList<ReferenceNode>();
	}

	public void setNodeResult(Node node) {
		this.nodeResult = node;
	}

	public void setNodeAttributes(NodeAttributes attributes) {
		this.nodeAttributes = attributes;
	}

	public Node getNodeResult() {
		return this.nodeResult;
	}

	public NodeAttributes getNodeAttributes() {
		return this.nodeAttributes;
	}

	public void setType(NodeId nodeId) {
		this.type = nodeId;
	}

	public NodeId getType() {
		return this.type;
	}

	public ExpandedNodeId getParentNodeId() {
		return parentNodeId;
	}

	public void setParentNodeId(ExpandedNodeId parentNodeId) {
		this.parentNodeId = parentNodeId;
	}

	public NodeId getReferenceTypeId() {
		return referenceTypeId;
	}

	public void setReferenceTypeId(NodeId referenceTypeId) {
		this.referenceTypeId = referenceTypeId;
	}

	public List<ReferenceNode> getAdditionalReferences() {
		return this.additionalReferences;
	}

	public boolean addAdditionalReference(ReferenceNode referenceNode) {
		return this.additionalReferences.add(referenceNode);
	}

	public void setAdditionalReferences(ReferenceNode[] array) {
		this.additionalReferences = Arrays.asList(array);
	}

	public List<Node> getChildren() {
		return children;
	}

	public void setChildren(List<Node> children) {
		this.children = children;
	}

	public void setAdditionalNodesMap(Map<Node, Boolean> additionalNodes) {
		this.additionalNodesMap = additionalNodes;
	}

	public Map<Node, Boolean> getAdditionalNodes() {
		return this.additionalNodesMap;
	}

	public void setNamingRuleTyp(NamingRuleType selfRule) {
		this.namingRuleTyp = selfRule;
	}

	public NamingRuleType getNamingRule() {
		return this.namingRuleTyp;
	}

	public void setHasRuleItself(boolean hasRuleItself) {
		this.hasRuleItself = hasRuleItself;
	}

	public boolean hasRuleItself() {
		return this.hasRuleItself;
	}
}
