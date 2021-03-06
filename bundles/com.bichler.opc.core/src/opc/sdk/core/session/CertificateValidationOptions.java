package opc.sdk.core.session;

/**
 * Enum for the validatet certificates.
 * 
 * @author Thomas Z&ouml;bauer
 *
 */
public enum CertificateValidationOptions {
	/**
	 * Use the default options.
	 */
	DEFAULT,
	/**
	 * Ingore expired certificates
	 */
	SUPPRESSCERTIFICATEEXPIRED,
	/**
	 * Ignore mismatches between the URL and the DNS names in the certificate
	 */
	SUPPRESSHOSTNAMEINVALID,
	/**
	 * Ignore
	 */
	SUPPRESSUSENOTALLOWED,
	/**
	 * Ignore errors when it is not possible to check the revocation status for a
	 * certificate
	 */
	SUPPRESSREVOCATIONSTATUSUNKNOWN,
	/**
	 * Attempt to check the revocation status online
	 */
	CHECKREVOCATIONSTATUSONLINE,
	/**
	 * Attempt to check the revocation status offline
	 */
	CHECKREVOCATIONSTATUSOFFLINE,
	/**
	 * Never trust the certificate
	 */
	TREATASINVALID;
}
