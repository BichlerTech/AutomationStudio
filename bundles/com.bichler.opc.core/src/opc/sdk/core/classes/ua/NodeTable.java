package opc.sdk.core.classes.ua;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import opc.sdk.core.node.Node;
import opc.sdk.core.types.TypeTable;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.ReferenceNode;

/**
 * A table of nodes
 * 
 * @author Thomas Z&ouml;bauer
 * 
 */
public class NodeTable {
	private NamespaceTable namespaceUris = null;
	private TypeTable typeTree = null;
	private ConcurrentMap<NodeId, Node> localNodes = null;
	private ConcurrentMap<ExpandedNodeId, Node> remoteNodes = null;

	/**
	 * Initializes the object
	 * 
	 * @param NamespaceUris
	 * @param ServerUris
	 * @param TypeTable
	 */
	public NodeTable(NamespaceTable namespaceUris, TypeTable typeTree) {
		this.namespaceUris = namespaceUris;
		this.typeTree = typeTree;
		this.localNodes = new ConcurrentHashMap<>();
		this.remoteNodes = new ConcurrentHashMap<>();
	}

	/**
	 * Adds a node to the table (takes ownership of the object passed in).
	 * 
	 * @param Node
	 */
	public void attach(Node node) {
		// remove duplicates
		if (exists(this.namespaceUris.toExpandedNodeId(node.getNodeId()))) {
			remove(this.namespaceUris.toExpandedNodeId(node.getNodeId()));
		}
		internalAdd(node);
		// add reverse reference
		for (ReferenceNode reference : node.getReferences()) {
			Node targetNode = find(reference.getTargetId());
			if (targetNode == null) {
				continue;
			}
			// type definition and modelling rule references are one way
			if (!Identifiers.HasTypeDefinition.equals(reference.getReferenceTypeId())
					&& !Identifiers.HasModellingRule.equals(reference.getReferenceTypeId())) {
				List<ReferenceNode> refList = new ArrayList<>(Arrays.asList(targetNode.getReferences()));
				refList.add(new ReferenceNode(reference.getReferenceTypeId(), !reference.getIsInverse(),
						this.namespaceUris.toExpandedNodeId(node.getNodeId())));
			}
		}
		// see if it is a type
		if (this.typeTree != null) {
			this.typeTree.add(node, null);
		}
	}

	/**
	 * Returns true if the node is in the table.
	 * 
	 * @param NodeId
	 * @return
	 */
	public boolean exists(ExpandedNodeId nodeId) {
		return internalFind(nodeId) != null;
	}

	/**
	 * Finds a node in the node set.
	 * 
	 * @param NodeId
	 * @return Node
	 */
	public Node find(ExpandedNodeId nodeId) {
		return internalFind(nodeId);
	}

	/**
	 * Removes a node from the table.
	 * 
	 * @param NodeId
	 * @return TRUE if the node is removed, otherwise FALSE.
	 */
	public boolean remove(ExpandedNodeId nodeId) {
		// find the target
		Node source = find(nodeId);
		if (source == null) {
			return false;
		}
		// remove references
		for (ReferenceNode refernence : source.getReferences()) {
			Node target = internalFind(refernence.getTargetId());
			if (target == null) {
				continue;
			}
			// remove remote node if nothing else references it.
			internalRemove(target);
		}
		internalRemove(source);
		return true;
	}

	/**
	 * Adds the node to the table.
	 * 
	 * @param Node
	 */
	private void internalAdd(Node node) {
		if (node == null || NodeId.isNull(node.getNodeId())) {
			return;
		}
		this.localNodes.put(node.getNodeId(), node);
	}

	/**
	 * Finds the specified node. Returns null if the node does node exist.
	 * 
	 * @param nodeId
	 * @return
	 */
	@SuppressWarnings("unlikely-arg-type")
	private Node internalFind(ExpandedNodeId nodeId) {
		if (ExpandedNodeId.isNull(nodeId)) {
			return null;
		}
		// check for remote node
		if (nodeId.getServerIndex() != null && nodeId.getServerIndex().intValue() != 0) {
			return this.remoteNodes.get(nodeId);
		}
		// convert to local nodeId
		NodeId localId = null;
		try {
			localId = this.namespaceUris.toNodeId(nodeId);
		} catch (ServiceResultException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
		}
		if (NodeId.isNull(localId)) {
			return null;
		}
		return this.localNodes.get(nodeId);
	}

	/**
	 * Removes the node from the table.
	 * 
	 * @param target
	 */
	private boolean internalRemove(Node node) {
		if (node == null || NodeId.isNull(node.getNodeId())) {
			return false;
		}
		Node removed = this.localNodes.remove(node.getNodeId());
		if (removed == null) {
			removed = this.remoteNodes.remove(this.namespaceUris.toExpandedNodeId(node.getNodeId()));
		}
		return removed != null;
	}

	public int size() {
		return this.localNodes.size();
	}
}
