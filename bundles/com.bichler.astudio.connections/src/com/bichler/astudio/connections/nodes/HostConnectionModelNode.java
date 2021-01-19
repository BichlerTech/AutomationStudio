package com.bichler.astudio.connections.nodes;

import org.eclipse.swt.graphics.Image;

import com.bichler.astudio.navigation.nodes.StudioModelNode;

public class HostConnectionModelNode extends StudioModelNode {
	
	public HostConnectionModelNode(){
		super();
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
		return this.filesystem.getConnectionName();
	}

	@Override
	public Object[] getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refresh() {
		// nothing
	}
}
