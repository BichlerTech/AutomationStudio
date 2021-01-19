package com.bichler.opcua.statemachine.addressspace.elements;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.ByteString;
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
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.ReferenceNode;
import org.opcfoundation.ua.utils.AbstractStructure;
import org.opcfoundation.ua.utils.MultiDimensionArrayUtils;
import org.opcfoundation.ua.utils.MultiDimensionArrayUtils.ArrayIterator;

import com.bichler.opcua.statemachine.addressspace.importer.StatemachineNodesetImporter;

import opc.sdk.core.enums.ValueRanks;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.NodeIdUtil;
import opc.sdk.core.node.ReferenceTypeNode;
import opc.sdk.core.node.UADataTypeNode;
import opc.sdk.core.node.UAObjectNode;
import opc.sdk.core.node.UAObjectTypeNode;
import opc.sdk.core.node.UAReferenceTypeNode;
import opc.sdk.core.node.UAVariableNode;
import opc.sdk.core.node.UAVariableTypeNode;

public abstract class BaseNodeXMLGen extends BaseXMLGen {

	private Node node = null;

	static String ISFORWARD = "IsForward";
	static String REFERENCETYPE = "ReferenceType";
	static String REFERENCE = "Reference";
	static String REFERENCES = "References";
	static String UAOBJECT = "UAObject";
	static String UAVARIABLE = "UAVariable";
	static String UADATATYPE = "UADataType";
	static String UAMETHOD = "UAMethod";
	static String UAOBJECTTYPE = "UAObjectType";
	static String UAVARIABLETYPE = "UAVariableType";
	static String UAREFERENCETYPE = "UAReferenceType";

	static String DATATYPE = "DataType";
	static String PARENTNODEID = "ParentNodeId";
	static String NODEID = "NodeId";
	static String BROWSENAME = "BrowseName";
	static String ACCESSLEVEL = "AccessLevel";
	static String USERACCESSLEVEL = "UserAccessLevel";
	static String DISPLAYNAME = "DisplayName";
	static String ISABSTRACT = "IsAbstract";
	static String EVENTNOTIFIER = "EventNotifier";
	static String VALUE = "Value";
	static String HISTORIZING = "Historizing";
	static String EXECUTEABLE = "Executeable";
	static String MINIMUMSAMPLINGINTERVAL = "MinimumSamplingInterval";
	static String USEREXECUTEABLE = "UserExecuteable";
	static String METHODDECLARATIONID = "MethodDeclarationId";
	static String VALUERANK = "ValueRank";
	static String ARRAYDIMENSIONS = "ArrayDimensions";
	static String DEFINITION = "Definition";
	static String INVERSENAME = "InverseName";
	static String SYMMETRIC = "Symmetric";
	static String WRITEMASK = "WriteMask";
	static String DESCRIPTION = "Description";

	static String LISTOF = "ListOf";
	static String MATRIX = "Matrix";
	static String DIMENSIONS = "Dimensions";
	static String ELEMENTS = "Elements";
	static String INT32 = "Int32";
	static String EXTENSIONOBJECT = "ExtensionObject";
	static String TYPEID = "TypeId";
	static String IDENTIFIER = "Identifier";
	static String BODY = "Body";

	public BaseNodeXMLGen() {
		super();
	}

	public BaseNodeXMLGen(Node node) {
		super();
		this.node = node;
	}

	public Node getNode() {
		return this.node;
	}

	public void writeXMLReference(BufferedWriter out, ReferenceNode refNode, NamespaceTable serverTable,
			NamespaceTable exportTable, StatemachineNodesetImporter importer) throws IOException {

		// boolean isHierachical =
		// serverInstance.getTypeTable().isTypeOf(refNode.getReferenceTypeId(),
		// Identifiers.HierarchicalReferences);
		/*
		 * if(isHierachical && !refNode.getIsInverse()) { return ; }
		 */

		NodeId nodeid = mapNodeId(refNode.getTargetId(), serverTable, exportTable);
		Node refType = importer.getNodesItemById(refNode.getReferenceTypeId());

		out.write("\t\t\t<" + REFERENCE + "" + helpReferenceType(refType) + "" + helpReferenceIsForward(refNode) + ">"
				+ nodeid.toString() + "</" + REFERENCE + ">");
		out.newLine();
	}

	void helpDescription(BufferedWriter out, LocalizedText text) throws IOException {
		if (text == null) {
			return;
		}
		if (text.getText() == null) {
			return;
		}
		if (text.getText().isEmpty()) {
			return;
		}

		out.write("\t\t<" + DESCRIPTION + ">" + text.getText() + "</" + DESCRIPTION + ">");
		out.newLine();
	}

	void helpDisplayName(BufferedWriter out, LocalizedText text) throws IOException {
		out.write("\t\t<" + DISPLAYNAME + ">" + helpString(text.getText()) + "</" + DISPLAYNAME + ">");
		out.newLine();
	}

	String helpParentNodeId(StatemachineNodesetImporter importer, Node node, NamespaceTable nsTable,
			NamespaceTable exportTable) {
		// NamespaceTable nsTable = serverInstance.getNamespaceUris();
		NodeId parentId = null;
		for (ReferenceNode refNode : node.getReferences()) {
			try {
				boolean isParent = isParentInHierachie(importer, node.getNodeId(),
						nsTable.toNodeId(refNode.getTargetId()));

				if (isParent) {
					parentId = nsTable.toNodeId(refNode.getTargetId());
					break;
				}
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			}
		}

		if (parentId == null) {
			return "";
		}

		NodeId mappedId = mapNodeId(parentId, nsTable, exportTable);
		return " " + PARENTNODEID + "=\"" + mappedId.toString() + "\"";
	}

	String helpValueRank(Node node) {
		Integer valueRanks = ValueRanks.Scalar.getValue();
		if (node instanceof UAVariableNode) {
			valueRanks = ((UAVariableNode) node).getValueRank();
		} else if (node instanceof UAObjectTypeNode) {
			valueRanks = ((UAVariableTypeNode) node).getValueRank();
		}
		if (valueRanks == -1) {
			return "";
		}

		return " " + VALUERANK + "=\"" + valueRanks + "\"";
	}

	String helpArrayDimensions(Node node) {
		UnsignedInteger[] arrayDimensions = new UnsignedInteger[0];
		if (node instanceof UAVariableNode) {
			arrayDimensions = ((UAVariableNode) node).getArrayDimensions();
		} else if (node instanceof UAObjectTypeNode) {
			arrayDimensions = ((UAVariableTypeNode) node).getArrayDimensions();
		}

		if (arrayDimensions.length == 0) {
			return "";
		}

		return " " + ARRAYDIMENSIONS + "=\"" + arrayDimensions.length + "\"";
	}

	String helpAccessLevel(Node node) {
		UnsignedByte accessLevel = ((UAVariableNode) node).getAccessLevel();
		if (UnsignedByte.ONE.equals(accessLevel)) {
			return "";
		}

		return " " + ACCESSLEVEL + "=\"" + accessLevel.toString() + "\"";
	}

	String helpUserAccessLevel(Node node) {
		UnsignedByte userAccessLevel = ((UAVariableNode) node).getUserAccessLevel();
		if (UnsignedByte.ONE.equals(userAccessLevel)) {
			return "";
		}

		return " " + USERACCESSLEVEL + "=\"" + userAccessLevel.toString() + "\"";
	}

	String helpMinimumSamplingInterval(Node node) {
		Double minimumSamplingInterval = ((UAVariableNode) node).getMinimumSamplingInterval();
		if (minimumSamplingInterval != 0) {
			return " " + MINIMUMSAMPLINGINTERVAL + "=\"" + minimumSamplingInterval.toString() + "\"";
		}
		return "";
	}

	String helpHistorizing(Node node) {
		Boolean historizing = ((UAVariableNode) node).getHistorizing();
		if (!historizing) {
			return "";
		}
		return " " + HISTORIZING + "=\"" + historizing.toString() + "\"";
	}

	String helpDataType(StatemachineNodesetImporter importer, Node node) {
		NodeId datatypeId = NodeId.NULL;
		if (node instanceof UAVariableNode) {
			datatypeId = ((UAVariableNode) node).getDataType();
		} else if (node instanceof UAVariableTypeNode) {
			datatypeId = ((UAVariableTypeNode) node).getDataType();
		}

		if (NodeId.equals(Identifiers.BaseDataType, datatypeId)) {
			return "";
		}

		if (NodeId.isNull(datatypeId)) {
			return " " + DATATYPE + "=\"" + datatypeId.toString() + "\"";
		}

		Node datatypeNode = importer.getNodesItemById(datatypeId);
		return " " + DATATYPE + "=\"" + datatypeNode.getBrowseName().getName() + "\"";
	}

	String helpNodeId(NodeId nodeId) {
		return " " + NODEID + "=\"" + nodeId.toString() + "\"";
	}

	String helpBrowseName(QualifiedName browseName, boolean isZeroNamespace) {
		if (!isZeroNamespace) {
			return helpBrowseName(browseName);
		}

		return " " + BROWSENAME + "=\"" + helpString(getNode().getBrowseName().getName()) + "\"";
	}

	String helpBrowseName(QualifiedName browseName) {
		return " " + BROWSENAME + "=\"" + helpString(getNode().getBrowseName().toString()) + "\"";
	}

	String helpEventNofifier(Node node) {
		UnsignedByte eventNotifier = ((UAObjectNode) node).getEventNotifier();
		if (UnsignedByte.ZERO.equals(eventNotifier)) {
			return "";
		}

		return " " + EVENTNOTIFIER + "=\"" + eventNotifier.byteValue() + "\"";
	}

	String helpReferenceType(Node refType) {
		String id = null;
		if (refType.getNodeId().getNamespaceIndex() > 0) {
			id = refType.getNodeId().toString();
		} else {
			id = refType.getBrowseName().getName();
		}

		return " " + REFERENCETYPE + "=\"" + id + "\"";
	}

	String helpIsAbstract(Node node) {
		Boolean isAbstract = false;
		if (node instanceof UAObjectTypeNode) {
			isAbstract = ((UAObjectTypeNode) node).getIsAbstract();
		} else if (node instanceof UAVariableTypeNode) {
			isAbstract = ((UAVariableTypeNode) node).getIsAbstract();
		} else if (node instanceof UADataTypeNode) {
			isAbstract = ((UADataTypeNode) node).getIsAbstract();
		} else if (node instanceof UAReferenceTypeNode) {
			isAbstract = ((UAReferenceTypeNode) node).getIsAbstract();
		}

		if (!isAbstract) {
			return "";
		}

		return " " + ISABSTRACT + "=\"" + isAbstract.toString() + "\"";
	}

	void helpValue(BufferedWriter out, NamespaceTable nsTable, Node node) throws IOException {
		Variant value = Variant.NULL;
		if (node instanceof UAVariableNode) {
			value = ((UAVariableNode) node).getValue();
		} else if (node instanceof UAVariableTypeNode) {
			value = ((UAVariableTypeNode) node).getValue();
		}

		if (value.isEmpty()) {
			return;
		}

		// start <Value> tag
		out.write("\t\t<" + VALUE + ">");
		out.newLine();
		// simple typesddd
		helpWriteValue(out, node, value, nsTable);

		// end <Value> tag
		out.write("\t\t</" + VALUE + ">");
		out.newLine();
	}

	String helpArrayDimension(Node node) {
		UnsignedInteger[] arrayDimensions = null;
		if (node instanceof UAVariableNode) {
			arrayDimensions = ((UAVariableNode) node).getArrayDimensions();
		} else if (node instanceof UAVariableTypeNode) {
			arrayDimensions = ((UAVariableTypeNode) node).getArrayDimensions();
		}

		int length = 0;
		if (arrayDimensions != null) {
			length = arrayDimensions.length;
		}
		String help = "";
		switch (length) {
		case 0:
			return "";
		default:
			for (int i = 0; i < arrayDimensions.length; i++) {
				if (i > 0) {
					help += ", ";
				}
				help += arrayDimensions[i].toString();

			}
			break;
		}
		return " ArrayDimensions=\"" + help + "\"";
	}

	String helpSymmetric(Node node) {
		if (!((ReferenceTypeNode) node).getSymmetric()) {
			return "";
		}

		return " Symmetric=\"" + ((ReferenceTypeNode) node).getSymmetric() + "\"";
	}

	String helpInverseName(Node node) {
		return "";// " InverseName=\""+((ReferenceNode)node).;
	}

	String helpWriteMask(Node node) {
		if (UnsignedInteger.ZERO.equals(node.getWriteMask())) {
			return "";
		}

		return " WriteMask=\"" + node.getWriteMask() + "\"";
	}

	String helpUserWriteMask(Node node) {
		if (UnsignedInteger.ZERO.equals(node.getUserWriteMask())) {
			return "";
		}

		return " UserWriteMask=\"" + node.getUserWriteMask() + "\"";
	}

	String helpString(String value) {
		String replaced = value.replaceAll("<", "&lt;");
		replaced = replaced.replaceAll(">", "&gt;");
		return replaced;
	}

	NodeId mapNodeId(NodeId nodeId, NamespaceTable serverTable, NamespaceTable exportTable) {
		int index = nodeId.getNamespaceIndex();
		String serverUri = serverTable.getUri(index);
		int exportNsIndex = exportTable.getIndex(serverUri);

		return NodeIdUtil.createNodeId(exportNsIndex, nodeId.getValue());
	}

	NodeId mapNodeId(ExpandedNodeId nodeId, NamespaceTable serverTable, NamespaceTable exportTable) {
		int index = nodeId.getNamespaceIndex();
		String serverUri = serverTable.getUri(index);
		int exportNsIndex = exportTable.getIndex(serverUri);

		return NodeIdUtil.createNodeId(exportNsIndex, nodeId.getValue());
	}

	private String datatypeToXML(Object datatype, boolean checkForExtensionObject) {
		String xmlName = null;
		if (datatype == null) {
			return null;
		} else if (datatype instanceof Boolean) {
			xmlName = "Boolean";
		} else if (datatype instanceof Byte) {
			xmlName = "SByte";
		} else if (datatype instanceof UnsignedByte) {
			xmlName = "Byte";
		} else if (datatype instanceof Short) {
			xmlName = "Int16";
		} else if (datatype instanceof UnsignedShort) {
			xmlName = "UInt16";
		} else if (datatype instanceof Integer) {
			xmlName = "Int32";
		} else if (datatype instanceof UnsignedInteger) {
			xmlName = "UInt32";
		} else if (datatype instanceof Long) {
			xmlName = "Int64";
		} else if (datatype instanceof UnsignedLong) {
			xmlName = "UInt64";
		} else if (datatype instanceof Float) {
			xmlName = "Float";
		} else if (datatype instanceof Double) {
			xmlName = "Double";
		} else if (datatype instanceof String) {
			xmlName = "String";
		} else if (datatype instanceof DateTime) {
			xmlName = "DateTime";
		} else if (datatype instanceof UUID) {
			xmlName = "Guid";
		} else if (datatype instanceof NodeId) {
			xmlName = "NodeId";
		} else if (datatype instanceof ExpandedNodeId) {
			xmlName = "ExpandedNodeId";
		} else if (datatype instanceof StatusCode) {
			xmlName = "StatusCode";
		} else if (datatype instanceof DiagnosticInfo) {
			xmlName = "DiagnosticInfo";
		} else if (datatype instanceof QualifiedName) {
			xmlName = "QualifiedName";
		} else if (datatype instanceof LocalizedText) {
			xmlName = "LocalizedText";
		} else if (datatype instanceof ExtensionObject) {
			xmlName = "ExtensionObject";
		} else if (datatype instanceof ByteString) {
			xmlName = "ByteString";
		}
		// extension object
		else {
			if (!checkForExtensionObject) {
				Class<?> compositeClass = MultiDimensionArrayUtils.getComponentType(datatype.getClass());
				Object instance = newInstanceForClass(compositeClass);
				xmlName = datatypeToXML(instance, true);
			}

			if (xmlName == null) {
				return "ExtensionObject";
			}
		}

		return xmlName;
	}

	private Object fetchObjectGetter(Field field, AbstractStructure obj) {

		for (Method m : obj.getClass().getMethods()) {
			if ((m.getName().startsWith("get")) && (m.getName().length() == (field.getName().length() + 3))) {
				if (m.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
					// Method found, run it
					try {
						return m.invoke(obj);
					} catch (IllegalAccessException e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE,
								"Could not determine method: " + m.getName());
					} catch (InvocationTargetException e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE,
								"Could not determine method: " + m.getName());
					}
				}
			}
		}
		return null;
	}

	private List<Field> fetchObjectFields(AbstractStructure obj) {
		List<Field> fields = new ArrayList<>();
		Class<?> someClass = obj.getClass();

		for (Class<?> c = someClass; c != null; c = c.getSuperclass()) {
			Field[] declaredFields = c.getDeclaredFields();
			if (declaredFields == null) {
				return fields;
			}

			for (Field f : declaredFields) {
				if ((f.getModifiers() & Modifier.STATIC) != 0) {
					continue;
				}
				fields.add(f);
			}
		}
		return fields;
	}

	private Object newInstanceForClass(Class<?> iClass) {
		Constructor<?> constructor2use = null;
		int parameterCount = -1;
		for (Constructor constructor : iClass.getConstructors()) {
			int count = constructor.getParameterCount();
			if (count == 0) {
				constructor2use = constructor;
				parameterCount = count;
				break;
			} else if (count == 1) {
				constructor2use = constructor;
				parameterCount = count;
			}
		}

		Object instance = null;
		try {
			switch (parameterCount) {
			case 0:
				instance = constructor2use.newInstance();
				break;
			case 1:
				instance = constructor2use.newInstance("0");
				break;
			default:
				if (iClass.isAssignableFrom(NodeId.class)) {
					instance = new NodeId(0, 0);
				}
				break;
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		return instance;
	}

	private boolean isParentInHierachie(StatemachineNodesetImporter importer, NodeId child, NodeId parent) {
		Node childNode = importer.getNodesItemById(child);
		if (childNode == null) {
			return false;
		}
		ExpandedNodeId parentNodeId = childNode.findTarget(Identifiers.HasComponent, true);
		if (parentNodeId == null) {
			return false;
		}
		Node parentNode = importer.getNodesItemById(parentNodeId);
		if (parentNode == null) {
			return false;
		}
		while (!parentNode.getNodeId().equals(Identifiers.RootFolder)) {
			if (parentNode.getNodeId().equals(parent)) {
				return true;
			}
			parentNodeId = parentNode.findTarget(Identifiers.HasComponent, true);
			if (parentNodeId == null) {
				return false;
			}
			parentNode = importer.getNodesItemById(parentNodeId);
			if (parentNode == null) {
				return false;
			}
		}
		return false;
	}

	private String datatypeToXML(Object datatype) {
		String xmlName = null;

		if (datatype instanceof Byte) {
			xmlName = "SByte";
		} else if (datatype instanceof UnsignedByte) {
			xmlName = "Byte";
		} else if (datatype instanceof Short) {
			xmlName = "Int16";
		} else if (datatype instanceof UnsignedShort) {
			xmlName = "UInt16";
		} else if (datatype instanceof Integer) {
			xmlName = "Int32";
		} else if (datatype instanceof UnsignedInteger) {
			xmlName = "UInt32";
		} else if (datatype instanceof Long) {
			xmlName = "Int64";
		} else if (datatype instanceof UnsignedLong) {
			xmlName = "UInt64";
		} else if (datatype instanceof Float) {
			xmlName = "Float";
		} else if (datatype instanceof Double) {
			xmlName = "Double";
		} else if (datatype instanceof String) {
			xmlName = "String";
		} else if (datatype instanceof DateTime) {
			xmlName = "DateTime";
		} else if (datatype instanceof UUID) {
			xmlName = "Guid";
		} else if (datatype instanceof NodeId) {
			xmlName = "NodeId";
		} else if (datatype instanceof ExpandedNodeId) {
			xmlName = "ExpandedNodeId";
		} else if (datatype instanceof StatusCode) {
			xmlName = "StatusCode";
		} else if (datatype instanceof DiagnosticInfo) {
			xmlName = "DiagnosticInfo";
		} else if (datatype instanceof QualifiedName) {
			xmlName = "QualifiedName";
		} else if (datatype instanceof LocalizedText) {
			xmlName = "LocalizedText";
		} else if (datatype instanceof ExtensionObject) {
			xmlName = "ExtensionObject";
		} else if (datatype instanceof ByteString) {
			xmlName = "ByteString";
		}

		return xmlName;
	}

	private String helpReferenceIsForward(ReferenceNode refNode) {
//		try {
		if (Identifiers.HasModellingRule.equals(refNode.getReferenceTypeId())) {
			return "";
		}

		if (!refNode.getIsInverse()) {
			return "";
		}
		return " " + ISFORWARD + "=\"false\"";
//		} catch (NullPointerException npe) {
//			npe.printStackTrace();
//			return "";
//		}
	}

	private void writeComplexStructure(BufferedWriter out, NamespaceTable nsTable, String preTabs,
			AbstractStructure obj) throws IOException {

		List<Field> fields = fetchObjectFields(obj);
		String className = obj.getClass().getSimpleName();
		out.write(preTabs + "\t<" + className + ">");
		out.newLine();

		for (Field f : fields) {
			Object var = fetchObjectGetter(f, obj);
			boolean useTabs = false;
			if (var instanceof LocalizedText) {
				useTabs = true;

				if (((LocalizedText) var).getText() == null) {
					out.write(preTabs + "\t\t<" + f.getName() + " />");
					out.newLine();
					continue;
				} else if (((LocalizedText) var).getText().isEmpty()) {
					out.write(preTabs + "\t\t<" + f.getName() + " />");
					out.newLine();
					continue;
				}
			} else if (var instanceof NodeId) {
				useTabs = true;
			}
			// Arraydimensions
			if ("ArrayDimensions".equals(f.getName())) {
				// empty tag
				if (var == null) {
					out.write(preTabs + "\t\t<ArrayDimensions />");
					out.newLine();
					continue;
				}
				// empty array dimensions
				else if (var instanceof UnsignedInteger[] && ((UnsignedInteger[]) var).length == 0) {
					out.write(preTabs + "\t\t<ArrayDimensions />");
					out.newLine();
					continue;
				} else if (var instanceof UnsignedInteger[] && ((UnsignedInteger[]) var).length > 0) {
					out.write(preTabs + "\t\t<ArrayDimensions>");
					out.newLine();
					for (UnsignedInteger dim : ((UnsignedInteger[]) var)) {
						out.write(preTabs + "\t\t\t<UInt32>" + dim.toString() + "</UInt32>");
						out.newLine();
					}
					out.write(preTabs + "\t\t<ArrayDimensions>");
					out.newLine();
					continue;
				}
			}
			// Everythign else
			else {
				out.write(preTabs + "\t\t<" + f.getName() + ">");
				writeValue(out, nsTable, preTabs + "\t\t\t", var);
			}
			if (!useTabs) {
				out.write("</" + f.getName() + ">");
			} else {
				out.write(preTabs + "\t\t</" + f.getName() + ">");
			}
			out.newLine();
		}

		out.write(preTabs + "\t</" + className + ">");
		out.newLine();
	}

	private void helpWriteValue(BufferedWriter out, Node node, Variant value, NamespaceTable nsTable)
			throws IOException {
//		String type = datatypeToXML(value.getValue());
//		boolean isArray = value.isArray();
//		int dimensions = value.getDimension();
//		// simple value
//		if (!isArray) {
//			writeValueSimple(out, type, value);
//		}
//		// matrix
//		else if (dimensions > 1) {
//			writeValueMatrix(out, type, value);
//		}
//		// array single dimension
//		else {
//			writeValueArray(out, type, value);
//		}

		String type = datatypeToXML(value.getValue(), false);
		boolean isArray = value.isArray();
		int dimensions = 0;
		if (isArray) {
			dimensions = value.getDimension();
		}
		// simple value
		if (!isArray) {
			if (value.isEmpty()) {
				// just a hack: TODO Why Value is null
				writeValueSimple(out, nsTable, "String", new Variant(""));
			} else {
				writeValueSimple(out, nsTable, type, value);
			}
		}
		// matrix
		else if (dimensions > 1) {
			writeValueMatrix(out, nsTable, type, value);
		}
		// array single dimension
		else {
			writeValueArray(out, nsTable, type, value);
		}

	}

	private void writeValue(BufferedWriter out, NamespaceTable nsTable, String preTabs, Object obj) throws IOException {
		if (obj instanceof AbstractStructure) {
			ExpandedNodeId typeId = ((AbstractStructure) obj).getXmlEncodeId();
			out.newLine();
			out.write(preTabs + "\t<" + TYPEID + ">");
			out.newLine();
			try {
				out.write(preTabs + "\t\t<" + IDENTIFIER + ">" + nsTable.toNodeId(typeId).toString() + "</" + IDENTIFIER
						+ ">");
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"Cannot parse ExpandedNodeId to Nodeid: " + typeId.toString());
			}
			out.newLine();
			out.write(preTabs + "\t</" + TYPEID + ">");
			out.newLine();
			out.write(preTabs + "\t<" + BODY + ">");
			out.newLine();

			writeComplexStructure(out, nsTable, preTabs + "\t", (AbstractStructure) obj);

			out.write(preTabs + "\t</" + BODY + ">");
			out.newLine();
		} else if (obj instanceof LocalizedText) {
			// Locale
			out.newLine();
			if (((LocalizedText) obj).getLocale() != null
					&& !LocalizedText.NO_LOCALE.equals(((LocalizedText) obj).getLocale())) {
				out.write(preTabs + "\t<Locale>" + LocalizedText.toLocaleId(((LocalizedText) obj).getLocale())
						+ "</Locale>");
				out.newLine();
			}

			// Text
			out.write(preTabs + "\t<Text>" + ((LocalizedText) obj).getText() + "</Text>");
			out.newLine();
		} else if (obj instanceof ByteString) {
			String bytestring = new String(((ByteString) obj).getValue());
			out.write(bytestring);
		} else if (obj instanceof NodeId) {
			out.newLine();
			out.write(preTabs + "\t<Identifier>" + obj.toString() + "</Identifier>");
			out.newLine();
		} else {
			out.write(obj.toString());
		}
	}

	private void writeValueArray(BufferedWriter out, NamespaceTable nsTable, String type, Variant value)
			throws IOException {
		String listType = LISTOF + type;
		int[] arrayDims = value.getArrayDimensions();
		ArrayIterator<Object> iterator = MultiDimensionArrayUtils.arrayIterator(value.getValue(), arrayDims);
		// start ListOf<TYPE>
		out.write("\t\t\t<" + listType + " " + XMLNS_TYPES + ">");
		out.newLine();
		// iterate array
		while (iterator.hasNext()) {
			Object obj = iterator.next();
			out.write("\t\t\t<" + type + ">");
			writeValue(out, nsTable, "\t\t\t\t", obj);
			out.write("</" + type + ">");
			out.newLine();
		}
		// end ListOf<TYPE>
		out.write("\t\t\t</" + listType + ">");
		out.newLine();
	}

	private void writeValueMatrix(BufferedWriter out, NamespaceTable nsTable, String type, Variant value)
			throws IOException {
		// multidimensional array (matrix)
		int[] arrayDims = value.getArrayDimensions();
		ArrayIterator<Object> iterator = MultiDimensionArrayUtils.arrayIterator(value.getValue(), arrayDims);

		out.write("\t\t\t<" + MATRIX + ">");
		out.newLine();
		out.write("\t\t\t\t<" + DIMENSIONS + ">");
		out.newLine();
		for (int i = 0; i < arrayDims.length; i++) {
			out.write("\t\t\t\t\t<" + INT32 + ">" + arrayDims[i] + "</" + INT32 + ">");
			out.newLine();
		}
		out.write("\t\t\t\t</" + DIMENSIONS + ">");
		out.newLine();
		out.write("\t\t\t\t<" + ELEMENTS + ">");
		out.newLine();

		while (iterator.hasNext()) {
			Object obj = iterator.next();
			out.write("\t\t\t\t\t<" + type + ">");
			writeValue(out, nsTable, "\t\t\t\t\t\t", obj);
			out.write("</" + type + ">");
			out.newLine();
		}

		out.write("\t\t\t\t</" + ELEMENTS + ">");
		out.newLine();
		out.write("\t\t\t</" + MATRIX + ">");
		out.newLine();
	}

	private void writeValueSimple(BufferedWriter out, NamespaceTable nsTable, String type, Variant value)
			throws IOException {
//		out.write("\t\t\t<" + type + " " + XMLNS_TYPES + ">");
//		writeValue(out, "\t\t\t\t", value.getValue());
//		out.write("</" + type + ">");
//		out.newLine();
		out.write("\t\t\t<" + type + " " + XMLNS_TYPES + ">");
		writeValue(out, nsTable, "\t\t\t\t", value.getValue());
		// \t\t\t
		out.write("</" + type + ">");
		out.newLine();
	}
}
