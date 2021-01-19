package com.bichler.astudio.opcua.components.ui.modelbrowser;

import java.util.Arrays;

import opc.sdk.core.node.Node;
import opc.sdk.core.types.TypeTable;
import opc.sdk.server.core.OPCInternalServer;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
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
import org.opcfoundation.ua.core.ReferenceDescription;
import org.opcfoundation.ua.core.ReferenceNode;

import com.bichler.astudio.components.ui.ComponentsSharedImages;
import com.bichler.astudio.opcua.components.ui.modelbrowser.nodes.OPCUABrowserModelNode;


public class OPCUAModelLabelProvider extends LabelProvider {
	
	private OPCInternalServer internalServer = null;
	
	/**
	 * Returns the image for the item
	 */
	@Override
	public Image getImage(Object element) {
		
		NodeId typeDef = null;
		
		try {
			ExpandedNodeId expTypeDef = ((OPCUABrowserModelNode) element).getNode().findTarget(
					Identifiers.HasTypeDefinition, false);
			if(ExpandedNodeId.isNull(expTypeDef)){
				expTypeDef = ExpandedNodeId.NULL;
			}
			typeDef = this.internalServer.getNamespaceUris().toNodeId(expTypeDef);
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}

		NodeClass nodeClass = ((OPCUABrowserModelNode) element).getNode().getNodeClass();
		ReferenceNode[] references =  ((OPCUABrowserModelNode) element).getNode().getReferences();

		Image labelImage = getImageForLabel(typeDef, nodeClass, references);
		return labelImage;
	}

	/**
	 * Returns the label for the item
	 */
	@Override
	public String getText(Object element) {
		return ((OPCUABrowserModelNode) element).getNode().getDisplayName().getText();
	}

	/**
	 * Returns the image for the given item TODO: DIFFERENCE IN METHOD AND
	 * METHOD"TYPE" (IMAGE)
	 * 
	 * @param typeDef
	 * @param nodeClass
	 * @return image
	 */
	private Image getImageForLabel(NodeId typeDef, NodeClass nodeClass, ReferenceNode[] references) {
		Image labelImage = null;

		// type node
		if (NodeId.isNull(typeDef) || NodeClass.Method.equals(nodeClass)) {
			if (NodeClass.ObjectType.equals(nodeClass)) {
				return ComponentsSharedImages.getImage(ComponentsSharedImages.ICON_OBJECTTYPE);
			} else if (NodeClass.VariableType.equals(nodeClass)) {
				return ComponentsSharedImages.getImage(ComponentsSharedImages.ICON_VARIABLETYPE);
			} else if (NodeClass.ReferenceType.equals(nodeClass)) {
				return ComponentsSharedImages.getImage(ComponentsSharedImages.ICON_REFERENCETYPE);
			} else if (NodeClass.DataType.equals(nodeClass)) {
				return ComponentsSharedImages.getImage(ComponentsSharedImages.ICON_DATATYPE);
			} else if (NodeClass.View.equals(nodeClass)) {
				return ComponentsSharedImages.getImage(ComponentsSharedImages.ICON_VIEW);
			} else if (NodeClass.Method.equals(nodeClass)) {
				for(ReferenceNode ref : references) {
					if(ref.getReferenceTypeId().equals(Identifiers.HasModellingRule)) {
						if(ref.getTargetId().equals(Identifiers.ModellingRule_Mandatory)) {
							return ComponentsSharedImages.getImage(ComponentsSharedImages.ICON_METHOD_M);
						} else if(ref.getTargetId().equals(Identifiers.ModellingRule_Optional)) {
							return ComponentsSharedImages.getImage(ComponentsSharedImages.ICON_METHOD_O);
						}
					}
				}
				return ComponentsSharedImages.getImage(ComponentsSharedImages.ICON_METHOD);
			}
		} else if (Identifiers.FolderType.equals(typeDef)) {
			return ComponentsSharedImages.getImage(ComponentsSharedImages.ICON_FOLDER);
		} else if (Identifiers.BaseObjectType.equals(typeDef)) {
			for(ReferenceNode ref : references) {
				if(ref.getReferenceTypeId().equals(Identifiers.HasModellingRule)) {
					if(ref.getTargetId().equals(Identifiers.ModellingRule_Mandatory)) {
						return ComponentsSharedImages.getImage(ComponentsSharedImages.ICON_OBJECT_O);
					} else if(ref.getTargetId().equals(Identifiers.ModellingRule_Optional)) {
						return ComponentsSharedImages.getImage(ComponentsSharedImages.ICON_OBJECT_O);
					}
				}
			}
			return ComponentsSharedImages.getImage(ComponentsSharedImages.ICON_OBJECT);
		} else if (Identifiers.BaseVariableType.equals(typeDef)) {
			for(ReferenceNode ref : references) {
				if(ref.getReferenceTypeId().equals(Identifiers.HasModellingRule)) {
					if(ref.getTargetId().equals(Identifiers.ModellingRule_Mandatory)) {
						return ComponentsSharedImages.getImage(ComponentsSharedImages.ICON_VARIABLE_M);
					} else if(ref.getTargetId().equals(Identifiers.ModellingRule_Optional)) {
						return ComponentsSharedImages.getImage(ComponentsSharedImages.ICON_VARIABLE_O);
					}
				}
			}
			return ComponentsSharedImages.getImage(ComponentsSharedImages.ICON_VARIABLE);
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
			labelImage = getImageForLabel(baseType, nodeClass, references);
			if (labelImage != null) {
				return labelImage;
			}
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

	/**
	 * Fetches the type definition of the current inputed element
	 * 
	 * @param element
	 * @return
	 */
	@SuppressWarnings("unused")
	private NodeId fetchTypeDefinition(Object element) {
		// id of the item, which type def is needed
		NodeId nodeId = ((Node) element).getNodeId();
		
		TypeTable typeTree = internalServer
				.getTypeTable();

		BrowseDescription[] parentToBrowse = { new BrowseDescription(nodeId,
				BrowseDirection.Inverse, Identifiers.HierarchicalReferences,
				true, NodeClass.getMask(Arrays.asList(NodeClass.values())),
				BrowseResultMask.getMask(Arrays.asList(BrowseResultMask
						.values()))) };
		// get the parent
		BrowseResult[] parentResult = null;
		try {
			parentResult = internalServer
					.getMaster().browse(parentToBrowse, UnsignedInteger.ZERO, null,null);

		} catch (ServiceResultException e1) {
			e1.printStackTrace();
		}

		// check if its the parent?? and get its nodeid
		NodeId parent = null;
		if (parentResult.length == 1) {
			try {
				parent = this.internalServer.getNamespaceUris().toNodeId(
						parentResult[0].getReferences()[0].getNodeId());
			} catch (ServiceResultException e) {
				e.printStackTrace();
			}
		}

		// browse children to get the typedef of the element
		BrowseDescription[] nodesToBrowse = { new BrowseDescription(parent,
				BrowseDirection.Forward, Identifiers.HierarchicalReferences,
				true, NodeClass.getMask(Arrays.asList(NodeClass.values())),
				BrowseResultMask.getMask(Arrays.asList(BrowseResultMask
						.values()))) };

		BrowseResult[] results = null;
		try {
			results = internalServer
					.getMaster().browse(parentToBrowse, UnsignedInteger.ZERO, null,null);
		} catch (ServiceResultException e1) {
			e1.printStackTrace();
		}

		NodeId typeDef = null;
		for (ReferenceDescription result : results[0].getReferences()) {
			NodeId lookupId = null;
			try {
				lookupId = this.internalServer.getNamespaceUris().toNodeId(
						result.getNodeId());
			} catch (ServiceResultException e) {
				e.printStackTrace();
			}

			if (lookupId.equals(((Node) element).getNodeId())) {
				try {
					typeDef = this.internalServer.getNamespaceUris().toNodeId(
							result.getTypeDefinition());
					break;
				} catch (ServiceResultException e) {
					System.err.println("error");
					return null;
				}
			}
		}

		return typeDef;
	}
}
