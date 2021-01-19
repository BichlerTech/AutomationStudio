package com.bichler.astudio.opcua.addressspace.xml.exporter;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceNode;

import com.bichler.astudio.opcua.addressspace.xml.exporter.factory.IModelFactory;

import opc.sdk.core.application.operation.ICancleOperation;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.UAVariableNode;
import opc.sdk.core.node.UAVariableTypeNode;
import opc.sdk.server.core.OPCInternalServer;

public abstract class AbstractModelExporter {

	ICancleOperation progressMonitor;
	int workCount;
	OPCInternalServer serverInstance;
	NamespaceTable serverNSTable;
//	private StringTable serverUriTable;
//	private TypeTable typeTable;

	public AbstractModelExporter(OPCInternalServer sInstance) {
		this.serverNSTable = sInstance.getNamespaceUris();
//		this.serverUriTable = sInstance.getServerUris();
//		this.typeTable = sInstance.getTypeTable();
		this.serverInstance = sInstance;
	}

	public boolean writeNodes(FileOutputStream out, List<String> namespaces, IServerTypeModel typeModel,
			Map<Integer, List<Node>> nodes) throws IOException {
		// check if there is something
		if (nodes == null || nodes.size() <= 0) {
			return true;
		}
		if (checkProgressMonitorCancled()) {
			return false;
		}
		// <NamespaceUris>
		NamespaceTable nsTable = new NamespaceTable();
		for (String namespace : namespaces) {
			nsTable.add(namespace);
		}

		for (Entry<Integer, List<Node>> e : nodes.entrySet()) {
			NamespaceTable foundNs = preFindNamespaces(e.getValue().toArray(new Node[0]));

			for (String found : foundNs.toArray()) {
				try {
					nsTable.add(found);
				} catch (IllegalArgumentException iae) {
					// skip
				}
			}

			if (checkProgressMonitorCancled()) {
				return false;
			}
		}

		List<Node> allNodes = new ArrayList<>();
		for (Entry<Integer, List<Node>> e : nodes.entrySet()) {
			allNodes.addAll(e.getValue());
		}

		List<Node> sorted = sortNodes(allNodes);

		// find all required namespaces
		Map<String, List<String>> nsRequiredTable = preFindRequiredNamespaces(sorted.toArray(new Node[0]));
		if (checkProgressMonitorCancled()) {
			return false;
		}

		// open stream
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
		return export(writer, sorted, nsTable, nsRequiredTable, typeModel);
	}

	public void setProgressMonitor(ICancleOperation monitor, int workCount) {
		this.progressMonitor = monitor;
		this.workCount = workCount;
	}

	boolean export(BufferedWriter out, List<Node> nodes, NamespaceTable namespaces,
			Map<String, List<String>> nsRequiredTable, IServerTypeModel typeModel) throws IOException {

		IModelFactory factory = createModelFactory();
		return factory.export(out, nodes, namespaces, nsRequiredTable, typeModel);
	}

	boolean checkProgressMonitorCancled() {
		if (this.progressMonitor != null && this.progressMonitor.isCanceled()) {
			return true;
		}
		return false;
	}

	NamespaceTable preFindNamespaces(Node[] nodes) {
		NamespaceTable nsTable = new NamespaceTable();
		for (Node node : nodes) {
			if (checkProgressMonitorCancled()) {
				return null;
			}
			namespacesForNode(nsTable, node);
		}
		return nsTable;
	}

	Map<String, List<String>> preFindRequiredNamespaces(Node[] nodes) {
		Map<String, List<String>> nsTable = new HashMap<>();
		Set<NodeId> breaker = new HashSet<>();
		for (Node node : nodes) {
			if (checkProgressMonitorCancled()) {
				return null;
			}

			int index = node.getNodeId().getNamespaceIndex();
			if (index == serverNSTable.getIndex(NamespaceTable.OPCUA_NAMESPACE)) {
				// skip default opc ua nodes
				continue;
			}

			String uri = serverNSTable.getUri(index);
			List<String> requiredModel = nsTable.get(uri);
			if (requiredModel == null) {
				requiredModel = new ArrayList<>();
				requiredModel.add(NamespaceTable.OPCUA_NAMESPACE);
				nsTable.put(uri, requiredModel);
			}

			requiredNamespaceForNode(breaker, requiredModel, uri, uri, node);
		}
		return nsTable;
	}

	private void addIndexToTable(int index, NamespaceTable nsTable) {
		String uri = this.serverNSTable.getUri(index);
		// add uri to table if required
		nsTable.add(uri);
	}

	private void addRequiredNamespaceToTable(Set<NodeId> breaker, List<String> requiredModel, String uri,
			String rekParentUri, NodeId nodeId) {
		int nodeIdIndex = nodeId.getNamespaceIndex();
		String nodeIdUri = serverNSTable.getUri(nodeIdIndex);

		// add unknown uri to table
		if (!uri.equalsIgnoreCase(nodeIdUri) && !requiredModel.contains(nodeIdUri)) {
			if (uri.equals(nodeIdUri)) {
//				requiredModel.add(nodeIdUri);
			} else if (requiredModel.contains(rekParentUri)) {
				int index = requiredModel.indexOf(rekParentUri);
				requiredModel.add(index, nodeIdUri);
			} else {
				requiredModel.add(nodeIdUri);
			}
		}

		// check for subtypes
		if (nodeIdIndex > 0 && !breaker.contains(nodeId)) {
			breaker.add(nodeId);
			requiredNamespaceForNode(breaker, requiredModel, uri, nodeIdUri,
					this.serverInstance.getAddressSpaceManager().getNodeById(nodeId));
		}
	}

	/**
	 *
	 * @param nsTable
	 * @param node
	 */
	private void namespacesForNode(NamespaceTable nsTable, Node node) {
		int index = node.getNodeId().getNamespaceIndex();
		if (index == nsTable.getIndex(NamespaceTable.OPCUA_NAMESPACE)) {
			// skip default object nodes
			return;
		}
		String uri = this.serverNSTable.getUri(index);
		// add nodeuri to table if required
		nsTable.add(uri);
		// find target references from different namespace
		ReferenceNode[] references = node.getReferences();
		if (references != null) {
			for (ReferenceNode reference : references) {
				ExpandedNodeId targetId = reference.getTargetId();
				NodeId localId = null;
				try {
					localId = this.serverNSTable.toNodeId(targetId);
					int refIndex = localId.getNamespaceIndex();
					addIndexToTable(refIndex, nsTable);
				} catch (ServiceResultException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				}
			}
		}
		// find datatype from different namespace
		NodeClass nodeClass = node.getNodeClass();
		NodeId dataType = null;
		if (nodeClass == NodeClass.Variable) {
			dataType = ((UAVariableNode) node).getDataType();
		} else if (nodeClass == NodeClass.VariableType) {
			dataType = ((UAVariableTypeNode) node).getDataType();
		}
		// datatype
		if (dataType != null) {
			int dtIndex = dataType.getNamespaceIndex();
			addIndexToTable(dtIndex, nsTable);
		}
	}

	private List<Node> sortNodes(List<Node> nodes) {
		LinkedList<Node> sorted = new LinkedList<>();
		Map<NodeId, Node> map = new HashMap<>();
		for (Node node : nodes) {
			map.put(node.getNodeId(), node);
		}
		// types
		rekSortNodes(Identifiers.ObjectTypesFolder, map, sorted);
		rekSortNodes(Identifiers.EventTypesFolder, map, sorted);
		rekSortNodes(Identifiers.VariableTypesFolder, map, sorted);
		rekSortNodes(Identifiers.ReferenceTypesFolder, map, sorted);
		rekSortNodes(Identifiers.DataTypesFolder, map, sorted);
		// objects
		rekSortNodes(Identifiers.ObjectsFolder, map, sorted);
		// remaining
		for (Node node : map.values()) {
			sorted.addLast(node);
		}

		return sorted;
	}

	private void rekSortNodes(NodeId nodeId, Map<NodeId, Node> nodes, LinkedList<Node> sorted) {
		Node[] children = serverInstance.getAddressSpaceManager().findChildren(nodeId);
		List<Node> childrenList = new ArrayList<>();
		for (Node child : children) {
			childrenList.add(child);
		}
		childrenList.sort(new Comparator<Node>() {

			@Override
			public int compare(Node o1, Node o2) {
				if (o1.getBrowseName() == null) {
					return 1;
				}
				if (o2.getBrowseName() == null) {
					return -1;
				}
				return o1.getBrowseName().toString().compareTo(o2.getBrowseName().toString());
			}

		});

		for (Node child : childrenList) {
			Node found = nodes.remove(child.getNodeId());
			if (found != null) {
				sorted.addLast(found);
			}

			rekSortNodes(child.getNodeId(), nodes, sorted);
		}
	}

	private void requiredNamespaceForNode(Set<NodeId> breaker, List<String> requiredModel, String uri,
			String rekParentUri, Node node) {
		// find target references from different namespace
		if (node == null) {
//			System.out.println();
			return;
		}
		ReferenceNode[] references = node.getReferences();
		if (references != null) {
			for (ReferenceNode reference : references) {
				ExpandedNodeId targetId = reference.getTargetId();
				NodeId localId = null;
				try {
					localId = serverNSTable.toNodeId(targetId);
					addRequiredNamespaceToTable(breaker, requiredModel, uri, rekParentUri, localId);
//					int refIndex = localId.getNamespaceIndex();
//					String refUri = serverNSTable.getUri(refIndex);
//
//					// add unknown uri to table
//					if (!uri.equalsIgnoreCase(refUri) && !requiredModel.contains(refUri)) {
//						if (!uri.equals(rekParentUri)) {
//							requiredModel.add(refUri);
//						} else if (requiredModel.contains(rekParentUri)) {
//							int index = requiredModel.indexOf(rekParentUri);
//							requiredModel.add(index, refUri);
//						}
//					}
//
//					// check for subtypes
//					if (refIndex > 0 && !breaker.contains(localId)) {
//						breaker.add(localId);
//						requiredNamespaceForNode(breaker, requiredModel, uri, refUri,
//								this.serverInstance.getAddressSpaceManager().getNodeById(localId));
//					}

				} catch (ServiceResultException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				}
			}
		}

		// find datatype from different namespace
		NodeClass nodeClass = node.getNodeClass();
		NodeId dataTypeId = null;
		if (nodeClass == NodeClass.Variable) {
			dataTypeId = ((UAVariableNode) node).getDataType();
		} else if (nodeClass == NodeClass.VariableType) {
			dataTypeId = ((UAVariableTypeNode) node).getDataType();
		}
		// datatype
		if (!NodeId.isNull(dataTypeId)) {
			addRequiredNamespaceToTable(breaker, requiredModel, uri, rekParentUri, dataTypeId);
		}
	}

	protected abstract IModelFactory createModelFactory();
}
