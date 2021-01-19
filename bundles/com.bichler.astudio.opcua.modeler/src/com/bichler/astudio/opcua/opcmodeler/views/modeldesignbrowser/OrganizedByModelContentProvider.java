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

public class OrganizedByModelContentProvider implements ITreeContentProvider {
	private UnsignedInteger nodeClassFilter = null;

	public OrganizedByModelContentProvider(UnsignedInteger nodeClassFilter) {
		this.nodeClassFilter = nodeClassFilter;
	}

	public OrganizedByModelContentProvider() {
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
		if (Identifiers.HierarchicalReferences.equals(parentId)) {
			/** only return componentof node */
			for (Node child : children) {
				if (Identifiers.Organizes.equals(child.getNodeId())) {
					return new Object[] { child };
				}
			}
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
