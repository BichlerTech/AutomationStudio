package opc.sdk.core.classes.ua.core;

public class ExclusiveLevelAlarmType extends ExclusiveLimitAlarmType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.ExclusiveLevelAlarmType;

	public ExclusiveLevelAlarmType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public String toString() {
		return "ExclusiveLevelAlarmType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
