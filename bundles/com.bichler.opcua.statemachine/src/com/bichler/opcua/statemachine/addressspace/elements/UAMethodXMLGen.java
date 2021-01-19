package com.bichler.opcua.statemachine.addressspace.elements;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.ReferenceNode;

import com.bichler.opcua.statemachine.addressspace.importer.StatemachineNodesetImporter;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.UAMethodNode;

public class UAMethodXMLGen extends BaseNodeXMLGen {

	public UAMethodXMLGen() {
		super();
	}

	public UAMethodXMLGen(UAMethodNode node) {
		super(node);
	}

	@Override
	public void writeXML(BufferedWriter out, StatemachineNodesetImporter importer, NamespaceTable serverTable,
			NamespaceTable exportTable) throws IOException {

		NodeId mapId = mapNodeId(getNode().getNodeId(), serverTable, exportTable);
		// SymbolicName=\""+getNode().getBrowseName().getName().toString()+"\"

		out.write("\t<" + UAMETHOD + helpNodeId(mapId) + helpBrowseName(getNode().getBrowseName())
				+ helpParentNodeId(importer, getNode(), serverTable, exportTable) + helpMethodDeclarationId(importer)
				+ ">");
		out.newLine();
		helpDisplayName(out, getNode().getDisplayName());
		helpDescription(out, getNode().getDescription());

		out.write("\t\t<" + REFERENCES + ">");
		out.newLine();
		for (ReferenceNode refNode : getNode().getReferences()) {
			writeXMLReference(out, refNode, serverTable, exportTable, importer);
		}
		out.write("\t\t</" + REFERENCES + ">");
		out.newLine();

		out.write("\t</" + UAMETHOD + ">");
		out.newLine();
	}

	private String helpMethodDeclarationId(StatemachineNodesetImporter importer) {
		NodeId methodDeclarationId = findTypeModel(importer);

		if (NodeId.isNull(methodDeclarationId)) {
			return "";
		}

		return " " + METHODDECLARATIONID + "=\"" + methodDeclarationId.toString() + "\"";
	}

	private NodeId findTypeModel(StatemachineNodesetImporter importer) {
		Node methodNode = getNode();

		String methodName = (methodNode.getBrowseName() != null ? methodNode.getBrowseName().getName() : "");
		
		return rekFindTypeModel(importer, methodNode.getNodeId(), methodName);
	}

	private NodeId rekFindTypeModel(StatemachineNodesetImporter importer, NodeId nodeId, String methodName) {
		Node node = importer.getNodesItemById(nodeId);

		NodeId lookup = null;
		boolean find = true;
		while (find) {
			NodeId parentId = findParent(importer, node);
			if (NodeId.equals(Identifiers.RootFolder, parentId)) {
				break;
			}
			node = importer.getNodesItemById(parentId);
			NodeId typeId = findTypeOfNode(importer, parentId);
			if (NodeId.isNull(typeId)) {
				break;
			}
			lookup = lookupTypeForBrowsename(importer, methodName, typeId);
			if (!NodeId.isNull(lookup)) {
				find = false;
			}

		}
		return lookup;
	}

	private NodeId findTypeOfNode(StatemachineNodesetImporter importer, NodeId nodeId) {
		Node node = importer.getNodesItemById(nodeId);

		if (node == null) {
			return NodeId.NULL;
		}

		ExpandedNodeId typeDef = node.findTarget(Identifiers.HasTypeDefinition, false);

		try {
			return importer.getNamespaceTable().toNodeId(typeDef);
		} catch (ServiceResultException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Cannot convert ExpandedNodeId to NodeId");
		}

		return NodeId.NULL;
	}

	private NodeId findParent(StatemachineNodesetImporter importer, Node node) {
		for (ReferenceNode reference : node.getReferences()) {
			boolean isInverse = reference.getIsInverse();
			if (!isInverse) {
				continue;
			}
			boolean isHierachical = isTypeOf(importer, reference.getReferenceTypeId(),
					Identifiers.HierarchicalReferences);
			if (!isHierachical) {
				continue;
			}

			try {
				return importer.getNamespaceTable().toNodeId(reference.getTargetId());
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Cannot convert ExpandedNodeId to NodeId");
			}
		}

		return NodeId.NULL;
	}

	private NodeId lookupTypeForBrowsename(StatemachineNodesetImporter importer, String name, NodeId typeId) {
		Node typeNode = importer.getNodesItemById(typeId);
		if (typeNode == null) {
			System.out.println("");
		}
		String browsename = typeNode.getBrowseName().getName();
		if (name.equals(browsename)) {
			return typeNode.getNodeId();
		}

		NodeId found = null;
		for (ReferenceNode reference : typeNode.getReferences()) {
			boolean isInverse = reference.getIsInverse();
			if (isInverse) {
				continue;
			}
			boolean isHierachical = isTypeOf(importer, reference.getReferenceTypeId(),
					Identifiers.HierarchicalReferences);
			if (!isHierachical) {
				continue;
			}

			try {
				NodeId targetId = importer.getNamespaceTable().toNodeId(reference.getTargetId());
				found = lookupTypeForBrowsename(importer, name, targetId);
				if (found != null) {
					break;
				}
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Cannot convert ExpandedNodeId to NodeId");
			}
		}
		return found;
	}

	private boolean isTypeOf(StatemachineNodesetImporter importer, NodeId subtype, NodeId supertype) {
		if (NodeId.equals(subtype, supertype)) {
			return true;
		}

		Node node = importer.getNodesItemById(subtype);
		if (node == null) {
			return false;
		}

		Node superNode = null;
		for (ReferenceNode reference : node.getReferences()) {
			if (!reference.getIsInverse()) {
				continue;
			}

			if (!NodeId.equals(Identifiers.HasSubtype, reference.getReferenceTypeId())) {
				continue;
			}

			superNode = importer.getNodesItemById(reference.getTargetId());
			break;
		}

		if (superNode == null) {
			return false;
		}
		
		return isTypeOf(importer, superNode.getNodeId(), supertype);
	}
}
