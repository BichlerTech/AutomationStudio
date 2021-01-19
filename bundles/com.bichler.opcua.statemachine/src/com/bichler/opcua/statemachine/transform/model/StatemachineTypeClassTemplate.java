package com.bichler.opcua.statemachine.transform.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.uml2.uml.Classifier;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.ReferenceNode;

import com.bichler.opcua.statemachine.addressspace.importer.StatemachineNodesetImporter;
import com.bichler.opcua.statemachine.transform.AbstractStateMachineToOpcTransformer;

import opc.sdk.core.node.Node;

public class StatemachineTypeClassTemplate extends ObjectTypeClassTemplate {

	public StatemachineTypeClassTemplate(Classifier classifier, NodeId nodeId) {
		super(classifier, nodeId);
	}

	@Override
	public List<Node> toOpcUaNodes(AbstractStateMachineToOpcTransformer transformer,
			StatemachineNodesetImporter importer, ClassTemplate[] templates, List<Node> createdNodes,
			List<Node> temporary, boolean addTypeNode, String nodeIdPrefix, Classifier supertype, Node parent,
			Map<NodeId, NodeId> mapping) {

		List<Node> nodes = super.toOpcUaNodes(transformer, importer, templates, createdNodes, temporary, addTypeNode,
				nodeIdPrefix, supertype, parent, mapping);

		// only for composited classes
		if (!addTypeNode) {
			List<Node> statemachineStructure = createStatemachineStructure(transformer, importer, nodeIdPrefix,
					parent.getNodeId(), createdNodes, mapping);
			nodes.addAll(statemachineStructure);
		}
		return nodes;
	}

	private List<Node> createStatemachineStructure(AbstractStateMachineToOpcTransformer transformer,
			StatemachineNodesetImporter importer, String nodeIdPrefix, NodeId parentId, List<Node> createdNodes,
			Map<NodeId, NodeId> mapping) {

		List<Node> nodes = new ArrayList<>();

		String name = getName();
		Node statemachineType = findNodeByName(name, createdNodes);
		List<Node> structure = copyStructure(transformer, importer, statemachineType, nodeIdPrefix, parentId,
				createdNodes, mapping);
		nodes.addAll(structure);

		return nodes;
	}

	private List<Node> copyStructure(AbstractStateMachineToOpcTransformer transformer,
			StatemachineNodesetImporter importer, Node node, String nodeIdPrefix, NodeId parentId,
			List<Node> createdNodes, Map<NodeId, NodeId> mapping) {

		List<Node> nodes = new ArrayList<>();
		NamespaceTable nsTable = importer.getNamespaceTable();

		for (Node createdNode : createdNodes) {
			ReferenceNode[] references = createdNode.getReferences();
			for (ReferenceNode reference : references) {
				try {
					if (!reference.getIsInverse()) {
						continue;
					}

					if (!importer.isHierachicalReference(reference.getReferenceTypeId())) {
						continue;
					}
					ExpandedNodeId targetId = reference.getTargetId();
					if (!node.getNodeId().equals(nsTable.toNodeId(targetId))) {
						continue;
					}

					Node copy = ClassTemplateFactory.copyNode(transformer, importer, nsTable, getClassifier(),
							nodeIdPrefix, parentId, reference.getReferenceTypeId(), createdNode, mapping);
					if (copy == null) {
						continue;
					}
					nodes.add(copy);
					mapping.put(createdNode.getNodeId(), copy.getNodeId());

					List<Node> structure = copyStructure(transformer, importer, createdNode,
							nodeIdPrefix + "." + copy.getBrowseName().getName(), copy.getNodeId(), createdNodes,
							mapping);
					nodes.addAll(structure);
				} catch (ServiceResultException e) {
					e.printStackTrace();
				}
			}
		}

		return nodes;
	}
/*
	private Node findNodeById(NodeId id, List<Node> createdNodes) {
		for (Node node : createdNodes) {
			NodeId nodeId = node.getNodeId();
			if (nodeId.equals(id)) {
				return node;
			}
		}
		return null;
	}*/

	private Node findNodeByName(String name, List<Node> createdNodes) {
		for (Node node : createdNodes) {
			String typeName = node.getBrowseName().getName();
			if (name.equals(typeName)) {
				return node;
			}
		}
		return null;
	}

}
