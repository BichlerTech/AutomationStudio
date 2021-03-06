package opc.sdk.server.core.managers;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.opcfoundation.ua.builtintypes.ByteString;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.ServiceResult;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AnonymousIdentityToken;
import org.opcfoundation.ua.core.EndpointDescription;
import org.opcfoundation.ua.core.IssuedIdentityToken;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.core.UserIdentityToken;
import org.opcfoundation.ua.core.UserNameIdentityToken;
import org.opcfoundation.ua.core.UserTokenPolicy;
import org.opcfoundation.ua.core.UserTokenType;
import org.opcfoundation.ua.core.X509IdentityToken;
import org.opcfoundation.ua.transport.security.Cert;
import org.opcfoundation.ua.transport.security.CertificateValidator;
import org.opcfoundation.ua.transport.security.KeyPair;
import org.opcfoundation.ua.transport.security.PrivKey;
import org.opcfoundation.ua.transport.security.SecurityAlgorithm;
import org.opcfoundation.ua.transport.security.SecurityPolicy;
import org.opcfoundation.ua.transport.security.SecurityPolicyUri;
import org.opcfoundation.ua.utils.CertificateUtils;
import org.opcfoundation.ua.utils.CryptoUtil;
import org.opcfoundation.ua.utils.EndpointUtil;

import com.bichler.opc.comdrv.ComDRVManager;
import com.bichler.opc.driver.base.ComDRV;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.user.AuthorityRule;
import opc.sdk.core.node.user.DBRole;
import opc.sdk.core.node.user.DBUser;
import opc.sdk.core.session.AllowNoneCertificateValidator;
import opc.sdk.server.core.OPCInternalServer;
import opc.sdk.server.core.user.IServerAuthentification;
import opc.sdk.server.service.session.OPCServerSession;

public class OPCUserAuthentificationManager extends ComDRV implements IServerAuthentification, IOPCManager {
	/**
	 * Flag that indicates annonymous authentification is allowed.
	 */
	private boolean allowAnonymous = true;
	/**
	 * Flag that indicates none security is allowed.
	 */
	private boolean allowSecurityNone = true;
	private Map<Integer, DBRole> roles = new HashMap<>();
	private List<Integer> roleIndizes = new ArrayList<>();
	private Map<String, DBUser> users = new HashMap<>();
	private OPCInternalServer server = null;
	private String dbUserPath = null;
	private Logger logger = Logger.getLogger(getClass().getName());
	private Provider provider;
	private CertificateValidator userAuthCertStore;

	public OPCUserAuthentificationManager(OPCInternalServer server) {
		this.server = server;
		String securityProviderName = CryptoUtil.getSecurityProviderName();
		this.provider = Security.getProvider(securityProviderName);
	}

	public boolean isAllowAnnonymous() {
		return this.allowAnonymous;
	}

	public boolean isAllowSecurityNone() {
		return this.allowSecurityNone;
	}

	public void setAllowAnnonymous(boolean allowAnnonymous) {
		this.allowAnonymous = allowAnnonymous;
	}

	public void setAllowSecurityNone(boolean allowSecurityNone) {
		this.allowSecurityNone = allowSecurityNone;
	}

	public void setUserDatabase(String dbUser) {
		this.dbUserPath = dbUser;
	}

	@Override
	public boolean start() {
		this.userAuthCertStore = this.server.getApplicationConfiguration().getUserAuthCertValidator();
		if (!this.server.isUseUserAuthentification())
			return false;
		if (this.hasInitialized) {
			// already initialized
			return true;
		}
		if (this.dbUserPath == null) {
			// TODO: no userdatabase has been set
			return false;
		}
		File usrFile = new File(this.dbUserPath);
		// no dbfile
		if (!usrFile.exists()) {
			return false;
		}
		// load sqlite driver
		Connection sqlConnection = null;
		try {
			Class.forName("org.sqlite.JDBC");
			sqlConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbUserPath);
			initUserConfiguration(sqlConnection);
		} catch (ClassNotFoundException | SQLException | LinkageError e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			// error find sqlite driver
		} finally {
			if (sqlConnection != null) {
				try {
					sqlConnection.close();
				} catch (SQLException e) {
					logger.log(Level.SEVERE, e.getMessage(), e);
				}
			}
		}
		this.hasInitialized = true;
		return this.hasInitialized;
	}

	@Override
	public boolean stop() {
		this.roles.clear();
		this.roleIndizes.clear();
		this.users.clear();
		// TODO: remove authority from nodes (?)
		this.hasInitialized = false;
		return this.hasInitialized;
	}

	/**
	 * Check if login is possible to the opc ua server from a client connection.
	 * 
	 * @param session
	 * @param newIdentity
	 * @return
	 * @throws ServiceResultException
	 */
	@Override
	public StatusCode verifyAuthentification(OPCServerSession session, UserIdentityToken newIdentity)
			throws ServiceResultException {
		if (newIdentity instanceof AnonymousIdentityToken) {
			return verifyAnonymous(session, (AnonymousIdentityToken) newIdentity);
		} else if (newIdentity instanceof UserNameIdentityToken) {
			return verifyUsernamePassword(session, (UserNameIdentityToken) newIdentity);
		} else if (newIdentity instanceof IssuedIdentityToken) {
			return verifyIssued(session, (IssuedIdentityToken) newIdentity);
		} else if (newIdentity instanceof X509IdentityToken) {
			return verifyX509Identity(session, (X509IdentityToken) newIdentity);
		}
		return StatusCode.GOOD;
	}

	@Override
	public StatusCode verifyAnonymous(OPCServerSession session, AnonymousIdentityToken newIdentity) {
		if (this.allowAnonymous) {
			return StatusCode.GOOD;
		}
		return StatusCode.BAD;
	}

	@Override
	public StatusCode verifyIssued(OPCServerSession session, IssuedIdentityToken newIdentity) {
		return null;
	}

	public ServiceResult checkAuthorityFromNode(AuthorityRule serviceRule, OPCServerSession session, Node node) {
		// usually this check has been done previously
		if (node == null) {
			return new ServiceResult(StatusCode.GOOD);
		}
		int[] roleIndizes = null;
		// no session allows all rightes
		if (session == null) {
			return new ServiceResult(StatusCode.GOOD);
		}
		roleIndizes = session.getRoleIndizes();
		// no role for this session
		if (roleIndizes == null) {
			return new ServiceResult(StatusCode.GOOD);
		}
		// get roleindex of session
		boolean isValid = node.checkAuthority(roleIndizes, serviceRule);
		if (!isValid) {
			return new ServiceResult(StatusCodes.Bad_UserAccessDenied);
		}
		return new ServiceResult(StatusCode.GOOD);
	}

	@Override
	public StatusCode verifyUsernamePassword(OPCServerSession session, UserNameIdentityToken token)
			throws ServiceResultException {
		EndpointDescription ep = session.getEndpointDescription();
		UserTokenPolicy endpointPolicy = ep.findUserTokenPolicy(UserTokenType.UserName);
		if (endpointPolicy == null) {
			throw new ServiceResultException(StatusCodes.Bad_IdentityTokenRejected, "UserName not supported");
		}
		String username = null;
		String password = null;
		// security endpoint policy
		String endpointSecurityPolicyUri = endpointPolicy.getSecurityPolicyUri();
		// security token policy no decryption
		if (endpointSecurityPolicyUri == null || endpointSecurityPolicyUri.isEmpty()
				|| SecurityPolicy.NONE.getPolicyUri().equals(endpointSecurityPolicyUri)) {
			try {
				username = token.getUserName();
				password = new String(ByteString.asByteArray(token.getPassword()), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}
		// secure token to decrypt
		else {
			Cert cert = new Cert(ByteString.asByteArray(ep.getServerCertificate()));
			KeyPair[] keyPair = this.server.getApplication().getApplicationInstanceCertificates();
			PrivKey privKey = null;
			for (KeyPair kp : keyPair) {
				if (kp.getCertificate().equals(cert)) {
					privKey = kp.getPrivateKey();
					break;
				}
			}
			// last send nonce
			ByteString serverNonce = session.getServerNonce();
			ByteString pw = decryptUserPassword(cert, endpointSecurityPolicyUri, token.getEncryptionAlgorithm(),
					ByteString.asByteArray(token.getPassword()), ByteString.asByteArray(serverNonce));
			username = token.getUserName();
			try {
				// pw =
				password = new String(ByteString.asByteArray(pw), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}
		if (username == null || username.isEmpty()) {
			throw new ServiceResultException(StatusCodes.Bad_IdentityTokenInvalid, "Username [empty] not supported");
		}
		// call underlying user management
		if (ComDRVManager.getDRVManager().loginUser(username, password, -1))
			return StatusCode.GOOD;
		// find user
		DBUser user = this.users.get(username);
		if (user == null) {
			throw new ServiceResultException(StatusCodes.Bad_IdentityTokenRejected, "User not supported");
		}
		// does username/password phrase work
		StatusCode result = user.match(username, password);
		if (result.isBad()) {
			return result;
		}
		// find role to user
		int mask = user.getRoleMask();
		List<DBRole> sessionRoles = new ArrayList<>();
		for (DBRole role : this.roles.values()) {
			if ((mask & (int) (Math.scalb(1.0, role.getId()))) != 0) {
				sessionRoles.add(role);
			}
		}
		int[] sessionIndizes = null;
		// fill up indizes
		if (!sessionRoles.isEmpty()) {
			sessionIndizes = new int[sessionRoles.size()];
			for (int i = 0; i < sessionRoles.size(); i++) {
				DBRole role = sessionRoles.get(i);
				int index = this.roleIndizes.indexOf(role.getId());
				sessionIndizes[i] = index;
			}
		}
		session.setRoleIndizes(sessionIndizes);
		return result;
	}

	@Override
	public StatusCode verifyX509Identity(OPCServerSession session, X509IdentityToken newIdentity)
			throws ServiceResultException {
		StatusCode result = new StatusCode(StatusCodes.Bad_SecurityChecksFailed);
		Cert certificate = new Cert(newIdentity.getCertificateData().getValue());
		// server certificate
		KeyPair serverCert = this.server.getApplication().getApplicationInstanceCertificate();
		// client authentication certificate
		final X509Certificate x509Cert = certificate.getCertificate();
		try {
			// check if server certificate is issuer
			x509Cert.checkValidity();
			serverCert.getCertificate().getCertificate().checkValidity();
			try {
				x509Cert.verify(x509Cert.getPublicKey());
				PublicKey pkey = serverCert.getCertificate().getCertificate().getPublicKey();
				x509Cert.verify(pkey);
				return StatusCode.GOOD;
			} catch (GeneralSecurityException e1) {
				// skip
			}
			// validate through cert store
			if (this.userAuthCertStore != null) {
				result = this.userAuthCertStore.validateCertificate(null, certificate);
			}
		} catch (CertificateExpiredException | CertificateNotYetValidException e) {
			result = new StatusCode(StatusCodes.Bad_CertificateTimeInvalid);
		}
		return result;
	}

	// found problem, when in has a zero in the middle, the function works wrong
	byte[] unpadZeros(byte[] in) {
		int i = 0;
		while (i < in.length && in[i] != 0)
			i++;
		return Arrays.copyOfRange(in, 0, i);
	}

	byte[] unpadZerosBackwards(byte[] in) {
		int i = in.length;
		while (i > 0 && in[i - 1] == 0)
			i--;
		return Arrays.copyOfRange(in, 0, i);
	}

	private byte[] unpadZeros(byte[] in, int length) {
		return Arrays.copyOfRange(in, 0, length);
	}

	/**
	 * calcualte length of password in bytecount
	 * 
	 * @param start
	 * @return
	 */
	private int getLength(byte[] start) {
		int length = 0;
		for (int i = start.length; i > 0; i--) {
			length *= 256;
			if (start[i - 1] == 0)
				continue;
			length += start[i - 1];
		}
		return length;
	}

	private ByteString decryptUserPassword(Cert cert, String securityPolicy, String encryptionAlgorithm, byte[] data,
			byte[] serverNonce) throws ServiceResultException {
		if (data == null) {
			return null;
		}
		SecurityAlgorithm algorithm = SecurityAlgorithm.valueOfUri(encryptionAlgorithm);
		if (algorithm == null) {
			throw new ServiceResultException(StatusCodes.Bad_IdentityTokenInvalid);
			// return data;
		}
		// cert.getCertificate().get
		KeyPair[] keyPair = this.server.getApplication().getApplicationInstanceCertificates();
		PrivKey privKey = null;
		for (KeyPair kp : keyPair) {
			if (kp.getCertificate().equals(cert)) {
				privKey = kp.getPrivateKey();
				break;
			}
		}
		try {
			Cipher cipher = CryptoUtil.getAsymmetricCipher(algorithm);
			if (privKey != null)
				cipher.init(Cipher.DECRYPT_MODE, privKey.getPrivateKey());
			byte[] decrypt = cipher.doFinal(data);
			// first 4 bytes are the length
			byte[] start = getPasswordLength(decrypt);
			// calcualte password length in byte
			int length = getLength(start);
			decrypt = removeFirst(decrypt, start);
			decrypt = unpadZeros(decrypt, length);
			if (serverNonce != null) {
				decrypt = removeLast(decrypt, serverNonce);
			}
			return ByteString.valueOf(decrypt);
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		} catch (ServiceResultException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			throw e;
		}
		// nothing more to do with no security
		/**
		 * case SecurityPolicy.URI_BINARY_NONE: if (algorithm == null) { return data; }
		 * break;
		 */
		if (SecurityPolicyUri.URI_BINARY_BASIC128RSA15.equals(securityPolicy)) {
			if (algorithm == SecurityAlgorithm.RsaOaep) {
				return null;
			}
		} else if (SecurityPolicyUri.URI_BINARY_BASIC256.equals(securityPolicy)) {
		}
		byte[] decrypted = null;
		return ByteString.valueOf(decrypted);
	}

	/** new decrypt function to test pwd decryption */
	public int decryptAsymm(Cert cert, /* PrivateKey decryptingKey, */
			String encryptionAlgorithm, byte[] data, byte[] output, int outputOffset) throws ServiceResultException {
		SecurityAlgorithm algorithm = SecurityAlgorithm.valueOfUri(encryptionAlgorithm);
		if (algorithm == null) {
			throw new ServiceResultException(StatusCodes.Bad_IdentityTokenInvalid);
		}
		KeyPair[] keyPair = this.server.getApplication().getApplicationInstanceCertificates();
		PrivKey privKey = null;
		for (KeyPair kp : keyPair) {
			if (kp.getCertificate().equals(cert)) {
				privKey = kp.getPrivateKey();
				break;
			}
		}
		if (privKey == null) {
			logger.log(Level.SEVERE, "decrypt: error");
			throw new ServiceResultException(StatusCodes.Bad_InternalError, "decrypt: error. Private key is null");
		}
		RSAPrivateKey decryptingKeyRSA = privKey.getPrivateKey();
		int inputBlockSize = decryptingKeyRSA.getModulus().bitLength() / 8;
		Cipher cipher;
		try {
			cipher = getAsymmetricCipher(algorithm, decryptingKeyRSA);
		} catch (InvalidKeyException e) {
			logger.log(Level.INFO, "decrypt: The provided RSA key is invalid", e);
			throw new ServiceResultException(StatusCodes.Bad_SecurityChecksFailed, e);
		} catch (GeneralSecurityException e) {
			throw new ServiceResultException(StatusCodes.Bad_InternalError, e);
		}
		// Verify block sizes
		if (data.length % inputBlockSize != 0) {
			logger.log(Level.SEVERE, "decrypt: Wrong blockSize!!!");
			throw new ServiceResultException(StatusCodes.Bad_InternalError,
					"Error in asymmetric decrypt: Input data is not an even number of encryption blocks.");
		}
		try {
			logger.log(Level.INFO, "JceCipherDecrypt={}", cipher.toString());
			int maxIndex = outputOffset + data.length;
			// initialize return value, value tells how bytes has been stored in
			// output
			int totalDecryptedBytes = 0;
			// this value tells how many bytes where added to buffer in each
			// iteration
			int amountOfDecryptedBytes = -1;
			int inputOffset = 0;
			for (int index = outputOffset; index < maxIndex; index += inputBlockSize) {
				amountOfDecryptedBytes = cipher.doFinal(data, inputOffset, inputBlockSize, output, outputOffset);
				inputOffset += inputBlockSize;
				outputOffset += amountOfDecryptedBytes;
				// Update amount of total decrypted bytes
				totalDecryptedBytes += amountOfDecryptedBytes;
			}
			return totalDecryptedBytes;
		} catch (GeneralSecurityException e) {
			logger.log(Level.SEVERE, "decrypt: error", e);
			throw new ServiceResultException(StatusCodes.Bad_InternalError, e);
		}
	}

	private Cipher getAsymmetricCipher(SecurityAlgorithm algorithm, PrivateKey privateKey)
			throws NoSuchProviderException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
			InvalidAlgorithmParameterException {
		Cipher cipher;
		try {
			cipher = Cipher.getInstance(algorithm.getTransformation(), provider);
		} catch (NoSuchAlgorithmException e) {
			// SunJCE does not recognize RSA/NONE/PKCS1Padding, but will
			// return such with plain RSA
			cipher = Cipher.getInstance(algorithm.getStandardName(), provider);
		}
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		logger.log(Level.WARNING, "decrypt: cipher.provider={}", cipher.getProvider());
		return cipher;
	}

	public void addAuthority(String ns, String id, int[] role, int[] auth) {
		int nsindex = server.getNamespaceUris().getIndex(ns);
		if (nsindex >= 0) {
			NodeId nid = NodeId.parseNodeId("ns=" + nsindex + ";" + id);
			if (nid != null) {
				for (int i = 0; i < role.length; i++) {
					Integer index = roleIndizes.indexOf(role[i]);
					// find node
					Node node = this.server.getAddressSpaceManager().getNodeById(nid);
					if (node == null || index == -1) {
						continue;
					}
					// if(index >= 0)
					// set authority
					node.setAuthority(index, auth[i]);
				}
			}
		}
	}

	private void initUserConfiguration(Connection sqlConnection) {
		PreparedStatement statement = null;
		// configuration
		try {
			String query = "SELECT PROPERTY,VALUE FROM CONFIGURATION";
			statement = sqlConnection.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				String property = rs.getString("PROPERTY");
				String value = rs.getString("VALUE");
				if ("ALLOW_ANNONYMOUS".equals(property)) {
					this.allowAnonymous = Boolean.parseBoolean(value);
				} else if ("ALLOW_UNSECURE".equals(property)) {
					this.allowSecurityNone = Boolean.parseBoolean(value);
				}
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			return;
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					logger.log(Level.SEVERE, e.getMessage(), e);
				}
			}
		}
		// user roles
		Map<Integer, DBRole> roles = new HashMap<>();
		List<Integer> roleIndizes = new ArrayList<>();
		try {
			String query = "SELECT ID, NAME FROM ROLES;";
			statement = sqlConnection.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("ID");
				String name = rs.getString("NAME");
				DBRole role = new DBRole(id, name);
				roles.put(id, role);
				roleIndizes.add(id);
			}
		} catch (SQLException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			// cannot find role table
			return;
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
				}
			}
		}
		// user logins
		Map<String, DBUser> users = new HashMap<>();
		try {
			String query = "SELECT ID, ID_ROLE, NAME, PASSWORD FROM USERS;";
			statement = sqlConnection.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("ID");
				int rolemask = rs.getInt("ID_ROLE");
				String name = rs.getString("NAME");
				String password = rs.getString("PASSWORD");
				DBUser user = new DBUser(id, rolemask, name, password);
				users.put(name, user);
			}
		} catch (SQLException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			// cannot find role table
			return;
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
				}
			}
		}
		// node authorities
		try {
			// map nodes with authorities
			String query = "SELECT NS_NODE, ID_NODE, ID_ROLE, AUTHORITY FROM NODES;";
			statement = sqlConnection.prepareStatement(query);
			// find nodes with authority restriction
			ResultSet rs = statement.executeQuery();
			long lastwatchdogwrite = 0;
			// iterate resultset
			while (rs.next()) {
				if (lastwatchdogwrite + (1000L * 1000000L * 10L) < System.nanoTime()) {
					lastwatchdogwrite = System.nanoTime();
				}
				String ns_node = rs.getString("NS_NODE");
				int nsIndex = server.getNamespaceUris().getIndex(ns_node);
				String id_node = rs.getString("ID_NODE");
				int id_role = rs.getInt("ID_ROLE");
				int authority = rs.getInt("AUTHORITY");
				// parse nodeid with namespace
				NodeId nodeId = NodeId.parseNodeId((nsIndex > 0 ? "ns=" + nsIndex + ";" : "") + "" + id_node);
				// NodeId nodeId = NodeId.parseNodeId(id_node);
				Integer index = roleIndizes.indexOf(id_role);
				// find node
				Node node = this.server.getAddressSpaceManager().getNodeById(nodeId);
				if (node == null || index == -1) {
					continue;
				}
				// set authority
				node.setAuthority(index, authority);
			}
		} catch (SQLException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			// some error on creating a db connection
			return;
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
				}
			}
		}
		// fill in userauthentifications
		this.roleIndizes.addAll(roleIndizes);
		this.roles.putAll(roles);
		this.users.putAll(users);
	}

	private byte[] removeFirst(byte[] origin, byte[] aparted) {
		int start2cut = aparted.length;
		int originLength = origin.length;
		int newLength = originLength - start2cut;
		byte[] newArray = new byte[newLength];
		for (int i = 0; i < newLength; i++) {
			newArray[i] = origin[start2cut++];
		}
		return newArray;
	}

	private byte[] removeLast(byte[] origin, byte[] aparted) throws ServiceResultException {
		int end2cut = aparted.length;
		int originLength = origin.length;
		int newLength = originLength - end2cut;
		byte[] newArray = null;
		try {
			newArray = new byte[newLength];
			for (int i = 0; i < newLength; i++) {
				newArray[i] = origin[i];
			}
		} catch (Exception e) {
			throw new ServiceResultException(StatusCodes.Bad_IdentityTokenRejected);
		}
		return newArray;
	}

	private byte[] getPasswordLength(byte[] decrypted) {
		final int toArrayLength = 4;
		byte[] fromArray = new byte[toArrayLength];
		for (int i = 0; i < toArrayLength; i++) {
			fromArray[i] = decrypted[i];
		}
		return fromArray;
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
