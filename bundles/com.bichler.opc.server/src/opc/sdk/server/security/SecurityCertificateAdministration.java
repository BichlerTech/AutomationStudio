package opc.sdk.server.security;

import org.opcfoundation.ua.transport.security.Cert;
import org.opcfoundation.ua.transport.security.KeyPair;
import org.opcfoundation.ua.transport.security.PrivKey;

/**
 * Set of the private key and the public cert.
 * 
 * @author Thomas Z&ouml;chbauer
 *
 */
public class SecurityCertificateAdministration {
	private Cert cert = null;
	private PrivKey privKey = null;
	private KeyPair resultKeyPair = null;

	public SecurityCertificateAdministration(Cert cert, PrivKey privKey) {
		this.cert = cert;
		this.privKey = privKey;
	}

	public SecurityCertificateAdministration(KeyPair result) {
		setResultKeyPair(result);
	}

	public Cert getCert() {
		return this.cert;
	}

	public PrivKey getPrivKey() {
		return this.privKey;
	}

	public KeyPair getResultKeyPair() {
		return resultKeyPair;
	}

	public void setResultKeyPair(KeyPair resultKeyPair) {
		this.resultKeyPair = resultKeyPair;
	}
}
