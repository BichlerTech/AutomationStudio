package com.bichler.astudio.opcua.addressspace.xml.exporter.elements;

import java.io.BufferedWriter;
import java.io.IOException;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.ReferenceNode;

import com.bichler.astudio.opcua.addressspace.xml.exporter.IServerTypeModel;

import opc.sdk.core.node.UAObjectTypeNode;
import opc.sdk.server.core.OPCInternalServer;

public class UAObjectTypeXMLGen extends BaseNodeXMLGen {

	public UAObjectTypeXMLGen() {
		super();
	}

	public UAObjectTypeXMLGen(UAObjectTypeNode node) {
		super(node);
	}

	@Override
	public void writeXML(BufferedWriter out, OPCInternalServer serverInstance, NamespaceTable serverTable,
			NamespaceTable exportTable, IServerTypeModel typeModel) throws IOException {
		NodeId mapId = mapNodeId(getNode().getNodeId(), serverTable, exportTable);
		// SymbolicName=\""+getNode().getBrowseName().getName().toString()+"\"
		// ParentNodeId=

		out.write("\t<" + UAOBJECTTYPE + helpSymbolicName(getNode().getNodeId()) + helpNodeId(mapId) + helpBrowseName(getNode().getBrowseName(), mapId)
				+  helpIsAbstract(getNode()) + ">");
		out.newLine();
		helpDisplayName(out, getNode().getDisplayName());
		helpDescription(out, getNode().getDescription());
	
		out.write("\t\t<" + REFERENCES + ">");
		out.newLine();
		for (ReferenceNode refNode : getNode().getReferences()) {
			writeXMLReference(out, serverInstance, refNode, serverTable, exportTable);
		}
		out.write("\t\t</" + REFERENCES + ">");
		out.newLine();

		out.write("\t</" + UAOBJECTTYPE + ">");
		out.newLine();
	}
}
