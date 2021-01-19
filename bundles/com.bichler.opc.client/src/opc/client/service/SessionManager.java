package opc.client.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.ByteString;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.ServiceResponse;
import org.opcfoundation.ua.builtintypes.ServiceResult;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.common.ServiceFaultException;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.ActivateSessionResponse;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.core.ApplicationType;
import org.opcfoundation.ua.core.CancelRequest;
import org.opcfoundation.ua.core.CancelResponse;
import org.opcfoundation.ua.core.CloseSessionResponse;
import org.opcfoundation.ua.core.CreateSessionResponse;
import org.opcfoundation.ua.core.EndpointConfiguration;
import org.opcfoundation.ua.core.EndpointDescription;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.SignatureData;
import org.opcfoundation.ua.core.SignedSoftwareCertificate;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.core.UserIdentityToken;
import org.opcfoundation.ua.core.UserTokenPolicy;
import org.opcfoundation.ua.encoding.EncoderContext;
import org.opcfoundation.ua.encoding.EncodingException;
import org.opcfoundation.ua.transport.AsyncResult;
import org.opcfoundation.ua.transport.security.Cert;
import org.opcfoundation.ua.transport.security.CertificateValidator;
import org.opcfoundation.ua.transport.security.SecurityAlgorithm;
import org.opcfoundation.ua.transport.security.SecurityConstants;
import org.opcfoundation.ua.transport.security.SecurityPolicy;
import org.opcfoundation.ua.utils.CryptoUtil;
import org.opcfoundation.ua.utils.TimerUtil;

import opc.client.application.core.ApplicationConfiguration;
import opc.sdk.core.enums.RequestType;
import opc.sdk.core.session.UserIdentity;
import opc.sdk.core.session.UserIdentityRole;
import opc.sdk.core.utils.ArrayUtils;

/**
 * The session manager handles all created sessions for the UAClient
 * application. Usually there is 1 active session which is able to change with
 * the changeActiveSession method.
 * 
 * @author Thomas Z&ouml;chbauer
 * @since 23.05.2012, HB-Softsolution e.U.
 */
public class SessionManager {
	/** A default session name */
	public static final String DEFAULTSESSIONNAME = "OPCUA HB Browser Session " + UUID.randomUUID();
	/** Session Manager Lock */
	private Object lock = new Object();
	/** Map with all Sessions, coupled with SessionId - Session */
	private ConcurrentMap<ClientSession, NodeId> sessions = null;
	/**
	 * Current Active Session of the ClientInstance, which is used to send Requests
	 */
	private ClientSession activeSession = null;
	/**
	 * Subscription Manager which handles all Subscriptions for a Client
	 */
	private SubscriptionManager subscriptionManager = null;
	/**
	 * Node Manager which handels all Nodes for a Client
	 */
	private NodeManager nodeManager = null;

	/**
	 * New SessionManager
	 * 
	 * @param subscriptionManager
	 * @param nodeManager
	 * @param timer
	 */
	public SessionManager(SubscriptionManager subscriptionManager, NodeManager nodeManager) {
		this.sessions = new ConcurrentHashMap<>();
		this.subscriptionManager = subscriptionManager;
		this.nodeManager = nodeManager;
	}

	public void dispose() {
		synchronized (this.lock) {
			for (ClientSession session : sessions.keySet()) {
				try {
					closeSession(session, true);
				} catch (ServiceResultException e) {
					Logger.getLogger(getClass().getName()).log(Level.FINE, null, e);
				}
			}
			Timer timer = TimerUtil.getTimer();
			timer.purge();
		}
	}

	/**
	 * Changes the current operating active session.
	 * 
	 * @param Session New active session
	 */
	public boolean changeActiveSession(ClientSession session) {
		if (this.activeSession == session) {
			return false;
		}
		synchronized (this.lock) {
			this.activeSession = session;
			return true;
		}
	}

	/**
	 * Getter of the current active Session
	 * 
	 * @return Active Client Instance Session.
	 */
	public ClientSession getActiveSession() {
		if (this.activeSession == null) {
			return null;
		}
		return this.activeSession;
	}

	/**
	 * Get the active SessionId.
	 * 
	 * @return SessionId
	 */
	public NodeId getActiveSessionId() {
		if (this.activeSession == null) {
			return null;
		}
		return this.activeSession.getSessionId();
	}

	public ClientSession[] getSessions() {
		return this.sessions.keySet().toArray(new ClientSession[0]);
	}

	public ClientSession getSessionById(NodeId sessionId) {
		if (NodeId.isNull(sessionId)) {
			return null;
		}
		for (ClientSession s : this.sessions.keySet()) {
			if (s.getSessionId().equals(sessionId)) {
				return s;
			}
		}
		return null;
	}

	/**
	 * Getter of all Sessions from the Client.
	 * 
	 * @return All Sessions.
	 */
	public boolean sessionExist(ClientSession session) {
		return this.sessions.containsKey(session);
	}

	public boolean sessionIdExist(NodeId sessionId) {
		for (NodeId v : this.sessions.values()) {
			if (sessionId.equals(v)) {
				return true;
			}
		}
		return false;
	}

	public void reconnect(ClientSession session) throws ServiceResultException {
		session.reconnect(true);
	}

	/**
	 * Returns the Endpoints from a Server without using the Discovery Service.
	 * 
	 * @param ServerUri                            Uri of the Server
	 * @param ClientApplicationConfiguration       Configuration for the
	 *                                             ClientApplication
	 * @param ClientApplicationInstanceCertificate Client Instance KeyPair
	 * @param AsyncOperation                       If the value is TRUE, the Service
	 *                                             will send asynchronous over the
	 *                                             stack.<br>
	 *                                             If the value is FALSE, the
	 *                                             Service will send synchronous
	 *                                             over the stack.
	 * @return
	 * @throws ServiceResultException
	 */
	protected EndpointDescription[] getEndpoints(String serverUri,
			ApplicationConfiguration clientApplicationConfiguration, boolean asyncOperation, boolean validateEndpoints)
			throws ServiceResultException {
		ClientSession session = new ClientSession(
				clientApplicationConfiguration, null, null, clientApplicationConfiguration.getSecurityConfiguration()
						.getApplicationKeyPair(), /* this.profileManager, */
				this.subscriptionManager, this.nodeManager, this, asyncOperation);
		ApplicationDescription clientDescription = new ApplicationDescription();
		clientDescription.setApplicationName(clientApplicationConfiguration.getApplicationName());
		clientDescription.setApplicationUri(clientApplicationConfiguration.getApplicationUri());
		clientDescription.setApplicationType(ApplicationType.Client);
		clientDescription.setProductUri(clientApplicationConfiguration.getProductUri());
		session.open(serverUri, clientDescription, validateEndpoints);
		return session.getServerEndpoints();
	}

	/**
	 * Activate Session Operation.
	 * 
	 * @param preferredLocales
	 * 
	 * @param Session          Session to activate.
	 * @param Identity         Client specified a user identity token that supports
	 *                         digital signatures.
	 * @param AsyncOperation   If the value is TRUE, the Service will send
	 *                         asynchronous over the stack.<br>
	 *                         If the value is FALSE, the Service will send
	 *                         synchronous over the stack.
	 * @return {@link ActivateSessionResponse} of the Service.
	 * @throws ServiceResultException
	 */
	protected ActivateSessionResponse activateSession(ClientSession session, UserIdentity identity,
			String[] preferredLocales, boolean asyncOperation) throws ServiceResultException {
		UserIdentity tmpIdentity = identity;
		// find the matching description
		boolean found = false;
		URI expectedUrl = null;
		try {
			expectedUrl = new URI(session.getConnectedEndpoint().getEndpointUrl());
		} catch (URISyntaxException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			throw new ServiceResultException(StatusCodes.Bad_UnexpectedError,
					"Could not create a URI from the ConnectedEndpoint!");
		}
		/** VALIDATE the Session */
		for (int ii = 0; ii < session.getServerEndpoints().length; ii++) {
			EndpointDescription serverEndpoint = session.getServerEndpoints()[ii];
			URI actualUri = null;
			try {
				actualUri = new URI(serverEndpoint.getEndpointUrl());
			} catch (URISyntaxException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				continue;
			}
			if (actualUri.getScheme().equals(expectedUrl.getScheme())
					&& serverEndpoint.getSecurityPolicyUri()
							.equals(session.getConnectedEndpoint().getSecurityPolicyUri())
					&& serverEndpoint.getSecurityMode().equals(session.getConnectedEndpoint()
							.getSecurityMode())) {/** Update Endpoint with the latest Information */
				/** ApplicationName */
				session.getConnectedEndpoint().getServer()
						.setApplicationName(serverEndpoint.getServer().getApplicationName());
				/** ApplicationUri */
				session.getConnectedEndpoint().getServer()
						.setApplicationUri(serverEndpoint.getServer().getApplicationUri());
				/** ApplicationType */
				session.getConnectedEndpoint().getServer()
						.setApplicationType(serverEndpoint.getServer().getApplicationType());
				/** ProductUri */
				session.getConnectedEndpoint().getServer().setProductUri(serverEndpoint.getServer().getProductUri());
				/** TransportProfileUri */
				session.getConnectedEndpoint().setTransportProfileUri(serverEndpoint.getTransportProfileUri());
				/** UserIdentityTokens */
				session.getConnectedEndpoint().setUserIdentityTokens(serverEndpoint.getUserIdentityTokens());
				found = true;
				break;
			}
		}
		/**
		 * No matching Endpoint has been found, Server did not return an
		 * EndpointDescription that matched the one used to create the secure channel
		 */
		if (!found) {
			throw new ServiceResultException(StatusCodes.Bad_SecurityChecksFailed);
		}
		SecurityPolicy securityPolicy = SecurityPolicy.getSecurityPolicy(session.getSecurityPolicyUri());
		SecurityAlgorithm algorithm = securityPolicy.getAsymmetricSignatureAlgorithm();
		/** Validate the Servers Signature */
		byte[] dataToSign = ArrayUtils.append(session.getClientCertificate().getEncoded(),
				session.getCreateSessionClientNonce());
		if (!SecurityConstants.SECURITY_POLICY_URI_BINARY_NONE.equals(session.getSecurityPolicyUri())
				&& !CryptoUtil.verifyAsymm(session.getServerCertificate().getCertificate(),
						algorithm/* SecurityAlgorithm.RsaSha1 */, dataToSign,
						ByteString.asByteArray(session.getServerSignature().getSignature()))) {
			throw new ServiceResultException(StatusCodes.Bad_ApplicationSignatureInvalid);
		}
		/** get the Validator to check Certifiactes provided by the server */
		CertificateValidator certValidator = session.getConfiguration().getCertificateValidator();
		/** validate software certificates */
		List<SignedSoftwareCertificate> softwareCertificates = new ArrayList<>();
		if (session.getServerSoftwareCertificates() != null && session.getServerSoftwareCertificates().length > 0) {
			for (SignedSoftwareCertificate signedCertificate : session.getServerSoftwareCertificates()) {
				ServiceResult result = new ServiceResult(StatusCode.GOOD);
				SignedSoftwareCertificate softwareCertificate = validateSoftwareCertificate(certValidator,
						signedCertificate);
				if (result.isBad()) {
					continue;
				}
				softwareCertificates.add(softwareCertificate);
			}
		}
		/** check the software certificates meet application requirements */
		validateSoftwareCertificates(softwareCertificates);
		SignatureData clientSignature = null;
		if (session.getServerCertificate() != null) {
			/** create the client signature */
			dataToSign = ArrayUtils.append(session.getServerCertificate().getEncoded(), session.getServerNonce());
			clientSignature = session.sign(dataToSign);
		}
		if (tmpIdentity == null) {
			tmpIdentity = new UserIdentityRole();
		}
		/** get identity token */
		UserIdentityToken identityToken = tmpIdentity.getIdentityToken();
		/**
		 * check that the user identity is supported by the endpoint with the policyId
		 */
		UserTokenPolicy identityPolicy = session.getConnectedEndpoint()
				.findUserTokenPolicy(identityToken.getPolicyId());
		if (identityPolicy == null) {
			/** try looking up the tokentyp if the policy id was not found */
			identityPolicy = session.getConnectedEndpoint().findUserTokenPolicy(tmpIdentity.getTokenType());
			if (identityPolicy == null) {
				throw new ServiceResultException(StatusCodes.Bad_UserAccessDenied,
						"Endpoint does not supported the user identity type provided.");
			}
		}
		tmpIdentity.setPolicyId(identityPolicy.getPolicyId());
		/** select the security policy for the user token */
		String securityPolicyUri = identityPolicy.getSecurityPolicyUri();
		if (securityPolicyUri == null || securityPolicyUri.isEmpty()) {
			session.setSecurityPolicyUri(session.getConnectedEndpoint().getSecurityPolicyUri());
		}
		/** sign data with user token */
		SignatureData userTokenSignature = tmpIdentity.sign(dataToSign, securityPolicyUri);
		/** encrypt token */
		try {
			tmpIdentity.encrypt(userTokenSignature, session.getClientCertificate(),
					session.getApplicationInstanceCertificate().getPrivateKey(), session.getConnectedEndpoint(),
					session.getServerNonce(), securityPolicyUri);
		} catch (ServiceResultException e) {
			if (e.getStatusCode().equals(new StatusCode(StatusCodes.Bad_NonceInvalid))) {
				/** wrapp message */
				throw new ServiceResultException(StatusCodes.Bad_NonceInvalid,
						"The server does not support the tokentype " + tmpIdentity.getTokenType().name()
								+ " with the session security " + session.getSecurityPolicyUri()
								+ "! Required security for login is " + securityPolicyUri);
			}
			throw e;
		}
		identityToken = tmpIdentity.getIdentityToken();
		/** send the software certificates assigned to the client */
		List<SignedSoftwareCertificate> clientSoftwareCertificates = getSoftwareCertificates();
		if (preferredLocales != null && preferredLocales.length > 0) {
			session.setPreferredLocales(preferredLocales);
		}
		// activate session service
		ExtensionObject encodedIdentityToken = null;
		try {
			encodedIdentityToken = ExtensionObject.binaryEncode(identityToken, EncoderContext.getDefaultInstance());
		} catch (EncodingException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			throw new ServiceResultException(StatusCodes.Bad_IdentityTokenInvalid);
		}
		if (!asyncOperation) {
			return session.activate(null, clientSignature, clientSoftwareCertificates, encodedIdentityToken,
					tmpIdentity, userTokenSignature);
		} else {
			return session.beginActivateSession(null, clientSignature, clientSoftwareCertificates, encodedIdentityToken,
					tmpIdentity, userTokenSignature);
		}
	}

	/**
	 * Cancels a Request. TODO: NOT SUPPORTED !
	 * 
	 * @param Session Session to Use
	 * @param Request Request to cancel.
	 * @return
	 * @throws ServiceResultException
	 * @throws ServiceFaultException
	 * @throws IllegalArgumentException
	 */
	protected CancelResponse cancelRequest(ClientSession session, CancelRequest request) throws ServiceResultException {
		if (session == null) {
			throw new IllegalArgumentException("Session");
		}
		return session.cancelRequest(request);
	}

	/**
	 * Disconnects a session from a server.
	 * 
	 * @param Session             Session to be disconnected.
	 * @param DeleteSubscriptions If the value is TRUE, the Server deletes all
	 *                            Subscriptions associated with the Session. If the
	 *                            value is FALSE, the Server keeps the Subscriptions
	 *                            associated with the Session until they timeout
	 *                            based on their own lifetime.
	 * @return {@link CloseSessionResponse} of the Service.
	 * @throws ServiceResultException
	 * @throws ServiceFaultException
	 * @throws IllegalArgumentException
	 */
	protected CloseSessionResponse closeSession(ClientSession session, boolean deleteSubscriptions)
			throws ServiceResultException {
		if (session == null) {
			throw new ServiceResultException(StatusCodes.Bad_InternalError, "No Active Session!");
		}
		CloseSessionResponse closeResult = session.closeSession(deleteSubscriptions);
		// if no other sessions exists, so stop stack timer too
		if (this.sessions.isEmpty()) {
			Timer timer = TimerUtil.getTimer();
			timer.purge();
		}
		return closeResult;
	}

	public void closeSessionInternally(ClientSession session) {
		if (this.sessions.containsKey(session))
			this.sessions.remove(session);
		if (this.sessions.isEmpty()) {
			Timer timer = TimerUtil.getTimer();
			timer.purge();
		}
	}

	/**
	 * Creates a Session to a server.
	 * 
	 * @param Endpoint                             Server Endpoint to connect.
	 * @param Sessionname                          Name for the Session.
	 * @param ClientApplicationInstanceCertificate InstanceCertificate to authorize
	 *                                             with the Server�s certificate.
	 * @param ClientConfiguration                  Clients Application Configuration
	 * @param AsyncOperation                       If the value is TRUE, the Service
	 *                                             will send asynchronous over the
	 *                                             stack.<br>
	 *                                             If the value is FALSE, the
	 *                                             Service will send synchronous
	 *                                             over the stack.
	 * @return {@link ClientSession} of the Service.
	 * @throws ServiceResultException
	 */
	protected ClientSession createSession(EndpointDescription endpoint, String sessionName,
			ApplicationConfiguration clientDescription, boolean asyncOperation, boolean validateEndpoints,
			long keepAliveInterval) throws ServiceResultException {
		String tmpSessionName = sessionName;
		EndpointConfiguration endpointConfiguration = EndpointConfiguration.defaults();
		endpointConfiguration.setChannelLifetime(clientDescription.getTransportQuotas().getChannelLifetime());
		endpointConfiguration.setMaxArrayLength(clientDescription.getTransportQuotas().getMaxArrayLength());
		endpointConfiguration.setMaxBufferSize(clientDescription.getTransportQuotas().getMaxBufferSize());
		endpointConfiguration.setMaxByteStringLength(clientDescription.getTransportQuotas().getMaxByteStringLength());
		endpointConfiguration.setMaxMessageSize(clientDescription.getTransportQuotas().getMaxMessageSize());
		endpointConfiguration.setMaxStringLength(clientDescription.getTransportQuotas().getMaxStringLength());
		endpointConfiguration.setOperationTimeout(clientDescription.getTransportQuotas().getOperationTimeout());
		endpointConfiguration
				.setSecurityTokenLifetime(clientDescription.getTransportQuotas().getSecurityTokenLifetime());
		endpointConfiguration.setUseBinaryEncoding(true);
		ClientSession session = new ClientSession(clientDescription, endpoint, endpointConfiguration,
				clientDescription.getSecurityConfiguration().getApplicationKeyPair(),
				/* this.profileManager, */this.subscriptionManager, this.nodeManager, this, asyncOperation);
		session.setKeepAliveInterval(keepAliveInterval);
		// create the session
		for (ClientSession s : this.sessions.keySet()) {
			if (s.getSessionName() != null && s.getSessionName().equals(tmpSessionName)) {
				tmpSessionName = SessionManager.DEFAULTSESSIONNAME;
				break;
			}
		}
		session.open(tmpSessionName, validateEndpoints);
		return session;
	}

	protected void saveSession(CreateSessionResponse response, final ClientSession session)
			throws ServiceResultException {
		// CTT 7.1-Err017
		if (sessionIdExist(session.getSessionId())) {
			ServiceResultException sre = new ServiceResultException(new StatusCode(StatusCodes.Bad_SessionIdInvalid),
					"Session is invalid! SessionId is currently in use!");
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, RequestType.CreateSession.name(),
					sre.getAdditionalTextField());
			try {
				closeSession(session, true);
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			}
			throw sre;
		} else {
			this.sessions.put(session, response.getSessionId());
			// update all event elements
		}
	}

	/**
	 * Cancels a Request. TODO: NOT SUPPORTED!
	 * 
	 * @param Session Session to use.
	 * @param Request Request to cancel a Request.
	 * @return {@link AsyncResult} of the service.
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected AsyncResult<ServiceResponse> asyncCancelRequest(ClientSession session, CancelRequest request)
			throws ServiceResultException {
		NodeId requestId = Identifiers.CancelRequest;
		return session.beginCancelRequest(request, requestId);
	}

	/**
	 * Disconnects a session from a server asynchronous.
	 * 
	 * @param Session             Session to use.
	 * @param DeleteSubscriptions If the value is TRUE, the Server deletes all
	 *                            Subscriptions associated with the Session. If the
	 *                            value is FALSE, the Server keeps the Subscriptions
	 *                            associated with the Session until they timeout
	 *                            based on their own lifetime.
	 * @return {@link AsyncResult} of the Service.
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected CloseSessionResponse asyncCloseSession(ClientSession session, boolean deleteSubscriptions)
			throws ServiceResultException {
		NodeId requestId = Identifiers.CloseSessionRequest;
		if (session == null) {
			throw new ServiceResultException(StatusCodes.Bad_InternalError, "No Active Session!");
		}
		return session.beginCloseSession(deleteSubscriptions, requestId);
	}

	protected void removeSession(ClientSession session) {
		this.sessions.remove(session);
		if (this.activeSession == session) {
			this.activeSession = null;
		}
	}

	// protected void setTimer(Timer timer)
	// {
	// this.timer = timer;
	// }
	/**
	 * Returns the software certificates assigned to the application.
	 * 
	 * @return
	 */
	private List<SignedSoftwareCertificate> getSoftwareCertificates() {
		return new ArrayList<>();
	}

	/**
	 * 
	 * @param softwareCertificates
	 */
	private void validateSoftwareCertificates(List<SignedSoftwareCertificate> softwareCertificates) {
		// always accept valid certificates :)
	}

	/**
	 * 
	 * @param certValidator
	 * @param signedCertificate
	 * @param result
	 * @return
	 * @throws ServiceResultException
	 */
	private SignedSoftwareCertificate validateSoftwareCertificate(CertificateValidator certValidator,
			SignedSoftwareCertificate signedCertificate) throws ServiceResultException {
		Cert certificate = new Cert(ByteString.asByteArray(signedCertificate.getCertificateData()));
		/** validate the certificate */
		StatusCode code = certValidator.validateCertificate(certificate);
		if (code != null && code.isBad()) {
			throw new ServiceResultException(code);
		}
		byte[] extension = certificate.getCertificate().getExtensionValue("0.0.0.0");
		if (extension == null) {
			throw new ServiceResultException(StatusCodes.Bad_CertificateInvalid);
		}
		return new SignedSoftwareCertificate();
	}
}
