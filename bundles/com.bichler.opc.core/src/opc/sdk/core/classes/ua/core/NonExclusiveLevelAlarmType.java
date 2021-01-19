package opc.sdk.core.classes.ua.core;

public class NonExclusiveLevelAlarmType extends NonExclusiveLimitAlarmType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.NonExclusiveLevelAlarmType;

	public NonExclusiveLevelAlarmType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public String toString() {
		return "NonExclusiveLevelAlarmType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
