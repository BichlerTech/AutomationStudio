package opc.sdk.core.classes.ua.core;

public class AuditHistoryEventUpdateEventType extends AuditHistoryUpdateEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.AuditHistoryEventUpdateEventType;
	private PropertyType updatedNode;
	private PropertyType performInsertReplace;
	private PropertyType oldValues;
	private PropertyType filter;
	private PropertyType newValues;

	public AuditHistoryEventUpdateEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getUpdatedNode() {
		return updatedNode;
	}

	public void setUpdatedNode(PropertyType value) {
		updatedNode = value;
	}

	public PropertyType getPerformInsertReplace() {
		return performInsertReplace;
	}

	public void setPerformInsertReplace(PropertyType value) {
		performInsertReplace = value;
	}

	public PropertyType getOldValues() {
		return oldValues;
	}

	public void setOldValues(PropertyType value) {
		oldValues = value;
	}

	public PropertyType getFilter() {
		return filter;
	}

	public void setFilter(PropertyType value) {
		filter = value;
	}

	public PropertyType getNewValues() {
		return newValues;
	}

	public void setNewValues(PropertyType value) {
		newValues = value;
	}

	@Override
	public String toString() {
		return "AuditHistoryEventUpdateEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
