package opc.sdk.core.classes.ua.core;

public class StateType extends BaseObjectType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.StateType;
	private PropertyType stateNumber;

	public StateType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getStateNumber() {
		return stateNumber;
	}

	public void setStateNumber(PropertyType value) {
		stateNumber = value;
	}

	@Override
	public String toString() {
		return "StateType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
