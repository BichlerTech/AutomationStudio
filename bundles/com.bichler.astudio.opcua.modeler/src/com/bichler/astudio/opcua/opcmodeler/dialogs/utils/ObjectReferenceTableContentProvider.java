package com.bichler.astudio.opcua.opcmodeler.dialogs.utils;

import java.util.ArrayList;
import java.util.List;

import opc.sdk.core.node.Node;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.ReferenceNode;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;

public class ObjectReferenceTableContentProvider implements IStructuredContentProvider {
	private NodeId filter = null;

	public ObjectReferenceTableContentProvider(NodeId filter) {
		this.filter = filter;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
	}

	@Override
	public Object[] getElements(Object inputElement) {
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		// checks the inputed Object
		List<ReferenceNode> reference = new ArrayList<ReferenceNode>();
		// a node means to find out what type it is!
		if (inputElement instanceof Node) {
			ExpandedNodeId ref = ((Node) inputElement).findTarget(Identifiers.HasTypeDefinition, false);
			NodeId refNodeId = null;
			// the inputed element is an type
			NodeId nodeId = ((Node) inputElement).getNodeId();
			if (ref == null) {
				ref = new ExpandedNodeId(nodeId);
				// ref = ServerInstance.getInstance().getServerInstance()
				// .getNamespaceUris()
				// .toExpandedNodeId(((Node) inputElement).getNodeId());
			}
			try {
				refNodeId = ServerInstance.getInstance().getServerInstance().getNamespaceUris().toNodeId(ref);
			} catch (ServiceResultException e) {
				e.printStackTrace();
			}
			ExpandedNodeId expNodeId = new ExpandedNodeId(nodeId);
			// ExpandedNodeId nodeId = ServerInstance.getInstance()
			// .getServerInstance().getNamespaceUris()
			// .toExpandedNodeId(((Node) inputElement).getNodeId());
			ReferenceNode rn = checkSubtypesOfRef(refNodeId, expNodeId);
			reference.add(rn);
		}
		return reference.toArray();
	}

	private ReferenceNode checkSubtypesOfRef(NodeId refNodeId, ExpandedNodeId targetNodeId) {
		NodeId ref = null;
		try {
			ref = ServerInstance.getInstance().getServerInstance().getNamespaceUris().toNodeId(targetNodeId);
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
		if (Identifiers.FolderType.equals(refNodeId) && !Identifiers.FolderType.equals(ref)) {
			ReferenceNode rn = new ReferenceNode();
			rn.setIsInverse(true);
			rn.setReferenceTypeId(Identifiers.Organizes);
			rn.setTargetId(targetNodeId);
			return rn;
		} else if (filter.equals(refNodeId)) {
			ReferenceNode rn = new ReferenceNode();
			rn.setIsInverse(true);
			rn.setReferenceTypeId(Identifiers.HasComponent);
			rn.setTargetId(targetNodeId);
			return rn;
		} else if (ref.equals(refNodeId)) {
			ReferenceNode rn = new ReferenceNode();
			rn.setIsInverse(true);
			rn.setReferenceTypeId(Identifiers.HasComponent);
			rn.setTargetId(targetNodeId);
			return rn;
		}
		NodeId superType = ServerInstance.getInstance().getServerInstance().getTypeTable().findSuperType(refNodeId);
		return checkSubtypesOfRef(superType, targetNodeId);
	}
}
