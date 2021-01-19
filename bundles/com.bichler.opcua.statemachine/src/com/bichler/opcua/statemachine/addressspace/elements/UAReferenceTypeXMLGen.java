package com.bichler.opcua.statemachine.addressspace.elements;

import java.io.BufferedWriter;
import java.io.IOException;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.ReferenceNode;

import com.bichler.opcua.statemachine.addressspace.importer.StatemachineNodesetImporter;

import opc.sdk.core.node.UAReferenceTypeNode;

public class UAReferenceTypeXMLGen extends BaseNodeXMLGen {

	public UAReferenceTypeXMLGen() {
		super();
	}
	
	public UAReferenceTypeXMLGen(UAReferenceTypeNode node) {
		super(node);
	}

	@Override
	public void writeXML(BufferedWriter out, StatemachineNodesetImporter importer, NamespaceTable serverTable,
			NamespaceTable exportTable) throws IOException {
		NodeId mapId = mapNodeId(getNode().getNodeId(), serverTable, exportTable);
		// SymbolicName=\""+getNode().getBrowseName().getName().toString()+"\"

		out.write("\t<" + UAREFERENCETYPE + helpNodeId(mapId) + helpBrowseName(getNode().getBrowseName())
				 + helpIsAbstract(getNode()) + helpInverseName(getNode())+ helpSymmetric(getNode())+">");
		out.newLine();
		helpDisplayName(out, getNode().getDisplayName());
		helpDescription(out, getNode().getDescription());

		out.write("\t\t<" + REFERENCES + ">");
		out.newLine();
		for (ReferenceNode refNode : getNode().getReferences()) {
			writeXMLReference(out, refNode, serverTable, exportTable, importer);
		}
		out.write("\t\t</" + REFERENCES + ">");
		out.newLine();

		out.write("\t</" + UAREFERENCETYPE + ">");
		out.newLine();
	}
}
