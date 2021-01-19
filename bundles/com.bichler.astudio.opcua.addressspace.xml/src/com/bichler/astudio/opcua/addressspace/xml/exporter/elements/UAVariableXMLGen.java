package com.bichler.astudio.opcua.addressspace.xml.exporter.elements;

import java.io.BufferedWriter;
import java.io.IOException;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceNode;

import com.bichler.astudio.opcua.addressspace.xml.exporter.IServerTypeModel;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.UAVariableNode;
import opc.sdk.server.core.OPCInternalServer;

public class UAVariableXMLGen extends BaseNodeXMLGen {

	public UAVariableXMLGen() {
		super();
	}

	public UAVariableXMLGen(UAVariableNode node) {
		super(node);
	}

	@Override
	public void writeXML(BufferedWriter out, OPCInternalServer serverInstance, NamespaceTable serverTable,
			NamespaceTable exportTable, IServerTypeModel typeModel) throws IOException {
		NodeId mapId = mapNodeId(getNode().getNodeId(), serverTable, exportTable);
		// SymbolicName=\""+getNode().getBrowseName().getName().toString()+"\"

		boolean isMethodArg = isMethodArgument(serverInstance);
		
		out.write("\t<" + UAVARIABLE + helpSymbolicName(getNode().getNodeId())
				+ helpParentNodeId(serverInstance, getNode(), exportTable) + helpNodeId(mapId)
				+ helpBrowseName(getNode().getBrowseName(), mapId, isMethodArg) + helpWriteMask(getNode()) + helpUserWriteMask(getNode())
				+ helpDataType(serverInstance, getNode()) + helpValueRank(getNode())
				+ helpArrayDimension((UAVariableNode) getNode()) + helpAccessLevel(getNode())
				+ helpUserAccessLevel(getNode()) + helpMinimumSamplingInterval(getNode()) + helpHistorizing(getNode())
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

		writeExtensions(out);

		out.write("\t</" + UAVARIABLE + ">");
		out.newLine();
	}

	

	private boolean isMethodArgument(OPCInternalServer serverInstance) {
		ExpandedNodeId target = getNode().findTarget(Identifiers.HasProperty, true);
		if(ExpandedNodeId.isNull(target)) {
			return false;
		}
		
		Node targetNode = serverInstance.getAddressSpaceManager().getNodeById(target);
		if(targetNode == null) {
			return false;
		}
		if(targetNode.getNodeClass() == NodeClass.Method) {
			return true;
		}
		
		return false;
	}

}
