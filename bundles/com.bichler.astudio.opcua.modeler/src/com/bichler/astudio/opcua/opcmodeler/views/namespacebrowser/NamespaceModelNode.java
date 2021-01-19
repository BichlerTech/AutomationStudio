package com.bichler.astudio.opcua.opcmodeler.views.namespacebrowser;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceDescription;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;

import opc.sdk.core.node.Node;

public class NamespaceModelNode {
	private Node node;
	private List<NamespaceModelNode> children = null;

	public NamespaceModelNode(Node node) {
		this.node = node;
	}

	public Node getNode() {
		return this.node;
	}

	public void addChild(NamespaceModelNode child) {
		this.children.add(child);
	}

	public NamespaceModelNode[] getChildren() {
		return this.children.toArray(new NamespaceModelNode[0]);
	}

	public void init() {
		if (this.children == null) {
			this.children = new ArrayList<>();
			if (this.node == null) {
				return;
			}
			BrowseDescription[] nodesToBrowse = new BrowseDescription[] { new BrowseDescription(
					(NodeId) this.node.getNodeId(), BrowseDirection.Forward, Identifiers.HierarchicalReferences, true,
					NodeClass.getMask(NodeClass.ALL), BrowseResultMask.getMask(BrowseResultMask.ALL)) };
			try {
				BrowseResult[] parent = ServerInstance.getInstance().getServerInstance().getMaster()
						.browse(nodesToBrowse, UnsignedInteger.ZERO, null, null);
				if (parent != null && parent.length > 0) {
					ReferenceDescription[] parentReferences = parent[0].getReferences();
					List<NodeId> nodeIds = new ArrayList<>();
					for (ReferenceDescription pRef : parentReferences) {
						NodeId id = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
								.toNodeId(pRef.getNodeId());
						Node child = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
								.getNodeById(id);
						addChild(new NamespaceModelNode(child));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
