package opc.sdk.core.classes.ua.core;

public class AuditCertificateExpiredEventType extends AuditCertificateEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.AuditCertificateExpiredEventType;

	public AuditCertificateExpiredEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public String toString() {
		return "AuditCertificateExpiredEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
