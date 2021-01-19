package opc.sdk.core.classes.ua.core;

public class PropertyType extends BaseVariableType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.PropertyType;

	public PropertyType() {
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

	@Override
	public String toString() {
		return "PropertyType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
