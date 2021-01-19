package com.bichler.astudio.opcua.addressspace.xml.exporter.factory;

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
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.ReferenceNode;

import com.bichler.astudio.opcua.addressspace.xml.exporter.IServerTypeModel;
import com.bichler.astudio.opcua.addressspace.xml.exporter.elements.BaseNodeXMLGen;
import com.bichler.astudio.opcua.addressspace.xml.exporter.elements.BaseXMLGen;
import com.bichler.astudio.opcua.addressspace.xml.exporter.elements.UAAlias;
import com.bichler.astudio.opcua.components.ui.Studio_ResourceManager;

import opc.sdk.core.application.operation.ICancleOperation;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.UAVariableNode;
import opc.sdk.core.node.UAVariableTypeNode;
import opc.sdk.server.core.OPCInternalServer;

public class XMLModelV2Factory extends AbstractXMLModelFactory {

	private NamespaceTable nsTable;
	private OPCInternalServer serverInstance;

	public XMLModelV2Factory(ICancleOperation progressMonitor, OPCInternalServer serverInstance) {
		super(progressMonitor);
		this.serverInstance = serverInstance;
		this.nsTable = serverInstance.getNamespaceUris();
	}

	@Override
	public boolean export(BufferedWriter out, List<Node> nodes, NamespaceTable namespaces2export,
			Map<String, List<String>> nsRequiredTable, IServerTypeModel typeModel) {
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
			List<UAAlias> aliases = defineAliases(nodes);
			writeAliases(out, aliases);
			if (checkProgressMonitorCancled()) {
				return false;
			}

			// write nodes
			writeNodes(out, nodes, namespaces2export, typeModel);
			if (checkProgressMonitorCancled()) {
				return false;
			}

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

	private String dateNow() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
		return formatter.format(LocalDate.now()) + "T00:00:00Z";
	}

	private List<UAAlias> defineAliases(List<Node> nodes) {
		Map<NodeId, UAAlias> aliases = new HashMap<>();

		for (Node node : nodes) {
			// alias for references
			ReferenceNode[] references = node.getReferences();
			if (references != null) {
				for (ReferenceNode reference : references) {
					UAAlias alias = defineAliasReferenceType(reference);
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
				UAAlias alias = defineAliasDataType(datatypeId);
				if (alias != null) {
					aliases.put(datatypeId, alias);
				}
			} else if (node instanceof UAVariableTypeNode) {
				datatypeId = ((UAVariableTypeNode) node).getDataType();
				UAAlias alias = defineAliasDataType(datatypeId);
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

	private UAAlias defineAliasReferenceType(ReferenceNode reference) {
		NodeId referenceTypeId = reference.getReferenceTypeId();

		Node referenceTypeNode = this.serverInstance.getAddressSpaceManager().getNodeById(referenceTypeId);
		if (referenceTypeNode == null) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					"DatatypeNode " + (referenceTypeId != null ? referenceTypeId.toString() : "")
							+ " does not exist in OpcUa AddressSpace");
			return null;
		}

		UAAlias alias = new UAAlias(referenceTypeId, referenceTypeNode.getBrowseName().getName());
		return alias;
	}

	private UAAlias defineAliasDataType(NodeId datatypeId) {
		Node dataTypeNode = this.serverInstance.getAddressSpaceManager().getNodeById(datatypeId);
		if (dataTypeNode == null) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "DatatypeNode "
					+ (datatypeId != null ? datatypeId.toString() : "") + " does not exist in OpcUa AddressSpace");
			return null;
		}
		UAAlias alias = new UAAlias(datatypeId, dataTypeNode.getBrowseName().getName());
		return alias;
	}

	private void endDocument(BufferedWriter out) throws IOException {
		out.write("</" + BaseNodeXMLGen.UANODESET + ">");
		out.flush();
	}

	private void startDocument(BufferedWriter out) throws IOException {
		out.write("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
		out.newLine();

		String time = dateNow();
		out.write("<" + BaseNodeXMLGen.UANODESET + " " + BaseNodeXMLGen.XMLNS_XSD + " " + BaseNodeXMLGen.XMLNS_XSI + " "
				+ BaseNodeXMLGen.LAST_MODIFIED + "=\"" + time + "\" " + BaseNodeXMLGen.XMLNS_NODESET + ">");
		out.newLine();
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
				HashMap<String, String> details = Studio_ResourceManager.INFORMATIONMODEL_DETAILS.get(s);
				if (details == null) {
					details = new HashMap<>();
					if (NamespaceTable.OPCUA_NAMESPACE.equalsIgnoreCase(s)) {
						details.put("1.04.6", "2020-04-14T00:00:00Z");
					} else {
						details.put("1.00", "" + dateNow());
					}
				}
				List<Entry<String, String>> detail2use = new ArrayList<>(details.entrySet());
				detail2use.sort(new Comparator<Entry<String, String>>() {

					@Override
					public int compare(Entry<String, String> o1, Entry<String, String> o2) {
						return o1.getKey().compareTo(o2.getKey()) * (-1);
					}
				});

				out.write("\t\t\t<" + BaseNodeXMLGen.REQUIRED_MODEL + " " + BaseNodeXMLGen.MODEL_URI + "=\"" + s + "\" "
						+ BaseNodeXMLGen.VERSION + "=\"" + detail2use.get(0).getKey() + "\" "
						+ BaseNodeXMLGen.PUBLICATION_NDATE + "=\"" + detail2use.get(0).getValue() + "\" />");
				out.newLine();
			}
			out.write("\t\t</" + BaseNodeXMLGen.MODEL + ">");
			out.newLine();
		}
		out.write("\t</" + BaseNodeXMLGen.MODELS + ">");
		out.newLine();
	}

	private void writeNodes(BufferedWriter out, List<Node> nodes, NamespaceTable namespaces,
			IServerTypeModel typeModel) {
		
		for (Node node : nodes) {
			BaseXMLGen element = XMLElementFactory.factor(node);
			try {
				element.writeXML(out, this.serverInstance, this.nsTable, namespaces, typeModel);
			} catch (IOException e) {
				Logger.getLogger(XMLModelV2Factory.class.getName()).log(Level.SEVERE, node.toString(), e);
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
	}
}
