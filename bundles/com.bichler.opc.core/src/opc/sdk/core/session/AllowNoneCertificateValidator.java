package opc.sdk.core.session;

import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.cert.CertificateStore;
import org.opcfoundation.ua.cert.DefaultCertificateValidator;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.transport.security.Cert;
import org.opcfoundation.ua.transport.security.SecurityPolicy;

public class AllowNoneCertificateValidator extends DefaultCertificateValidator {
	private boolean flagAllowNone = true;

	public AllowNoneCertificateValidator(CertificateStore certificateStore) {
		super(certificateStore);
	}

	public StatusCode validateCertificate(SecurityPolicy securityPolicy, ApplicationDescription applicationDescription,
			Cert cert) {
		if (getCertificateStore() == null) {
			return StatusCode.GOOD;
		}
		if (this.flagAllowNone && securityPolicy == SecurityPolicy.NONE) {
			return StatusCode.GOOD;
		}
		return validateCertificate(null, cert);
	}

	@Override
	public StatusCode validateCertificate(ApplicationDescription applicationDescription, Cert cert) {
		if (getCertificateStore() == null) {
			return StatusCode.GOOD;
		}
		return super.validateCertificate(applicationDescription, cert);
	}

	// @Override
	// public StatusCode validateCertificate(Cert c) {
	// if(getCertificateStore() == null) {
	// return StatusCode.GOOD;
	// }
	//
	// return super.validateCertificate(c);
	// }
	public void setAllowNone(boolean allowNone) {
		this.flagAllowNone = allowNone;
	}
}
