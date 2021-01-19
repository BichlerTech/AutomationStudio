package com.bichler.astudio.opcua.datadictionary.modeler.model;

import java.util.HashMap;
import java.util.Map;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AddNodesItem;
import org.opcfoundation.ua.core.AddNodesResult;
import org.opcfoundation.ua.core.AddReferencesItem;

import opc.sdk.core.node.Node;
import opc.sdk.server.core.OPCInternalServer;

public class OpcUtil {

	public static Node getNode(OPCInternalServer opcServer, NodeId nodeId) {
		return opcServer.getAddressSpaceManager().getNodeById(nodeId);
	}

	public static Node getNode(OPCInternalServer opcServer, ExpandedNodeId nodeId) {
		return opcServer.getAddressSpaceManager().getNodeById(nodeId);
	}

	public static AddNodesResult[] addNode(OPCInternalServer opcServer, AddNodesItem[] nodes, boolean includeChildren)
			throws ServiceResultException {
		Map<ExpandedNodeId, AddNodesItem> mappedNodes = new HashMap<>();
		for (AddNodesItem item : nodes) {
			mappedNodes.put(item.getRequestedNewNodeId(), item);
		}
		return opcServer.getMaster().addNodes(nodes, mappedNodes, includeChildren, null, false);
	}

	public static void addReference(OPCInternalServer opcServer, AddReferencesItem[] references)
			throws ServiceResultException {
		opcServer.getMaster().addReferences(references, null);
	}
	
	public static boolean isTypeOf(OPCInternalServer opcServer, NodeId subTypeId, NodeId superTypeId) {
		return opcServer.getTypeTable().isTypeOf(subTypeId, superTypeId);
	}

	
}
