package opc.sdk.core.classes.ua.core;

public class AuditHistoryAtTimeDeleteEventType extends AuditHistoryDeleteEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.AuditHistoryAtTimeDeleteEventType;
	private PropertyType oldValues;
	private PropertyType reqTimes;

	public AuditHistoryAtTimeDeleteEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getOldValues() {
		return oldValues;
	}

	public void setOldValues(PropertyType value) {
		oldValues = value;
	}

	public PropertyType getReqTimes() {
		return reqTimes;
	}

	public void setReqTimes(PropertyType value) {
		reqTimes = value;
	}

	@Override
	public String toString() {
		return "AuditHistoryAtTimeDeleteEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
