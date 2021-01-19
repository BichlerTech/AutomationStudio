package com.bichler.astudio.opcua.opcmodeler.exporter;

import java.util.List;

import opc.sdk.core.enums.BuiltinType;
import opc.sdk.core.informationmodel.xml.ImportNamespaces;
import opc.sdk.core.informationmodel.xml.NodeType;
import opc.sdk.core.informationmodel.xml.NodesCategoryTags;
import opc.sdk.core.types.TypeTable;

import org.opcfoundation.ua.builtintypes.BuiltinsMap;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.IdType;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.utils.BijectionMap;
import org.opcfoundation.ua.utils.CryptoUtil;
import org.opcfoundation.ua.utils.MultiDimensionArrayUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Util to write DOM Elements.
 * 
 * @author Thomas Zöchbauer
 */
public class WriteUtil {
	private static final String LOCALE = "Locale";
	private static final String INIL = "i:nil";
	private static final String VALUE = "Value";
	private static final String XMLNS = "xmlns";

	private WriteUtil() {
	}

	/**
	 * Creates the Header of the XML-Export-Document. <NodeSet>
	 * 
	 * @param Document Document to create the Element.
	 * @return DOM Header Element.
	 */
	protected static Element writeBeginn(Document document) {
		Element nodeSet = document.createElementNS(ImportNamespaces.TYPES.toString(), NodesCategoryTags.NodeSet.name());
		nodeSet.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:i", "http://www.w3.org/2001/XMLSchema-instance");
		return nodeSet;
	}

	/**
	 * Creates the Namespace Uri Section of the XML-Exported-Document. <Namespace>
	 * 
	 * @param Document   Document to create the Element.
	 * @param Namespaces Namespaces to append in the DOM-Element.
	 * @return DOM NamespaceUri Element.
	 */
	protected static Element writeNamespaceUris(Document document, String[] namespaces) {
		Element namespaceUris = document.createElement(NodesCategoryTags.NamespaceUris.name());
		for (String namespace : namespaces) {
			Element string = createStringElement(document);
			string.setTextContent(namespace);
			namespaceUris.appendChild(string);
		}
		return namespaceUris;
	}

	/**
	 * Creates the Namespace Uri Section of the XML-Exported-Document. <Namespace>
	 * 
	 * @param Document   Document to create the Element.
	 * @param Namespaces Namespaces to append in the DOM-Element.
	 * @return DOM NamespaceUri Element.
	 */
	protected static void appendNamespaceUri(Document document, String namespace) {
		Node namespaceUris = document.getElementsByTagName(NodesCategoryTags.NamespaceUris.name()).item(0);
		if (namespaceUris != null) {
			Element string = createStringElement(document);
			string.setTextContent(namespace);
			namespaceUris.appendChild(string);
		}
		return;
	}

	/**
	 * Create the Server Uri Section of the XML-Exported-Document. <ServerUri>
	 * 
	 * @param Document    Document to create the Element.
	 * @param ServersUris ServerUris to append in dhe DOM-Element
	 * @return DOM ServerUri Element.
	 */
	protected static Element writeServerUris(Document document, String[] serversUris) {
		Element serverUris = document.createElement(NodesCategoryTags.ServerUris.name());
		for (String uri : serversUris) {
			Element string = createStringElement(document);
			string.setTextContent(uri);
			serverUris.appendChild(string);
		}
		return serverUris;
	}

	/**
	 * Create a Nodes Element, which groups all Node Tags. <Nodes>
	 * 
	 * @param Document Document to create the Element.
	 * @return DOM Nodes Element.
	 */
	protected static Element writeNodes(Document document) {
		return document.createElement(NodesCategoryTags.Nodes.name());
	}

	/**
	 * Create a Node Element, which groups all Node Attribute Tags. <Node>
	 * 
	 * @param Document Document to create the Element.
	 * @param NodeType Type of the Node (Nodeclass).
	 * @return DOM Node Element.
	 */
	protected static Element writeNodeHeader(Document document, NodeType nodeType) {
		Element node = document.createElement(NodesCategoryTags.Node.name());
		node.setAttribute("i:type", nodeType.name());
		return node;
	}

	/**
	 * Create a NodeId Element, which represents a NodeId Attribute from a Node.
	 * <NodeId>
	 * 
	 * @param Document    Document to create the Element.
	 * @param NodeIdValue NodeId Attribute.
	 * @return DOM NodeId Element.
	 */
	protected static Element writeNodeId(Document document, String nodeIdValue) {
		Element nodeId = document.createElement(NodesCategoryTags.NodeId.name());
		Element identifier = createIdentifiersElement(document, nodeIdValue);
		nodeId.appendChild(identifier);
		return nodeId;
	}

	/**
	 * Create a NodeClass Element, which represents a NodeClass Attribute from a
	 * Node. <NodeClass>
	 * 
	 * @param Document       Document to create the Element.
	 * @param NodeClassValue NodeClass of a Node used as TextContent.
	 * @return DOM NodeClass Element.
	 */
	protected static Element writeNodeClass(Document document, NodeClass nodeClassValue) {
		Element nodeClass = document.createElement(NodesCategoryTags.NodeClass.name());
		nodeClass.setTextContent(nodeClassValue.name() + "_" + nodeClassValue.getValue());
		return nodeClass;
	}

	/**
	 * Create a BrowseName Element, which represents a BrowseName Attribute from a
	 * Node. <BrowseName>
	 * 
	 * @param Document        Document to create the Element.
	 * @param BrowsenameValue QualifiedName of a Browsename used as TextContent.
	 * @return DOM BrowseName Element.
	 */
	protected static Element writeBrowsename(Document document, QualifiedName browsenameValue) {
		Element browsename = document
				.createElement(NodesCategoryTags.BrowseName.name().replace("<", "&lt;").replace(">", "&gt;"));
		String[] nodeIdArray = new String[2];
		nodeIdArray[0] = Integer.toString(browsenameValue.getNamespaceIndex());
		nodeIdArray[1] = browsenameValue.getName();
		Element namespaceIndex = document.createElement(NodesCategoryTags.NamespaceIndex.name());
		namespaceIndex.setTextContent(nodeIdArray[0]);
		browsename.appendChild(namespaceIndex);
		Element identifier = document.createElement(NodesCategoryTags.Name.name());
		identifier.setTextContent(nodeIdArray[1]);
		browsename.appendChild(identifier);
		return browsename;
	}

	/**
	 * Create a DisplayName Element, which represents a DisplayName Attribute from a
	 * Node. <DisplayName>
	 * 
	 * @param Document         Document to create the Element.
	 * @param DisplaynameValue LocalizedText of a Displayname used as TextContent.
	 * @return DOM DisplayName Element.
	 */
	protected static Element writeDisplayname(Document document, LocalizedText displaynameValue) {
		Element displayname = document
				.createElement(NodesCategoryTags.DisplayName.name().replace("<", "&lt;").replace(">", "&gt;"));
		Element locale = document.createElement(LOCALE);
		Element text = document.createElement(NodesCategoryTags.Text.name());
		if (displaynameValue.getLocaleId() == null) {
			locale.setAttribute(INIL, "true");
		} else {
			locale.setTextContent(displaynameValue.getLocaleId());
		}
		displayname.appendChild(locale);
		text.setTextContent(displaynameValue.getText());
		displayname.appendChild(text);
		return displayname;
	}

	/**
	 * Create a Description Element, which represents a Description Attribute from a
	 * Node. <Description>
	 * 
	 * @param Document         Document to create the Element.
	 * @param DescriptionValue LocalizedText of a Description used as TextContent.
	 * @return DOM Description Element.
	 */
	protected static Element writeDescription(Document document, LocalizedText descriptionValue) {
		Element description = document
				.createElement(NodesCategoryTags.Description.name().replace("<", "&lt;").replace(">", "&gt;"));
		Element locale = document.createElement(LOCALE);
		Element text = document.createElement(NodesCategoryTags.Text.name());
		if (descriptionValue.getLocaleId() == null) {
			locale.setAttribute(INIL, "true");
		} else {
			locale.setTextContent(descriptionValue.getLocaleId());
		}
		description.appendChild(locale);
		text.setTextContent(descriptionValue.getText());
		description.appendChild(text);
		return description;
	}

	/**
	 * Create a WriteMask Element, which represents a WriteMask Attribute from a
	 * Node. <WriteMask>
	 * 
	 * @param Document       Document to create the Element.
	 * @param WriteMaskValue Value of a WriteMask used as TextContent.
	 * @return DOM WriteMask Element.
	 */
	protected static Element writeWriteMask(Document document, String writeMaskValue) {
		Element writeMask = document.createElement(NodesCategoryTags.WriteMask.name());
		writeMask.setTextContent(writeMaskValue);
		return writeMask;
	}

	/**
	 * Create an UserWriteMask Element, which represents an UserWriteMask Attribute
	 * from a Node. <UserWriteMask>
	 * 
	 * @param Document           Document to create the Element.
	 * @param UserWriteMaskValue Value of a UserWriteMask used as TextContent.
	 * @return DOM UserWriteMask Element.
	 */
	protected static Element writeUserWriteMask(Document document, String userWriteMaskValue) {
		Element userWriteMask = document.createElement(NodesCategoryTags.UserWriteMask.name());
		userWriteMask.setTextContent(userWriteMaskValue);
		return userWriteMask;
	}

	/**
	 * Create a Reference Element, which groups all ReferenceNodes from a Node.
	 * <References>
	 * 
	 * @param Document Document to create the Element.
	 * @return DOM References Element.
	 */
	protected static Element writeReferences(Document document) {
		return document.createElement(NodesCategoryTags.References.name());
	}

	/**
	 * Create a ReferenceNode Element, which represents a ReferenceNode from a Node.
	 * <ReferenceNode>
	 * 
	 * @param Document             Document to create the Element.
	 * @param ReferenceTypeIdValue Value of a ReferenceTypeId used as TextContent.
	 * @param IsInverseValue       Value of an InverseName used as TextContent.
	 * @param TargetIdValue        Value of a TargetId used as TextContent.
	 * @return DOM ReferenceNode Element.
	 */
	protected static Element writeReferenceNode(Document document, String referenceTypeIdValue, String isInverseValue,
			String targetIdValue) {
		Element referenceNode = document.createElement(NodesCategoryTags.ReferenceNode.name());
		Element referenceTypeId = document.createElement(NodesCategoryTags.ReferenceTypeId.name());
		Element identifierTypeId = createIdentifiersElement(document, referenceTypeIdValue);
		referenceTypeId.appendChild(identifierTypeId);
		referenceNode.appendChild(referenceTypeId);
		Element isInverse = document.createElement(NodesCategoryTags.IsInverse.name());
		isInverse.setTextContent(isInverseValue);
		referenceNode.appendChild(isInverse);
		Element targetId = document.createElement(NodesCategoryTags.TargetId.name());
		Element identifierTargetId = createIdentifiersElement(document, targetIdValue);
		targetId.appendChild(identifierTargetId);
		referenceNode.appendChild(targetId);
		return referenceNode;
	}

	/**
	 * Create an EventNotifier Element, which represents an EventNotifier Attribute
	 * from a Node. <EventNotifier>
	 * 
	 * @param Document           Document to create the Element.
	 * @param EventNotifierValue Value of an EventNotifier used as TextContent.
	 * @return DOM EventNotifier Element.
	 */
	protected static Element writeEventNotifier(Document document, String eventNotifierValue) {
		Element eventNotifier = document.createElement(NodesCategoryTags.EventNotifier.name());
		eventNotifier.setTextContent(eventNotifierValue);
		return eventNotifier;
	}

	/**
	 * Create an IsAbstract Element, which represents an IsAbstract Attribute from a
	 * Node. <IsAbstract>
	 * 
	 * @param Document        Document to create the Element.
	 * @param IsAbstractValue Value of an IsAbstract used as TextContent.
	 * @return DOM IsAbstract Element.
	 */
	protected static Element writeIsAbstract(Document document, String isAbstractValue) {
		Element isAbstract = document.createElement(NodesCategoryTags.IsAbstract.name());
		isAbstract.setTextContent(isAbstractValue);
		return isAbstract;
	}

	/**
	 * Appends a Value Element, which represents a Value from a Node.
	 * 
	 * <Value> <Value>
	 * <Null xmlns= "http://opcfoundation.org/UA/2008/02/Types.xsd"/> </Value>
	 * </Value>
	 * 
	 * @param Document   Document to create the Element.
	 * @param DataType   Value of a DataType used as TextContent.
	 * @param ValueValue Value of a Node..
	 * @return
	 */
	protected static Element writeValue(Document document, String dataType, TypeTable typeTree, Variant valueValue) {
		Element value = document.createElement(VALUE);
		// check null value
		boolean valueNotNull = valueValue != null && !valueValue.isEmpty();
		// no value
		if (!valueNotNull) {
			Element subValue = document.createElement(VALUE);
			value.appendChild(subValue);
			Element nullValue = document.createElementNS(ImportNamespaces.TYPES.toString(), "Null");
			nullValue.setAttribute(XMLNS, ImportNamespaces.TYPES.toString());
			subValue.appendChild(nullValue);
			return value;
		}
		NodeId decoded = NodeId.parseNodeId(dataType);
		BijectionMap<NodeId, Class<?>> map = BuiltinsMap.ID_CLASS_MAP;
		createValueElement(document, typeTree, map, decoded, value, valueValue);
		return value;
	}

	/**
	 * Create a Value Element, which represents a Value Type Attribute from a Node.
	 * <'Type'>
	 * 
	 * @param Document   Document to write.
	 * @param TypeTree   TypeTable from the Server used to declare the Value Type.
	 * @param Map        BijectionMap of Builtin DataTypes.
	 * @param DataType   NodeId of a DataType used as TextContent.
	 * @param Value      Value Element used to append the Value.
	 * @param ValueValue Variant Value of a Node.
	 */
	private static void createValueElement(Document document, TypeTable typeTree, BijectionMap<NodeId, Class<?>> map,
			NodeId dataType, Element value, Variant valueValue) {
		String tagName;
		/** Buildin Types */
		// variant is not null or value in variant is not null
		if (valueValue != null && !Variant.NULL.equals(valueValue)
				&& BuiltinsMap.ID_MAP.containsKey(valueValue.getCompositeClass())) {
			BuiltinType builtin = null;
			Integer builtinType = BuiltinsMap.ID_MAP.get(valueValue.getCompositeClass());
			for (BuiltinType type : BuiltinType.values()) {
				if (builtinType == type.getValue()) {
					builtin = type;
					break;
				}
			}
			if (builtin != null) {
				tagName = builtin.name();
				Element subValue = document.createElement(VALUE);
				if (valueValue.isArray()) {
					int[] dimensions = valueValue.getArrayDimensions();
					Element matrix = document.createElement(NodesCategoryTags.Matrix.name());
					subValue.appendChild(matrix);
					Element arrayDimension = document.createElement(NodesCategoryTags.Dimensions.name());
					matrix.appendChild(arrayDimension);
					Element dimension;
					for (int dim : dimensions) {
						dimension = document.createElement(NodesCategoryTags.Int32.name());
						dimension.setTextContent(Integer.toString(dim));
						arrayDimension.appendChild(dimension);
					}
					Element elements = document.createElement(NodesCategoryTags.Elements.name());
					matrix.appendChild(elements);
					Element dataValue;
					Object[] array = (Object[]) MultiDimensionArrayUtils.muxArray(valueValue.getValue());
					for (Object arrayItem : array) {
						dataValue = document.createElement(tagName);
						if (arrayItem == null || arrayItem.toString().isEmpty()) {
							arrayItem = " ";
						}
						dataValue.setTextContent(arrayItem.toString());
						elements.appendChild(dataValue);
					}
					value.appendChild(subValue);
					return;
				} else {
					Element dataValue = document.createElementNS(ImportNamespaces.TYPES.toString(), tagName);
					dataValue.setTextContent(valueValue.getValue().toString());
					subValue.appendChild(dataValue);
					value.appendChild(subValue);
					return;
				}
			}
		}
		/**
		 * Localized Text is not in the BijectionMap ID_CLASS_MAP
		 */
		Class<?> valueType = map.getRight(dataType);
		String dataTypeString;
		if (Identifiers.LocalizedText.equals(dataType)) {
			if (valueValue == null)
				throw new NullPointerException();
			dataTypeString = valueValue.getCompositeClass().getName();
			String[] valueTypes = dataTypeString.split("\\.");
			// fetch TagName LocalizedText
			tagName = valueTypes[valueTypes.length - 1];
			Element subValue = document.createElement(VALUE);
			Element localizedTextElement = createLocalizedTextElement(document, tagName,
					(LocalizedText) valueValue.getValue());
			subValue.appendChild(localizedTextElement);
			value.appendChild(subValue);
			// exit
			return;
		}
		/**
		 * Not an base value type
		 */
		if (valueType == null) {
			// is a structure (enum value)
			boolean isStructure = typeTree.isEnumeration(dataType);
			// is a complex value (complex value -> extension obj)
			boolean isComplex = typeTree.isStructure(dataType);
			if (isStructure) {
				if (valueValue == null)
					throw new NullPointerException();
				// enums have integer classes
				valueType = valueValue.getCompositeClass();
				dataTypeString = valueType.getName();
				String[] valueTypes = dataTypeString.split("\\.");
				// fetch TagName LocalizedText
				tagName = valueTypes[valueTypes.length - 1];
				Element subValue = document.createElement(VALUE);
				Element dataValue = document.createElementNS(ImportNamespaces.TYPES.toString(), tagName);
				dataValue.setTextContent(valueValue.getValue().toString());
				subValue.appendChild(dataValue);
				value.appendChild(subValue);
				// exit
				return;
			} else if (isComplex) {
				Element subValue = document.createElement(VALUE);
				value.appendChild(subValue);
				Element nullValue = document.createElementNS(ImportNamespaces.TYPES.toString(), "Null");
				subValue.appendChild(nullValue);
				return;
			}
			// get its superType data type
			NodeId superType = typeTree.findSuperType(dataType);
			// create a value element with its super type
			createValueElement(document, typeTree, map, superType, value, valueValue);
			// exit if successfull
			return;
		}
		dataTypeString = valueType.getName();
		String[] valueTypes = dataTypeString.split("\\.");
		tagName = valueTypes[valueTypes.length - 1];
		// change the data type identifier with its string
		Element subValue = document.createElement(VALUE);
		Element dataValue = document.createElementNS(ImportNamespaces.TYPES.toString(), tagName);
		if (valueValue == null)
			throw new NullPointerException();
		dataValue.setTextContent(valueValue.getValue().toString());
		subValue.appendChild(dataValue);
		value.appendChild(subValue);
		// exit if successfull
		return;
	}

	/**
	 * Create a DataType Element, which represents a DataType Attribute from a Node.
	 * <DataType>
	 * 
	 * @param Document      Document to create the Element.
	 * @param DataTypeValue Value of a DataType used as TextContent.
	 * @return DOM DataType Element.
	 */
	protected static Element writeDataType(Document document, String dataTypeValue) {
		Element dataType = document.createElement(NodesCategoryTags.DataType.name());
		Element identifier = createIdentifiersElement(document, dataTypeValue);
		dataType.appendChild(identifier);
		return dataType;
	}

	/**
	 * Create a ValueRank Element, which represents a ValueRank Attribute from a
	 * Node. <ValueRank>
	 * 
	 * @param Document       Document to create the Element.
	 * @param ValueRankValue Value of a ValueRank used as TextContent.
	 * @return DOM ValueRank Element.
	 */
	protected static Element writeValueRank(Document document, String valueRankValue) {
		Element valueRank = document.createElement(NodesCategoryTags.ValueRank.name());
		valueRank.setTextContent(valueRankValue);
		return valueRank;
	}

	/**
	 * Create a ArrayDimension Element, which represents a ArrayDimension Attribute
	 * from a Node. <ArrayDimension>
	 * 
	 * @param Document            Document to create the Element.
	 * @param ArrayDimensionValue Value of an ArrayDimension used as TextContent.
	 * @return DOM ArrayDimension Element.
	 */
	protected static Element writeArrayDimension(Document document, String arrayDimensionValue) {
		Element arrayDimension = document.createElement(NodesCategoryTags.ArrayDimensions.name());
		arrayDimension.setTextContent(arrayDimensionValue);
		return arrayDimension;
	}

	/**
	 * Create a AccessLevel Element, which represents an AccessLevel Attribute from
	 * a Node. <AccessLevel>
	 * 
	 * @param Document         Document to create the Element.
	 * @param AccessLevelValue Value of an AccessLevel used as TextContent.
	 * @return DOM AccessLevel Element.
	 */
	protected static Element writeAccessLevel(Document document, String accessLevelValue) {
		Element accessLevel = document.createElement(NodesCategoryTags.AccessLevel.name());
		accessLevel.setTextContent(accessLevelValue);
		return accessLevel;
	}

	/**
	 * Create a UserAccessLevel Element, which represents a UserAccessLevel
	 * Attribute from a Node. <UserAccessLevel>
	 * 
	 * @param Document             Document to create the Element.
	 * @param UserAccessLevelValue Value of an UserAccessLevel used as TextContent.
	 * @return DOM UserAccessLevel Element.
	 */
	protected static Element writeUserAccessLevel(Document document, String userAccessLevelValue) {
		Element userAccessLevel = document.createElement(NodesCategoryTags.UserAccessLevel.name());
		userAccessLevel.setTextContent(userAccessLevelValue);
		return userAccessLevel;
	}

	/**
	 * Create a MinimumSamplingInterval Element, which represents a
	 * MinimumSamplingInterval Attribute from a Node. <MinimumSamplingInterval>
	 * 
	 * @param Document                     Document to create the Element.
	 * @param MinimumSamplingIntervalValue Value of a MinimumSamplingInterval used
	 *                                     as TextContent.
	 * @return DOM MinimumSamplingInterval Element.
	 */
	protected static Element writeMinimumSamplingInterval(Document document, String minimumSamplingIntervalValue) {
		Element minimumSamplingInterval = document.createElement(NodesCategoryTags.MinimumSamplingInterval.name());
		minimumSamplingInterval.setTextContent(minimumSamplingIntervalValue);
		return minimumSamplingInterval;
	}

	/**
	 * Create a Historizing Element, which represents a Historizing Attribute from a
	 * Node. <Historizing>
	 * 
	 * @param Document         Document to create the Element.
	 * @param HistorizingValue Value of a Historizing used as TextContent.
	 * @return DOM Historizing Element.
	 */
	protected static Element writeHistorizing(Document document, String historizingValue) {
		Element historizing = document.createElement(NodesCategoryTags.Historizing.name());
		historizing.setTextContent(historizingValue);
		return historizing;
	}

	/**
	 * Create a Executeable Element, which represents a Executeable Attribute from a
	 * Node. <Executeable>
	 * 
	 * @param Document         Document to create the Element.
	 * @param ExecuteableValue Value of a Executeable used as TextContent.
	 * @return DOM Executeable Element.
	 */
	protected static Element writeExecutable(Document document, String executeableValue) {
		Element executeable = document.createElement(NodesCategoryTags.Executeable.name());
		executeable.setTextContent(executeableValue);
		return executeable;
	}

	/**
	 * Create a UserExecuteable Element, which represents a UserExecuteable
	 * Attribute from a Node. <UserExecuteable>
	 * 
	 * @param Document             Document to create the Element.
	 * @param UserExecuteableValue Value of a UserExecuteable used as TextContent.
	 * @return DOM UserExecuteable Element.
	 */
	protected static Element writeUserExecutable(Document document, String userExecuteableValue) {
		Element userExecuteable = document.createElement(NodesCategoryTags.UserExecuteable.name());
		userExecuteable.setTextContent(userExecuteableValue);
		return userExecuteable;
	}

	/**
	 * Create a Symmetric Element, which represents a Symmetric Attribute from a
	 * Node. <Symmetric>
	 * 
	 * @param Document       Document to create the Element.
	 * @param SymmetricValue Value of a Symmetric used as TextContent.
	 * @return DOM Symmetric Element.
	 */
	protected static Element writeSymmetric(Document document, String symmetricValue) {
		Element symmetric = document.createElement(NodesCategoryTags.Symmetric.name());
		symmetric.setTextContent(symmetricValue);
		return symmetric;
	}

	/**
	 * Create an InverseName Element, which represents an InverseName Attribute from
	 * a Node. <InverseName>
	 * 
	 * @param Document         Document to create the Element.
	 * @param InverseNameValue Value of an InverseName used as TextContent.
	 * @return DOM InverseName Element.
	 */
	protected static Element writeInversename(Document document, LocalizedText inverseNameValue) {
		Element inverseName = document.createElement(NodesCategoryTags.InverseName.name());
		Element locale = document.createElement(LOCALE);
		Element text = document.createElement(NodesCategoryTags.Text.name());
		if (inverseNameValue.getLocale() != null) {
			locale.setTextContent(inverseNameValue.getLocale().getVariant());
		}
		inverseName.appendChild(locale);
		text.setTextContent(inverseNameValue.getText());
		inverseName.appendChild(text);
		return inverseName;
	}

	public static void writeTypeMappingNode(Document document, Element nodesStorage, ExpandedNodeId key,
			ExpandedNodeId value, List<Integer> nsIndizes) {
		if (ExpandedNodeId.isNull(key) || ExpandedNodeId.isNull(value)) {
			return;
		}
		int keyIndex = nsIndizes.indexOf(key.getNamespaceIndex());
		int valueIndex = nsIndizes.indexOf(value.getNamespaceIndex());
		Element node = document.createElement(NodesCategoryTags.Node.name());
		node.setAttribute("sourceId", typeIdToString(key, keyIndex));
		node.setAttribute("targetId", typeIdToString(value, valueIndex));
		nodesStorage.appendChild(node);
	}

	public static String typeIdToString(ExpandedNodeId nodeId, int nsIndex) {
		String srvPart = !nodeId.isLocal() ? "srv=" + nodeId.getServerIndex() + ";" : "";
		/**
		 * changed HBS 10.05.2017
		 */
		String nsPart = nsIndex > 0 ? "ns=" + nsIndex + ";" : "";
		if (nodeId.getIdType() == IdType.Numeric)
			return srvPart + nsPart + "i=" + nodeId.getValue();
		if (nodeId.getIdType() == IdType.String)
			return srvPart + nsPart + "s=" + nodeId.getValue();
		if (nodeId.getIdType() == IdType.Guid)
			return srvPart + nsPart + "g=" + nodeId.getValue();
		if (nodeId.getIdType() == IdType.Opaque) {
			if (nodeId.getValue() == null)
				return srvPart + nsPart + "b=null";
			return srvPart + nsPart + "b=" + CryptoUtil.base64Encode((byte[]) nodeId.getValue());
		}
		return "error";
	}

	/**
	 * Create a String Element, which represents a String Tag. <String>
	 * 
	 * @param Document Document to create the Element.
	 * @return DOM String Element.
	 */
	private static Element createStringElement(Document document) {
		return document.createElement(NodesCategoryTags.String.name());
	}

	/**
	 * Create an Identifier Element, which represents an Identifier Tag.
	 * <Identifier>
	 * 
	 * @param Document   Document to create the Element.
	 * @param Identifier Value of an Identifier Tag used as TextContent.
	 * @return DOM Identifier Element.
	 */
	private static Element createIdentifiersElement(Document document, String identifier) {
		Element identifiers = document.createElement(NodesCategoryTags.Identifier.name());
		identifiers.setTextContent(identifier);
		return identifiers;
	}

	/**
	 * Create a LocalizedText Element, which represents a LocalizedText Tag.
	 * <LocalizedText>
	 * 
	 * @param Document      Document to create the Element.
	 * @param TagName       TagName of a LocalizedText.
	 * @param LocalizedText LocalizedText to create as Tag.
	 * @return DOM LocalizedText Element.
	 */
	private static Element createLocalizedTextElement(Document document, String tagName, LocalizedText localizedText) {
		Element localizedTextElement = document.createElement(tagName);
		localizedTextElement.setAttribute(XMLNS, ImportNamespaces.TYPES.toString());
		Element localElement = document.createElement(LOCALE);
		if (localizedText.getLocale() != null && !LocalizedText.NO_LOCALE.equals(localizedText.getLocale())) {
			localElement.setAttribute(XMLNS, ImportNamespaces.TYPES.toString());
			localElement.setTextContent(localizedText.getLocaleId());
		} else {
			localElement.setAttribute(INIL, "true");
		}
		localizedTextElement.appendChild(localElement);
		Element textElement = document.createElement(NodesCategoryTags.Text.name());
		if (!localizedText.getText().isEmpty()) {
			textElement.setTextContent(localizedText.getText());
		} else {
			textElement.setAttribute(INIL, "true");
			textElement.setAttribute(XMLNS, ImportNamespaces.TYPES.toString());
		}
		localizedTextElement.appendChild(textElement);
		return localizedTextElement;
	}
}
