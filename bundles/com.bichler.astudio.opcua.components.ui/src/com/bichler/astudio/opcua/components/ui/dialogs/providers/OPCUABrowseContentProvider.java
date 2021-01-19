package com.bichler.astudio.opcua.components.ui.dialogs.providers;

import java.util.ArrayList;
import java.util.List;

import opc.sdk.core.node.Node;
import opc.sdk.server.core.OPCInternalServer;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.StatusCodes;

import com.bichler.astudio.opcua.components.ui.dialogs.OPCTreeViewerItem;

public class OPCUABrowseContentProvider implements ITreeContentProvider {

	private OPCInternalServer server = null;
	
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
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {

		// ServerModelNode server =
		// ResourceManager.getServerModelNode(this.session);

		List<OPCTreeViewerItem> children = ((OPCTreeViewerItem) parentElement)
				.getChildren();

		if (children != null && children.size() > 0) {
			return children.toArray();
		}

		return buildBrowse((OPCTreeViewerItem) parentElement);

	}
	
	protected Object[] buildBrowse(OPCTreeViewerItem parent) {
		NodeId nodeId = ((OPCTreeViewerItem) parent).getNodeId();
		List<OPCTreeViewerItem> children = null;
		try {
			
			Node[] ch = server.getAddressSpaceManager().findChildren(nodeId);

			children = getBrowsedItems(ch);

			if (children.isEmpty()) {
				throw new ServiceResultException(StatusCodes.Bad_NothingToDo);
			}

			((OPCTreeViewerItem) parent).setChildren(children);


		} catch (ServiceResultException e) {
		
		} catch(Exception e) {
			e.printStackTrace();
		}

		if (children == null || children.isEmpty()) {
			return new Object[0];
		}

		return children.toArray();
	}
	
	/**
	 * Fetches the browsed tree view items
	 * 
	 * @param session
	 * 
	 * @param browse
	 * @return a list of OPCTreeViewerItems
	 */
	private List<OPCTreeViewerItem> getBrowsedItems(Node[] children) {

		// check if something has to be done
		if (children == null || children.length == 0) {
			return new ArrayList<OPCTreeViewerItem>();
		}

		List<OPCTreeViewerItem> browsedItems = new ArrayList<OPCTreeViewerItem>();

		// fetches the reference descriptions
		for (Node child : children) {
			// creates the new items

			if (child == null) {
				continue;
			}

			OPCTreeViewerItem newItem = null;
			String displayname = null;

			
				if (child.getDisplayName() != null
						&& child.getDisplayName().getText() != null) {
					displayname = child.getDisplayName()
							.getText();
				}
				newItem = new OPCTreeViewerItem(displayname);
	
			newItem.setServer(server);
			newItem.setBrowseName(child.getBrowseName());
			
			newItem.setNodeId(child.getNodeId());
			
			newItem.setNodeClass(child.getNodeClass());
		
			//newItem.setServerName(this.serverName);
			// set the basic attributes of the item
			// [NodeId, TypeDefId, NodeClass, BrowseName, ReferenceTypeId,
			// DisplayName]

			//NodeId id = NodeId.NULL;
			//try {

				
				// set the type definition of the item
				
				//child.getReferences()
				
				//NodeId typeDefId = session.getNamespaceUris().toNodeId(
				//		referenceDescription.getTypeDefinition());
				//newItem.setTypeDefinition(typeDefId);
			//} catch (ServiceResultException e) {
			//	continue;
			//}

			//newItem.setNodeClass(referenceDescription.getNodeClass());

			//newItem.setBrowseName(referenceDescription.getBrowseName());
			//newItem.setReferenceTypeId(referenceDescription
				//	.getReferenceTypeId());

			// fetch node
			fetchNode(newItem);

			// add to list
			browsedItems.add(newItem);
		}

		return browsedItems;
	}
	
	/**
	 * Fetches the related node from the tree view item and "refresh" its values
	 * by resetting the node (Browse)
	 * 
	 * @param tree
	 *            view item
	 */
	public void fetchNode(OPCTreeViewerItem newItem) {
		// checks for the required values
		if (newItem.getNodeId() == null) {
			// Status status = new Status(Status.ERROR, "BrowseContentProvider"
			// + serverID,
			// "Error fetching the treeviewer item content for the related node!");
			//
			// Activator.getDefault().getLog().log(status);
			throw new IllegalArgumentException();
		} else if (newItem.getBrowseName() == null) {
			// Status status = new Status(Status.ERROR, "BrowseContentProvider"
			// + serverID,
			// "Error fetching the treeviewer item content for the related node!");
			//
			// Activator.getDefault().getLog().log(status);
			throw new IllegalArgumentException();
		} else if (newItem.getNodeClass() == null) {
			// Status status = new Status(Status.ERROR, "BrowseContentProvider"
			// + serverID, CustomString.getString("ERROR.NODE.ATTRIBUTES"));
			//
			// Activator.getDefault().getLog().log(status);
			throw new IllegalArgumentException();
		} else if (newItem.getReferenceTypeId() == null) {
			// Status status = new Status(Status.ERROR, "BrowseContentProvider"
			// + serverID, CustomString.getString("ERROR.NODE.ATTRIBUTES"));
			//
			// Activator.getDefault().getLog().log(status);
			//throw new IllegalArgumentException();
		} else if (newItem.getTypeDefinition() == null) {
			// Status status = new Status(Status.ERROR, "BrowseContentProvider"
			// + serverID, CustomString.getString("ERROR.NODE.ATTRIBUTES"));
			//
			// Activator.getDefault().getLog().log(status);
			//throw new IllegalArgumentException();
		}

		Node node = getBrowsedNode(newItem.getNodeId(), newItem);
		newItem.setNode(node);
		//if (node instanceof VariableNode) {
		//	newItem.setHistorizing(((VariableNode) node).getHistorizing());
		//}

	}
	
	/**
	 * Browses a Node with the NodeId and its related tree view item
	 * 
	 * @param NodeId
	 *            from the node to read
	 * @param selected
	 *            tree view item
	 * @return UA Node
	 */
	private Node getBrowsedNode(NodeId nodeToRead,
			OPCTreeViewerItem item4nodeToRead) {
		Node node = null;


		// send the service
		try {
			
			node =server.getAddressSpaceManager().getNodeById(nodeToRead);
			
			/**node = OPCUAClient.getInstance()
					.getManager()
					.readNodes(item4nodeToRead.getServer().getSession(),
							new NodeId[] { nodeToRead },
							new NodeClass[] { item4nodeToRead.getNodeClass() },
							true, true)[0];
							*/
			// creates the attributes for the current node

			// ExtensionObject nodesAttribute = null;
			// try {
			// nodesAttribute = createAttributes(newItem, result,
			// attributesToRead);
			// } catch (ArrayIndexOutOfBoundsException aioobe) {
			// // aioobe.printStackTrace();
			// }
			//boolean browseParent = false;

			//if (item4nodeToRead instanceof MethodOPCTreeViewerItem) {
			//	browseParent = true;
			//}
/*
			ExpandedNodeId parentNodeId = null;
			if (browseParent) {
				ReferenceDescription[] parent = OPCUAClient.getInstance()
						.browse(item4nodeToRead.getServer().getSession(), nodeToRead,
								BrowseDirection.Inverse, true,
								NodeClass.getMask(NodeClass.ALL),
								Identifiers.HasComponent,
								BrowseResultMask.getMask(BrowseResultMask.ALL),
								UnsignedInteger.ZERO, null).getReferences();

				if (parent.length == 1) {
					parentNodeId = parent[0].getNodeId();
				}
				item4nodeToRead.setParentId(parentNodeId);
			}*/

			// build the node
			// node = this.factory.createNode(
			// newItem.getBrowseName(),
			// nodesAttribute,
			// newItem.getNodeClass(),
			// parentNodeId,
			// newItem.getReferenceTypeId(),
			// NamespaceTable.getDefault().toExpandedNodeId(
			// newItem.getNodeId()),
			// NamespaceTable.getDefault().toExpandedNodeId(
			// newItem.getTypeDefinition()));

			// if (node instanceof VariableNode) {
			// newItem.setValueRank(((VariableNode) node).getValueRank());
			// }
		//} catch (ServiceResultException e) {
			// Status status = new Status(Status.ERROR, "BrowseContentProvider "
			// + serverID,
			// CustomString.getString("ERROR.BROWSE.CHILDREN"), e);
			//
			// Activator.getDefault().getLog().log(status);
		} catch (NullPointerException npe) {
			// npe.printStackTrace();
			// System.out.println("NULLPOINTER");

		}

		return node;
	}
	
	public void fetchOPCTreeViewerItem(
			OPCTreeViewerItem item) {

		Node node = getBrowsedNode(item.getNodeId(), item);

		item.setNode(node);

		if (node != null) {
			item.setBrowseName(node.getBrowseName());
			if (node.getDisplayName() != null
					&& node.getDisplayName().getText() != null) {
				item.setDisplayName(node.getDisplayName().getText());
			} else if (node.getBrowseName() != null
					&& node.getBrowseName().getName() != null) {
				item.setDisplayName(node.getBrowseName().getName());
			}

			item.setNodeClass(node.getNodeClass());
			// item.setServerName(session.getSessionName());
		}

		// item.setDisplayName(displayname);
		// item.setSession(session);
		// item.setServerID(serverID);
		// item.setServerName(serverName);
		//
		// item.setNodeId(nodeId);
		// item.setTypeDefinition(typeDefinition);
		// item.setNodeClass(nodeClass);
		// item.setBrowseName(browseName);

	}

	@Override
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		// TODO Auto-generated method stub
		return getChildren(element).length > 0;
	}

	public OPCInternalServer getServer() {
		return server;
	}

	public void setServer(OPCInternalServer server) {
		this.server = server;
	}

}
