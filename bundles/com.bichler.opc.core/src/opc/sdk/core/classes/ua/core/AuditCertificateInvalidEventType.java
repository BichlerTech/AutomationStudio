package opc.sdk.core.classes.ua.core;

public class AuditCertificateInvalidEventType extends AuditCertificateEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.AuditCertificateInvalidEventType;

	public AuditCertificateInvalidEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public String toString() {
		return "AuditCertificateInvalidEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
