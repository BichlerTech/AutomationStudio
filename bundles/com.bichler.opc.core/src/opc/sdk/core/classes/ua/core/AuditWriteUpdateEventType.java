package opc.sdk.core.classes.ua.core;

public class AuditWriteUpdateEventType extends AuditUpdateEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.AuditWriteUpdateEventType;
	private PropertyType indexRange;
	private PropertyType oldValue;
	private PropertyType attributeId;
	private PropertyType newValue;

	public AuditWriteUpdateEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getIndexRange() {
		return indexRange;
	}

	public void setIndexRange(PropertyType value) {
		indexRange = value;
	}

	public PropertyType getOldValue() {
		return oldValue;
	}

	public void setOldValue(PropertyType value) {
		oldValue = value;
	}

	public PropertyType getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(PropertyType value) {
		attributeId = value;
	}

	public PropertyType getNewValue() {
		return newValue;
	}

	public void setNewValue(PropertyType value) {
		newValue = value;
	}

	@Override
	public String toString() {
		return "AuditWriteUpdateEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
