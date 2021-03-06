package opc.sdk.core.classes.ua.core;

public class LimitAlarmType extends AlarmConditionType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.LimitAlarmType;
	private PropertyType lowLimit;
	private PropertyType highHighLimit;
	private PropertyType highLimit;
	private PropertyType lowLowLimit;

	public LimitAlarmType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getLowLimit() {
		return lowLimit;
	}

	public void setLowLimit(PropertyType value) {
		lowLimit = value;
	}

	public PropertyType getHighHighLimit() {
		return highHighLimit;
	}

	public void setHighHighLimit(PropertyType value) {
		highHighLimit = value;
	}

	public PropertyType getHighLimit() {
		return highLimit;
	}

	public void setHighLimit(PropertyType value) {
		highLimit = value;
	}

	public PropertyType getLowLowLimit() {
		return lowLowLimit;
	}

	public void setLowLowLimit(PropertyType value) {
		lowLowLimit = value;
	}

	@Override
	public String toString() {
		return "LimitAlarmType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
