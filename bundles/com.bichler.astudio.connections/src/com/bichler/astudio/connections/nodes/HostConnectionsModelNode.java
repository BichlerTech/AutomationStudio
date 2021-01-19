package com.bichler.astudio.connections.nodes;

import org.eclipse.swt.graphics.Image;

import com.bichler.astudio.connections.ConnectionsHostManager;
import com.bichler.astudio.navigation.nodes.StudioModelNode;

public class HostConnectionsModelNode extends StudioModelNode {

	protected ConnectionsHostManager connectionsManager = null;

	public HostConnectionsModelNode() {
		super();
	}

	public ConnectionsHostManager getConnectionsManager() {
		return connectionsManager;
	}

	public void setConnectionsManager(ConnectionsHostManager manager) {
		this.connectionsManager = manager;
	}

	@Override
	public void nodeDBLClicked() {
		// TODO Auto-generated method stub

	}

	@Override
	public Image getLabelImage() {
		return null;
	}

	@Override
	public String getLabelText() {
		return null;
	}
	
	@Override
	public Object[] getChildren() {
		return this.getChildrenList().toArray();
	}

	@Override
	public void refresh() {
		// remove connection manager
		this.connectionsManager = null;
	}
}
