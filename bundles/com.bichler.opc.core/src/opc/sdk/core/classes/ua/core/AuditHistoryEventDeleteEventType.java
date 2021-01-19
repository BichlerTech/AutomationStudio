package opc.sdk.core.classes.ua.core;

public class AuditHistoryEventDeleteEventType extends AuditHistoryDeleteEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.AuditHistoryEventDeleteEventType;
	private PropertyType oldValues;
	private PropertyType eventIds;

	public AuditHistoryEventDeleteEventType() {
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

	public PropertyType getEventIds() {
		return eventIds;
	}

	public void setEventIds(PropertyType value) {
		eventIds = value;
	}

	@Override
	public String toString() {
		return "AuditHistoryEventDeleteEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
