package opc.sdk.core.classes.ua.core;

public class NonExclusiveDeviationAlarmType extends NonExclusiveLimitAlarmType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.NonExclusiveDeviationAlarmType;
	private PropertyType setpointNode;

	public NonExclusiveDeviationAlarmType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getSetpointNode() {
		return setpointNode;
	}

	public void setSetpointNode(PropertyType value) {
		setpointNode = value;
	}

	@Override
	public String toString() {
		return "NonExclusiveDeviationAlarmType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
