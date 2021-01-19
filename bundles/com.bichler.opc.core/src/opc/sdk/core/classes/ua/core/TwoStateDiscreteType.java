package opc.sdk.core.classes.ua.core;

public class TwoStateDiscreteType extends DiscreteItemType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.TwoStateDiscreteType;
	private PropertyType falseState;
	private PropertyType trueState;

	public TwoStateDiscreteType() {
		super();
	}

	@Override
	public java.lang.Boolean getValue() {
		return getVariant() != null ? (java.lang.Boolean) getVariant().getValue() : null;
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getFalseState() {
		return falseState;
	}

	public void setFalseState(PropertyType value) {
		falseState = value;
	}

	public PropertyType getTrueState() {
		return trueState;
	}

	public void setTrueState(PropertyType value) {
		trueState = value;
	}

	@Override
	public String toString() {
		return "TwoStateDiscreteType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
