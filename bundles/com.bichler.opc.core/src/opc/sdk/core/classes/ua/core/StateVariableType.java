package opc.sdk.core.classes.ua.core;

public class StateVariableType extends BaseDataVariableType {
	private static final org.opcfoundation.ua.builtintypes.NodeId ID = CoreIdentifiers.StateVariableType;
	private PropertyType effectiveDisplayName;
	private PropertyType number;
	private PropertyType name;
	private PropertyType id_;

	public StateVariableType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.LocalizedText getValue() {
		return getVariant() != null ? (org.opcfoundation.ua.builtintypes.LocalizedText) getVariant().getValue() : null;
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return ID;
	}

	public PropertyType getEffectiveDisplayName() {
		return effectiveDisplayName;
	}

	public void setEffectiveDisplayName(PropertyType value) {
		effectiveDisplayName = value;
	}

	public PropertyType getNumber() {
		return number;
	}

	public void setNumber(PropertyType value) {
		number = value;
	}

	public PropertyType getName() {
		return name;
	}

	public void setName(PropertyType value) {
		name = value;
	}

	public PropertyType getId() {
		return id_;
	}

	public void setId(PropertyType value) {
		id_ = value;
	}

	@Override
	public String toString() {
		return "StateVariableType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
