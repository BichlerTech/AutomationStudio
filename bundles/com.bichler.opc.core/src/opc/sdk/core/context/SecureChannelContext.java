package opc.sdk.core.context;

import opc.sdk.core.enums.RequestEncoding;

import org.opcfoundation.ua.core.EndpointDescription;
import org.opcfoundation.ua.transport.SecureChannel;
import org.opcfoundation.ua.transport.security.CertificateValidator;
import org.opcfoundation.ua.transport.security.SecurityPolicy;

/**
 * SecureChannelContext from a {@link SecureChannel}. This is a Helper class
 * that supports access to internal References.
 * 
 * @author Thomas Z&ouml;bauer
 */
public class SecureChannelContext {
	private String secureChannelId = null;
	private EndpointDescription endpointDescription = null;
	private RequestEncoding messageEncoding = null;
	private SecurityPolicy securityPolicy;
	private CertificateValidator certificateValidator = null;

	public SecureChannelContext(String secureChannelId, EndpointDescription endpointDescription,
			RequestEncoding messageEncoding, SecurityPolicy securityPolicy) {
		this.secureChannelId = secureChannelId;
		this.endpointDescription = endpointDescription;
		this.messageEncoding = messageEncoding;
		this.securityPolicy = securityPolicy;
	}

	/**
	 * Initializes a new instance with the specified property values.
	 * 
	 * @param securityPolicy
	 * @param certificateValidator
	 */
	public SecureChannelContext(String secureChannelId, EndpointDescription endpointDescription,
			RequestEncoding messageEncoding, SecurityPolicy securityPolicy, CertificateValidator certificateValidator) {
		this(secureChannelId, endpointDescription, messageEncoding, securityPolicy);
		this.certificateValidator = certificateValidator;
	}

	/**
	 * The unique identifier for the secure channel.
	 * 
	 * @return secureChannelId The secure channel identifier
	 */
	public String getSecureChannelId() {
		return this.secureChannelId;
	}

	/**
	 * The description of the endpoint used with the channel.
	 * 
	 * @return endpointDescription The endpoint description.
	 */
	public EndpointDescription getEndpointDescription() {
		return endpointDescription;
	}

	/**
	 * The encoding used with the channel.
	 * 
	 * @retur messageEncoding The message encoding.
	 */
	public RequestEncoding getMessageEncoding() {
		return messageEncoding;
	}

	/**
	 * SecurityPolicy of the SecureChannel from the Service.
	 * 
	 * @return SecurityPolicy
	 */
	public SecurityPolicy getSecurityPolicy() {
		return this.securityPolicy;
	}

	/**
	 * CertificateValidator of the server.
	 * 
	 * @return CertificateValidator
	 */
	public CertificateValidator getCertificateValidator() {
		return this.certificateValidator;
	}
}
