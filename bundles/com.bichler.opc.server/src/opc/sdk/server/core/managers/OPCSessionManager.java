package opc.sdk.server.core.managers;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.x500.X500Principal;

import org.opcfoundation.ua.builtintypes.ByteString;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.ServiceResult;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.ActivateSessionResponse;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.core.CloseSessionResponse;
import org.opcfoundation.ua.core.CreateSessionResponse;
import org.opcfoundation.ua.core.EndpointDescription;
import org.opcfoundation.ua.core.RequestHeader;
import org.opcfoundation.ua.core.SignatureData;
import org.opcfoundation.ua.core.SignedSoftwareCertificate;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.core.UserIdentityToken;
import org.opcfoundation.ua.transport.ServerSecureChannel;
import org.opcfoundation.ua.transport.security.Cert;
import org.opcfoundation.ua.transport.security.CertificateValidator;
import org.opcfoundation.ua.transport.security.SecurityAlgorithm;
import org.opcfoundation.ua.transport.security.SecurityPolicy;
import org.opcfoundation.ua.utils.CryptoUtil;
import org.opcfoundation.ua.utils.EndpointUtil;
import org.opcfoundation.ua.utils.bytebuffer.ByteBufferUtils;

import opc.sdk.core.enums.RequestType;
import opc.sdk.core.session.UserIdentity;
import opc.sdk.core.session.UserIdentityRole;
import opc.sdk.server.core.OPCInternalServer;
import opc.sdk.server.service.session.OPCServerSession;
import opc.sdk.server.service.session.OPCSessionConfigurator;
import opc.sdk.server.service.util.CertificateFactory;

public class OPCSessionManager implements IOPCManager {
	/** manager lock */
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock(false);
	/** opc server instance */
	private OPCInternalServer server = null;
	/** session configuration */
	private OPCSessionConfigurator configuration = null;
	/** opc sessions */
	private Map<NodeId, OPCServerSession> sessions = null;
	private WatchSessionsTask task = null;

	// is activated as default, not all trusted certificate support client app URI
	private boolean validateClientAppUri = true;

	/**
	 * 
	 * @return
	 */
	public boolean isValidateClientAppUri() {
		return validateClientAppUri;
	}

	public void setValidateClientAppUri(boolean validateClientAppUri) {
		this.validateClientAppUri = validateClientAppUri;
	}

	/**
	 * Handler to manage user login (Anonymous-, Username/Password-, Issuer-,
	 * X509Certificate- Token
	 */
	public OPCSessionManager(OPCInternalServer server) {
		this.server = server;
		this.configuration = this.server.getSessionConfigurator();
		this.sessions = new HashMap<>();
	}

	protected ActivateSessionResponse activateSession(NodeId authentificationToken, SignatureData clientSignature,
			SignedSoftwareCertificate[] clientSoftwareCertificates, String[] locales, ExtensionObject userIdentityToken,
			SignatureData userTokenSignature, ServerSecureChannel channel) throws ServiceResultException {
		this.lock.writeLock().lock();
		try {
			ByteString serverNonce = null;
			UserIdentityToken newIdentity = null;
			OPCServerSession session = this.sessions.get(authentificationToken);
			if (session == null) {
				throw new ServiceResultException(new StatusCode(StatusCodes.Bad_SessionClosed));
			}
			// create new server nonce
			if (serverNonce == null && configuration.getMinNonceLength(channel) >= 0) {
				serverNonce = CryptoUtil.createNonce(configuration.getMinNonceLength(channel));
			}
			// validate before activate
			newIdentity = session.validateBeforeActivate(clientSignature, clientSoftwareCertificates, userIdentityToken,
					userTokenSignature, locales, serverNonce, channel);
			// verify user login
			StatusCode error = this.server.getUserAuthentifiationManager().verifyAuthentification(session, newIdentity);
			// login not allowed
			if (error.isBad()) {
				throw new ServiceResultException(error);
			}
			// validate client software certificates
			StatusCode results[] = null;
			SignedSoftwareCertificate[] softwareCertificates = null;
			if (!SecurityPolicy.NONE.getPolicyUri().equals(channel.getSecurityPolicy().getPolicyUri())) {
				if (clientSoftwareCertificates != null) {
					results = new StatusCode[clientSoftwareCertificates.length];
					softwareCertificates = new SignedSoftwareCertificate[clientSoftwareCertificates.length];
					for (int i = 0; i < clientSoftwareCertificates.length; i++) {
						SignedSoftwareCertificate scert = clientSoftwareCertificates[i];
						ServiceResult result = new ServiceResult();
						SignedSoftwareCertificate softwareCertificate = validateSoftwareCertificate(
								this.server.getApplication().getOpctcpSettings().getCertificateValidator(),
								ByteString.asByteArray(scert.getCertificateData()), scert.getSignature(), result);
						if (result.isBad()) {
							results[i] = result.getCode();
							// TODO: DIAGNOSTICS
						} else {
							results[i] = StatusCode.GOOD;
							softwareCertificates[i] = softwareCertificate;
							// TODO: DIAGNOSTICS
						}
					}
				}
			}
			UserIdentity identity = null;
			UserIdentity effectiveIdentity = null;
			// parse the token manually if the identity is not provided.
			if (identity == null) {
				if (newIdentity == null) {
					identity = new UserIdentityRole();
				} else {
					identity = new UserIdentityRole(newIdentity);
				}
			}
			// use the identity as the effectiveIdentity if not provided.
			if (effectiveIdentity == null) {
				effectiveIdentity = identity;
			}
			session.activate(clientSoftwareCertificates, newIdentity, locales, identity, effectiveIdentity, serverNonce,
					channel);
			// this.server.addLocales(locales);
			ActivateSessionResponse response = new ActivateSessionResponse();
			response.setServerNonce(session.getServerNonce());
			response.setResults(results);
			return response;
		} finally {
			this.lock.writeLock().unlock();
		}
	}

	protected UnsignedInteger cancel(UnsignedInteger requestHandle, OPCServerSession session)
			throws ServiceResultException {
		this.lock.writeLock().lock();
		try {
			// throw new ServiceResultException(new
			// StatusCode(StatusCodes.Bad_NothingToDo));
			return UnsignedInteger.ZERO;
		} finally {
			this.lock.writeLock().unlock();
		}
	}

	protected CreateSessionResponse createSession(String sessionName, String endpointUrl, String serverUri,
			ApplicationDescription clientDescription, ByteString clientCertificate, ByteString clientNonce,
			UnsignedInteger maxResponseMessageSize, Double requestedSessionTimeout, ServerSecureChannel channel)
			throws ServiceResultException {
		this.lock.writeLock().lock();
		try {
			String applicationUri = this.server.getApplication().getApplicationDescription().getApplicationUri();
			int minNonceLength = this.configuration.getMinNonceLength(channel);
			// check the server uri if it is not NULL or EMPT
			if (!(serverUri == null || serverUri.isEmpty())) {
				if (!serverUri.equals(applicationUri)) {
					throw new ServiceResultException(StatusCodes.Bad_ServerUriInvalid);
				}
			}
			// validate client application instance certificate
			Cert parsedClientCertificate = null;
			if (clientCertificate != null && clientCertificate.getLength() > 0) {
				parsedClientCertificate = new Cert(ByteString.asByteArray(clientCertificate));
				if (!channel.getSecurityPolicy().getPolicyUri().equals(SecurityPolicy.NONE.getPolicyUri())) {
					StatusCode result = this.server.getApplicationConfiguration().getCertificateValidator()
							.validateCertificate(channel.getSecurityPolicy(), null, parsedClientCertificate);
					if (result != null && result.isBad()) {
						throw new ServiceResultException(StatusCodes.Bad_CertificateUseNotAllowed);
					}
					X509Certificate cert2check = parsedClientCertificate.getCertificate();
					String clientAppUri = clientDescription.getApplicationUri();
					Map<String, String> mapOid = new HashMap<>();
					String subject = cert2check.getSubjectX500Principal().getName(X500Principal.RFC1779, mapOid);
					int startCN = subject.indexOf("CN=") + 3;
					int endCN = subject.indexOf(",", startCN);
					// no , at the end
					if (endCN == -1) {
						endCN = subject.length();
					}
					String certAppUri = subject.substring(startCN, endCN);
					if (certAppUri.contains("@")) {
						certAppUri = certAppUri.substring(certAppUri.indexOf("@") + 1);
					}
					if (validateClientAppUri) {
						if (clientAppUri != null) {
							boolean contains = clientAppUri.contains(certAppUri);
							if (!contains) {
								throw new ServiceResultException(StatusCodes.Bad_CertificateUriInvalid);
							}
						} else {
							throw new ServiceResultException(StatusCodes.Bad_CertificateUriInvalid);
						}
					}
				}
			}
			/** verify the nonce provided by the client */
			if (clientNonce != null) {
				if (clientNonce.getLength() < minNonceLength) {
					throw new ServiceResultException(StatusCodes.Bad_NonceInvalid);
				}
			}
			/** create the session */
			ByteString serverCertificate = ByteString.valueOf(
					this.server.getApplication().getApplicationInstanceCertificate().getCertificate().getEncoded());
			OPCServerSession session = createSession(sessionName, clientNonce, clientDescription, endpointUrl,
					clientCertificate, requestedSessionTimeout, maxResponseMessageSize, channel);
			// sign the nonce provided by the client
			SignatureData serverSignature = null;
			// sign the client nonce (if provided
			if (parsedClientCertificate != null && clientNonce != null) {
				// signer key
				RSAPrivateKey signerKey = this.server.getApplication().getApplicationInstanceCertificate()
						.getPrivateKey().getPrivateKey();
				// signer algorithm
				SecurityPolicy securityPolicy = channel.getSecurityPolicy();
				SecurityAlgorithm algorithmUri = securityPolicy.getAsymmetricSignatureAlgorithm();
				// data to sign
				ByteString dataToSign = ByteString.EMPTY;
				if (session != null) {
					switch (securityPolicy) {
					case NONE:
						dataToSign = ByteString.valueOf(ByteBufferUtils.concatenate(ByteString.asByteArray(dataToSign),
								ByteString.asByteArray(session.getClientNonce())));
						break;
					default:
						dataToSign = ByteString.valueOf(
								ByteBufferUtils.concatenate(ByteString.asByteArray(session.getClientCertificate()),
										ByteString.asByteArray(session.getClientNonce())));
						break;
					}
				}
				// create the server signature
				serverSignature = CryptoUtil.signAsymm(signerKey, algorithmUri, ByteString.asByteArray(dataToSign));
			}
			// return the software certificates assigned to the server
			SignedSoftwareCertificate[] serverSoftwareCertificates = null;
			// return the endpoints supported by the server
			EndpointDescription[] serverEndpoints = this.server.getEndpointDescriptions();
			serverEndpoints = EndpointUtil.selectByUrl(serverEndpoints, channel.getEndpoint().getEndpointUrl());
			UnsignedInteger maxRequestMessageSize = this.configuration.getMaxRequestedMessageSize();
			// response of the service
			CreateSessionResponse response = new CreateSessionResponse();
			if (session != null) {
				response.setSessionId(session.getSessionId());
				response.setAuthenticationToken(session.getAuthentifikationToken());
				response.setRevisedSessionTimeout(session.getSessionTimeout());
				response.setServerNonce(session.getServerNonce());
			}
			response.setServerCertificate(serverCertificate);
			response.setServerEndpoints(serverEndpoints);
			response.setServerSoftwareCertificates(serverSoftwareCertificates);
			response.setServerSignature(serverSignature);
			response.setMaxRequestMessageSize(maxRequestMessageSize);
			return response;
		} finally {
			this.lock.writeLock().unlock();
		}
	}

	private OPCServerSession createSession(String sessionName, ByteString clientNonce,
			ApplicationDescription clientDescription, String endpointUrl, ByteString clientCertificate,
			Double sessionTimeout, UnsignedInteger maxResponseMessageSize, ServerSecureChannel channel)
			throws ServiceResultException {
		double revisedSessionTimeout = sessionTimeout;
		UnsignedInteger maxSessionCount = this.configuration.getMaxSessionCount();
		double maxSessionTimeout = this.configuration.getMaxSessionTimeout();
		double minSessionTimeout = this.configuration.getMinSessionTimeout();
		int minNonceLength = this.configuration.getMinNonceLength(channel);
		UnsignedInteger maxRequestAge = this.configuration.getMaxRequestAge();
		int maxBrowseContinuationPoints = this.configuration.getMaxBrowseContinuationPoints();
		int maxHistoryContinuationPoints = this.configuration.getMaxHistoryContinuationPoints();
		NodeId newAuthentificationToken = null;
		OPCServerSession session = null;
		if (maxSessionCount.longValue() > 0 && this.sessions.size() >= maxSessionCount.longValue()) {
			throw new ServiceResultException(StatusCodes.Bad_TooManySessions);
		}
		UUID authentificationToken = UUID.randomUUID();
		newAuthentificationToken = new NodeId(1, authentificationToken);
		if (sessionTimeout > maxSessionTimeout)
			revisedSessionTimeout = maxSessionTimeout;
		if (sessionTimeout < minSessionTimeout) {
			revisedSessionTimeout = minSessionTimeout;
		}
		if (sessionName == null || sessionName.isEmpty()) {
			sessionName = "Session " + newAuthentificationToken;
		}
		if (this.configuration.getSecurityCert() == null) {
			throw new ServiceResultException(StatusCodes.Bad_CertificateUseNotAllowed);
		}

		try {
			URI uri = new URI(endpointUrl);
			Cert serverCert = CertificateFactory.create(
					ByteString.valueOf(this.configuration.getSecurityCert().getCert().encodedCertificate), true);
			NodeId sessionId = newAuthentificationToken;
			ByteString serverNonce = null;
			if (serverNonce == null && minNonceLength >= 0) {
				serverNonce = CryptoUtil.createNonce(minNonceLength);
			}
			session = new OPCServerSession(this.server, sessionId, this.configuration, serverCert,
					newAuthentificationToken, serverNonce, sessionName, clientDescription, endpointUrl,
					clientCertificate, clientNonce, revisedSessionTimeout, maxResponseMessageSize, maxRequestAge,
					maxBrowseContinuationPoints, maxHistoryContinuationPoints, channel);
			// Save the Session
			this.sessions.put(newAuthentificationToken, session);
			server.logService(RequestType.CreateSession,
					"Session " + sessionName + " with id " + sessionId + " has been created");
		} catch (URISyntaxException e) {
			server.logService(RequestType.CreateSession, e.getMessage());
		}
		return session;
	}

	protected CloseSessionResponse closeSession(Boolean deleteSubscriptions, OPCServerSession session)
			throws ServiceResultException {
		// find the session
		this.lock.writeLock().lock();
		try {
			if (deleteSubscriptions) {
				this.server.getSubscriptionManager().deleteAllSubscriptionsFromSession(session);
				// session.getServer().getSubscriptionManager()
				// .deleteAllSubscriptions(session.getSessionId());
				// }
				// this.server.getSubscriptionManager()
				// .removeSessionPublishingQueue(session.getSessionId());
			}
			session.close();
			this.sessions.remove(session.getAuthentifikationToken());
			server.logService(RequestType.CloseSession,
					"Session " + session.getSessionname() + " " + session.getSessionId() + " is closed");
			CloseSessionResponse response = new CloseSessionResponse();
			return response;
		} finally {
			this.lock.writeLock().unlock();
		}
		// /** update diagnostics */
		// ServerDiagnosticsSummaryDataType diagnostics = this.server
		// .getServerDiagnostics();
		// ServerDiagnostics[] categories = {
		// ServerDiagnostics.CurrentSessionCount };
		// Object[] values = { diagnostics.getCurrentSessionCount().dec() };
		// this.server.getDiagnosticsNodeManager().updateServerDiagnosticsSummary(
		// categories, values);
	}

	public OPCServerSession validateRequest(RequestType requestType, RequestHeader requestHeader, int secureChannelId)
			throws ServiceResultException {
		this.lock.writeLock().lock();
		try {
			OPCServerSession session = null;
			try {
				if (RequestType.FindServers == requestType || RequestType.GetEndpoints == requestType
						|| RequestType.RegisterServer == requestType) {
					return null;
				}
				// check for create request
				else if (RequestType.CreateSession == requestType || RequestType.ActivateSession == requestType) {
					return null;
				}
				// find session
				if ((session = this.sessions.get(requestHeader.getAuthenticationToken())) == null) {
					throw new ServiceResultException(StatusCodes.Bad_SessionIdInvalid);
				}
				// validate requestheader
				session.validateRequest(requestHeader, requestType, secureChannelId);
			} catch (ServiceResultException e) {
				if (e != null && StatusCodes.Bad_SessionNotActivated.equals(e.getStatusCode().getValue())) {
					if (session != null) {
						this.server.getMaster().closeSession(true, session);
					}
				}
				throw e;
			}
			return session;
		} finally {
			this.lock.writeLock().unlock();
		}
	}

	private SignedSoftwareCertificate validateSoftwareCertificate(CertificateValidator certificateValidator,
			byte[] certificateData, ByteString signature, ServiceResult result) {
		SignedSoftwareCertificate softCertificate;
		// validate the certificate
		Cert certificate = null;
		try {
			certificate = new Cert(certificateData);
			StatusCode stcode = certificateValidator.validateCertificate(certificate);
			if (stcode == null || stcode == StatusCode.BAD) {
				result.setCode(new StatusCode(StatusCodes.Bad_CertificateInvalid));
				return null;
			}
		} catch (ServiceResultException e) {
			result.setCode(e.getStatusCode());
			return null;
		}
		// find the software certificate
		byte[] encodedData = certificate.getCertificate().getExtensionValue("0.0.0.0.0");
		softCertificate = new SignedSoftwareCertificate(ByteString.valueOf(certificateData), signature);
		if (encodedData == null) {
			result.setCode(new StatusCode(StatusCodes.Bad_CertificateInvalid));
			return null;
		}
		return softCertificate;
	}

	@Override
	public boolean start() {
		this.task = new WatchSessionsTask();
		this.server.scheduleTask(this.task, 0, (long) this.configuration.getMinSessionTimeout());
		this.hasInitialized = true;
		return this.hasInitialized;
	}

	@Override
	public boolean stop() {
		// free resources
		this.hasInitialized = false;
		/** runnable is closed by ExecutorService */
		return this.hasInitialized;
	}

	void monitorSession() {
		// remove expired sessions
		this.lock.writeLock().lock();
		List<OPCServerSession> expiredSessions = new ArrayList<>();
		try {
			// find expired session
			for (OPCServerSession session : this.sessions.values()) {
				if (session.hasExpired()) {
					expiredSessions.add(session);
				}
			}
			// remove
			for (OPCServerSession expired : expiredSessions) {
				try {
					closeSession(true, expired);
				} catch (ServiceResultException e) {
					Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
				}
			}
		} finally {
			this.lock.writeLock().unlock();
		}
	}

	class WatchSessionsTask implements Runnable {
		@Override
		public void run() {
			monitorSession();
		}
	}

	@Override
	public OPCInternalServer getServer() {
		return this.server;
	}

	boolean hasInitialized = false;

	@Override
	public boolean isInitialized() {
		return this.hasInitialized;
	}
}
