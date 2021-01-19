package com.bichler.opcua.statemachine.addressspace;

import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.ReferenceNode;

import com.bichler.opcua.statemachine.addressspace.elements.BaseNodeXMLGen;
import com.bichler.opcua.statemachine.addressspace.elements.BaseXMLGen;
import com.bichler.opcua.statemachine.addressspace.elements.UAAlias;
import com.bichler.opcua.statemachine.addressspace.importer.StatemachineNodesetImporter;

import opc.sdk.core.application.operation.ICancleOperation;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.UAVariableNode;
import opc.sdk.core.node.UAVariableTypeNode;

public class XMLModelV2Factory extends AbstractXMLModelFactory {

	private NamespaceTable nsTable;
	// private OPCInternalServer serverInstance;

	public XMLModelV2Factory(ICancleOperation progressMonitor, NamespaceTable nsTable) {
		super(progressMonitor);

		this.nsTable = nsTable;
	}

	@Override
	public boolean export(BufferedWriter out, StatemachineNodesetImporter importer, List<Node> nodes,
			NamespaceTable namespaces2export, Map<String, List<String>> nsRequiredTable) {

		try {
			// start document
			startDocument(out);
			if (checkProgressMonitorCancled()) {
				return false;
			}

			// write namespace table
			writeModelNamespaceDefinition(out, namespaces2export, nsRequiredTable);
			if (checkProgressMonitorCancled()) {
				return false;
			}

			// write namespace table
			List<UAAlias> aliases = defineAliases(importer, nodes);
			writeAliases(out, aliases);
			if (checkProgressMonitorCancled()) {
				return false;
			}

			// write nodes
			importer.setAdditionalNodes(nodes);
			writeNodes(out, importer, nodes, namespaces2export);
			if (checkProgressMonitorCancled()) {
				return false;
			}
			importer.clearAdditionalNodes();

			// end document
			endDocument(out);
			if (checkProgressMonitorCancled()) {
				return false;
			}
		} catch (IOException e) {
			Logger.getLogger(XMLModelV2Factory.class.getName()).log(Level.SEVERE, null, e);
			return false;
		}

		return true;
	}

	private List<UAAlias> defineAliases(StatemachineNodesetImporter importer, List<Node> nodes) {
		Map<NodeId, UAAlias> aliases = new HashMap<>();

		for (Node node : nodes) {
			// alias for references
			ReferenceNode[] references = node.getReferences();
			if (references != null) {
				for (ReferenceNode reference : references) {
					UAAlias alias = defineAliasReferenceType(importer, reference);
					if (alias == null) {
						continue;
					}
					aliases.put(reference.getReferenceTypeId(), alias);
				}
			}

			// alias for datatypes
			NodeId datatypeId = NodeId.NULL;
			if (node instanceof UAVariableNode) {
				datatypeId = ((UAVariableNode) node).getDataType();
				UAAlias alias = defineAliasDataType(importer, datatypeId);
				if (alias != null) {
					aliases.put(datatypeId, alias);
				}
			} else if (node instanceof UAVariableTypeNode) {
				datatypeId = ((UAVariableTypeNode) node).getDataType();
				UAAlias alias = defineAliasDataType(importer, datatypeId);
				if (alias != null) {
					aliases.put(datatypeId, alias);
				}
			}
		}

		List<UAAlias> aliasList = new ArrayList<>(aliases.values());
		aliasList.sort(new Comparator<UAAlias>() {

			@Override
			public int compare(UAAlias o1, UAAlias o2) {
				return o1.getNodeId().compareTo(o2.getNodeId());
			}
		});

		return aliasList;
	}

	private UAAlias defineAliasReferenceType(StatemachineNodesetImporter importer, ReferenceNode reference) {
		NodeId referenceTypeId = reference.getReferenceTypeId();

		Node referenceTypeNode = importer.getNodesItemById(referenceTypeId);
		if (referenceTypeNode == null) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					"DatatypeNode " + (referenceTypeId != null ? referenceTypeId.toString() : "")
							+ " does not exist in OpcUa AddressSpace");
			return null;
		}

		UAAlias alias = new UAAlias(referenceTypeId, referenceTypeNode.getBrowseName().getName());
		return alias;
	}

	private UAAlias defineAliasDataType(StatemachineNodesetImporter importer, NodeId datatypeId) {
//		if (NodeId.isNull(datatypeId)) {
//			return null;
//		}

		Node dataTypeNode = importer.getNodesItemById(datatypeId);
		if (dataTypeNode == null) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "DatatypeNode "
					+ (datatypeId != null ? datatypeId.toString() : "") + " does not exist in OpcUa AddressSpace");
			return null;
		}
		UAAlias alias = new UAAlias(datatypeId, dataTypeNode.getBrowseName().getName());
		return alias;
	}

	private void writeAliases(BufferedWriter out, List<UAAlias> aliases) throws IOException {
		out.write("\t<" + BaseNodeXMLGen.ALIASES + ">");
		out.newLine();
		for (UAAlias alias : aliases) {
			out.write("\t\t<" + BaseNodeXMLGen.ALIAS + " " + BaseNodeXMLGen.ALIAS + "=\"" + alias.getAliasName() + "\">"
					+ alias.getNodeId().toString() + "</" + BaseNodeXMLGen.ALIAS + ">");
			out.newLine();
		}

		out.write("\t</" + BaseNodeXMLGen.ALIASES + ">");
		out.newLine();
	}

	private void startDocument(BufferedWriter out) throws IOException {
		String time = dateNow();

		out.write("<" + BaseNodeXMLGen.UANODESET + " " + BaseNodeXMLGen.XMLNS_XSD + " " + BaseNodeXMLGen.XMLNS_XSI + " "
				+ BaseNodeXMLGen.LAST_MODIFIED + "=\"" + time + "\" " + BaseNodeXMLGen.XMLNS_NODESET + ">");
		out.newLine();
	}

	private void writeModelNamespaceDefinition(BufferedWriter out, NamespaceTable namespaces2export,
			Map<String, List<String>> nsRequiredTable) throws IOException {

		// write OPC UA namespace table uris
		out.write("\t<" + BaseNodeXMLGen.NAMESPACEURIS + ">");
		out.newLine();
		for (String namespace : namespaces2export.toArray()) {
			// no default opc ua namespace
			if (NamespaceTable.OPCUA_NAMESPACE.equalsIgnoreCase(namespace)) {
				continue;
			}

			out.write("\t\t<" + BaseNodeXMLGen.URI + ">" + namespace + "</" + BaseNodeXMLGen.URI + ">");
			out.newLine();
		}
		out.write("\t</" + BaseNodeXMLGen.NAMESPACEURIS + ">");
		out.newLine();
		// write OPC UA models and required models
		out.write("\t<" + BaseNodeXMLGen.MODELS + ">");
		out.newLine();

		for (String namespace : namespaces2export.toArray()) {
			List<String> requiredNs = nsRequiredTable.get(namespace);
			if (requiredNs == null || (requiredNs != null && requiredNs.isEmpty())) {
				continue;
			}

			out.write("\t\t<" + BaseNodeXMLGen.MODEL + " " + BaseNodeXMLGen.MODEL_URI + "=\"" + namespace + "\" "
					+ BaseNodeXMLGen.PUBLICATION_NDATE + "=\"" + dateNow() + "\" " + BaseNodeXMLGen.VERSION
					+ "=\"1.0.0\">");
			out.newLine();

			for (String s : requiredNs) {
				out.write("\t\t\t<" + BaseNodeXMLGen.REQUIRED_MODEL + " " + BaseNodeXMLGen.MODEL_URI + "=\"" + s + "\" "
						+ BaseNodeXMLGen.VERSION + "=\"1.00\" " + BaseNodeXMLGen.PUBLICATION_NDATE + "=\"" + dateNow()
						+ "\" />");
				out.newLine();
			}
			out.write("\t\t</" + BaseNodeXMLGen.MODEL + ">");
			out.newLine();
		}
		out.write("\t</" + BaseNodeXMLGen.MODELS + ">");
		out.newLine();
	}

	private void writeNodes(BufferedWriter out, StatemachineNodesetImporter importer, List<Node> nodes,
			NamespaceTable namespaces) {
//		for(List<Node> nodeSet : nodes) {
		for (Node node : nodes) {
			if (node.getNodeId().getNamespaceIndex() == 0) {
				continue;
			}
			BaseXMLGen element = XMLElementFactory.factor(node);
			try {
				element.writeXML(out, importer, this.nsTable, namespaces);
			} catch (IOException e) {
				Logger.getLogger(XMLModelV2Factory.class.getName()).log(Level.SEVERE, node.toString(), e);
			}
		}
//		}
	}

	private void endDocument(BufferedWriter out) throws IOException {
		out.write("</" + BaseNodeXMLGen.UANODESET + ">");
		out.flush();
	}

	private String dateNow() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
		return formatter.format(LocalDate.now()) + "T00:00:00Z";
	}
}
