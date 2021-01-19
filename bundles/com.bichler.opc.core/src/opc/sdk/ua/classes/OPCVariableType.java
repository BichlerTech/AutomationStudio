package opc.sdk.ua.classes;

import java.util.Set;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.encoding.DecodingException;

import opc.sdk.core.classes.ua.BinaryImporterDecoder;
import opc.sdk.core.context.ISystemContext;
import opc.sdk.ua.AttributesToSave;
import opc.sdk.ua.constants.NodeStateChangeMasks;

public class OPCVariableType extends BaseType {
	private Object value = null;
	private NodeId dataType = null;
	private int valueRank;
	private UnsignedInteger[] arrayDimensions = null;

	public OPCVariableType(BaseNode parent) {
		super(NodeClass.VariableType, parent);
	}

	@Override
	public void update(ISystemContext context, BinaryImporterDecoder decoder, Set<AttributesToSave> attributesToLoad)
			throws DecodingException {
		super.update(context, decoder, attributesToLoad);
		if (attributesToLoad.contains(AttributesToSave.Value)) {
			setWrappedValue(decoder.getVariant(null));
		}
		if (attributesToLoad.contains(AttributesToSave.DataType)) {
			setDataType(decoder.getNodeId(null));
		}
		if (attributesToLoad.contains(AttributesToSave.ValueRank)) {
			setValueRank(decoder.getInt32(null));
		}
		if (attributesToLoad.contains(AttributesToSave.ArrayDimensions)) {
			UnsignedInteger[] arrayDim = decoder.getUInt32Array(null);
			if (arrayDim != null && arrayDim.length > 0) {
				setArrayDimensions(arrayDim);
			} else {
				setArrayDimensions(null);
			}
		}
	}

	/**
	 * Set the value of the variable as a Variant.
	 * 
	 * @param value
	 */
	public void setWrappedValue(Variant value) {
		setValue(extractValueFromVariant(value.getValue()));
	}

	@Override
	protected void initialize(ISystemContext context, BaseNode source) {
		if (source != null && source instanceof OPCVariableType) {
			setValue(((OPCVariableType) source).getValue());
			setDataType(((OPCVariableType) source).getDataType());
			setValueRank(((OPCVariableType) source).getValueRank());
			setArrayDimensions(((OPCVariableType) source).getArrayDimensions());
		}
		this.value = extractValueFromVariant(this.value);
		super.initialize(context, source);
	}

	/**
	 * Sets the value to its default value if it is not valid.
	 * 
	 * @param context
	 * @param value
	 * @param throwOnError
	 * @return
	 */
	protected Object extractValueFromVariant(Object value) {
		return value;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		if (this.value != value) {
			setChangeMask(NodeStateChangeMasks.VALUE);
		}
		this.value = value;
	}

	public NodeId getDataType() {
		return dataType;
	}

	public void setDataType(NodeId dataType) {
		if (this.dataType != dataType) {
			setChangeMask(NodeStateChangeMasks.NONVALUE);
		}
		this.dataType = dataType;
	}

	public int getValueRank() {
		return valueRank;
	}

	public void setValueRank(int valueRank) {
		if (this.valueRank != valueRank) {
			setChangeMask(NodeStateChangeMasks.NONVALUE);
		}
		this.valueRank = valueRank;
	}

	public UnsignedInteger[] getArrayDimensions() {
		return arrayDimensions;
	}

	public void setArrayDimensions(UnsignedInteger[] arrayDimensions) {
		if (this.arrayDimensions != arrayDimensions) {
			setChangeMask(NodeStateChangeMasks.NONVALUE);
		}
		this.arrayDimensions = arrayDimensions;
	}
}
