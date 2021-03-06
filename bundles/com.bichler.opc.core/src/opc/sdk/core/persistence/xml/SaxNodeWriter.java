package opc.sdk.core.persistence.xml;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.BuiltinsMap;
import org.opcfoundation.ua.builtintypes.ByteString;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceNode;
import org.opcfoundation.ua.utils.BijectionMap;
import org.opcfoundation.ua.utils.MultiDimensionArrayUtils;
import org.opcfoundation.ua.utils.MultiDimensionArrayUtils.ArrayIterator;
import org.xml.sax.SAXException;

import opc.sdk.core.application.operation.ICancleOperation;
import opc.sdk.core.context.StringTable;
import opc.sdk.core.enums.BuiltinType;
import opc.sdk.core.informationmodel.xml.ImportNamespaces;
import opc.sdk.core.informationmodel.xml.NodeType;
import opc.sdk.core.informationmodel.xml.NodesCategoryTags;
import opc.sdk.core.node.DataTypeNode;
import opc.sdk.core.node.MethodNode;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.NodeIdUtil;
import opc.sdk.core.node.ObjectNode;
import opc.sdk.core.node.ObjectTypeNode;
import opc.sdk.core.node.ReferenceTypeNode;
import opc.sdk.core.node.UAVariableNode;
import opc.sdk.core.node.UAVariableTypeNode;
import opc.sdk.core.node.VariableNode;
import opc.sdk.core.node.VariableTypeNode;
import opc.sdk.core.node.ViewNode;
import opc.sdk.core.types.TypeTable;
import opc.sdk.ua.constants.ExportNamespaces;

public class SaxNodeWriter {
	private NamespaceTable serverNsUris;
	private StringTable serverUris;
	private TypeTable serverTypeTable;
	private int workCount = 1;
	private ICancleOperation progressMonitor;

	public SaxNodeWriter(NamespaceTable namespaceUris, StringTable serverUris, TypeTable typeTable) {
		this.serverNsUris = namespaceUris;
		this.serverUris = serverUris;
		this.serverTypeTable = typeTable;
	}

	/**
	 * Writes all nodes in xml format to output stream!
	 * 
	 * @param out
	 * @param nodes
	 * @return !FALSE if cancled by user!
	 * @throws IOException
	 */
	public boolean writeNodes(OutputStream out, Node[] nodes) throws IOException {
		// check if there is something
		if (nodes == null || nodes.length <= 0) {
			return true;
		}
		if (checkProgressMonitorCancled()) {
			return false;
		}
		// find all required namespaces
		NamespaceTable nsTable = preFindNamespaces(nodes);
		if (checkProgressMonitorCancled()) {
			return false;
		}
		// open stream
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
		return export(writer, nodes, nsTable);
	}

	public boolean writeNodes(OutputStream out, Node[] nodes, NamespaceTable nsTable) throws IOException {
		// check if there is something
		if (nodes == null || nodes.length <= 0) {
			return true;
		}
		if (checkProgressMonitorCancled()) {
			return false;
		}
		// open stream
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
		return export(writer, nodes, nsTable);
	}

	boolean export(BufferedWriter writer, Node[] nodes, NamespaceTable nsExport) throws IOException {
		// start document
		startDocument(writer);
		if (checkProgressMonitorCancled()) {
			return false;
		}
		// starts header of document <nodeset>
		writeStartHeaderDocument(writer);
		if (checkProgressMonitorCancled()) {
			return false;
		}
		// create namespace section
		writeNamespaceTable(writer, nsExport.toArray());
		if (checkProgressMonitorCancled()) {
			return false;
		}
		// create server uri section
		writeServerTable(writer);
		if (checkProgressMonitorCancled()) {
			return false;
		}
		// start to export nodes
		writeNodes(writer, nodes, nsExport);
		if (checkProgressMonitorCancled()) {
			return false;
		}
		// close nodeset
		writeEndHeaderDocument(writer);
		if (checkProgressMonitorCancled()) {
			return false;
		}
		return true;
	}

	boolean checkProgressMonitorCancled() {
		if (this.progressMonitor != null && this.progressMonitor.isCanceled()) {
			return true;
		}
		return false;
	}

	void checkProgressMonitorIncrementWork() {
		if (this.progressMonitor != null && !this.progressMonitor.isCanceled()) {
			this.progressMonitor.worked(workCount);
		}
	}

	void checkProgressMonitorSubtask(String subtask) {
		if (this.progressMonitor != null && !this.progressMonitor.isCanceled()) {
			this.progressMonitor.subTask(subtask);
		}
	}

	NamespaceTable preFindNamespaces(Node[] nodes) {
		NamespaceTable nsTable = new NamespaceTable();
		for (Node node : nodes) {
			if (checkProgressMonitorCancled()) {
				return null;
			}
			int index = node.getNodeId().getNamespaceIndex();
			if (index == nsTable.getIndex(NamespaceTable.OPCUA_NAMESPACE)) {
				// skip default object nodes
				continue;
			}
			String uri = this.serverNsUris.getUri(index);
			// add nodeuri to table if required
			nsTable.add(uri);
			// find target references from different namespace
			ReferenceNode[] references = node.getReferences();
			if (references != null) {
				for (ReferenceNode reference : references) {
					ExpandedNodeId targetId = reference.getTargetId();
					NodeId localId = null;
					try {
						localId = this.serverNsUris.toNodeId(targetId);
						int refIndex = localId.getNamespaceIndex();
						String refUri = this.serverNsUris.getUri(refIndex);
						// add uri to table if required
						nsTable.add(refUri);
					} catch (ServiceResultException e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
					}
				}
			}
			// find datatype from different namespace
			NodeClass nodeClass = node.getNodeClass();
			NodeId dataType = null;
			if (nodeClass == NodeClass.Variable) {
				dataType = ((UAVariableNode) node).getDataType();
			} else if (nodeClass == NodeClass.VariableType) {
				dataType = ((UAVariableTypeNode) node).getDataType();
			}
			// datatype
			if (dataType != null) {
				int dtIndex = dataType.getNamespaceIndex();
				String dtUri = this.serverNsUris.getUri(dtIndex);
				// add uri to table if requried
				nsTable.add(dtUri);
			}
		}
		return nsTable;
	}

	private void createLocalizedName(BufferedWriter out, NodesCategoryTags tag, LocalizedText name) throws IOException {
		out.write("<" + tag.name() + ">");
		out.newLine();
		if (name.getLocaleId() == null || name.getLocaleId().isEmpty()) {
			out.write("<" + NodesCategoryTags.Locale.name() + " i:nil=\"true\"/>");
			out.newLine();
		} else {
			out.write("<" + NodesCategoryTags.Locale.name() + ">");
			out.write("" + name.getLocaleId());
			out.write("</" + NodesCategoryTags.Locale.name() + ">");
			out.newLine();
		}
		if (name.getText() == null || name.getText().isEmpty()) {
			out.write("<" + NodesCategoryTags.Text.name() + "/>");
			out.newLine();
		} else {
			out.write("<" + NodesCategoryTags.Text.name() + ">");
			out.write(name.getText().replace("<", "&lt;").replace(">", "&gt;"));
			out.write("</" + NodesCategoryTags.Text.name() + ">");
			out.newLine();
		}
		out.write("</" + tag.name() + ">");
		out.newLine();
	}

	private void createNodeId(BufferedWriter out, NodesCategoryTags tagname, NodeId nodeId, NamespaceTable nsExport)
			throws IOException {
		NodeId wrap = wrapNodeId(nodeId, nsExport);
		out.write("<" + tagname.name() + ">");
		out.newLine();
		// identifier
		out.write("<" + NodesCategoryTags.Identifier.name() + ">");
		// wrap nodeid matching to file
		out.write(wrap.toString());
		out.write("</" + NodesCategoryTags.Identifier.name() + ">");
		out.newLine();
		out.write("</" + tagname.name() + ">");
		out.newLine();
	}

	private void createValue(BufferedWriter out, Variant value, NodeId dataType, boolean startValueTag,
			NamespaceTable nsExport, String... additional) throws IOException {
		// value
		if (startValueTag) {
			out.write("<" + NodesCategoryTags.Value.name() + ">");
			out.newLine();
		}
		// check for empty variant value
		boolean isNull = value == null || value.isEmpty();
		// check for empty string
		if (!isNull) {
			if (value.getValue() instanceof String) {
				isNull = ((String) value.getValue()).isEmpty();
			}
		}
		if (isNull) {
			// value
			String additionalAttributes = createAdditionalAttributes(additional);
			// open value tag
			out.write("<" + NodesCategoryTags.Value.name() + additionalAttributes + ">");
			out.newLine();
			// null
			out.write("<" + NodesCategoryTags.Null.name() + "/>");
			out.newLine();
			// value
			out.write("</" + NodesCategoryTags.Value.name() + ">");
			out.newLine();
		} else {
			createValue(out, value, dataType, BuiltinsMap.ID_CLASS_MAP, nsExport, additional);
		}
		// close value
		if (startValueTag) {
			out.write("</" + NodesCategoryTags.Value.name() + ">");
			out.newLine();
		}
	}

	private String createAdditionalAttributes(String... additionals) {
		StringBuilder additionalAttributes = new StringBuilder();
		if (additionals != null) {
			for (String additional : additionals) {
				additionalAttributes.append(" " + additional);
			}
		}
		return additionalAttributes.toString();
	}

	private void createValue(BufferedWriter out, Variant value, NodeId dataType,
			BijectionMap<NodeId, Class<?>> idClassMap, NamespaceTable nsExport, String... additional)
			throws IOException {
		String tagName;
		// variant is not null or value in variant is not null
		if (value != null && !Variant.NULL.equals(value)) {
			BuiltinType builtin = null;
			// get builtin from class
			Integer builtinType = null;
			// BultinType Bytestring
			if (Identifiers.ByteString.equals(dataType)) {
				builtinType = BuiltinType.ByteString.getValue();
			}
			// Other BuiltinTypes
			else {
				builtinType = BuiltinsMap.ID_MAP.get(value.getCompositeClass());
			}
			if (builtinType != null) {
				for (BuiltinType type : BuiltinType.values()) {
					if (builtinType == type.getValue()) {
						builtin = type;
						break;
					}
				}
			}
			// or structure
			else {
				// check builtin from structure
				if (!NodeId.isNull(dataType) && Identifiers.Structure.equals(dataType)) {
					builtin = BuiltinType.ExtensionObject;
				}
			}
			if (builtin != null) {
				tagName = builtin.name();
				String additionalAttributes = createAdditionalAttributes(additional);
				// open value tag
				out.write("<" + NodesCategoryTags.Value.name() + additionalAttributes + ">");
				out.newLine();
				Object variantValue = value.getValue();
				// array value
				if (variantValue.getClass().isArray()) {
					int[] dimensions = value.getArrayDimensions();
					// matrix
					out.write("<" + NodesCategoryTags.Matrix.name() + ">");
					out.newLine();
					// dimensions
					out.write("<" + NodesCategoryTags.Dimensions.name() + ">");
					out.newLine();
					// particular dimensions
					for (int dim : dimensions) {
						out.write("<" + NodesCategoryTags.Int32.name() + ">");
						out.write(Integer.toString(dim));
						out.write("</" + NodesCategoryTags.Int32.name() + ">");
						out.newLine();
					}
					// close dimension
					out.write("</" + NodesCategoryTags.Dimensions.name() + ">");
					out.newLine();
					// class name
					String className = "";
					// element type tag
					if (!value.isEmpty()) {
						Class<?> compositeClass = SaxNodeWriter.getComponentType(value.getValue().getClass());
						className = " className=\"" + compositeClass.getName() + "\"";
					}
					out.write("<" + NodesCategoryTags.Elements.name() + className + ">");
					out.newLine();
					Object valueObject;
					if (builtin == BuiltinType.ByteString) {
						if (dimensions.length > 0) {
							ArrayIterator<Object> iterator = MultiDimensionArrayUtils.arrayIterator(value.getValue(),
									dimensions);
							while (iterator.hasNext()) {
								Object h = iterator.next();
								createValueTag(out, builtin, tagName, h, nsExport);
							}
						} else {
							createValueTag(out, builtin, tagName, value.getValue(), nsExport);
						}
					} else {
						valueObject = SaxNodeWriter.muxArray(value.getValue());
						Object[] array;
						if (valueObject != null && valueObject.getClass() == byte[].class) {
							array = new Byte[((byte[]) valueObject).length];
							for (int i = 0; i < ((byte[]) valueObject).length; i++) {
								array[i] = ((byte[]) valueObject)[i];
							}
						} else {
							array = (Object[]) SaxNodeWriter.muxArray(value.getValue());
						}
						// values
						for (Object arrayItem : array) {
							createValueTag(out, builtin, tagName, arrayItem, nsExport);
						}
					}
					// close
					out.write("</" + NodesCategoryTags.Elements.name() + ">");
					out.newLine();
					out.write("  </" + NodesCategoryTags.Matrix.name() + ">");
					out.newLine();
				}
				// scalar value
				else {
					createValueTag(out, builtin, tagName, value.getValue(), nsExport);
				}
				out.write("</" + NodesCategoryTags.Value.name() + ">");
				out.newLine();
				return;
			}
		}
		/**
		 * Localized Text is not in the BijectionMap ID_CLASS_MAP
		 */
		if (Identifiers.LocalizedText.equals(dataType)) {
			out.write("<" + NodesCategoryTags.Value.name() + ">");
			createLocalizedName(out, NodesCategoryTags.LocalizedText, (LocalizedText) value.getValue());
			out.write("</" + NodesCategoryTags.Value.name() + ">");// exit
			return;
		}
		String dataTypeString;
		Class<?> valueType = idClassMap.getRight(dataType);
		/**
		 * Not an base value type
		 */
		if (valueType == null) {
			// is a structure (enum value)
			boolean isEnumeration = this.serverTypeTable.isEnumeration(dataType);
			// is a complex value (complex value -> extension obj)
			if (isEnumeration) {
				// enums have integer classes
				valueType = value.getCompositeClass();
				dataTypeString = valueType.getName();
				String[] valueTypes = dataTypeString.split("\\.");
				// fetch TagName
				tagName = valueTypes[valueTypes.length - 1];
				out.write("<" + NodesCategoryTags.Value.name() + ">");
				out.write("<" + tagName + " xmlns=\"" + ExportNamespaces.TYPES.toString() + "\">");
				out.write(value.getValue().toString());
				out.write("</" + tagName + ">");
				out.write("</" + NodesCategoryTags.Value.name() + ">");
				// exit
				return;
			}
			// get its superType data type
			NodeId superType = this.serverTypeTable.findSuperType(dataType);
			// create a value element with its super type
			createValue(out, value, superType, idClassMap, nsExport);
			// exit if successfull
			return;
		}
		dataTypeString = valueType.getName();
		String[] valueTypes = dataTypeString.split("\\.");
		tagName = valueTypes[valueTypes.length - 1];
		// change the data type identifier with its string
		out.write("<" + NodesCategoryTags.Value.name() + ">");
		out.newLine();
		out.write("<" + tagName + " xmlns=\"" + ExportNamespaces.TYPES.toString() + "\">");
		out.write(value.getValue().toString());
		out.write("</" + tagName + ">");
		out.write("</" + NodesCategoryTags.Value.name() + ">");
		// exit if successfull
	}

	private void writeExtensionObject(BufferedWriter out, String tagName, Object value, NamespaceTable nsExport)
			throws IOException {
		out.write("<" + tagName + " className=\"" + value.getClass().getName() + "\" >");
		out.newLine();
		Field[] fields = value.getClass().getDeclaredFields();
		List<Field> fields2print = new ArrayList<>();
		for (Field f : fields) {
			boolean isStatic = (f.getModifiers() & 8) == 8;
			if (isStatic) {
				continue;
			}
			fields2print.add(f);
		}
		createValueFields(out, value, fields2print.toArray(new Field[0]), nsExport);
		out.write("</" + tagName + ">\n");
	}

	private void exportByteString(BufferedWriter out, String tagName, Object value) throws IOException {
		try {
			String byteStringValue = null;
			if (value instanceof byte[]) {
				byteStringValue = Arrays.toString((byte[]) value);
			} else if (value instanceof Object[]) {
				byteStringValue = Arrays.toString((Object[]) value);
			}
			/*
			 * else if(value instanceof String) {
			 * 
			 * byteStringValue = breakByteStringValue((String) value, 76); }
			 */
			else if (value instanceof ByteString) {
				/**
				 * use for java 9
				 */
				// Base64.Encoder encoder = Base64.getEncoder();
				// byteStringValue =
				// encoder.encodeToString(ByteString.asByteArray(((ByteString)
				// value)));
				/**
				 * use for java1.7
				 */
				byteStringValue = new String(((ByteString) value).getValue());
				// byteStringValue = DatatypeConverter.printBase64Binary(((ByteString)
				// value).getValue());
				// byteStringValue =
				// breakByteStringValue(DatatypeConverter.printBase64Binary(((ByteString)
				// value).getValue()), 76);
				// byteStringValue = breakByteStringValue(new String((((ByteString)
				// value).getValue())), 76);
			}
			out.write("<" + tagName + ">");
			out.write(byteStringValue);
			out.write("</" + tagName + ">\n"); // +System.lineSeparator()
		} catch (NullPointerException npe) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, npe);
		}
	}

	private String breakByteStringValue(String value, final int breakCount) {
		StringBuilder newByteString = new StringBuilder();
		// bytestring length
		int valueLength = value.length();
		String byteStringValue = value;
		while (valueLength > breakCount) {
			// get a row max length break counter from bytestring
			String row = byteStringValue.substring(0, breakCount - 1);
			newByteString.append(row);
			newByteString.append("\n");
			// reduce row from bytestring
			byteStringValue = byteStringValue.substring(breakCount);
			valueLength = byteStringValue.length();
		}
		newByteString.append(byteStringValue);
		return newByteString.toString();
	}

	private void writeDefault(BufferedWriter out, String tagName, Object value) throws IOException {
		try {
			out.write("<" + tagName + ">");
			out.write(value.toString());
			out.write("</" + tagName + ">\n");
		} catch (NullPointerException npe) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, npe);
		}
	}

	private void createValueTag(BufferedWriter out, BuiltinType builtin, String tagName, Object value,
			NamespaceTable nsExport) throws IOException {
		switch (builtin) {
		case ExtensionObject:
			writeExtensionObject(out, tagName, value, nsExport);
			break;
		case LocalizedText:
			createLocalizedName(out, NodesCategoryTags.LocalizedText, (LocalizedText) value);
			break;
		case NodeId:
			createNodeId(out, NodesCategoryTags.NodeId, (NodeId) value, nsExport);
			break;
		case ByteString:
			exportByteString(out, tagName, value);
			break;
		default:
			writeDefault(out, tagName, value);
			break;
		}
	}

	private void createValueFields(BufferedWriter out, Object value, Field[] fields, NamespaceTable nsExport)
			throws IOException {
		for (Field f : fields) {
			String[] additional = { "fieldname=\"" + f.getName() + "\"" };
			try {
				Method method;
				if (f.getName().compareTo("hash") == 0)
					method = value.getClass().getMethod("hashCode");
				else
					method = value.getClass().getMethod("get" + f.getName().replaceFirst(f.getName().substring(0, 1),
							f.getName().substring(0, 1).toUpperCase()));
				NodeId datatypeId = NodeId.NULL;
				Object value2write = method.invoke(value);
				if (value2write != null) {
					Integer biid = BuiltinsMap.ID_MAP.get(value2write.getClass());
					BuiltinType type = BuiltinType.getType(new NodeId(0, biid));
					if (type == null) {
						type = BuiltinType.ExtensionObject;
					}
					datatypeId = type.getBuildinTypeId();
				}
				createValue(out, new Variant(value2write), datatypeId, false, nsExport, additional);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				Logger.getAnonymousLogger(getClass().getName()).log(Level.SEVERE, null, e);
			}
		}
	}

	private void writeEndHeaderDocument(BufferedWriter out) throws IOException {
		out.write("</" + NodesCategoryTags.NodeSet.name() + ">");
		out.flush();
	}

	private void writeNamespaceTable(BufferedWriter out, String[] namespaceUris) throws IOException {
		// begin namespace uris
		out.write("<" + NodesCategoryTags.NamespaceUris.name() + ">");
		out.newLine();
		// namespace
		for (String namespace : namespaceUris) {
			// start tag
			out.write("  <" + NodesCategoryTags.String.name() + ">");
			// namespace to write
			out.write(namespace);
			// end tag
			out.write("</" + NodesCategoryTags.String.name() + ">");
			out.newLine();
		}
		// end namespace uris
		out.write("</" + NodesCategoryTags.NamespaceUris.name() + ">");
		out.newLine();
	}

	/**
	 * Write <node>
	 * 
	 * @param out
	 * @param node
	 * @param nsExport
	 * @throws IOException
	 */
	private void writeNode(BufferedWriter out, Node node, NamespaceTable nsExport) throws IOException {
		// start header
		writeNodeHeader(out, node);
		// base node attributes
		writeBaseAttributes(out, node, nsExport);
		// particular node attributes
		writeParticularAttributes(out, node, nsExport);
		// end header
		writeEndNode(out);
	}

	private void writeBaseAttributes(BufferedWriter out, Node node, NamespaceTable nsExport) throws IOException {
		NodeId nodeId = node.getNodeId();
		NodeClass nodeClass = node.getNodeClass();
		QualifiedName browsename = node.getBrowseName();
		LocalizedText displayname = node.getDisplayName();
		LocalizedText description = node.getDescription();
		UnsignedInteger writeMask = node.getWriteMask();
		UnsignedInteger userWriteMask = node.getUserWriteMask();
		ReferenceNode[] references = node.getReferences();
		// nodeid
		writeNodeId(out, nodeId, nsExport);
		// nodeclass
		writeNodeClass(out, nodeClass);
		// browsename
		writeBrowsename(out, browsename);
		// displayname
		writeDisplayname(out, displayname);
		// description
		writeDescripton(out, description);
		// write mask
		writeWriteMask(out, writeMask);
		// userwrite mask
		writeUserWriteMask(out, userWriteMask);
		// references
		writeReferences(out, references, nsExport);
	}

	private void writeDataType(BufferedWriter out, Node node) throws IOException {
		Boolean isAbstract = ((DataTypeNode) node).getIsAbstract();
		out.write("<" + NodesCategoryTags.IsAbstract.name() + ">");
		out.write(isAbstract.toString());
		out.write("</" + NodesCategoryTags.IsAbstract.name() + ">");
		out.newLine();
	}

	private void writeMethod(BufferedWriter out, Node node) throws IOException {
		Boolean executeable = ((MethodNode) node).getExecutable();
		Boolean userExecuteable = ((MethodNode) node).getUserExecutable();
		// executeable
		out.write("<" + NodesCategoryTags.Executeable.name() + ">");
		out.write(executeable.toString());
		out.write("</" + NodesCategoryTags.Executeable.name() + ">");
		out.newLine();
		// user executeable
		out.write("<" + NodesCategoryTags.UserExecuteable.name() + ">");
		out.write(userExecuteable.toString());
		out.write("</" + NodesCategoryTags.UserExecuteable.name() + ">");
		out.newLine();
	}

	private void writeObject(BufferedWriter out, Node node) throws IOException {
		UnsignedByte eventNotifier = ((ObjectNode) node).getEventNotifier();
		// event notifier
		out.write("<" + NodesCategoryTags.EventNotifier.name() + ">");
		out.write(eventNotifier.toString());
		out.write("</" + NodesCategoryTags.EventNotifier.name() + ">");
		out.newLine();
	}

	private void writeObjectType(BufferedWriter out, Node node) throws IOException {
		Boolean isAbstract = ((ObjectTypeNode) node).getIsAbstract();
		// is abstract
		out.write("<" + NodesCategoryTags.IsAbstract.name() + ">");
		out.write(isAbstract.toString());
		out.write("</" + NodesCategoryTags.IsAbstract.name() + ">");
		out.newLine();
	}

	private void writeReferenceType(BufferedWriter out, Node node) throws IOException {
		Boolean isAbstract = ((ReferenceTypeNode) node).getIsAbstract();
		LocalizedText inverseName = ((ReferenceTypeNode) node).getInverseName();
		if (inverseName == null || LocalizedText.NULL.equals(inverseName)) {
			inverseName = LocalizedText.EMPTY;
		}
		// is abstract
		out.write("<" + NodesCategoryTags.IsAbstract.name() + ">");
		out.write(isAbstract.toString());
		out.write("</" + NodesCategoryTags.IsAbstract.name() + ">");
		out.newLine();
		// inverse name
		createLocalizedName(out, NodesCategoryTags.InverseName, inverseName);
		// symmetric
		out.write("<" + NodesCategoryTags.Symmetric.name() + ">");
		out.write(inverseName.toString());
		out.write("</" + NodesCategoryTags.Symmetric.name() + ">");
		out.newLine();
	}

	private void writeVariable(BufferedWriter out, Node node, NamespaceTable nsExport) throws IOException {
		Boolean historizing;
		UnsignedByte accessLevel;
		UnsignedByte userAccessLevel;
		Double minimumSampling;
		UnsignedInteger[] arrayDimension;
		NodeId dataType;
		Variant value;
		Integer valueRank;
		accessLevel = ((VariableNode) node).getAccessLevel();
		arrayDimension = ((VariableNode) node).getArrayDimensions();
		dataType = ((VariableNode) node).getDataType();
		historizing = ((VariableNode) node).getHistorizing();
		minimumSampling = ((VariableNode) node).getMinimumSamplingInterval();
		userAccessLevel = ((VariableNode) node).getUserAccessLevel();
		value = ((VariableNode) node).getValue();
		valueRank = ((VariableNode) node).getValueRank();
		// accesslevel
		out.write("<" + NodesCategoryTags.AccessLevel.name() + ">");
		out.write(accessLevel.toString());
		out.write("</" + NodesCategoryTags.AccessLevel.name() + ">");
		out.newLine();
		// arrayDimension
		createArrayDimension(out, arrayDimension);
		// datatype
		createNodeId(out, NodesCategoryTags.DataType, dataType, nsExport);
		// historizing
		out.write("<" + NodesCategoryTags.Historizing.name() + ">");
		out.write(historizing.toString());
		out.write("</" + NodesCategoryTags.Historizing.name() + ">");
		out.newLine();
		// minimumsampling interval
		out.write("<" + NodesCategoryTags.MinimumSamplingInterval.name() + ">");
		out.write(minimumSampling.toString());
		out.write("</" + NodesCategoryTags.MinimumSamplingInterval.name() + ">");
		out.newLine();
		// useraccess level
		out.write("<" + NodesCategoryTags.UserAccessLevel.name() + ">");
		out.write(userAccessLevel.toString());
		out.write("</" + NodesCategoryTags.UserAccessLevel.name() + ">");
		out.newLine();
		// value
		createValue(out, value, dataType, true, nsExport);
		// value rank
		out.write("<" + NodesCategoryTags.ValueRank.name() + ">");
		out.write(valueRank.toString());
		out.write("</" + NodesCategoryTags.ValueRank.name() + ">");
		out.newLine();
	}

	private void writeVariableType(BufferedWriter out, Node node, NamespaceTable nsExport) throws IOException {
		Boolean isAbstract;
		UnsignedInteger[] arrayDimension;
		NodeId dataType;
		Variant value;
		Integer valueRank;
		arrayDimension = ((VariableTypeNode) node).getArrayDimensions();
		dataType = ((VariableTypeNode) node).getDataType();
		isAbstract = ((VariableTypeNode) node).getIsAbstract();
		value = ((VariableTypeNode) node).getValue();
		valueRank = ((VariableTypeNode) node).getValueRank();
		// arrayDimension
		createArrayDimension(out, arrayDimension);
		// datatype
		createNodeId(out, NodesCategoryTags.DataType, dataType, nsExport);
		// is abstract
		out.write("<" + NodesCategoryTags.IsAbstract.name() + ">");
		out.write(isAbstract.toString());
		out.write("</" + NodesCategoryTags.IsAbstract.name() + ">");
		out.newLine();
		// value
		createValue(out, value, dataType, true, nsExport);
		// value rank
		out.write("<" + NodesCategoryTags.ValueRank.name() + ">");
		out.write(valueRank.toString());
		out.write("</" + NodesCategoryTags.ValueRank.name() + ">");
		out.newLine();
	}

	private void writeView(BufferedWriter out, Node node) throws IOException {
		Boolean containsNoLoop = ((ViewNode) node).getContainsNoLoops();
		UnsignedByte eventNotifier = ((ViewNode) node).getEventNotifier();
		// contains no loops
		out.write("<" + NodesCategoryTags.ContainsNoLoops.name() + ">");
		out.write(containsNoLoop.toString());
		out.write("</" + NodesCategoryTags.ContainsNoLoops.name() + ">");
		// event notifier
		out.write("<" + NodesCategoryTags.EventNotifier.name() + ">");
		out.write(eventNotifier.toString());
		out.write("</" + NodesCategoryTags.EventNotifier.name() + ">");
	}

	private void writeParticularAttributes(BufferedWriter out, Node node, NamespaceTable nsExport) throws IOException {
		// particular nodeclass
		NodeClass nodeClass = node.getNodeClass();
		// initial
		switch (nodeClass) {
		case DataType:
			writeDataType(out, node);
			break;
		case Method:
			writeMethod(out, node);
			break;
		case Object:
			writeObject(out, node);
			break;
		case ObjectType:
			writeObjectType(out, node);
			break;
		case ReferenceType:
			writeReferenceType(out, node);
			break;
		case Variable:
			writeVariable(out, node, nsExport);
			break;
		case VariableType:
			writeVariableType(out, node, nsExport);
			break;
		case View:
			writeView(out, node);
			break;
		default:
			break;
		}
	}

	private void createArrayDimension(BufferedWriter out, UnsignedInteger[] arrayDimension) throws IOException {
		if (arrayDimension == null) {
			out.write("<" + NodesCategoryTags.ArrayDimensions.name() + "/>");
			out.newLine();
		} else {
			StringBuilder arraydim = new StringBuilder();
			try {
				if (arrayDimension.length == 0) {
					out.write("<" + NodesCategoryTags.ArrayDimensions.name() + "/>");
					out.newLine();
					return;
				} else {
					arraydim.append("{");
					String comma = "";
					for (UnsignedInteger item : arrayDimension) {
						arraydim.append(comma + item.toString());
						comma = ",";
					}
					arraydim.append("}");
				}
			} catch (NullPointerException npe) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, npe);
				arraydim = new StringBuilder();
				arraydim.append("{0}");
			}
			out.write("<" + NodesCategoryTags.ArrayDimensions.name() + ">");
			out.write(arraydim.toString());
			out.write("</" + NodesCategoryTags.ArrayDimensions.name() + ">");
			out.newLine();
		}
	}

	private void writeReferences(BufferedWriter out, ReferenceNode[] references, NamespaceTable nsExport)
			throws IOException {
		out.write("<" + NodesCategoryTags.References.name() + ">");
		out.newLine();
		for (ReferenceNode reference : references) {
			writeReferenceNode(out, reference, nsExport);
		}
		out.write("</" + NodesCategoryTags.References.name() + ">");
		out.newLine();
	}

	private void writeReferenceNode(BufferedWriter out, ReferenceNode reference, NamespaceTable nsExport)
			throws IOException {
		// skip node
		if (NodeId.isNull(reference.getReferenceTypeId()) || ExpandedNodeId.isNull(reference.getTargetId())) {
			return;
		}
		try {
			NodeId destType = wrapNodeId(reference.getReferenceTypeId(), nsExport);
			if (NodeId.isNull(destType)) {
				return;
			}
			ExpandedNodeId destTarget = wrapNodeId(reference.getTargetId(), nsExport);
			if (ExpandedNodeId.isNull(destTarget)) {
				return;
			}
			// reference node
			out.write("<" + NodesCategoryTags.ReferenceNode + ">");
			out.newLine();
			// reference type id
			out.write("<" + NodesCategoryTags.ReferenceTypeId.name() + ">");
			out.newLine();
			// identifier
			out.write("<" + NodesCategoryTags.Identifier.name() + ">");
			out.write(destType.toString());
			out.write("</" + NodesCategoryTags.Identifier.name() + ">");
			out.newLine();
			out.write("</" + NodesCategoryTags.ReferenceTypeId.name() + ">");
			out.newLine();
			// reference direction
			out.write("<" + NodesCategoryTags.IsInverse.name() + ">");
			out.write(reference.getIsInverse().toString());
			out.write("</" + NodesCategoryTags.IsInverse.name() + ">");
			out.newLine();
			// target id
			out.write("<" + NodesCategoryTags.TargetId.name() + ">");
			out.newLine();
			// identifier
			out.write("    <" + NodesCategoryTags.Identifier.name() + ">");
			// NodeIdUtil.createNodeId(index, identifier)
			NodeId nid = nsExport.toNodeId(destTarget);
			out.write(nid.toString());
			out.write("</" + NodesCategoryTags.Identifier.name() + ">");
			out.newLine();
			out.write("</" + NodesCategoryTags.TargetId.name() + ">");
			out.newLine();
			// close
			out.write("</" + NodesCategoryTags.ReferenceNode + ">");
			out.newLine();
		} catch (Exception e) {
			// ignore reference (example: maybe a type reference, source
			// BaseObjectType referencing to a namespace index which is not
			// required to export
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
		}
	}

	private void writeUserWriteMask(BufferedWriter out, UnsignedInteger userWriteMask) throws IOException {
		out.write("<" + NodesCategoryTags.UserWriteMask.name() + ">");
		out.write(userWriteMask.toString());
		out.write("</" + NodesCategoryTags.UserWriteMask.name() + ">");
		out.newLine();
	}

	private void writeWriteMask(BufferedWriter out, UnsignedInteger writeMask) throws IOException {
		out.write("<" + NodesCategoryTags.WriteMask.name() + ">");
		out.write(writeMask.toString());
		out.write("</" + NodesCategoryTags.WriteMask.name() + ">");
		out.newLine();
	}

	private void writeDescripton(BufferedWriter out, LocalizedText description) throws IOException {
		createLocalizedName(out, NodesCategoryTags.Description, description);
	}

	private void writeDisplayname(BufferedWriter out, LocalizedText displayname) throws IOException {
		createLocalizedName(out, NodesCategoryTags.DisplayName, displayname);
	}

	private void writeBrowsename(BufferedWriter out, QualifiedName browsename) throws IOException {
		out.write("<" + NodesCategoryTags.BrowseName.name() + ">");
		out.newLine();
		out.write("  <" + NodesCategoryTags.NamespaceIndex.name() + ">");
		out.write(Integer.toString(browsename.getNamespaceIndex()));
		out.write("</" + NodesCategoryTags.NamespaceIndex.name() + ">");
		out.newLine();
		out.write("<" + NodesCategoryTags.Name.name() + ">");
		out.write(browsename.getName().replace("<", "&lt;").replace(">", "&gt;"));
		out.write("</" + NodesCategoryTags.Name.name() + ">");
		out.newLine();
		out.write("</" + NodesCategoryTags.BrowseName.name() + ">");
		out.newLine();
	}

	private void writeNodeClass(BufferedWriter out, NodeClass nodeClass) throws IOException {
		out.write("<" + NodesCategoryTags.NodeClass.name() + ">");
		out.write(nodeClass.name() + "_" + nodeClass.getValue());
		out.write("</" + NodesCategoryTags.NodeClass.name() + ">");
		out.newLine();
	}

	private void writeNodeId(BufferedWriter out, NodeId nodeId, NamespaceTable nsExport) throws IOException {
		createNodeId(out, NodesCategoryTags.NodeId, nodeId, nsExport);
	}

	/**
	 * 
	 * @param targetId
	 * @param nsExport
	 * @return
	 */
	private ExpandedNodeId wrapNodeId(ExpandedNodeId targetId, NamespaceTable nsExport) {
		NodeId wrap2 = NodeId.NULL;
		try {
			wrap2 = this.serverNsUris.toNodeId(targetId);
		} catch (ServiceResultException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			wrap2 = NodeIdUtil.createNodeId(targetId.getNamespaceIndex(), targetId.getValue());
		}
		NodeId wrap2id = wrapNodeId(wrap2, nsExport);
		return new ExpandedNodeId(nsExport.getUri(wrap2id.getNamespaceIndex()), wrap2id.getValue(), nsExport);
	}

	private NodeId wrapNodeId(NodeId nodeId, NamespaceTable nsExport) {
		// get source ns uri
		String sourceUri = this.serverNsUris.getUri(nodeId.getNamespaceIndex());
		// wrap destination index
		int destination = nsExport.getIndex(sourceUri);
		if (destination < 0) {
			return NodeId.NULL;
		}
		return NodeIdUtil.createNodeId(destination, nodeId.getValue());
	}

	private void writeEndNode(BufferedWriter out) throws IOException {
		out.write("</" + NodesCategoryTags.Node.name() + ">");
		out.newLine();
		checkProgressMonitorIncrementWork();
	}

	private void writeNodeHeader(BufferedWriter out, Node node) throws IOException {
		out.write("<" + NodesCategoryTags.Node.name() + " i:type=\"");
		switch (node.getNodeClass()) {
		case DataType:
			out.write(NodeType.DataTypeNode.name());
			break;
		case Method:
			out.write(NodeType.MethodNode.name());
			break;
		case Object:
			out.write(NodeType.ObjectNode.name());
			break;
		case ObjectType:
			out.write(NodeType.ObjectTypeNode.name());
			break;
		case ReferenceType:
			out.write(NodeType.ReferenceTypeNode.name());
			break;
		case Variable:
			out.write(NodeType.VariableNode.name());
			break;
		case VariableType:
			out.write(NodeType.VariableTypeNode.name());
			break;
		case View:
			out.write(NodeType.View.name());
			break;
		default:
			break;
		}
		// end tag
		out.write("\">");
		out.newLine();
	}

	/**
	 * Write <nodes>
	 * 
	 * @param out
	 * @param nodes
	 * @param nsExport
	 * @throws SAXException
	 * @throws IOException
	 */
	private void writeNodes(BufferedWriter out, Node[] nodes, NamespaceTable nsExport) throws IOException {
		out.write("<" + NodesCategoryTags.Nodes + ">");
		out.newLine();
		for (int i = 0; i < nodes.length; i++) {
			Node node = nodes[i];
			if (checkProgressMonitorCancled()) {
				return;
			}
			writeNode(out, node, nsExport);
		}
		out.write("</" + NodesCategoryTags.Nodes + ">");
		out.newLine();
	}

	private void writeServerTable(BufferedWriter out) throws IOException {
		if (this.serverUris != null) {
			String[] tmpServerUris = this.serverUris.toArray();
			if (tmpServerUris.length > 0) {
				// start tag
				out.write("<" + NodesCategoryTags.ServerUris.name() + ">");
				out.newLine();
				for (String serverUri : tmpServerUris) {
					out.write("<" + NodesCategoryTags.String + ">");
					out.write(serverUri);
					out.write("</" + NodesCategoryTags.String + ">");
					out.newLine();
				}
				// end tag
				out.write("</" + NodesCategoryTags.ServerUris.name() + ">");
			} else {
				// no element
				out.write("<" + NodesCategoryTags.ServerUris.name() + "/>");
			}
			out.newLine();
		}
	}

	/**
	 * Writes <NodeSet> Tag with attributes
	 * 
	 * @param out
	 * @throws IOException
	 * @throws SAXException
	 */
	private void writeStartHeaderDocument(BufferedWriter out) throws IOException {
		// start nodeset tag
		out.write("<" + NodesCategoryTags.NodeSet.name() + " xmlns:i=\"" + ImportNamespaces.XMLSCHEMA.toString()
				+ "\" xmlns=\"" + ImportNamespaces.TYPES.toString() + "\">");
		out.newLine();
	}

	/**
	 * Writes down xml header
	 * 
	 * @param out
	 * @throws IOException
	 */
	private void startDocument(BufferedWriter out) throws IOException {
		// starts document
		//
		out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.newLine();
	}

	public void setProgressMonitor(ICancleOperation monitor, int workCount) {
		this.progressMonitor = monitor;
		this.workCount = workCount;
	}

	/**
	 * Multiplex multi-dimension array (x[][][]) to single-dimension array (x[])
	 * 
	 * @param src multi-dimension array
	 * @return single-dimension array
	 */
	public static Object muxArray(Object src) {
		return muxArray(src, getArrayLengths(src), getComponentType(src.getClass()));
	}

	/**
	 * Multiplex multi-dimension array (x[][][]) to single-dimension array (x[])
	 * 
	 * @param src           multi-dimension array
	 * @param dims
	 * @param componentType
	 * @return single-dimension array
	 */
	public static Object muxArray(Object src, int[] dims, Class<?> componentType) {
		int len = getLength(dims);
		Object dst = Array.newInstance(componentType, len);
		muxArray(src, dims, dst);
		return dst;
	}

	/**
	 * Multiplexes multi-dimension array into a single-dimension array
	 * 
	 * @param src  multi-dimension array
	 * @param dims dimensions
	 * @param dst  single-dimension array
	 */
	public static void muxArray(Object src, int[] dims, Object dst) {
		int len = getLength(dims);
		if (Array.getLength(dst) != len)
			throw new IllegalArgumentException("The length of src does not match the length of dst");
		fillMux(0, dims, src, dst, 0);
	}

	/**
	 * Get the number of dimensions in a multi-dimension array.
	 * 
	 * @param value multi-dimension array
	 * @return the number of dimensions
	 */
	public static int getDimension(Object value) {
		Class<?> clazz = value.getClass();
		String signature = clazz.getName();
		int dim = 0;
		for (; dim < signature.length(); dim++)
			if (signature.charAt(dim) != '[')
				break;
		return dim;
	}

	/**
	 * Get array length of each dimension of a multi-dimension array.
	 * 
	 * @param value multi-dimension array
	 * @return lengths of each dimension
	 */
	public static int[] getArrayLengths(Object value) {
		int dim = getDimension(value);
		int[] result = new int[dim];
		if (dim == 0)
			return result;
		Object o = value;
		for (int i = 0; i < dim; i++) {
			result[i] = o == null ? 0 : Array.getLength(o);
			if (result[i] == 0)
				break;
			o = Array.get(o, 0);
		}
		return result;
	}

	/**
	 * Get the component type of an array class
	 * 
	 * @param clazz (array) class
	 * @return component type
	 */
	public static Class<?> getComponentType(Class<?> clazz) {
		Class<?> result = clazz;
		while (result.isArray())
			result = result.getComponentType();
		return result;
	}

	/**
	 * Returns the total number of elements in a multi-dimension array
	 * 
	 * @param dims lengths of each dimension
	 * @return total number of elements
	 */
	private static int getLength(int[] dims) {
		int len = dims[0];
		for (int i = 1; i < dims.length; i++)
			len *= dims[i];
		return len;
	}

	private static int fillMux(int lvl, int[] dims, Object src, Object dst, int dstIndex) {
		int tmpDstIndex = dstIndex;
		if (lvl == dims.length - 1) {
			int len = dims[lvl];
			System.arraycopy(src, 0, dst, tmpDstIndex, len);
			return tmpDstIndex + len;
		}
		for (int i = 0; i < dims[lvl]; i++)
			tmpDstIndex = fillMux(lvl + 1, dims, Array.get(src, i), dst, tmpDstIndex);
		return 0;
	}
}
