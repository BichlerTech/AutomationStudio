package opc.sdk.core.classes.ua.core;

public class AuditCreateSessionEventType extends AuditSessionEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.AuditCreateSessionEventType;
	private PropertyType revisedSessionTimeout;
	private PropertyType clientCertificateThumbprint;
	private PropertyType clientCertificate;
	private PropertyType secureChannelId;

	public AuditCreateSessionEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getRevisedSessionTimeout() {
		return revisedSessionTimeout;
	}

	public void setRevisedSessionTimeout(PropertyType value) {
		revisedSessionTimeout = value;
	}

	public PropertyType getClientCertificateThumbprint() {
		return clientCertificateThumbprint;
	}

	public void setClientCertificateThumbprint(PropertyType value) {
		clientCertificateThumbprint = value;
	}

	public PropertyType getClientCertificate() {
		return clientCertificate;
	}

	public void setClientCertificate(PropertyType value) {
		clientCertificate = value;
	}

	public PropertyType getSecureChannelId() {
		return secureChannelId;
	}

	public void setSecureChannelId(PropertyType value) {
		secureChannelId = value;
	}

	@Override
	public String toString() {
		return "AuditCreateSessionEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
