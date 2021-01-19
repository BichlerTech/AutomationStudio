package com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceDescription;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.constants.ImageConstants;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.ReferenceTypeNode;

public class InverseModelLabelProvider extends LabelProvider {
	/**
	 * Returns the image for the item
	 */
	@Override
	public Image getImage(Object element) {
		NodeId typeDef = null;
		try {
			ExpandedNodeId expTypeDef = ((Node) element).findTarget(Identifiers.HasTypeDefinition, false);
			if (ExpandedNodeId.isNull(expTypeDef)) {
				expTypeDef = ExpandedNodeId.NULL;
			}
			typeDef = ServerInstance.getInstance().getServerInstance().getNamespaceUris().toNodeId(expTypeDef);
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
		NodeClass nodeClass = ((Node) element).getNodeClass();
		Image labelImage = getImageForLabel(typeDef, nodeClass);
		return labelImage;
	}

	/**
	 * Returns the label for the item
	 */
	@Override
	public String getText(Object element) {
		if (element instanceof ReferenceTypeNode) {
			if (((ReferenceTypeNode) element).getSymmetric()) {
				return ((Node) element).getDisplayName().getText();
			} else {
				return ((ReferenceTypeNode) element).getInverseName().getText();
			}
		}
		return ((Node) element).getDisplayName().getText();
	}

	/**
	 * Returns the image for the given item TODO: DIFFERENCE IN METHOD AND
	 * METHOD"TYPE" (IMAGE)
	 * 
	 * @param typeDef
	 * @param nodeClass
	 * @return image
	 */
	private Image getImageForLabel(NodeId typeDef, NodeClass nodeClass) {
		Image labelImage = null;
		// type node
		if (NodeId.isNull(typeDef) || NodeClass.Method.equals(nodeClass)) {
			if (NodeClass.ObjectType.equals(nodeClass)) {
				labelImage = resolveImage(ImageConstants.DESIGNIMGPATH, ImageConstants.OBJECTTYPE_IMG);
				return labelImage;
			} else if (NodeClass.VariableType.equals(nodeClass)) {
				labelImage = resolveImage(ImageConstants.DESIGNIMGPATH, ImageConstants.VARIABLETYPE_IMG);
				return labelImage;
			} else if (NodeClass.ReferenceType.equals(nodeClass)) {
				labelImage = resolveImage(ImageConstants.DESIGNIMGPATH, ImageConstants.REFERENCETYPE_IMG);
				return labelImage;
			} else if (NodeClass.DataType.equals(nodeClass)) {
				labelImage = resolveImage(ImageConstants.DESIGNIMGPATH, ImageConstants.DATATYPE_IMG);
				return labelImage;
			} else if (NodeClass.View.equals(nodeClass)) {
				labelImage = resolveImage(ImageConstants.DESIGNIMGPATH, ImageConstants.VIEW_IMG);
				return labelImage;
			} else if (NodeClass.Method.equals(nodeClass)) {
				labelImage = resolveImage(ImageConstants.DESIGNIMGPATH, ImageConstants.METHOD_IMG);
				return labelImage;
			}
		} else if (Identifiers.FolderType.equals(typeDef)) {
			labelImage = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
			return labelImage;
		} else if (Identifiers.BaseObjectType.equals(typeDef)) {
			labelImage = resolveImage(ImageConstants.DESIGNIMGPATH, ImageConstants.OBJECT_IMG);
			return labelImage;
		} else if (Identifiers.BaseDataVariableType.equals(typeDef)) {
			labelImage = resolveImage(ImageConstants.DESIGNIMGPATH, ImageConstants.VARIABLE_IMG);
			return labelImage;
		} else if (Identifiers.PropertyType.equals(typeDef)) {
			labelImage = resolveImage(ImageConstants.DESIGNIMGPATH, ImageConstants.PROPERTY_IMG);
			return labelImage;
		}
		
		else if (NodeClass.Method.equals(nodeClass)) {
			// NodeId baseType = browseParentType(typeDef);
		}
		// lookup for some type
		else {
			NodeId baseType = browseParentType(typeDef);
			labelImage = getImageForLabel(baseType, nodeClass);
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

	/**
	 * Resolves an image with the given path and the image name to the plugin
	 * 
	 * @param path
	 * @param imgName
	 * @return
	 */
	private Image resolveImage(IPath path, String imgName) {
		path = path.append(imgName);
		URL imgURL = FileLocator.find(Activator.getDefault().getBundle(), path, null);
		URL fileURL = null;
		try {
			fileURL = FileLocator.toFileURL(imgURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageDescriptor descriptor = ImageDescriptor.createFromURL(fileURL);
		Image image = descriptor.createImage();
		return image;
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
		// get the parent
		BrowseResult[] parentResult = null;
		try {
			parentResult = ServerInstance.browse(nodeId, Identifiers.HierarchicalReferences, NodeClass.ALL,
					BrowseResultMask.ALL, BrowseDirection.Inverse, true);
		} catch (ServiceResultException e1) {
			e1.printStackTrace();
		}
		// check if its the parent?? and get its nodeid
		NodeId parent = null;
		if (parentResult.length == 1) {
			try {
				parent = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
						.toNodeId(parentResult[0].getReferences()[0].getNodeId());
			} catch (ServiceResultException e) {
				e.printStackTrace();
			}
		}
		// browse children to get the typedef of the element
		BrowseResult[] results = null;
		try {
			results = ServerInstance.browse(parent, Identifiers.HierarchicalReferences, NodeClass.ALL,
					BrowseResultMask.ALL, BrowseDirection.Forward, true);
		} catch (ServiceResultException e1) {
			e1.printStackTrace();
		}
		NodeId typeDef = null;
		for (ReferenceDescription result : results[0].getReferences()) {
			NodeId lookupId = null;
			try {
				lookupId = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
						.toNodeId(result.getNodeId());
			} catch (ServiceResultException e) {
				e.printStackTrace();
			}
			if (lookupId.equals(((Node) element).getNodeId())) {
				try {
					typeDef = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
							.toNodeId(result.getTypeDefinition());
					break;
				} catch (ServiceResultException e) {
					e.printStackTrace();
					return null;
				}
			}
		}
		return typeDef;
	}
}
