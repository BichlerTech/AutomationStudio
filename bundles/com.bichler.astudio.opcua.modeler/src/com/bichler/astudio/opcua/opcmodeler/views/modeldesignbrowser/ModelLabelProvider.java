package com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceDescription;
import org.opcfoundation.ua.core.ReferenceNode;

import com.bichler.astudio.images.opcua.OPCImagesActivator;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;

import opc.sdk.core.node.Node;

public class ModelLabelProvider extends LabelProvider {
	/**
	 * Returns the image for the item
	 */
	@Override
	public Image getImage(Object element) {
		NodeId typeDef = null;
		try {
			ReferenceNode[] references = ((BrowserModelNode) element).getNode().getReferences();
			for (ReferenceNode reference : references) {
				reference.getReferenceTypeId();
			}

			if (((BrowserModelNode) element).getNode().getBrowseName().getNamespaceIndex() > 0) {
//				System.out.print("");
			}

			ExpandedNodeId expTypeDef = ((BrowserModelNode) element).getNode().findTarget(Identifiers.HasTypeDefinition,
					false);
			if (ExpandedNodeId.isNull(expTypeDef)) {
				expTypeDef = ExpandedNodeId.NULL;
			}
			typeDef = ServerInstance.getInstance().getServerInstance().getNamespaceUris().toNodeId(expTypeDef);
		} catch (ServiceResultException e) {
			e.printStackTrace();
		} catch (NullPointerException npe) {
			npe.printStackTrace();
		}

		NodeClass nodeClass = ((BrowserModelNode) element).getNode().getNodeClass();
		ReferenceNode[] references = ((BrowserModelNode) element).getNode().getReferences();
		Image labelImage = getImageForLabel(typeDef, nodeClass, references);
		return labelImage;
	}

	/**
	 * Returns the label for the item
	 */
	@Override
	public String getText(Object element) {
		return ((BrowserModelNode) element).getNode().getDisplayName().getText();
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
				return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
						OPCImagesActivator.OBJECTTYPE);
			} else if (NodeClass.VariableType.equals(nodeClass)) {
				return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
						OPCImagesActivator.VARIABLETYPE);
			} else if (NodeClass.ReferenceType.equals(nodeClass)) {
				return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
						OPCImagesActivator.REFERENCETYPE);
			} else if (NodeClass.DataType.equals(nodeClass)) {
				return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
						OPCImagesActivator.DATATYPE);
			} else if (NodeClass.View.equals(nodeClass)) {
				return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
						OPCImagesActivator.VIEW);
			} else if (NodeClass.Method.equals(nodeClass)) {
				for (ReferenceNode ref : references) {
					if (ref.getReferenceTypeId().equals(Identifiers.HasModellingRule)) {
						if (ref.getTargetId().equals(Identifiers.ModellingRule_Mandatory)) {
							return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
									OPCImagesActivator.METHOD_M);
						} else if (ref.getTargetId().equals(Identifiers.ModellingRule_Optional)) {
							return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
									OPCImagesActivator.METHOD_O);
						}
					}
				}
				return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
						OPCImagesActivator.METHOD);
			}
		} else if (Identifiers.FolderType.equals(typeDef)) {
			for (ReferenceNode reference : references) {
				try {
					if (reference.getReferenceTypeId().equals(Identifiers.HasModellingRule)) {
						if (reference.getTargetId().equals(Identifiers.ModellingRule_Mandatory)) {
							return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
									OPCImagesActivator.FOLDER_M);
						} else if (reference.getTargetId().equals(Identifiers.ModellingRule_Optional)) {
							return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
									OPCImagesActivator.FOLDER_O);
						}
					}
				} catch (NullPointerException ex) {
					ex.printStackTrace();
				}
			}
			return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
					OPCImagesActivator.FOLDER);
		} else if (Identifiers.BaseObjectType.equals(typeDef)) {
			for (ReferenceNode ref : references) {
				if (ref.getReferenceTypeId().equals(Identifiers.HasModellingRule)) {
					if (ref.getTargetId().equals(Identifiers.ModellingRule_Mandatory)) {
						return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
								OPCImagesActivator.OBJECT_M);
					} else if (ref.getTargetId().equals(Identifiers.ModellingRule_Optional)) {
						return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
								OPCImagesActivator.OBJECT_O);
					}
				}
			}
			return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
					OPCImagesActivator.OBJECT);
		} else if (Identifiers.BaseDataVariableType.equals(typeDef)) {
			for (ReferenceNode ref : references) {
				if (ref.getReferenceTypeId().equals(Identifiers.HasModellingRule)) {
					try {
						if (ServerInstance.getInstance().getServerInstance().getNamespaceUris()
								.toNodeId(ref.getTargetId()).equals(Identifiers.ModellingRule_Mandatory)) {
							return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
									OPCImagesActivator.VARIABLE_M);
						} else if (ServerInstance.getInstance().getServerInstance().getNamespaceUris()
								.toNodeId(ref.getTargetId()).equals(Identifiers.ModellingRule_Optional)) {
							return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
									OPCImagesActivator.VARIABLE_O);
						}
					} catch (ServiceResultException sre) {
						return null;
					}
				}
			}
			return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
					OPCImagesActivator.VARIABLE);
		} else if (Identifiers.PropertyType.equals(typeDef)) {
			for (ReferenceNode ref : references) {
				if (ref.getReferenceTypeId().equals(Identifiers.HasModellingRule)) {
					try {
						if (ServerInstance.getInstance().getServerInstance().getNamespaceUris()
								.toNodeId(ref.getTargetId()).equals(Identifiers.ModellingRule_Mandatory)) {
							return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
									OPCImagesActivator.PROPERTY_M);
						} else if (ServerInstance.getInstance().getServerInstance().getNamespaceUris()
								.toNodeId(ref.getTargetId()).equals(Identifiers.ModellingRule_Optional)) {
							return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
									OPCImagesActivator.PROPERTY_O);
						}
					} catch (ServiceResultException sre) {
						return null;
					}
				}
			}
			return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
					OPCImagesActivator.PROPERTY);
		}

		else if (NodeClass.Method.equals(nodeClass)) {
			// NodeId baseType = browseParentType(typeDef);
		}
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
		BrowseDescription[] parentToBrowse = { new BrowseDescription(nodeId, BrowseDirection.Inverse,
				Identifiers.HierarchicalReferences, true, NodeClass.getMask(Arrays.asList(NodeClass.values())),
				BrowseResultMask.getMask(Arrays.asList(BrowseResultMask.values()))) };
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
		BrowseDescription[] nodesToBrowse = { new BrowseDescription(parent, BrowseDirection.Forward,
				Identifiers.HierarchicalReferences, true, NodeClass.getMask(Arrays.asList(NodeClass.values())),
				BrowseResultMask.getMask(Arrays.asList(BrowseResultMask.values()))) };
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
