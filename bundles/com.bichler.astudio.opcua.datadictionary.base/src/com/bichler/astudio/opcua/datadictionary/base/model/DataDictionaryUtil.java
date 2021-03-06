package com.bichler.astudio.opcua.datadictionary.base.model;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AddNodesItem;
import org.opcfoundation.ua.core.DataTypeAttributes;
import org.opcfoundation.ua.core.MethodAttributes;
import org.opcfoundation.ua.core.NodeAttributes;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ObjectAttributes;
import org.opcfoundation.ua.core.ObjectTypeAttributes;
import org.opcfoundation.ua.core.ReferenceTypeAttributes;
import org.opcfoundation.ua.core.VariableAttributes;
import org.opcfoundation.ua.core.VariableTypeAttributes;
import org.opcfoundation.ua.core.ViewAttributes;

import opc.sdk.core.node.AbstractNodeFactory;
import opc.sdk.core.node.DataTypeNode;
import opc.sdk.core.node.MethodNode;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.ObjectNode;
import opc.sdk.core.node.ObjectTypeNode;
import opc.sdk.core.node.ReferenceTypeNode;
import opc.sdk.core.node.VariableNode;
import opc.sdk.core.node.VariableTypeNode;
import opc.sdk.core.node.ViewNode;

public class DataDictionaryUtil {

	public static Node decodeNode(NamespaceTable namespaceTable, AddNodesItem addNodesItem,
			AbstractNodeFactory nodeFactory) {
		QualifiedName browseName = addNodesItem.getBrowseName();
		ExtensionObject nodesAttribute = addNodesItem.getNodeAttributes();
		NodeClass nodeClass = addNodesItem.getNodeClass();
		ExpandedNodeId parentNodeId = addNodesItem.getParentNodeId();
		NodeId referenceTypeId = addNodesItem.getReferenceTypeId();
		ExpandedNodeId requestedNewNodeId = addNodesItem.getRequestedNewNodeId();
		ExpandedNodeId typeDefinition = addNodesItem.getTypeDefinition();
		Node create = null;
		try {
			create = nodeFactory.createNode(namespaceTable, browseName, nodesAttribute, nodeClass, parentNodeId,
					referenceTypeId, requestedNewNodeId, typeDefinition);
		} catch (ServiceResultException e) {
			Logger.getLogger(DataDictionaryUtil.class.getName()).log(Level.SEVERE, e.getMessage());
		}
		return create;
	}

	public static boolean isServerBaseNode(Node node) {
		// namespaceindex is 0
		if (node.getNodeId().getNamespaceIndex() == 0) {
			// id is a number
			if (node.getNodeId().getValue() instanceof UnsignedInteger) {
				return true;
			}
		}
		return false;
	}

	public static NodeAttributes fetchNodeAttribute(Node node) {
		NodeAttributes attributes = null;
		switch (node.getNodeClass()) {
		case Object:
			attributes = new ObjectAttributes();
			((ObjectAttributes) attributes).setDescription(node.getDescription());
			((ObjectAttributes) attributes).setDisplayName(node.getDisplayName());
			((ObjectAttributes) attributes).setEventNotifier(((ObjectNode) node).getEventNotifier());
			((ObjectAttributes) attributes).setUserWriteMask(node.getUserWriteMask());
			((ObjectAttributes) attributes).setWriteMask(node.getWriteMask());
			break;
		case ObjectType:
			attributes = new ObjectTypeAttributes();
			((ObjectTypeAttributes) attributes).setDescription(node.getDescription());
			((ObjectTypeAttributes) attributes).setDisplayName(node.getDisplayName());
			((ObjectTypeAttributes) attributes).setIsAbstract(((ObjectTypeNode) node).getIsAbstract());
			((ObjectTypeAttributes) attributes).setUserWriteMask(node.getUserWriteMask());
			((ObjectTypeAttributes) attributes).setWriteMask(node.getWriteMask());
			break;
		case Variable:
			attributes = new VariableAttributes();
			((VariableAttributes) attributes).setAccessLevel(((VariableNode) node).getAccessLevel());
			((VariableAttributes) attributes).setArrayDimensions(((VariableNode) node).getArrayDimensions());
			((VariableAttributes) attributes).setDataType(((VariableNode) node).getDataType());
			((VariableAttributes) attributes).setDescription(node.getDescription());
			((VariableAttributes) attributes).setDisplayName(node.getDisplayName());
			((VariableAttributes) attributes).setHistorizing(((VariableNode) node).getHistorizing());
			((VariableAttributes) attributes)
					.setMinimumSamplingInterval(((VariableNode) node).getMinimumSamplingInterval());
			((VariableAttributes) attributes).setUserAccessLevel(((VariableNode) node).getAccessLevel());
			((VariableAttributes) attributes).setUserWriteMask(node.getUserWriteMask());
			((VariableAttributes) attributes).setValue(((VariableNode) node).getValue());
			((VariableAttributes) attributes).setValueRank(((VariableNode) node).getValueRank());
			((VariableAttributes) attributes).setWriteMask(node.getWriteMask());
			break;
		case VariableType:
			attributes = new VariableTypeAttributes();
			((VariableTypeAttributes) attributes).setIsAbstract(((VariableTypeNode) node).getIsAbstract());
			((VariableTypeAttributes) attributes).setArrayDimensions(((VariableTypeNode) node).getArrayDimensions());
			((VariableTypeAttributes) attributes).setDataType(((VariableTypeNode) node).getDataType());
			((VariableTypeAttributes) attributes).setDescription(node.getDescription());
			((VariableTypeAttributes) attributes).setDisplayName(node.getDisplayName());
			((VariableTypeAttributes) attributes).setUserWriteMask(node.getUserWriteMask());
			((VariableTypeAttributes) attributes).setValue(((VariableTypeNode) node).getValue());
			((VariableTypeAttributes) attributes).setValueRank(((VariableTypeNode) node).getValueRank());
			((VariableTypeAttributes) attributes).setWriteMask(node.getWriteMask());
			break;
		case Method:
			attributes = new MethodAttributes();
			((MethodAttributes) attributes).setDescription(node.getDescription());
			((MethodAttributes) attributes).setDisplayName(node.getDisplayName());
			((MethodAttributes) attributes).setExecutable(((MethodNode) node).getExecutable());
			((MethodAttributes) attributes).setUserExecutable(((MethodNode) node).getUserExecutable());
			((MethodAttributes) attributes).setUserWriteMask(node.getUserWriteMask());
			((MethodAttributes) attributes).setUserWriteMask(node.getWriteMask());
			break;
		case DataType:
			attributes = new DataTypeAttributes();
			((DataTypeAttributes) attributes).setDescription(node.getDescription());
			((DataTypeAttributes) attributes).setDisplayName(node.getDisplayName());
			((DataTypeAttributes) attributes).setIsAbstract(((DataTypeNode) node).getIsAbstract());
			((DataTypeAttributes) attributes).setUserWriteMask(node.getUserWriteMask());
			((DataTypeAttributes) attributes).setWriteMask(node.getWriteMask());
			break;
		case ReferenceType:
			attributes = new ReferenceTypeAttributes();
			((ReferenceTypeAttributes) attributes).setDescription(node.getDescription());
			((ReferenceTypeAttributes) attributes).setDisplayName(node.getDisplayName());
			((ReferenceTypeAttributes) attributes).setInverseName(((ReferenceTypeNode) node).getInverseName());
			((ReferenceTypeAttributes) attributes).setIsAbstract(((ReferenceTypeNode) node).getIsAbstract());
			((ReferenceTypeAttributes) attributes).setSymmetric(((ReferenceTypeNode) node).getSymmetric());
			((ReferenceTypeAttributes) attributes).setUserWriteMask(node.getUserWriteMask());
			((ReferenceTypeAttributes) attributes).setWriteMask(node.getWriteMask());
			break;
		case View:
			attributes = new ViewAttributes();
			((ViewAttributes) attributes).setContainsNoLoops(((ViewNode) node).getContainsNoLoops());
			((ViewAttributes) attributes).setDescription(node.getDescription());
			((ViewAttributes) attributes).setDisplayName(node.getDisplayName());
			((ViewAttributes) attributes).setEventNotifier(((ViewNode) node).getEventNotifier());
			((ViewAttributes) attributes).setUserWriteMask(node.getUserWriteMask());
			((ViewAttributes) attributes).setWriteMask(node.getWriteMask());
			break;
		case Unspecified:
			attributes = null;
			break;
		default:
			return null;
		}
		return attributes;
	}

}
