package opc.sdk.core.classes.ua.core;

public class AuditHistoryValueUpdateEventType extends AuditHistoryUpdateEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.AuditHistoryValueUpdateEventType;
	private PropertyType performInsertReplace;
	private PropertyType updatedNode;
	private PropertyType oldValues;
	private PropertyType newValues;

	public AuditHistoryValueUpdateEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getPerformInsertReplace() {
		return performInsertReplace;
	}

	public void setPerformInsertReplace(PropertyType value) {
		performInsertReplace = value;
	}

	public PropertyType getUpdatedNode() {
		return updatedNode;
	}

	public void setUpdatedNode(PropertyType value) {
		updatedNode = value;
	}

	public PropertyType getOldValues() {
		return oldValues;
	}

	public void setOldValues(PropertyType value) {
		oldValues = value;
	}

	public PropertyType getNewValues() {
		return newValues;
	}

	public void setNewValues(PropertyType value) {
		newValues = value;
	}

	@Override
	public String toString() {
		return "AuditHistoryValueUpdateEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
