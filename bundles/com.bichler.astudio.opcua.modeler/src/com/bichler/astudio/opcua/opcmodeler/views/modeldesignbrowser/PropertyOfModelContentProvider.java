package com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import opc.sdk.core.node.Node;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceDescription;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;

public class PropertyOfModelContentProvider implements ITreeContentProvider {
	private UnsignedInteger nodeClassFilter = null;

	public PropertyOfModelContentProvider(UnsignedInteger nodeClassFilter) {
		this.nodeClassFilter = nodeClassFilter;
	}

	public PropertyOfModelContentProvider() {
		this.nodeClassFilter = NodeClass.getMask(NodeClass.ALL);
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		NodeId parentId = ((Node) parentElement).getNodeId();
		// AddressSpace addressSpace = ServerInstance.getInstance()
		// .getServerInstance().getAddressSpace();
		// MasterNodeManager manager = ServerInstance.getInstance()
		// .getServerInstance().getMasterNodeManager();
		// TypeTable typeTree = ServerInstance.getInstance().getServerInstance()
		// .getTypeTree();
		BrowseDescription[] nodesToBrowse = {
				new BrowseDescription(parentId, BrowseDirection.Forward, Identifiers.HierarchicalReferences, true,
						this.nodeClassFilter, BrowseResultMask.getMask(Arrays.asList(BrowseResultMask.values()))) };
		BrowseResult[] result = null;
		try {
			result = ServerInstance.browse(parentId, Identifiers.HierarchicalReferences,
					NodeClass.getSet(this.nodeClassFilter), BrowseResultMask.ALL, BrowseDirection.Forward, true);
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
		Node[] children = fetchBrowsedItems(result);
		/** now we are in aggregates folder */
		if (Identifiers.Aggregates.equals(parentId)) {
			List<Node> ch = new ArrayList<Node>();
			/** only return organized by node */
			for (Node child : children) {
				if (Identifiers.HasProperty.equals(child.getNodeId())) {
					ch.add(child);
				}
			}
			return ch.toArray();
		}
		return (Object[]) children;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public void dispose() {
	}

	private Node[] fetchBrowsedItems(BrowseResult[] browseResults) {
		List<Node> children = new ArrayList<Node>();
		for (ReferenceDescription desc : browseResults[0].getReferences()) {
			Node node = ServerInstance.getNode(desc.getNodeId());
			children.add(node);
		}
		return children.toArray(new Node[children.size()]);
	}
}
