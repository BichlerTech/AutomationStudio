package opc.sdk.server.security;

/**
 * Path of the certificates.
 * 
 * @author Thomas Z&ouml;chbauer
 *
 */
public enum CertificatePath {
	privatekey, publiccert;

	public String getPath() {
		return this.toString();
	}
}
