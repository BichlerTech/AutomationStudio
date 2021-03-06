package opc.sdk.core.application;

import org.jdom.Content;

/**
 * The configuration of the security to use.
 * 
 * @author Thomas Z&ouml;bauer
 * 
 */
public class SecurityConfiguration {
	private ApplicationCertificate applicationCertificate = null;
	private TrustedPeerCertificates trustedPeerCertificates = null;
	// private int nonceLength = 0;

	public SecurityConfiguration() {
		// this.nonceLength = 32;
	}

	public void setApplicationCertificate(ApplicationCertificate applicationCertificate) {
		this.applicationCertificate = applicationCertificate;
	}

	public TrustedPeerCertificates getTrustedPeerCertificates() {
		return trustedPeerCertificates;
	}

	public void setTrustedPeerCertificates(TrustedPeerCertificates trustedPeerCertificates) {
		this.trustedPeerCertificates = trustedPeerCertificates;
	}

	public void createAppCert(Content content) {
		this.applicationCertificate = new ApplicationCertificate(content);
	}

	public void createTrustedPeerCert(Content content) {
		this.trustedPeerCertificates = new TrustedPeerCertificates(content);
	}

	public ApplicationCertificate getApplicationCertificate() {
		return this.applicationCertificate;
	}

	public String getApplicationCertificateStorePath() {
		return this.applicationCertificate.getStorePath();
	}

	public String getTrustedPeerCertificateStorePath() {
		return this.trustedPeerCertificates.getStorePath();
	}

	public String getApplicationCertificateKeyName() {
		return this.applicationCertificate.getCertKeyName();
	}
	// public int getMinNonceLength() {
	// return this.nonceLength;
	// }
}
