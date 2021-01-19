package com.bichler.opcua.statemachine.addressspace.elements;

import java.io.BufferedWriter;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;

import com.bichler.opcua.statemachine.addressspace.importer.StatemachineNodesetImporter;

import opc.sdk.core.node.ViewNode;


public class UAViewXMLGen extends BaseNodeXMLGen {

	public UAViewXMLGen() {
		super();
	}

	public UAViewXMLGen(ViewNode node) {
		super(node);
	}

	@Override
	public void writeXML(BufferedWriter out, StatemachineNodesetImporter importer, NamespaceTable serverTable, NamespaceTable exportTable) {
		//NodeId mapId = mapNodeId(getNode().getNodeId(), serverTable, exportTable);
	}
}
