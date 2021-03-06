package opc.sdk.ua.classes;

import java.util.EnumSet;
import java.util.Set;

import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.ServiceResult;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.AccessLevel;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.encoding.DecodingException;
import org.opcfoundation.ua.utils.NumericRange;

import opc.sdk.core.classes.ua.BinaryImporterDecoder;
import opc.sdk.core.context.ISystemContext;
import opc.sdk.core.enums.ValueRanks;
import opc.sdk.ua.AttributesToSave;
import opc.sdk.ua.IOPCContext;
import opc.sdk.ua.IValueHandler;
import opc.sdk.ua.constants.NodeStateChangeMasks;
import opc.sdk.ua.constants.VariableCopyPolicy;

public abstract class BaseVariableType<T> extends BaseInstance {
	private IValueHandler<T> handlerValue = null;

	public void setValueHandler(IValueHandler<T> handler) {
		this.handlerValue = handler;
	}

	private T value = null;
	private Boolean isValueType = null;
	private DateTime timestamp = null;
	private StatusCode statusCode = null;
	private NodeId dataType = null;
	private Integer valueRank = null;
	private UnsignedInteger[] arrayDimensions = null;
	private Byte accessLevel = null;
	private Byte userAccessLevel = null;
	private Double minimumSamplingInterval = null;
	private Boolean historizing = null;
	private VariableCopyPolicy copyPolicy = null;

	/**
	 * Initializes the instance with its default attribute values.
	 * 
	 * @param nodeClass
	 * @param parent
	 */
	public BaseVariableType(BaseNode parent) {
		super(NodeClass.Variable, parent);
		this.timestamp = DateTime.MIN_VALUE;
		this.accessLevel = userAccessLevel = new Byte("" + AccessLevel.CurrentRead.getValue());
		this.copyPolicy = VariableCopyPolicy.COPYONREAD;
	}

	@Override
	protected void initializeChildren(IOPCContext context) {
	}

	/**
	 * Converts a values contained in a variant to the value defined for the
	 * variable.
	 * 
	 * @param context
	 * @param value
	 * @param throwOnError
	 * @return
	 */
	protected Object extractValueFromVariant(ISystemContext context, Object value, boolean throwOnError) {
		return value;
	}

	protected Object extractValueFromVariant(Object value, Boolean throwOnError) {
		return value;
	}

	@Override
	public void update(ISystemContext context, BinaryImporterDecoder decoder, Set<AttributesToSave> attributesToLoad)
			throws DecodingException {
		super.update(context, decoder, attributesToLoad);
		if (attributesToLoad.contains(AttributesToSave.Value)) {
			setWrappedValue(decoder.getVariant(null));
		}
		if (attributesToLoad.contains(AttributesToSave.StatusCode)) {
			setStatusCode(decoder.getStatusCode(null));
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
		if (attributesToLoad.contains(AttributesToSave.AccessLevel)) {
			setAccessLevels(decoder.getByte(null).byteValue());
		}
		if (attributesToLoad.contains(AttributesToSave.UserAccessLevel)) {
			setUserAccessLevels(decoder.getByte(null).byteValue());
		}
		if (attributesToLoad.contains(AttributesToSave.MinimumSamplingInterval)) {
			setMinimumSamplingInterval(decoder.getDouble(null));
		}
		if (attributesToLoad.contains(AttributesToSave.Historizing)) {
			setHistorizing(decoder.getBoolean(null));
		}
	}

	/**
	 * The value of the variable as a Variant.
	 * 
	 * @param variant
	 */
	public void setWrappedValue(Variant variant) {
		setValue((T) extractValueFromVariant(null, variant.getValue(), false));
	}

	@Override
	protected void initialize(ISystemContext context, BaseNode source) {
		BaseVariableType<?> instance = (BaseVariableType<?>) source;
		if (instance != null) {
			value = (T) extractValueFromVariant(instance.value, false);
			timestamp = instance.timestamp;
			statusCode = instance.statusCode;
			dataType = instance.dataType;
			valueRank = instance.valueRank;
			arrayDimensions = null;
			accessLevel = instance.accessLevel;
			userAccessLevel = instance.userAccessLevel;
			minimumSamplingInterval = instance.minimumSamplingInterval;
			historizing = instance.historizing;
			if (instance.arrayDimensions != null) {
				this.arrayDimensions = instance.arrayDimensions;
			}
			value = (T) extractValueFromVariant(value, false);
		}
		super.initialize(context, source);
	}

	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.BaseVariableType;
	}

	protected NodeId getDefaultDataTypeId() {
		return Identifiers.BaseDataType;
	}

	protected int getDefaultValueRank() {
		return ValueRanks.Any.getValue();
	}

	public NodeId getDataType() {
		return this.dataType;
	}

	public T getValue() {
		if (this.handlerValue != null) {
			return (T) this.handlerValue.getValue();
		}
		return this.value;
	}

	public int getValueRank() {
		return this.valueRank;
	}

	public void setAccessLevels(byte value) {
		this.accessLevel = value;
	}

	public void setArrayDimensions(UnsignedInteger[] value) {
		this.arrayDimensions = value;
	}

	public void setDataType(NodeId value) {
		this.dataType = value;
	}

	public void setHistorizing(boolean isHistorizing) {
		this.historizing = isHistorizing;
	}

	public void setMinimumSamplingInterval(double value) {
		this.minimumSamplingInterval = value;
	}

	public void setCopyPolicy(VariableCopyPolicy copyPolicy) {
		this.copyPolicy = copyPolicy;
	}

	public void setUserAccessLevels(byte value) {
		this.userAccessLevel = value;
	}

	public void setValue(T value) {
		if (this.handlerValue != null) {
			this.handlerValue.setValue(value);
		}
		this.value = value;
	}

	public void setValueRank(int value) {
		this.valueRank = value;
	}

	/**
	 * Get the StatusCode associated with the variable value.
	 * 
	 * @return StatusCode
	 */
	public StatusCode getStatusCode() {
		return this.statusCode;
	}

	/**
	 * Set the StatusCode associated with the variable value.
	 * 
	 * @param StatusCode StatusCode value
	 */
	public void setStatusCode(StatusCode value) {
		if (this.statusCode != value) {
			setChangeMask(NodeStateChangeMasks.VALUE);
		}
		this.statusCode = value;
	}

	public static ServiceResult applyIndexRangeAndDataEncoding(ISystemContext context, NumericRange indexRange,
			QualifiedName dataEncoding, Object value) {
		// apply index range
		if (!indexRange.isEmpty()) {
			boolean ensureValidRange = indexRange.ensureValid(value);
			if (!ensureValidRange) {
				return new ServiceResult(StatusCodes.Bad_IndexRangeInvalid);
			}
		}
		// apply data encoding
		if (!QualifiedName.isNull(dataEncoding)) {
			System.err.println("applyDataEncoding");
		}
		return new ServiceResult(StatusCode.GOOD);
	}
}
