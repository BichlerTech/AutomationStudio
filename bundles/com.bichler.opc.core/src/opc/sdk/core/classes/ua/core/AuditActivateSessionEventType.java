package opc.sdk.core.classes.ua.core;

public class AuditActivateSessionEventType extends AuditSessionEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.AuditActivateSessionEventType;
	private PropertyType userIdentityToken;
	private PropertyType clientSoftwareCertificates;

	public AuditActivateSessionEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getUserIdentityToken() {
		return userIdentityToken;
	}

	public void setUserIdentityToken(PropertyType value) {
		userIdentityToken = value;
	}

	public PropertyType getClientSoftwareCertificates() {
		return clientSoftwareCertificates;
	}

	public void setClientSoftwareCertificates(PropertyType value) {
		clientSoftwareCertificates = value;
	}

	@Override
	public String toString() {
		return "AuditActivateSessionEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
