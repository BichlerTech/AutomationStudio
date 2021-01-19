package com.bichler.opcua.statemachine.addressspace.elements;

import java.io.BufferedWriter;
import java.io.IOException;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.ReferenceNode;

import com.bichler.astudio.opcua.components.addressspace.DefinitionBean;
import com.bichler.astudio.opcua.components.addressspace.DefinitionFieldBean;
import com.bichler.opcua.statemachine.addressspace.importer.StatemachineNodesetImporter;

import opc.sdk.core.node.UADataTypeNode;

public class UADataTypeXMLGen extends BaseNodeXMLGen {

	public UADataTypeXMLGen() {
		super();
	}

	public UADataTypeXMLGen(UADataTypeNode node) {
		super(node);
	}

	@Override
	public void writeXML(BufferedWriter out, StatemachineNodesetImporter importer, NamespaceTable serverTable,
			NamespaceTable exportTable) throws IOException {
		
		NodeId mapId = mapNodeId(getNode().getNodeId(), serverTable, exportTable);
		// SymbolicName=\""+getNode().getBrowseName().getName().toString()+"\"

		out.write("\t<" + UADATATYPE + helpNodeId(mapId) + helpBrowseName(getNode().getBrowseName()) + ">");
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
		
		helpDefinition(out, importer.getDatatypeDefinition().get(getNode().getNodeId()));
		
		out.write("\t</" + UADATATYPE + ">");
		out.newLine();
	}
	
	private void helpDefinition(BufferedWriter out, DefinitionBean definition) throws IOException {		
		if(definition == null) {
			return ;
		}
		out.write("\t\t<Definition Name=\""+definition.getDefinitionName()+"\">");
		out.newLine();
		for(DefinitionFieldBean field : definition.getFields()) {
			String dataType = field.getDatatype() != null ? " DataType=\""+field.getDatatype().toString()+"\"" : "";
			String valueRank = field.getValueRank() != null ? " ValueRank=\""+field.getValueRank()+"\"" : "";
			String value = field.getValue() != null ? " Value=\""+field.getValue()+"\"" : "";
			out.write("\t\t\t<Field Name=\""+field.getName()+"\""+dataType+valueRank+value+" />");
			out.newLine();
		}
		out.write("\t\t</Definition>");
		out.newLine();
	}
}
