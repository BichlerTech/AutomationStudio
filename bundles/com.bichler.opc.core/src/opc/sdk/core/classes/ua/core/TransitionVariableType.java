package opc.sdk.core.classes.ua.core;

public class TransitionVariableType extends BaseDataVariableType {
	private static final org.opcfoundation.ua.builtintypes.NodeId ID = CoreIdentifiers.TransitionVariableType;
	private PropertyType number;
	private PropertyType name;
	private PropertyType transitionTime;
	private PropertyType typeId;

	public TransitionVariableType() {
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

	public PropertyType getTransitionTime() {
		return transitionTime;
	}

	public void setTransitionTime(PropertyType value) {
		transitionTime = value;
	}

	public PropertyType getId() {
		return typeId;
	}

	public void setId(PropertyType value) {
		typeId = value;
	}

	@Override
	public String toString() {
		return "TransitionVariableType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
