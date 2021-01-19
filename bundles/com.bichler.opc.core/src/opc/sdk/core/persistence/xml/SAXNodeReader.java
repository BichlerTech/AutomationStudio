package opc.sdk.core.persistence.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.DiagnosticInfo;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.UnsignedLong;
import org.opcfoundation.ua.builtintypes.UnsignedShort;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.builtintypes.XmlElement;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AddNodesItem;
import org.opcfoundation.ua.core.AddReferencesItem;
import org.opcfoundation.ua.core.Argument;
import org.opcfoundation.ua.core.DataTypeAttributes;
import org.opcfoundation.ua.core.EnumValueType;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.MethodAttributes;
import org.opcfoundation.ua.core.NodeAttributes;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ObjectAttributes;
import org.opcfoundation.ua.core.ObjectTypeAttributes;
import org.opcfoundation.ua.core.ReferenceNode;
import org.opcfoundation.ua.core.ReferenceTypeAttributes;
import org.opcfoundation.ua.core.VariableAttributes;
import org.opcfoundation.ua.core.VariableTypeAttributes;
import org.opcfoundation.ua.encoding.EncoderContext;
import org.opcfoundation.ua.encoding.EncodingException;
import org.opcfoundation.ua.utils.MultiDimensionArrayUtils;

import opc.sdk.core.application.OPCEntry;
import opc.sdk.core.application.operation.ICancleOperation;
import opc.sdk.core.context.StringTable;
import opc.sdk.core.enums.BuiltinType;
import opc.sdk.core.informationmodel.xml.NodeType;
import opc.sdk.core.informationmodel.xml.NodesCategoryTags;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.NodeIdUtil;
import opc.sdk.core.types.TypeTable;
import opc.sdk.core.utils.OPCDateFormat;

/**
 * SAX Parser to import OPC Nodes in a Servers address space.
 * 
 * @author Thomas Z&ouml;bauer
 * 
 */
public class SAXNodeReader {
	private static final String CACHEDVALISNULLL = "Cached Value Element instance is null!";
	private static final String EXTENSIONOBJECT = "ExtensionObject";
	private static final String LOCALIZEDTEXT = "LocalizedText";
	private static final String LISTOF = "ListOf";
	private static final String CLASSNAME = "className";
	/**
	 * Flag to read a Node Tag.
	 */
	private boolean isNode = false;
	/**
	 * Flag to read a AccessLevel Tag.
	 */
	private boolean isAccessLevel = false;
	/**
	 * Flag to read a ArrayDimensions Tag.
	 */
	private boolean isArrayDimensions = false;
	private boolean isArgument = false;
	/**
	 * Flag to read a BrowseName Tag.
	 */
	private boolean isBrowseName = false;
	/**
	 * Flag to read a DataType Tag.
	 */
	private boolean isDataType = false;
	/**
	 * Flag to read a Description Tag.
	 */
	private boolean isDescription = false;
	/**
	 * Flag to read a DisplayName Tag.
	 */
	private boolean isDisplayName = false;
	/**
	 * Flag to read a EventNotifier Tag.
	 */
	private boolean isEventNotifier = false;
	/**
	 * Flag to read a Executeable Tag.
	 */
	private boolean isExecutable = false;
	private boolean isExtensionObject = false;
	/**
	 * Flag to read a UserExecuteable Tag.
	 */
	private boolean isUserExecuteable = false;
	/**
	 * Flag to read a Historizing Tag.
	 */
	private boolean isHistorizing = false;
	private boolean isListOfValue = false;
	/**
	 * Flag to read a InverseName Tag.
	 */
	private boolean isInverseName = false;
	/**
	 * Flag to read a IsAbstract Tag.
	 */
	private boolean isAbstract = false;
	/**
	 * Flag to read a MinimumSamplingInterval Tag.
	 */
	private boolean isMinimumSamplingInterval = false;
	/**
	 * Flag to read a NodeClass Tag.
	 */
	private boolean isNodeClass = false;
	/**
	 * Flag to read a NodeId Tag.
	 */
	private boolean isNodeId = false;
	/**
	 * Flag to read a Reference Tag.
	 */
	private boolean isReference = false;
	/**
	 * Flag to read a Symmetric Tag.
	 */
	private boolean isSymmetric = false;
	/**
	 * Flag to read a UserWriteMask Tag.
	 */
	private boolean isUserWriteMask = false;
	/**
	 * Flag to read a UserAccessLevel Tag.
	 */
	private boolean isUserAccessLevel = false;
	/**
	 * Flag to read a Value Tag.
	 */
	private boolean isValue = false;
	/**
	 * Flag to read a ValueValue Tag.
	 */
	private boolean isValueUnderValue = false;
	/**
	 * Flag to read a Type of a Value Tag.
	 */
	private boolean isTypeOfValue = false;
	/**
	 * Flag to read a ValueRank Tag.
	 */
	private boolean isValueRank = false;
	/**
	 * Flag to read a WriteMask Tag.
	 */
	private boolean isWriteMask = false;
	/**
	 * Flag to read a Identifier Tag.
	 */
	private boolean isIdentifier = false;
	/**
	 * Flag to read a NamespaceIndex Tag.
	 */
	private boolean isNamespaceIndex = false;
	/**
	 * Flag to read a Name Tag.
	 */
	private boolean isName = false;
	/**
	 * Flag to read <LocalizedText> Tags
	 */
	private boolean isLocalizedText = false;
	/**
	 * Flag to read a Locale Tag.
	 */
	private boolean isLocale = false;
	/**
	 * Flag to read a Text Tag.
	 */
	private boolean isText = false;
	/**
	 * Flag to read a Inverse Tag.
	 */
	private boolean isInverse = false;
	/**
	 * Flag to read a ReferenceTypeId Tag.
	 */
	private boolean isReferenceTypeId = false;
	/**
	 * Flag to read a TargetId Tag.
	 */
	private boolean isTargetId = false;
	/**
	 * Flag to read a ReferenceNode Tag.
	 */
	private boolean isReferenceNode = false;
	/**
	 * Flag to read a NamespaceUris Tag.
	 */
	private boolean isNamespaceUris = false;
	/**
	 * Flag to read a ServerUris Tag.
	 */
	private boolean isServerUris = false;
	/*
	 * we need to store bytestring value, because the value can be combined by some
	 * events
	 */
	private String byteStringtoParse = "";
	/**
	 * Flag to read a String Tag.
	 */
	private boolean isString = false;
	/** Parent Tag is a QualifiedName Tag */
	private int cachedQualifiedName;
	/** Parent tag is a LocalizedText Tag */
	private Locale cachedLocale = null;
	/** List to store all ReferenceNodes */
	private List<ReferenceNode> cachedReferences = null;
	/** The current cached ReferenceNode to import */
	private ReferenceNode cachedReferenceNode = null;
	/** An AddNodesItem to create a new Node */
	private AddNodesItem addNodesItem = null;
	/** Attributes of a Node to cache */
	private NodeAttributes nodeElementAttributes = null;
	/** The ServerNamespace Table to import */
	private NamespaceTable serverNamespaceTable = null;
	/** A Typetable from the imported Node Types */
	private TypeTable serverTypeTable = null;
	/** A ServerUri Table to import */
	private StringTable serverUriTable = null;
	/** Map to store all Nodes to create with an AddNodes Service */
	private Map<ExpandedNodeId, AddNodesItem> allNodeItems = null;
	/** List to store all References to create with an AddReference Service */
	private List<AddReferencesItem> allReferenceItems = null;
	/** List to store all Namespaces */
	private NamespaceTable namespacesFromFile = null;
	/** Map to store all References from an imported Node */
	private Map<ExpandedNodeId, List<ReferenceNode>> referencesPerNode = null;
	private List listOfValues = null;
	private StartElement complexElement = null;
	private Argument cachedArgument = null;
	private ICancleOperation progressMonitor;
	/**
	 * Flag to read a ValueValueValue Tag.
	 * 
	 */
	private List<Boolean> isElementUnderMatrix = new ArrayList<>();
	private List<Boolean> isExtensionObjectTag = new ArrayList<>();
	/**
	 * A cached Value Tag to import
	 * 
	 */
	private List<StartElement> cachedValueElement = new ArrayList<>();
	/**
	 * Value of an array
	 * 
	 */
	private List<List<Object>> simpleArrayValue = new ArrayList<>();
	private List<Object> extObjInstance = new ArrayList<>();
	private List<StartElement> cachedExtensionValueElement = new ArrayList<>();
	/**
	 * Flag to read a Matrix Tag.
	 */
	// private boolean isMatrix = false;
	/**
	 * Flag to read a ValueValueArrayDimension Tag.
	 * 
	 */
	private List<Boolean> isDimensionsUnderMatrix = new ArrayList<>();
	/**
	 * REC
	 */
	private List<List<Integer>> cachedDimensions = new ArrayList<>();
	/**
	 * REC
	 */
	private List<StartElement> cachedElementUnderMatrixType = new ArrayList<>();
	private int currentline = 0;
	private int lastline = 0;
	private boolean isEnumValueType;
	private LocalizedText enumVDescription;
	private LocalizedText enumVDisplayName;
	private Long enumVValue;

	/**
	 * default constructor
	 */
	public SAXNodeReader() {
	}

	/**
	 * SAX Parser to import OPC Nodes. This parser uses the server namespace table
	 * and the server uri table to append maybe new read namespace uris or server
	 * uris. The server typetable is used, to fetch existing hierachical references.
	 * 
	 * @param NamespaceTable Server�s NamespaceTable to add the imported
	 *                       Namespaces.
	 * @param ServerTable    ServerUriTable to add the imported ServerUris.
	 * @param TypeTable      Server�s TypeTable to add the imported Types.
	 */
	public SAXNodeReader(NamespaceTable namespaceTable, StringTable serverTable, TypeTable typeTable) {
		this.serverNamespaceTable = namespaceTable;
		this.serverUriTable = serverTable;
		this.serverTypeTable = typeTable;
		this.allNodeItems = new HashMap<>();
		this.allReferenceItems = new ArrayList<>();
		this.namespacesFromFile = new NamespaceTable();
		this.referencesPerNode = new HashMap<>();
		// init simple values
		this.cachedDimensions = new ArrayList<>();
	}

	/**
	 * Creates the OPC Nodes from the XML file. Returns an Entry of AddNodesItem and
	 * AddReferencesItem, which contains all Nodes and References to add. Returns
	 * Null if an error occures.
	 * 
	 * @param xmlFilePath
	 * @return
	 */
	public OPCEntry<List<AddNodesItem>, List<AddReferencesItem>> createNodes(String xmlFilePath) {
		XMLEventReader reader = null;
		File fileToParse = null;
		FileInputStream fis = null;
		try {
			fileToParse = new File(xmlFilePath);
			// open the document
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			fis = new FileInputStream(fileToParse);
			reader = inputFactory.createXMLEventReader(fis, "UTF-8");
			while (reader.hasNext()) {
				if (checkMonitorChancled()) {
					return null;
				}
				// reads element
				XMLEvent evt = null;
				try {
					evt = reader.nextEvent();
					if (evt == null)
						throw new NullPointerException("Next event is null!");
					this.currentline = evt.getLocation().getLineNumber();
				} catch (XMLStreamException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				}
				if (evt == null)
					throw new NullPointerException("Next event is null!");
				// start element
				if (evt.isStartElement()) {
					StartElement element = evt.asStartElement();
					// Element is a Node Element
					openFlagTag(element);
				}
				// character for data
				else if (evt.isCharacters()) {
					setTagData(evt);
				}
				// end element
				else if (evt.isEndElement()) {
					EndElement element = evt.asEndElement();
					closeFlagTag(element);
				}
				evt = null;
				this.lastline = this.currentline;
			}
			// set the node class to the new addReferencesItem
			completeNodeClassToAllReferenceItems();
			// complete all items
			completeReferencesToAddNodeItems();
			return new OPCEntry<List<AddNodesItem>, List<AddReferencesItem>>(
					new ArrayList<>(this.allNodeItems.values()), new ArrayList<>(this.allReferenceItems));
		}
		// Appea
		catch (NullPointerException npe) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, npe);
			return null;
		} catch (FileNotFoundException | XMLStreamException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
		}
		// dispose the parser
		finally {
			if (reader != null) {
				try {
					reader.close();
					reader = null;
				} catch (XMLStreamException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				}
			}
			if (fis != null) {
				try {
					fis.close();
					fis = null;
				} catch (IOException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				}
			}
			this.dispose();
		}
		return null;
	}

	public void setProgressMonitor(ICancleOperation monitor) {
		this.progressMonitor = monitor;
	}

	private Variant constructDefaultValue() {
		// Variant variant = null;
		// variant = new Variant("");
		return new Variant(null);
	}

	/**
	 * Close the flags because a Tag is closed in the XML file.
	 * 
	 * @param Element Element that is closed.
	 */
	void closeFlagTag(EndElement element) {
		if (NodesCategoryTags.NamespaceUris.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isNamespaceUris = false;
		} else if (NodesCategoryTags.ServerUris.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isServerUris = false;
		} else if (NodesCategoryTags.String.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isString = false;
		}
		if (NodesCategoryTags.Node.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			// add the new item
			try {
				if (this.nodeElementAttributes != null) {
					// if we have no value, so add a default one
					if (this.nodeElementAttributes instanceof VariableAttributes) {
						if (((VariableAttributes) this.nodeElementAttributes).getValue() == null) {
							((VariableAttributes) this.nodeElementAttributes).setValue(constructDefaultValue());
						}
					} else if (this.nodeElementAttributes instanceof VariableTypeAttributes) {
						if (((VariableTypeAttributes) this.nodeElementAttributes).getValue() == null) {
							((VariableTypeAttributes) this.nodeElementAttributes).setValue(constructDefaultValue());
						}
					}
					// array dimensions
					if (this.nodeElementAttributes instanceof VariableAttributes) {
						Integer valueRank = ((VariableAttributes) this.nodeElementAttributes).getValueRank();
						if (valueRank <= 0) {
							((VariableAttributes) this.nodeElementAttributes)
									.setArrayDimensions(new UnsignedInteger[0]);
						}
					} else if (this.nodeElementAttributes instanceof VariableTypeAttributes) {
						Integer valueRank = ((VariableTypeAttributes) this.nodeElementAttributes).getValueRank();
						if (valueRank <= 0) {
							((VariableTypeAttributes) this.nodeElementAttributes)
									.setArrayDimensions(new UnsignedInteger[0]);
						}
					}
				}
				this.addNodesItem.setNodeAttributes(
						ExtensionObject.binaryEncode(this.nodeElementAttributes, EncoderContext.getDefaultInstance()));
			} catch (EncodingException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			}
			this.allNodeItems.put(this.addNodesItem.getRequestedNewNodeId(), this.addNodesItem);
			// prepare to add a new one
			this.isNode = false;
			this.nodeElementAttributes = null;
			this.addNodesItem = null;
		} else if (NodesCategoryTags.Identifier.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isIdentifier = false;
			if (this.isNodeId && this.isValue && this.isValueUnderValue && isTypeOfValue) {
				this.isTypeOfValue = false;
				removeCachedValueElementRec();
			}
		} else if (NodesCategoryTags.NodeId.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isNodeId = false;
		} else if (NodesCategoryTags.NodeClass.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isNodeClass = false;
		} else if (NodesCategoryTags.BrowseName.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isBrowseName = false;
		} else if (NodesCategoryTags.NamespaceIndex.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isNamespaceIndex = false;
		} else if (NodesCategoryTags.Name.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isName = false;
		} else if (NodesCategoryTags.DisplayName.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isDisplayName = false;
			this.cachedLocale = null;
		} else if (NodesCategoryTags.Description.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isDescription = false;
			this.cachedLocale = null;
		} else if (NodesCategoryTags.LocalizedText.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isLocalizedText = false;
			this.cachedLocale = null;
		} else if (NodesCategoryTags.Locale.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isLocale = false;
		} else if (NodesCategoryTags.Text.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isText = false;
			if (this.isLocalizedText && this.isValue && this.isValueUnderValue && this.isTypeOfValue) {
				this.isTypeOfValue = false;
				removeCachedValueElementRec();
			}
		} else if (NodesCategoryTags.References.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isReference = false;
			cacheReferences();
			this.cachedReferences = null;
		} else if (NodesCategoryTags.ReferenceTypeId.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isReferenceTypeId = false;
		} else if (NodesCategoryTags.TargetId.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isTargetId = false;
		} else if (NodesCategoryTags.IsInverse.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isInverse = false;
		} else if (NodesCategoryTags.ReferenceNode.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			if (!ExpandedNodeId.isNull(this.cachedReferenceNode.getTargetId())) {
				this.cachedReferences.add(this.cachedReferenceNode);
			}
			this.isReferenceNode = false;
			this.cachedReferenceNode = null;
		} else if (NodesCategoryTags.WriteMask.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isWriteMask = false;
		} else if (NodesCategoryTags.UserWriteMask.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isUserWriteMask = false;
		} else if (NodesCategoryTags.IsAbstract.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isAbstract = false;
		} else if (NodesCategoryTags.AccessLevel.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isAccessLevel = false;
		} else if (NodesCategoryTags.ArrayDimensions.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isArrayDimensions = false;
		} else if (NodesCategoryTags.DataType.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isDataType = false;
		} else if (NodesCategoryTags.EventNotifier.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isEventNotifier = false;
		} else if (NodesCategoryTags.Executeable.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isExecutable = false;
		} else if (NodesCategoryTags.Historizing.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isHistorizing = false;
		} else if (NodesCategoryTags.InverseName.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isInverseName = false;
		} else if (NodesCategoryTags.MinimumSamplingInterval.name()
				.equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isMinimumSamplingInterval = false;
		} else if (NodesCategoryTags.Symmetric.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isSymmetric = false;
		} else if (NodesCategoryTags.UserExecuteable.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isUserExecuteable = false;
		} else if (NodesCategoryTags.UserAccessLevel.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isUserAccessLevel = false;
		} else if (NodesCategoryTags.Dimensions.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			removeIsDimensionsUnderMatrix();
		}
		// else if
		// (NodesCategoryTags.MATRIX.name().equalsIgnoreCase(element.getName().getLocalPart()))
		// {
		// this.isMatrix = false;
		// }
		else if (NodesCategoryTags.Elements.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			removeElementUnderMatrixRec();
			try {
				this.createArrayValue();
			} catch (Exception e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			}
			removeElementUnderMatrixType();
		} else if (NodesCategoryTags.Value.name().equalsIgnoreCase(element.getName().getLocalPart()) && isValue
				&& isValueUnderValue && !isEnumValueType) {
			boolean isExtObjTag = isExtensionObjectTagRec();
			if (isExtObjTag) {
				removeCachedExtensionElement();
			} else {
				this.isValueUnderValue = false;
			}
		} else if (element.getName().getLocalPart().contains(LISTOF) && isValue && isValueUnderValue) {
			Object t = null;
			if (element.getName().getLocalPart().contains(EXTENSIONOBJECT)) {
				t = this.listOfValues.toArray(new ExtensionObject[0]);
			} else if (element.getName().getLocalPart().contains(LOCALIZEDTEXT)) {
				t = this.listOfValues.toArray(new LocalizedText[0]);
			}
			Variant value;
			if (t == null) {
				value = Variant.NULL;
			} else {
				value = new Variant(t);
			}
			if (this.nodeElementAttributes instanceof VariableAttributes) {
				((VariableAttributes) this.nodeElementAttributes).setValue(value);
			} else if (this.nodeElementAttributes instanceof VariableTypeAttributes) {
				((VariableTypeAttributes) this.nodeElementAttributes).setValue(value);
			}
			this.isListOfValue = false;
			this.listOfValues.clear();
			this.listOfValues = null;
		} else if (NodesCategoryTags.ExtensionObject.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			boolean isExtObjTag = isExtensionObjectTagRec();
			if (isExtObjTag) {
				boolean isElementMatrix = isElementUnderMatrixRec();
				Object object = removeExtensionObject();
				if (isElementMatrix) {
					// it must be an array
					addArrayValue(object);
				} else {
					setValueObject(object);
				}
				removeIsExtensionObjectRec();
			}
		} else if (EXTENSIONOBJECT.equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isExtensionObject = false;
		} else if ("Argument".equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isArgument = false;
			if (this.isExtensionObject) {
				ExtensionObject extObj = null;
				try {
					extObj = ExtensionObject.binaryEncode(this.cachedArgument, EncoderContext.getDefaultInstance());
				} catch (EncodingException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				}
				this.listOfValues.add(extObj);
			}
			this.complexElement = null;
			this.cachedArgument = null;
		} else if (NodesCategoryTags.Value.name().equalsIgnoreCase(element.getName().getLocalPart())
				&& !this.isEnumValueType) {
			byteStringtoParse = "";
			this.isValue = false;
		} else if (NodesCategoryTags.ValueRank.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isValueRank = false;
		} else if (this.isValue && this.isValueUnderValue && !this.isEnumValueType) {
			isExtensionObjectTagRec();
			this.isTypeOfValue = false;
			removeCachedValueElementRec();
		} else if (NodesCategoryTags.EnumValueType.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			EnumValueType enumvType = new EnumValueType();
			enumvType.setDescription(this.enumVDescription);
			enumvType.setDisplayName(this.enumVDisplayName);
			enumvType.setValue(this.enumVValue);
			try {
				this.listOfValues.add(ExtensionObject.binaryEncode(enumvType, EncoderContext.getDefaultInstance()));
				((VariableAttributes) this.nodeElementAttributes)
						.setArrayDimensions(new UnsignedInteger[] { new UnsignedInteger(this.listOfValues.size()) });
			} catch (EncodingException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			}
			this.enumVDescription = null;
			this.enumVDisplayName = null;
			this.enumVValue = null;
			this.isDescription = false;
			this.isDisplayName = false;
			this.cachedLocale = null;
			this.isEnumValueType = false;
		}
	}

	/**
	 * Set the Flags for the given tag of a Node from the file, to get the data
	 * (Attribute of a Node).
	 * 
	 * @param Element Node Tag that is opened.
	 */
	void fetchTagOfNodeAttributes(StartElement element) {
		boolean tmpIsElementUnderMatrix = isElementUnderMatrixRec();
		boolean isExtObjValue = isExtensionObjectTagRec();
		if (this.isExtensionObject) {
			this.complexElement = element.asStartElement();
		}
		if (NodesCategoryTags.AccessLevel.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isAccessLevel = true;
		} else if (NodesCategoryTags.ArrayDimensions.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isArrayDimensions = true;
		} else if (NodesCategoryTags.BrowseName.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isBrowseName = true;
		} else if (NodesCategoryTags.Name.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isName = true;
		} else if (NodesCategoryTags.NamespaceIndex.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isNamespaceIndex = true;
		} else if (NodesCategoryTags.DataType.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isDataType = true;
		} else if (NodesCategoryTags.Description.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isDescription = true;
		} else if (NodesCategoryTags.LocalizedText.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isLocalizedText = true;
			if (this.isValue) {
				addCachedValueElement(element);
			}
		} else if (NodesCategoryTags.Text.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isText = true;
			if (this.isLocalizedText && this.isValue && this.isValueUnderValue) {
				Attribute attr = element
						.getAttributeByName(new QName("http://www.w3.org/2001/XMLSchema-instance", "nil"));
				if (attr != null) {
					if (attr.getValue() != null && Boolean.parseBoolean(attr.getValue())) {
						// here we create an localized Text variable with an blank text
						setValue("");
					}
				}
				this.isTypeOfValue = true;
			}
		} else if (NodesCategoryTags.Locale.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			new QName("http://www.w3.org/2001/XMLSchema-instance", "nil");
			Attribute attr = element.getAttributeByName(new QName("http://www.w3.org/2001/XMLSchema-instance", "nil"));
			if (attr != null) {
				if (attr.getValue() != null && Boolean.parseBoolean(attr.getValue())) {
					this.cachedLocale = getLocale("");
				}
			}
			this.isLocale = true;
		} else if (NodesCategoryTags.DisplayName.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isDisplayName = true;
		} else if (NodesCategoryTags.EventNotifier.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isEventNotifier = true;
		} else if (NodesCategoryTags.Executeable.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isExecutable = true;
		} else if (NodesCategoryTags.Historizing.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isHistorizing = true;
		} else if (NodesCategoryTags.InverseName.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isInverseName = true;
		} else if (NodesCategoryTags.IsAbstract.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isAbstract = true;
		} else if (NodesCategoryTags.MinimumSamplingInterval.name()
				.equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isMinimumSamplingInterval = true;
		} else if (NodesCategoryTags.NodeClass.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isNodeClass = true;
		} else if (NodesCategoryTags.NodeId.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isNodeId = true;
			if (this.isValue) {
				addCachedValueElement(element);
			}
		} else if (NodesCategoryTags.Identifier.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isIdentifier = true;
			if (this.isNodeId && this.isValue && this.isValueUnderValue) {
				this.isTypeOfValue = true;
			}
		} else if (NodesCategoryTags.References.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isReference = true;
		} else if (NodesCategoryTags.ReferenceNode.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isReferenceNode = true;
		} else if (NodesCategoryTags.IsInverse.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isInverse = true;
		} else if (NodesCategoryTags.ReferenceTypeId.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isReferenceTypeId = true;
		} else if (NodesCategoryTags.TargetId.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isTargetId = true;
		} else if (NodesCategoryTags.Symmetric.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isSymmetric = true;
		} else if (NodesCategoryTags.UserWriteMask.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isUserWriteMask = true;
		}
		// else if
		// (NodesCategoryTags.MATRIX.name().equalsIgnoreCase(element.getName().getLocalPart())
		// && isValue
		// && isValueUnderValue)
		// {
		// this.isMatrix = true;
		// }
		else if (NodesCategoryTags.Value.name().equalsIgnoreCase(element.getName().getLocalPart()) && isValue
				&& isExtObjValue) {
			addCachedExtensionObj(element.asStartElement());
		} else if (NodesCategoryTags.Value.name().equalsIgnoreCase(element.getName().getLocalPart()) && isValue) {
			this.isValueUnderValue = true;
			if (isExtObjValue) {
				// prepared for further use
			}
		} else if (NodesCategoryTags.Value.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isValue = true;
		} else if (NodesCategoryTags.ExtensionObject.name().equalsIgnoreCase(element.getName().getLocalPart())
				&& this.isValue) {
			this.isExtensionObject = true;
			addExtensionObjTag();
			String className = null;
			Iterator<?> attr = element.getAttributes();
			while (attr.hasNext()) {
				Attribute attribute = (Attribute) attr.next();
				QName key = attribute.getName();
				if (CLASSNAME.equals(key.getLocalPart())) {
					className = attribute.getValue();
					break;
				}
			}
			if (className != null) {
				try {
					Class<?> extObjClass = Class.forName(className);
					Object obj = extObjClass.newInstance();
					addExtensionObjectInstance(obj);
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				}
			}
		} else if (NodesCategoryTags.Dimensions.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			addIsDimensionUnderMatrix();
			addCachedArrayDimension();
		} else if (NodesCategoryTags.Elements.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			addIsElementUnderMatrix();
			addElementUnderMatrixType(element);
			addArrayElement();
		} else if (NodesCategoryTags.ValueRank.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isValueRank = true;
		} else if (NodesCategoryTags.WriteMask.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isWriteMask = true;
		} else if (NodesCategoryTags.UserAccessLevel.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isUserAccessLevel = true;
		} else if (NodesCategoryTags.UserExecuteable.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isUserExecuteable = true;
		} else if (NodesCategoryTags.EnumValueType.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isEnumValueType = true;
		} else if ((this.isValue && this.isValueUnderValue && tmpIsElementUnderMatrix)
				|| (isValue && isValueUnderValue)) {
			addCachedValueElement(element.asStartElement());
			this.isTypeOfValue = true;
		}
	}

	private void addElementUnderMatrixType(StartElement element) {
		this.cachedElementUnderMatrixType.add(element);
	}

	private StartElement getElementUnderMatrixType() {
		return this.cachedElementUnderMatrixType.get(this.cachedElementUnderMatrixType.size() - 1);
	}

	private void removeElementUnderMatrixType() {
		this.cachedElementUnderMatrixType.remove(this.cachedElementUnderMatrixType.size() - 1);
	}

	/**
	 * Set the Flags, that the tag is opened, which is currently read with the
	 * parser.
	 * 
	 * @param Element An opened Tag from the File.
	 */
	void openFlagTag(StartElement element) {
		if (this.isNode) {
			fetchTagOfNodeAttributes(element);
		} else if (NodesCategoryTags.Node.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isNode = true;
			// create an addnodeitem from its node class
			Iterator<?> iter = element.getAttributes();
			while (iter.hasNext()) {
				String nodeClass = ((Attribute) iter.next()).getValue();
				// attributes for a datatype node
				if (NodeType.DataTypeNode.name().equalsIgnoreCase(nodeClass)) {
					this.nodeElementAttributes = new DataTypeAttributes();
				} else if (NodeType.ReferenceTypeNode.name().equalsIgnoreCase(nodeClass)) {
					this.nodeElementAttributes = new ReferenceTypeAttributes();
				} else if (NodeType.ObjectTypeNode.name().equalsIgnoreCase(nodeClass)) {
					this.nodeElementAttributes = new ObjectTypeAttributes();
				} else if (NodeType.VariableTypeNode.name().equalsIgnoreCase(nodeClass)) {
					this.nodeElementAttributes = new VariableTypeAttributes();
				} else if (NodeType.ObjectNode.name().equalsIgnoreCase(nodeClass)) {
					this.nodeElementAttributes = new ObjectAttributes();
				} else if (NodeType.VariableNode.name().equalsIgnoreCase(nodeClass)) {
					this.nodeElementAttributes = new VariableAttributes();
				} else if (NodeType.MethodNode.name().equalsIgnoreCase(nodeClass)) {
					this.nodeElementAttributes = new MethodAttributes();
				} else {
					Logger.getLogger(getClass().getName()).log(Level.INFO, "Unknown node type!");
				}
				this.addNodesItem = new AddNodesItem();
			}
		} else if (NodesCategoryTags.NamespaceUris.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isNamespaceUris = true;
		} else if (NodesCategoryTags.ServerUris.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isServerUris = true;
		} else if (NodesCategoryTags.String.name().equalsIgnoreCase(element.getName().getLocalPart())) {
			this.isString = true;
		}
	}

	/**
	 * Sets a tag data to the given node attribute, or create collections to cache
	 * information of a node.
	 * 
	 * @param Evt SAX Event to get the data of the Tag.
	 */
	void setTagData(XMLEvent evt) {
		boolean isDimensionUnderMatrix = getIsDimensionUnderMatrix();
		Characters chars = evt.asCharacters();
		if (this.isNamespaceUris && this.isString) {
			int index = this.serverNamespaceTable.getIndex(chars.getData());
			if (index == -1) {
				this.serverNamespaceTable.add(index, chars.getData());
			}
			if (this.namespacesFromFile.getIndex(chars.getData()) < 0) {
				this.namespacesFromFile.add(chars.getData());
			}
		} else if (this.isServerUris && this.isString) {
			List<String> uris = this.serverUriTable.getStrings();
			String uri = chars.getData();
			if (!uris.contains(uri)) {
				this.serverUriTable.getStrings().add(uri);
			}
		}
		// default node attributes (for each node)
		else if (this.isNodeId && this.isIdentifier && !isValue) {
			NodeId nodeId = null;
			try {
				nodeId = NodeId.parseNodeId(chars.getData().trim());
				nodeId = changeNodeId(nodeId);
				this.addNodesItem.setRequestedNewNodeId(this.serverNamespaceTable.toExpandedNodeId(nodeId));
			} catch (IllegalArgumentException iae) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, iae);
				if (this.currentline == this.lastline) {
					NodeId id = null;
					try {
						id = this.serverNamespaceTable.toNodeId(this.addNodesItem.getRequestedNewNodeId());
						String nId = id.toString();
						nId += chars.getData().trim();
						nodeId = NodeId.parseNodeId(nId);
						nodeId = changeNodeId(nodeId);
						ExpandedNodeId enid = this.serverNamespaceTable.toExpandedNodeId(nodeId);
						this.addNodesItem.setRequestedNewNodeId(enid);
					} catch (ServiceResultException e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
					}
				}
			}
		} else if (this.isNodeClass) {
			NodeClass nodeClass = NodeClass.valueOf(chars.getData().split("_")[0]);
			this.addNodesItem.setNodeClass(nodeClass);
		} else if (this.isBrowseName && !isValue) {
			if (this.isNamespaceIndex) {
				this.cachedQualifiedName = Integer.parseInt(chars.getData());
			} else if (this.isName) {
				if (this.currentline == this.lastline) {
					String name = "";
					if (this.addNodesItem.getBrowseName() != null) {
						name = this.addNodesItem.getBrowseName().getName();
					}
					name += chars.getData();
					this.addNodesItem.setBrowseName(new QualifiedName(this.cachedQualifiedName, name));
				} else {
					this.addNodesItem.setBrowseName(new QualifiedName(this.cachedQualifiedName, chars.getData()));
				}
			}
		} else if (this.isDisplayName && !isValue) {
			if (this.isLocale) {
				this.cachedLocale = getLocale(chars.getData());
			} else if (this.isText) {
				if (this.currentline == this.lastline) {
					String name = "";
					if (this.nodeElementAttributes.getDisplayName() != null) {
						name = this.nodeElementAttributes.getDisplayName().getText();
					}
					name += chars.getData();
					this.nodeElementAttributes.setDisplayName(new LocalizedText(name, this.cachedLocale));
				} else {
					this.nodeElementAttributes.setDisplayName(new LocalizedText(chars.getData(), this.cachedLocale));
				}
			}
		} else if (this.isDisplayName && isEnumValueType) {
			if (this.isLocale) {
				this.cachedLocale = getLocale(chars.getData());
			} else if (this.isText) {
				this.enumVDisplayName = new LocalizedText(chars.getData(), this.cachedLocale);
				this.cachedLocale = null;
			}
		} else if (this.isDescription && !isValue) {
			if (this.isLocale) {
				this.cachedLocale = getLocale(chars.getData());
			} else if (this.isText) {
				this.nodeElementAttributes.setDescription(new LocalizedText(chars.getData(), this.cachedLocale));
				this.cachedLocale = null;
			}
		} else if (this.isDescription && isEnumValueType) {
			if (this.isLocale) {
				this.cachedLocale = getLocale(chars.getData());
			} else if (this.isText) {
				this.enumVDescription = new LocalizedText(chars.getData(), this.cachedLocale);
			}
		} else if (this.isLocalizedText && this.isLocale && this.isValue) {
			this.cachedLocale = getLocale(chars.getData());
		} else if (this.isWriteMask && !isValue) {
			this.nodeElementAttributes.setWriteMask(new UnsignedInteger(chars.getData()));
		} else if (this.isUserWriteMask && !isValue) {
			this.nodeElementAttributes.setUserWriteMask(new UnsignedInteger(chars.getData()));
		} else if (this.isReference && !isValue) {
			if (this.isReferenceTypeId) {
				if (this.isIdentifier) {
					NodeId nodeId = null;
					try {
						nodeId = NodeId.parseNodeId(chars.getData());
						nodeId = changeNodeId(nodeId);
						this.cachedReferenceNode.setReferenceTypeId(nodeId);
					} catch (IllegalArgumentException iae) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, iae);
						if (this.currentline == this.lastline) {
							NodeId refTypeId = this.cachedReferenceNode.getReferenceTypeId();
							String reference = refTypeId.toString();
							reference += chars.getData().trim();
							try {
								nodeId = NodeId.parseNodeId(reference);
								nodeId = changeNodeId(nodeId);
								this.cachedReferenceNode.setReferenceTypeId(nodeId);
							} catch (IllegalArgumentException e) {
								Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
							}
						}
					}
				}
			} else if (this.isInverse) {
				this.cachedReferenceNode.setIsInverse(new Boolean(chars.getData()));
			} else if (this.isTargetId) {
				if (this.isIdentifier) {
					NodeId nodeId = null;
					try {
						nodeId = NodeId.parseNodeId(chars.getData());
						nodeId = changeNodeId(nodeId);
						// only if target is reachable
						if (!NodeId.isNull(nodeId)) {
							ExpandedNodeId enid = this.serverNamespaceTable.toExpandedNodeId(nodeId);
							this.cachedReferenceNode.setTargetId(enid);
						}
					} catch (IllegalArgumentException iae) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, iae);
						if (this.currentline == this.lastline) {
							ExpandedNodeId targetId = this.cachedReferenceNode.getTargetId();
							String target = targetId.toString();
							target += chars.getData().trim();
							nodeId = NodeId.parseNodeId(target);
							nodeId = changeNodeId(nodeId);
							// only if target is reachable
							if (!NodeId.isNull(nodeId)) {
								ExpandedNodeId enid = changeNodeId(this.serverNamespaceTable.toExpandedNodeId(nodeId));
								this.cachedReferenceNode.setTargetId(enid);
							}
						}
					}
				}
			} else if (this.isReferenceNode && this.cachedReferenceNode == null) {
				this.cachedReferenceNode = new ReferenceNode();
			} else if (this.cachedReferences == null) {
				this.cachedReferences = new ArrayList<>();
			}
		}
		// custom node attributes
		else if (this.isAbstract && !isValue) {
			setIsAbstract(new Boolean(chars.getData()));
		} else if (this.isAccessLevel && !isValue) {
			setAccessLevel(new UnsignedByte(chars.getData()));
		} else if (this.isArrayDimensions && !isValue) {
			// parse it from string like "{x1,x2,...,n}"
			setArrayDimensions(chars.getData());
		} else if (this.isValueRank && !isValue) {
			try {
				setValueRank(new Integer(chars.getData()));
				if (getArrayDimensions() == null) {
					// set default arraydimensions
					this.setArrayDimensions("{0}");
				}
			} catch (NumberFormatException ex) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
			}
		} else if (isDimensionUnderMatrix && isValueUnderValue && isValue && this.isTypeOfValue) {
			addCachedArrayDimensionValue(chars.getData());
		} else if (this.isValueUnderValue && isValue && isTypeOfValue) {
			// current cached value element
			StartElement cve = getCachedValueElementRec();
			if (cve == null)
				throw new NullPointerException(CACHEDVALISNULLL);
			boolean isExtensionObj = isExtensionObjectTagRec();
			boolean tmpIsElementUnderMatrix = isElementUnderMatrixRec();
			if (cve.getName().getLocalPart().contains(LISTOF)) {
				setListOfValue();
			}
			/**
			 * extension data
			 */
			else if (isExtensionObj && !this.isListOfValue) {
				setExtensionValue(chars.getData());
			} else if (this.isEnumValueType) {
				try {
					this.enumVValue = Long.parseLong(chars.getData());
				} catch (NumberFormatException ex) {
					// print nothing
				}
			}
			/**
			 * values under matrix
			 */
			else if (tmpIsElementUnderMatrix) {
				// it must be an array
				setArrayValue(chars.getData());
			}
			/**
			 * Check for ListOf..... prepare list to store object values
			 * 
			 * particular for method arguments
			 */
			else {
				if (!this.isListOfValue) {
					setValue(chars.getData());
				} else {
					setListOfValue(chars.getData());
				}
			}
		} else if (this.isMinimumSamplingInterval) {
			setMinimumSamplingInterval(new Double(chars.getData()));
		} else if (this.isInverseName && (this.isText || this.isLocale)) {
			setInverseName(chars.getData());
		} else if (this.isHistorizing) {
			setHistorizing(new Boolean(chars.getData()));
		} else if (this.isExecutable) {
			setExecuteable(new Boolean(chars.getData()));
		} else if (this.isEventNotifier) {
			setEventNotifier(new UnsignedByte(chars.getData()));
		} else if (this.isDataType && this.isIdentifier) {
			// change to the correct namespace id
			NodeId nodeId;
			try {
				nodeId = NodeId.parseNodeId(chars.getData().trim());
				nodeId = changeNodeId(nodeId);
				setDataType(nodeId);
			} catch (IllegalArgumentException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			}
		} else if (this.isUserExecuteable) {
			setUserExecuteable(new Boolean(chars.getData()));
		} else if (this.isUserAccessLevel) {
			setUserAccessLevel(new UnsignedByte(chars.getData()));
		} else if (this.isSymmetric) {
			setSymmetric(new Boolean(chars.getData()));
		}
	}

	private void addCachedArrayDimension() {
		this.cachedDimensions.add(new ArrayList<Integer>());
	}

	private void addCachedArrayDimensionValue(String value) {
		try {
			value = value.replace("\n", "").trim();
			if (value.isEmpty())
				return;
			Integer dim = Integer.parseInt(value);
			List<Integer> arraydimensions = getCachedArrayDimension();
			arraydimensions.add(dim);
		} catch (NumberFormatException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getLocalizedMessage());
		}
	}

	private List<Integer> getCachedArrayDimension() {
		return this.cachedDimensions.get(this.cachedDimensions.size() - 1);
	}

	private List<Integer> removeCachedArrayDimension() {
		return this.cachedDimensions.remove(this.cachedDimensions.size() - 1);
	}

	private void addArrayElement() {
		this.simpleArrayValue.add(new ArrayList<>());
	}

	private void addArrayValue(Object value) {
		List<Object> values = this.simpleArrayValue.get(this.simpleArrayValue.size() - 1);
		values.add(value);
	}

	private void addExtensionObjectInstance(Object obj) {
		this.extObjInstance.add(obj);
	}

	private void addIsElementUnderMatrix() {
		this.isElementUnderMatrix.add(true);
	}

	private void addExtensionObjTag() {
		this.isExtensionObjectTag.add(true);
	}

	private void addCachedExtensionObj(StartElement element) {
		this.cachedExtensionValueElement.add(element);
	}

	private void addCachedValueElement(StartElement element) {
		this.cachedValueElement.add(element);
	}

	private void addIsDimensionUnderMatrix() {
		this.isDimensionsUnderMatrix.add(true);
	}

	private void createArrayValue() {
		boolean tmpIsExtensionObject = isExtensionObjectTagRec();
		StartElement type = getElementUnderMatrixType();
		// check bytestring
		Class<?> typeClass = null;
		if (type != null) {
			if (type != null) {
				Iterator<?> iter = type.getAttributes();
				while (iter.hasNext()) {
					Attribute attribute = (Attribute) iter.next();
					if (CLASSNAME.equals(attribute.getName().getLocalPart())) {
						if ("byte".equals(attribute.getValue())) {
							typeClass = byte[].class;
						}
					}
				}
			}
		}
		List<Integer> arraydimensions = removeCachedArrayDimension();
		if (typeClass == null && arraydimensions.isEmpty()) {
			return;
		}
		int[] darray = new int[arraydimensions.size()];
		for (int i = 0; i < arraydimensions.size(); i++) {
			darray[i] = arraydimensions.get(i);
		}
		Object array = null;
		List<Object> values = getArrayValue();
		if (typeClass == null) {
			// match to array
			if (!values.isEmpty()) {
				if (values.get(0) != null) {
					typeClass = values.get(0).getClass();
					/** create the multidimensional array */
				}
			} else {
				if (type != null) {
					Iterator<?> iter = type.getAttributes();
					while (iter.hasNext()) {
						Attribute attribute = (Attribute) iter.next();
						if (CLASSNAME.equals(attribute.getName().getLocalPart())) {
							try {
								typeClass = Class.forName(attribute.getValue());
							} catch (ClassNotFoundException e) {
								Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
							}
						}
					}
				}
			}
		}
		if (typeClass != null) {
			if (values != null) {
				Object src;
				// ValueRank: Scalar BuiltIn: ByteString reduce from byte[0][0]
				// to byte[0]
				if (typeClass == byte[].class && darray.length == 0) {
					array = values.get(0);
				} else {
					src = values.toArray();
					darray = MultiDimensionArrayUtils.getArrayLengths(src);
					array = MultiDimensionArrayUtils.demuxArray(src, darray, typeClass);
				}
			}
		}
		removeArrayValue();
		/** now clear value */
		Variant val = new Variant(array);
		if (tmpIsExtensionObject) {
			setExtensionArrayValue(val);
		} else {
			if (this.nodeElementAttributes instanceof VariableAttributes) {
				((VariableAttributes) this.nodeElementAttributes).setValue(val);
			} else if (this.nodeElementAttributes instanceof VariableTypeAttributes) {
				((VariableTypeAttributes) this.nodeElementAttributes).setValue(val);
			}
		}
	}

	private List<Object> getArrayValue() {
		return this.simpleArrayValue.get(this.simpleArrayValue.size() - 1);
	}

	private boolean getIsDimensionUnderMatrix() {
		boolean isDimensionUnderMatrix = false;
		if (!this.isDimensionsUnderMatrix.isEmpty()) {
			isDimensionUnderMatrix = this.isDimensionsUnderMatrix.get(this.isDimensionsUnderMatrix.size() - 1);
		}
		return isDimensionUnderMatrix;
	}

	private StartElement getCachedExtensionElementRec() {
		StartElement cve = null;
		if (!this.cachedExtensionValueElement.isEmpty()) {
			cve = this.cachedExtensionValueElement.get(this.cachedExtensionValueElement.size() - 1);
		}
		return cve;
	}

	private StartElement getCachedValueElementRec() {
		StartElement cve = null;
		if (!this.cachedValueElement.isEmpty()) {
			cve = this.cachedValueElement.get(this.cachedValueElement.size() - 1);
		}
		return cve;
	}

	private boolean isElementUnderMatrixRec() {
		boolean isUnderMatrix = false;
		if (!this.isElementUnderMatrix.isEmpty()) {
			isUnderMatrix = this.isElementUnderMatrix.get(this.isElementUnderMatrix.size() - 1);
		}
		return isUnderMatrix;
	}

	private boolean isExtensionObjectTagRec() {
		boolean isExtensionObj = false;
		if (!this.isExtensionObjectTag.isEmpty()) {
			isExtensionObj = this.isExtensionObjectTag.get(this.isExtensionObjectTag.size() - 1);
		}
		return isExtensionObj;
	}

	private void setListOfValue() {
		this.isListOfValue = true;
		StartElement cve = getCachedValueElementRec();
		if (cve == null)
			throw new NullPointerException(CACHEDVALISNULLL);
		String listType = cve.getName().getLocalPart().split(LISTOF)[1];
		createListOfValues(listType);
	}

	private void setExtensionArrayValue(Variant array) {
		StartElement cee = getCachedExtensionElementRec();
		if (cee == null)
			throw new NullPointerException("Cached Extension Element instance is null!");
		Iterator<?> iterator = cee.getAttributes();
		String fieldname = null;
		while (iterator.hasNext()) {
			Attribute next = (Attribute) iterator.next();
			if ("fieldname".equals(next.getName().getLocalPart())) {
				fieldname = next.getValue();
				break;
			}
		}
		Object extObj = getExtensionObjectInstance();
		if (extObj == null)
			throw new NullPointerException("Extension Object instance is null!");
		Method[] methods = extObj.getClass().getMethods();
		// set value on extension object
		if (methods != null) {
			invokeMethods(methods, fieldname, extObj, array);
		}
	}

	private void setExtensionValue(String value) {
		StartElement cve = getCachedValueElementRec();
		if (cve == null)
			throw new NullPointerException(CACHEDVALISNULLL);
		String xType = cve.getName().getLocalPart();
		Variant val = constructValueAttribute(xType, value);
		StartElement cee = getCachedExtensionElementRec();
		if (cee == null)
			throw new NullPointerException("Cached Extension Element instance is null!");
		Iterator<?> iterator = cee.getAttributes();
		String fieldname = null;
		while (iterator.hasNext()) {
			Attribute next = (Attribute) iterator.next();
			if ("fieldname".equalsIgnoreCase(next.getName().getLocalPart())) {
				fieldname = next.getValue();
				break;
			}
		}
		Object extObj = getExtensionObjectInstance();
		if (extObj == null)
			throw new NullPointerException("Extension Object instance is null!");
		Method[] methods = extObj.getClass().getMethods();
		// set value on extension object
		if (methods != null) {
			invokeMethods(methods, fieldname, extObj, val);
		}
	}

	private void invokeMethods(Method[] methods, String fieldname, Object extObj, Variant val) {
		for (Method method : methods) {
			String methodname = method.getName();
			if (("set" + fieldname).equals(methodname)) {
				try {
					method.invoke(extObj, val.getValue());
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				}
				break;
			}
		}
	}

	/**
	 * Cache all references from a node.
	 */
	private void cacheReferences() {
		switch (this.addNodesItem.getNodeClass()) {
		case VariableType:
		case DataType:
		case Method:
		case ObjectType:
		case View:
		case ReferenceType:
			this.addNodesItem.setTypeDefinition(null);
			break;
		case Unspecified:
			break;
		case Variable:
		case Object:
			Node referenceNode = new Node();
			referenceNode.setReferences(this.cachedReferences.toArray(new ReferenceNode[this.cachedReferences.size()]));
			ExpandedNodeId type = referenceNode.findTarget(Identifiers.HasTypeDefinition, false);
			this.addNodesItem.setTypeDefinition(type);
			break;
		}
		// cache the references of a node
		if (!this.cachedReferences.isEmpty()) {
			this.referencesPerNode.put(this.addNodesItem.getRequestedNewNodeId(), this.cachedReferences);
		}
	}

	/**
	 * Changes the NodeId from the current File (for example: ns = 1; i = 109) to
	 * the NodeId used on the Server. This will be done with the namespace indizes
	 * of the file, which will be read and the current NamespaceTable on the server.
	 * 
	 * @param NodeId NodeId to change to compare with the Server�s NamespaceTable.
	 * @return Changed NodeId that it is suitable to insert it to the Server�s
	 *         AddressSpace.
	 */
	private NodeId changeNodeId(NodeId nodeId) {
		try {
			String currentNamespace;
			int namespaceIndex = nodeId.getNamespaceIndex();
			currentNamespace = this.namespacesFromFile.getUri(namespaceIndex);
			return NodeIdUtil.create(nodeId.getValue(), currentNamespace, this.serverNamespaceTable);
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			return NodeId.NULL;
		}
	}

	/**
	 * Changes the NodeId from the current File (for example: ns = 1; i = 109) to
	 * the NodeId used on the Server. This will be done with the namespace indizes
	 * of the file, which will be read and the current NamespaceTable on the server.
	 * 
	 * @param NodeId NodeId to change to compare with the Server�s NamespaceTable.
	 * @return Changed NodeId that it is suitable to insert it to the Server�s
	 *         AddressSpace.
	 */
	public ExpandedNodeId changeNodeId(ExpandedNodeId nodeId) {
		try {
			String currentNamespace;
			int namespaceIndex = nodeId.getNamespaceIndex();
			currentNamespace = this.namespacesFromFile.getUri(namespaceIndex);
			return new ExpandedNodeId(nodeId.getServerIndex(), currentNamespace, nodeId.getValue(),
					this.namespacesFromFile);
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			return ExpandedNodeId.NULL;
		}
	}

	/**
	 * Recursive Method of fillReferencesToAdd().
	 * 
	 * @param ReferenceId       Id of the recursive parent Reference.
	 * @param OriginalReference Id of the original impored reference id, that has to
	 *                          be changed when it is different to the Server�s
	 *                          NamespaceTable.
	 * @param IsInverse         Direction of the reference node.<br>
	 *                          TRUE is an Inverse Reference<br>
	 *                          FALSE is a Forward Reference.
	 * @return TRUE if the Reference is hierachical, otherwise FALSE.
	 */
	private boolean checkHierachical(NodeId referenceId, NodeId originalReference, boolean isInverse) {
		// is reference type of existing hieracical reference
		boolean isHierachical = this.serverTypeTable.isTypeOf(referenceId, Identifiers.HierarchicalReferences);
		if (isHierachical || Identifiers.HierarchicalReferences.equals(referenceId)) {
			// reference is a child of hierachical reference
			return true;
		}
		// check parent
		else if (isInverse) {
			ExpandedNodeId refId = this.serverNamespaceTable.toExpandedNodeId(referenceId);
			// ExpandedNodeId refId =
			List<ReferenceNode> references = this.referencesPerNode.get(refId);
			if (references != null) {
				for (ReferenceNode refNode : references) {
					NodeId targetId = null;
					try {
						targetId = this.serverNamespaceTable.toNodeId(refNode.getTargetId());
					} catch (ServiceResultException e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
					}
					return checkHierachical(targetId, originalReference, refNode.getIsInverse());
				}
			}
		}
		return false;
	}

	/**
	 * Completes the AddReferencesItem with filling the target NodeClass.
	 */
	private void completeNodeClassToAllReferenceItems() {
		for (AddReferencesItem addRefItem : this.allReferenceItems) {
			NodeClass targetClass = this.allNodeItems.get(addRefItem.getTargetNodeId()).getNodeClass();
			addRefItem.setTargetNodeClass(targetClass);
		}
	}

	/**
	 * Completes the AddNodesItem with the parent nodeId and the referencetypeId to
	 * the parent. Add all other additional references to the service result.
	 */
	private void completeReferencesToAddNodeItems() {
		for (Entry<ExpandedNodeId, List<ReferenceNode>> entry : this.referencesPerNode.entrySet()) {
			AddNodesItem item = this.allNodeItems.get(entry.getKey());
			for (ReferenceNode refNode : entry.getValue()) {
				if (checkHierachical(refNode.getReferenceTypeId(), refNode.getReferenceTypeId(), refNode.getIsInverse())
						&& item.getReferenceTypeId() == null && item.getParentNodeId() == null) {
					item.setReferenceTypeId(refNode.getReferenceTypeId());
					item.setParentNodeId(refNode.getTargetId());
				} else if (refNode.getReferenceTypeId().compareTo(Identifiers.HasEncoding) == 0
						&& item.getReferenceTypeId() == null && item.getParentNodeId() == null) {
					item.setReferenceTypeId(refNode.getReferenceTypeId());
					item.setParentNodeId(refNode.getTargetId());
				} else if (!Identifiers.HasTypeDefinition.equals(refNode.getReferenceTypeId())) {
					AddReferencesItem addReferenceItem = new AddReferencesItem();
					addReferenceItem.setIsForward(!refNode.getIsInverse());
					addReferenceItem.setReferenceTypeId(refNode.getReferenceTypeId());
					try {
						addReferenceItem
								.setSourceNodeId(this.serverNamespaceTable.toNodeId(item.getRequestedNewNodeId()));
					} catch (ServiceResultException e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
					}
					addReferenceItem.setTargetNodeId(refNode.getTargetId());
					this.allReferenceItems.add(addReferenceItem);
				}
			}
		}
	}

	/**
	 * Constructs the value attribute of a variable or variable type node.
	 * 
	 * @param xType
	 * @param value
	 * @return
	 */
	public Variant constructValueAttribute(String xType, String value) {
		return new Variant(constructValueObject(xType, value));
	}

	public List<?> createListOfValues(String type) {
		if (BuiltinType.Boolean.name().equalsIgnoreCase(type)) {
			this.listOfValues = new ArrayList<>();
		} else if (BuiltinType.SByte.name().equalsIgnoreCase(type)) {
			this.listOfValues = new ArrayList<Byte>();
		} else if (BuiltinType.Byte.name().equalsIgnoreCase(type)) {
			this.listOfValues = new ArrayList<UnsignedByte>();
		} else if (BuiltinType.Int16.name().equalsIgnoreCase(type)) {
			this.listOfValues = new ArrayList<Short>();
		} else if (BuiltinType.UInt16.name().equalsIgnoreCase(type)) {
			this.listOfValues = new ArrayList<UnsignedShort>();
		} else if (BuiltinType.Int32.name().equalsIgnoreCase(type)) {
			this.listOfValues = new ArrayList<Integer>();
		} else if (BuiltinType.UInt32.name().equalsIgnoreCase(type)) {
			this.listOfValues = new ArrayList<UnsignedInteger>();
		} else if (BuiltinType.Int64.name().equalsIgnoreCase(type)) {
			this.listOfValues = new ArrayList<Long>();
		} else if (BuiltinType.UInt64.name().equalsIgnoreCase(type)) {
			this.listOfValues = new ArrayList<UnsignedLong>();
		} else if (BuiltinType.Float.name().equalsIgnoreCase(type)) {
			this.listOfValues = new ArrayList<Float>();
		} else if (BuiltinType.Double.name().equalsIgnoreCase(type)) {
			this.listOfValues = new ArrayList<Double>();
		} else if (BuiltinType.String.name().equalsIgnoreCase(type)) {
			this.listOfValues = new ArrayList<String>();
		} else if (BuiltinType.DateTime.name().equalsIgnoreCase(type)) {
			this.listOfValues = new ArrayList<DateTime>();
		} else if (BuiltinType.Guid.name().equalsIgnoreCase(type)) {
			this.listOfValues = new ArrayList<UUID>();
		} else if (BuiltinType.ByteString.name().equalsIgnoreCase(type)) {
			this.listOfValues = new ArrayList<byte[]>();
		} else if (BuiltinType.XmlElement.name().equalsIgnoreCase(type)) {
			this.listOfValues = new ArrayList<XmlElement>();
		} else if (BuiltinType.NodeId.name().equalsIgnoreCase(type)) {
			this.listOfValues = new ArrayList<NodeId>();
		} else if (BuiltinType.ExpandedNodeId.name().equalsIgnoreCase(type)) {
			this.listOfValues = new ArrayList<ExpandedNodeId>();
		} else if (BuiltinType.StatusCode.name().equalsIgnoreCase(type)) {
			this.listOfValues = new ArrayList<StatusCode>();
		} else if (BuiltinType.QualifiedName.name().equalsIgnoreCase(type)) {
			this.listOfValues = new ArrayList<QualifiedName>();
		} else if (BuiltinType.LocalizedText.name().equalsIgnoreCase(type)) {
			this.listOfValues = new ArrayList<LocalizedText>();
		} else if (BuiltinType.ExtensionObject.name().equalsIgnoreCase(type)) {
			this.listOfValues = new ArrayList<ExtensionObject>();
		} else if (BuiltinType.DataValue.name().equalsIgnoreCase(type)) {
			this.listOfValues = new ArrayList<DataValue>();
		} else if (BuiltinType.Variant.name().equalsIgnoreCase(type)) {
			this.listOfValues = new ArrayList<Variant>();
		} else if (BuiltinType.DiagnosticInfo.name().equalsIgnoreCase(type)) {
			this.listOfValues = new ArrayList<DiagnosticInfo>();
		}
		return this.listOfValues;
	}

	/**
	 * Constructs the value attribute of a variable or variable type node.
	 * 
	 * @param xType
	 * @param tmpValue
	 * @return
	 */
	public Object constructValueObject(String xType, String value) {
		String tmpValue = value.replace("\n", "").trim();
		try {
			if (BuiltinType.Boolean.name().equalsIgnoreCase(xType)) {
				return new Boolean(tmpValue);
			} else if (BuiltinType.SByte.name().equalsIgnoreCase(xType)) {
				return new Byte(tmpValue);
			} else if (BuiltinType.Byte.name().equalsIgnoreCase(xType)) {
				return new UnsignedByte(tmpValue);
			} else if (BuiltinType.Int16.name().equalsIgnoreCase(xType)) {
				return new Short(tmpValue);
			} else if (BuiltinType.UInt16.name().equalsIgnoreCase(xType)) {
				return new UnsignedShort(tmpValue);
			} else if (BuiltinType.Int32.name().equalsIgnoreCase(xType)) {
				return new Integer(tmpValue);
			} else if (BuiltinType.UInt32.name().equalsIgnoreCase(xType)) {
				return new UnsignedInteger(tmpValue);
			} else if (BuiltinType.Int64.name().equalsIgnoreCase(xType)) {
				return new Long(tmpValue);
			} else if (BuiltinType.UInt64.name().equalsIgnoreCase(xType)) {
				return new UnsignedLong(tmpValue);
			} else if (BuiltinType.Float.name().equalsIgnoreCase(xType)) {
				return new Float(tmpValue);
			} else if (BuiltinType.Double.name().equalsIgnoreCase(xType)) {
				return new Double(tmpValue);
			} else if (BuiltinType.String.name().equalsIgnoreCase(xType)) {
				/** return original value */
				return value;
				// return tmpValue;
			} else if (BuiltinType.DateTime.name().equalsIgnoreCase(xType)) {
				Object var = OPCDateFormat.parseAsVariantDateTime(tmpValue).getValue();
				/** just for the case */
				if (var == null) {
					try {
						// [yyyy-mm-dd]T[hh:mm:ss]
						String date = tmpValue.split("T")[0];
						int year = Integer.parseInt(date.split("-")[0]);
						int month = Integer.parseInt(date.split("-")[1]);
						int day = Integer.parseInt(date.split("-")[2]);
						date = tmpValue.split("T")[1];
						int hours = Integer.parseInt(date.split(":")[0]);
						int minutes = Integer.parseInt(date.split(":")[1]);
						int seconds = Integer.parseInt(date.split(":")[2]);
						DateTime dateTime = new DateTime(year, month, day, hours, minutes, seconds, 0);
						var = dateTime;
					} catch (NumberFormatException npe) {
						DateTime dateTime = new DateTime();
						var = dateTime;
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, npe);
					}
				}
				return var;
			} else if (BuiltinType.Guid.name().equalsIgnoreCase(xType)) {
				return new UUID(0, 0);
			} else if (BuiltinType.ByteString.name().equalsIgnoreCase(xType)) {
				tmpValue = tmpValue.replaceAll(" ", "");
				byteStringtoParse += tmpValue;
				try {
					return byteStringtoParse.getBytes();
				} catch (Exception e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
					throw e;
				}
			} else if (BuiltinType.XmlElement.name().equalsIgnoreCase(xType)) {
				return new XmlElement(tmpValue);
			} else if (BuiltinType.NodeId.name().equals(xType)) {
				// HB 19.01.2017
				NodeId nodeId = NodeId.parseNodeId(tmpValue);
				return changeNodeId(nodeId);
			} else if (BuiltinType.ExpandedNodeId.name().equalsIgnoreCase(xType)) {
				// HB 19.01.2017
				ExpandedNodeId nodeId = ExpandedNodeId.parseExpandedNodeId(tmpValue, this.namespacesFromFile);
				return changeNodeId(nodeId);
			} else if (BuiltinType.StatusCode.name().equalsIgnoreCase(xType)) {
				if ("".equals(tmpValue)) {
					return StatusCode.GOOD;
				}
				return StatusCode.BAD;
			} else if (BuiltinType.QualifiedName.name().equalsIgnoreCase(xType)) {
				return new QualifiedName(tmpValue);
			} else if (BuiltinType.LocalizedText.name().equalsIgnoreCase(xType)) {
				return new LocalizedText(tmpValue, this.cachedLocale);
			} else if (BuiltinType.ExtensionObject.name().equalsIgnoreCase(xType)) {
				return null;
			} else if (BuiltinType.DataValue.name().equalsIgnoreCase(xType)) {
				return new DataValue();
			} else if (BuiltinType.Variant.name().equalsIgnoreCase(xType)) {
				return new Variant(null);
			} else if (BuiltinType.DiagnosticInfo.name().equalsIgnoreCase(xType)) {
				return new DiagnosticInfo();
			}
		} catch (NumberFormatException ex) {
			return null;
		} catch (IllegalArgumentException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
		}
		return null;
	}

	private boolean checkMonitorChancled() {
		if (this.progressMonitor != null && this.progressMonitor.isCanceled()) {
			return true;
		}
		return false;
	}

	/**
	 * Disposes the parsers instance variables which are filled with values.
	 */
	private void dispose() {
		this.allNodeItems = null;
		this.allReferenceItems = null;
		this.namespacesFromFile = null;
		this.referencesPerNode = null;
	}

	private Object getExtensionObjectInstance() {
		Object obj = null;
		if (!this.extObjInstance.isEmpty()) {
			obj = this.extObjInstance.get(this.extObjInstance.size() - 1);
		}
		return obj;
	}

	/**
	 * Returns the Locale from a given LocaleId from the file.
	 * 
	 * @param Data LocaleId as String.
	 * @return Locale as {@link Locale} object, or NULL.
	 */
	private Locale getLocale(String data) {
		if (data != null && !data.isEmpty()) {
			for (Locale loc : Locale.getAvailableLocales()) {
				if (loc.toString().equals(data)) {
					return loc;
				}
			}
		}
		return null;
	}

	private void removeIsDimensionsUnderMatrix() {
		this.isDimensionsUnderMatrix.remove(this.isDimensionsUnderMatrix.size() - 1);
	}

	private StartElement removeCachedExtensionElement() {
		return this.cachedExtensionValueElement.remove(this.cachedExtensionValueElement.size() - 1);
	}

	private StartElement removeCachedValueElementRec() {
		StartElement cve = null;
		if (!this.cachedValueElement.isEmpty()) {
			cve = this.cachedValueElement.remove(this.cachedValueElement.size() - 1);
		}
		return cve;
	}

	private Object removeExtensionObject() {
		Object obj = null;
		if (!this.extObjInstance.isEmpty()) {
			obj = this.extObjInstance.remove(this.extObjInstance.size() - 1);
		}
		return obj;
	}

	private boolean removeElementUnderMatrixRec() {
		// remove last
		return this.isElementUnderMatrix.remove(this.isElementUnderMatrix.size() - 1);
	}

	private boolean removeIsExtensionObjectRec() {
		// remove last
		return this.isExtensionObjectTag.remove(this.isExtensionObjectTag.size() - 1);
	}

	private List<Object> removeArrayValue() {
		return this.simpleArrayValue.remove(this.simpleArrayValue.size() - 1);
	}

	/**
	 * Sets the AccessLevel to the current imported Node Attribute.
	 * 
	 * @param AccessLevel AccessLevel value.
	 */
	private void setAccessLevel(UnsignedByte accessLevel) {
		((VariableAttributes) this.nodeElementAttributes).setAccessLevel(accessLevel);
	}

	/**
	 * Sets the ArrayDimension to the current imported Node Attribute.
	 * 
	 * @param ArrayDimension ArrayDimension value.
	 */
	private void setArrayDimensions(String arrayDimension) {
		String tmpArrayDimension = arrayDimension;
		// parse array dimensions // parse it from string like "{x1,x2,...,n}"
		UnsignedInteger[] dim = new UnsignedInteger[0];
		if (tmpArrayDimension != null) // && !tmpArrayDimension.isEmpty())
		{
			tmpArrayDimension = tmpArrayDimension.replace("{", "").replace("}", "").replace("\n", "").trim();
			if (!tmpArrayDimension.isEmpty()) {
				String[] dimensions = tmpArrayDimension.split(",");
				dim = new UnsignedInteger[dimensions.length];
				// fill each dimension
				for (int i = 0; i < dimensions.length; i++) {
					dim[i] = new UnsignedInteger(dimensions[i].trim());
				}
			}
		}
		if (this.nodeElementAttributes instanceof VariableAttributes) {
			((VariableAttributes) this.nodeElementAttributes).setArrayDimensions(dim);
		} else if (this.nodeElementAttributes instanceof VariableTypeAttributes) {
			((VariableTypeAttributes) this.nodeElementAttributes).setArrayDimensions(dim);
		}
	}

	/**
	 * Sets the ArrayDimension to the current imported Node Attribute.
	 * 
	 * @param ArrayDimension ArrayDimension value.
	 */
	private UnsignedInteger[] getArrayDimensions() {
		if (this.nodeElementAttributes instanceof VariableAttributes) {
			return ((VariableAttributes) this.nodeElementAttributes).getArrayDimensions();
		} else if (this.nodeElementAttributes instanceof VariableTypeAttributes) {
			return ((VariableTypeAttributes) this.nodeElementAttributes).getArrayDimensions();
		}
		return null;
	}

	/**
	 * Sets the DataType to the current imported Node Attribute.
	 * 
	 * @param DataType DataType value.
	 */
	private void setDataType(NodeId dataType) {
		if (this.nodeElementAttributes instanceof VariableAttributes) {
			((VariableAttributes) this.nodeElementAttributes).setDataType(dataType);
		} else if (this.nodeElementAttributes instanceof VariableTypeAttributes) {
			((VariableTypeAttributes) this.nodeElementAttributes).setDataType(dataType);
		}
	}

	/**
	 * Sets the EventNotifier to the current imported Node Attribute.
	 * 
	 * @param EventNotifier EventNotifier value.
	 */
	private void setEventNotifier(UnsignedByte eventNotifier) {
		((ObjectAttributes) this.nodeElementAttributes).setEventNotifier(eventNotifier);
	}

	/**
	 * Sets the Executeable to the current imported Node Attribute.
	 * 
	 * @param Executeable Executeable value.
	 */
	private void setExecuteable(Boolean executeAble) {
		((MethodAttributes) this.nodeElementAttributes).setExecutable(executeAble);
	}

	/**
	 * Sets the Historizing to the current imported Node Attribute.
	 * 
	 * @param Historizing Historizing value.
	 */
	private void setHistorizing(Boolean isHistorizing) {
		((VariableAttributes) this.nodeElementAttributes).setHistorizing(isHistorizing);
	}

	/**
	 * Sets the InverseName to the current imported Node Attribute.
	 * 
	 * @param InverseName InverseName value.
	 */
	private void setInverseName(String data) {
		if (this.isLocale) {
			this.cachedLocale = getLocale(data);
		} else if (this.isText) {
			((ReferenceTypeAttributes) this.nodeElementAttributes)
					.setInverseName(new LocalizedText(data, this.cachedLocale));
		}
	}

	/**
	 * Sets the IsAbstract to the current imported Node Attribute.
	 * 
	 * @param IsAbstract IsAbstract value.
	 */
	private void setIsAbstract(Boolean value) {
		if (this.nodeElementAttributes instanceof DataTypeAttributes) {
			((DataTypeAttributes) this.nodeElementAttributes).setIsAbstract(value);
		} else if (this.nodeElementAttributes instanceof ObjectTypeAttributes) {
			((ObjectTypeAttributes) this.nodeElementAttributes).setIsAbstract(value);
		} else if (this.nodeElementAttributes instanceof VariableTypeAttributes) {
			((VariableTypeAttributes) this.nodeElementAttributes).setIsAbstract(value);
		} else if (this.nodeElementAttributes instanceof ReferenceTypeAttributes) {
			((ReferenceTypeAttributes) this.nodeElementAttributes).setIsAbstract(value);
		}
	}

	/**
	 * Sets the MinimumSamplingInterval to the current imported Node Attribute.
	 * 
	 * @param MinimumSamplingInterval MinimumSamplingInterval value.
	 */
	private void setMinimumSamplingInterval(Double minimumSamplingInterval) {
		((VariableAttributes) this.nodeElementAttributes).setMinimumSamplingInterval(minimumSamplingInterval);
	}

	/**
	 * Sets the IsSymmetric to the current imported Node Attribute.
	 * 
	 * @param IsSymmetric IsSymmetric value.
	 */
	private void setSymmetric(Boolean isSymmetric) {
		((ReferenceTypeAttributes) this.nodeElementAttributes).setSymmetric(isSymmetric);
	}

	/**
	 * Sets the UserAccessLevel to the current imported Node Attribute.
	 * 
	 * @param UserAccessLevel UserAccessLevel value.
	 */
	private void setUserAccessLevel(UnsignedByte userAccessLevel) {
		((VariableAttributes) this.nodeElementAttributes).setUserAccessLevel(userAccessLevel);
	}

	/**
	 * Sets the UserExecuteable to the current imported Node Attribute.
	 * 
	 * @param UserExecuteable UserExecuteable value.
	 */
	private void setUserExecuteable(Boolean userExecuteAble) {
		((MethodAttributes) this.nodeElementAttributes).setUserExecutable(userExecuteAble);
	}

	private void setArrayValue(String value) {
		StartElement cve = getCachedValueElementRec();
		if (cve == null)
			throw new NullPointerException(CACHEDVALISNULLL);
		if ("null".equalsIgnoreCase(cve.getName().getLocalPart())) {
			return;
		}
		String xType = cve.getName().getLocalPart();
		Variant val = constructValueAttribute(xType, value);
		// we can have max n dimensions
		addArrayValue(val.getValue());
	}

	private void setValueObject(Object object) {
		Variant val = new Variant(object);
		if (this.nodeElementAttributes instanceof VariableAttributes) {
			((VariableAttributes) this.nodeElementAttributes).setValue(val);
		} else if (this.nodeElementAttributes instanceof VariableTypeAttributes) {
			((VariableTypeAttributes) this.nodeElementAttributes).setValue(val);
		}
	}

	/**
	 * Sets the Value to the current imported Node Attribute.
	 * 
	 * @param VALUE Value value as String.
	 */
	private void setValue(String value) {
		StartElement cve = getCachedValueElementRec();
		if (cve == null)
			throw new NullPointerException(CACHEDVALISNULLL);
		if ("null".equalsIgnoreCase(cve.getName().getLocalPart())) {
			return;
		}
		Variant val;
		String xType = cve.getName().getLocalPart();
		val = constructValueAttribute(xType, value);
		if (this.nodeElementAttributes instanceof VariableAttributes) {
			((VariableAttributes) this.nodeElementAttributes).setValue(val);
		} else if (this.nodeElementAttributes instanceof VariableTypeAttributes) {
			((VariableTypeAttributes) this.nodeElementAttributes).setValue(val);
		}
	}

	/**
	 * Sets the Value to the current imported Node Attribute.
	 * 
	 * @param VALUE Value value as String.
	 */
	private void setListOfValue(String data) {
		StartElement cve = getCachedValueElementRec();
		if (cve == null)
			throw new NullPointerException(CACHEDVALISNULLL);
		if (EXTENSIONOBJECT.equals(cve.getName().getLocalPart())) {
			this.isExtensionObject = true;
		} else if ("Argument".equals(cve.getName().getLocalPart()) && !isArgument && complexElement != null) {
			this.isArgument = true;
			this.cachedArgument = new Argument();
		} else if (this.isArgument && complexElement != null) {
			setArguments(data);
		} else if (this.isLocalizedText && this.isText) {
			this.listOfValues.add(new LocalizedText(data, this.cachedLocale));
		}
	}

	private void setArguments(String data) {
		String tmpData = data;
		// construct argument
		if (tmpData.contains("\n")) {
			return;
		} else if ("Name".equals(complexElement.getName().getLocalPart())) {
			this.cachedArgument.setName(tmpData);
		} else if ("Identifier".equals(complexElement.getName().getLocalPart()) && this.isDataType) {
			try {
				this.cachedArgument.setDataType(NodeId.parseNodeId(tmpData));
			} catch (IllegalArgumentException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			}
		} else if (NodesCategoryTags.ValueRank.name().equals(complexElement.getName().getLocalPart())) {
			this.cachedArgument.setValueRank(new Integer(tmpData));
		} else if (NodesCategoryTags.ArrayDimensions.name().equals(complexElement.getName().getLocalPart())) {
			UnsignedInteger[] dim = new UnsignedInteger[0];
			if (!tmpData.isEmpty()) {
				tmpData = tmpData.replace("{", "").replace("}", "").replace("\n", "");
				String[] dimensions = tmpData.split(",");
				dim = new UnsignedInteger[dimensions.length];
				// fill each dimension
				for (int i = 0; i < dimensions.length; i++) {
					dim[i] = new UnsignedInteger(dimensions[i].trim());
				}
			}
			this.cachedArgument.setArrayDimensions(dim);
		} else if ("Text".equals(complexElement.getName().getLocalPart()) && isDescription) {
			this.cachedArgument.setDescription(new LocalizedText(tmpData, Locale.ENGLISH));
		}
	}

	/**
	 * Sets the ValueRank to the current imported Node Attribute.
	 * 
	 * @param ValueRank ValueRank value.
	 */
	private void setValueRank(Integer valueRank) {
		if (this.nodeElementAttributes instanceof VariableAttributes) {
			((VariableAttributes) this.nodeElementAttributes).setValueRank(valueRank);
		} else if (this.nodeElementAttributes instanceof VariableTypeAttributes) {
			((VariableTypeAttributes) this.nodeElementAttributes).setValueRank(valueRank);
		}
	}
}
