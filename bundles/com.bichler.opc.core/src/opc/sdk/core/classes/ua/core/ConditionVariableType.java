package opc.sdk.core.classes.ua.core;

public class ConditionVariableType extends BaseDataVariableType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.ConditionVariableType;
	private PropertyType sourceTimestamp;

	public ConditionVariableType() {
		super();
	}

	@Override
	public java.lang.Object getValue() {
		return getVariant() != null ? getVariant().getValue() : null;
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getSourceTimestamp() {
		return sourceTimestamp;
	}

	public void setSourceTimestamp(PropertyType value) {
		sourceTimestamp = value;
	}

	@Override
	public String toString() {
		return "ConditionVariableType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
