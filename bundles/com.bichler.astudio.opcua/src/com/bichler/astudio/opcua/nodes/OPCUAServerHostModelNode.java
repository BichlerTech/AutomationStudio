package com.bichler.astudio.opcua.nodes;

import org.eclipse.swt.graphics.Image;

import com.bichler.astudio.navigation.nodes.StudioModelNode;

public class OPCUAServerHostModelNode extends StudioModelNode {

	private String serverName = "";

	public OPCUAServerHostModelNode() {
		super();
		// children = new ArrayList<VisuModelNode>();
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	@Override
	public void nodeDBLClicked() {
		// TODO Auto-generated method stub

	}

	@Override
	public Image getLabelImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLabelText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}
}
