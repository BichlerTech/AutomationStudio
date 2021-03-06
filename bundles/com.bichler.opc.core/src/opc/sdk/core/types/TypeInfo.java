package opc.sdk.core.types;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.BuiltinsMap;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.Structure;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.UnsignedLong;
import org.opcfoundation.ua.builtintypes.UnsignedShort;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.builtintypes.XmlElement;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.IdType;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.encoding.IEncodeable;
import org.opcfoundation.ua.utils.BijectionMap;
import org.opcfoundation.ua.utils.MultiDimensionArrayUtils;

import opc.sdk.core.enums.BuiltinType;
import opc.sdk.core.enums.ValueRanks;

/**
 * TypeInfo class of an address space type.
 * 
 * @author Thomas Z&ouml;bauer
 * 
 */
public class TypeInfo {
	private Boolean deleted = null;
	private NodeId typeId = null;
	private QualifiedName browsename = null;
	private TypeInfo superType = null;
	private ExpandedNodeId[] encoding = null;
	private Map<NodeId, TypeInfo> subTypes = null;
	private BuiltinType builtInType = null;
	private Integer valueRank = null;
	public static final TypeInfo Unknown = new TypeInfo();

	public TypeInfo() {
		this.subTypes = new HashMap<>();
		this.builtInType = BuiltinType.Null;
	}

	public TypeInfo(BuiltinType builtInType, ValueRanks valueRank) {
		this(builtInType, valueRank.getValue());
	}

	public TypeInfo(BuiltinType builtInType, int valueRank) {
		this.builtInType = builtInType;
		this.valueRank = valueRank;
	}

	public TypeInfo(BuiltinType builtInType, ValueRanks valueRank, NodeId type) {
		this(builtInType, valueRank.getValue());
		this.typeId = type;
	}

	public boolean isTypeOf(NodeId nodeId) {
		TypeInfo info = this.superType;
		while (info != null) {
			if (!info.isDeleted() && nodeId.equals(info.getNodeId())) {
				return true;
			}
			info = info.getSuperType();
		}
		return false;
	}

	public void addSubType(TypeInfo subTypeInfo) {
		this.subTypes.put(subTypeInfo.getNodeId(), subTypeInfo);
	}

	public void removeSubType(NodeId subtypeId) {
		this.subTypes.remove(subtypeId);
	}

	public TypeInfo getSuperType() {
		return this.superType;
	}

	public NodeId getNodeId() {
		return this.typeId;
	}

	public boolean isDeleted() {
		return this.deleted;
	}

	public Set<NodeId> getSubtypes() {
		if (this.subTypes == null) {
			return Collections.emptySet();
		}
		return this.subTypes.keySet();
	}

	void setNodeId(NodeId nodeId) {
		this.typeId = nodeId;
	}

	public void setSuperType(TypeInfo superTypeInfo) {
		this.superType = superTypeInfo;
	}

	public void setDeleted(boolean isDeleted) {
		this.deleted = isDeleted;
	}

	public ExpandedNodeId[] getEncodings() {
		return this.encoding;
	}

	public void setEncodings(ExpandedNodeId[] nodeIds) {
		this.encoding = nodeIds;
	}

	public void setEncodingAt(ExpandedNodeId nodeId, int index) {
		this.encoding[index] = nodeId;
	}

	public QualifiedName getBrowseName() {
		return this.browsename;
	}

	public void setBrowseName(QualifiedName browseName) {
		this.browsename = browseName;
	}

	public static TypeInfo isInstanceOfDataType(DataValue value, NodeId expectedDataType, Integer expectedValueRank,
			NamespaceTable namespaceUris, TypeTable typeTree) {
		/**
		 * C# TypeInfo line 1076
		 */
		BuiltinType expectedType;
		// get the type info
		TypeInfo typeInfo = construct(value, typeTree);
		if (BuiltinType.Null.equals(typeInfo.getBuiltInsType()) || TypeInfo.Unknown.equals(typeInfo)) {
			expectedType = TypeInfo.getBuiltInType(expectedDataType, typeTree);
			// null is allowed for all array types
			if (ValueRanks.Scalar.getValue() != expectedValueRank) {
				return new TypeInfo(expectedType, ValueRanks.OneDimension);
			}
			// check if the type supports null�s
			switch (expectedType) {
			case String:
			case ByteString:
			case XmlElement:
			case NodeId:
			case ExpandedNodeId:
			case LocalizedText:
			case QualifiedName:
			case DataValue:
			case Variant:
			case ExtensionObject:
				return new TypeInfo(expectedType, ValueRanks.Scalar);
			default:
				break;
			}
			// nulls not allowed
			return null;
		}
		// A ByteString is equivalent to an Array of Bytes
		if (BuiltinType.ByteString.equals(typeInfo.getBuiltInsType())
				&& typeInfo.getValueRank() == ValueRanks.Scalar.getValue()
				&& expectedValueRank >= ValueRanks.OneOrMoreDimensions.getValue()) {
			if (typeTree.isTypeOf(expectedDataType, new NodeId(0, BuiltinType.Byte.getValue()))) {
				return typeInfo;
			}
			return null;
		}
		// check the value rank
		int currentValueRank = typeInfo.getValueRank();
		if (!ValueRanks.IsValid(currentValueRank, expectedValueRank)) {
			return null;
		}
		// check for special predefined types
		if (expectedDataType.getIdType().equals(IdType.Numeric) && expectedDataType.getNamespaceIndex() == 0) {
			BuiltinType actualType = typeInfo.getBuiltInsType();
			if (Identifiers.Number.equals(expectedDataType)) {
				switch (actualType) {
				case SByte:
				case Int16:
				case Int32:
				case Int64:
				case Byte:
				case UInt16:
				case UInt32:
				case UInt64:
				case Double:
				case Float:
					return typeInfo;
				case Variant:
					if (typeInfo.getValueRank() == ValueRanks.Scalar.getValue()) {
						return null;
					}
					break;
				default:
					return null;
				}
			} else if (Identifiers.Integer.equals(expectedDataType)) {
				switch (actualType) {
				case SByte:
				case Int16:
				case Int32:
				case Int64:
					return typeInfo;
				case Variant:
					if (typeInfo.getValueRank() == ValueRanks.Scalar.getValue()) {
						return null;
					}
					break;
				default:
					return null;
				}
			} else if (Identifiers.UInteger.equals(expectedDataType)) {
				switch (actualType) {
				case Byte:
				case UInt16:
				case UInt32:
				case UInt64:
					return typeInfo;
				case Variant:
					if (typeInfo.getValueRank() == ValueRanks.Scalar.getValue()) {
						return null;
					}
					break;
				default:
					return null;
				}
			} else if (Identifiers.Boolean.equals(expectedDataType)) {
				if (BuiltinType.Boolean.equals(typeInfo.getBuiltInsType())) {
					return typeInfo;
				}
				return null;
			} else if (Identifiers.Enumeration.equals(expectedDataType)) {
				if (BuiltinType.Int32.equals(typeInfo.getBuiltInsType())) {
					return typeInfo;
				}
				return null;
			} else if (Identifiers.Structure.equals(expectedDataType)) {
				if (BuiltinType.ExtensionObject.equals(typeInfo.getBuiltInsType())) {
					return typeInfo;
				}
				return null;
			} else if (Identifiers.BaseDataType.equals(expectedDataType)) {
				if (!BuiltinType.Variant.equals(typeInfo.getBuiltInsType())) {
					return typeInfo;
				}
				return null;
			}
		}
		// check simple types
		if (!BuiltinType.ExtensionObject.equals(typeInfo.getBuiltInsType())
				&& !BuiltinType.Variant.equals(typeInfo.getBuiltInsType())) {
			if (typeTree.isTypeOf(expectedDataType, new NodeId(0, typeInfo.getBuiltInsType().getValue()))) {
				return typeInfo;
			}
			// check for enumeration
			if (BuiltinType.Int32.equals(typeInfo.getBuiltInsType())
					&& typeTree.isTypeOf(expectedDataType, Identifiers.Enumeration)) {
				return typeInfo;
			}
			// check for direct subtypes of basedatatype
			if (BuiltinType.Variant.equals(getBuiltInType(expectedDataType, typeTree))) {
				return typeInfo;
			}
			return null;
		}
		// handle scalar
		if (typeInfo.getValueRank() < 0) {
			// check extension object vs. expected type
			if (BuiltinType.ExtensionObject.equals(typeInfo.getBuiltInsType())) {
				expectedType = getBuiltInType(expectedDataType, typeTree);
				if (BuiltinType.Variant.equals(expectedType)) {
					return typeInfo;
				}
				if (BuiltinType.ExtensionObject.equals(expectedType)) {
					return typeInfo;
				}
			}
			if (typeTree == null)
				throw new NullPointerException("The type tree is null!");
			// expected type is extension object so compare type tree
			NodeId actualDataTypeId = typeInfo.getDataTypeId(value, namespaceUris, typeTree);
			if (typeTree.isTypeOf(actualDataTypeId, expectedDataType)) {
				return typeInfo;
			}
			return null;
		}
		return typeInfo;
	}

	/**
	 * Returns the data type id that describes a value
	 * 
	 * @param value
	 * @param namespaceUris
	 * @param typeTree
	 * @return
	 */
	private NodeId getDataTypeId(Object value, NamespaceTable namespaceUris, TypeTable typeTree) {
		if (BuiltinType.Null.equals(this.builtInType)) {
			return NodeId.NULL;
		}
		if (BuiltinType.ExtensionObject.equals(this.builtInType)) {
			IEncodeable encodeable = (IEncodeable) value;
			try {
				if (encodeable != null) {
					return namespaceUris.toNodeId(((Structure) encodeable).getTypeId());
				}
				ExtensionObject extension = (ExtensionObject) value;
				if (extension == null)
					throw new NullPointerException("Extension object is null!");
				return typeTree.findDataTypeId(namespaceUris.toNodeId(extension.getTypeId()));
			} catch (ServiceResultException e) {
				// cannot cast expandednodeid to nodeid
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			}
			return Identifiers.Structure;
		}
		return new NodeId(0, this.builtInType.getValue());
	}

	public Integer getValueRank() {
		return this.valueRank;
	}

	private static BuiltinType getBuiltInType(NodeId expectedDataTypeId, TypeTable typeTree) {
		NodeId typeId = expectedDataTypeId;
		while (!NodeId.isNull(typeId)) {
			if (typeId != null && typeId.getNamespaceIndex() == 0 && typeId.getIdType().equals(IdType.Numeric)) {
				BuiltinType id = BuiltinType.getType(typeId);
				if (id != null && id.getValue() > BuiltinType.Null.getValue()
						&& id.getValue() <= BuiltinType.Enumeration.getValue()
						&& id.getValue() != BuiltinType.DiagnosticInfo.getValue()) {
					return id;
				}
			}
			if (typeTree == null) {
				break;
			}
			typeId = typeTree.findSuperType(typeId);
		}
		return BuiltinType.Null;
	}

	/**
	 * Returns the type for the provided value
	 * 
	 * @param value
	 * @param typeTable
	 * @return
	 */
	public static TypeInfo construct(DataValue value, TypeTable typeTable) {
		if (value == null) {
			return TypeInfo.Unknown;
		}
		TypeInfo typeInfo = construct(value.getValue(), typeTable);
		// check for instances of matrix
		if (BuiltinType.Null.equals(typeInfo.getBuiltInsType())) {
			// Matrix
		}
		return typeInfo;
	}

	public static TypeInfo construct(Class<?> type) {
		if (type == null) {
			return TypeInfo.Unknown;
		} else {
			BuiltinType builtinType = getBuiltInType(type);
			if (builtinType != null || !BuiltinType.Null.equals(builtinType)) {
				return new TypeInfo(builtinType, ValueRanks.Scalar.getValue());
			}
		}
		return TypeInfo.Unknown;
	}

	/**
	 * Constructs the typ
	 * 
	 * @param value
	 * @return
	 */
	private static TypeInfo construct(Variant value, TypeTable typeTree) {
		if (value == null || value.isEmpty()) {
			return TypeInfo.Unknown;
		}
		Class<?> type = value.getCompositeClass();
		Class<?> valueTypeClass = value.getValue().getClass();
		String name = type.getName();
		int[] dimension;
		if (valueTypeClass.isArray()) {
			name = valueTypeClass.getCanonicalName();
		}
		dimension = MultiDimensionArrayUtils.getArrayLengths(value.getValue());
		// parse array types
		if (name.charAt(name.length() - 1) == ']') {
			int index = name.indexOf('[');
			if (index != -1) {
				name = name.substring(0, index);
			}
		}
		// handle scalar
		if (dimension == null || dimension.length <= 0) {
			BuiltinType builtInType = getBuiltInType(type);
			if (!BuiltinType.Null.equals(builtInType)) {
				NodeId valueType = getBuiltInTypeNodeId(type);
				return typeTree.findType(valueType, builtInType, ValueRanks.Scalar);
			}
			// check for collection
			if (name.endsWith("Collection")) {
				builtInType = getBuiltInType(name.substring(0, name.length() - "Collection".length()));
				if (!BuiltinType.Null.equals(builtInType)) {
					return new TypeInfo(builtInType, ValueRanks.OneDimension);
				}
				// check for encodable object
				/* isGeneric? */
				return TypeInfo.Unknown;
			}
			// check for generic type
			else {
				if (value.getValue() instanceof Structure) {
					return new TypeInfo(BuiltinType.ExtensionObject, ValueRanks.Scalar);
				}
			}
		}
		if (dimension == null)
			throw new NullPointerException("dimension is null!");
		// handle one dimension array
		if (dimension.length == 1) {
			BuiltinType builtInType = getBuiltInType(type);
			if (BuiltinType.Byte.equals(builtInType) && type == byte[].class) {
				return new TypeInfo(BuiltinType.ByteString, ValueRanks.Scalar);
			}
			if (!BuiltinType.Null.equals(builtInType)) {
				return new TypeInfo(builtInType, ValueRanks.OneDimension);
			}
			return TypeInfo.Unknown;
		}
		// handle multi dimensional array of byte strings
		if (dimension.length > 1) {
			BuiltinType builtInType = getBuiltInType(name);
			if (BuiltinType.Byte.equals(builtInType) && type == byte[].class) {
				return new TypeInfo(BuiltinType.ByteString, dimension.length - 1);
			}
		}
		// handle simple multi dimensional array (enclosing [] + number of
		// //commas)
		BuiltinType builtInType = getBuiltInType(name);
		return new TypeInfo(builtInType, dimension.length);
	}

	private static BuiltinType getBuiltInType(String type) {
		BuiltinType builtInType = null;
		try {
			builtInType = getBuiltInType(Class.forName(type));
		} catch (ClassNotFoundException e) {
			Logger.getLogger(TypeInfo.class.getName()).log(Level.SEVERE, null, e);
			StringBuilder name = new StringBuilder(type);
			name.setCharAt(0, (char) (name.charAt(0) - 32));
			for (int i = 1; i < name.length(); i++) {
				if (name.charAt(i - 1) == ' ' && name.charAt(i) != ' ') {
					name.setCharAt(i, (char) (name.charAt(i) - 32));
				}
			}
			builtInType = BuiltinType.valueOf(name.toString());
		}
		return builtInType;
	}

	private static BuiltinType getBuiltInType(Class<?> type) {
		BijectionMap<NodeId, Class<?>> map = BuiltinsMap.ID_CLASS_MAP;
		NodeId result = map.getLeft(type);
		if (type == LocalizedText.class) {
			return BuiltinType.LocalizedText;
		}
		if (type == QualifiedName.class) {
			return BuiltinType.QualifiedName;
		}
		if (result == null) {
			return BuiltinType.Null;
		}
		return BuiltinType.getType(result);
	}

	private static NodeId getBuiltInTypeNodeId(Class<?> type) {
		BijectionMap<NodeId, Class<?>> map = BuiltinsMap.ID_CLASS_MAP;
		return map.getLeft(type);
	}

	public BuiltinType getBuiltInsType() {
		return this.builtInType;
	}

	/**
	 * Returns the default value for the specified data type and value rank.
	 * 
	 * @param DataType
	 * @param ValueRank
	 * @param TypeTree
	 * @return a default value
	 */
	public static Object getDefaultValue(NodeId dataType, int valueRank, TypeTable typeTree) {
		if (valueRank != ValueRanks.Scalar.getValue()) {
			return null;
		}
		BuiltinType builtinType;
		if (!NodeId.isNull(dataType) && IdType.Numeric.equals(dataType.getIdType())
				&& dataType.getNamespaceIndex() == 0) {
			UnsignedInteger id = (UnsignedInteger) dataType.getValue();
			if (id.intValue() <= ((UnsignedInteger) Identifiers.DiagnosticInfo.getValue()).intValue()) {
				return getDefaultValue(BuiltinType.getType(dataType));
			} else if (Identifiers.Duration.equals(dataType) || Identifiers.Number.equals(dataType)) {
				return (double) 0;
			} else if (Identifiers.Date.equals(dataType) || Identifiers.UtcTime.equals(dataType)) {
				return DateTime.MIN_VALUE;
			} else if (Identifiers.Counter.equals(dataType) || Identifiers.IntegerId.equals(dataType)) {
				return UnsignedInteger.ZERO;
			} else if (Identifiers.UInteger.equals(dataType)) {
				return UnsignedLong.ZERO;
			} else if (Identifiers.Integer.equals(dataType)) {
				return (long) 0;
			} else if (Identifiers.IdType.equals(dataType)) {
				return IdType.Numeric.getValue();
			} else if (Identifiers.Enumeration.equals(dataType)) {
				return 0;
			}
		}
		builtinType = getBuiltInType(dataType, typeTree);
		if (!BuiltinType.Null.equals(builtinType)) {
			return getDefaultValue(builtinType);
		}
		return null;
	}

	/**
	 * Get the default value for the specified built-in type.
	 * 
	 * @param Type Builtin Type
	 * @return a default value
	 */
	public static Object getDefaultValue(BuiltinType type) {
		switch (type) {
		case Boolean:
			return false;
		case SByte:
			return (byte) 0;
		case Byte:
			return UnsignedByte.ZERO;
		case Int16:
			return (short) 0;
		case UInt16:
			return UnsignedShort.ZERO;
		case Int32:
		case Enumeration:
			return 0;
		case UInt32:
			return UnsignedInteger.ZERO;
		case Int64:
		case Integer:
			return (long) 0;
		case UInt64:
		case UInteger:
			return UnsignedLong.ZERO;
		case Float:
			return (float) 0;
		case Double:
		case Number:
			return 0.0;
		case String:
			return "";
		case DateTime:
			return DateTime.MIN_VALUE;
		case Guid:
			return UUID.randomUUID();
		case ByteString:
			return new byte[0];
		case XmlElement:
			return new XmlElement("");
		case StatusCode:
			return StatusCode.GOOD;
		case NodeId:
			return NodeId.NULL;
		case ExpandedNodeId:
			return ExpandedNodeId.NULL;
		case QualifiedName:
			return QualifiedName.NULL;
		case LocalizedText:
			return LocalizedText.EMPTY;
		case Variant:
			return Variant.NULL;
		case DataValue:
			return new DataValue();
		default:
			break;
		}
		return null;
	}

	public static NodeId getDataTypeId(Object value) {
		if (value == null) {
			return NodeId.NULL;
		}
		NodeId dataTypeId = getDataTypeId(value.getClass());
		if (dataTypeId == null || NodeId.isNull(dataTypeId)) {
			// some kind of array
		}
		return dataTypeId;
	}

	public static NodeId getDataTypeId(Class<?> type) {
		TypeInfo typeInfo = TypeInfo.construct(type);
		NodeId dataTypeId = getDataTypeId(typeInfo);
		if (dataTypeId == null || NodeId.isNull(dataTypeId)) {
			// arraytpye
		}
		return dataTypeId;
	}

	public static NodeId getDataTypeId(TypeInfo typeInfo) {
		switch (typeInfo.getBuiltInsType()) {
		case Boolean:
			return Identifiers.Boolean;
		case Byte:
			return Identifiers.Byte;
		case ByteString:
			return Identifiers.ByteString;
		case DataValue:
			return Identifiers.DataValue;
		case DateTime:
			return Identifiers.DateTime;
		case DiagnosticInfo:
			return Identifiers.DiagnosticInfo;
		case Double:
			return Identifiers.Double;
		case Enumeration:
			return Identifiers.Enumeration;
		case ExpandedNodeId:
			return Identifiers.ExpandedNodeId;
		case ExtensionObject:
			return Identifiers.Structure;
		case Float:
			return Identifiers.Float;
		case Guid:
			return Identifiers.Guid;
		case Int16:
			return Identifiers.Int16;
		case Int32:
			return Identifiers.Int32;
		case Int64:
			return Identifiers.Int64;
		case LocalizedText:
			return Identifiers.LocalizedText;
		case NodeId:
			return Identifiers.NodeId;
		case SByte:
			return Identifiers.SByte;
		case Number:
			return Identifiers.Number;
		case QualifiedName:
			return Identifiers.QualifiedName;
		case StatusCode:
			return Identifiers.StatusCode;
		case String:
			return Identifiers.String;
		case UInt16:
			return Identifiers.UInt16;
		case UInt32:
			return Identifiers.UInt32;
		case UInt64:
			return Identifiers.UInt64;
		case UInteger:
			return Identifiers.UInteger;
		case Variant:
			return Identifiers.BaseDataType;
		case Integer:
			return Identifiers.Integer;
		case XmlElement:
			return Identifiers.XmlElement;
		default:
			return NodeId.NULL;
		}
	}

	public TypeInfo cloneInfo(NodeId typeId, BuiltinType builtInType, ValueRanks valueRank) {
		TypeInfo clone = new TypeInfo(builtInType, valueRank, typeId);
		clone.setBrowseName(new QualifiedName(this.browsename.getNamespaceIndex(), this.browsename.getName()));
		clone.setDeleted(this.deleted);
		clone.setSuperType(this.superType);
		clone.setSubTypes(this.subTypes);
		clone.setEncodings(this.encoding);
		return clone;
	}

	void setSubTypes(Map<NodeId, TypeInfo> subTypes) {
		this.subTypes = subTypes;
	}

	void setBuiltInType(BuiltinType builtInType) {
		this.builtInType = builtInType;
	}
}
