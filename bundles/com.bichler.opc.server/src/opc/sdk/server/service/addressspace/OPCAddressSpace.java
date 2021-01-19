package opc.sdk.server.service.addressspace;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.ReferenceNode;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.mapper.NodeIdMapper;

public class OPCAddressSpace {
	private Map<NodeId, Node> nodes = null;

	public OPCAddressSpace() {
		this.nodes = new HashMap<>();
	}

	/**
	 * Only address space manager should access
	 * 
	 * @return
	 */
	public NodeId[] getNodeIds() {
		return this.nodes.keySet().toArray(new NodeId[0]);
	}

	public Node[] getNodes() {
		return this.nodes.values().toArray(new Node[0]);
	}

	public Node getNodeById(NodeId nodeId) {
		return this.nodes.get(nodeId);
	}

	public void addNode(NodeId nodeId, Node node) {
		// node already exist in address space
		if (this.nodes.containsKey(nodeId)) {
			// add new references
			ReferenceNode[] newReferences = node.getReferences();
			Node addressNode = this.nodes.get(nodeId);
			if (newReferences == null) {
				return;
			}
			for (ReferenceNode reference : newReferences) {
				addressNode.ensureReferenceExists(reference);
			}
		}
		// add new node
		else {
			this.nodes.put(nodeId, node);
		}
	}

	public Node removeNodeById(NodeId nodeId) {
		return this.nodes.remove(nodeId);
	}

	public boolean fillNodes(List<Node> nodes2fill) {
		return nodes2fill.addAll(this.nodes.values());
	}

	/**
	 * Map all nodeids in this addressspace.
	 * 
	 * @param NamespaceUris  Server namespace table to map expandednodeids.
	 * @param old2newMapping Mappingtable, Key is old nsindex, Value is new nsindex.
	 */
	public void mapNamespaceIndex(NamespaceTable namespaceUris, Map<Integer, Integer> old2newMapping,
			NamespaceTable newMappingTable) {
		// map all nodes
		Map<NodeId, Node> newNodes = new HashMap<>();
		for (Node node : this.nodes.values()) {
			NodeIdMapper.mapNode(namespaceUris, node, old2newMapping, newMappingTable, newNodes);
		}
		// set new mapped nodes
		this.nodes = newNodes;
	}
}
