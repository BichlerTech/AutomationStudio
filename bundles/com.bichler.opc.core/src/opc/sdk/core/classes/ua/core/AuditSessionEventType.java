package opc.sdk.core.classes.ua.core;

public class AuditSessionEventType extends AuditEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.AuditSessionEventType;
	private PropertyType sessionId;

	public AuditSessionEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getSessionId() {
		return sessionId;
	}

	public void setSessionId(PropertyType value) {
		sessionId = value;
	}

	@Override
	public String toString() {
		return "AuditSessionEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
