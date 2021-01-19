package com.bichler.opcua.statemachine.addressspace;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceNode;

import com.bichler.opcua.statemachine.addressspace.importer.StatemachineNodesetImporter;

import opc.sdk.core.application.operation.ICancleOperation;
import opc.sdk.core.context.StringTable;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.UAVariableNode;
import opc.sdk.core.node.UAVariableTypeNode;

public abstract class AbstractModelExporter {

	ICancleOperation progressMonitor;
	int workCount;

	NamespaceTable serverNSTable;
	private StringTable serverUriTable;
	// private TypeTable typeTable;

	public AbstractModelExporter(NamespaceTable nsTable, StringTable serverUri) {
		this.serverNSTable = nsTable;
		this.serverUriTable = serverUri;
	}

	public boolean writeNodes(FileOutputStream out, StatemachineNodesetImporter importer, NamespaceTable nsTable,
			List<Node> nodes) throws IOException {
		// check if there is something
		if (nodes == null || nodes.size() <= 0) {
			return true;
		}
		if (checkProgressMonitorCancled()) {
			return false;
		}
		// <NamespaceUris>
		/*
		 * NamespaceTable nsTable = new NamespaceTable(); for (String namespace :
		 * namespaces) { nsTable.add(namespace); }
		 */

		for (Node e : nodes) {
			// String uri = serverNSTable.getUri(e.getKey());
			Node[] nodeArray = new Node[] { e };
			NamespaceTable foundNs = preFindNamespaces(nodeArray);

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
		for (Node e : nodes) {
			allNodes.add(e);
		}

		List<Node> sorted = sortNodes(importer, allNodes);

		// find all required namespaces
		Map<String, List<String>> nsRequiredTable = preFindRequiredNamespaces(allNodes.toArray(new Node[0]));
		if (checkProgressMonitorCancled()) {
			return false;
		}

		// open stream
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
		return export(writer, importer, sorted, nsTable, nsRequiredTable);
	}

	/*
	 * public boolean writeNodes(FileOutputStream out, List<String> namespaces,
	 * Map<Integer, List<Node>> nodes) throws IOException {
	 * 
	 * List<Node> nodes2 = new ArrayList<>(); for (Entry<Integer, List<Node>>
	 * nodeList : nodes.entrySet()) { nodes2.addAll(nodeList.getValue()); }
	 * 
	 * return writeNodes(out, namespaces, nodes2); }
	 */

	public void setProgressMonitor(ICancleOperation monitor, int workCount) {
		this.progressMonitor = monitor;
		this.workCount = workCount;
	}

	boolean export(BufferedWriter out, StatemachineNodesetImporter importer, List<Node> nodes,
			NamespaceTable namespaces, Map<String, List<String>> nsRequiredTable) throws IOException {

		IModelFactory factory = createModelFactory();
		return factory.export(out, importer, nodes, namespaces, nsRequiredTable);
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
			int index = node.getNodeId().getNamespaceIndex();
			if (index == nsTable.getIndex(NamespaceTable.OPCUA_NAMESPACE)) {
				// skip default object nodes
				continue;
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
						String refUri = this.serverNSTable.getUri(refIndex);
						// add uri to table if required
						nsTable.add(refUri);
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
				String dtUri = this.serverNSTable.getUri(dtIndex);
				// add uri to table if requried
				nsTable.add(dtUri);
			}
		}
		return nsTable;
	}

	Map<String, List<String>> preFindRequiredNamespaces(Node[] nodes) {
		Map<String, List<String>> nsTable = new HashMap<>();

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
				nsTable.put(uri, requiredModel);
			}

			// find target references from different namespace
			ReferenceNode[] references = node.getReferences();
			if (references != null) {
				for (ReferenceNode reference : references) {
					ExpandedNodeId targetId = reference.getTargetId();
					NodeId localId = null;
					try {
						localId = serverNSTable.toNodeId(targetId);
						int refIndex = localId.getNamespaceIndex();
						String refUri = serverNSTable.getUri(refIndex);
						// add unknown uri to table
						if (!uri.equalsIgnoreCase(refUri) && !requiredModel.contains(refUri)) {
							requiredModel.add(refUri);
						}
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
				String dtUri = serverNSTable.getUri(dtIndex);
				// add unknown uri to table
				if (!uri.equalsIgnoreCase(dtUri) && !requiredModel.contains(dtUri)) {
					requiredModel.add(dtUri);
				}
			}
		}
		return nsTable;
	}

	private void sortNodes(Map<Node, List<Node>> hierachy, LinkedList<Node> sorted, Map<NodeId, Node> map,
			Node parent) {
		List<Node> children = hierachy.get(parent);
		if (children == null) {
			return;
		}

		children.sort(new Comparator<Node>() {

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

		for (Node child : children) {
			sorted.add(child);
			map.remove(child.getNodeId());
			sortNodes(hierachy, sorted, map, child);
		}
	}

	private void sortNodesHierachy(StatemachineNodesetImporter importer, Map<Node, List<Node>> hierachy,
			Map<NodeId, Node> map) {
		for (Entry<NodeId, Node> entry : map.entrySet()) {
			Node value = entry.getValue();
			// get parent of node
			Node parent = findParent(importer, map, value);

			List<Node> children = hierachy.get(parent);
			if (children == null) {
				children = new ArrayList<>();
				hierachy.put(parent, children);
			}
			children.add(value);
		}
	}

	private List<Node> sortNodes(StatemachineNodesetImporter importer, List<Node> nodes) {
		LinkedList<Node> sorted = new LinkedList<>();

		Map<NodeId, Node> map = new HashMap<>();
		for (Node node : nodes) {
			map.put(node.getNodeId(), node);
		}

		Map<Node, List<Node>> hierachy = new HashMap<>();
		sortNodesHierachy(importer, hierachy, map);
		sortNodes(hierachy, sorted, map, null);

		// TODO: NOT WORKING

//		// types
//		rekSortNodes(importer, Identifiers.BaseObjectType, map, sorted);
//		rekSortNodes(importer, Identifiers.BaseEventType, map, sorted);
//		rekSortNodes(importer, Identifiers.BaseVariableType, map, sorted);
//		rekSortNodes(importer, Identifiers.ReferenceTypeNode, map, sorted);
//		rekSortNodes(importer, Identifiers.BaseDataType, map, sorted);
//		// objects
//		rekSortNodes(importer, Identifiers.ObjectsFolder, map, sorted);
//		// remaining
		for (Node node : map.values()) {
			sorted.addLast(node);
		}

		return sorted;
	}

	private Node findParent(StatemachineNodesetImporter importer, Map<NodeId, Node> map, Node node) {
		if (node.getReferences() == null) {
			return null;
		}

		for (ReferenceNode reference : node.getReferences()) {
			boolean isReference = importer.isInverseHierachical(reference);
			if (!isReference) {
				continue;
			}
			try {
				NodeId target = importer.getNamespaceTable().toNodeId(reference.getTargetId());
				return map.get(target);
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"Cannot convert ExpandedNodeId to NodeId " + reference.getTargetId());
			}
		}
		return null;
	}

//	private void rekSortNodes(StatemachineNodesetImporter importer, NodeId nodeId, Map<NodeId, Node> nodes,
//			LinkedList<Node> sorted) {
//		Node[] children = importer.findChildren(nodeId, nodes);
//		List<Node> childrenList = new ArrayList<>();
//		for (Node child : children) {
//			childrenList.add(child);
//		}
//		childrenList.sort(new Comparator<Node>() {
//
//			@Override
//			public int compare(Node o1, Node o2) {
//				if (o1.getBrowseName() == null) {
//					return 1;
//				}
//				if (o2.getBrowseName() == null) {
//					return -1;
//				}
//				return o1.getBrowseName().toString().compareTo(o2.getBrowseName().toString());
//			}
//
//		});
//
//		for (Node child : childrenList) {
//			Node found = nodes.remove(child.getNodeId());
//			if (found != null) {
//				sorted.addLast(found);
//			}
//
//			rekSortNodes(importer, child.getNodeId(), nodes, sorted);
//		}
//	}

	protected abstract IModelFactory createModelFactory();

	/*
	 * abstract void writeStartDocument(BufferedWriter out) throws IOException;
	 * 
	 * abstract void writeStartHeaderDocument(BufferedWriter out) throws
	 * IOException;
	 * 
	 * abstract void writeNamespaceTable(BufferedWriter out, String[] namespaceUris,
	 * String[] requiredModels) throws IOException;
	 * 
	 * abstract void writeServerTable(BufferedWriter out) throws IOException;
	 * 
	 * abstract void writeNodes(BufferedWriter out, Node[] nodes, NamespaceTable
	 * nsExport) throws IOException;
	 * 
	 * abstract void writeEndHeaderDocument(BufferedWriter out) throws IOException;
	 */
}
