package com.bichler.astudio.opcua.components.ui.modelbrowser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import opc.sdk.core.node.Node;
import opc.sdk.core.types.TypeTable;
import opc.sdk.server.core.OPCInternalServer;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceNode;

import com.bichler.astudio.opcua.components.ui.modelbrowser.nodes.OPCUABrowserDataTypeInternalModelNode;
import com.bichler.astudio.opcua.components.ui.modelbrowser.nodes.OPCUABrowserDataTypeModelNode;
import com.bichler.astudio.opcua.components.ui.modelbrowser.nodes.OPCUABrowserEventTypeInternalModelNode;
import com.bichler.astudio.opcua.components.ui.modelbrowser.nodes.OPCUABrowserEventTypeModelNode;
import com.bichler.astudio.opcua.components.ui.modelbrowser.nodes.OPCUABrowserFolderInternalModelNode;
import com.bichler.astudio.opcua.components.ui.modelbrowser.nodes.OPCUABrowserFolderModelNode;
import com.bichler.astudio.opcua.components.ui.modelbrowser.nodes.OPCUABrowserInternalModelNode;
import com.bichler.astudio.opcua.components.ui.modelbrowser.nodes.OPCUABrowserMethodInternalModelNode;
import com.bichler.astudio.opcua.components.ui.modelbrowser.nodes.OPCUABrowserMethodModelNode;
import com.bichler.astudio.opcua.components.ui.modelbrowser.nodes.OPCUABrowserModelNode;
import com.bichler.astudio.opcua.components.ui.modelbrowser.nodes.OPCUABrowserObjectTypeInternalModelNode;
import com.bichler.astudio.opcua.components.ui.modelbrowser.nodes.OPCUABrowserObjectTypeModelNode;
import com.bichler.astudio.opcua.components.ui.modelbrowser.nodes.OPCUABrowserReferenceTypeInternalModelNode;
import com.bichler.astudio.opcua.components.ui.modelbrowser.nodes.OPCUABrowserReferenceTypeModelNode;
import com.bichler.astudio.opcua.components.ui.modelbrowser.nodes.OPCUABrowserVariableInternalModelNode;
import com.bichler.astudio.opcua.components.ui.modelbrowser.nodes.OPCUABrowserVariableModelNode;
import com.bichler.astudio.opcua.components.ui.modelbrowser.nodes.OPCUABrowserVariableTypeInternalModelNode;
import com.bichler.astudio.opcua.components.ui.modelbrowser.nodes.OPCUABrowserVariableTypeModelNode;
import com.bichler.astudio.opcua.components.ui.modelbrowser.nodes.OPCUABrowserViewInternalModelNode;
import com.bichler.astudio.opcua.components.ui.modelbrowser.nodes.OPCUABrowserViewModelNode;


public class OPCUAModelContentProvider implements ITreeContentProvider {

	private OPCInternalServer internalServer;
	
	private UnsignedInteger nodeClassFilter = null;
	
	private OPCUABrowserModelNode browsermodelnode = new OPCUABrowserModelNode();

	public OPCUAModelContentProvider(UnsignedInteger nodeClassFilter){
		this.setNodeClassFilter(nodeClassFilter);
	}
	
	public OPCUAModelContentProvider() {
		this.setNodeClassFilter(NodeClass.getMask(NodeClass.ALL));
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		OPCUABrowserModelNode[]children = new OPCUABrowserModelNode[0];
		
		if(parentElement != null && ((OPCUABrowserModelNode) parentElement).getNode() != null) {
			NodeId parentId = ((OPCUABrowserModelNode) parentElement).getNode().getNodeId();

			//AddressSpace addressSpace = internalServer.getMasterNodeManager().getAddressSpaceManager().
			
			
			Node[] result = null;
			result  = internalServer
						.getAddressSpaceManager().findChildren(parentId);
		
			/*
			Node[] children = fetchBrowsedItems(
				result.toArray(new BrowseResult[result.size()]), addressSpace);
			 */
			children = this.fetchBrowsedItems(result);
		}
		return (Object[])children;
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
	private Node[] fetchBrowsedItems(BrowseResult[] browseResults,
			AddressSpace addressSpace) {

		List<Node> children = new ArrayList<Node>();

		for (ReferenceDescription desc : browseResults[0].getReferences()) {
			Node node = addressSpace.getNode(desc.getNodeId());
			children.add(node);
		}

		return children.toArray(new Node[children.size()]);
	} */
	
	private OPCUABrowserModelNode[] fetchBrowsedItems(Node[] nodes) {

		List<OPCUABrowserModelNode> children = new ArrayList<OPCUABrowserModelNode>();
		OPCUABrowserModelNode mnode = null;
		for (Node node : nodes) {
			
			mnode = new OPCUABrowserModelNode();
			mnode.setNode(node);
			
		}

		return children.toArray(new OPCUABrowserModelNode[children.size()]);
	}
	
	/**
	 * Returns the image for the given item TODO: DIFFERENCE IN METHOD AND
	 * METHOD"TYPE" (IMAGE)
	 * 
	 * @param typeDef
	 * @param nodeClass
	 * @return image
	 */
	@SuppressWarnings("unused")
	private OPCUABrowserModelNode getBrowserModelForNode(Node node, NodeId typeDef, NodeClass nodeClass, ReferenceNode[] references) {
		
		// type node
		if(Identifiers.ObjectsFolder.equals(node.getNodeId())) {
			return new OPCUABrowserFolderModelNode();
		} else if(Identifiers.TypesFolder.equals(node.getNodeId()) ||
				Identifiers.DataTypesFolder.equals(node.getNodeId()) ||
				Identifiers.EventTypesFolder.equals(node.getNodeId()) ||
				Identifiers.ObjectTypesFolder.equals(node.getNodeId()) ||
				Identifiers.ReferenceTypesFolder.equals(node.getNodeId()) ||
				Identifiers.VariableTypesFolder.equals(node.getNodeId()) ||
				Identifiers.ViewsFolder.equals(node.getNodeId())
				) {
//			return browsermodelnode;
			return new OPCUABrowserInternalModelNode();
		}
		if (NodeId.isNull(typeDef) || NodeClass.Method.equals(nodeClass)) {
			if (NodeClass.ObjectType.equals(nodeClass)) {
				/** we have also to check if we have an event type(is normal an object type) */
				TypeTable typeTable = internalServer
						.getTypeTable();
				if(typeTable.isTypeOf(node.getNodeId(), Identifiers.BaseEventType)) {
					if(node.getNodeId().getNamespaceIndex() == 0) {
						return new OPCUABrowserEventTypeInternalModelNode();
					}  else {
						return new OPCUABrowserEventTypeModelNode();
					}
				} else {
					if(node.getNodeId().getNamespaceIndex() == 0) {
						return new OPCUABrowserObjectTypeInternalModelNode();
					}  else {
						return new OPCUABrowserObjectTypeModelNode();
					}
				}
			} else if (NodeClass.VariableType.equals(nodeClass)) {
				if(node.getNodeId().getNamespaceIndex() == 0) {
					return new OPCUABrowserVariableTypeInternalModelNode();
				} else {
					return new OPCUABrowserVariableTypeModelNode();
				}
			} else if (NodeClass.ReferenceType.equals(nodeClass)) {
				if(node.getNodeId().getNamespaceIndex() == 0) {
					return new OPCUABrowserReferenceTypeInternalModelNode();
				} else {
					return new OPCUABrowserReferenceTypeModelNode();
				}
			} else if (NodeClass.DataType.equals(nodeClass)) {
				if(node.getNodeId().getNamespaceIndex() == 0) {
					return new OPCUABrowserDataTypeInternalModelNode();
				} else {
					return new OPCUABrowserDataTypeModelNode();
				}
			} else if (NodeClass.View.equals(nodeClass)) {
				if(node.getNodeId().getNamespaceIndex() == 0) {
					return new OPCUABrowserViewInternalModelNode();
				} else {
					return new OPCUABrowserViewModelNode();
				}
			} else if (NodeClass.Method.equals(nodeClass)) {
				if(node.getNodeId().getNamespaceIndex() == 0) {
					return new OPCUABrowserMethodInternalModelNode();
				} else {
					return new OPCUABrowserMethodModelNode();
				}
			}
		} else if (Identifiers.FolderType.equals(typeDef)) {
			if(node.getNodeId().getNamespaceIndex() == 0) {
				return new OPCUABrowserFolderInternalModelNode();
			} else {
				return new OPCUABrowserFolderModelNode();
			}
		} else if (Identifiers.BaseObjectType.equals(typeDef)) {
			if(node.getNodeId().getNamespaceIndex() == 0) {
				return new OPCUABrowserInternalModelNode();
			} else {
				return new OPCUABrowserModelNode();
			}
		} else if (Identifiers.ObjectsFolder.equals(typeDef)) {
			if(node.getNodeId().getNamespaceIndex() == 0) {
				return new OPCUABrowserFolderInternalModelNode();
			} else {
				return new OPCUABrowserFolderModelNode();
			}
		} else if (Identifiers.BaseVariableType.equals(typeDef)) {
			if(node.getNodeId().getNamespaceIndex() == 0) {
				return new OPCUABrowserVariableInternalModelNode();
			} else {
				return new OPCUABrowserVariableModelNode();
			}
		} else if (NodeClass.Method.equals(nodeClass)) {
			//NodeId baseType = browseParentType(typeDef);
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
			return getBrowserModelForNode(node, baseType, nodeClass, references);
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

		BrowseDescription[] nodesToBrowse = { new BrowseDescription(superType,
				BrowseDirection.Inverse, Identifiers.HasSubtype, true,
				NodeClass.getMask(Arrays.asList(NodeClass.values())),
				BrowseResultMask.getMask(Arrays.asList(BrowseResultMask
						.values()))) };

		
		BrowseResult[] result = null;
		try {
			result = internalServer
					.getMaster().browse(nodesToBrowse, UnsignedInteger.ZERO, null,null);
		} catch (ServiceResultException e1) {
			e1.printStackTrace();
		}

		NodeId baseTypeDef = null;
		try {
			baseTypeDef = this.internalServer.getNamespaceUris().toNodeId(
					result[0].getReferences()[0].getNodeId());
		} catch (ServiceResultException e) {
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException npe) {
			return null;
		} catch( NullPointerException npe){
			return null;
		}

		return baseTypeDef;
	}

	public UnsignedInteger getNodeClassFilter() {
		return nodeClassFilter;
	}

	public void setNodeClassFilter(UnsignedInteger nodeClassFilter) {
		this.nodeClassFilter = nodeClassFilter;
	}

	public OPCUABrowserModelNode getBrowsermodelnode() {
		return browsermodelnode;
	}

	public void setBrowsermodelnode(OPCUABrowserModelNode browsermodelnode) {
		this.browsermodelnode = browsermodelnode;
	}
}
