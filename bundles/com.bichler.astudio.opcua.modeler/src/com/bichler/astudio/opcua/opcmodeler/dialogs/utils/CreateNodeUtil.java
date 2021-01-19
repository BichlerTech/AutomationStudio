package com.bichler.astudio.opcua.opcmodeler.dialogs.utils;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceDescription;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;

import opc.sdk.core.node.Node;

public class CreateNodeUtil {
	public static Object[] getNodeChildrenForTreeViewer(Object parentElement, UnsignedInteger nodeClassFilter) {
		List<Node> children = new ArrayList<Node>();
		List<Node> tmpchildren = null;
		BrowseResult[] result = null;
		// boolean modeling = false;
		// operate roots
		if (parentElement instanceof List<?>) {
			for (Object pElement : (List<?>) parentElement) {
				if (pElement instanceof NodeId) {
					NodeId element = (NodeId) pElement;

					try {
						result = ServerInstance.browse(element, Identifiers.HierarchicalReferences,
								NodeClass.getSet(nodeClassFilter), BrowseResultMask.ALL, BrowseDirection.Forward, true);
					} catch (ServiceResultException e) {
						e.printStackTrace();
					}
					/** we need to check if the element already exists */
					if (children.size() == 0) {
						tmpchildren = fetchBrowsedItems(result);
						for (Node node : tmpchildren) {
							children.add(node);
						}
					} else {
						tmpchildren = fetchBrowsedItems(result);
						/**
						 * now check every node if it is already contained in the children list
						 */
						/**
						 * if it already exists, add all new references and its values
						 */
						boolean found = false;
						for (Node node : tmpchildren) {
							found = false;
							// Node foundExisting = null;
							for (Node existing : children) {
								if (existing.getBrowseName().equals(node.getBrowseName())) {
									/**
									 * check if we need to insert an new reference should be done
									 */
									// foundExisting = existing;
									// updateReferences(node, foundExisting);
									found = true;
									break;
								}
							}
							/**
							 * if we found the node, so we remove it from list and insert the new one
							 */
							if (found) {
								// children.remove(foundExisting);
								continue;
							}
							children.add(node);
						}
					}
				}
			}
			return children.toArray();
		}
		// operate children
		if (parentElement instanceof Node) {
			NodeId parentId = ((Node) parentElement).getNodeId();
			try {
				result = ServerInstance.browse(parentId, Identifiers.HierarchicalReferences,
						NodeClass.getSet(nodeClassFilter), BrowseResultMask.ALL, BrowseDirection.Forward, true);
			} catch (ServiceResultException e) {
				e.printStackTrace();
			}
			tmpchildren = fetchBrowsedItems(result);
			for (Node node : tmpchildren) {
				children.add(node);
			}
			return children.toArray();
		}
		return new Object[0];
	}

	/*
	 * private static void updateReferences(Node existing, Node newone) {
	 * List<ReferenceNode> existingRefs = new ArrayList<ReferenceNode>();
	 * List<ReferenceNode> newRefs = new ArrayList<ReferenceNode>(); for
	 * (ReferenceNode ref : existing.getReferences()) { existingRefs.add(ref); } for
	 * (ReferenceNode ref : newone.getReferences()) { if (ref.getIsInverse()) {
	 * continue; } newRefs.add(ref); } boolean foundRef = false; /** add all new
	 * reference types to the existing node * for (ReferenceNode ref : newRefs) {
	 * for (ReferenceNode ex : existingRefs) { if
	 * (ex.getReferenceTypeId().equals(ref.getReferenceTypeId())) { foundRef = true;
	 * } } /** we couldn't find the reference, so add it to the existing node
	 *
	 * if (!foundRef) { existingRefs.add(ref); } } /** now set the new existing refs
	 * list to the existing node * existing.setReferences(existingRefs.toArray(new
	 * ReferenceNode[existingRefs.size()])); return; }
	 */

	private static List<Node> fetchBrowsedItems(BrowseResult[] browseResults) {
		List<Node> children = new ArrayList<Node>();
		for (ReferenceDescription desc : browseResults[0].getReferences()) {
			/** we also make a clone of the got node */
			Node node = ServerInstance.getNode(desc.getNodeId()).clone();
			children.add(node);
		}
		return children;
	}
}
