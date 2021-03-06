package opc.sdk.core.classes.ua.core;

public class AuditCertificateMismatchEventType extends AuditCertificateEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.AuditCertificateMismatchEventType;

	public AuditCertificateMismatchEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public String toString() {
		return "AuditCertificateMismatchEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
