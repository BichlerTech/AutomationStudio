package opc.sdk.core.classes.ua.core;

public class AuditUpdateStateEventType extends AuditUpdateMethodEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.AuditUpdateStateEventType;
	private PropertyType oldStateId;
	private PropertyType newStateId;

	public AuditUpdateStateEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getOldStateId() {
		return oldStateId;
	}

	public void setOldStateId(PropertyType value) {
		oldStateId = value;
	}

	public PropertyType getNewStateId() {
		return newStateId;
	}

	public void setNewStateId(PropertyType value) {
		newStateId = value;
	}

	@Override
	public String toString() {
		return "AuditUpdateStateEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
