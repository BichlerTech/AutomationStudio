package com.bichler.astudio.opcua.addressspace.xml.exporter.elements;

import java.io.BufferedWriter;
import java.io.IOException;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.ReferenceNode;

import com.bichler.astudio.opcua.addressspace.xml.exporter.IServerTypeModel;
import com.bichler.astudio.opcua.components.addressspace.DefinitionBean;
import com.bichler.astudio.opcua.components.addressspace.DefinitionFieldBean;
import com.bichler.astudio.opcua.components.ui.Studio_ResourceManager;

import opc.sdk.core.node.UADataTypeNode;
import opc.sdk.server.core.OPCInternalServer;

public class UADataTypeXMLGen extends BaseNodeXMLGen {

	public UADataTypeXMLGen() {
		super();
	}

	public UADataTypeXMLGen(UADataTypeNode node) {
		super(node);
	}

	@Override
	public void writeXML(BufferedWriter out, OPCInternalServer serverInstance, NamespaceTable serverTable,
			NamespaceTable exportTable, IServerTypeModel typeModel) throws IOException {
		NodeId mapId = mapNodeId(getNode().getNodeId(), serverTable, exportTable);
		// SymbolicName=\""+getNode().getBrowseName().getName().toString()+"\"

		out.write("\t<" + UADATATYPE + helpSymbolicName(getNode().getNodeId()) + helpNodeId(mapId) + helpBrowseName(getNode().getBrowseName(), mapId) + ">");
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

		helpDefinition(out, Studio_ResourceManager.DATATYPE_DEFINITIONS.get(getNode().getNodeId()));
		
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
