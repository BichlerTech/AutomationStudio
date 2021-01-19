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
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceNode;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;

public class TypesReferenceTableContentProvider implements IStructuredContentProvider {
	private NodeId filter = null;
	private NodeClass nodeClass = null;

	public TypesReferenceTableContentProvider(NodeId filter, NodeClass nodeClass) {
		this.filter = filter;
		this.nodeClass = nodeClass;
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
			NodeId nodeId = ((Node) inputElement).getNodeId();
			// the inputed element is an type
			if (ref == null) {
				ref = new ExpandedNodeId(nsTable.getUri(nodeId.getNamespaceIndex()), nodeId.getValue(), nsTable);
				// ref = ServerInstance.getInstance().getServerInstance()
				// .getNamespaceUris()
				// .toExpandedNodeId(((Node) inputElement).getNodeId());
			}
			try {
				refNodeId = ServerInstance.getInstance().getServerInstance().getNamespaceUris().toNodeId(ref);
			} catch (ServiceResultException e) {
				e.printStackTrace();
			}
			ExpandedNodeId expNodeId = new ExpandedNodeId(nsTable.getUri(nodeId.getNamespaceIndex()), nodeId.getValue(),
					nsTable);
			// ExpandedNodeId nodeId = ServerInstance.getInstance()
			// .getServerInstance().getNamespaceUris()
			// .toExpandedNodeId(((Node) inputElement).getNodeId());
			ReferenceNode rn = checkSubtypesOfRef(refNodeId, expNodeId, ((Node) inputElement).getNodeClass());
			reference.add(rn);
		} else if (inputElement instanceof ReferenceNode[]) {
			for (ReferenceNode node : (ReferenceNode[]) inputElement) {
				reference.add(node);
			}
		} else if (inputElement instanceof ReferenceNode) {
			reference.add((ReferenceNode) inputElement);
		}
		return reference.toArray();
	}

	private ReferenceNode checkSubtypesOfRef(NodeId refNodeId, ExpandedNodeId targetNodeId, NodeClass nodeClass) {
		NodeId ref = null;
		try {
			ref = ServerInstance.getInstance().getServerInstance().getNamespaceUris().toNodeId(targetNodeId);
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
		/**
		 * -------------------------------------------------
		 */
		if (Identifiers.FolderType.equals(refNodeId) && !Identifiers.FolderType.equals(ref)) {
			ReferenceNode rn = new ReferenceNode();
			rn.setIsInverse(true);
			rn.setReferenceTypeId(Identifiers.Organizes);
			rn.setTargetId(targetNodeId);
			return rn;
		}
		/**
		 * -------------------------------------------------
		 */
		if (ServerInstance.getNode(ref).getNodeClass().equals(NodeClass.Method)) {
			ReferenceNode rn = new ReferenceNode();
			rn.setIsInverse(true);
			rn.setReferenceTypeId(Identifiers.HasComponent);
			rn.setTargetId(targetNodeId);
			return rn;
		}
		/**
		 * -------------------------------------------------
		 */
		if (filter.equals(refNodeId)) {
			// object cannot be its subtype of parent object
			// variable cannot be its subtype of parent variable
			if ((this.nodeClass == NodeClass.Object && nodeClass == NodeClass.Object)
					|| (this.nodeClass == NodeClass.Variable && nodeClass == NodeClass.Variable)) {
				// skip
			} else {
				ReferenceNode rn = new ReferenceNode();
				rn.setIsInverse(true);
				rn.setReferenceTypeId(Identifiers.HasSubtype);
				rn.setTargetId(targetNodeId);
				return rn;
			}
		}
		/**
		 * -------------------------------------------------
		 */
		if (Identifiers.BaseObjectType.equals(refNodeId)) {
			ReferenceNode rn = new ReferenceNode();
			rn.setIsInverse(true);
			rn.setReferenceTypeId(Identifiers.HasComponent);
			rn.setTargetId(targetNodeId);
			return rn;
		}
		if (Identifiers.BaseVariableType.equals(refNodeId)) {
			ReferenceNode rn = new ReferenceNode();
			rn.setIsInverse(true);
			rn.setReferenceTypeId(Identifiers.HasComponent);
			rn.setTargetId(targetNodeId);
			return rn;
		}
		/**
		 * -------------------------------------------------
		 */
		if (ref.equals(refNodeId)) {
			if (this.filter.equals(Identifiers.BaseVariableType)) {
				ReferenceNode rn = new ReferenceNode();
				switch (this.nodeClass) {
				case Variable:
					rn.setIsInverse(true);
					rn.setReferenceTypeId(Identifiers.HasComponent);
					rn.setTargetId(targetNodeId);
					break;
				case VariableType:
					rn.setIsInverse(true);
					rn.setReferenceTypeId(Identifiers.HasSubtype);
					rn.setTargetId(targetNodeId);
				default:
					break;
				}
				return rn;
			}
			/**
			 * -------------------------------------------------
			 */
			if (this.filter.equals(Identifiers.BaseObjectType)) {
				ReferenceNode rn = new ReferenceNode();
				switch (this.nodeClass) {
				case ObjectType:
					rn.setIsInverse(true);
					rn.setReferenceTypeId(Identifiers.HasSubtype);
					rn.setTargetId(targetNodeId);
					break;
				case Object:
					rn.setIsInverse(true);
					rn.setReferenceTypeId(Identifiers.HasComponent);
					rn.setTargetId(targetNodeId);
					break;
				case DataType:
					break;
				case Method:
					break;
				case ReferenceType:
					break;
				case Unspecified:
					break;
				case Variable:
					break;
				case VariableType:
					break;
				case View:
					break;
				default:
					break;
				}
				return rn;
			}
		}
		NodeId superType = ServerInstance.getInstance().getServerInstance().getTypeTable().findSuperType(refNodeId);
		return checkSubtypesOfRef(superType, targetNodeId, nodeClass);
	}
}
