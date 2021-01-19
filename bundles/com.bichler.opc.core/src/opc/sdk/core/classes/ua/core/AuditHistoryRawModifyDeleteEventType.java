package opc.sdk.core.classes.ua.core;

public class AuditHistoryRawModifyDeleteEventType extends AuditHistoryDeleteEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.AuditHistoryRawModifyDeleteEventType;
	private PropertyType startTime;
	private PropertyType oldValues;
	private PropertyType isDeleteModified;
	private PropertyType endTime;

	public AuditHistoryRawModifyDeleteEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getStartTime() {
		return startTime;
	}

	public void setStartTime(PropertyType value) {
		startTime = value;
	}

	public PropertyType getOldValues() {
		return oldValues;
	}

	public void setOldValues(PropertyType value) {
		oldValues = value;
	}

	public PropertyType getIsDeleteModified() {
		return isDeleteModified;
	}

	public void setIsDeleteModified(PropertyType value) {
		isDeleteModified = value;
	}

	public PropertyType getEndTime() {
		return endTime;
	}

	public void setEndTime(PropertyType value) {
		endTime = value;
	}

	@Override
	public String toString() {
		return "AuditHistoryRawModifyDeleteEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
