package opc.sdk.core.classes.ua.core;

public class AuditCertificateEventType extends AuditSecurityEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.AuditCertificateEventType;
	private PropertyType certificate;

	public AuditCertificateEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getCertificate() {
		return certificate;
	}

	public void setCertificate(PropertyType value) {
		certificate = value;
	}

	@Override
	public String toString() {
		return "AuditCertificateEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
