package opc.sdk.core.session;

import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.EndpointDescription;
import org.opcfoundation.ua.core.IssuedIdentityToken;
import org.opcfoundation.ua.core.SignatureData;
import org.opcfoundation.ua.core.UserIdentityToken;
import org.opcfoundation.ua.core.UserTokenType;
import org.opcfoundation.ua.transport.security.Cert;
import org.opcfoundation.ua.transport.security.PrivKey;

/**
 * An interface to an object with stores the identity of an user to activate a
 * session.
 * 
 * @author Thomas Z&ouml;bauer
 */
public interface UserIdentity {
	/**
	 * A display name that identifies the user.
	 * 
	 * @return The display name.
	 */
	String getDisplayName();

	/**
	 * Getter of the Type of the Identity Token.
	 * 
	 * @return UserTokenType
	 */
	UserTokenType getTokenType();

	/**
	 * The type of issued token.
	 * 
	 * @return The type of the issued token.
	 */
	IssuedIdentityToken getIssuedTokenType();

	/**
	 * Whether the object can create signatures to prove possession of the user's
	 * credentials.
	 * 
	 * @return TRUE, if signatures are supported, otherwise FALSE;
	 */
	boolean getSupportsSignatures();

	/**
	 * Returns a UA User Identity token containing the user information.
	 * 
	 * @return UserIdentityToken
	 */
	UserIdentityToken getIdentityToken();

	/**
	 * Sets the user policy token id.
	 * 
	 * @param policyId
	 */
	void setPolicyId(String policyId);

	/**
	 * Creates a signature with the token.
	 * 
	 * @param DataToSign
	 * @param SecurityPolicyUri
	 * @throws ServiceResultException
	 */
	SignatureData sign(byte[] dataToSign, String securityPolicyUri) throws ServiceResultException;

	void encrypt(SignatureData userTokenSignature, Cert certificate, PrivKey privKey,
			EndpointDescription endpointDescription, byte[] serverNonce, String securityPolicyUri)
			throws ServiceResultException;

	byte[] decrypt(Cert certificate, byte[] sendernonce, String securityPolicyUri);
}
