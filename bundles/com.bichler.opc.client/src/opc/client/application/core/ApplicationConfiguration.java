package opc.client.application.core;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom.JDOMException;
import org.opcfoundation.ua.application.Application;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.ApplicationType;
import org.opcfoundation.ua.core.EndpointDescription;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.transport.security.AllowAllCertificatesValidator;
import org.opcfoundation.ua.transport.security.CertificateValidator;
import org.opcfoundation.ua.transport.security.KeyPair;
import org.opcfoundation.ua.transport.security.SecurityPolicy;

import opc.client.application.ClientConfiguration;
import opc.sdk.core.session.AllowNoneCertificateValidator;

/**
 * Application configuration of a client application.
 * 
 * @author Thomas Z&ouml;chbauer
 * @since 23.05.2012, HB-Softsolution e.U.
 * 
 */
public class ApplicationConfiguration {
	private Application application = null;
	/** Configuration File of the Application */
	private InputStream originConfigFile = null;
	/** Instance Settings */
	private String applicationName = null;
	private String applicationUri = null;
	private String productUri = null;
	private String applicationType = null;
	/** Security Configuratin */
	private ClientSecurityConfiguration clientSecurityConfig = null;
	private TransportQuotas transportQuotas = null;
	private ClientApplicationConfiguration clientConfiguration = null;
	private TraceConfiguration traceConfiguration = null;
	// validator for the certificates
	private CertificateValidator certificateValidator = null;

	/*
	 * default empty constructor to create an application configuration from scratch
	 */
	public ApplicationConfiguration() {
		// do nothing
	}

	public ApplicationConfiguration(InputStream configurationFile) throws JDOMException {
		this.originConfigFile = configurationFile;
		createApplicationConfigurationFromFile(this.originConfigFile);
	}

	/**
	 * Application Configuration for a Client.
	 * 
	 * @param ConfigFile
	 * @param ClientApplicationInstanceCertificate
	 * 
	 * @throws ServiceResultException
	 * @throws JDOMException
	 */
	public ApplicationConfiguration(InputStream configurationFile, KeyPair clientApplicationInstanceCertificate)
			throws ServiceResultException, JDOMException {
		this(configurationFile);
		setSecurityConfig(clientApplicationInstanceCertificate);
	}

	public void setClientSecurityConfig(ClientSecurityConfiguration clientSecurityConfig) {
		this.clientSecurityConfig = clientSecurityConfig;
	}

	public TransportQuotas getTransportQuotas() {
		return this.transportQuotas;
	}

	public InputStream getClientConfigurationFile() {
		return this.originConfigFile;
	}

	public void validate(ApplicationType applicationType) throws ServiceResultException {
		if (this.applicationName == null || this.applicationName.isEmpty()) {
			throw new ServiceResultException(StatusCodes.Bad_ConfigurationError, "Application Name is NULL!");
		}
		if (this.clientSecurityConfig == null) {
			throw new ServiceResultException(StatusCodes.Bad_ConfigurationError,
					"No Security Configuration has been initialized!");
		}
		this.clientSecurityConfig.validate();
		// ensure application uri matches the certificate.
		if (this.applicationUri.isEmpty()) {
			StringBuilder builder = new StringBuilder();
			builder.append("urn:");
			try {
				builder.append(InetAddress.getLocalHost().getHostName());
			} catch (UnknownHostException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				throw new ServiceResultException(StatusCodes.Bad_ConfigurationError,
						"Cannot get the Hostname from the Local Machine!");
			}
			builder.append(":");
			builder.append(this.applicationName);
			this.applicationUri = builder.toString();
		}
		if (applicationType.equals(ApplicationType.Client) || applicationType.equals(ApplicationType.ClientAndServer)) {
			if (this.clientConfiguration == null) {
				throw new ServiceResultException(StatusCodes.Bad_ConfigurationError, "ClientConfiguration is NULL!");
			}
		}
		if (applicationType.equals(ApplicationType.DiscoveryServer)) {
			// validate discovery server configuration
		}
		this.certificateValidator = new AllowNoneCertificateValidator(null);
	}

	public CertificateValidator getCertificateValidator() {
		return this.certificateValidator;
	}

	public int getNonceLength(EndpointDescription endpoint) throws ServiceResultException {
		SecurityPolicy securityPolicy = SecurityPolicy.getSecurityPolicy(endpoint.getSecurityPolicyUri());
		// return securityPolicy.getSecureChannelNonceLength();
		return 32;
	}

	public LocalizedText getApplicationName() {
		return new LocalizedText(this.applicationName, Locale.getDefault());
	}

	public String getApplicationUri() {
		return this.applicationUri;
	}

	public String getProductUri() {
		return this.productUri;
	}

	public double getDefaultSessionTimeout() {
		return this.clientConfiguration.getDefaultSessionTimeout();
	}

	public void setDefaultSessionTimeout(double timeout) {
		this.clientConfiguration.setDefaultSessionTimeout(timeout);
	}

	public ClientApplicationConfiguration getClientConfiguaration() {
		return this.clientConfiguration;
	}

	public ClientSecurityConfiguration getSecurityConfiguration() {
		return this.clientSecurityConfig;
	}

	public void setSecurityConfig(KeyPair keyPair) throws ServiceResultException {
		this.clientSecurityConfig.setApplicationCertificate(keyPair);
		validate(ApplicationType.Client);
	}

	public String getApplicationType() {
		return this.applicationType;
	}

	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}

	public TraceConfiguration getTraceConfiguration() {
		return traceConfiguration;
	}

	public void setTraceConfiguration(TraceConfiguration traceConfiguration) {
		this.traceConfiguration = traceConfiguration;
	}

	public void setCertificateValidator(CertificateValidator certValidator) {
		this.certificateValidator = certValidator;
	}

	/**
	 * Reads the variables from the file
	 * 
	 * @param ConfigFile
	 * @throws JDOMException
	 */
	private void createApplicationConfigurationFromFile(InputStream configFile) throws JDOMException {
		ApplicationConfigurationReader xmlReader = new ApplicationConfigurationReader(configFile);
		this.applicationName = xmlReader.getApplicationName();
		this.applicationUri = xmlReader.getApplicationUri();
		this.productUri = xmlReader.getProductUri();
		this.setApplicationType(xmlReader.getApplicationType());
		this.clientSecurityConfig = xmlReader.getSecurityConfiguration();
		this.transportQuotas = xmlReader.getTransportQuotas();
		this.clientConfiguration = xmlReader.getClientConfiguration();
		this.setTraceConfiguration(xmlReader.getTraceConfiguration());
		setClientConfiguration();
	}

	private void setClientConfiguration() {
		if (!ClientConfiguration.getCertificatePath().equals(this.getSecurityConfiguration().getStorePath())) {
			ClientConfiguration.setCertificatePath(this.getSecurityConfiguration().getStorePath());
		}
	}

	public void setApplicationName(String name) {
		this.applicationName = name;
	}

	public void setApplicationUri(String uri) {
		this.applicationUri = uri;
	}

	public void setProductUri(String uri) {
		this.productUri = uri;
	}

	public void setTransportQuotas(TransportQuotas transp) {
		this.transportQuotas = transp;
	}

	public void setClientApplicationConfiguration(ClientApplicationConfiguration app) {
		this.clientConfiguration = app;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}
}
