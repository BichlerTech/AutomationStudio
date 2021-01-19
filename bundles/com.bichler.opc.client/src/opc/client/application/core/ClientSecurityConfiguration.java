package opc.client.application.core;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.transport.security.KeyPair;

import opc.sdk.core.session.CertificateIdentifier;

/**
 * 
 * A client security configuration.
 * 
 * @author Thomas Z&ouml;chbauer
 * @since 23.05.2012, HB-Softsolution e.U.
 */
public class ClientSecurityConfiguration {
	// -application certificate
	private String storePath = null;
	private String commonName = null;
	private String privateKeyPassword = null;
	private String organisation = null;
	private String dc = null;
	private String nonceLength = null;
	private CertificateIdentifier applicationCertificate = null;
	private KeyPair applicationKeyPair = null;
	private List<CertificateIdentifier> trustedIssuerCertificates = null;
	private List<CertificateIdentifier> trustedPeerCertificates = null;

	/*
	 * default empty constructor to create a client security configuration from
	 * scratch
	 */
	public ClientSecurityConfiguration() {
		// do nothing
	}

	/**
	 * A Client�s Security Configuration.
	 * 
	 * @param privateKeyPassword
	 * 
	 * 
	 * @param StorePath
	 * 
	 * @param SubjectName
	 * @param Thumbprint
	 * @param RawData
	 * @param NonceLength
	 */
	public ClientSecurityConfiguration(String storePath, String subjectName, String privateKeyPassword,
			String nonceLength) {
		this.storePath = storePath;
		String[] subjectNamePropertie = subjectName.split(",");
		this.commonName = subjectNamePropertie[0].split("=")[1];
		this.organisation = subjectNamePropertie[1].split("=")[1];
		this.dc = subjectNamePropertie[2].split("=")[1];
		this.privateKeyPassword = privateKeyPassword;
		this.nonceLength = nonceLength;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}

	/**
	 * Returns the Application Certificate
	 * 
	 * @return ApplicationCertificate
	 */
	public KeyPair getApplicationKeyPair() {
		return this.applicationKeyPair;
	}

	/**
	 * Returns the Length from the Client Nonce.
	 * 
	 * @return NonceLength
	 */
	public int getNonceLength() {
		return new Integer(this.nonceLength);
	}

	/**
	 * Gets the password of the Cleint�s Private Key.
	 * 
	 * @return Password of the Private Key.
	 */
	public String getPassword() {
		return this.privateKeyPassword;
	}

	/**
	 * Gets the storepath of the CertificateStore.
	 * 
	 * @return StorePath
	 */
	public String getStorePath() {
		return this.storePath;
	}

	/**
	 * Gets the subjectname of the application certificate.
	 * 
	 * @return SubjectName
	 */
	public String getCommonName() {
		return this.commonName;
	}

	public String getOrganisation() {
		return this.organisation;
	}

	public String getDC() {
		return this.dc;
	}

	/**
	 * Application Certificate
	 * 
	 * @param ApplicationCertificate
	 */
	public void setApplicationCertificate(KeyPair keyPair) {
		this.applicationCertificate = new CertificateIdentifier(keyPair.getCertificate().getCertificate());
		this.applicationKeyPair = keyPair;
	}

	/**
	 * Validates the Client Security Configuration.
	 * 
	 * @throws ServiceResultException
	 */
	public void validate() throws ServiceResultException {
		if (this.applicationCertificate == null) {
			throw new ServiceResultException(StatusCodes.Bad_ConfigurationError,
					"Client Security Configuration is Invalid!");
		}
		this.trustedIssuerCertificates = createDefaultTrustList(this.trustedIssuerCertificates);
		this.trustedPeerCertificates = createDefaultTrustList(this.trustedPeerCertificates);
	}

	/**
	 * Creates an Default Certificate Trust List.
	 * 
	 * @param TrustList A List with Trusted Certificates.
	 * @return TrustedCertificates
	 */
	private List<CertificateIdentifier> createDefaultTrustList(List<CertificateIdentifier> trustList) {
		if (trustList != null && !trustList.isEmpty()) {
			return trustList;
		}
		return new ArrayList<>();
	}

	public void setPrivateKeyPassword(String passwd) {
		this.privateKeyPassword = passwd;
	}

	/**
	 * @deprecated Do not set NoneLength from outside.
	 * @param nonce
	 */
	@Deprecated
	public void setNonceLength(String nonce) {
		this.nonceLength = nonce;
	}
}
