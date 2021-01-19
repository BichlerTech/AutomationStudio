package opc.sdk.core.classes.ua.core;

public class MultiStateDiscreteType extends DiscreteItemType {
	public static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.MultiStateDiscreteType;
	private PropertyType enumStrings;

	public MultiStateDiscreteType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.UnsignedInteger getValue() {
		return getVariant() != null ? (org.opcfoundation.ua.builtintypes.UnsignedInteger) getVariant().getValue()
				: null;
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getEnumStrings() {
		return enumStrings;
	}

	public void setEnumStrings(PropertyType value) {
		enumStrings = value;
	}

	@Override
	public String toString() {
		return "MultiStateDiscreteType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
