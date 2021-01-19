package com.bichler.opcua.statemachine.transform;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;

public class ExtractOpcUaStructureContext {
	private boolean isTypeDefined = false;
	private ExpandedNodeId nodeIdOfType;

	public ExpandedNodeId getNodeIdOfType() {
		return this.nodeIdOfType;
	}
	
	public boolean isTypeDefined() {
		return isTypeDefined;
	}

	public void setTypeDefined(boolean isTypeDefined) {
		this.isTypeDefined = isTypeDefined;
	}

	public void setIdOfType(ExpandedNodeId typeId) {
		this.nodeIdOfType = typeId;
		
	}

}
