package com.bichler.astudio.opcua.events;

import com.bichler.astudio.opcua.opcmodeler.commands.OPCUAUpdateNodeIdEvent;
import com.bichler.astudio.opcua.nodes.OPCUAServerDriverModelNode;

public class OPCUAUpdateNodeIdDriverParameter {

	public static final String PARAMETER_ID = "opcuaupdatenodeiddriver";

	private OPCUAUpdateNodeIdEvent trigger;
	private OPCUAServerDriverModelNode drivernode;

	public OPCUAUpdateNodeIdDriverParameter(OPCUAUpdateNodeIdEvent trigger, OPCUAServerDriverModelNode drivernode) {
		this.trigger = trigger;
		this.drivernode = drivernode;
	}

	public OPCUAUpdateNodeIdEvent getTrigger() {
		return trigger;
	}

	public OPCUAServerDriverModelNode getDrivernode() {
		return drivernode;
	}

}
