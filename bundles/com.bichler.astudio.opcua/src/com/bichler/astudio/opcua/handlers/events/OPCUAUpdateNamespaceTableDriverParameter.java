package com.bichler.astudio.opcua.handlers.events;

import com.bichler.astudio.opcua.opcmodeler.commands.NamespaceTableChangeParameter;
import com.bichler.astudio.opcua.components.ui.handler.update.IOPCUAUpdateable;
import com.bichler.astudio.opcua.nodes.OPCUAServerDriverModelNode;

public class OPCUAUpdateNamespaceTableDriverParameter extends NamespaceTableChangeParameter
		implements IOPCUAUpdateable {

	public static final String PARAMETER_ID = "updatedrivernamespacetable";

	private OPCUAServerDriverModelNode driver;

	public OPCUAUpdateNamespaceTableDriverParameter(NamespaceTableChangeParameter trigger,
			OPCUAServerDriverModelNode driver) {
		super();

		setIndexMapping(trigger.getIndexMapping());
		setMapping(trigger.getMapping());
		setNamespaceTable2change(trigger.getNamespaceTable2change());
		setOriginTable(trigger.getOriginNamespaceTable());
		this.driver = driver;
	}

	@Override
	public String getDrvName() {
		return this.driver.getDriverName();
	}

}
