package com.bichler.astudio.opcua.datadictionary.base.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Base64;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jdom.Namespace;
import org.opcfoundation.ua.builtintypes.ByteString;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.ReferenceNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.bichler.astudio.opcua.components.addressspace.DefinitionBean;
import com.bichler.astudio.opcua.components.addressspace.DefinitionFieldBean;

import opc.sdk.core.enums.BuiltinType;
import opc.sdk.core.node.DataTypeNode;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.VariableNode;

public abstract class AbstractDataDictionaryHelper {

	enum DefinitionType {
		Structure, Enumeration, Simple;
	}

	private static final String NAMESPACE_OPC = "opc";
	private static final String NAMESPACE_TNS = "tns";
	private static final String NAMESPACE_UA = "ua";

	private static final Namespace NS = Namespace.getNamespace("http://opcfoundation.org/BinarySchema");
	private static final Namespace NS_OPC = Namespace.getNamespace("opc", "http://opcfoundation.org/BinarySchema/");
	private static final Namespace NS_XSI = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
	private static final Namespace NS_UA = Namespace.getNamespace("ua", "http://opcfoundation.org/UA/");

	private static final String ELEMENT_TYPEDICTIONARY = "TypeDictionary";
	private static final String ELEMENT_STRUCTUREDTYPE = "StructuredType";
	private static final String ELEMENT_ENUMERATEDTYPE = "EnumeratedType";
	private static final String ELEMENT_DOCUMENTATION = "Documentation";
	private static final String ELEMENT_ENUMERATEDVALUE = "EnumeratedValue";
	private static final String ELEMENT_FIELD = "Field";

	private static final String ATTRIBUTE_BASETYPE = "BaseType";
	private static final String ATTRIBUTE_LENGTHFIELD = "LengthField";
	private static final String ATTRIBUTE_LENGTHINBITS = "LengthInBits";
	private static final String ATTRIBUTE_NAME = "Name";
	private static final String ATTRIBUTE_TYPENAME = "TypeName";
	private static final String ATTRIBUTE_VALUE = "Value";

	private static final String NO_OF = "NoOf";
	private static final String opcInt32 = "opc:Int32";

	public AbstractDataDictionaryHelper() {

	}

	public Document createEmptyDataDictionary(String namespaceUri) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;

		try {
			builder = dbf.newDocumentBuilder();

			Document doc = builder.newDocument();
			doc.setXmlStandalone(true);

			Element root = createRootDataDictionary(doc, namespaceUri);
			String textContent = "This dictionary defines the types " + namespaceUri
					+ " used by the OPC Binary type description";
			Element documentation = createDocumentation(doc, textContent);
			root.appendChild(documentation);

			return doc;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ByteString createNodeFromDefinition(DataTypeNode node, ByteString value, DefinitionBean definition)
			throws SAXException, IOException, ParserConfigurationException {

		boolean isStructure = isTypeOf(node.getNodeId(), Identifiers.Structure); 
		boolean isEnumeration = isTypeOf(node.getNodeId(), Identifiers.Enumeration); 

		DefinitionType type = DefinitionType.Simple;
		if (isStructure) {
			type = DefinitionType.Structure;
		} else if (isEnumeration) {
			type = DefinitionType.Enumeration;
		}

		// create document
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		DocumentBuilder builder = factory.newDocumentBuilder();

		byte[] decoded = Base64.getDecoder().decode(value.getValue());
		String decodedString = new String(decoded);

		StringBuilder xmlStringBuilder = new StringBuilder();
		xmlStringBuilder.append(decodedString);
		ByteArrayInputStream input = new ByteArrayInputStream(xmlStringBuilder.toString().getBytes("UTF-8"));

		org.w3c.dom.Document doc = builder.parse(input);
		// find existing definition node
		org.w3c.dom.Node found = findNodeFromDocument(doc, definition.getDefinitionName());
		if (found != null) {
			// remove node
			doc.getFirstChild().removeChild(found);
//			doc.removeChild(found);
		}

		// create definition node <StructuredType>
		Element definitionNode = null;
		DefinitionFieldBean[] fields = null;
		LocalizedText description = null;
		switch (type) {
		case Structure:
			definitionNode = doc.createElementNS(NS_OPC.getURI(), ELEMENT_STRUCTUREDTYPE);
			definitionNode.setPrefix(NS_OPC.getPrefix());
			definitionNode.setAttribute(ATTRIBUTE_NAME, definition.getDefinitionName());

			// supertype
			ExpandedNodeId superType = node.findTarget(Identifiers.HasSubtype, true);
			Node superTypeNode = getNode(superType); // OpcUtils.getNode(this.opcServer, superType);

			definitionNode.setAttribute(ATTRIBUTE_BASETYPE, constructTypeDefinition(superTypeNode));
			// Description <opc:Documentation> </opc:Documentation>
			description = node.getDescription();
			if (description != null && description.getText() != null && !description.getText().isEmpty()) {
				Element documentation = createDocumentation(doc, description.getText());
				definitionNode.appendChild(documentation);
			}

			// TODO: Switchfield
			// add fields
			fields = definition.getFields();
			for (DefinitionFieldBean field : fields) {
				Element fieldNode = doc.createElementNS(NS_OPC.getURI(), ELEMENT_FIELD);
				fieldNode.setPrefix(NS_OPC.getPrefix());

				NodeId datatypeId = field.getDatatype();
				Node datatypeNode = getNode(datatypeId); // OpcUtils.getNode(this.opcServer, datatypeId);

				fieldNode.setAttribute(ATTRIBUTE_NAME, field.getName());
				fieldNode.setAttribute(ATTRIBUTE_TYPENAME, constructTypeDefinition(datatypeNode));
				// append field
				definitionNode.appendChild(fieldNode);
				// check for lengthfield
				Integer valueRank = field.getValueRank();
				if (valueRank != null && valueRank >= 0) {
					// append lengthfield
					fieldNode.setAttribute(ATTRIBUTE_LENGTHFIELD, NO_OF + field.getName());
					Element lengthFieldNode = doc.createElementNS(NS_OPC.getURI(), ELEMENT_FIELD);
					lengthFieldNode.setPrefix(NS_OPC.getPrefix());
					lengthFieldNode.setAttribute(ATTRIBUTE_NAME, NO_OF + field.getName());
					lengthFieldNode.setAttribute(ATTRIBUTE_TYPENAME, opcInt32);

					definitionNode.appendChild(lengthFieldNode);
				}
			}
			break;
		case Enumeration:
			definitionNode = doc.createElementNS(NS_OPC.getURI(), ELEMENT_ENUMERATEDTYPE);
			definitionNode.setPrefix(NS_OPC.getPrefix());
			definitionNode.setAttribute(ATTRIBUTE_NAME, definition.getDefinitionName());
			definitionNode.setAttribute(ATTRIBUTE_LENGTHINBITS, "32");

			// Description <opc:Documentation> </opc:Documentation>
			description = node.getDescription();
			if (description != null && description.getText() != null && !description.getText().isEmpty()) {
				Element documentation = createDocumentation(doc, description.getText());
				definitionNode.appendChild(documentation);
			}

			fields = definition.getFields();
			for (DefinitionFieldBean field : fields) {
				Element fieldNode = doc.createElementNS(NS_OPC.getURI(), ELEMENT_ENUMERATEDVALUE);
				fieldNode.setPrefix(NS_OPC.getPrefix());
				// enumeratedvalue attributes
				fieldNode.setAttribute(ATTRIBUTE_NAME, field.getName());
				fieldNode.setAttribute(ATTRIBUTE_VALUE, field.getValue().toString());
				// append field
				definitionNode.appendChild(fieldNode);
			}

			break;
		case Simple:
		default:
			break;
		}

		// append definition field for child
		doc.getFirstChild().appendChild(definitionNode);
		try {
			String out = prettyPrint(doc);
			byte[] encoded = Base64.getEncoder().encode(out.getBytes());
			return ByteString.valueOf(encoded);
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		}
		return ByteString.EMPTY;
	}

	public ByteString encodeBase64(String uaStructure) {
		byte[] encoded = Base64.getEncoder().encode(uaStructure.getBytes());
		ByteString encodedValue = ByteString.valueOf(encoded);
		return encodedValue;
	}

	public ByteString findByteStringFromNamespaceDataTypeId(NodeId datatypeId) {
		ByteString byteString = ByteString.EMPTY;
		Node datatype = getNode(datatypeId); // OpcUtils.getNode(this.opcServer, datatypeId);
		if (datatype == null) {
			return byteString;
		}

		for (ReferenceNode reference : datatype.getReferences()) {
			NodeId referenceId = reference.getReferenceTypeId();
			boolean isEncoding = isTypeOf(referenceId, Identifiers.HasEncoding); // this.opcServer.getTypeTable().isTypeOf(referenceId,
																					// Identifiers.HasEncoding);
			if (!isEncoding) {
				continue;
			}

			Node defaultBinary = getNode(reference.getTargetId()); // OpcUtils.getNode(this.opcServer,
																	// reference.getTargetId());
			if (defaultBinary.getBrowseName().getName().compareTo("Default Binary") != 0) {
				continue;
			}

			Node description = null;
			for (ReferenceNode descRef : defaultBinary.getReferences()) {
				NodeId descRefId = descRef.getReferenceTypeId();
				boolean isDescription = isTypeOf(descRefId, Identifiers.HasDescription);
//						this.opcServer.getTypeTable().isTypeOf(descRefId, Identifiers.HasDescription);
				if (!isDescription) {
					continue;
				}
				description = getNode(descRef.getTargetId()); // OpcUtils.getNode(this.opcServer,
																// descRef.getTargetId());
				if (description != null) {
					break;
				}
			}

			if (description == null) {
				continue;
			}
			for (ReferenceNode dictRef : description.getReferences()) {
				if (!dictRef.getIsInverse()) {
					continue;
				}

				boolean isComponent = isTypeOf(dictRef.getReferenceTypeId(), Identifiers.HasComponent);
//				this.opcServer.getTypeTable().isTypeOf(dictRef.getReferenceTypeId(), Identifiers.HasComponent);
				if (!isComponent) {
					continue;
				}

				Node dataDict = getNode(dictRef.getTargetId()); // OpcUtils.getNode(this.opcServer,
																// dictRef.getTargetId());
				if (dataDict == null) {
					continue;
				}
				return (ByteString) ((VariableNode) dataDict).getValue().getValue();
			}
		}

		return byteString;
	}

	public org.w3c.dom.Node findNodeFromByteString(ByteString value, String nodeName)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		DocumentBuilder builder = factory.newDocumentBuilder();

		byte[] decoded = Base64.getDecoder().decode(value.getValue());
		String decodedString = new String(decoded);
		// -----------------------------------------------------
		StringBuilder xmlStringBuilder = new StringBuilder();
		xmlStringBuilder.append(decodedString);
		ByteArrayInputStream input = new ByteArrayInputStream(xmlStringBuilder.toString().getBytes("UTF-8"));

		org.w3c.dom.Document doc = builder.parse(input);
		org.w3c.dom.Node datatypeNode = findDataTypeNodeList(doc, nodeName);
		return datatypeNode;
	}

	public org.w3c.dom.Node findNodeFromDocument(Document document, String nodename) {
		org.w3c.dom.Node datatypeNode = findDataTypeNodeList(document, nodename);
		return datatypeNode;
	}

	public String nodeToString(org.w3c.dom.Node node) throws TransformerException {
		DOMSource source = new DOMSource();
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

		source.setNode(node);
		transformer.transform(source, result);

		return writer.toString();
	}

	public String prettyPrint(Document document) {
		try {
			Transformer tf = TransformerFactory.newInstance().newTransformer();
			tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			tf.setOutputProperty(OutputKeys.INDENT, "yes");
			tf.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			Writer out = new StringWriter();
			tf.transform(new DOMSource(document), new StreamResult(out));
			return out.toString();
		} catch (TransformerFactoryConfigurationError | TransformerException e) {
			e.printStackTrace();
		}

		return "";
	}

	org.w3c.dom.Node findDataTypeNodeList(org.w3c.dom.Document doc, String name) {
		NodeList root = doc.getChildNodes();
		org.w3c.dom.Node typeDictionary = null;
		for (int i = 0; i < root.getLength(); i++) {
			org.w3c.dom.Node node = root.item(i);

			if (node.getNodeName().contains(ELEMENT_TYPEDICTIONARY)) {
				typeDictionary = node;
				break;
			}
		}

		if (typeDictionary == null) {
			return null;
		}

		NodeList children = typeDictionary.getChildNodes();
//		org.w3c.dom.Node found = null;
		for (int i = 0; i < children.getLength(); i++) {
			org.w3c.dom.Node child = children.item(i);
			NamedNodeMap attributes = child.getAttributes();
			// check for attributes
			if (attributes == null) {
				continue;
			}

			org.w3c.dom.Node attributeName = attributes.getNamedItem(ATTRIBUTE_NAME);
			// check for attribute name
			if (attributeName == null) {
				continue;
			}

			if (attributeName.getNodeValue() != null && attributeName.getNodeValue().contains(name)) {
				return child;
			}
		}

		return null;
	}

	private Element createDocumentation(Document doc, String textContent) {
		Element element = doc.createElementNS(NS_OPC.getURI(), ELEMENT_DOCUMENTATION);
		element.setPrefix(NS_OPC.getPrefix());
		element.setTextContent(textContent);
		return element;
	}

	private Element createRootDataDictionary(Document doc, String namespaceUri) {
		Namespace nsTns = Namespace.getNamespace(NAMESPACE_TNS, namespaceUri);

		Element element = doc.createElementNS(NS_OPC.getURI(), ELEMENT_TYPEDICTIONARY);

		element.setAttribute("xmlns", NS.getURI());
		element.setPrefix(NS_OPC.getPrefix());

		element.setAttribute("xmlns:" + NS_XSI.getPrefix(), NS_XSI.getURI());
		element.setAttribute("xmlns:" + NS_UA.getPrefix(), NS_UA.getURI());
		element.setAttribute("xmlns:" + nsTns.getPrefix(), nsTns.getURI());
		element.setAttribute("DefaultByteOrder", "LittleEndian");
		element.setAttribute("TargetNamespace", namespaceUri);

		doc.appendChild(element);

		return element;
	}

	private String constructTypeDefinition(Node node) {
		// Null node is variant
		if (node == null) {
			return "ua:Variant";
		}

		NodeId nodeId = node.getNodeId();

		if (Identifiers.Structure.equals(nodeId)) {
			return "ua:ExtensionObject";
		}
		// Prefix opc: is for builtin types
		String prefix = NAMESPACE_OPC;
		String typeName = node.getBrowseName().getName();

		if (node.getNodeId().getNamespaceIndex() > 0) {
			// Prefix for target namespace
			prefix = NAMESPACE_TNS;
		} else {
			boolean isStructure = isTypeOf(nodeId, Identifiers.Structure); // this.opcServer.getTypeTable().isTypeOf(nodeId,
																			// Identifiers.Structure);
			if (isStructure) {
				// Prefix for ns=0, structure types
				prefix = NAMESPACE_UA;
			} else {
				BuiltinType builtIn = BuiltinType.getType(nodeId);
				if (builtIn == null) {
					prefix = NAMESPACE_UA;
				} else if (builtIn.getValue() > 15) {
					prefix = NAMESPACE_UA;

					switch (builtIn) {
					case Variant:
						typeName = "Variant";
						break;
					case Number:
					case Integer:
					case UInteger:
						typeName = "Variant";
						break;
					case Enumeration:
						typeName = "Int32";
						break;
					}
				}
			}
		}

		return prefix + ":" + typeName;
	}

	protected abstract Node getNode(ExpandedNodeId nodeId);

	protected abstract Node getNode(NodeId nodeId);

	protected abstract boolean isTypeOf(NodeId type, NodeId typeOf);
}
