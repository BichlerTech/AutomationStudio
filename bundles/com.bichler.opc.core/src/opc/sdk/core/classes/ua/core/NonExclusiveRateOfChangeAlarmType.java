package opc.sdk.core.classes.ua.core;

public class NonExclusiveRateOfChangeAlarmType extends NonExclusiveLimitAlarmType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.NonExclusiveRateOfChangeAlarmType;

	public NonExclusiveRateOfChangeAlarmType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public String toString() {
		return "NonExclusiveRateOfChangeAlarmType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
