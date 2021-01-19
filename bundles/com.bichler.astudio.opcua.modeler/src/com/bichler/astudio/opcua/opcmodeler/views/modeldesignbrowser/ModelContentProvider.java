package com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
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
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserDataTypeInternalModelNode;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserDataTypeModelNode;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserEventTypeInternalModelNode;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserEventTypeModelNode;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserFolderInternalModelNode;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserFolderModelNode;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserInternalModelNode;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserMethodInternalModelNode;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserMethodModelNode;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserObjectInternalModelNode;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserObjectModelNode;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserObjectTypeInternalModelNode;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserObjectTypeModelNode;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserReferenceTypeInternalModelNode;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserReferenceTypeModelNode;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserVariableInternalModelNode;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserVariableModelNode;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserVariableTypeInternalModelNode;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserVariableTypeModelNode;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserViewInternalModelNode;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserViewModelNode;

import opc.sdk.core.node.Node;
import opc.sdk.core.types.TypeTable;

public class ModelContentProvider implements ITreeContentProvider {
	private UnsignedInteger nodeClassFilter = null;
	private NodeId id;

	// private BrowserModelNode browsermodelnode = new BrowserModelNode(null);
	public ModelContentProvider() {
		this.nodeClassFilter = NodeClass.getMask(NodeClass.ALL);
	}

	public ModelContentProvider(UnsignedInteger nodeClassFilter) {
		this.nodeClassFilter = nodeClassFilter;
	}

	public ModelContentProvider(UnsignedInteger mask, NodeId nodeId) {
		this(mask);
		this.id = nodeId;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		BrowserModelNode[] children = new BrowserModelNode[0];
		// this is the first visible node entry point
		if (!NodeId.isNull(id) && parentElement != null && ((BrowserModelNode) parentElement).getParent() == null
				&& !((BrowserModelNode) parentElement).getNode().getNodeId().equals(this.id)) {
			Node idNode = ServerInstance.getNode(id);
			BrowserModelNode y = new BrowserModelNode(null);
			y.setNode(idNode);
			List<BrowserModelNode> child = new ArrayList<BrowserModelNode>();
			child.add(y);
			return child.toArray();
		}
		if (parentElement != null && ((BrowserModelNode) parentElement).getNode() != null) {
			NodeId parentId = ((BrowserModelNode) parentElement).getNode().getNodeId();
			BrowseResult[] result = null;
			try {
				result = ServerInstance.browse(parentId, Identifiers.HierarchicalReferences,
						NodeClass.getSet(this.nodeClassFilter), BrowseResultMask.ALL, BrowseDirection.Forward, true);
			} catch (ServiceResultException e) {
				e.printStackTrace();
			}
			// fetch children
			children = this.fetchBrowsedItems((BrowserModelNode) parentElement, result);
			BrowserModelNode[] currentChildren = ((BrowserModelNode) parentElement).getChildren();
			List<BrowserModelNode> newChildren = new ArrayList<>();
			for (BrowserModelNode child : children) {
				boolean found = false;
				NodeId childId = child.getNode().getNodeId();
				for (BrowserModelNode cc : currentChildren) {
					NodeId ccId = cc.getNode().getNodeId();
					if (childId.equals(ccId)) {
						newChildren.add(cc);
						found = true;
						break;
					}
				}
				if (!found) {
					newChildren.add(child);
				}
			}
			// only changed children
			children = newChildren.toArray(new BrowserModelNode[0]);
			((BrowserModelNode) parentElement).setChildren(children);
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

	/**
	 * private Node[] fetchBrowsedItems(BrowseResult[] browseResults, AddressSpace
	 * addressSpace) {
	 * 
	 * List<Node> children = new ArrayList<Node>();
	 * 
	 * for (ReferenceDescription desc : browseResults[0].getReferences()) { Node
	 * node = addressSpace.getNode(desc.getNodeId()); children.add(node); }
	 * 
	 * return children.toArray(new Node[children.size()]); }
	 * 
	 * @param parentElement
	 */
	private BrowserModelNode[] fetchBrowsedItems(BrowserModelNode parentElement, BrowseResult[] browseResults) {
		List<BrowserModelNode> children = new ArrayList<>();
		BrowserModelNode mnode = null;
		Node node = null;
		if (browseResults != null && browseResults.length > 0 && browseResults[0].getReferences() != null) {
			for (ReferenceDescription desc : browseResults[0].getReferences()) {
				try {
					node = ServerInstance.getNode(desc.getNodeId());
					mnode = getBrowserModelForNode(parentElement, node, ServerInstance.getInstance().getServerInstance()
							.getNamespaceUris().toNodeId(desc.getTypeDefinition()), desc.getNodeClass());
					if (mnode == null) {
						continue;
					}
					mnode.setNode(node);
					children.add(mnode);
				} catch (ServiceResultException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
				}
			}
		}
		return children.toArray(new BrowserModelNode[children.size()]);
	}

	/**
	 * Returns the image for the given item TODO: DIFFERENCE IN METHOD AND
	 * METHOD"TYPE" (IMAGE)
	 * 
	 * @param parent
	 * 
	 * @param typeDef
	 * @param nodeClass
	 * @return image
	 */
	private BrowserModelNode getBrowserModelForNode(BrowserModelNode parent, Node node, NodeId typeDef,
			NodeClass nodeClass/*
								 * , ReferenceNode[] references
								 */) {
		// type node
		if (Identifiers.ObjectsFolder.equals(node.getNodeId())) {
			return new BrowserFolderModelNode(parent);
		} else if (Identifiers.TypesFolder.equals(node.getNodeId())
				|| Identifiers.DataTypesFolder.equals(node.getNodeId())
				|| Identifiers.EventTypesFolder.equals(node.getNodeId())
				|| Identifiers.ObjectTypesFolder.equals(node.getNodeId())
				|| Identifiers.ReferenceTypesFolder.equals(node.getNodeId())
				|| Identifiers.VariableTypesFolder.equals(node.getNodeId())
				|| Identifiers.ViewsFolder.equals(node.getNodeId())) {
			// return browsermodelnode;
			return new BrowserInternalModelNode(parent);
		}
		if (NodeId.isNull(typeDef) || NodeClass.Method.equals(nodeClass)) {
			if (NodeClass.ObjectType.equals(nodeClass)) {
				/**
				 * we have also to check if we have an event type(is normal an object type)
				 */
				TypeTable typeTable = ServerInstance.getInstance().getServerInstance().getTypeTable();
				if (typeTable.isTypeOf(node.getNodeId(), Identifiers.BaseEventType)) {
					if (node.getNodeId().getNamespaceIndex() == 0) {
						return new BrowserEventTypeInternalModelNode(parent);
					} else {
						return new BrowserEventTypeModelNode(parent);
					}
				} else {
					if (node.getNodeId().getNamespaceIndex() == 0) {
						return new BrowserObjectTypeInternalModelNode(parent);
					} else {
						return new BrowserObjectTypeModelNode(parent);
					}
				}
			} else if (NodeClass.VariableType.equals(nodeClass)) {
				if (node.getNodeId().getNamespaceIndex() == 0) {
					return new BrowserVariableTypeInternalModelNode(parent);
				} else {
					return new BrowserVariableTypeModelNode(parent);
				}
			} else if (NodeClass.ReferenceType.equals(nodeClass)) {
				if (node.getNodeId().getNamespaceIndex() == 0) {
					return new BrowserReferenceTypeInternalModelNode(parent);
				} else {
					return new BrowserReferenceTypeModelNode(parent);
				}
			} else if (NodeClass.DataType.equals(nodeClass)) {
				if (node.getNodeId().getNamespaceIndex() == 0) {
					return new BrowserDataTypeInternalModelNode(parent);
				} else {
					return new BrowserDataTypeModelNode(parent);
				}
			} else if (NodeClass.View.equals(nodeClass)) {
				if (node.getNodeId().getNamespaceIndex() == 0) {
					return new BrowserViewInternalModelNode(parent);
				} else {
					return new BrowserViewModelNode(parent);
				}
			} else if (NodeClass.Method.equals(nodeClass)) {
				if (node.getNodeId().getNamespaceIndex() == 0) {
					return new BrowserMethodInternalModelNode(parent);
				} else {
					return new BrowserMethodModelNode(parent);
				}
			}
		} else if (Identifiers.FolderType.equals(typeDef)) {
			if (node.getNodeId().getNamespaceIndex() == 0) {
				return new BrowserFolderInternalModelNode(parent);
			} else {
				return new BrowserFolderModelNode(parent);
			}
		} else if (Identifiers.BaseObjectType.equals(typeDef)) {
			if (node.getNodeId().getNamespaceIndex() == 0) {
				return new BrowserObjectInternalModelNode(parent);
			} else {
				return new BrowserObjectModelNode(parent);
			}
		} else if (Identifiers.ObjectsFolder.equals(typeDef)) {
			if (node.getNodeId().getNamespaceIndex() == 0) {
				return new BrowserFolderInternalModelNode(parent);
			} else {
				return new BrowserFolderModelNode(parent);
			}
		} else if (Identifiers.BaseVariableType.equals(typeDef)) {
			if (node.getNodeId().getNamespaceIndex() == 0) {
				return new BrowserVariableInternalModelNode(parent);
			} else {
				return new BrowserVariableModelNode(parent);
			}
		} else if (NodeClass.Method.equals(nodeClass)) {
			// NodeId baseType = browseParentType(typeDef);
		}
		/*
		 * else if (Identifiers.References.equals(typeDef)) { labelImage =
		 * PlatformUI.getWorkbench().getSharedImages()
		 * .getImage(ISharedImages.IMG_DEF_VIEW); return labelImage; } else if
		 * (Identifiers.BaseEventType.equals(typeDef)) { labelImage =
		 * PlatformUI.getWorkbench().getSharedImages()
		 * .getImage(ISharedImages.IMG_TOOL_REDO); return labelImage; }
		 */
		// lookup for some type
		else {
			NodeId baseType = browseParentType(typeDef);
			return getBrowserModelForNode(parent, node, baseType, nodeClass/*
																			 * , references
																			 */);
		}
		return null;
	}

	/**
	 * Browses the parents type
	 * 
	 * @param superType
	 * @return
	 */
	private NodeId browseParentType(NodeId superType) {
		// AddressSpace addressSpace = ServerInstance.getInstance()
		// .getServerInstance().getAddressSpace();
		// MasterNodeManager manager = ServerInstance.getInstance()
		// .getServerInstance().getMasterNodeManager();
		// TypeTable typeTree = ServerInstance.getInstance().getServerInstance()
		// .getTypeTree();
		// BrowseDescription[] nodesToBrowse = { new
		// BrowseDescription(superType,
		// BrowseDirection.Inverse, Identifiers.HasSubtype, true,
		// NodeClass.getMask(Arrays.asList(NodeClass.values())),
		// BrowseResultMask.getMask(Arrays.asList(BrowseResultMask
		// .values()))) };
		BrowseResult[] result = null;
		try {
			result = ServerInstance.browse(superType, Identifiers.HasSubtype, NodeClass.ALL, BrowseResultMask.ALL,
					BrowseDirection.Inverse, true);
		} catch (ServiceResultException e1) {
			e1.printStackTrace();
		}
		NodeId baseTypeDef = null;
		try {
			baseTypeDef = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
					.toNodeId(result[0].getReferences()[0].getNodeId());
		} catch (ServiceResultException e) {
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException npe) {
			return null;
		} catch (NullPointerException npe) {
			return null;
		}
		return baseTypeDef;
	}
}
