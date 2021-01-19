package com.bichler.astudio.view.drivermodel.handler.util;

import com.bichler.astudio.opcua.widget.model.AbstractDatapointNode;

public abstract class AbstractDriverModelViewNode extends AbstractDatapointNode implements INodeDecorator {

	private IDriverStructResourceManager structManager;

	public AbstractDriverModelViewNode(
			IDriverStructResourceManager structManager) {
		this.structManager = structManager;
	}

	public IDriverStructResourceManager getStructureManager() {
		return this.structManager;
	}
	
	public abstract String getText();

}
