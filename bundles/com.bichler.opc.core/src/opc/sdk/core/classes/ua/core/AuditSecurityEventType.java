package opc.sdk.core.classes.ua.core;

public class AuditSecurityEventType extends AuditEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.AuditSecurityEventType;

	public AuditSecurityEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public String toString() {
		return "AuditSecurityEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
