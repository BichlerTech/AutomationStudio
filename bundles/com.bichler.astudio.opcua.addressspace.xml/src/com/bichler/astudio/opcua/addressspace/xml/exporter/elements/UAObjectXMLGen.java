package com.bichler.astudio.opcua.addressspace.xml.exporter.elements;

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

import com.bichler.astudio.opcua.addressspace.xml.exporter.IServerTypeModel;

import opc.sdk.core.node.UAObjectNode;
import opc.sdk.server.core.OPCInternalServer;

public class UAObjectXMLGen extends BaseNodeXMLGen {

	public UAObjectXMLGen() {
		super();
	}

	public UAObjectXMLGen(UAObjectNode node) {
		super(node);
	}

	@Override
	public void writeXML(BufferedWriter out, OPCInternalServer serverInstance, NamespaceTable serverTable,
			NamespaceTable exportTable, IServerTypeModel typeModel) throws IOException {
		NodeId mapId = mapNodeId(getNode().getNodeId(), serverTable, exportTable);

//		if (mapId.getValue() instanceof UnsignedInteger && ((UnsignedInteger) mapId.getValue()).longValue() == 5032) {
//			System.out.println("");
//		}

		boolean isDefaultEncoding = isDefaultEncoding(serverInstance);
		out.write("\t<" + UAOBJECT + helpSymbolicName(getNode().getNodeId())
				+ helpParentNodeId(serverInstance, getNode(), exportTable) + helpNodeId(mapId)
				+ helpBrowseName(getNode().getBrowseName(), mapId, isDefaultEncoding) + helpEventNofifier(getNode()) + ">");
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

		writeExtensions(out);

		out.write("\t</" + UAOBJECT + ">");
		out.newLine();
	}

	private boolean isDefaultEncoding(OPCInternalServer serverInstance) {
		ExpandedNodeId typeDef = getNode().findTarget(Identifiers.HasTypeDefinition, false);
		if(ExpandedNodeId.isNull(typeDef)) {
			return false;
		}
		
		NodeId typeId = NodeId.NULL;
		try {
			typeId = serverInstance.getNamespaceUris().toNodeId(typeDef);
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
