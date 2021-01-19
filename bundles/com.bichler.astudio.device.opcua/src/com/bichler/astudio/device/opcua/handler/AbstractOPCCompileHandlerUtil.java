package com.bichler.astudio.device.opcua.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceNode;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.UAVariableNode;
import opc.sdk.core.node.UAVariableTypeNode;

public class AbstractOPCCompileHandlerUtil {
	
	private static Logger logger = Logger.getLogger(AbstractOPCCompileHandlerUtil.class.getName());
	
	public static List<Integer> getIndexFromNamespaceToExport(NamespaceTable nsTable, Object[] namespaces2export) {
		List<Integer> iList = new ArrayList<>();
		// create namespaces to export
		for (Object o : namespaces2export) {
			Integer index = nsTable.getIndex((String) o);
			if (index < 0) {
				continue;
			}
			if (!iList.contains(index)) {
				iList.add(index);
			}
		}
		return iList;
	}
	
	public static Map<Integer, List<Node>> getNodesForNamespacetableToExport(NamespaceTable nsTable, List<Integer> iList) {
		Map<Integer, List<Node>> list = new HashMap<>();
		// find additional namespaces which are requried
		List<Integer> mList = new ArrayList<>();
		// list with all namespace index without namespaces with no nodes
		List<Integer> rList = new ArrayList<Integer>();
		for (Integer index : iList) {
			Node[] addressSpace = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
					.getAllNodes(index);
			if (addressSpace != null && addressSpace.length > 0) {
				rList.add(index);
				for (Node n : addressSpace) {
					fetchNamespaceIndexFromNode(n, nsTable, iList, mList);
					List<Node> nodes = list.get(index);
					if (nodes == null) {
						nodes = new ArrayList<Node>();
						list.put(index, nodes);
					}
					nodes.add(n);
				}
			}
			else {
				rList.add(index);
				list.put(index, new ArrayList<Node>());
			}
		}
		for (Integer index : mList) {
			if (!iList.contains(index)) {
				iList.add(index);
			}
		}

		iList.clear();
		iList.addAll(rList);
		return list;
	}
	
	/**
	 * Used to compile information model. Only allowed target references are
	 * included
	 * 
	 * @param node
	 * @param nsTable
	 * @param iList      allowed namespaces to export
	 * @param collection
	 */
	public static void fetchNamespaceIndexFromNode(Node node, NamespaceTable nsTable, List<Integer> iList,
			List<Integer> collection) {
		NodeId nodeId = node.getNodeId();
		matchIndizes(collection, nodeId);
		NodeClass nodeClass = node.getNodeClass();
		NodeId dataTypeId = null;
		switch (nodeClass) {
		case Variable:
			dataTypeId = ((UAVariableNode) node).getDataType();
			matchIndizes(collection, dataTypeId);
			break;
		case VariableType:
			dataTypeId = ((UAVariableTypeNode) node).getDataType();
			matchIndizes(collection, dataTypeId);
			break;
		default:
			break;
		}
		ReferenceNode[] references = node.getReferences();
		if (references != null) {
			for (ReferenceNode reference : references) {
				// first match target if it should exported
				ExpandedNodeId targetId = reference.getTargetId();
				try {
					NodeId targetNodeId = nsTable.toNodeId(targetId);
					if (!NodeId.isNull(targetNodeId) && !iList.contains(targetNodeId.getNamespaceIndex())) {
						// skip
						continue;
					}
					matchIndizes(collection, targetNodeId);
					// check reference type
					NodeId refTypId = reference.getReferenceTypeId();
					matchIndizes(collection, refTypId);
				} catch (ServiceResultException e) {
					logger.log(Level.SEVERE, e.getMessage());
				}
			}
		}
	}
	
	/**
	 * Used to compile device models. Target referenced nodes are included to the
	 * exported namespace table.
	 * 
	 * @param node
	 * @param nsTable
	 * @param collection
	 */
	public static void fetchNamespaceIndexFromNode(Node node, NamespaceTable nsTable, List<Integer> collection) {
		NodeId nodeId = node.getNodeId();
		matchIndizes(collection, nodeId);
		NodeClass nodeClass = node.getNodeClass();
		NodeId dataTypeId = null;
		switch (nodeClass) {
		case Variable:
			dataTypeId = ((UAVariableNode) node).getDataType();
			matchIndizes(collection, dataTypeId);
			break;
		case VariableType:
			dataTypeId = ((UAVariableTypeNode) node).getDataType();
			matchIndizes(collection, dataTypeId);
			break;
		default:
			break;
		}
		ReferenceNode[] references = node.getReferences();
		if (references != null) {
			for (ReferenceNode reference : references) {
				// first match target if it should exported
				ExpandedNodeId targetId = reference.getTargetId();
				try {
					NodeId targetNodeId = nsTable.toNodeId(targetId);
					matchIndizes(collection, targetNodeId);
				} catch (ServiceResultException e) {
					logger.log(Level.SEVERE, e.getMessage());
				}
				// check reference type
				NodeId refTypId = reference.getReferenceTypeId();
				matchIndizes(collection, refTypId);
			}
		}
	}

	public static NamespaceTable createNamespaceTableToExport(NamespaceTable serverNS, List<Integer> collection) {
		NamespaceTable exportTable = new NamespaceTable();
		for (Integer i : collection) {
			String uri = serverNS.getUri(i);
			exportTable.add(uri);
		}
		return exportTable;
	}
	
	public static void matchIndizes(List<Integer> collection, NodeId nodeId) {
		if (NodeId.isNull(nodeId)) {
			return;
		}
		Integer index = nodeId.getNamespaceIndex();
		if (!collection.contains(index)) {
			collection.add(index);
		}
	}
}
