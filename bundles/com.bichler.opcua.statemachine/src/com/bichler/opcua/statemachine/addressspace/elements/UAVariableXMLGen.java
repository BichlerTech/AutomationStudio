package com.bichler.opcua.statemachine.addressspace.elements;

import java.io.BufferedWriter;
import java.io.IOException;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceNode;

import com.bichler.opcua.statemachine.addressspace.importer.StatemachineNodesetImporter;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.UAVariableNode;

public class UAVariableXMLGen extends BaseNodeXMLGen {

	public UAVariableXMLGen() {
		super();
	}

	public UAVariableXMLGen(UAVariableNode node) {
		super(node);
	}

	@Override
	public void writeXML(BufferedWriter out, StatemachineNodesetImporter importer, NamespaceTable serverTable,
			NamespaceTable exportTable) throws IOException {

		boolean isMethodArg = isMethodArgument(importer);

		NodeId mapId = mapNodeId(getNode().getNodeId(), serverTable, exportTable);
		// SymbolicName=\""+getNode().getBrowseName().getName().toString()+"\"

		out.write("\t<" + UAVARIABLE + helpNodeId(mapId) + helpBrowseName(getNode().getBrowseName(), isMethodArg)
				+ helpParentNodeId(importer, getNode(), serverTable, exportTable) + helpWriteMask(getNode())
				+ helpUserWriteMask(getNode()) + helpDataType(importer, getNode()) + helpValueRank(getNode())
				+ helpArrayDimension((UAVariableNode) getNode()) + helpAccessLevel(getNode())
				+ helpUserAccessLevel(getNode()) + helpMinimumSamplingInterval(getNode()) + helpHistorizing(getNode())
				+ ">");
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

		helpValue(out, serverTable, getNode());

		out.write("\t</" + UAVARIABLE + ">");
		out.newLine();
	}

	private boolean isMethodArgument(StatemachineNodesetImporter importer) {
		ExpandedNodeId target = getNode().findTarget(Identifiers.HasProperty, true);
		Node targetNode = importer.getNodesItemById(target);
		
		if(targetNode == null) {
			return false;
		}
		
		if(targetNode.getNodeClass() == NodeClass.Method) {
			return true;
		}
		
		return false;
	}

}
