package opc.sdk.core.classes.ua.core;

public class DiscreteAlarmType extends AlarmConditionType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.DiscreteAlarmType;

	public DiscreteAlarmType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public String toString() {
		return "DiscreteAlarmType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
