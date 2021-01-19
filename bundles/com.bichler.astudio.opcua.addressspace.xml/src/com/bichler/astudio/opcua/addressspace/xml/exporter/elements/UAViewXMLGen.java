package com.bichler.astudio.opcua.addressspace.xml.exporter.elements;

import java.io.BufferedWriter;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;

import com.bichler.astudio.opcua.addressspace.xml.exporter.IServerTypeModel;

import opc.sdk.core.node.ViewNode;
import opc.sdk.server.core.OPCInternalServer;

public class UAViewXMLGen extends BaseNodeXMLGen {

	public UAViewXMLGen() {
		super();
	}

	public UAViewXMLGen(ViewNode node) {
		super(node);
	}

	@Override
	public void writeXML(BufferedWriter out, OPCInternalServer serverInstance, NamespaceTable serverTable, NamespaceTable exportTable, IServerTypeModel typeModel) {
		NodeId mapId = mapNodeId(getNode().getNodeId(), serverTable, exportTable);
	}
}
