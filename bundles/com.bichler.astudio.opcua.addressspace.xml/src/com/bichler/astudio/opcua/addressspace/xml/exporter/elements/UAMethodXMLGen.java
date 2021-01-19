package com.bichler.astudio.opcua.addressspace.xml.exporter.elements;

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

import com.bichler.astudio.opcua.addressspace.xml.exporter.IServerTypeModel;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.UAMethodNode;
import opc.sdk.server.core.OPCInternalServer;

public class UAMethodXMLGen extends BaseNodeXMLGen {

	public UAMethodXMLGen() {
		super();
	}

	public UAMethodXMLGen(UAMethodNode node) {
		super(node);
	}

	@Override
	public void writeXML(BufferedWriter out, OPCInternalServer serverInstance, NamespaceTable serverTable,
			NamespaceTable exportTable, IServerTypeModel typeModel) throws IOException {
		NodeId mapId = mapNodeId(getNode().getNodeId(), serverTable, exportTable);
		// SymbolicName=\""+getNode().getBrowseName().getName().toString()+"\"

		ExpandedNodeId methodDeclarationId = typeModel
				.getTypeIdFromObject(serverTable.toExpandedNodeId(getNode().getNodeId()));
		NodeId id_methodDeclaration = mapNodeId(methodDeclarationId, serverTable, exportTable);

		out.write("\t<" + UAMETHOD + helpSymbolicName(getNode().getNodeId())
				+ helpParentNodeId(serverInstance, getNode(), exportTable) + helpNodeId(mapId)
				+ helpBrowseName(getNode().getBrowseName(), mapId)
				+ helpMethodDeclarationId(serverInstance, id_methodDeclaration) + ">");
		out.newLine();
		helpDisplayName(out, getNode().getDisplayName());
		helpDescription(out, getNode().getDescription());

		out.write("\t\t<" + REFERENCES + ">");
		out.newLine();
		for (ReferenceNode refNode : getNode().getReferences()) {
			writeXMLReference(out, serverInstance, refNode, serverTable, exportTable);
		}
		out.write("\t\t</" + REFERENCES + ">");
		out.newLine();

		writeExtensions(out);

		out.write("\t</" + UAMETHOD + ">");
		out.newLine();
	}

	private String helpMethodDeclarationId(OPCInternalServer serverInstance, NodeId methodDeclarationId) {
		if (NodeId.isNull(methodDeclarationId)) {
			methodDeclarationId = findTypeModel(serverInstance);

			if (NodeId.isNull(methodDeclarationId)) {
				return "";
			}
		}

		return " " + METHODDECLARATIONID + "=\"" + methodDeclarationId.toString() + "\"";
	}

	private NodeId findTypeModel(OPCInternalServer serverInstance) {
		Node methodNode = getNode();
		
		String methodName = (methodNode.getBrowseName() != null ? methodNode.getBrowseName().getName() : "");
		
		return rekFindTypeModel(serverInstance, methodNode.getNodeId(), methodName);
	}

	private NodeId rekFindTypeModel(OPCInternalServer serverInstance, NodeId nodeId, String methodName) {
		Node node = serverInstance.getAddressSpaceManager().getNodeById(nodeId);

		NodeId lookup = null;
		boolean loop = true;
		while (loop) {
			NodeId parentId = findParent(serverInstance, node);
			if (NodeId.equals(Identifiers.RootFolder, parentId)) {
				break;
			}
			node = serverInstance.getAddressSpaceManager().getNodeById(parentId);
			NodeId typeId = findTypeOfNode(serverInstance, parentId);
			if(NodeId.isNull(typeId)) {
				break;
			}
			lookup = lookupTypeForBrowsename(serverInstance, methodName, typeId);
			if (!NodeId.isNull(lookup)) {
				loop = false;
			}
			
		}
		return lookup;
	}

	private NodeId findTypeOfNode(OPCInternalServer serverInstance, NodeId nodeId) {
		Node node = serverInstance.getAddressSpaceManager().getNodeById(nodeId);

		if (node == null) {
			return NodeId.NULL;
		}

		ExpandedNodeId typeDef = node.findTarget(Identifiers.HasTypeDefinition, false);

		try {
			return serverInstance.getNamespaceUris().toNodeId(typeDef);
		} catch (ServiceResultException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Cannot convert ExpandedNodeId to NodeId");
		}

		return NodeId.NULL;
	}

	private NodeId findParent(OPCInternalServer serverInstance, Node node) {
		for (ReferenceNode reference : node.getReferences()) {
			boolean isInverse = reference.getIsInverse();
			if (!isInverse) {
				continue;
			}
			boolean isHierachical = serverInstance.getTypeTable().isTypeOf(reference.getReferenceTypeId(),
					Identifiers.HierarchicalReferences);
			if (!isHierachical) {
				continue;
			}

			try {
				return serverInstance.getNamespaceUris().toNodeId(reference.getTargetId());
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Cannot convert ExpandedNodeId to NodeId");
			}
		}

		return NodeId.NULL;
	}

	private NodeId lookupTypeForBrowsename(OPCInternalServer serverInstance, String name, NodeId typeId) {
		Node typeNode = serverInstance.getAddressSpaceManager().getNodeById(typeId);
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
			boolean isHierachical = serverInstance.getTypeTable().isTypeOf(reference.getReferenceTypeId(),
					Identifiers.HierarchicalReferences);
			if (!isHierachical) {
				continue;
			}

			try {
				NodeId targetId = serverInstance.getNamespaceUris().toNodeId(reference.getTargetId());
				found = lookupTypeForBrowsename(serverInstance, name, targetId);
				if (found != null) {
					break;
				}
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Cannot convert ExpandedNodeId to NodeId");
			}
		}
		return found;
	}
}
