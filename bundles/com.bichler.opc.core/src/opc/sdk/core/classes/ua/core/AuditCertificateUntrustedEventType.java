package opc.sdk.core.classes.ua.core;

public class AuditCertificateUntrustedEventType extends AuditCertificateEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.AuditCertificateUntrustedEventType;

	public AuditCertificateUntrustedEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public String toString() {
		return "AuditCertificateUntrustedEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
