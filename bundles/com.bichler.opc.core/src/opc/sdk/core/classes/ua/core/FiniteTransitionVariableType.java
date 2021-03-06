package opc.sdk.core.classes.ua.core;

public class FiniteTransitionVariableType extends TransitionVariableType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.FiniteTransitionVariableType;
	private PropertyType typeId;

	public FiniteTransitionVariableType() {
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
		return "FiniteTransitionVariableType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
