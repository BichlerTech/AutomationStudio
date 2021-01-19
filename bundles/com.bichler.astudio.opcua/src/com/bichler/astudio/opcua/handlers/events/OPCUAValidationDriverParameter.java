package com.bichler.astudio.opcua.handlers.events;

import com.bichler.astudio.opcua.nodes.OPCUAServerModelNode;

public class OPCUAValidationDriverParameter extends AbstractOPCUAValidationParameter {

	private String drvName = "";
	private OPCUAServerModelNode node;

	public OPCUAValidationDriverParameter() {
		super();
	}

	public String getDrvName() {
		return drvName;
	}

	public void setDrvName(String drvName) {
		this.drvName = drvName;
	}

	public OPCUAServerModelNode getModelNode() {
		return this.node;
	}

	public void setModelNode(OPCUAServerModelNode node) {
		this.node = node;
	}
}
