package opc.sdk.ua.classes;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.MessageSecurityMode;
import org.opcfoundation.ua.core.SessionSecurityDiagnosticsDataType;

import opc.sdk.core.enums.ValueRanks;

public class SessionSecurityDiagnosticsType extends BaseDataVariableType<SessionSecurityDiagnosticsDataType> {
	protected BaseDataVariableType<NodeId> sessionId = null;
	protected BaseDataVariableType<String> clientUserIdOfSession = null;
	protected BaseDataVariableType<String[]> clientUserIdHistory = null;
	protected BaseDataVariableType<String> authenticationMechanism = null;
	protected BaseDataVariableType<String> encoding = null;
	protected BaseDataVariableType<String> transportProtocol = null;
	protected BaseDataVariableType<MessageSecurityMode> securityMode = null;
	protected BaseDataVariableType<String> securityPolicyUri = null;
	protected BaseDataVariableType<byte[]> clientCertificate = null;

	protected SessionSecurityDiagnosticsType(BaseNode parent) {
		super(parent);
	}

	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.SessionSecurityDiagnosticsType;
	}

	@Override
	protected NodeId getDefaultDataTypeId() {
		return Identifiers.SessionSecurityDiagnosticsDataType;
	}

	@Override
	protected int getDefaultValueRank() {
		return ValueRanks.Scalar.getValue();
	}

	/**
	 * Get the description for the SessionId Variable.
	 * 
	 * @return SessionId
	 */
	public BaseDataVariableType<NodeId> getSessionId() {
		return sessionId;
	}

	/**
	 * Set the description for the SessionId Variable.
	 * 
	 * @param SessionId SessionId Variable
	 */
	public void setSessionId(BaseDataVariableType<NodeId> sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * Get the description for the ClientUserIdOfSession Variable.
	 * 
	 * @return ClientUserIdOfSession
	 */
	public BaseDataVariableType<String> getClientUserIdOfSession() {
		return clientUserIdOfSession;
	}

	/**
	 * Set the description for the ClientUserIdOfSession Variable.
	 * 
	 * @param ClientUserIdOfSession ClientUserIdOfSession Variable
	 */
	public void setClientUserIdOfSession(BaseDataVariableType<String> clientUserIdOfSession) {
		this.clientUserIdOfSession = clientUserIdOfSession;
	}

	/**
	 * Get the description for the ClientUserIdHistory Variable.
	 * 
	 * @return ClientUserIdHistory
	 */
	public BaseDataVariableType<String[]> getClientUserIdHistory() {
		return clientUserIdHistory;
	}

	/**
	 * Set the description for the ClientUserIdHistory Variable.
	 * 
	 * @param ClientUserIdHistory ClientUserIdHistory Variable
	 */
	public void setClientUserIdHistory(BaseDataVariableType<String[]> clientUserIdHistory) {
		this.clientUserIdHistory = clientUserIdHistory;
	}

	/**
	 * Get the description for the AuthenticationMechanism Variable.
	 * 
	 * @return AuthenticationMechanism
	 */
	public BaseDataVariableType<String> getAuthenticationMechanism() {
		return authenticationMechanism;
	}

	/**
	 * Set the description for the AuthenticationMechanism Variable.
	 * 
	 * @param AuthenticationMechanism AuthenticationMechanism Variable
	 */
	public void setAuthenticationMechanism(BaseDataVariableType<String> authenticationMechanism) {
		this.authenticationMechanism = authenticationMechanism;
	}

	/**
	 * Get the description for the Encoding Variable.
	 * 
	 * @return Encoding
	 */
	public BaseDataVariableType<String> getEncoding() {
		return encoding;
	}

	/**
	 * Set the description for the Encoding Variable.
	 * 
	 * @param Encoding Encoding Variable
	 */
	public void setEncoding(BaseDataVariableType<String> encoding) {
		this.encoding = encoding;
	}

	/**
	 * Get the description for the TransportProtocol Variable.
	 * 
	 * @return TransportProtocol
	 */
	public BaseDataVariableType<String> getTransportProtocol() {
		return transportProtocol;
	}

	/**
	 * Set the description for the TransportProtocol Variable.
	 * 
	 * @param TransportProtocol TransportProtocol Variable
	 */
	public void setTransportProtocol(BaseDataVariableType<String> transportProtocol) {
		this.transportProtocol = transportProtocol;
	}

	/**
	 * Get the description for the SecurityMode Variable.
	 * 
	 * @return SecurityMode
	 */
	public BaseDataVariableType<MessageSecurityMode> getSecurityMode() {
		return securityMode;
	}

	/**
	 * Set the description for the SecurityMode Variable.
	 * 
	 * @param SecurityMode SecurityMode Variable
	 */
	public void setSecurityMode(BaseDataVariableType<MessageSecurityMode> securityMode) {
		this.securityMode = securityMode;
	}

	/**
	 * Get the description for the SecurityPolicyUri Variable.
	 * 
	 * @return SecurityPolicyUri
	 */
	public BaseDataVariableType<String> getSecurityPolicyUri() {
		return securityPolicyUri;
	}

	/**
	 * Set the description for the SecurityPolicyUri Variable.
	 * 
	 * @param SecurityMode SecurityPolicyUri Variable
	 */
	public void setSecurityPolicyUri(BaseDataVariableType<String> securityPolicyUri) {
		this.securityPolicyUri = securityPolicyUri;
	}

	/**
	 * Get the description for the ClientCertificate Variable.
	 * 
	 * @return ClientCertificate
	 */
	public BaseDataVariableType<byte[]> getClientCertificate() {
		return clientCertificate;
	}

	/**
	 * Set the description for the ClientCertificate Variable.
	 * 
	 * @param ClientCertificate ClientCertificate Variable
	 */
	public void setClientCertificate(BaseDataVariableType<byte[]> clientCertificate) {
		this.clientCertificate = clientCertificate;
	}
}
