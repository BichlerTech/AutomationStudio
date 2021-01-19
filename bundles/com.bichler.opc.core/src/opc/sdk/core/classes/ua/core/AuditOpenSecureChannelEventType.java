package opc.sdk.core.classes.ua.core;

public class AuditOpenSecureChannelEventType extends AuditChannelEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.AuditOpenSecureChannelEventType;
	private PropertyType clientCertificateThumbprint;
	private PropertyType requestType;
	private PropertyType securityMode;
	private PropertyType requestedLifetime;
	private PropertyType securityPolicyUri;
	private PropertyType clientCertificate;

	public AuditOpenSecureChannelEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getClientCertificateThumbprint() {
		return clientCertificateThumbprint;
	}

	public void setClientCertificateThumbprint(PropertyType value) {
		clientCertificateThumbprint = value;
	}

	public PropertyType getRequestType() {
		return requestType;
	}

	public void setRequestType(PropertyType value) {
		requestType = value;
	}

	public PropertyType getSecurityMode() {
		return securityMode;
	}

	public void setSecurityMode(PropertyType value) {
		securityMode = value;
	}

	public PropertyType getRequestedLifetime() {
		return requestedLifetime;
	}

	public void setRequestedLifetime(PropertyType value) {
		requestedLifetime = value;
	}

	public PropertyType getSecurityPolicyUri() {
		return securityPolicyUri;
	}

	public void setSecurityPolicyUri(PropertyType value) {
		securityPolicyUri = value;
	}

	public PropertyType getClientCertificate() {
		return clientCertificate;
	}

	public void setClientCertificate(PropertyType value) {
		clientCertificate = value;
	}

	@Override
	public String toString() {
		return "AuditOpenSecureChannelEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
