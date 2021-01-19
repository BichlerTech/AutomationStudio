package opc.sdk.core.node.mapper;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceNode;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.NodeIdUtil;
import opc.sdk.core.node.VariableNode;
import opc.sdk.core.node.VariableTypeNode;

public class NodeIdMapper {
	private static Logger logger = Logger.getLogger(NodeIdMapper.class.getName());

	public static void mapNode(NamespaceTable namespaceUris, Node node, Map<Integer, Integer> old2newMapping,
			NamespaceTable newNamespaceUris, Map<NodeId, Node> newNodes) {
		NodeClass nodeClass = node.getNodeClass();
		// change all nodeids from type specific node
		NodeId newId = mapNamespaceIndex(node.getNodeId(), old2newMapping);
		node.setNodeId(newId);
		newNodes.put(newId, node);
		switch (nodeClass) {
		case Object:
			// no more attributes
			break;
		case ObjectType:
			break;
		case Variable:
			((VariableNode) node).setDataType(mapNamespaceIndex(((VariableNode) node).getDataType(), old2newMapping));
			break;
		case VariableType:
			((VariableTypeNode) node)
					.setDataType(mapNamespaceIndex(((VariableTypeNode) node).getDataType(), old2newMapping));
			break;
		case Method:
			break;
		case DataType:
			break;
		case ReferenceType:
			break;
		case View:
			break;
		default:
			break;
		}
		// change all nodeids from references
		ReferenceNode[] references = node.getReferences();
		// nothing to change
		if (references == null) {
			return;
		}
		for (ReferenceNode reference : references) {
			NodeId referenceId = reference.getReferenceTypeId();
			ExpandedNodeId targetExpId = reference.getTargetId();
			// change ids
			reference.setReferenceTypeId(mapNamespaceIndex(referenceId, old2newMapping));
			try {
				reference.setTargetId(mapNamespaceIndex(namespaceUris, targetExpId, old2newMapping, newNamespaceUris));
			} catch (Exception e) {
				logger.log(Level.SEVERE, e.getMessage());
			}
		}
	}

	/**
	 * Change namespaceindex of a nodeId.
	 * 
	 * @param namespaceUris
	 * @param nodeId
	 * @param old2newMapping
	 * @return New expandednodeid.
	 */
	public static ExpandedNodeId mapNamespaceIndex(NamespaceTable namespaceUris, ExpandedNodeId nodeId,
			Map<Integer, Integer> old2newMapping, NamespaceTable newNamespaceUris) {
		ExpandedNodeId expId = null;
		try {
			NodeId newId = mapNamespaceIndex(namespaceUris.toNodeId(nodeId), old2newMapping);
			if (newId == null || NodeId.isNull(newId)) {
				return null;
			}
			// map namespace uri
			String newUri = newNamespaceUris.getUri(newId.getNamespaceIndex());
			expId = new ExpandedNodeId(/* namespaceUris.getUri(newId.getNamespaceIndex()) */ newUri, newId.getValue(),
					namespaceUris);
		} catch (ServiceResultException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return expId;
	}

	/**
	 * Change namespaceindex of a nodeId.
	 * 
	 * @param nodeId
	 * @param old2newMapping
	 * @return New nodeid.
	 */
	public static NodeId mapNamespaceIndex(NodeId nodeId, Map<Integer, Integer> old2newMapping) {
		if (NodeId.isNull(nodeId)) {
			return nodeId;
		}
		int oldIndex = nodeId.getNamespaceIndex();
		Integer newIndex = old2newMapping.get(oldIndex);
		// available id to map
		if (newIndex != null) {
			return NodeIdUtil.createNodeId(newIndex, nodeId.getValue());
		}
		return null;
	}
}
