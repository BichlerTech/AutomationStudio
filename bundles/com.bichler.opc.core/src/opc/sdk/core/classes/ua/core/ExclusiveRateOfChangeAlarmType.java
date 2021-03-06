package opc.sdk.core.classes.ua.core;

public class ExclusiveRateOfChangeAlarmType extends ExclusiveLimitAlarmType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.ExclusiveRateOfChangeAlarmType;

	public ExclusiveRateOfChangeAlarmType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public String toString() {
		return "ExclusiveRateOfChangeAlarmType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
