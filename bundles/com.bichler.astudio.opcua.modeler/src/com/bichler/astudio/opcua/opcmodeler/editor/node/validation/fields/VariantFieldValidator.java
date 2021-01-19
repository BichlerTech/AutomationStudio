package com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.widgets.Text;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.utils.MultiDimensionArrayUtils;

import com.bichler.astudio.opcua.modeller.controls.CometCombo;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters.ArrayDimStringConverter;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.richclientgui.toolbox.validation.validator.IFieldValidator;

import opc.sdk.core.enums.BuiltinType;
import opc.sdk.core.enums.ValueRanks;

public class VariantFieldValidator implements IFieldValidator<Variant> {
	private CometCombo combo_valueType;
	private Text textField;
	private CometCombo combo_valueRank;
	private Text txt_arrayDimension;
	private Map<ValueRanks, Map<NodeId, Variant>> internalValueCache;

	public VariantFieldValidator(CometCombo valueType, CometCombo valueRank, Text arrayDimension,
			Map<ValueRanks, Map<NodeId, Variant>> valueCache) {
		super();
		this.combo_valueType = valueType;
		this.combo_valueRank = valueRank;
		this.txt_arrayDimension = arrayDimension;
		this.internalValueCache = valueCache;
	}

	@Override
	public String getErrorMessage() {
		if (this.textField != null && !this.textField.isDisposed() && this.textField.getText().isEmpty()) {
			return getWarningMessage();
		}
		return CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.value.type");
	}

	@Override
	public String getWarningMessage() {
		return CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.value.null");
	}

	private final ArrayDimStringConverter ARRAYDIM_CONVERTER = new ArrayDimStringConverter();

	@Override
	public boolean isValid(Variant value) {
		/** isvalid declaration */
		boolean isValid = false;
		ValueRanks valueRank = null;
		BuiltinType builtinType = null;
		String selection = this.combo_valueType.getText();
		UnsignedInteger[] currentDimensions = ARRAYDIM_CONVERTER.convertFromString(this.txt_arrayDimension.getText());
		/** get value ranks */
		String vr = this.combo_valueRank.getText();
		if (vr.isEmpty()) {
			valueRank = ValueRanks.Any;
		} else {
			valueRank = ValueRanks.valueOf(vr);
		}
		if (selection.isEmpty()) {
			isValid = false;
		} else {
			// buildins from selection
			builtinType = BuiltinType.valueOf(selection);
			switch (builtinType) {
			case Null:
				if (value == null) {
					isValid = true;
				} else if (value.isEmpty()) {
					isValid = true;
				}
				break;
			default:
				boolean arrayRequried = false;
				switch (valueRank) {
				case Scalar:
					arrayRequried = false;
					switch (builtinType) {
					case ByteString:
						isValid = true;
						break;
					case Boolean:
						break;
					case Byte:
						break;
					case DataValue:
						break;
					case DateTime:
						break;
					case DiagnosticInfo:
						break;
					case Double:
						break;
					case Enumeration:
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
					case Integer:
						break;
					case LocalizedText:
						break;
					case NodeId:
						break;
					case Null:
						break;
					case Number:
						break;
					case QualifiedName:
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
					case UInteger:
						break;
					case Variant:
						break;
					case XmlElement:
						break;
					default:
						break;
					}
					break;
				case Any:
				case ScalarOrOneDimension:
					break;
				// check value with its array dimension
				default:
					arrayRequried = true;
					if (value == null) {
						isValid = true;
						break;
					} else if (value.isEmpty()) {
						isValid = true;
						break;
					}
					Object arrayValue = value.getValue();
					int[] lengths = MultiDimensionArrayUtils.getArrayLengths(arrayValue);
					// validate more than one or more dimension
					if (valueRank != ValueRanks.OneOrMoreDimensions && lengths.length != currentDimensions.length) {
						isValid = false;
						break;
					}
					for (int i = 0; i < currentDimensions.length; i++) {
						int dim = currentDimensions[i].intValue();
						if (dim == 0) {
							continue;
						}
						int dimLength = lengths[i];
						if (dim != dimLength) {
							isValid = false;
							break;
						}
					}
					break;
				} // end default
					//
				if (value != null) {
					if (arrayRequried && value.isArray()) {
						isValid = true;
						break;
					} else if (!arrayRequried && !value.isArray()) {
						isValid = true;
						break;
					}
				}
				break;
			}
		}
		// complex builtin types
		if (isValid && builtinType != null) {
			switch (builtinType) {
			case DateTime:
				isValid = true;
				break;
			case Boolean:
				break;
			case Byte:
				break;
			case ByteString:
				break;
			case DataValue:
				break;
			case DiagnosticInfo:
				break;
			case Double:
				break;
			case Enumeration:
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
			case Integer:
				break;
			case LocalizedText:
				break;
			case NodeId:
				break;
			case Null:
				break;
			case Number:
				break;
			case QualifiedName:
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
			case UInteger:
				break;
			case Variant:
				break;
			case XmlElement:
				break;
			default:
				break;
			}
		}
		// set value to cache
		if (isValid && valueRank != null && builtinType != null) {
			Map<NodeId, Variant> values2valuerank = this.internalValueCache.get(valueRank);
			if (values2valuerank == null) {
				values2valuerank = new HashMap<NodeId, Variant>();
				this.internalValueCache.put(valueRank, values2valuerank);
			}
			values2valuerank.put(builtinType.getBuildinTypeId(), value);
		}
		return isValid;
	}

	@Override
	public boolean warningExist(Variant value) {
		return false;
	}

	public void setTextField(Text control) {
		this.textField = control;
	}
}
