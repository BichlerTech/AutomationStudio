package com.bichler.opcua.statemachine.datadictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AddNodesItem;
import org.opcfoundation.ua.core.AddReferencesItem;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.ObjectAttributes;
import org.opcfoundation.ua.core.ReferenceNode;
import org.opcfoundation.ua.core.VariableAttributes;
import org.opcfoundation.ua.encoding.EncoderContext;
import org.opcfoundation.ua.encoding.EncodingException;

import com.bichler.astudio.opcua.datadictionary.base.model.DataDictionaryUtil;
import com.bichler.opcua.statemachine.addressspace.importer.StatemachineNodesetImporter;
import com.bichler.opcua.statemachine.transform.AbstractStateMachineToOpcTransformer;

import opc.sdk.core.node.DefaultNodeFactory;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.ObjectNode;
import opc.sdk.core.node.VariableNode;

public class DatatypeUtils {

	public static Node getNode(StatemachineNodesetImporter importer, NodeId nodeId) {
		Node node = importer.getNodesItemById(nodeId);
		if (node == null) {
			node = importer.getStructuredDatatypeNodesItemList()
					.get(importer.getNamespaceTable().toExpandedNodeId(nodeId));
		}
		return node;
	}

	public static Node getNode(StatemachineNodesetImporter importer, ExpandedNodeId nodeId) {
		Node node = importer.getNodesItemById(nodeId);
		if (node == null) {
			node = importer.getStructuredDatatypeNodesItemList().get(nodeId);
		}
		return node;
	}

//
	public static void addNode(AbstractStateMachineToOpcTransformer transformer, StatemachineNodesetImporter importer,
			AddNodesItem[] nodes, boolean includeChildren) throws ServiceResultException {
		List<Node> newNodes = new ArrayList<>();
		for (AddNodesItem item : nodes) {
			Node decoded = DataDictionaryUtil.decodeNode(importer.getNamespaceTable(), item, new DefaultNodeFactory());
			newNodes.add(decoded);

			if (includeChildren) {
				ExpandedNodeId typedefinition = item.getTypeDefinition();
				if (!ExpandedNodeId.isNull(typedefinition)) {
					List<Node> children = createChildrenNodes(transformer, importer, typedefinition,
							decoded.getNodeId());
					newNodes.addAll(children);
				}
			}
		}
		importer.addNode(newNodes);
	}

	public static void addReferences(StatemachineNodesetImporter importer, AddReferencesItem[] nodes)
			throws ServiceResultException {

		for (AddReferencesItem reference : nodes) {
			ReferenceNode newReference = new ReferenceNode();
			newReference.setIsInverse(!reference.getIsForward());
			newReference.setReferenceTypeId(reference.getReferenceTypeId());
			newReference.setTargetId(reference.getTargetNodeId());

			importer.addReference(reference.getSourceNodeId(), newReference);
		}
	}

//
	public static boolean isTypeOf(StatemachineNodesetImporter importer, NodeId subTypeId, NodeId superTypeId) {
		NodeId typeId = subTypeId;
		try {
			do {
				// check for same type nodeid
				if (NodeId.equals(superTypeId, typeId)) {
					return true;
				}
				// get type node
				Node typeOf = getNode(importer, typeId);
				// check for type references
				boolean found = false;
				for (ReferenceNode reference : typeOf.getReferences()) {
					// inverse direction
					if (!reference.getIsInverse()) {
						continue;
					}
					// check for type reference
					if (!NodeId.equals(Identifiers.HasSubtype, reference.getReferenceTypeId())) {
						continue;
					}
					// continue to lookup
					typeId = importer.getNamespaceTable().toNodeId(reference.getTargetId());
					found = true;
				}
				
				if(!found) {
					typeId = NodeId.NULL;
				}
			} while (!NodeId.isNull(typeId));
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private static Node copy(AbstractStateMachineToOpcTransformer transformer, StatemachineNodesetImporter importer,
			Node nodeToCopy, NodeId parentId, NodeId referenceTypeId) throws EncodingException {
		ReferenceNode[] references = nodeToCopy.getReferences();
		ExpandedNodeId typeDefinition = ExpandedNodeId.NULL;
		// fetch type of node
		for (ReferenceNode reference : references) {
			if (!Identifiers.HasTypeDefinition.equals(reference.getReferenceTypeId())) {
				continue;
			}
			typeDefinition = reference.getTargetId();
			break;
		}

		AddNodesItem item = new AddNodesItem();
		item.setBrowseName(nodeToCopy.getBrowseName());
		item.setNodeClass(nodeToCopy.getNodeClass());
		item.setParentNodeId(importer.getNamespaceTable().toExpandedNodeId(parentId));
		item.setReferenceTypeId(referenceTypeId);

		NodeId newId = transformer.getNextNumericNodeId(parentId.getNamespaceIndex());

		item.setRequestedNewNodeId(importer.getNamespaceTable().toExpandedNodeId(newId));
		item.setTypeDefinition(typeDefinition);

		switch (nodeToCopy.getNodeClass()) {
		case Object:
			ObjectAttributes oa = new ObjectAttributes();
			oa.setDescription(nodeToCopy.getDescription());
			oa.setDisplayName(nodeToCopy.getDisplayName());
			oa.setEventNotifier(((ObjectNode) nodeToCopy).getEventNotifier());
			oa.setUserWriteMask(nodeToCopy.getUserWriteMask());
			oa.setWriteMask(nodeToCopy.getWriteMask());
			item.setNodeAttributes(ExtensionObject.binaryEncode(oa, EncoderContext.getDefaultInstance()));
			break;
		case Variable:
			VariableAttributes va = new VariableAttributes();
			va.setAccessLevel(((VariableNode) nodeToCopy).getAccessLevel());
			va.setArrayDimensions(((VariableNode) nodeToCopy).getArrayDimensions());
			va.setDataType(((VariableNode) nodeToCopy).getDataType());
			va.setDescription(nodeToCopy.getDescription());
			va.setDisplayName(nodeToCopy.getDisplayName());
			va.setHistorizing(((VariableNode) nodeToCopy).getHistorizing());
			va.setMinimumSamplingInterval(((VariableNode) nodeToCopy).getMinimumSamplingInterval());
			va.setUserAccessLevel(((VariableNode) nodeToCopy).getUserAccessLevel());
			va.setUserWriteMask(nodeToCopy.getUserWriteMask());
			va.setValue(((VariableNode) nodeToCopy).getValue());
			va.setValueRank(((VariableNode) nodeToCopy).getValueRank());
			va.setWriteMask(nodeToCopy.getWriteMask());
			item.setNodeAttributes(ExtensionObject.binaryEncode(va, EncoderContext.getDefaultInstance()));
			break;
		default:
			break;
		}
		// create OPCUA node
		Node decoded = DataDictionaryUtil.decodeNode(importer.getNamespaceTable(), item, new DefaultNodeFactory());
		// copy references for node
		List<ReferenceNode> copyReferences = new ArrayList<>();
		for (ReferenceNode reference : decoded.getReferences()) {
			copyReferences.add(reference);
		}

		for (ReferenceNode reference : nodeToCopy.getReferences()) {
			// skip hierachical references
			if (importer.isHierachicalReference(reference.getReferenceTypeId())) {
				continue;
			}
			// skip typedefinition reference
			if (Identifiers.HasTypeDefinition.equals(reference.getReferenceTypeId())) {
				continue;
			}

			if (reference.getTargetId().getNamespaceIndex() > 0) {
				continue;
			}

			ReferenceNode newReference = new ReferenceNode();
			newReference.setIsInverse(reference.getIsInverse());
			newReference.setReferenceTypeId(reference.getReferenceTypeId());
			newReference.setTargetId(reference.getTargetId());
			copyReferences.add(newReference);
		}

		decoded.setReferences(copyReferences.toArray(new ReferenceNode[0]));

		return decoded;
	}
	
	private static List<Node> createChildrenNodes(AbstractStateMachineToOpcTransformer transformer,
			StatemachineNodesetImporter importer, ExpandedNodeId parentTypeId, NodeId parentId) {
		List<Node> nodes = new ArrayList<>();

		for (Entry<ExpandedNodeId, Node> entry : importer.getNodesItemList().entrySet()) {
			Node value = entry.getValue();
			ReferenceNode[] references = value.getReferences();
			for (ReferenceNode refNode : references) {
				if (!importer.isInverseHierachical(refNode)) {
					continue;
				}

				if (!importer.isHierachicalReference(refNode.getReferenceTypeId())) {
					continue;
				}

				if (!refNode.getTargetId().equals(parentTypeId)) {
					continue;
				}

				try {
					Node child = copy(transformer, importer, value, parentId, refNode.getReferenceTypeId());
					nodes.add(child);

					List<Node> list = createChildrenNodes(transformer, importer,
							importer.getNamespaceTable().toExpandedNodeId(value.getNodeId()), child.getNodeId());
					nodes.addAll(list);
				} catch (EncodingException e) {
					e.printStackTrace();
				}
			}
		}

		return nodes;
	}
}
