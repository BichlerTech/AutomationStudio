package com.bichler.astudio.opcua.components.ui.dialogs.providers;

import java.util.ArrayList;
import java.util.List;

import opc.client.service.ClientSession;
import opc.sdk.core.node.Node;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceDescription;
import org.opcfoundation.ua.core.StatusCodes;

import com.bichler.astudio.opcua.components.ui.dialogs.OPCRemoteTreeViewerItem;
import com.bichler.astudio.opcua.components.ui.serverbrowser.OPCUAServerModelNode;
import com.bichler.astudio.opcua.components.ui.serverbrowser.providers.UAServerModelNode;


public class OPCUARemoteBrowseContentProvider implements ITreeContentProvider {
	// factory (initialized)
	// private NodeFactory factory = null;
	// private ClientSession session = null;
	private String serverName = "";

	// private int serverId = -1;

	private UAServerModelNode server = null;

	// private Shell shell = null;

	public OPCUARemoteBrowseContentProvider(/** Integer serverId, */
	String serverName, Composite parent) {
		// this.serverId = serverId;
		this.serverName = serverName;
		// this.factory = new NodeFactory();
		// this.shell = parent.getShell();
	}

	public OPCUARemoteBrowseContentProvider(/* Integer serverId, */String serverName, Shell shell) {
		// this.serverId = serverId;
		this.serverName = serverName;
	}

	@Override
	public Object[] getChildren(Object parentElement) {

		// ServerModelNode server =
		// ResourceManager.getServerModelNode(this.session);

		List<OPCRemoteTreeViewerItem> children = ((OPCRemoteTreeViewerItem) parentElement).getChildren();

		if (children != null && children.size() > 0) {
			return children.toArray();
		}

		return buildBrowse((OPCRemoteTreeViewerItem) parentElement);

	}

	protected Object[] buildBrowse(OPCRemoteTreeViewerItem parent) {
		NodeId nodeId = ((OPCRemoteTreeViewerItem) parent).getNodeId();
		List<OPCRemoteTreeViewerItem> children = null;
		try {
			if (server == null) {
				return new Object[0];
			}
			if (server.isConnected() && server.getDevice().getUaclient() != null) {
				BrowseResult browse = server.getDevice().getUaclient().browse(
						server.getDevice().getUaclient().getActiveSession(), nodeId, BrowseDirection.Forward, true,
						NodeClass.getMask(NodeClass.ALL), Identifiers.HierarchicalReferences,
						BrowseResultMask.getMask(BrowseResultMask.ALL), UnsignedInteger.ZERO, null, false);

				children = getBrowsedItems(server.getDevice().getUaclient().getActiveSession(), browse);

				if (children.isEmpty()) {
					throw new ServiceResultException(StatusCodes.Bad_NothingToDo);
				}

				((OPCRemoteTreeViewerItem) parent).setChildren(children);

				if (browse.getStatusCode() != null && browse.getStatusCode().isBad()) {
					throw new ServiceResultException(browse.getStatusCode());
				}
			}

		} catch (ServiceResultException e) {
			e.printStackTrace();
		}

		if (children == null || children.isEmpty()) {
			return new Object[0];
		}

		return children.toArray();
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {

		boolean hasChildren = ((OPCRemoteTreeViewerItem) element).hasChildren();
		return hasChildren;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	/**
	 * Fetches the browsed tree view items
	 * 
	 * @param session
	 * 
	 * @param browse
	 * @return a list of OPCTreeViewerItems
	 */
	private List<OPCRemoteTreeViewerItem> getBrowsedItems(ClientSession session, BrowseResult result) {

		// check if something has to be done
		if (result == null || result.getReferences() == null || result.getReferences().length == 0) {
			return new ArrayList<OPCRemoteTreeViewerItem>();
		}

		ReferenceDescription[] browse = result.getReferences();

		List<OPCRemoteTreeViewerItem> browsedItems = new ArrayList<OPCRemoteTreeViewerItem>();

		// fetches the reference descriptions
		for (ReferenceDescription referenceDescription : browse) {
			// creates the new items

			if (referenceDescription == null || referenceDescription.getNodeClass() == null) {
				continue;
			}

			OPCRemoteTreeViewerItem newItem = null;
			String displayname = null;

			if (NodeClass.Method.equals(referenceDescription.getNodeClass())) {
				continue;
				/**
				 * if (referenceDescription.getDisplayName() != null &&
				 * referenceDescription.getDisplayName().getText() != null) {
				 * displayname = referenceDescription.getDisplayName()
				 * .getText(); } newItem = new
				 * MethodOPCTreeViewerItem(displayname);
				 */
			} else {
				if (referenceDescription.getDisplayName() != null
						&& referenceDescription.getDisplayName().getText() != null) {
					displayname = referenceDescription.getDisplayName().getText();
				}
				newItem = new OPCRemoteTreeViewerItem(displayname);
			}
			newItem.setServer(server);
			/// newItem.getServer().getDevice().getUaclient().setActiveSession(session);
			newItem.setServerName(this.serverName);
			// set the basic attributes of the item
			// [NodeId, TypeDefId, NodeClass, BrowseName, ReferenceTypeId,
			// DisplayName]

			NodeId id = NodeId.NULL;
			try {
				id = session.getNamespaceUris().toNodeId(referenceDescription.getNodeId());

				newItem.setNodeId(id);
				// set the type definition of the item
				NodeId typeDefId = session.getNamespaceUris().toNodeId(referenceDescription.getTypeDefinition());
				newItem.setTypeDefinition(typeDefId);
			} catch (ServiceResultException e) {
				continue;
			}

			newItem.setNodeClass(referenceDescription.getNodeClass());

			newItem.setBrowseName(referenceDescription.getBrowseName());
			newItem.setReferenceTypeId(referenceDescription.getReferenceTypeId());

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
	public void fetchNode(OPCRemoteTreeViewerItem newItem) {
		// checks for the required values
		if (newItem.getNodeId() == null) {
			// Status status = new Status(Status.ERROR, "BrowseContentProvider"
			// + serverID,
			// "Error fetching the treeviewer item content for the related
			// node!");
			//
			// Activator.getDefault().getLog().log(status);
			throw new IllegalArgumentException();
		} else if (newItem.getBrowseName() == null) {
			// Status status = new Status(Status.ERROR, "BrowseContentProvider"
			// + serverID,
			// "Error fetching the treeviewer item content for the related
			// node!");
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
			throw new IllegalArgumentException();
		} else if (newItem.getTypeDefinition() == null) {
			// Status status = new Status(Status.ERROR, "BrowseContentProvider"
			// + serverID, CustomString.getString("ERROR.NODE.ATTRIBUTES"));
			//
			// Activator.getDefault().getLog().log(status);
			throw new IllegalArgumentException();
		}

		Node node = getBrowsedNode(newItem.getNodeId(), newItem);
		newItem.setNode(node);
		// if (node instanceof VariableNode) {
		// newItem.setHistorizing(((VariableNode) node).getHistorizing());
		// }

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
			OPCRemoteTreeViewerItem item4nodeToRead) {
		Node node = null;

		// ServerModelNode server =
		// ResourceManager.getInstance()
		// .getServerModelNode(item4nodeToRead.getSession());

		if (server == null
				|| (server instanceof UAServerModelNode && !((UAServerModelNode) server)
						.isConnected())) {
			return null;
		}

		// send the service
		try {
			node = server.getDevice().getUaclient().readNode(server.getDevice().getUaclient().getActiveSession(), nodeToRead, item4nodeToRead.getNodeClass());
					// .getManager()
					// .readNodes(server.getDevice().getUaclient().getActiveSession(),
					// new NodeId[] { nodeToRead },
					// new NodeClass[] { item4nodeToRead.getNodeClass() },
					// true, true)[0];
			// creates the attributes for the current node

			// ExtensionObject nodesAttribute = null;
			// try {
			// nodesAttribute = createAttributes(newItem, result,
			// attributesToRead);
			// } catch (ArrayIndexOutOfBoundsException aioobe) {
			// // aioobe.printStackTrace();
			// }
			boolean browseParent = false;

			// if (item4nodeToRead instanceof MethodOPCTreeViewerItem) {
			// browseParent = true;
			// }

			ExpandedNodeId parentNodeId = null;
			if (browseParent) {
				ReferenceDescription[] parent = server
						.getDevice().getUaclient()
						.browse(server.getDevice().getUaclient().getActiveSession(),
								nodeToRead, BrowseDirection.Inverse, true,
								NodeClass.getMask(NodeClass.ALL),
								Identifiers.HasComponent,
								BrowseResultMask.getMask(BrowseResultMask.ALL),
								UnsignedInteger.ZERO, null, false).getReferences();

				if (parent.length == 1) {
					parentNodeId = parent[0].getNodeId();
				}
				item4nodeToRead.setParentId(parentNodeId);
			}

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
		} catch (ServiceResultException e) {
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

	// private void fetchChildren(Node node) {
	//
	// try {
	// ReferenceDescription[] childrenReferences = OPCUAClient
	// .getInstance().browse(null, node.getNodeId(),
	// BrowseDirection.Forward, true,
	// NodeClass.getMask(NodeClass.ALL),
	// Identifiers.HierarchicalReferences,
	// BrowseResultMask.getMask(BrowseResultMask.ALL),
	// UnsignedInteger.ZERO, null, false);
	//
	// } catch (ServiceResultException e) {
	//
	// }
	//
	// }

	/**
	 * Creates the ExtensionObject attributes for a Node. Attributes are
	 * different from ua node to ua node so is done with a factory
	 * 
	 * @param newItem
	 * @param results
	 * @param attributesToRead
	 * @return
	 */
	// private ExtensionObject createAttributes(OPCTreeViewerItem newItem,
	// DataValue[] results, List<UnsignedInteger> attributesToRead) {
	//
	// ExtensionObject attributes = NodeAttributeFactory.createNodeAttributes(
	// newItem.getNodeClass(), results);
	//
	// return attributes;
	// }

	public void fetchOPCTreeViewerItem(ClientSession session, OPCRemoteTreeViewerItem item) {

		Node node = getBrowsedNode(item.getNodeId(), item);

		item.setNode(node);

		if (node != null) {
			item.setBrowseName(node.getBrowseName());
			if (node.getDisplayName() != null && node.getDisplayName().getText() != null) {
				item.setDisplayName(node.getDisplayName().getText());
			} else if (node.getBrowseName() != null && node.getBrowseName().getName() != null) {
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

	public OPCUAServerModelNode getServer() {
		return server;
	}

	public void setServer(UAServerModelNode server) {
		this.server = server;
	}

}
