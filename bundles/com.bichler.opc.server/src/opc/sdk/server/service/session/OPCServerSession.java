package opc.sdk.server.service.session;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.ByteString;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AnonymousIdentityToken;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.core.EndpointDescription;
import org.opcfoundation.ua.core.RequestHeader;
import org.opcfoundation.ua.core.SignatureData;
import org.opcfoundation.ua.core.SignedSoftwareCertificate;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.core.UserIdentityToken;
import org.opcfoundation.ua.core.UserTokenPolicy;
import org.opcfoundation.ua.core.UserTokenType;
import org.opcfoundation.ua.core.X509IdentityToken;
import org.opcfoundation.ua.encoding.EncoderContext;
import org.opcfoundation.ua.transport.ServerSecureChannel;
import org.opcfoundation.ua.transport.security.Cert;
import org.opcfoundation.ua.transport.security.PrivKey;
import org.opcfoundation.ua.transport.security.SecurityAlgorithm;
import org.opcfoundation.ua.transport.security.SecurityConstants;
import org.opcfoundation.ua.transport.security.SecurityPolicy;
import org.opcfoundation.ua.utils.CryptoUtil;

import com.bichler.opc.comdrv.ComDRVManager;
import com.bichler.opc.comdrv.IOPCServerSession;

import opc.sdk.core.enums.RequestType;
import opc.sdk.core.session.UserIdentity;
import opc.sdk.core.session.UserIdentityRole;
import opc.sdk.core.utils.ArrayUtils;
import opc.sdk.server.OPCServerOperation;
import opc.sdk.server.core.OPCInternalServer;
import opc.sdk.server.service.history.HistoryReadCPItem;
import opc.sdk.server.service.opc.browse.BrowseContinuationPoint;
import opc.sdk.server.service.util.CertificateFactory;
import opc.sdk.server.service.util.UUIDUtil;
import opc.sdk.ua.FileHandle;
import opc.sdk.ua.IOPCOperation;
import opc.sdk.ua.IOPCSession;

public class OPCServerSession implements IOPCSession, IOPCServerSession {
	/** session lock */
	private ReentrantLock lock = new ReentrantLock();
	/** session authentification token, identifying a session with a request */
	private NodeId authentifikationToken;
	/** Timestamp of last contact with a client */
	private long clientLastContact = -1;
	/** last received client nonce */
	private ByteString clientNonce;
	/** List of stored browseContinuationPoints. */
	private List<BrowseContinuationPoint> cpBrowse = null;
	/** effective user identity to use for this session */
	private UserIdentity effectiveIdentity;
	/** id of securechannel to use */
	private int securechannelId = -1;
	/** session service activated */
	private boolean isActivate = false;
	/** session max age */
	private UnsignedInteger maxRequestAge = UnsignedInteger.ZERO;
	/** session id */
	private NodeId sessionId = NodeId.NULL;
	/** session locales */
	private Map<String, Locale> locales = new HashMap<>();
	/** server nonce */
	private ByteString serverNonce;
	/** session name */
	private String sessionname;
	/** server certificate */
	private Cert serverCertificate;
	/** received client certificate */
	private ByteString clientCertificate;
	/** session timeout */
	private double sessionTimeout;
	/** max message size */
	private UnsignedInteger maxMessageSize;
	/** max browse continuation points */
	private int maxBrowseContinuationPoints;
	/** max history continuation points */
	private int maxHistoryContinuationPoints;
	/** endpoint description */
	private EndpointDescription endpointDescription;
	/** user identitiy */
	private UserIdentity identity;
	/** software certificates */
	private SignedSoftwareCertificate[] softwareCertificates;
	/** user identity token */
	private UserIdentityToken identityToken;
	private OPCSessionConfigurator sessionConfiguration;
	private int[] roleIndizes = null;
	private OPCInternalServer server;
	private AtomicInteger handles = new AtomicInteger(1);
	private List<FileHandle> filehandles = new ArrayList<>();

	public OPCServerSession(OPCInternalServer server, NodeId sessionId, OPCSessionConfigurator securityConfiguration,
			Cert serverCert, NodeId authentificationToken, ByteString serverNonce, String sessionName,
			ApplicationDescription clientDescription, String endpointUrl, ByteString clientCertificate,
			ByteString clientNonce, double sessionTimeout, UnsignedInteger maxMessageSize,
			UnsignedInteger maxRequestAge, int maxBrowseContinuationPoints, int maxHistoryContinuationPoints,
			ServerSecureChannel channel) {
		this.server = server;
		// session id
		this.sessionId = sessionId;
		this.clientNonce = clientNonce;
		// this.server = server;
		this.authentifikationToken = authentificationToken;
		this.serverNonce = serverNonce;
		this.isActivate = false;
		this.sessionname = sessionName;
		this.serverCertificate = serverCert;
		this.clientCertificate = clientCertificate;
		this.sessionTimeout = sessionTimeout;
		this.securechannelId = channel.getSecureChannelId();
		this.setMaxMessageSize(maxMessageSize);
		this.maxRequestAge = maxRequestAge;
		this.maxBrowseContinuationPoints = maxBrowseContinuationPoints;
		this.setMaxHistoryContinuationPoints(maxHistoryContinuationPoints);
		this.cpBrowse = new ArrayList<>();
		this.sessionConfiguration = securityConfiguration;
		EndpointDescription description = null;
		// get the correct endpoint for the request
		for (EndpointDescription endpoint : channel.getServer().getEndpointDescriptions()) {
			if (endpoint.getSecurityPolicyUri().equals(channel.getSecurityPolicy().getPolicyUri())
					&& endpoint.getEndpointUrl().equals(channel.getEndpoint().getEndpointUrl())
					&& endpoint.getSecurityMode().equals(channel.getMessageSecurityMode())) {
				description = endpoint;
				break;
			}
		}
		this.endpointDescription = description;
		// use anonymous default identity
		this.setIdentity(new UserIdentityRole());
		this.clientLastContact = System.nanoTime();
	}

	public boolean activate(SignedSoftwareCertificate[] clientSoftwareCertificates, UserIdentityToken newIdentity,
			String[] locales, UserIdentity identity, UserIdentity effectiveIdentity, ByteString serverNonce,
			ServerSecureChannel channel) {
		boolean changed = false;
		if (identity != null) {
			if (updateUserIdentity(newIdentity, identity, effectiveIdentity)) {
				changed = true;
			}
		}
		// update locale ids
		boolean hasLocalesChanged = updateLocaleIds(locales);
		changed = changed || hasLocalesChanged;
		if (!this.isActivate) {
			// toggle the activated flag
			this.isActivate = true;
			// save software certificates
			this.setSoftwareCertificates(clientSoftwareCertificates);
		} else {
			// bind to the new secure channel.
			this.securechannelId = channel.getSecureChannelId();
		}
		// update server nonce
		this.serverNonce = serverNonce;
		// log
		server.logService(RequestType.ActivateSession,
				getSessionId() + " " + getSessionname() + " has been activated!");
		// TODO: Audit events
		/** session diagnostics */
		// SessionDiagnostics[] diagnostics = {
		// SessionDiagnostics.ClientLastContactTime };
		// Object[] values = { DateTime.currentTime() };
		//
		// this.server.getDiagnosticsNodeManager().updateSessionDiagnosticsObject(
		// this, diagnostics, values);
		return changed;
	}

	public void close() {
		for (FileHandle e : new ArrayList<>(this.filehandles)) {
			IOPCOperation context = new OPCServerOperation(this);
			// call close method for file handle
			ComDRVManager.getDRVManager().callMethod(context, e.getObjectId(), e.getMethodId(),
					new Variant[] { new Variant(e.getId()) });
		}
		this.filehandles.clear();
	}

	/**
	 * Adds a locale for this session
	 * 
	 * @param locale
	 */
	public void addLocale(Locale locale) {
		this.locales.put(locale.getLanguage(), locale);
	}

	/**
	 * Checks if the session has been expired
	 * 
	 * @return
	 */
	public boolean hasExpired() {
		this.lock.lock();
		try {
			if (this.clientLastContact + (this.sessionTimeout * 1000000) < System.nanoTime()) {
				return true;
			}
		} finally {
			this.lock.unlock();
		}
		return false;
	}

	/**
	 * Restores a ContinuationPoint.
	 * 
	 * @param ContinuationPointData Byte Array of the ContinuationPoint.
	 * @return ContinuationPoint
	 */
	public BrowseContinuationPoint restoreContinuationPoint(ByteString continuationPoint) {
		if (this.cpBrowse == null || this.cpBrowse.isEmpty()) {
			return null;
		}
		if (continuationPoint == null || continuationPoint.getLength() != 16) {
			return null;
		}
		this.lock.lock();
		try {
			UUID id = UUIDUtil.toUUID(ByteString.asByteArray(continuationPoint));
			for (int ii = 0; ii < this.cpBrowse.size(); ii++) {
				if (this.cpBrowse.get(ii).getId().equals(id)) {
					BrowseContinuationPoint cp = this.cpBrowse.remove(ii);
					return cp;
				}
			}
		} finally {
			this.lock.unlock();
		}
		return null;
	}

	/**
	 * Stores a browse continuation point
	 * 
	 * @param cp
	 */
	public StatusCode saveContinuationPoint(BrowseContinuationPoint cp) {
		StatusCode result = StatusCode.GOOD;
		this.lock.lock();
		try {
			// remove the first continuationPoint if too many exists
			while (this.cpBrowse.size() >= this.maxBrowseContinuationPoints) {
				BrowseContinuationPoint cp2remove = this.cpBrowse.remove(0);
				cp2remove.dispose();
				result = new StatusCode(StatusCodes.Bad_NoContinuationPoints);
				// result = new
				// StatusCode(StatusCodes.Bad_NoContinuationPoints);
			}
			// add to end of the list
			this.cpBrowse.add(cp);
			return result;
		} finally {
			this.lock.unlock();
		}
	}

	/**
	 * Activates the session and binds it to the current secure channel.
	 * 
	 * @param Context                    OperationContext for the Request to store
	 *                                   ServerObjects to execute the Service.
	 * @param ClientSignature            This is a signature generated with the
	 *                                   private key associated with the
	 *                                   clientCertificate.
	 * @param ClientSoftwareCertificates These are the SoftwareCertificates which
	 *                                   have been issued to the Client application.
	 *                                   The productUri contained in the
	 *                                   SoftwareCertificates shall match the
	 *                                   productUri in the ApplicationDescription
	 *                                   passed by the Client in the CreateSession
	 *                                   requests. Certificates without matching
	 *                                   productUris should be ignored.
	 * @param UserIdentityToken          The credentials of the user associated with
	 *                                   the Client application. The Server uses
	 *                                   these credentials to determine whether the
	 *                                   Client should be allowed to activate a
	 *                                   Session and what resources the Client has
	 *                                   access to during this Session.
	 * @param userTokenSignature         If the Client specified a user identity
	 *                                   token that supports digital signatures,
	 *                                   then it shall create a signature and pass
	 *                                   it as this parameter. Otherwise the
	 *                                   parameter is omitted.
	 * @param serverSecureChannel
	 * @param LocaleIds                  List of locale ids in priority order for
	 *                                   localized strings. The first localeId in
	 *                                   the list has the highest priority. If the
	 *                                   Server returns a localized string to the
	 *                                   Client, the Server shall return the
	 *                                   translation with the highest priority that
	 *                                   it can. If it does not have a translation
	 *                                   for any of the locales identified in this
	 *                                   list, then it shall return the string value
	 *                                   that it has and include the locale id with
	 *                                   the string.
	 * @param ServerNonce                A random number that should never be used
	 *                                   in any other request. This number shall
	 *                                   have a minimum length of 32 bytes. The
	 *                                   Client shall use this value to prove
	 *                                   possession of its application instance
	 *                                   Certificate in the next call to
	 *                                   ActivateSession request.
	 * @return UserIdentityToken
	 * @throws ServiceResultException
	 */
	public UserIdentityToken validateBeforeActivate(SignatureData clientSignature,
			SignedSoftwareCertificate[] clientSoftwareCertificates, ExtensionObject userIdentityToken,
			SignatureData userTokenSignature, String[] localeIds, ByteString serverNonce,
			ServerSecureChannel serverSecureChannel) throws ServiceResultException {
		this.lock.lock();
		try {
			// verify that a secure chanel was specified
			if (serverSecureChannel == null) {
				throw new ServiceResultException(StatusCodes.Bad_SecureChannelIdInvalid);
			}
			// verify that the same security policy has been used
			EndpointDescription endpoint = null;
			// get the correct endpoint for the request
			for (EndpointDescription description : serverSecureChannel.getServer().getEndpointDescriptions()) {
				if (description.getSecurityPolicyUri().equals(serverSecureChannel.getSecurityPolicy().getPolicyUri())
						&& description.getEndpointUrl().equals(serverSecureChannel.getEndpoint().getEndpointUrl())
						&& description.getSecurityMode().equals(serverSecureChannel.getMessageSecurityMode())) {
					endpoint = description;
					break;
				}
			}
			if (endpoint == null) {
				throw new ServiceResultException(StatusCodes.Bad_InvalidArgument);
			}
			if (!endpoint.getSecurityPolicyUri().equals(this.endpointDescription.getSecurityPolicyUri())
					|| !endpoint.getSecurityMode().equals(this.endpointDescription.getSecurityMode())) {
				throw new ServiceResultException(StatusCodes.Bad_SecurityPolicyRejected);
			}
			// verify client signature
			if (this.clientCertificate != null) {
				byte[] dataToSign = ArrayUtils.append(this.serverCertificate.encodedCertificate,
						ByteString.asByteArray(this.serverNonce));
				if (!verifySecurityPolicy(this.clientCertificate, this.endpointDescription.getSecurityPolicyUri(),
						dataToSign, clientSignature)) {
					throw new ServiceResultException(StatusCodes.Bad_ApplicationSignatureInvalid);
				}
			}
			if (!this.isActivate) {
				// must active the session on the channel that was used to
				// create it.
				if (this.securechannelId != serverSecureChannel.getSecureChannelId()) {
					throw new ServiceResultException(StatusCodes.Bad_SecureChannelIdInvalid);
				}
			} else {
				// cannot change certificates after activation
				if (clientSoftwareCertificates != null && clientSoftwareCertificates.length > 0) {
					throw new ServiceResultException(StatusCodes.Bad_InvalidArgument);
				}
			}
			// validate the user identity token
			UserIdentityToken identityToken = validateUserIdentityToken(userIdentityToken, userTokenSignature);
			return identityToken;
		} finally {
			this.lock.unlock();
		}
	}

	public void validateRequest(RequestHeader requestHeader, RequestType requestType, int secureChannelId)
			throws ServiceResultException {
		this.lock.lock();
		try {
			// check the context that he is valid
			// if (!isSecureChannelValid(secureChannelId)) {
			// // TODO: update diagnostics
			// // updateDiagnosticCounters(requestType, true, true);
			// throw new ServiceResultException(
			// StatusCodes.Bad_SecureChannelIdInvalid);
			// }
			// verify that session has been activated
			if (!this.isActivate && RequestType.CloseSession != requestType) {
				// TODO:
				// updateDiagnosticCounters(requestType, true, true);
				throw new ServiceResultException(StatusCodes.Bad_SessionNotActivated);
				// throw new
				// ServiceResultException(StatusCodes.Bad_SessionNotActivated);
			}
			// verify timestamp
			if (requestHeader.getTimestamp() == null) {
				throw new ServiceResultException(StatusCodes.Bad_InvalidTimestamp);
			}
			long validRange = requestHeader.getTimestamp().getTimeInMillis() + this.maxRequestAge.longValue();
			if (validRange < DateTime.currentTime().getTimeInMillis()) {
				// TODO:
				// updateDiagnosticCounters(requestType, true, false);
				throw new ServiceResultException(StatusCodes.Bad_InvalidTimestamp);
			}
			// set last contact time
			this.clientLastContact = System.nanoTime();
			// request accepted
			// updateDiagnosticCounters(requestType, false, false);
		} finally {
			this.lock.unlock();
		}
	}

	public NodeId getAuthentifikationToken() {
		return this.authentifikationToken;
	}

	public ByteString getClientCertificate() {
		return this.clientCertificate;
	}

	public ByteString getClientNonce() {
		return this.clientNonce;
	}

	/**
	 * Get language locales for this session
	 * 
	 * @return
	 */
	public Locale[] getLocales() {
		return this.locales.values().toArray(new Locale[0]);
	}

	public String[] getLocaleIds() {
		return this.locales.keySet().toArray(new String[0]);
	}

	public int[] getRoleIndizes() {
		return this.roleIndizes;
	}

	public void setRoleIndizes(int[] roleIndex) {
		this.roleIndizes = roleIndex;
	}

	@Override
	public NodeId getSessionId() {
		return this.sessionId;
	}

	public Double getSessionTimeout() {
		return sessionTimeout;
	}

	public Cert getServerCert() {
		return this.serverCertificate;
	}

	public ByteString getServerNonce() {
		return this.serverNonce;
	}

	/**
	 * Checks if the secure channel is currently valid.
	 * 
	 * @param SecureChannelId The identifier for the SecureChannel that the new
	 *                        token should belong to. This parameter shall be null
	 *                        when creating a new SecureChannel.
	 * @return TRUE if the Securuechannel is valid, otherwise FALSE.
	 */
	public boolean isSecureChannelValid(int secureChannelId) {
		this.lock.lock();
		try {
			return this.securechannelId == secureChannelId;
		} finally {
			this.lock.unlock();
		}
	}

	public void saveHistoryContinuationPoint(UUID uuid, HistoryReadCPItem historyRead) {
		// TODO Auto-generated method stub
	}

	public HistoryReadCPItem restoreHistoryContinuationPoint(byte[] id) {
		// TODO Auto-generated method stub
		return null;
	}

	public EndpointDescription getEndpointDescription() {
		return this.endpointDescription;
	}

	public OPCSessionConfigurator getSessionConfiguration() {
		return this.sessionConfiguration;
	}

	public String getSessionname() {
		return this.sessionname;
	}

	public OPCInternalServer getServer() {
		return this.server;
	}

	@Override
	public AtomicInteger getSeqNrFilehandles() {
		return this.handles;
	}

	@Override
	public List<FileHandle> getFileHandles() {
		return this.filehandles;
	}

	public UnsignedInteger getMaxMessageSize() {
		return maxMessageSize;
	}

	public void setMaxMessageSize(UnsignedInteger maxMessageSize) {
		this.maxMessageSize = maxMessageSize;
	}

	public int getMaxHistoryContinuationPoints() {
		return maxHistoryContinuationPoints;
	}

	public void setMaxHistoryContinuationPoints(int maxHistoryContinuationPoints) {
		this.maxHistoryContinuationPoints = maxHistoryContinuationPoints;
	}

	public UserIdentity getIdentity() {
		return identity;
	}

	public void setIdentity(UserIdentity identity) {
		this.identity = identity;
	}

	public SignedSoftwareCertificate[] getSoftwareCertificates() {
		return softwareCertificates;
	}

	public void setSoftwareCertificates(SignedSoftwareCertificate[] softwareCertificates) {
		this.softwareCertificates = softwareCertificates;
	}

	public UserIdentityToken getIdentityToken() {
		return identityToken;
	}

	public void setIdentityToken(UserIdentityToken identityToken) {
		this.identityToken = identityToken;
	}

	/**
	 * Update the requested locale ids
	 * 
	 * @param LocaleIds List of locale ids in priority order for localized strings.
	 *                  The first localeId in the list has the highest priority. If
	 *                  the Server returns a localized string to the Client, the
	 *                  Server shall return the translation with the highest
	 *                  priority that it can. If it does not have a translation for
	 *                  any of the locales identified in this list, then it shall
	 *                  return the string value that it has and include the locale
	 *                  id with the string.
	 * @return TRUE if the new LocaleIds are different from the old LocaleIds,
	 *         otherwise FALSE.
	 */
	private boolean updateLocaleIds(String[] localeIds) {
		if (localeIds == null) {
			return false;
		}
		boolean changes = false;
		this.lock.lock();
		try {
			for (int i = 0; i < localeIds.length; i++) {
				Locale locale = new Locale(localeIds[i]);
				Locale hasChanged = this.locales.put(localeIds[i], locale);
				if (hasChanged == null) {
					changes = true;
				}
			}
			if (changes) {
				/** session diagnostics */
				// SessionDiagnostics[] diagnostics = {
				// SessionDiagnostics.LocaleIds };
				// Object[] values = { localeIds };
				// this.server.getDiagnosticsNodeManager()
				// .updateSessionDiagnosticsObject(this, diagnostics,
				// values);
			}
		} finally {
			this.lock.unlock();
		}
		return changes;
	}

	/**
	 * Updates the user identity
	 * 
	 * @param NewIdentity       New Identity to update. The credentials of the user
	 *                          associated with the Client application. The Server
	 *                          uses these credentials to determine whether the
	 *                          Client should be allowed to activate a Session and
	 *                          what resources the Client has access to during this
	 *                          Session.
	 * @param Identity          If the Client specified a user identity token that
	 *                          supports digital signatures, then it shall create a
	 *                          signature and pass it as this parameter. Otherwise
	 *                          the parameter is omitted.
	 * @param EffectiveIdentity If the Client specified a user identity token that
	 *                          supports digital signatures, then it shall create a
	 *                          signature and pass it as this parameter. Otherwise
	 *                          the parameter is omitted.
	 * @return TRUE if the new identity is different from the old identity,
	 *         otherwise FALSE.
	 * 
	 */
	private boolean updateUserIdentity(UserIdentityToken newIdentity, UserIdentity identity,
			UserIdentity effectiveIdentity) {
		if (newIdentity == null) {
			throw new IllegalArgumentException("New Identity is null");
		}
		// check changed
		boolean changed = false;
		this.lock.lock();
		try {
			// no active identity, means first initialization
			changed = this.effectiveIdentity == null && effectiveIdentity != null;
			// identity exists, check if the new identity is different
			if (this.effectiveIdentity != null) {
				changed = !this.effectiveIdentity.equals(effectiveIdentity);
			}
			// always save the new identity since it may have additional
			// information that does not affect eqality
			this.setIdentityToken(newIdentity);
			this.setIdentity(identity);
			this.effectiveIdentity = effectiveIdentity;
			/** update session diagnostics */
			// SessionDiagnostics[] diagnostics = {
			// SessionDiagnostics.ClientUserIdOfSession,
			// SessionDiagnostics.AuthenticationMechanism,
			// SessionDiagnostics.ClientUserIdHistory };
			//
			// List<String> historyUser = new ArrayList<String>(
			// Arrays.asList(this.securityDiagnostics
			// .getClientUserIdHistory()));
			// historyUser.add(identity.getDisplayName());
			//
			// Object[] values = { identity.getDisplayName(),
			// identity.getTokenType().name(),
			// historyUser.toArray(new String[0]) };
			// this.server.getDiagnosticsNodeManager()
			// .updateSessionDiagnosticsObject(this, diagnostics, values);
		} finally {
			this.lock.unlock();
		}
		return changed;
	}

	/**
	 * Verifies a SecurityPolicy.
	 * 
	 * @param ClientCertificate A Certificate that identifies the Client.
	 * @param SecurityPolicyUri The URI for SecurityPolicy to use when securing
	 *                          messages sent over the SecureChannel.
	 * @param DataToVerify      ServerCertificate and ServerNonce to make a unique
	 *                          verification.
	 * @param Signature         This is a signature generated with the private key
	 *                          associated with the serverCertificate.
	 * @return TRUE if the SecurityPolicy is valids, otherwise FALSE.
	 * @throws ServiceResultException
	 */
	private boolean verifySecurityPolicy(ByteString clientCertificate, String securityPolicyUri, byte[] dataToVerify,
			SignatureData signature) throws ServiceResultException {
		// check if nothing to do
		if (signature == null) {
			return true;
		}
		// nothing more to do
		if (securityPolicyUri == null || securityPolicyUri.isEmpty()) {
			return true;
		} else if (SecurityPolicy.NONE.getPolicyUri().equals(securityPolicyUri)) {
			return true;
		} else if (SecurityPolicy.BASIC128RSA15.getPolicyUri().equals(securityPolicyUri)
				|| SecurityPolicy.BASIC256.getPolicyUri().equals(securityPolicyUri)) {
			if (SecurityConstants.RsaSha1.equals(signature.getAlgorithm())) {
				return true;
			}
		} else if (SecurityPolicy.AES128_SHA256_RSAOAEP.getPolicyUri().equals(securityPolicyUri)) {
			if (SecurityAlgorithm.RsaSha256.getUri().equals(signature.getAlgorithm())) {
				return true;
			}
		} else if (SecurityPolicy.AES256_SHA256_RSAPSS.getPolicyUri().equals(securityPolicyUri)) {
			if (SecurityAlgorithm.RsaPssSha256.getUri().equals(signature.getAlgorithm())) {
				return true;
			}
		} else if (SecurityPolicy.BASIC256SHA256.getPolicyUri().equals(securityPolicyUri)) {
			if (SecurityAlgorithm.RsaSha256.getUri().equals(signature.getAlgorithm())) {
				return true;
			}
		} else {
			throw new ServiceResultException(StatusCodes.Bad_SecurityPolicyRejected,
					"Unsupported Security Policy " + securityPolicyUri);
		}
		// throw new ServiceResultException(StatusCodes.Bad_SecurityChecksFailed,
		// "Unexpected signature algoritm: " + signature.getAlgorithm());
		Logger.getLogger(getClass().getName()).log(Level.INFO,
				"Unexpected signature algoritm: " + signature.getAlgorithm());
		return false;
	}

	/**
	 * Validates the UserIdentityToken and the UserIdentitySignature.
	 * 
	 * @param UserIdentityToken  The credentials of the user associated with the
	 *                           Client application. The Server uses these
	 *                           credentials to determine whether the Client should
	 *                           be allowed to activate a Session and what resources
	 *                           the Client has access to during this Session.
	 * @param UserTokenSignature If the Client specified a user identity token that
	 *                           supports digital signatures, then it shall create a
	 *                           signature and pass it as this parameter. Otherwise
	 *                           the parameter is omitted.
	 * @return UserIdentityToken
	 * @throws ServiceResultException
	 */
	private UserIdentityToken validateUserIdentityToken(ExtensionObject userIdentityToken,
			SignatureData userTokenSignature) throws ServiceResultException {
		UserTokenPolicy policy = null;
		UserIdentityToken token = null;
		// check for empty token
		if (userIdentityToken == null || userIdentityToken.decode(EncoderContext.getDefaultInstance()) == null) {
			// not changing the token if already activated
			if (this.isActivate) {
				return null;
			}
			// check if the anonymous login is permitted
			if (this.endpointDescription.getUserIdentityTokens() != null
					&& this.endpointDescription.getUserIdentityTokens().length > 0) {
				boolean found = false;
				for (int ii = 0; ii < this.endpointDescription.getUserIdentityTokens().length; ii++) {
					if (this.endpointDescription.getUserIdentityTokens()[ii].getTokenType()
							.equals(UserTokenType.Anonymous)) {
						found = true;
						policy = this.endpointDescription.getUserIdentityTokens()[ii];
						break;
					}
				}
				if (!found) {
					throw new ServiceResultException(StatusCodes.Bad_UserAccessDenied,
							"Anonymous user token policy not supported");
				}
				// create an anonymous token to use for subsequent
				// validation
				AnonymousIdentityToken anonymousToken = new AnonymousIdentityToken();
				anonymousToken.setPolicyId(policy.getPolicyId());
				return anonymousToken;
			}
		}
		// check for unrecognized token.
		if (userIdentityToken == null
				|| !(userIdentityToken.decode(EncoderContext.getDefaultInstance()) instanceof UserIdentityToken)) {
			throw new ServiceResultException(StatusCodes.Bad_UserAccessDenied, "Invalid user identity token provided");
		}
		// get the token
		token = userIdentityToken.decode(EncoderContext.getDefaultInstance());
		policy = this.endpointDescription.findUserTokenPolicy(token.getPolicyId());
		if (policy == null) {
			if (!(token instanceof AnonymousIdentityToken)) {
				throw new ServiceResultException(StatusCodes.Bad_IdentityTokenInvalid, "UserTokenPolicy not supported");
			}
			policy = UserTokenPolicy.ANONYMOUS;
			token.setPolicyId(policy.getPolicyId());
		}
		// determine the security policy uri.
		String securityPolicyUri = policy.getSecurityPolicyUri();
		if (securityPolicyUri == null) {
			securityPolicyUri = this.endpointDescription.getSecurityPolicyUri();
		}
		// decypt the token
		if (this.serverCertificate == null) {
			this.serverCertificate = CertificateFactory.create(this.endpointDescription.getServerCertificate(), true);
		}
		// check that the token is permitted for the endpoint
		UserTokenPolicy identityPolicy = this.endpointDescription.findUserTokenPolicy(token.getPolicyId());
		if (identityPolicy == null) {
			throw new ServiceResultException(StatusCodes.Bad_UserAccessDenied);
		}
		// find the policy
		securityPolicyUri = identityPolicy.getSecurityPolicyUri();
		if (securityPolicyUri == null || securityPolicyUri.isEmpty()) {
			securityPolicyUri = this.endpointDescription.getSecurityPolicyUri();
		}
		// check user token signature for authentication with certificate
		if (token instanceof X509IdentityToken) {
			SecurityAlgorithm algorithm = SecurityAlgorithm.valueOfUri(userTokenSignature.getAlgorithm());
			Cert cert = new Cert(((X509IdentityToken) token).getCertificateData().getValue());
			boolean verified = false;
			try {
				Signature signature = Signature.getInstance(algorithm.getTransformation());
			
				signature.initVerify(cert.getCertificate());
				signature.update(ByteString.asByteArray(this.endpointDescription.getServerCertificate()));
				signature.update(this.serverNonce.getValue());
				
				verified = signature.verify(userTokenSignature.getSignature().getValue());
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (SignatureException e) {
				e.printStackTrace();
			}
			if (!verified) {
				throw new ServiceResultException(StatusCodes.Bad_UserSignatureInvalid);
			}
		}
		return token;
	}
}
