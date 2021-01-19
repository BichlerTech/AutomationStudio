package opc.sdk.core.classes.ua.core;

public class OffNormalAlarmType extends DiscreteAlarmType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.OffNormalAlarmType;
	private PropertyType normalState;

	public OffNormalAlarmType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getNormalState() {
		return normalState;
	}

	public void setNormalState(PropertyType value) {
		normalState = value;
	}

	@Override
	public String toString() {
		return "OffNormalAlarmType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
