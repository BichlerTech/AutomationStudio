package com.bichler.astudio.opcua.driver;

import java.io.IOException;
import java.util.List;

import org.opcfoundation.ua.common.NamespaceTable;

import com.bichler.astudio.opcua.opcmodeler.commands.NamespaceTableChangeParameter;
import com.bichler.astudio.opcua.opcmodeler.commands.OPCUAUpdateNodeIdEvent;
import com.bichler.astudio.opcua.properties.driver.IDriverNode;

public interface IOPCDriverExporter {

	public void updateDatapoints(List<IDriverNode> entries, NamespaceTableChangeParameter trigger) throws IOException;

	void updateDatapoints(List<IDriverNode> entries, OPCUAUpdateNodeIdEvent trigger, NamespaceTable nsTable);

}
