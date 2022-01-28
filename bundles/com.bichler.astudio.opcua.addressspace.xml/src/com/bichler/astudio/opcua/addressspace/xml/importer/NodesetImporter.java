package com.bichler.astudio.opcua.addressspace.xml.importer;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.AddNodesItem;
import org.opcfoundation.ua.core.AddReferencesItem;
import org.opcfoundation.ua.core.Argument;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.DataTypeAttributes;
import org.opcfoundation.ua.core.EnumValueType;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.MethodAttributes;
import org.opcfoundation.ua.core.NodeAttributes;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ObjectAttributes;
import org.opcfoundation.ua.core.ObjectTypeAttributes;
import org.opcfoundation.ua.core.ReferenceTypeAttributes;
import org.opcfoundation.ua.core.VariableAttributes;
import org.opcfoundation.ua.core.VariableTypeAttributes;
import org.opcfoundation.ua.encoding.EncoderContext;
import org.opcfoundation.ua.encoding.EncodingException;

import com.bichler.astudio.opcua.components.addressspace.DefinitionBean;
import com.bichler.astudio.opcua.components.ui.Studio_ResourceManager;

import opc.sdk.core.node.NodeIdUtil;
import opc.sdk.core.persistence.xml.SAXNodeReader;
import opc.server.hbserver.OPCUADriverServer;

/**
 * @author andreas.schartlmueller@sequality.at
 */
public class NodesetImporter {
	private static final String INPUT_ARGUMENTS = "InputArguments";
	private static final String OUTPUT_ARGUMENTS = "OutputArguments";
	private static final String MINIMUM_SAMPLING_INTERVAL = "MinimumSamplingInterval";
	private static final String IS_ABSTRACT = "IsAbstract";
	private static final String ACCESS_LEVEL = "AccessLevel";
	private static final String UA_VARIABLE_TYPE = "UAVariableType";
	private static final String ENUM_VALUES = "EnumValues";
	private static final String ENUM_STRINGS = "EnumStrings";
	/* XML Identifiers */
	private static final String USER_ACCESS_LEVEL = "UserAccessLevel";
	private static final String ARRAY_DIMENSIONS = "ArrayDimensions";
	private static final String VALUE_RANK = "ValueRank";
	private static final String HISTORIZING = "Historizing";
	private static final String DATA_TYPE = "DataType";
	private static final String UA_DATA_TYPE = "UADataType";
	private static final String UA_METHOD = "UAMethod";
	private static final String HAS_MODELLING_RULE = "HasModellingRule";
	private static final String DEFINITION = "Definition";
	private static final String MODELS = "Models";
	private static final String SYMMETRIC = "Symmetric";
	private static final String DESCRIPTION = "Description";
	private static final String EXTENSIONS = "Extensions";
	private static final String UA_REFERENCE_TYPE = "UAReferenceType";
	private static final String UA_OBJECT_TYPE = "UAObjectType";
	private static final String IS_FORWARD = "IsForward";
	private static final String NAMESPACE_URIS = "NamespaceUris";
	private static final String ALIASES = "Aliases";
	private static final String ALIAS = "Alias";
	private static final String UA_OBJECT = "UAObject";
	private static final String UA_VARIABLE = "UAVariable";
	private static final String REFERENCE_TYPE = "ReferenceType";
	private static final String PARENT_NODE_ID = "ParentNodeId";
	private static final String NODE_ID = "NodeId";
	private static final String BROWSE_NAME = "BrowseName";
	private static final String DISPLAY_NAME = "DisplayName";
	private static final String REFERENCES = "References";
	private static final String HAS_TYPE_DEFINITION = "HasTypeDefinition";
	private static final String HAS_DESCRIPTION = "HasDescription";
	private static final String HAS_ENCODING = "HasEncoding";
	private static final String HAS_SUBTYPE = "HasSubtype";
	private static final String ORGANIZES = "Organizes";
	private static final String HAS_PROPERTY = "HasProperty";
	private static final String HAS_COMPONENT = "HasComponent";
	public static final String SYMBOLICNAME = "SymbolicName";

	private OPCUADriverServer opcServer = null;
	private int[] namespaceMap = null;
	private NamespaceTable namespaceTable = null;
	private ArrayList<AddNodesItem> nodesItemList = null;
	private ArrayList<NodeSetBean> nodesModellingRule = null;
	private ArrayList<AddReferencesItem> additionalReferences = null;
	private ArrayList<String> spezificReferenceType = null;
	private Map<String, NodeId> spezificDataTypeMapping = null;
//	private Map<NodeId, List<String>> nodeExtensions = null;

	public NodesetImporter() {

	}

	private void addUaNode(NodeSetBean bean, NodeAttributes attributes) throws EncodingException {
		AddNodesItem item = createNodesItem(bean, attributes);
		ArrayList<String> extensions = bean.getExtensions();
		if (extensions.size() > 0) {
			Studio_ResourceManager.NODE_EXTENSIONS.put(bean.getNodeId(), extensions);
		}

		addUaNodeCustomAttribute(bean);

		DefinitionBean definitionBean = bean.getDefinitionBean();
		if (definitionBean != null) {
			Studio_ResourceManager.DATATYPE_DEFINITIONS.put(bean.getNodeId(), definitionBean);
		}

		if (NodeId.isNull(item.getReferenceTypeId()) && !ExpandedNodeId.isNull(item.getParentNodeId())) {
//			Logger.getLogger(getClass().getName()).log(Level.INFO,
//					"Unhandled AddNodesItem: " + item.getRequestedNewNodeId() + " Browsename: "
//							+ item.getBrowseName());
//			return ;
			item.setParentNodeId(null);
		}

		getNodesItemList().add(item);
//		send2Opc();
	}

	/**
	 * Adds OPC UA node custom attributes
	 * 
	 * @param bean Nodeset2 model bean
	 */
	private void addUaNodeCustomAttribute(NodeSetBean bean) {
		// symbolic name
		String symbolicName = bean.getSymbolicName();
		if (symbolicName != null) {
			HashMap<NodeId, String> mapSymbolicName = Studio_ResourceManager.NODE_CUSTOMATTRIBUTES.get(SYMBOLICNAME);
			if (mapSymbolicName == null) {
				mapSymbolicName = new HashMap<>();
				Studio_ResourceManager.NODE_CUSTOMATTRIBUTES.put(SYMBOLICNAME, mapSymbolicName);
			}

			mapSymbolicName.put(bean.getNodeId(), symbolicName);
		}

	}

	private void addSpezificDataTypeMapping(String dataType, NodeId nid) {
		if (spezificDataTypeMapping == null) {
			spezificDataTypeMapping = new HashMap<>();
		}
		spezificDataTypeMapping.put(dataType, nid);
	}

	private void addSpezificReferenceType(String referenceType) {
		if (spezificReferenceType == null) {
			spezificReferenceType = new ArrayList<>();
		}
		spezificReferenceType.add(referenceType);
	}

	/**
	 * HB 2017/10/13 verify if datatype already exists in opc ua internal dataspace.
	 * 
	 * @param dataType
	 * @return
	 */
	private boolean containsInternalDataType(String dataType) {
		if (opcServer.getServerInstance().getAddressSpaceManager().getNodeById(NodeId.parseNodeId(dataType)) != null)
			return true;
		return false;
	}

	/**
	 * @param referenceType
	 * @return
	 */
	private boolean containsSpezificReferenceType(String referenceType) {
		boolean retVal = false;
		if (spezificReferenceType != null) {
			retVal = spezificReferenceType.contains(referenceType);
		}
		return retVal;
	}

	private AddNodesItem createNodesItem(NodeSetBean bean, NodeAttributes attributes) throws EncodingException {
		AddNodesItem item = new AddNodesItem();
		item.setNodeClass(bean.getNodeClass());
		// BrowseName="1:BoxId"
		int nsBrowseName = 0;
		if (bean.getBrowseName() != null) {
			try {
				nsBrowseName = Integer.parseInt(bean.getBrowseName().substring(0, bean.getBrowseName().indexOf(":")));
				nsBrowseName = getMappedNamespaceByNodeId(nsBrowseName);
			} catch (Exception ex) {
				nsBrowseName = 0;
			}
			item.setBrowseName(new QualifiedName(nsBrowseName,
					bean.getBrowseName().substring(bean.getBrowseName().indexOf(":") + 1)));
		}
		// else {
		// Logger.getLogger(getClass().getName()).log(Level.WARNING,
		// "browseName is null\n" + bean.getNodeId().toString());
		// }
		if (bean.getNodeId() != null) {
			item.setRequestedNewNodeId(
					new ExpandedNodeId(this.namespaceTable.getUri(bean.getNodeId().getNamespaceIndex()),
							bean.getNodeId().getValue(), this.namespaceTable));
		}
		// else {
		// throw new EncodingException("node id is null!!");
		// }
		if (bean.getReferenceType() != null) {
			item.setReferenceTypeId(bean.getReferenceType());
		}
		// else {
		// Logger.getLogger(getClass().getName()).log(Level.WARNING,
		// "referenceTypeId is null\n" + bean.getNodeId().toString());
		// }
		if (bean.getTypeDefinition() != null) {
			item.setTypeDefinition(
					new ExpandedNodeId(this.namespaceTable.getUri(bean.getTypeDefinition().getNamespaceIndex()),
							bean.getTypeDefinition().getValue(), this.namespaceTable));
		}
		// else {
		// Logger.getLogger(getClass().getName()).log(Level.WARNING,
		// "typeDefinition is null\n" + bean.getNodeId().toString());
		// }
		if (bean.getParentNodeId() != null) {
			item.setParentNodeId(
					new ExpandedNodeId(this.namespaceTable.getUri(bean.getParentNodeId().getNamespaceIndex()),
							bean.getParentNodeId().getValue(), this.namespaceTable));
		}
		// else {
		// Logger.getLogger(getClass().getName()).log(Level.WARNING,
		// "parentNodeId is null\n" + bean.getNodeId().toString());
		// }
		item.setNodeAttributes(ExtensionObject.binaryEncode(attributes, EncoderContext.getDefaultInstance()));
		if (bean.getHasModellingRule() != null) {
			getNodesModellingRule().add(bean);
		}
		return item;
	}

	private DataTypeAttributes createDataTypeAttributes(String displayName, String description) {
		DataTypeAttributes attributes = new DataTypeAttributes();
		attributes.setDisplayName(new LocalizedText(displayName, Locale.ENGLISH));
		attributes.setDescription(new LocalizedText(description, Locale.ENGLISH));
		attributes.setWriteMask(UnsignedInteger.getFromBits((byte) 0));
		attributes.setUserWriteMask(UnsignedInteger.getFromBits((byte) 0));
		return attributes;
	}

	private MethodAttributes createMethodAttributes(String displayName, String description) {
		MethodAttributes attributes = new MethodAttributes();
		attributes.setDisplayName(new LocalizedText(displayName, Locale.ENGLISH));
		attributes.setDescription(new LocalizedText(description, Locale.ENGLISH));
		attributes.setWriteMask(UnsignedInteger.getFromBits((byte) 0));
		attributes.setUserWriteMask(UnsignedInteger.getFromBits((byte) 0));
		return attributes;
	}

	private ObjectAttributes createObjectAttributes(String displayName, String description) {
		ObjectAttributes attributes = new ObjectAttributes();
		attributes.setDisplayName(new LocalizedText(displayName, Locale.ENGLISH));
		attributes.setDescription(new LocalizedText(description, Locale.ENGLISH));
		attributes.setEventNotifier(UnsignedByte.getFromBits((byte) 0));
		attributes.setWriteMask(UnsignedInteger.getFromBits((byte) 0));
		attributes.setUserWriteMask(UnsignedInteger.getFromBits((byte) 0));
		return attributes;
	}

	private ObjectTypeAttributes createObjectTypeAttributes(String displayName, String description, Element element) {
		ObjectTypeAttributes attributes = new ObjectTypeAttributes();
		attributes.setDisplayName(new LocalizedText(displayName, Locale.ENGLISH));
		attributes.setDescription(new LocalizedText(description, Locale.ENGLISH));
		attributes.setWriteMask(UnsignedInteger.getFromBits((byte) 0));
		attributes.setUserWriteMask(UnsignedInteger.getFromBits((byte) 0));

		String isAbstract = element.getAttributeValue(IS_ABSTRACT);
		if (isAbstract != null) {
			attributes.setIsAbstract(Boolean.valueOf(isAbstract));
		} else {
			attributes.setIsAbstract(false);
		}

		return attributes;
	}

	private ReferenceTypeAttributes createReferenceTypeAttributes(NodeSetBean bean) {
		ReferenceTypeAttributes rta = new ReferenceTypeAttributes();
		rta.setDisplayName(new LocalizedText(bean.getDisplayName(), Locale.ENGLISH));
		rta.setDescription(new LocalizedText(bean.getDescription(), Locale.ENGLISH));
		rta.setSymmetric(bean.isSymmetric());
		rta.setWriteMask(UnsignedInteger.getFromBits((byte) 0));
		rta.setUserWriteMask(UnsignedInteger.getFromBits((byte) 0));
		return rta;
	}

	@SuppressWarnings("unchecked")
	private VariableAttributes createVariableAttributes(String displayName, String varDescription, Element element) throws Exception {
		SAXNodeReader saxReader = new SAXNodeReader();
		Object saxReaderRetVal = null;
		VariableAttributes attributesVar = new VariableAttributes();
		attributesVar.setDisplayName(new LocalizedText(displayName, Locale.ENGLISH));
		attributesVar.setDescription(new LocalizedText(varDescription, Locale.ENGLISH));
		String dataType = element.getAttributeValue(DATA_TYPE);
		if (dataType != null) {
			try {
				attributesVar.setDataType((NodeId) Identifiers.class.getDeclaredField(dataType).get(null));
			} catch (NoSuchFieldException nsfe) {
<<<<<<< HEAD
				// UADataTypes, falls DataType standardm��ig nicht vorhanden ist, pr�fen
=======
				// UADataTypes, falls DataType standardmäßig nicht vorhanden ist, prüfen
>>>>>>> 895f544a9595e6f7ac85742d15e36811a774868e
				// ob in eigener Liste ein Eintrag vorhanden ist und ggf neue NodeId erzeugen,
				// andernfalls Fehler/Abbruch
				NodeId nid = getSpezificDataType(dataType);
				if (nid != null) {
					attributesVar.setDataType(createNodeIdByXmlValue(nid.toString()));
				} else if (containsInternalDataType(dataType)) {
					attributesVar.setDataType(NodeId.parseNodeId(dataType));
				} else {
					throw new NoSuchFieldException(dataType + " not found");
				}
			} catch (Exception ex) {
				throw ex;
			}
		} else {
			attributesVar.setDataType(Identifiers.BaseDataType);
		}
		String historizing = element.getAttributeValue(HISTORIZING);
		if (historizing != null) {
			attributesVar.setHistorizing(Boolean.valueOf(historizing));
		}
		String valueRank = element.getAttributeValue(VALUE_RANK);
		if (valueRank != null) {
			attributesVar.setValueRank(Integer.parseInt(valueRank));
		} else {
			// set valuerank = -1 (scalar) as default
			attributesVar.setValueRank(-1);
		}
		String arrayDimensions = element.getAttributeValue(ARRAY_DIMENSIONS);
		if (arrayDimensions != null) {
			if (attributesVar.getValueRank() > 0) {
				String[] items = arrayDimensions.split(",");
				List<UnsignedInteger> list = new ArrayList<UnsignedInteger>();
				for (String item : items) {
					list.add(UnsignedInteger.parseUnsignedInteger(item));
				}
				attributesVar.setArrayDimensions(list.toArray(new UnsignedInteger[list.size()]));
			} else {
				attributesVar.setArrayDimensions(new UnsignedInteger[0]);
			}
		}
		String userAccessLevel = element.getAttributeValue(USER_ACCESS_LEVEL);
		if (userAccessLevel != null) {
			attributesVar.setUserAccessLevel(UnsignedByte.parseUnsignedByte(userAccessLevel));
		} else {
			String accessLevel = element.getAttributeValue(ACCESS_LEVEL);
			if (accessLevel != null) {
				attributesVar.setUserAccessLevel(UnsignedByte.parseUnsignedByte(accessLevel));
			}
			else {
				attributesVar.setUserAccessLevel(UnsignedByte.parseUnsignedByte("1"));
			}
		}
		String accessLevel = element.getAttributeValue(ACCESS_LEVEL);
		if (accessLevel != null) {
			attributesVar.setAccessLevel(UnsignedByte.parseUnsignedByte(accessLevel));
		} else {
			attributesVar.setAccessLevel(UnsignedByte.parseUnsignedByte("1"));
		}
		String minimumSamplingInterval = element.getAttributeValue(MINIMUM_SAMPLING_INTERVAL);
		if (minimumSamplingInterval != null) {
			try {
				attributesVar.setMinimumSamplingInterval(Double.parseDouble(minimumSamplingInterval));
			} catch (Exception e) {
				throw e;
			}
		}
		attributesVar.setWriteMask(UnsignedInteger.getFromBits((byte) 0));
		attributesVar.setUserWriteMask(UnsignedInteger.getFromBits((byte) 0));

		try {
			Element value = element.getChild("Value", element.getNamespace());
			if (value != null) {
				if (valueRank == null /*
										 * && ((NodeId) Identifiers.class.getDeclaredField(dataType).get(null)) != null
										 */
				) {
					// String,....
					if (value.getChildren() != null && value.getChildren().size() > 0) {
						Variant v = saxReader.constructValueAttribute(((Element) value.getChildren().get(0)).getName(),
								((Element) value.getChildren().get(0)).getValue());
						if (v != null) {
							attributesVar.setValue(v);
						} else {
							throw new Exception("unknown datatype");
						}
					}
				} else if (valueRank.equals("1")
						&& (element.getAttribute(BROWSE_NAME).getValue().endsWith(OUTPUT_ARGUMENTS)
								|| element.getAttribute(BROWSE_NAME).getValue().endsWith(INPUT_ARGUMENTS))) {
					if (((List<Element>) value.getChildren()) != null) {
						Element list = ((List<Element>) value.getChildren()).get(0);
						List<ExtensionObject> eoList = new ArrayList<>();
						for (Element eo : (List<Element>) list.getChildren()) {
							// <ExtensionObject>
							Element body = eo.getChild("Body", eo.getNamespace());
							Element arg = body.getChild("Argument", body.getNamespace());
							Element dt = arg.getChild("DataType", arg.getNamespace());
							Element identifier = dt.getChild("Identifier", dt.getNamespace());

							Argument argument = new Argument(arg.getChildText("Name", arg.getNamespace()),
									NodeId.parseNodeId(identifier.getText()),
									Integer.parseInt(arg.getChildText("ValueRank", arg.getNamespace())),
									new UnsignedInteger[] {},
									new LocalizedText(arg.getChildText("Description", arg.getNamespace())));
							eoList.add(ExtensionObject.binaryEncode(argument, EncoderContext.getDefaultInstance()));
						}
						attributesVar.setValue(new Variant(eoList.toArray(new ExtensionObject[eoList.size()])));
						if (arrayDimensions == null) {
							attributesVar
									.setArrayDimensions(new UnsignedInteger[] { new UnsignedInteger(eoList.size()) });
						}
					}
				}
				// HB 2017-11-15 add import enum values support
				else if (valueRank.equals("1") && element.getAttribute(BROWSE_NAME).getValue().endsWith(ENUM_VALUES)) {
					if (((List<Element>) value.getChildren()) != null) {
						Element list = ((List<Element>) value.getChildren()).get(0);
						List<ExtensionObject> eoList = new ArrayList<>();
						for (Element eo : (List<Element>) list.getChildren()) {
							// <ExtensionObject>
							Element body = eo.getChild("Body", eo.getNamespace());
							Element enumval = body.getChild("EnumValueType", body.getNamespace());
							Element val = enumval.getChild("Value", enumval.getNamespace());
							Element identifier = enumval.getChild("DisplayName", enumval.getNamespace());
							// name of EnumValueType
							Element locale = identifier.getChild("Locale", identifier.getNamespace());
							String llocale = "";
							if (locale != null) {
								llocale = locale.getText();
							}
							Element text = identifier.getChild("Text", identifier.getNamespace());
							String ltext = "";
							if (text != null) {
								ltext = text.getText();
							}
							LocalizedText dName = new LocalizedText(ltext, llocale);
							// description of EnumValueType
							Element description = enumval.getChild("Description", enumval.getNamespace());
							LocalizedText desc = new LocalizedText("");
							if (description != null) {
								Element locDescription = description.getChild("Locale", identifier.getNamespace());
								Element locText = description.getChild("Text", identifier.getNamespace());
								String dlocale = "";
								if (locDescription != null) {
									dlocale = locDescription.getText();
								}
								String dtext = "";
								if (locText != null) {
									dtext = locText.getText();
								}
								desc = new LocalizedText(dlocale, dtext);
							}

							long index = Long.parseLong(val.getText());
							EnumValueType enumv = new EnumValueType(index, dName, desc);
							eoList.add(ExtensionObject.binaryEncode(enumv, EncoderContext.getDefaultInstance()));
						}
						attributesVar.setValue(new Variant(eoList.toArray(new ExtensionObject[eoList.size()])));
						if (arrayDimensions == null) {
							attributesVar
									.setArrayDimensions(new UnsignedInteger[] { new UnsignedInteger(eoList.size()) });
						}
					}
				} else if (valueRank.equals("1") && (saxReaderRetVal = saxReader.constructValueObject(dataType,
						((Element) value.getChildren().get(0)).getValue())) != null) {
					Object array = null;
					List<Element> l = (List<Element>) ((Element) value.getChildren().get(0)).getChildren();
					Element e = null;
					for (int i = 0; i < l.size(); i++) {
						e = l.get(i);
						if (array == null) {
							array = Array.newInstance(saxReaderRetVal.getClass(), l.size());
						}
						if (dataType.equals("LocalizedText")) {
							String val = e.getChild("Text", e.getNamespace()).getValue();
							String loc = null;
							if (e.getChild("Locale", e.getNamespace()) != null) {
								loc = e.getChild("Locale", e.getNamespace()).getValue();
							}
							if (loc != null) {
								loc = loc.trim();
							}
							Array.set(array, i, new LocalizedText(val, loc));
						} else {
							// String,....
							if (value.getChildren() != null && value.getChildren().size() > 0) {
								Object v = saxReaderRetVal;
								Array.set(array, i, v);
							}
						}
					}
					attributesVar.setValue(new Variant(array));
					if (arrayDimensions == null) {
						attributesVar.setArrayDimensions(new UnsignedInteger[] { new UnsignedInteger(l.size()) });
					}
				} else {
//					List<UnsignedInteger> argDims = new ArrayList<>();
//					Element elementDims = arg.getChild("ArrayDimensions", arg.getNamespace());
//					for(Element entry : elementDims.getChildren()) {
//						String entryValue = entry.getValue();
//						System.out.println("");
//					}

					Logger.getLogger(getClass().getName()).log(Level.INFO,
							"unhandled variable: " + element.getAttribute(NODE_ID).getValue() + " Browsename: "
									+ element.getAttribute(BROWSE_NAME).getValue());
				}
			}
		} catch (Exception e) {
			// Logger.getLogger(getClass().getName()).log(Level.SEVERE, "", e);
			throw e;
		}
		return attributesVar;
	}

	private VariableTypeAttributes createVariableTypeAttributes(String displayName, String description, Element element)
			throws Exception {
		VariableTypeAttributes attributes = new VariableTypeAttributes();
		attributes.setDisplayName(new LocalizedText(displayName, Locale.ENGLISH));
		attributes.setDescription(new LocalizedText(description, Locale.ENGLISH));
		String dataType = element.getAttributeValue(DATA_TYPE);
		if (dataType != null) {
			try {
				attributes.setDataType((NodeId) Identifiers.class.getDeclaredField(dataType).get(null));
			} catch (NoSuchFieldException nsfe) {
<<<<<<< HEAD
				// UADataTypes, falls DataType standardm��ig nicht vorhanden ist, pr�fen
=======
				// UADataTypes, falls DataType standardmäßig nicht vorhanden ist, prüfen
>>>>>>> 895f544a9595e6f7ac85742d15e36811a774868e
				// ob in eigener Liste ein Eintrag vorhanden ist und ggf neue NodeId erzeugen,
				// andernfalls Fehler/Abbruch
				NodeId nid = getSpezificDataType(dataType);
				if (nid != null) {
					attributes.setDataType(nid);
				} else {
					throw new Exception(dataType + " not found");
				}
			} catch (Exception ex) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, dataType + "  not found", ex);
				throw ex;
			}
		} else {
			attributes.setDataType(Identifiers.BaseDataType);
		}
		String valueRank = element.getAttributeValue(VALUE_RANK);
		if (valueRank != null) {
			attributes.setValueRank(Integer.parseInt(valueRank));
		}
		String arrayDimensions = element.getAttributeValue(ARRAY_DIMENSIONS);
		if (arrayDimensions != null) {
			if (attributes.getValueRank() > 0) {
				String[] items = arrayDimensions.split(",");
				List<UnsignedInteger> list = new ArrayList<UnsignedInteger>();
				for (String item : items) {
					list.add(UnsignedInteger.parseUnsignedInteger(item));
				}
				attributes.setArrayDimensions(list.toArray(new UnsignedInteger[list.size()]));
			} else {
				attributes.setArrayDimensions(new UnsignedInteger[0]);
			}
		}
		String isAbstract = element.getAttributeValue(IS_ABSTRACT);
		if (isAbstract != null) {
			attributes.setIsAbstract(Boolean.valueOf(isAbstract));
		}
		attributes.setWriteMask(UnsignedInteger.getFromBits((byte) 0));
		attributes.setUserWriteMask(UnsignedInteger.getFromBits((byte) 0));
		return attributes;
	}

	private ArrayList<NodeSetBean> getNodesModellingRule() {
		if (nodesModellingRule == null) {
			nodesModellingRule = new ArrayList<>();
		}
		return nodesModellingRule;
	}

	private ArrayList<AddReferencesItem> getAdditionalReferences() {
		if (additionalReferences == null) {
			additionalReferences = new ArrayList<>();
		}
		return additionalReferences;
	}

	private ArrayList<AddNodesItem> getNodesItemList() {
		if (nodesItemList == null) {
			nodesItemList = new ArrayList<>();
		}
		return nodesItemList;
	}

//	private Map<NodeId, List<String>> getNodeExtensions() {
//		if (this.nodeExtensions == null) {
//			this.nodeExtensions = new HashMap<>();
//		}
//		return this.nodeExtensions;
//	}	

	@SuppressWarnings("unchecked")
	private void readXmlDocument(String xmlPath) throws Exception {
		try {
			nodesItemList = new ArrayList<>();
			SAXBuilder saxBuilder = new SAXBuilder();
			Document document = saxBuilder.build(new File(xmlPath));
			Element rootElement = document.getRootElement();
			List<Element> childrenList = rootElement.getChildren();
			/*
			 * HB first of all init existing namespace table
			 */
			initExisitingNamespaces();
			// first check required namespaces
			NamespaceTable nsTable = null;
			boolean finish = false;
			for (int i = 0; i < childrenList.size(); i++) {
				Element ele = childrenList.get(i);
				switch (ele.getName()) {
				case NAMESPACE_URIS:
					nsTable = findModelNamespaces(ele);
					break;
				case MODELS:
					checkModels(ele, nsTable);
					finish = true;
					break;
				}
				if (finish) {
					break;
				}
			}

			for (int i = 0; i < childrenList.size(); i++) {
				Element ele = childrenList.get(i);
				switch (ele.getName()) {
				case NAMESPACE_URIS:
					createNamespaceMapping(ele);
					break;
				case ALIASES:
					createAliasDatatypes(ele);
					break;
				case UA_OBJECT_TYPE:
					addUaObjectType(ele);
					break;
				case UA_DATA_TYPE:
					addUaDataType(ele);
					break;
				case UA_REFERENCE_TYPE:
					addUaReferenceType(ele);
					break;
				}
			}

			send2Opc();

			for (int i = 0; i < childrenList.size(); i++) {
				Element ele = childrenList.get(i);
				switch (ele.getName()) {
				case UA_VARIABLE_TYPE:
					addUaVariableType(ele);
					break;
				}
			}

			send2Opc();

			childrenList = rootElement.getChildren();
			for (int i = 0; i < childrenList.size(); i++) {
				Element ele = childrenList.get(i);
				switch (ele.getName()) {
				case UA_OBJECT:
					addUaObject(ele);
					break;
				case UA_VARIABLE:
					addUaVariable(ele);
					break;
				case UA_METHOD:
					addUaMethod(ele);
					break;
				}
			}

			send2Opc();

			handleHasModellingRules();
			handleAdditionalReferences();
		} catch (Exception ex) {
			throw ex;
		}
	}

	private void handleHasModellingRules() {
		Logger.getLogger(getClass().getName()).log(Level.INFO, "handle hasModellingRules");
		if (nodesModellingRule != null && getNodesModellingRule().size() > 0) {
			ArrayList<AddReferencesItem> list = new ArrayList<>();
			for (NodeSetBean b : getNodesModellingRule()) {
				AddReferencesItem descr = new AddReferencesItem();
				descr.setIsForward(true);
				descr.setReferenceTypeId(Identifiers.HasModellingRule);
				descr.setSourceNodeId(b.getNodeId());
				descr.setTargetNodeClass(NodeClass.Variable);
				descr.setTargetNodeId(
						new ExpandedNodeId(this.namespaceTable.getUri(b.getHasModellingRule().getNamespaceIndex()),
								b.getHasModellingRule().getValue(), this.namespaceTable));
				list.add(descr);
			}
			Logger.getLogger(getClass().getName()).log(Level.INFO, "add modelling rules to server");
			opcServer.getDriverConnection().addReferences(list.toArray(new AddReferencesItem[list.size()]));
		}
	}

	private void handleAdditionalReferences() {
		Logger.getLogger(getClass().getName()).log(Level.INFO, "handle additional References");
		if (this.additionalReferences != null)
			opcServer.getDriverConnection().addReferences(this.additionalReferences.toArray(new AddReferencesItem[0]));
	}

	// <Models>
	// <Model ModelUri="http://www.euromap.org/euromap83/"
	// PublicationDate="2017-08-29T09:13:02Z" Version="RC 1.00">
	// <RequiredModel ModelUri="http://opcfoundation.org/UA/"
	// PublicationDate="2013-12-02T00:00:00Z" Version="1.03"/>
	// <RequiredModel ModelUri="http://opcfoundation.org/UA/DI/"
	// PublicationDate="2013-12-02T00:00:00Z" Version="1.01"/>
	// </Model>
	// </Models>
	@SuppressWarnings("unchecked")
	private void checkModels(Element ele, NamespaceTable newModelTable) throws NameSpaceNotFountException {
		Logger.getLogger(getClass().getName()).log(Level.INFO, "check required namespaces from server");
		HashMap<String, HashMap<String, String>> modelDetail = new HashMap<>();

		for (Element model : (List<Element>) ele.getChildren()) {
			// <Model ModelUri="http://www.euromap.org/euromap83/"
			// PublicationDate="2017-08-29T09:13:02Z" Version="RC 1.00">
			String mUri = model.getAttributeValue("ModelUri");
			String mVersion = model.getAttributeValue("Version");
			String mPubDate = model.getAttributeValue("PublicationDate");

			HashMap<String, String> mDetail = new HashMap<>();
			mDetail.put(mVersion, mPubDate);
			modelDetail.put(mUri, mDetail);

			for (Element requiredModel : (List<Element>) model.getChildren()) {
				// <RequiredModel ModelUri="http://opcfoundation.org/UA/"
				// PublicationDate="2013-12-02T00:00:00Z" Version="1.03"/>
				String modelUri = requiredModel.getAttributeValue("ModelUri");
				Logger.getLogger(getClass().getName()).log(Level.INFO, "check namespaces {0} from server" + modelUri);
				if (namespaceTable.getIndex(modelUri) == -1) {
					if (newModelTable.getIndex(modelUri) == -1) {
						// Logger.getLogger(getClass().getName()).log(Level.SEVERE, "namespace " +
						// modelUri + " not in list");
						throw new NameSpaceNotFountException("namespace " + modelUri + " not in list");
					}
				}
			}
		}

		Studio_ResourceManager.INFORMATIONMODEL_DETAILS.putAll(modelDetail);
	}

	private void send2Opc() {
		// add objects and variables to opc server
		if (getNodesItemList().size() > 0) {
			try {
				boolean success = opcServer.getDriverConnection()
						.addNodes(getNodesItemList().toArray(new AddNodesItem[getNodesItemList().size()]));
				if (!success)
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Could not add not to server model: ");
			} catch (Exception ex) {
				Logger.getLogger(getClass().getName()).log(Level.WARNING, "error while writing to server", ex);
			}
		} else {
//			Logger.getLogger(getClass().getName()).log(Level.WARNING, "no data found",
//					new Exception("\"no objects, variables,... in given xml file\""));
		}
		nodesItemList = null;
	}

	private void initExisitingNamespaces() {
		namespaceTable = opcServer.getServerInstance().getNamespaceUris();
	}

	private NamespaceTable findModelNamespaces(Element element) {
		List<Element> namespaceChilds = element.getChildren();
		int[] namespaces = new int[namespaceChilds.size()];
		NamespaceTable nsTable = NamespaceTable.createFromArray(new String[0]);

		int idx = -1;
		for (int i = 0; i < namespaceChilds.size(); i++) {
			Element e = namespaceChilds.get(i);
			if ((idx = nsTable.getIndex(e.getValue())) > -1) {
				// namespace already in table
				namespaces[i] = idx;
			} else {
				namespaces[i] = nsTable.size();
				nsTable.add(e.getValue());
			}
		}

		return nsTable;
	}

	@SuppressWarnings("unchecked")
	private void createNamespaceMapping(Element element) {
		List<Element> namespaceChilds = element.getChildren();
		namespaceMap = new int[namespaceChilds.size()];
		int idx = -1;
		for (int i = 0; i < namespaceChilds.size(); i++) {
			Element e = namespaceChilds.get(i);
			if ((idx = namespaceTable.getIndex(e.getValue())) > -1) {
				// namespace already in table
				namespaceMap[i] = idx;
			} else {
				namespaceMap[i] = namespaceTable.size();
				namespaceTable.add(e.getValue());
			}
		}
		DataValue dv = new DataValue(new Variant(namespaceTable.toArray()));
		// add Namespace to Server
		this.opcServer.getDriverConnection().writeFromDriver(Identifiers.Server_NamespaceArray, Attributes.Value, null,
				dv, 0l);
	}

	private int getMappedNamespace(int xmlNs) {
		return namespaceMap[xmlNs - 1];
	}

	@SuppressWarnings("unchecked")
	private NodeSetBean parseElement(NodeClass nodeClass, Element element) throws Exception {
		NodeSetBean bean = new NodeSetBean(nodeClass, this);
		/* Parent */
		bean.setParentNodeId(createNodeIdByXmlValue(element.getAttributeValue(PARENT_NODE_ID)));
		/* Node */
		bean.setNodeId(createNodeIdByXmlValue(element.getAttributeValue(NODE_ID)));
		/* BrowseName */
		bean.setBrowseName(element.getAttributeValue(BROWSE_NAME));
		/* SymbolicName */
		bean.setSymbolicName(element.getAttributeValue(SYMBOLICNAME));
		/* Symmetric */
		String val = element.getAttributeValue(SYMMETRIC);
		if (val != null) {
			bean.setSymmetric(Boolean.valueOf(val));
		}

		List<Element> list = element.getChildren();
		for (Element e : list) {
			switch (e.getName()) {
			case DISPLAY_NAME:
				bean.setDisplayName(e.getValue());
				break;
			case DESCRIPTION:
				bean.setDescription(e.getValue());
				break;
			case DEFINITION:
				bean.setDefinition(e, this);
				break;
			case EXTENSIONS:
				bean.setExtensions(e);
				break;
			case REFERENCES:
				List<Element> references = e.getChildren();
				for (Element ref : references) {
					/*
					 * if (ref.getAttributeValue(IS_FORWARD) != null &&
<<<<<<< HEAD
					 * !Boolean.valueOf(ref.getAttributeValue(IS_FORWARD))) { // ==>use entry f�r
=======
					 * !Boolean.valueOf(ref.getAttributeValue(IS_FORWARD))) { // ==>use entry für
>>>>>>> 895f544a9595e6f7ac85742d15e36811a774868e
					 * reference type String referenceTyp = ref.getAttributeValue(REFERENCE_TYPE);
					 * // HAS_COMPONENT, HAS_ORDERED_COMPONENT,... try {
					 * bean.setReferenceType((NodeId)
					 * Identifiers.class.getDeclaredField(referenceTyp).get(null)); // use parent
					 * node from refrerence
					 * bean.setParentNodeId(createNodeIdByXmlValue(ref.getValue())); } catch
					 * (NoSuchFieldException nsfe) { // UAReferences, falls ReferenceType
<<<<<<< HEAD
					 * standardm��ig nicht vorhanden ist, pr�fen // ob in eigener Liste ein Eintrag
=======
					 * standardmäßig nicht vorhanden ist, prüfen // ob in eigener Liste ein Eintrag
>>>>>>> 895f544a9595e6f7ac85742d15e36811a774868e
					 * vorhanden ist und ggf neue NodeId erzeugen, // andernfalls Fehler/Abbruch if
					 * (containsSpezificReferenceType(referenceTyp)) {
					 * bean.setReferenceType(NodeId.parseNodeId(referenceTyp)); // use parent node
					 * from refrerence bean.setParentNodeId(createNodeIdByXmlValue(ref.getValue()));
					 * } else { throw new Exception(referenceTyp + " not found "); } } catch
					 * (Exception ex) { Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					 * referenceTyp + "  not found", ex); throw ex; } }
					 */
					switch (ref.getAttributeValue(REFERENCE_TYPE)) {
					case HAS_TYPE_DEFINITION:
						try {
							bean.setTypeDefinition(createNodeIdByXmlValue(ref.getValue()));
						} catch (IllegalArgumentException iae) {
							NodeId alias = getSpezificDataType(ref.getValue());
							if (!NodeId.isNull(alias)) {
								bean.setTypeDefinition(createNodeIdByXmlValue(alias.toString()));
							}
						}
						break;
					case HAS_DESCRIPTION:
						this.getAdditionalReferences()
								.add(bean.createTypeDescription(createNodeIdByXmlValue(ref.getValue()),
										ref.getAttributeValue(IS_FORWARD), this.namespaceTable));
						break;
					case HAS_ENCODING:
						if (ref.getAttributeValue(IS_FORWARD) != null
								&& !Boolean.valueOf(ref.getAttributeValue(IS_FORWARD))) {
							String referenceTyp = ref.getAttributeValue(REFERENCE_TYPE);
							try {
								bean.setReferenceType(
										(NodeId) Identifiers.class.getDeclaredField(referenceTyp).get(null));
								// use parent node from refrerence
								bean.setParentNodeId(createNodeIdByXmlValue(ref.getValue()));
							} catch (NoSuchFieldException nsfe) {
<<<<<<< HEAD
								// UAReferences, falls ReferenceType standardm��ig nicht vorhanden ist, pr�fen
=======
								// UAReferences, falls ReferenceType standardmäßig nicht vorhanden ist, prüfen
>>>>>>> 895f544a9595e6f7ac85742d15e36811a774868e
								// ob in eigener Liste ein Eintrag vorhanden ist und ggf neue NodeId erzeugen,
								// andernfalls Fehler/Abbruch
								if (containsSpezificReferenceType(referenceTyp)) {
									bean.setReferenceType(createNodeIdByXmlValue(referenceTyp));
									// use parent node from refrerence
									bean.setParentNodeId(createNodeIdByXmlValue(ref.getValue()));
								} else {
									throw new Exception(referenceTyp + " not found ");
								}
							} catch (Exception ex) {
								Logger.getLogger(getClass().getName()).log(Level.SEVERE, referenceTyp + "  not found",
										ex);
								throw ex;
							}
						} else {
							this.getAdditionalReferences()
									.add(bean.createTypeEncoding(createNodeIdByXmlValue(ref.getValue()),
											ref.getAttributeValue(IS_FORWARD), this.namespaceTable));
						}
						break;
					case HAS_MODELLING_RULE:
						bean.setHasModellingRule(createNodeIdByXmlValue(ref.getValue()));
						break;
					case HAS_SUBTYPE:
					case ORGANIZES:
					case HAS_PROPERTY:
					case HAS_COMPONENT:
						if (ref.getAttributeValue(IS_FORWARD) != null
								&& !Boolean.valueOf(ref.getAttributeValue(IS_FORWARD))) {
<<<<<<< HEAD
							// ==>use entry f�r reference type
=======
							// ==>use entry für reference type
>>>>>>> 895f544a9595e6f7ac85742d15e36811a774868e
							String referenceTyp = ref.getAttributeValue(REFERENCE_TYPE);
							// HAS_COMPONENT, HAS_ORDERED_COMPONENT,...
							try {
								bean.setReferenceType(
										(NodeId) Identifiers.class.getDeclaredField(referenceTyp).get(null));
								// use parent node from refrerence
								bean.setParentNodeId(createNodeIdByXmlValue(ref.getValue()));
							} catch (NoSuchFieldException nsfe) {
<<<<<<< HEAD
								// UAReferences, falls ReferenceType standardm��ig nicht vorhanden ist, pr�fen
=======
								// UAReferences, falls ReferenceType standardmäßig nicht vorhanden ist, prüfen
>>>>>>> 895f544a9595e6f7ac85742d15e36811a774868e
								// ob in eigener Liste ein Eintrag vorhanden ist und ggf neue NodeId erzeugen,
								// andernfalls Fehler/Abbruch
								if (containsSpezificReferenceType(referenceTyp)) {
									bean.setReferenceType(createNodeIdByXmlValue(referenceTyp));
									// use parent node from refrerence
									bean.setParentNodeId(createNodeIdByXmlValue(ref.getValue()));
								} else {
									throw new Exception(referenceTyp + " not found ");
								}
							} catch (Exception ex) {
								Logger.getLogger(getClass().getName()).log(Level.SEVERE, referenceTyp + "  not found",
										ex);
								throw ex;
							}
						} else {
							// add base interface type
							// *********************************************************************
							// remove after OCP UA nodeset2.xml model is in order
							NodeId interfaceType = new NodeId(0, 17602);
							NodeId interfaceTypeFolder = new NodeId(0, 17708);
							NodeId targetId = createNodeIdByXmlValue(ref.getValue());
							String isforward = "true";
							if (ref.getAttributeValue(IS_FORWARD) != null) {
								isforward = ref.getAttributeValue(IS_FORWARD);
							}

							if (interfaceTypeFolder.equals(bean.getNodeId()) && interfaceType.equals(targetId)) {
								getAdditionalReferences().add(bean.createAdditionalReference(
										createNodeIdByXmlValue(ref.getValue()), ref.getAttributeValue(REFERENCE_TYPE),
										isforward, this.namespaceTable, this));
							}
							// *********************************************************************
						}
						break;
					default:
						String isforward = "true";
						if (ref.getAttributeValue(IS_FORWARD) != null) {
							isforward = ref.getAttributeValue(IS_FORWARD);
						}

//						NodeId interfaceType = new NodeId(0, 17602);
//						NodeId targetId = createNodeIdByXmlValue(ref.getValue());
//						if(interfaceType.equals(targetId)) {
//							System.out.println("");
//						}

						if (bean.getReferenceType() == null && !Boolean.parseBoolean(isforward)
								&& bean.getParentNodeId() != null) {
							try {
								bean.setReferenceType(createNodeIdByXmlValue(ref.getAttributeValue(REFERENCE_TYPE)));
							} catch (IllegalArgumentException ex) {
								Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage());
							}
						} else {
							getAdditionalReferences().add(bean.createAdditionalReference(
									createNodeIdByXmlValue(ref.getValue()), ref.getAttributeValue(REFERENCE_TYPE),
									isforward, this.namespaceTable, this));
						}
						break;
					}
					// switch (ref.getAttributeValue(REFERENCE_TYPE)) {
					// case HAS_TYPE_DEFINITION: {
					// String val = ref.getValue();
					// int nsTypeDef = getMappedNamespaceByNodeId(val);
					// String nodeIdTypDef = parseXmlNodeId(val);
					// typeDefinition = new NodeId(nsTypeDef, nodeIdTypDef);
					// break;
					// }
					// }
				}
				break;
			}
		}
		return bean;
	}

	private void createAliasDatatypes(Element element) throws Exception {
		for (Object el : element.getChildren()) {
			NodeId nid = NodeId.parseNodeId(((Element) el).getValue());
			addSpezificDataTypeMapping(((Element) el).getAttributeValue(ALIAS), nid);
			addSpezificDataTypeMapping(((Element) el).getValue(), nid);
			// addSpezificDataType();
		}
	}

	private void addUaDataType(Element element) throws Exception {
		NodeSetBean bean = parseElement(NodeClass.DataType, element);
		DataTypeAttributes attributes = createDataTypeAttributes(bean.getDisplayName(), bean.getDescription());
		addUaNode(bean, attributes);
//		AddNodesItem item = createNodesItem(bean, attributes);
//		getNodesItemList().add(item);
//		send2Opc();
		/*
		 * HBS add specific datatypes from alias
		 */
		NodeId nid = NodeId.parseNodeId(element.getAttributeValue(NODE_ID));
		addSpezificDataTypeMapping(bean.getDisplayName(), nid);
		addSpezificDataTypeMapping(element.getAttributeValue(NODE_ID), nid);
	}

	private void addUaMethod(Element element) throws Exception {
		NodeSetBean bean = parseElement(NodeClass.Method, element);
		MethodAttributes attributes = createMethodAttributes(bean.getDisplayName(), bean.getDescription());
		addUaNode(bean, attributes);
		// AddNodesItem item = createNodesItem(bean, attributes);
//		getNodesItemList().add(item);
//		send2Opc();
	}

	private void addUaObject(Element element) throws Exception {
		NodeSetBean bean = parseElement(NodeClass.Object, element);
		ObjectAttributes attributes = createObjectAttributes(bean.getDisplayName(), bean.getDescription());
		addUaNode(bean, attributes);
//		AddNodesItem item = createNodesItem(bean, oa);
//		getNodesItemList().add(item);
//		send2Opc();
	}

	private void addUaObjectType(Element element) throws Exception {
		NodeSetBean bean = parseElement(NodeClass.ObjectType, element);
		ObjectTypeAttributes attributes = createObjectTypeAttributes(bean.getDisplayName(), bean.getDescription(), element);
		addUaNode(bean, attributes);
//		AddNodesItem item = createNodesItem(bean, attributes);
//		getNodesItemList().add(item);
//		send2Opc();
	}

	private void addUaReferenceType(Element element) throws Exception {
		NodeSetBean bean = parseElement(NodeClass.ReferenceType, element);
		ReferenceTypeAttributes attributes = createReferenceTypeAttributes(bean);
		addUaNode(bean, attributes);
//		AddNodesItem item = createNodesItem(bean, rta);
//		getNodesItemList().add(item);
//		send2Opc();
		addSpezificReferenceType(element.getAttributeValue(NODE_ID));
	}

	private void addUaVariable(Element element) throws Exception {
		NodeSetBean bean = parseElement(NodeClass.Variable, element);
		// referenceTypeId = Identifiers.HasProperty;
		VariableAttributes attributes = createVariableAttributes(bean.getDisplayName(), bean.getDescription(), element);
		addUaNode(bean, attributes);
//		AddNodesItem item = createNodesItem(bean, attributes);
//		getNodesItemList().add(item);
//		send2Opc();
	}

	private void addUaVariableType(Element element) throws Exception {
		NodeSetBean bean = parseElement(NodeClass.VariableType, element);
		VariableTypeAttributes attributes = createVariableTypeAttributes(bean.getDisplayName(), bean.getDescription(),
				element);
		addUaNode(bean, attributes);
	}

	private int getMappedNamespaceByNodeId(int namespaceIndex) throws Exception {
		try {
			if (namespaceIndex != 0) {
				return getMappedNamespace(namespaceIndex);
			}
		} catch (Exception ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
			throw ex;
		}
		return namespaceIndex;
	}

	protected NodeId createNodeIdByXmlValue(String value) throws Exception {
		if (value != null) {
			NodeId tmp = NodeId.parseNodeId(value);
			return NodeIdUtil.createNodeId(getMappedNamespaceByNodeId(tmp.getNamespaceIndex()), tmp.getValue());
		}
		return null;
	}

	protected NodeId getSpezificDataType(String dataType) {
		NodeId retVal = null;
		if (spezificDataTypeMapping != null) {
			retVal = spezificDataTypeMapping.get(dataType);
		}
		return retVal;
	}

	public void importNodeSet(OPCUADriverServer opcServer, String path) throws Exception {
		this.opcServer = opcServer;
		try {
			readXmlDocument(path);
			Studio_ResourceManager.persistInformationModelDetails();
		} catch (Exception e) {
			throw e;
		}
	}
}
