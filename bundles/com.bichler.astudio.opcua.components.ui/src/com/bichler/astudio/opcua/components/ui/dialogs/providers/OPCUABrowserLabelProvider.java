package com.bichler.astudio.opcua.components.ui.dialogs.providers;

import java.util.Arrays;

import opc.sdk.server.core.OPCInternalServer;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;

import com.bichler.astudio.components.ui.ComponentsSharedImages;
import com.bichler.astudio.opcua.components.ui.dialogs.OPCTreeViewerItem;

public class OPCUABrowserLabelProvider extends LabelProvider {

	private OPCInternalServer server;
	

	public OPCInternalServer getServer() {
		return server;
	}

	public OPCUABrowserLabelProvider(TreeViewer treeViewer) {
		
	}

	public void setServer(OPCInternalServer server) {
		this.server = server;
	}

	/**
	 * Returns the image for the item
	 */
	@Override
	public Image getImage(Object element) {
		// get the type id of the item for the icon
		NodeId typeDef = ((OPCTreeViewerItem) element).getTypeDefinition();
		NodeClass nodeClass = ((OPCTreeViewerItem) element).getNodeClass();
		// get the image
		final Image labelImage = getImageForLabel(typeDef, nodeClass);

		return labelImage;
	}

	/**
	 * Returns the label for the item
	 */
	@Override
	public String getText(Object element) {
		String s = "noname";
		if (element instanceof OPCTreeViewerItem) {
			if (((OPCTreeViewerItem) element).getDisplayName() != null
					&& ((OPCTreeViewerItem) element).getDisplayName() != null
					&& !((OPCTreeViewerItem) element).getDisplayName().isEmpty()) {
				s = ((OPCTreeViewerItem) element).getDisplayName();
			} else if (((OPCTreeViewerItem) element).getBrowseName() != null
					&& ((OPCTreeViewerItem) element).getBrowseName().getName() != null
					&& !((OPCTreeViewerItem) element).getBrowseName().getName().isEmpty()) {
				s = ((OPCTreeViewerItem) element).getBrowseName().getName();
			}
		}

		if (s != null && !s.isEmpty()) {
			if (s.length() > 150) {
				s = s.substring(0, 149);
			}
		}

		return s;
	}

	/**
	 * Browses the parents type
	 * 
	 * @param superType
	 * @return
	 */
	private NodeId browseParentType(NodeId superType) {

		NodeId baseTypeDef = null;

		try {
			BrowseDescription[] nodesToBrowse = { new BrowseDescription(superType, BrowseDirection.Inverse,
					Identifiers.HierarchicalReferences, true, NodeClass.getMask(NodeClass.ALL),
					BrowseResultMask.getMask(Arrays.asList(BrowseResultMask.values()))) };

			BrowseResult references = server.getMaster().browse(nodesToBrowse, UnsignedInteger.ZERO, null, null)[0];

			if (references != null && references.getReferences() != null && references.getReferences().length > 0) {

				if (references.getReferences()[0] != null) {
					baseTypeDef = server.getNamespaceUris().toNodeId(references.getReferences()[0].getNodeId());
				}
			}
		} catch (ServiceResultException e) {

		}

		return baseTypeDef;
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
				return ComponentsSharedImages.getImage(ComponentsSharedImages.ICON_METHOD);
			} else if (NodeClass.Object.equals(nodeClass)) {
				return ComponentsSharedImages.getImage(ComponentsSharedImages.ICON_OBJECT);
			}
		} else if (Identifiers.FolderType.equals(typeDef)) {
			return ComponentsSharedImages.getImage(ComponentsSharedImages.ICON_FOLDER);
		} else if (Identifiers.BaseObjectType.equals(typeDef)) {
			return ComponentsSharedImages.getImage(ComponentsSharedImages.ICON_OBJECT);
		} else if (Identifiers.BaseVariableType.equals(typeDef)) {
			return ComponentsSharedImages.getImage(ComponentsSharedImages.ICON_VARIABLE);
		}

		// used because of error in other information models

		else if (Identifiers.BaseDataType.equals(typeDef)) {
			return ComponentsSharedImages.getImage(ComponentsSharedImages.ICON_DATATYPE);
		} else if (Identifiers.BaseEventType.equals(typeDef)) {
			return ComponentsSharedImages.getImage(ComponentsSharedImages.ICON_EVENT);
		} else if (Identifiers.ConditionType.equals(typeDef)) {
			return ComponentsSharedImages.getImage(ComponentsSharedImages.ICON_ALARM);
		}
		// else if (NodeClass.Method.equals(nodeClass)) {
		// NodeId baseType = browseParentType(typeDef);
		// }
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
			if (NodeId.isNull(typeDef)) {
				return null;
			}

			NodeId baseType = browseParentType(typeDef);
			if (NodeId.isNull(baseType)) {
				return ComponentsSharedImages.getImage(ComponentsSharedImages.ICON_EVENT);
			}

			if (baseType.equals(typeDef)) {
				return ComponentsSharedImages.getImage(ComponentsSharedImages.ICON_EVENT);
			}

			labelImage = getImageForLabel(baseType, nodeClass);
			if (labelImage != null) {
				return labelImage;
			}
		}
		return ComponentsSharedImages.getImage(ComponentsSharedImages.ICON_EVENT);
	}
}
