package com.bichler.astudio.opcua.opcmodeler.wizards.opc.validation.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceDescription;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;

public class ValidationModelContentFactory {
	public static ReferenceDescription[] browse(NodeId nodeId, BrowseDirection direction, NodeId referenceId,
			UnsignedInteger nodeClassMask) {
		try {
			// BrowseDescription[] nodesToBrowse = new BrowseDescription[1];
			// BrowseDescription description = new BrowseDescription();
			// description.setBrowseDirection(direction);
			// description.setIncludeSubtypes(true);
			// description.setNodeClassMask(nodeClassMask);
			// description.setNodeId(nodeId);
			// description.setReferenceTypeId(referenceId);
			// description.setResultMask(BrowseResultMask.ALL);
			// nodesToBrowse[0] = description;
			BrowseResult[] result = ServerInstance.browse(nodeId, referenceId, NodeClass.getSet(nodeClassMask),
					BrowseResultMask.ALL, direction, true);
			if (result != null && result.length > 0 && result[0].getReferences() != null) {
				ReferenceDescription[] references = result[0].getReferences();
				return references;
			}
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
		return new ReferenceDescription[0];
	}

	public static ReferenceDescription[] browse(NodeId nodeId, BrowseDirection direction, NodeId referenceId) {
		return browse(nodeId, direction, referenceId, NodeClass.getMask(NodeClass.ALL));
	}

	private static ReferenceDescription[] browse(NodeId nodeId, BrowseDirection direction) {
		return browse(nodeId, direction, Identifiers.HierarchicalReferences);
	}

	protected static ReferenceDescription[] browse(NodeId nodeId) {
		return browse(nodeId, BrowseDirection.Forward);
	}

	protected static void updateValidationModelChildren(ValidationModel model) {
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		NodeId nodeId = model.getNodeId();
		ReferenceDescription[] results = ValidationModelContentFactory.browse(nodeId);
		for (ReferenceDescription refDesc : results) {
			ExpandedNodeId id = refDesc.getNodeId();
			NodeClass nClass = refDesc.getNodeClass();
			try {
				NodeId refId = nsTable.toNodeId(id);
				boolean exist = model.hasChild(refId);
				if (!exist) {
					ValidationModel child = new ValidationModel(refId, nClass, refDesc.getDisplayName());
					model.addChild(child);
				}
			} catch (ServiceResultException e) {
				e.printStackTrace();
			}
		}
	}

	private static void rekPath(List<NodeId> pathIds, NodeId nodeId) {
		pathIds.add(0, nodeId);
		ReferenceDescription[] results = browse(nodeId, BrowseDirection.Inverse);
		for (ReferenceDescription result : results) {
			try {
				ExpandedNodeId id = result.getNodeId();
				NodeId refId = ServerInstance.getInstance().getServerInstance().getNamespaceUris().toNodeId(id);
				rekPath(pathIds, refId);
				break;
			} catch (ServiceResultException e) {
				e.printStackTrace();
			}
		}
	}

	public static NodeId[] getSelectedPath(NodeId nodeId) {
		List<NodeId> pathIds = new ArrayList<>();
		rekPath(pathIds, nodeId);
		return pathIds.toArray(new NodeId[0]);
	}

	public static void setObjectModelSourceTreeInput(TreeViewer treeViewer, NodeId[] path) {
		ValidationModel root = new ValidationModel(Identifiers.RootFolder, NodeClass.Object, LocalizedText.EMPTY);
		treeViewer.setInput(root);
		// find last existing model
		ValidationModel last = null;
		int index = 0;
		for (NodeId id : path) {
			if (last == null && root.getNodeId().equals(id)) {
				last = root;
			} else {
				ValidationModel found = last.findChild(id);
				if (found == null) {
					break;
				}
				last = found;
			}
			index++;
		}
		// ValidationModel m2 = new ValidationModel(path[index + 1],
		// NodeClass.Method, LocalizedText.english("asdf"));
		// last.addChild(m2);
		last = followObjectModelSourceTreePath(last, path, index);
		// last.addChild(child);
		// for (int i = index; i < path.length; i++) {
		// NodeId new2 = path[i];
		//
		// }
		treeViewer.setSelection(new StructuredSelection(last));
	}

	private static ValidationModel followObjectModelSourceTreePath(ValidationModel last, NodeId[] path, int index) {
		if (path.length > index) {
			ReferenceDescription[] results = browse(last.getNodeId());
			for (ReferenceDescription refdesc : results) {
				ExpandedNodeId id = refdesc.getNodeId();
				try {
					NodeId refId = ServerInstance.getInstance().getServerInstance().getNamespaceUris().toNodeId(id);
					if (path[index].equals(refId)) {
						ValidationModel model = new ValidationModel(refId, refdesc.getNodeClass(),
								refdesc.getDisplayName());
						last.addChild(model);
						return followObjectModelSourceTreePath(model, path, index + 1);
					}
				} catch (ServiceResultException e) {
					e.printStackTrace();
				}
			}
		}
		return last;
	}
}
