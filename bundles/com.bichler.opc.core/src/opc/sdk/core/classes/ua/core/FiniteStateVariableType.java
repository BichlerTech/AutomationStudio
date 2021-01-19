package opc.sdk.core.classes.ua.core;

public class FiniteStateVariableType extends StateVariableType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.FiniteStateVariableType;
	private PropertyType typeId;

	public FiniteStateVariableType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.LocalizedText getValue() {
		return getVariant() != null ? (org.opcfoundation.ua.builtintypes.LocalizedText) getVariant().getValue() : null;
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public PropertyType getId() {
		return typeId;
	}

	@Override
	public void setId(PropertyType value) {
		typeId = value;
	}

	@Override
	public String toString() {
		return "FiniteStateVariableType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
