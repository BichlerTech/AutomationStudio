package com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map.Entry;

import org.opcfoundation.ua.builtintypes.BuiltinsMap;
import org.opcfoundation.ua.builtintypes.ByteString;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.EnumValueType;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceDescription;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.modeller.controls.CometCombo;
import com.bichler.astudio.opcua.opcmodeler.editor.node.DesignerFormToolkit;
import com.richclientgui.toolbox.validation.converter.IContentsStringConverter;

import opc.sdk.core.enums.BuiltinType;
import opc.sdk.core.enums.ValueRanks;
import opc.sdk.core.node.UAVariableNode;
import opc.sdk.core.types.TypeInfo;
import opc.sdk.core.utils.OPCDateFormat;

public class VariantStringConverter implements IContentsStringConverter<Variant> {
	/**
	 * This combo boxes describe the required value. The followed comment showes the
	 * internal type of the combo box.
	 */
	private NodeId dataTypeId = NodeId.NULL;
	private String dataType = ""; // NodeId
	private CometCombo valueType = null; // BuiltinType
	private CometCombo valueRank = null; // ValueRanks
	private Variant value = null;
	private Boolean isDialog = null;
	/**
	 * EDITOE
	 */
	private Boolean beforeEditorInput = null;

	/**
	 * This instance variables describes the current value
	 */
	// private BuiltinType type = null;
	public VariantStringConverter(String dataType, CometCombo valueType, CometCombo valueRank) {
		this.dataType = dataType;
		this.valueType = valueType;
		this.valueRank = valueRank;
	}

	/**
	 * 
	 * @param dataType
	 * @param valueType
	 * @param valueRank
	 * @param value
	 * @param isNodeEditPart Is always true
	 */
	public VariantStringConverter(String dataType, CometCombo valueType, CometCombo valueRank, boolean isNodeEditPart) {
		this(dataType, valueType, valueRank);
		this.beforeEditorInput = true;
	}

	public void setIsDialog(Boolean isDialog) {
		this.isDialog = isDialog;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public void setValue(Variant value) {
		this.value = value;
	}

	@Override
	public Variant convertFromString(String variantString) {
		return convert(variantString, this.value);
	}

	private Variant convert(String variantString, Variant val) {
		if (beforeEditorInput != null && beforeEditorInput) {
			return Variant.NULL;
		}
		ValueRanks vRank = null;
		if (this.valueRank.getText().isEmpty()) {
			vRank = ValueRanks.Any;
		} else {
			vRank = ValueRanks.valueOf(this.valueRank.getText());
		}
		switch (vRank) {
		case Any:
		case Scalar:
		case ScalarOrOneDimension:
			break;
		default:
			if (isDialog == null && val == null) {
				return null;
			}
			break;
		}
		// fetch a type for the value
		NodeId vType = null;
		BuiltinType iType = null;
		for (Entry<NodeId, String> entry : new DesignerFormToolkit().fetchDataTypes().entrySet()) {
			if (entry.getValue().compareTo(this.dataType) == 0) {
				vType = entry.getKey();
				break;
			}
		}
		for (BuiltinType type : BuiltinType.values()) {
			if (type.name().equals(this.valueType.getText())) {
				iType = type;
				break;
			}
		}
		Variant value = null;
		// check internal type and value rank!
		if (iType != null) {
			TypeInfo t = new TypeInfo(iType, vRank, vType);
			try {
				value = convert(variantString, t, val);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (NullPointerException npe) {
				return null;
			}
		}
		// when the internal type is null
		else {
			value = Variant.NULL;
		}
		return value;
	}

	private Variant convert(String variantString, TypeInfo typeInfo, Variant val)
			throws SecurityException, NoSuchMethodException {
		// class for the value
		Class<?> right = BuiltinsMap.ID_CLASS_MAP.getRight(typeInfo.getBuiltInsType().getBuildinTypeId());
		/**
		 * TODO: ADD manual types which are missing in the map
		 */
		if (typeInfo.getBuiltInsType() == BuiltinType.QualifiedName) {
			right = QualifiedName.class;
		}
		// "add qualified name to the builtinsmap, don't know why it is missing!
		if (right == null) {
			switch (typeInfo.getBuiltInsType()) {
			case LocalizedText:
				right = LocalizedText.class;
				break;
			case QualifiedName:
				right = QualifiedName.class;
				break;
			case Integer:
				right = Integer.class;
				break;
			case UInteger:
				right = UnsignedInteger.class;
				break;
			case Enumeration:
				right = Integer.class;
				break; // return val;
			case Boolean:
				break;
			case Byte:
				break;
			case ByteString:
				break;
			case DataValue:
				break;
			case DateTime:
				break;
			case DiagnosticInfo:
				break;
			case Double:
				break;
			case ExpandedNodeId:
				break;
			case ExtensionObject:
				break;
			case Float:
				break;
			case Guid:
				break;
			case Int16:
				break;
			case Int32:
				break;
			case Int64:
				break;
			case NodeId:
				break;
			case Null:
				break;
			case Number:
				break;
			case SByte:
				break;
			case StatusCode:
				break;
			case String:
				break;
			case UInt16:
				break;
			case UInt32:
				break;
			case UInt64:
				break;
			case Variant:
				break;
			case XmlElement:
				break;
			default:
				break;
			}
		}
		// constructor for the value
		Constructor<?> constructValue = null;
		// variant value
		Variant value = Variant.NULL;
		// value is null
		if (typeInfo.getBuiltInsType() == BuiltinType.Null) {
			return value;
		}
		if (!(variantString != null && !variantString.isEmpty())) {
			return value;
		}
		// no builtintype and it is a complex value
		if (right == null && BuiltinType.ExtensionObject.equals(typeInfo.getBuiltInsType())) {
			return value;
		}
		// builtintype is an int32 (enumeration, index for value) and there is
		// no builtin type
		if (right == null && BuiltinType.Int32.equals(typeInfo.getBuiltInsType())) {
			value = new Variant(new Integer(variantString));
			return value;
		}
		// value is scalar or one dimension
		// or value can be all value ranks
		if (typeInfo.getValueRank() == ValueRanks.ScalarOrOneDimension.getValue()
				|| typeInfo.getValueRank() == ValueRanks.Any.getValue()) {
			/** check if it is a scalar */
			if (variantString.compareTo("doubleclick") == 0) {
				return value;
			} else {
				Object newValue = null;
				// datetime
				if (BuiltinType.DateTime.equals(typeInfo.getBuiltInsType())) {
					Variant dateTimeValue = OPCDateFormat.parseAsVariantDateTime(variantString);
					return dateTimeValue;
				} else if (BuiltinType.LocalizedText.equals(typeInfo.getBuiltInsType())) {
					newValue = new LocalizedText(variantString, "");
				} else {
					constructValue = right.getConstructor(String.class);
					try {
						newValue = constructValue.newInstance(variantString);
					} catch (NumberFormatException nfe) {
						return null;
					} catch (IllegalArgumentException e) {
						return null;
					} catch (InstantiationException e) {
						return null;
					} catch (IllegalAccessException e) {
						return null;
					} catch (InvocationTargetException e) {
						return null;
					}
				}
				value = new Variant(newValue);
			}
		}
		// value is scalar ( no dimension)
		else if (typeInfo.getValueRank() == ValueRanks.Scalar.getValue()) {
			Object newValue = null;
			// datetime
			if (BuiltinType.DateTime.equals(typeInfo.getBuiltInsType())) {
				Variant dateTimeValue = OPCDateFormat.parseAsVariantDateTime(variantString);
				return dateTimeValue;
			} else if (BuiltinType.LocalizedText.equals(typeInfo.getBuiltInsType())) {
				newValue = new LocalizedText(variantString, "");
			} else if (BuiltinType.ExtensionObject.equals(typeInfo.getBuiltInsType())) {
				if (this.value != null) {
					newValue = this.value.getValue();
				}
			} else if (BuiltinType.NodeId == typeInfo.getBuiltInsType()) {
				// TODO: Uncomment if something makes errors
				newValue = val.getValue();
			} else if (BuiltinType.ByteString == typeInfo.getBuiltInsType()) {
				newValue = val.getValue();
			} else {
				/**
				 * Array value cannot be scalar.
				 */
				// if (val != null && val.isArray()) {
				// return null;
				// }
				constructValue = right.getConstructor(String.class);
				try {
					/**
					 * Boolean returns false with any String
					 */
					if (BuiltinType.Boolean == typeInfo.getBuiltInsType()) {
						if (variantString != null) {
							if ("true".equalsIgnoreCase(variantString)) {
								newValue = Boolean.TRUE;
							} else if ("false".equalsIgnoreCase(variantString)) {
								newValue = Boolean.FALSE;
							} else {
								return null;
							}
						}
					} else {
						newValue = constructValue.newInstance(variantString);
					}
				} catch (NumberFormatException nfe) {
					return null;
				} catch (IllegalArgumentException e) {
					return null;
				} catch (InstantiationException e) {
					return null;
				} catch (IllegalAccessException e) {
					return null;
				} catch (InvocationTargetException e) {
					return null;
				}
			}
			value = new Variant(newValue);
		} else if (typeInfo.getValueRank() == ValueRanks.OneOrMoreDimensions.getValue()) {
			if (this.value != null) {
				if (this.value.isArray()) {
					return this.value;
				} else {
					return null;
				}
			}
		}
		// value is an array so we had parsed it on an other place
		else if (typeInfo.getValueRank() == ValueRanks.OneDimension.getValue()) {
			if (this.value != null) {
				// int[] ml =
				// MultiDimensionArrayUtils.getArrayLengths(this.value
				// .getValue());
				//
				// Object ains = Array.newInstance(right, ml);
				// Class<?>[] cA = new Class<?>[1];
				//
				// variantString.split(",");
				//
				// for (int i = 0; i < ml[0]; i++) {
				// Object avins = construct(right, typeInfo, variantString);
				// ((Object[]) ains)[i] = avins;
				// }
				return this.value;
			}
			// right.
		} else if (typeInfo.getValueRank() == ValueRanks.TwoDimensions.getValue()) {
			if (this.value != null) {
				return this.value;
			}
		} else if (typeInfo.getValueRank() == ValueRanks.ThreeDimensions.getValue()) {
			if (this.value != null) {
				return this.value;
			}
		} else if (typeInfo.getValueRank() == ValueRanks.FourDimensions.getValue()) {
			if (this.value != null) {
				return this.value;
			}
		} else if (typeInfo.getValueRank() == ValueRanks.FiveDimensions.getValue()) {
			if (this.value != null) {
				return this.value;
			}
		}
		return value;
	}

	private Object construct(Class<?> right, TypeInfo typeInfo, String variantString)
			throws NoSuchMethodException, SecurityException {
		// constructor for the value
		Constructor<?> constructValue = null;
		// variant value
		Object value = null;
		// value is null
		if (typeInfo.getBuiltInsType() == BuiltinType.Null) {
			return value;
		}
		if (!(variantString != null && !variantString.isEmpty())) {
			return value;
		}
		// no builtintype and it is a complex value
		if (right == null && BuiltinType.ExtensionObject.equals(typeInfo.getBuiltInsType())) {
			return value;
		}
		// builtintype is an int32 (enumeration, index for value) and there is
		// no builtin type
		if (right == null && BuiltinType.Int32.equals(typeInfo.getBuiltInsType())) {
			value = new Integer(variantString);
			return value;
		}
		// value is scalar or one dimension
		// or value can be all value ranks
		Object newValue = null;
		// datetime
		if (BuiltinType.DateTime.equals(typeInfo.getBuiltInsType())) {
			Object dateTimeValue = OPCDateFormat.parseAsVariantDateTime(variantString).getValue();
			return dateTimeValue;
		} else if (BuiltinType.LocalizedText.equals(typeInfo.getBuiltInsType())) {
			newValue = new LocalizedText(variantString, "");
		} else {
			constructValue = right.getConstructor(String.class);
			try {
				newValue = constructValue.newInstance(variantString);
			} catch (NumberFormatException nfe) {
				return null;
			} catch (IllegalArgumentException e) {
				return null;
			} catch (InstantiationException e) {
				return null;
			} catch (IllegalAccessException e) {
				return null;
			} catch (InvocationTargetException e) {
				return null;
			}
		}
		return newValue;
	}

	/**
	 * Remember if the value is an array, ',' is the delimiter
	 */
	@Override
	public String convertToString(Variant value) {
		if (value.isEmpty()) {
			return "";
		}
		if (value.isArray()) {
			String toStringForm = new String();
			for (Object valueToString : (Object[]) value.getValue()) {
				if (toStringForm.isEmpty()) {
					toStringForm += valueToString.toString();
				} else {
					toStringForm += " , " + valueToString.toString();
				}
			}
			return toStringForm;
		}
		switch (dataType) {
		case "Enumeration":
			String txt = getEnumStrings(this.dataTypeId, value);
			if (txt == null) {
				return "";
			}
			return value.getValue().toString() + " (" + txt + ")";
		case "ByteString":
			byte[] byteStringValue = ((ByteString)value.getValue()).getValue();
			return new String(byteStringValue);
		default:
			return value.getValue().toString();
		}
	}

	public void setAfterEditorInput() {
		this.beforeEditorInput = false;
	}

	private String getEnumStrings(NodeId datatypeId, Variant value) {
		BrowseDescription nodesToBrowse = new BrowseDescription();
		nodesToBrowse.setBrowseDirection(BrowseDirection.Forward);
		nodesToBrowse.setIncludeSubtypes(true);
		nodesToBrowse.setNodeClassMask(NodeClass.getMask(NodeClass.Variable));
		nodesToBrowse.setNodeId(datatypeId);
		nodesToBrowse.setReferenceTypeId(Identifiers.HierarchicalReferences);
		nodesToBrowse.setResultMask(BrowseResultMask.getMask(BrowseResultMask.ALL));
		// LocalizedText[] txt = new LocalizedText[0];
		String txt = "";
		int ordinal = 0;

		if (value.getCompositeClass() == Integer.class) {
			ordinal = value.intValue();
		}
		try {
			BrowseResult[] result = ServerInstance.getInstance().getServerInstance().getMaster()
					.browse(new BrowseDescription[] { nodesToBrowse }, UnsignedInteger.ZERO, null, null);
			if (result != null && result.length > 0 && result[0].getReferences() != null) {
				for (ReferenceDescription refDesc : result[0].getReferences()) {
					ExpandedNodeId nodeId = refDesc.getNodeId();
					UAVariableNode valueNode = (UAVariableNode) ServerInstance.getNode(nodeId);
					if (valueNode.getValue().getValue() instanceof LocalizedText[]) {
						txt = "" + ((LocalizedText[]) valueNode.getValue().getValue())[ordinal];
					} else if (valueNode.getValue().getValue() instanceof EnumValueType[]) {
						EnumValueType[] values = (EnumValueType[]) valueNode.getValue().getValue();
						for (EnumValueType val : values) {
							if (val.getValue() == ordinal)
								return "" + val.getDisplayName();
						}
					}
				}
			}
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
		return txt;
	}

	public void setDataTypeId(NodeId nodeId) {
		this.dataTypeId = nodeId;
	}
}
