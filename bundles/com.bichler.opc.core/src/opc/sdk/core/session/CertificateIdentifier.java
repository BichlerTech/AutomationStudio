package opc.sdk.core.session;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.utils.CertificateUtils;

/**
 * Instance of an validatet certificate.
 * 
 * @author Thomas Z&ouml;bauer
 *
 */
public class CertificateIdentifier {
	private X509Certificate certificate = null;
	private CertificateValidationOptions validationOptions = null;

	/**
	 * The default constructor.
	 */
	public CertificateIdentifier() {
		initialize();
	}

	public CertificateIdentifier(byte[] rawData) {
		initialize();
		try {
			this.certificate = CertificateUtils.decodeX509Certificate(rawData);
		} catch (CertificateException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
		}
	}

	public CertificateIdentifier(X509Certificate certificate) {
		initialize();
		this.certificate = certificate;
	}

	public CertificateIdentifier(X509Certificate certificate, CertificateValidationOptions validationOptions) {
		initialize();
		this.certificate = certificate;
		this.setValidationOptions(validationOptions);
	}

	// Sets private members to default values
	private void initialize() {
		certificate = null;
		validationOptions = null;
	}

	/**
	 * Finds a certificate in a store.
	 * 
	 * @param needPrivateKey
	 * @return
	 */
	public X509Certificate find(boolean needPrivateKey) {
		// check if the entire certificate has been specified
		if (this.certificate != null && (!needPrivateKey)) {
			return certificate;
		}
		// open store
		return null;
	}

	public CertificateValidationOptions getValidationOptions() {
		return validationOptions;
	}

	public void setValidationOptions(CertificateValidationOptions validationOptions) {
		this.validationOptions = validationOptions;
	}
}
