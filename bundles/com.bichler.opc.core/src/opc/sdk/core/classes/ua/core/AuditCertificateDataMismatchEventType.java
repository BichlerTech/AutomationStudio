package opc.sdk.core.classes.ua.core;

public class AuditCertificateDataMismatchEventType extends AuditCertificateEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.AuditCertificateDataMismatchEventType;
	private PropertyType invalidUri;
	private PropertyType invalidHostname;

	public AuditCertificateDataMismatchEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getInvalidUri() {
		return invalidUri;
	}

	public void setInvalidUri(PropertyType value) {
		invalidUri = value;
	}

	public PropertyType getInvalidHostname() {
		return invalidHostname;
	}

	public void setInvalidHostname(PropertyType value) {
		invalidHostname = value;
	}

	@Override
	public String toString() {
		return "AuditCertificateDataMismatchEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
