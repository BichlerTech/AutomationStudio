package opc.sdk.core.application;

import org.opcfoundation.ua.transport.security.Cert;
import org.opcfoundation.ua.transport.security.PrivKey;

/**
 * SecurityCertAdministration
 * 
 * @author Thomas Z&ouml;bauer
 *
 */
public class SecurityCertificateAdministration {
	private Cert cert = null;
	private PrivKey privKey = null;

	public SecurityCertificateAdministration(Cert cert, PrivKey privKey) {
		this.cert = cert;
		this.privKey = privKey;
	}

	public Cert getCert() {
		return this.cert;
	}

	public PrivKey getPrivKey() {
		return this.privKey;
	}
}
