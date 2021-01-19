package com.bichler.astudio.opcua.addressspace.xml.exporter.elements;

import java.io.BufferedWriter;
import java.io.IOException;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.ReferenceNode;

import com.bichler.astudio.opcua.addressspace.xml.exporter.IServerTypeModel;

import opc.sdk.core.node.UAVariableTypeNode;
import opc.sdk.server.core.OPCInternalServer;

public class UAVariableTypeXMLGen extends BaseNodeXMLGen {

	public UAVariableTypeXMLGen() {
		super();
	}

	public UAVariableTypeXMLGen(UAVariableTypeNode node) {
		super(node);
	}

	@Override
	public void writeXML(BufferedWriter out, OPCInternalServer serverInstance, NamespaceTable serverTable,
			NamespaceTable exportTable, IServerTypeModel typeModel) throws IOException {
		NodeId mapId = mapNodeId(getNode().getNodeId(), serverTable, exportTable);
		// SymbolicName=\""+getNode().getBrowseName().getName().toString()+"\"

		out.write("\t<" + UAVARIABLETYPE + helpSymbolicName(getNode().getNodeId()) + helpNodeId(mapId)
				+ helpBrowseName(getNode().getBrowseName(), mapId) + helpIsAbstract(getNode())
				+ helpDataType(serverInstance, getNode()) + helpValueRank(getNode()) + helpArrayDimensions(getNode())
				+ ">");
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

		helpValue(out, serverInstance, serverTable, getNode());

		out.write("\t</" + UAVARIABLETYPE + ">");
		out.newLine();
	}

}
