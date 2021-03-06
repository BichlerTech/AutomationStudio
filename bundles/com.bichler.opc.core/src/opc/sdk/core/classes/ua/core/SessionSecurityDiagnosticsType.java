package opc.sdk.core.classes.ua.core;

public class SessionSecurityDiagnosticsType extends BaseDataVariableType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.SessionSecurityDiagnosticsType;
	private BaseDataVariableType clientUserIdOfSession;
	private BaseDataVariableType clientUserIdHistory;
	private BaseDataVariableType encoding;
	private BaseDataVariableType securityPolicyUri;
	private BaseDataVariableType transportProtocol;
	private BaseDataVariableType clientCertificate;
	private BaseDataVariableType securityMode;
	private BaseDataVariableType authenticationMechanism;
	private BaseDataVariableType sessionId;

	public SessionSecurityDiagnosticsType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.core.SessionSecurityDiagnosticsDataType getValue() {
		return getVariant() != null
				? (org.opcfoundation.ua.core.SessionSecurityDiagnosticsDataType) getVariant().getValue()
				: null;
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public BaseDataVariableType getClientUserIdOfSession() {
		return clientUserIdOfSession;
	}

	public void setClientUserIdOfSession(BaseDataVariableType value) {
		clientUserIdOfSession = value;
	}

	public BaseDataVariableType getClientUserIdHistory() {
		return clientUserIdHistory;
	}

	public void setClientUserIdHistory(BaseDataVariableType value) {
		clientUserIdHistory = value;
	}

	public BaseDataVariableType getEncoding() {
		return encoding;
	}

	public void setEncoding(BaseDataVariableType value) {
		encoding = value;
	}

	public BaseDataVariableType getSecurityPolicyUri() {
		return securityPolicyUri;
	}

	public void setSecurityPolicyUri(BaseDataVariableType value) {
		securityPolicyUri = value;
	}

	public BaseDataVariableType getTransportProtocol() {
		return transportProtocol;
	}

	public void setTransportProtocol(BaseDataVariableType value) {
		transportProtocol = value;
	}

	public BaseDataVariableType getClientCertificate() {
		return clientCertificate;
	}

	public void setClientCertificate(BaseDataVariableType value) {
		clientCertificate = value;
	}

	public BaseDataVariableType getSecurityMode() {
		return securityMode;
	}

	public void setSecurityMode(BaseDataVariableType value) {
		securityMode = value;
	}

	public BaseDataVariableType getAuthenticationMechanism() {
		return authenticationMechanism;
	}

	public void setAuthenticationMechanism(BaseDataVariableType value) {
		authenticationMechanism = value;
	}

	public BaseDataVariableType getSessionId() {
		return sessionId;
	}

	public void setSessionId(BaseDataVariableType value) {
		sessionId = value;
	}

	@Override
	public String toString() {
		return "SessionSecurityDiagnosticsType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
