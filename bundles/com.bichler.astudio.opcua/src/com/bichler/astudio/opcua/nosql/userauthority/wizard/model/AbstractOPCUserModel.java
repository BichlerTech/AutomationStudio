package com.bichler.astudio.opcua.nosql.userauthority.wizard.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.user.AuthorityRule;
import opc.sdk.core.node.user.DBAuthority;
import opc.sdk.server.core.OPCInternalServer;

import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.NodeClass;

public abstract class AbstractOPCUserModel {
	// parent
	private AbstractOPCUserModel parent = null;
	//
	private List<AbstractOPCUserModel> children = new ArrayList<AbstractOPCUserModel>();
	private String displayname = "";
	private NodeClass nodeClass = NodeClass.Unspecified;
	private OPCInternalServer server;
	private NodeId nodeId;
	/**
	 * Describes how a client user can access the node
	 */
	private DBAuthority authority = null;
	private boolean browsed = false;
	private boolean includeChildren = false;

	public AbstractOPCUserModel(String displayname, NodeClass nodeClass, NodeId nodeId) {
		this.setDisplayname(displayname);
		this.setNodeClass(nodeClass);
		this.setNodeId(nodeId);
		this.authority = new DBAuthority();
	}

	public void addChild(AbstractOPCUserModel child) {
		child.setParent(this);
		child.setServer(server);
		this.children.add(child);
	}

	private void afterFetchChildren() {
		this.browsed = true;
	}

	/**
	 * fetch children with the client instance and use addChild for them
	 * 
	 * @param force   TRUE fetch the tree children by code, FALSE by click selection
	 * @param mapping
	 */
	public void fetchChildren(boolean force) {
		if (this.browsed) {
			return;
		}
		NodeId parentId = this.getNodeId();
		Node[] nodes = this.server.getAddressSpaceManager().findChildren(parentId);
		for (Node node : nodes) {
			LocalizedText name = node.getDisplayName();
			NodeClass nc = node.getNodeClass();
			NodeId nodeId = node.getNodeId();
			NodeUserRoleModel newModel = new NodeUserRoleModel(name.getText(), nc, nodeId);
			// force to fetch children when a role has been created
			if (force) {
				newModel.setIncludeChildren(force);
				newModel.getAuthority().setAuthorityRole(getAuthority().getAuthorityRole());
			} else if (!force && this.includeChildren) {
				newModel.setIncludeChildren(this.includeChildren);
				newModel.getAuthority().setAuthorityRole(getAuthority().getAuthorityRole());
			}
			addChild(newModel);
		}
		afterFetchChildren();
	}

	public Collection<AbstractOPCUserModel> updateAuthority(AuthorityRule role, boolean isSet,
			boolean includeChildren) {
		// sets include children
		this.includeChildren = includeChildren;
		List<AbstractOPCUserModel> items = new ArrayList<>();
		this.authority.setAuthorityRole(role, isSet);
		items.add(this);
		if (includeChildren) {
			for (AbstractOPCUserModel item : this.children) {
				items.addAll(item.updateAuthority(role, isSet, includeChildren));
			}
		}
		return items;
	}

	public DBAuthority getAuthority() {
		return authority;
	}

	public AbstractOPCUserModel[] getChildren() {
		return children.toArray(new AbstractOPCUserModel[0]);
	}

	public String getDisplayname() {
		return displayname;
	}

	public NodeClass getNodeClass() {
		return nodeClass;
	}

	public NodeId getNodeId() {
		return this.nodeId;
	}

	public AbstractOPCUserModel getParent() {
		return parent;
	}

	public boolean isIncludeChildren() {
		return this.includeChildren;
	}

	public boolean isBrowsed() {
		return this.browsed;
	}

	public void setIncludeChildren(boolean includeChildren) {
		this.includeChildren = includeChildren;
	}

	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}

	public void setNodeClass(NodeClass nodeClass) {
		this.nodeClass = nodeClass;
	}

	protected void setNodeId(NodeId nodeId) {
		this.nodeId = nodeId;
	}

	private void setParent(AbstractOPCUserModel parent) {
		this.parent = parent;
	}

	private void setAuthority(DBAuthority authority) {
		this.authority = authority;
	}

	protected void setServer(OPCInternalServer server) {
		this.server = server;
	}

	public void mapChildren(Map<NodeId, DBAuthority> mapping) {
		if (mapping == null || mapping.isEmpty()) {
			return;
		}
		for (AbstractOPCUserModel child : this.children) {
			NodeId nodeId = child.getNodeId();
			DBAuthority childAuthority = mapping.get(nodeId);
			// mapping null are all rights
			if (childAuthority != null) {
				child.setAuthority(childAuthority);
			}
		}
	}
}
