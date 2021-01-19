package com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser;

import java.util.Arrays;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
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

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.constants.ImageConstants;
import com.bichler.astudio.opcua.opcmodeler.utils.extern.DesignerUtils;

import opc.sdk.core.node.Node;

public class CreateModellingLabelProvider extends LabelProvider {
	private Image imgChecked = null;
	private Image imgUnChecked = null;
	private Image img1Checked = null;

	// private CheckboxTreeViewer parentViewer = null;
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
		ReferenceNode[] references = ((Node) element).getReferences();
		Image labelImage = getImageForLabel(typeDef, nodeClass, references);
		return labelImage;
	}

	/**
	 * Returns the label for the item
	 */
	@Override
	public String getText(Object element) {
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
	private Image getImageForLabel(NodeId typeDef, NodeClass nodeClass, ReferenceNode[] references) {
		// type node
		if (NodeId.isNull(typeDef) || NodeClass.Method.equals(nodeClass)) {
			if (NodeClass.ObjectType.equals(nodeClass)) {
				return DesignerUtils.resolveImage(ImageConstants.DESIGNIMGPATH, ImageConstants.OBJECTTYPE_IMG);
			} else if (NodeClass.VariableType.equals(nodeClass)) {
				return DesignerUtils.resolveImage(ImageConstants.DESIGNIMGPATH, ImageConstants.VARIABLETYPE_IMG);
			} else if (NodeClass.ReferenceType.equals(nodeClass)) {
				return DesignerUtils.resolveImage(ImageConstants.DESIGNIMGPATH, ImageConstants.REFERENCETYPE_IMG);
			} else if (NodeClass.DataType.equals(nodeClass)) {
				return DesignerUtils.resolveImage(ImageConstants.DESIGNIMGPATH, ImageConstants.DATATYPE_IMG);
			} else if (NodeClass.View.equals(nodeClass)) {
				return DesignerUtils.resolveImage(ImageConstants.DESIGNIMGPATH, ImageConstants.VIEW_IMG);
			} else if (NodeClass.Method.equals(nodeClass)) {
				for (ReferenceNode ref : references) {
					if (ref.getReferenceTypeId().equals(Identifiers.HasModellingRule)) {
						if (ref.getTargetId().equals(Identifiers.ModellingRule_Mandatory)) {
							return this.img1Checked;
						} else if (ref.getTargetId().equals(Identifiers.ModellingRule_Optional)) {
							return this.imgChecked;
						}
					}
				}
				return this.img1Checked;
			}
		}
		else if (Identifiers.BaseObjectType.equals(typeDef)) {
			for (ReferenceNode ref : references) {
				if (ref.getReferenceTypeId().equals(Identifiers.HasModellingRule)) {
					if (ref.getTargetId().equals(Identifiers.ModellingRule_Mandatory)) {
						return this.img1Checked;
					} else if (ref.getTargetId().equals(Identifiers.ModellingRule_Optional)) {
						return this.imgChecked;
					}
				}
			}
			return this.img1Checked;
		} else if (Identifiers.BaseVariableType.equals(typeDef)) {
			for (ReferenceNode ref : references) {
				if (ref.getReferenceTypeId().equals(Identifiers.HasModellingRule)) {
					if (ref.getTargetId().equals(Identifiers.ModellingRule_Mandatory)) {
						return this.img1Checked;
					} else if (ref.getTargetId().equals(Identifiers.ModellingRule_Optional)) {
						return this.imgChecked;
					}
				}
			}
			return this.img1Checked;
		} else if (NodeClass.Method.equals(nodeClass)) {
			// NodeId baseType = browseParentType(typeDef);
		}
		else {
			NodeId baseType = browseParentType(typeDef);
			return getImageForLabel(baseType, nodeClass, references);
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
		// AddressSpace addressSpace = ServerInstance.getInstance()
		// .getServerInstance().getAddressSpace();
		// MasterNodeManager manager = ServerInstance.getInstance()
		// .getServerInstance().getMasterNodeManager();
		// TypeTable typeTree = ServerInstance.getInstance().getServerInstance()
		// .getTypeTree();
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
		if (parentResult.length > 0) {
			try {
				parent = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
						.toNodeId(parentResult[0].getReferences()[0].getNodeId());
			} catch (ServiceResultException e) {
				e.printStackTrace();
			}
		}
		// browse children to get the typedef of the element
		// BrowseDescription[] nodesToBrowse = { new BrowseDescription(parent,
		// BrowseDirection.Forward, Identifiers.HierarchicalReferences,
		// true, NodeClass.getMask(Arrays.asList(NodeClass.values())),
		// BrowseResultMask.getMask(Arrays.asList(BrowseResultMask
		// .values()))) };
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

	public Image getImgChecked() {
		return imgChecked;
	}

	public void setImgChecked(Image imgChecked) {
		this.imgChecked = imgChecked;
	}

	public Image getImgUnChecked() {
		return imgUnChecked;
	}

	public void setImgUnChecked(Image imgUnChecked) {
		this.imgUnChecked = imgUnChecked;
	}

	public Image getImg1Checked() {
		return img1Checked;
	}

	public void setImg1Checked(Image img1Checked) {
		this.img1Checked = img1Checked;
	}
}
