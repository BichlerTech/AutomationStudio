package opc.sdk.core.classes.ua.core;

public class ExclusiveDeviationAlarmType extends ExclusiveLimitAlarmType {
	public static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.ExclusiveDeviationAlarmType;
	private PropertyType setpointNode;

	public ExclusiveDeviationAlarmType() {
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
		return "ExclusiveDeviationAlarmType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
