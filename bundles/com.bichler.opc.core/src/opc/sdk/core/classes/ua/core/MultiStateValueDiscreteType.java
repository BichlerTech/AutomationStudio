package opc.sdk.core.classes.ua.core;

public class MultiStateValueDiscreteType extends DiscreteItemType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.MultiStateValueDiscreteType;
	private PropertyType enumValues;

	public MultiStateValueDiscreteType() {
		super();
	}

	@Override
	public java.lang.Number getValue() {
		return getVariant() != null ? (java.lang.Number) getVariant().getValue() : null;
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getEnumValues() {
		return enumValues;
	}

	public void setEnumValues(PropertyType value) {
		enumValues = value;
	}

	@Override
	public String toString() {
		return "MultiStateValueDiscreteType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
