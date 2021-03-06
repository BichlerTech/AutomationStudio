package opc.sdk.server.security;

import opc.sdk.core.application.ServerSecurityPolicy;

import org.opcfoundation.ua.application.Application;
import org.opcfoundation.ua.transport.https.HttpsSettings;
import org.opcfoundation.ua.transport.security.CertificateValidator;
import org.opcfoundation.ua.transport.tcp.io.OpcTcpSettings;

public class OPCSecurityConfigurator {
	/** server application instance */
	private Application applicaton = null;
	private ServerSecurityPolicy[] securityPolicies;
	private String[] addresses;
	private String certName = null;
	private String certStorePath = null;

	public OPCSecurityConfigurator(Application application) {
		this.applicaton = application;
		initDefault();
	}

	private void initDefault() {
		this.certName = "BTech";
	}

	public String getCertName() {
		return certName;
	}

	public String getCertStorePath() {
		return certStorePath;
	}

	/**
	 * Certificate validator for opc.tcp connections
	 * 
	 * @param validator
	 */
	public void setOPCTCPCertificateValidator(CertificateValidator validator) {
		OpcTcpSettings settings = this.applicaton.getOpctcpSettings();
		settings.setCertificateValidator(validator);
	}

	/**
	 * Certificate validator for https connections
	 * 
	 * @param validator
	 */
	public void setHttpsCertificateValidator(CertificateValidator validator) {
		HttpsSettings settings = this.applicaton.getHttpsSettings();
		settings.setCertificateValidator(validator);
	}

	public void setSecurityPolicies(ServerSecurityPolicy[] securityPolicies) {
		this.securityPolicies = securityPolicies;
	}

	public String[] getServerAddresses() {
		return this.addresses;
	}

	/**
	 * Server addresses
	 * 
	 * @param addresses
	 */
	public void setServerAddress(String[] addresses) {
		this.addresses = addresses;
	}

	public ServerSecurityPolicy[] getSecurityPolicies() {
		return this.securityPolicies;
	}

	public void setCertificateName(String applicationCertificateKeyName) {
		this.certName = applicationCertificateKeyName;
	}

	public void setInternCertStorePath(String applicationCertificateStorePath) {
		this.certStorePath = applicationCertificateStorePath;
	}
}
