package opc.sdk.core.session;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.ByteString;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AnonymousIdentityToken;
import org.opcfoundation.ua.core.EndpointDescription;
import org.opcfoundation.ua.core.IssuedIdentityToken;
import org.opcfoundation.ua.core.SignatureData;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.core.UserIdentityToken;
import org.opcfoundation.ua.core.UserNameIdentityToken;
import org.opcfoundation.ua.core.UserTokenPolicy;
import org.opcfoundation.ua.core.UserTokenType;
import org.opcfoundation.ua.core.X509IdentityToken;
import org.opcfoundation.ua.transport.security.Cert;
import org.opcfoundation.ua.transport.security.KeyPair;
import org.opcfoundation.ua.transport.security.PrivKey;
import org.opcfoundation.ua.transport.security.SecurityConstants;
import org.opcfoundation.ua.transport.security.SecurityPolicy;
import org.opcfoundation.ua.utils.EndpointUtil;

/**
 * Useridentity information to activate a session.
 * 
 * @author Thomas Z&ouml;bauer
 * 
 */
public class UserIdentityRole implements UserIdentity {
	private String displayName = null;
	private UserIdentityToken token = null;
	private UserTokenType tokenType = null;
	private String policyId = null;
	String decryptedPassword;
	private KeyPair x509TokenCert;
	private byte[] issuedTokenCert;

	/**
	 * Initializes an Anonymous User.
	 */
	public UserIdentityRole() {
		this.tokenType = UserTokenType.Anonymous;
		this.displayName = UserTokenType.Anonymous.name();
		this.token = null;
	}

	/**
	 * Initializes the object with a username and password.
	 * 
	 * @param username
	 * @param encryptedPassword
	 */
	public UserIdentityRole(String username, String password) {
		if (username == null || username.isEmpty()) {
			throw new IllegalArgumentException("Username are not allowed to be empty!");
		}
		if (password != null) {
			try {
				this.decryptedPassword = password;
				initialize(
						// new UserNameIdentityToken(UserTokenType.UserName.name(),
						// username,
						// password.getBytes("UTF-8"), null));
						new UserNameIdentityToken(UserTokenType.UserName.name(), username,
								ByteString.valueOf(password.getBytes("UTF-8")), null));
			} catch (UnsupportedEncodingException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			}
		} else {
			initialize(new UserNameIdentityToken(UserTokenType.UserName.name(), username, null, null));
		}
	}

	public UserIdentityRole(KeyPair keyPair) {
		if (keyPair == null) {
			throw new IllegalArgumentException("Authentification certificate/key is null!");
		}
		//
		this.x509TokenCert = keyPair;
		initialize(new X509IdentityToken(UserTokenType.Certificate.name(),
				ByteString.valueOf(keyPair.getCertificate().getEncoded())));
	}

	/**
	 * Initializes the object with an issuedidentity token.
	 * 
	 * @param username
	 * @param password
	 */
	public UserIdentityRole(byte[] issuedtoken) {
		if (issuedtoken == null || issuedtoken.length <= 0) {
			throw new IllegalArgumentException("issued token not null or empty!");
		}
		this.issuedTokenCert = issuedtoken;
		initialize(new IssuedIdentityToken(UserTokenType.IssuedToken.name(), ByteString.valueOf(issuedtoken), null));
	}

	/**
	 * Initializes with an UA Identity Token.
	 */
	public UserIdentityRole(UserIdentityToken token) {
		initialize(token);
	}

	private void initialize(UserIdentityToken token) {
		if (token == null) {
			throw new IllegalArgumentException("UserIdentityToken");
		}
		this.policyId = token.getPolicyId();
		if (token instanceof AnonymousIdentityToken) {
			this.tokenType = UserTokenType.Anonymous;
			this.displayName = UserTokenType.Anonymous.name();
			this.token = null;
		} else if (token instanceof UserNameIdentityToken) {
			initialize((UserNameIdentityToken) token);
		} else if (token instanceof X509IdentityToken) {
			initialize((X509IdentityToken) token);
		} else if (token instanceof IssuedIdentityToken) {
			initialize((IssuedIdentityToken) token);
		} else {
			throw new IllegalArgumentException("TokenType Invalid");
		}
	}

	private void initialize(UserNameIdentityToken userNameIdentityToken) {
		this.token = userNameIdentityToken;
		this.tokenType = UserTokenType.UserName;
		this.displayName = userNameIdentityToken.getUserName();
	}

	private void initialize(X509IdentityToken certificateToken) {
		this.token = certificateToken;
		this.tokenType = UserTokenType.Certificate;
		this.displayName = UserTokenType.Certificate.name();
	}

	private void initialize(IssuedIdentityToken issuedToken) {
		this.token = issuedToken;
		this.tokenType = UserTokenType.IssuedToken;
		this.displayName = UserTokenType.IssuedToken.name();
	}

	@Override
	public String getDisplayName() {
		return this.displayName;
	}

	/**
	 * Getter of the Identity Token
	 * 
	 * @Override
	 * @return UserIdentityToken
	 */
	@Override
	public UserIdentityToken getIdentityToken() {
		// check for anonymous
		if (this.token == null) {
			AnonymousIdentityToken tmpToken = new AnonymousIdentityToken();
			tmpToken.setPolicyId(this.policyId);
			this.token = tmpToken;
			return tmpToken;
		}
		if (this.token instanceof AnonymousIdentityToken) {
			return this.token;
		}
		// return a user name token
		if (this.token instanceof UserNameIdentityToken) {
			return this.token;
		}
		if (this.token instanceof X509IdentityToken) {
			return this.token;
		}
		return null;
	}

	@Override
	public IssuedIdentityToken getIssuedTokenType() {
		return null;
	}

	@Override
	public boolean getSupportsSignatures() {
		if (this.token instanceof X509IdentityToken) {
			return true;
		}
		return false;
	}

	@Override
	public UserTokenType getTokenType() {
		return this.tokenType;
	}

	public String getPolicyId() {
		return this.policyId;
	}

	@Override
	public SignatureData sign(byte[] dataToSign, String securityPolicyUri) throws ServiceResultException {
		switch (this.tokenType) {
		case Certificate:
			// return this.userTokenSignature;
		case Anonymous:
		case UserName:
		case IssuedToken:
		default:
			return new SignatureData();
		}
	}

	/**
	 * Encrypts the DecryptedPassword using the EncryptionAlgorithm and places the
	 * result in Password
	 * 
	 * @Override
	 * @param ServerCertificateData
	 * @param ServerNonce
	 * @param SecurityPolicyUri
	 * @throws ServiceResultException
	 */
	@Override
	public void encrypt(SignatureData userTokenSignature, Cert certificate, PrivKey privKey,
			EndpointDescription endpoint, byte[] serverNonce, String securityPolicyUri) throws ServiceResultException {
		byte[] tmpServerNonce = serverNonce;
		/** no encryption */
		if (UserTokenType.Anonymous.equals(this.tokenType)) {
			return;
		}
		/** password */
		else if (UserTokenType.UserName.equals(this.tokenType)) {
			UserNameIdentityToken usernameToken = (UserNameIdentityToken) this.token;
			/** avoid nullpointer from EndpointUtil.createUserNameIdentityToken */
			if (tmpServerNonce == null) {
				tmpServerNonce = new byte[0];
			}
			if (((UserNameIdentityToken) this.token).getPassword() == null) {
				this.token = EndpointUtil.createUserNameIdentityToken(endpoint, ByteString.valueOf(tmpServerNonce),
						usernameToken.getUserName(), "");
				return;
			}
			/** handle no encryption */
			if (securityPolicyUri == null
					|| SecurityConstants.SECURITY_POLICY_URI_BINARY_NONE.equals(securityPolicyUri)) {
				this.token = EndpointUtil.createUserNameIdentityToken(endpoint, ByteString.valueOf(tmpServerNonce),
						usernameToken.getUserName(), this.decryptedPassword);
				return;
			}
			/**
			 * encryption required, but server has not returend any nonce to encrypt
			 */
			if (tmpServerNonce.length == 0) {
				throw new ServiceResultException(StatusCodes.Bad_NonceInvalid);
			}
			/** encrypt the password */
			this.token = EndpointUtil.createUserNameIdentityToken(endpoint, ByteString.valueOf(tmpServerNonce),
					usernameToken.getUserName(), this.decryptedPassword);
		}
		/** Certificate */
		else if (UserTokenType.Certificate.equals(this.tokenType)) {
			Cert serverCert = new Cert(endpoint.getServerCertificate().getValue());
			this.token = createX509IdentityToken(endpoint, tmpServerNonce,
					serverCert, this.x509TokenCert.getCertificate(), this.x509TokenCert.getPrivateKey().getPrivateKey(),
					userTokenSignature);
		}
		/** IssuedToken Type */
		else if (UserTokenType.IssuedToken.equals(this.tokenType)) {
			this.token = EndpointUtil.createIssuedIdentityToken(endpoint, ByteString.valueOf(tmpServerNonce),
					this.issuedTokenCert);
		}
	}

	/**
	 * <p>createX509IdentityToken.</p>
	 *
	 * @param ep a {@link org.opcfoundation.ua.core.EndpointDescription} object.
	 * @param serverNonce an array of byte.
	 * @param certificate a {@link org.opcfoundation.ua.transport.security.Cert} object.
	 * @param key a {@link java.security.PrivateKey} object.
	 * @param signatureData a {@link org.opcfoundation.ua.core.SignatureData} object.
	 * @return a {@link org.opcfoundation.ua.core.X509IdentityToken} object.
	 * @throws org.opcfoundation.ua.common.ServiceResultException if any.
	 */
	private static X509IdentityToken createX509IdentityToken(
			EndpointDescription ep, byte[] serverNonce, Cert serverCertificate, Cert certificate, PrivateKey key, SignatureData signatureData) throws ServiceResultException{
		if (signatureData == null)
			throw new NullPointerException("signatureData must be defined (will be filled in)");
		UserTokenPolicy policy = ep.findUserTokenPolicy(UserTokenType.Certificate);
		if (policy==null) throw new ServiceResultException(StatusCodes.Bad_IdentityTokenRejected,
				"Certificate UserTokenType is not supported");

		X509IdentityToken token = new X509IdentityToken( policy.getPolicyId(), ByteString.valueOf(certificate.getEncoded()) );		
		
		String securityPolicyUri = policy.getSecurityPolicyUri();
		securityPolicyUri = "http://opcfoundation.org/UA/SecurityPolicy#Basic256Sha256";
		if (securityPolicyUri==null) securityPolicyUri = ep.getSecurityPolicyUri();
		SecurityPolicy securityPolicy = SecurityPolicy.getSecurityPolicy( securityPolicyUri );
		Cert serverCert = new Cert(ByteString.asByteArray(ep.getServerCertificate()));
		if ((securityPolicy!=null) && (serverCert != null))
			try {
				// Create a Signature object and initialize it with the private
				// key
				Signature signature = Signature.getInstance(securityPolicy
						.getAsymmetricSignatureAlgorithm().getTransformation());
				signature.initSign(key);

				signature.update(serverCertificate.getEncoded());
				signature.update(serverNonce);

				signatureData.setSignature(ByteString.valueOf(signature.sign()));
				signatureData.setAlgorithm(securityPolicy
						.getAsymmetricSignatureAlgorithm().getUri());

			} catch (NoSuchAlgorithmException e) {
				throw new ServiceResultException(
						StatusCodes.Bad_SecurityChecksFailed,
						"Signature generation failed: " + e.getMessage());
			} catch (InvalidKeyException e) {
				// Server certificate does not have encrypt usage
				throw new ServiceResultException(
						StatusCodes.Bad_CertificateInvalid,
						"Server certificate in endpoint is invalid: "
								+ e.getMessage());
			} catch (SignatureException e) {
				throw new ServiceResultException(
						StatusCodes.Bad_SecurityChecksFailed,
						"Signature generation failed: " + e.getMessage());
			}
		return token;
	}
	
	@Override
	public void setPolicyId(String policyId) {
		this.policyId = policyId;
		if (this.token != null) {
			this.token.setPolicyId(policyId);
		}
	}

	/**
	 * Decrypts the Password using the EncryptionAlgorithm and places the result in
	 * DecryptedPassword
	 */
	@Override
	public byte[] decrypt(Cert certificate, byte[] sendernonce, String securityPolicyUri) {
		return new byte[0];
	}
}
