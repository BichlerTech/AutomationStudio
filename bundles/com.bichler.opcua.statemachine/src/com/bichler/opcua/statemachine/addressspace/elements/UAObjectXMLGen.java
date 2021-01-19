package com.bichler.opcua.statemachine.addressspace.elements;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.ReferenceNode;

import com.bichler.opcua.statemachine.addressspace.importer.StatemachineNodesetImporter;

import opc.sdk.core.node.UAObjectNode;

public class UAObjectXMLGen extends BaseNodeXMLGen {

	public UAObjectXMLGen() {
		super();
	}

	public UAObjectXMLGen(UAObjectNode node) {
		super(node);
	}

	@Override
	public void writeXML(BufferedWriter out, StatemachineNodesetImporter importer, NamespaceTable serverTable,
			NamespaceTable exportTable) throws IOException {
		NodeId mapId = mapNodeId(getNode().getNodeId(), serverTable, exportTable);
		boolean isDefaultEncoding = isDefaultEncoding(importer);
		
		out.write("\t<" + UAOBJECT + helpNodeId(mapId) + helpBrowseName(getNode().getBrowseName(), isDefaultEncoding)
				+ helpParentNodeId(importer, getNode(), serverTable, exportTable) + helpEventNofifier(getNode()) +">");
		out.newLine();
		helpDisplayName(out, getNode().getDisplayName());
		helpDescription(out, getNode().getDescription());

		out.write("\t\t<" + REFERENCES + ">");
		out.newLine();
		for (ReferenceNode refNode : getNode().getReferences()) {			
			writeXMLReference(out, refNode, serverTable, exportTable, importer);
		}
		out.write("\t\t</"+REFERENCES+">");
		out.newLine();

		out.write("\t</" + UAOBJECT + ">");
		out.newLine();
	}

	private boolean isDefaultEncoding(StatemachineNodesetImporter importer) {
		ExpandedNodeId typeDef = getNode().findTarget(Identifiers.HasTypeDefinition, false);
		NodeId typeId = NodeId.NULL;
		try {
			typeId = importer.getNamespaceTable().toNodeId(typeDef);
			if (NodeId.equals(Identifiers.DataTypeEncodingType, typeId)) {
				return true;
			}
		} catch (ServiceResultException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					"Cannot convert ExpandedNodeId to NodeId " + typeDef.toString());
		}
		return false;
	}
}
