package com.bichler.astudio.opcua.addressspace.xml.importer;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.output.support.AbstractXMLOutputProcessor;
import org.jdom2.output.support.FormatStack;
import org.jdom2.output.support.XMLOutputProcessor;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.AddReferencesItem;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;

import com.bichler.astudio.opcua.components.addressspace.DefinitionBean;
import com.bichler.astudio.opcua.components.addressspace.DefinitionFieldBean;

/**
 * 
 * @author andreas.schartlmueller@sequality.at
 */
public class NodeSetBean {
	private static final XMLOutputProcessor noNamespaces = new AbstractXMLOutputProcessor() {

		@Override
		protected void printNamespace(final Writer out, final FormatStack fstack, final Namespace ns)
				throws IOException {
			// do nothing with printing Namespaces....
			final String prefix = ns.getPrefix();
			final String uri = ns.getURI();
			if (!prefix.equals("")) {
				write(out, " xmlns");

				write(out, ":");
				write(out, prefix);

				write(out, "=\"");
				attributeEscapedEntitiesFilter(out, fstack, uri);
				write(out, "\"");
			}
		}

	};

	private NodesetImporter importer = null;
	private NodeClass nodeClass = null;
	private NodeId parentNodeId = null;
	private NodeId nodeId = null;
	private String browseName = null;
	private String displayName = null;
	private NodeId referenceType = null;
	private NodeId typeDefinition = null;
	private String description = null;
	private boolean symmetric = false;
	private NodeId hasModellingRule = null;

	private ArrayList<String> extensionList = null;
	private DefinitionBean definitionBean = null;

	private String symbolicName = null;

	private NodeSetBean() {
	}

	public NodeSetBean(NodeClass nodeClass, NodesetImporter importer) {
		this.nodeClass = nodeClass;
		this.importer = importer;
	}

	public NodeClass getNodeClass() {
		return nodeClass;
	}

	public ArrayList<String> getExtensions() {
		return getExtensionList();
	}

	public void setExtensions(Element extensions) {
		List<Element> children = extensions.getChildren();
		for (Element extension : children) {
			if (!"Extension".equals(extension.getName())) {
				continue;
			}
			Format format = Format.getPrettyFormat();
			format.setTextMode(Format.TextMode.NORMALIZE);
			format.setEncoding("UTF-8");

			XMLOutputter xout = new XMLOutputter(noNamespaces);
			xout.setFormat(format);

			String extensionValue = xout.outputString(extension.getContent());
			getExtensionList().add(extensionValue);
		}
	}

	private ArrayList<String> getExtensionList() {
		if (this.extensionList == null) {
			this.extensionList = new ArrayList<>();
		}
		return this.extensionList;
	}

	public NodeId getParentNodeId() {
		return parentNodeId;
	}

	public void setParentNodeId(NodeId parameterNodeId) {
		this.parentNodeId = parameterNodeId;
	}

	public NodeId getNodeId() {
		return nodeId;
	}

	public void setNodeId(NodeId nodeId) {
		this.nodeId = nodeId;
	}

	public String getBrowseName() {
		return browseName;
	}

	public void setBrowseName(String browseName) {
		this.browseName = browseName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public NodeId getReferenceType() {
		return referenceType;
	}

	public void setReferenceType(NodeId referenceType) {
		this.referenceType = referenceType;
	}

	public NodeId getTypeDefinition() {
		return typeDefinition;
	}

	public void setTypeDefinition(NodeId typeDefinition) {
		this.typeDefinition = typeDefinition;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isSymmetric() {
		return symmetric;
	}

	public void setSymmetric(boolean symetric) {
		this.symmetric = symetric;
	}

	public void setHasModellingRule(NodeId hasModellingRule) {
		this.hasModellingRule = hasModellingRule;
	}

	public NodeId getHasModellingRule() {
		return hasModellingRule;
	}

	public AddReferencesItem createTypeDescription(NodeId typeDescription, String direction,
			NamespaceTable namespaceTable) {
		AddReferencesItem item = new AddReferencesItem();
		if (direction == null)
			item.setIsForward(true);
		else
			item.setIsForward(Boolean.parseBoolean(direction));
		item.setReferenceTypeId(Identifiers.HasDescription);
		item.setSourceNodeId(this.nodeId);
		item.setTargetNodeClass(NodeClass.Variable);
		item.setTargetNodeId(new ExpandedNodeId(namespaceTable.getUri(typeDescription.getNamespaceIndex()),
				typeDescription.getValue(), namespaceTable));
		return item;
	}

	public AddReferencesItem createTypeEncoding(NodeId typeDescription, String direction,
			NamespaceTable namespaceTable) {
		AddReferencesItem item = new AddReferencesItem();
		if (direction == null)
			item.setIsForward(true);
		else
			item.setIsForward(Boolean.parseBoolean(direction));
		item.setReferenceTypeId(Identifiers.HasEncoding);
		item.setSourceNodeId(this.nodeId);
		item.setTargetNodeClass(NodeClass.Variable);
		item.setTargetNodeId(new ExpandedNodeId(namespaceTable.getUri(typeDescription.getNamespaceIndex()),
				typeDescription.getValue(), namespaceTable));
		return item;
	}

	public AddReferencesItem createAdditionalReference(NodeId targetNodeId, String referenceType, String direction,
			NamespaceTable namespaceTable, NodesetImporter importer) {

		AddReferencesItem item = new AddReferencesItem();
		if (direction == null)
			item.setIsForward(true);
		else
			item.setIsForward(Boolean.parseBoolean(direction));
		try {
			NodeId refid = (NodeId) Identifiers.class.getDeclaredField(referenceType).get(null);
			item.setReferenceTypeId(refid);
		} catch (NoSuchFieldException nsfe) {
			try {
				// do we have other references?
				item.setReferenceTypeId(importer.createNodeIdByXmlValue(referenceType));
			} catch (Exception ex) {
				Logger.getLogger(getClass().getName()).log(Level.INFO,
						"No intern identifier found for " + referenceType);
				NodeId refNodeId = null;
				if ((refNodeId = importer.getSpezificDataType(referenceType)) == null) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, referenceType + "  not found", ex);
				} else {
					try {
						if (!NodeId.isNull(refNodeId)) {
							item.setReferenceTypeId(importer.createNodeIdByXmlValue(refNodeId.toString()));
						} else {
							Logger.getLogger(getClass().getName()).log(Level.SEVERE, referenceType + "  not found", ex);
						}
					} catch (Exception ex2) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, referenceType + "  not found", ex2);
					}
				}
			}
		} catch (Exception ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, referenceType + "  not found", ex);
		}
		item.setSourceNodeId(this.nodeId);
		// item.setTargetNodeClass(NodeClass.Object);

		// NamespaceTable nsTable =
		// ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		item.setTargetNodeId(new ExpandedNodeId(namespaceTable.getUri(targetNodeId.getNamespaceIndex()),
				targetNodeId.getValue(), namespaceTable));

		return item;
	}

	public DefinitionBean getDefinitionBean() {
		return this.definitionBean;
	}

	public void setDefinition(Element definition, NodesetImporter importer) {
		this.definitionBean = parseDefinitionField(definition, importer);
	}

	public String getSymbolicName() {
		return this.symbolicName;
	}

	public void setSymbolicName(String symbolicName) {
		this.symbolicName = symbolicName;
	}

	private DefinitionBean parseDefinitionField(Element definition, NodesetImporter importer) {
		String definitionName = definition.getAttributeValue("Name");

		DefinitionBean definitionBean = new DefinitionBean();
		definitionBean.setDefinitionName(definitionName);

		List<Element> children = definition.getChildren();
		for (Element field : children) {
			if (!"Field".equals(field.getName())) {
				continue;
			}
			DefinitionFieldBean fieldBean = new DefinitionFieldBean();

			List<Attribute> attributes = field.getAttributes();
			for (Attribute attribute : attributes) {
				String name = attribute.getName();

				switch (name) {
				case "Name":
					fieldBean.setName(attribute.getValue());
					break;
				case "Value":
					fieldBean.setValue(Integer.parseInt(attribute.getValue()));
					break;
				case "DataType":
					try {
						fieldBean.setDatatype(NodeId.parseNodeId(attribute.getValue()));
					} catch (IllegalArgumentException e) {
						// map alias to datatype id
						NodeId aliasId = importer.getSpezificDataType(attribute.getValue());
						fieldBean.setDatatype(aliasId);
					}
					break;
				case "ValueRank":
					fieldBean.setValueRank(Integer.parseInt(attribute.getValue()));
					break;
				default:
					break;
				}
			}
			definitionBean.addField(fieldBean);
		}
		return definitionBean;
	}
}
