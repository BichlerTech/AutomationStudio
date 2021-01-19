package opc.sdk.core.classes.ua.core;

public class TwoStateVariableType extends StateVariableType {
	private static final org.opcfoundation.ua.builtintypes.NodeId ID = CoreIdentifiers.TwoStateVariableType;
	private PropertyType falseState;
	private PropertyType typeId;
	private PropertyType transitionTime;
	private PropertyType trueState;
	private PropertyType effectiveTransitionTime;

	public TwoStateVariableType() {
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

	public PropertyType getFalseState() {
		return falseState;
	}

	public void setFalseState(PropertyType value) {
		falseState = value;
	}

	@Override
	public PropertyType getId() {
		return typeId;
	}

	@Override
	public void setId(PropertyType value) {
		typeId = value;
	}

	public PropertyType getTransitionTime() {
		return transitionTime;
	}

	public void setTransitionTime(PropertyType value) {
		transitionTime = value;
	}

	public PropertyType getTrueState() {
		return trueState;
	}

	public void setTrueState(PropertyType value) {
		trueState = value;
	}

	public PropertyType getEffectiveTransitionTime() {
		return effectiveTransitionTime;
	}

	public void setEffectiveTransitionTime(PropertyType value) {
		effectiveTransitionTime = value;
	}

	@Override
	public String toString() {
		return "TwoStateVariableType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
