package opc.sdk.core.application;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.cert.DefaultCertificateValidator;
import org.opcfoundation.ua.core.ApplicationType;
import org.opcfoundation.ua.core.EndpointDescription;
import org.opcfoundation.ua.core.SignatureData;
import org.opcfoundation.ua.core.SignedSoftwareCertificate;
import org.opcfoundation.ua.core.UserTokenPolicy;
import org.opcfoundation.ua.transport.security.AllowAllCertificatesValidator;
import org.opcfoundation.ua.transport.security.CertificateValidator;

import opc.sdk.core.session.AllowNoneCertificateValidator;
import opc.sdk.core.utils.OPCDateFormat;

/**
 * The application configuration for an opc server.
 * 
 * @author Thomas Z&ouml;chbauer
 * 
 */
public class ApplicationConfiguration {
	/** The XML namespaces of the ApplicationConfiguration XML file. */
	private List<Namespace> xmlNamespaces = null;
	/** The XML namespaces of the ApplicationConfiguration XML file. */
	private DateTime buildDate = null;
	private String buildNumber = "";
	private String manufacturerName = "";
	private String softwareVersion = "";
	/** The configured application name of the server. */
	private String productName = "";
	/** The configured application uri of the server. */
	private String applicationUri = "";
	/** The product uri of the server. */
	private String productUri = "";
	/** The application type of the application (server, client, both). */
	private String applicationType = "";
	/** The application certificates validation time */
	private String certificateValidity = "";
	/** The configuration of the security information of the server. */
	private SecurityConfiguration securityConfiguration = null;
	/** The transport configuration. */
	/** The configuration information of the server. */
	private ServerConfiguration serverConfiguration = null;
	/** The server max operation timeout. */
	private String operationTimeout = "";
	/** The max string length of a transport message. */
	private String maxStringLength = "";
	/** The max byte string length. */
	private String maxByteStringLength = "";
	/** The max array length. */
	private String maxArrayLength = "";
	/** The max message size. */
	private String maxMessageSize = "";
	/** The max buffer size. */
	private String maxBufferSize = "";
	/** The channel lifetime. */
	private String channelLifetime = "";
	/** The security token lifetime. */
	private String securityTokenLifetime = "";
	/** The security certificate administration. */
	private SecurityCertificateAdministration securityCertificateAdministration = null;
	/** The endpoints. */
	private AllowNoneCertificateValidator certificateValidator = null;
	private CertificateValidator userAuthCertStore;
	/** The name of the application */
	private String applicationName = "";
	private List<String> informationModels = new ArrayList<>();
	private HistoryConfiguration historyConfig;
	private List<String> nodeSets = new ArrayList<>();
	/** The trace output file path. */
	private String traceOutputFilePath = "";
	/** The trace delete on load. */
	private String traceDeleteOnLoad = "";
	/** The trace masks. */
	private String traceMasks = "";

	/**
	 * Reads a server configuration from XML or TXT-File.
	 * 
	 * @param file the file
	 * @throws IOException
	 * @throws JDOMException
	 */
	public ApplicationConfiguration(InputStream file) throws IOException {
		initialize(file);
		this.certificateValidator = new AllowNoneCertificateValidator(null);
	}

	public List<Namespace> getXmlNamespaces() {
		return xmlNamespaces;
	}

	public void setXmlNamespaces(List<Namespace> xmlNamespaces) {
		this.xmlNamespaces = xmlNamespaces;
	}

	/**
	 * Creates an empty application Configuration.
	 *
	 */
	public ApplicationConfiguration() {
	}

	/**
	 * Initializethe application configuration, security configuration, transport
	 * configuration and server configuration.
	 * 
	 * @param serverConfigFile the server config file
	 * @throws IOException
	 * @throws JDOMException
	 */
	@SuppressWarnings("unchecked")
	private void initialize(InputStream serverConfigFile) throws IOException {
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		try {
			doc = builder.build(serverConfigFile);
		} catch (JDOMException e) {
			throw new IOException(e.getCause());
		}
		Element root = doc.getRootElement();
		if (root.getName().equalsIgnoreCase(ApplicationConfigurationTags.ApplicationConfiguration.name())) {
			this.xmlNamespaces = (List<Namespace>) root.getAdditionalNamespaces();
			this.securityConfiguration = new SecurityConfiguration();
			this.serverConfiguration = new ServerConfiguration();
			this.historyConfig = new HistoryConfiguration();
			setApplicationConfiguration(root);
		}
	}

	/**
	 * Read all application configuration tags from the file and fills in.
	 * 
	 * @param element the new server configuration tag
	 */
	@SuppressWarnings("unchecked")
	private void setApplicationConfiguration(Element element) {
		for (Object objElement : element.getChildren()) {
			Element el = ((Element) objElement);
			if (el.getName().equalsIgnoreCase(ApplicationConfigurationTags.ApplicationName.name())) {
				setApplicationName(el.getContent());
			} else if (el.getName().equalsIgnoreCase(ApplicationConfigurationTags.BuildDate.name())) {
				setBuildDate(el.getContent());
			} else if (el.getName().equalsIgnoreCase(ApplicationConfigurationTags.ProductName.name())) {
				setProductName(el.getContent());
			} else if (el.getName().equalsIgnoreCase(ApplicationConfigurationTags.CertificateValidity.name())) {
				setCertificateValidation(el.getContent());
			} else if (el.getName().equalsIgnoreCase(ApplicationConfigurationTags.BUILDNumber.name())) {
				setBuildNumber(el.getContent());
			} else if (el.getName().equalsIgnoreCase(ApplicationConfigurationTags.ManufacturerName.name())) {
				setManufacturerName(el.getContent());
			} else if (el.getName().equalsIgnoreCase(ApplicationConfigurationTags.SoftwareVersion.name())) {
				setSoftwareVersion(el.getContent());
			} else if (el.getName().equalsIgnoreCase(ApplicationConfigurationTags.ApplicationUri.name())) {
				setApplicationUri(el.getContent());
			} else if (el.getName().equalsIgnoreCase(ApplicationConfigurationTags.ProductUri.name())) {
				setProductUri(el.getContent());
			} else if (el.getName().equalsIgnoreCase(ApplicationConfigurationTags.ApplicationType.name())) {
				setApplicatonType(el.getContent());
			} else if (el.getName().equalsIgnoreCase(ApplicationConfigurationTags.SecurityConfiguration.name())) {
				setSecurityConfiguration(el.getContent());
			} else if (el.getName().equalsIgnoreCase(ApplicationConfigurationTags.TransportQuotas.name())) {
				setTransportQuotas(el.getContent());
			} else if (el.getName().equalsIgnoreCase(ApplicationConfigurationTags.ServerConfiguration.name())) {
				setServerConfiguration(el.getContent());
			} else if (el.getName().equalsIgnoreCase(ApplicationConfigurationTags.InformationModels.name())) {
				setInformationModel(el.getContent());
			} else if (el.getName().equalsIgnoreCase(ApplicationConfigurationTags.Extensions.name())) {
				setExtensions(el.getContent());
			} else if (el.getName().equalsIgnoreCase(ApplicationConfigurationTags.TraceConfiguration.name())) {
				setTraceConfiguration(el.getContent());
			}
		}
	}

	// TODO: EXTENSIONMODEL How to react on extension objects?
	/**
	 * Sets the extensions.
	 * 
	 * @param content the new extensions
	 */
	private void setExtensions(List<Content> content) {
	}

	/**
	 * Read all trace configuration tags from the file and fills in.
	 * 
	 * @param content the new trace configuration
	 */
	private void setTraceConfiguration(List<Content> content) {
		for (Content c : content) {
			if (c instanceof Element) {
				if (((Element) c).getName().equals(ApplicationConfigurationTags.OutputFilePath.name())) {
					this.traceOutputFilePath = c.getValue();
				} else if (((Element) c).getName().equals(ApplicationConfigurationTags.DeleteOnLoad.name())) {
					this.traceDeleteOnLoad = c.getValue();
				} else if (((Element) c).getName().equals(ApplicationConfigurationTags.TraceMasks.name())) {
					this.traceMasks = c.getValue();
				}
			}
		}
	}

	public void setHistoryConfig(HistoryConfiguration config) {
		this.historyConfig = config;
	}

	/**
	 * Stores the content of the history configuration.
	 * 
	 * @param content
	 */
	private void setHistoryConfig(List<Content> content) {
		for (Content c : content) {
			if (c instanceof Element) {
				if (((Element) c).getName().equals(ApplicationConfigurationTags.Active.name())) {
					this.historyConfig.setActive((Element) c);
				} else if (((Element) c).getName().equals(ApplicationConfigurationTags.DriverName.name())) {
					this.historyConfig.setDrvName((Element) c);
				} else if (((Element) c).getName().equals(ApplicationConfigurationTags.DBUrl.name())) {
					this.historyConfig.setDatabase((Element) c);
				} else if (((Element) c).getName().equals(ApplicationConfigurationTags.DBName.name())) {
					this.historyConfig.setDatabaseName((Element) c);
				} else if (((Element) c).getName().equals(ApplicationConfigurationTags.User.name())) {
					this.historyConfig.setUser((Element) c);
				} else if (((Element) c).getName().equals(ApplicationConfigurationTags.Pw.name())) {
					this.historyConfig.setPw((Element) c);
				}
			}
		}
	}

	/**
	 * Read all server configuration tags from the file and fills in.
	 * 
	 * @param content the new server configuration
	 */
	private void setServerConfiguration(List<Content> content) {
		for (Content c : content) {
			if (c instanceof Element) {
				if (((Element) c).getName().equals(ServerConfigurationTags.BaseAddresses.getName())) {
					this.serverConfiguration.setEndpoints((Element) c);
				} else if (((Element) c).getName().equals(ServerConfigurationTags.SecurityPolicies.getName())) {
					this.serverConfiguration.setSecurityPolicies((Element) c);
				} else if (((Element) c).getName().equals(ServerConfigurationTags.UserTokenPolicies.getName())) {
					this.serverConfiguration.setUserTokenPolicies((Element) c);
				} else if (((Element) c).getName().equals(ServerConfigurationTags.DiagnosticsEnabled.getName())) {
					this.serverConfiguration.setDiagnosticsEnabled(((Element) c));
				} else if (((Element) c).getName().equals(ServerConfigurationTags.MaxSessionCount.getName())) {
					this.serverConfiguration.setMaxSessionCount(((Element) c));
				} else if (((Element) c).getName().equals(ServerConfigurationTags.MinSessionTimeout.getName())) {
					this.serverConfiguration.setMinSessionTimeout(((Element) c));
				} else if (((Element) c).getName().equals(ServerConfigurationTags.MaxSessionTimeout.getName())) {
					this.serverConfiguration.setMaxSessionTimeout(((Element) c));
				} else if (((Element) c).getName().equals(ServerConfigurationTags.MaxSubscriptionCount.getName())) {
					this.serverConfiguration.setMaxSubscriptionCount(((Element) c));
				} else if (((Element) c).getName()
						.equals(ServerConfigurationTags.MaxBrowseContinuationPoints.getName())) {
					this.serverConfiguration.setMaxBrowseContinuationPoints(((Element) c));
				} else if (((Element) c).getName()
						.equals(ServerConfigurationTags.MaxQueryContinuationPoints.getName())) {
					this.serverConfiguration.setMaxQueryContinuationPoints(((Element) c));
				} else if (((Element) c).getName()
						.equals(ServerConfigurationTags.MaxHistoryContinuationPoints.getName())) {
					this.serverConfiguration.setMaxHistoryContinuationPoints(((Element) c));
				} else if (((Element) c).getName().equals(ServerConfigurationTags.MaxRequestAge.getName())) {
					this.serverConfiguration.setMaxRequestAge(((Element) c));
				} else if (((Element) c).getName().equals(ServerConfigurationTags.MinPublishingInterval.getName())) {
					this.serverConfiguration.setMinPublishingInterval(((Element) c));
				} else if (((Element) c).getName().equals(ServerConfigurationTags.MaxPublishingInterval.getName())) {
					this.serverConfiguration.setMaxPublishingInterval(((Element) c));
				} else if (((Element) c).getName().equals(ServerConfigurationTags.PublishingResolution.getName())) {
					this.serverConfiguration.setPublishingResolution(((Element) c));
				} else if (((Element) c).getName().equals(ServerConfigurationTags.MaxSubscriptionLifetime.getName())) {
					this.serverConfiguration.setMaxSubscriptionLifetime(((Element) c));
				} else if (((Element) c).getName().equals(ServerConfigurationTags.MinSubscriptionLifetime.getName())) {
					this.serverConfiguration.setMinSubscriptionLifetime(((Element) c));
				} else if (((Element) c).getName().equals(ServerConfigurationTags.MaxMessageQueueSize.getName())) {
					this.serverConfiguration.setMaxMessageQueueSize(((Element) c));
				} else if (((Element) c).getName().equals(ServerConfigurationTags.MaxNotificationQueueSize.getName())) {
					this.serverConfiguration.setMaxNotificationQueueSize(((Element) c));
				} else if (((Element) c).getName()
						.equals(ServerConfigurationTags.MaxNotificationsPerPublish.getName())) {
					this.serverConfiguration.setMaxNotificationsPerPublish(((Element) c));
				} else if (((Element) c).getName()
						.equals(ServerConfigurationTags.MinMetadataSamplingInterval.getName())) {
					this.serverConfiguration.setMinMetadatasamplingInterval(((Element) c));
				} else if (((Element) c).getName().equals(ServerConfigurationTags.UseCertificateStore.getName())) {
					this.serverConfiguration.setUseServerCertificateStore(((Element) c));
				}
				/***/
				else if (((Element) c).getName().equals(ServerConfigurationTags.AvailableSamplingRates.getName())) {
					this.serverConfiguration.setAvailableSamplingRates(((Element) c));
				} else if (((Element) c).getName().equals(ServerConfigurationTags.MaxRegistrationInterval.getName())) {
					this.serverConfiguration.setMaxRegistrationInterval(((Element) c));
				} else if (((Element) c).getName().equals(ApplicationConfigurationTags.HistoryConfiguration.name())) {
					setHistoryConfig(((Element) c).getContent());
				}
			}
		}
	}

	private void setInformationModel(List<Element> content) {
		for (Content e : content) {
			if (e instanceof Element) {
				if (((Element) e).getContentSize() == 0) {
					return;
				}
				String model = ((Element) e).getContent(((Element) e).getContentSize() - 1).getValue();
				informationModels.add(model);
			}
		}
	}

	/**
	 * Read all transport quotas tags from the file and fills in
	 * 
	 * @param content the new transport quotas
	 */
	private void setTransportQuotas(List<Content> content) {
		for (Content c : content) {
			if (c instanceof Element) {
				if (((Element) c).getName().equals(ApplicationConfigurationTags.OperationTimeout.name())) {
					if (((Element) c).getContentSize() == 0) {
						this.operationTimeout = "";
					} else {
						this.operationTimeout = ((Content) ((Element) c).getContent().get(0)).getValue();
					}
				} else if (((Element) c).getName().equals(ApplicationConfigurationTags.MaxStringLength.name())) {
					if (((Element) c).getContentSize() == 0) {
						this.maxStringLength = "";
					} else {
						this.maxStringLength = ((Content) ((Element) c).getContent().get(0)).getValue();
					}
				} else if (((Element) c).getName().equals(ApplicationConfigurationTags.MaxByteStringLength.name())) {
					if (((Element) c).getContentSize() == 0) {
						this.maxByteStringLength = "";
					} else {
						this.maxByteStringLength = ((Content) ((Element) c).getContent().get(0)).getValue();
					}
				} else if (((Element) c).getName().equals(ApplicationConfigurationTags.MaxArrayLength.name())) {
					if (((Element) c).getContentSize() == 0) {
						this.maxArrayLength = "";
					} else {
						this.maxArrayLength = ((Content) ((Element) c).getContent().get(0)).getValue();
					}
				} else if (((Element) c).getName().equals(ApplicationConfigurationTags.MaxMessageSize.name())) {
					if (((Element) c).getContentSize() == 0) {
						this.maxMessageSize = "";
					} else {
						this.maxMessageSize = ((Content) ((Element) c).getContent().get(0)).getValue();
					}
				} else if (((Element) c).getName().equals(ApplicationConfigurationTags.MaxBufferSize.name())) {
					if (((Element) c).getContentSize() == 0) {
						this.maxBufferSize = "";
					} else {
						this.maxBufferSize = ((Content) ((Element) c).getContent().get(0)).getValue();
					}
				} else if (((Element) c).getName().equals(ApplicationConfigurationTags.ChannelLifetime.name())) {
					if (((Element) c).getContentSize() == 0) {
						this.channelLifetime = "";
					} else {
						this.channelLifetime = ((Content) ((Element) c).getContent().get(0)).getValue();
					}
				} else if (((Element) c).getName().equals(ApplicationConfigurationTags.SecurityTokenLifetime.name())) {
					if (((Element) c).getContentSize() == 0) {
						this.securityTokenLifetime = "";
					} else {
						this.securityTokenLifetime = ((Content) ((Element) c).getContent().get(0)).getValue();
					}
				}
			}
		}
	}

	/**
	 * Returns the history configuration.
	 */
	public HistoryConfiguration getHistoryConfiguration() {
		return this.historyConfig;
	}

	/**
	 * Gets the user token policies.
	 * 
	 * @return the user token policies
	 */
	public List<UserTokenPolicy> getUserTokenPolicies() {
		return this.serverConfiguration.getUserTokenPolicies();
	}

	/**
	 * Sets the applicaton type from the XML file.
	 * 
	 * @param content the new applicaton type
	 */
	private void setApplicatonType(List<Content> content) {
		for (Content c : content) {
			String type = c.getValue();
			setApplicationType(type);
		}
	}

	/**
	 * Gets the endpoints.
	 * 
	 * @return the endpoints
	 */
	public List<String> getEndpoints() {
		return this.serverConfiguration.getEndpoints();
	}

	/**
	 * Gets the application certificate store path.
	 * 
	 * @return the application certificate store path
	 */
	public String getApplicationCertificateStorePath() {
		return this.securityConfiguration.getApplicationCertificateStorePath();
	}

	public String getApplicationCertificateKeyName() {
		return this.securityConfiguration.getApplicationCertificateKeyName();
	}

	/**
	 * Gets the max request message size.
	 * 
	 * @return the max request message size
	 */
	public UnsignedInteger getMaxRequestMessageSize() {
		return new UnsignedInteger(this.maxMessageSize);
	}

	/**
	 * Gets the security configuration.
	 * 
	 * @return the security configuration
	 */
	public SecurityConfiguration getSecurityConfiguration() {
		return this.securityConfiguration;
	}

	/**
	 * Gets the session timeout.
	 * 
	 * @return the session timeout
	 */
	public Double getSessionTimeout() {
		return new Double(this.serverConfiguration.getSessionTimeout());
	}

	/**
	 * Gets the server certificate.
	 * 
	 * @return the server certificate
	 */
	public byte[] getServerCertificate() {
		return this.securityCertificateAdministration.getCert().encodedCertificate;
	}

	/**
	 * Gets the server configuration.
	 * 
	 * @return the server configuration
	 */
	public ServerConfiguration getServerConfiguration() {
		return this.serverConfiguration;
	}

	/**
	 * Gets the server endpoint description.
	 * 
	 * @return the server endpoint description
	 */
	public EndpointDescription[] getServerEndpointDescription() {
		return new EndpointDescription[0];
	}

	/**
	 * Gets the server nonce.
	 * 
	 * @return the server nonce
	 */
	public byte[] getServerNonce() {
		return new byte[0];
	}

	/**
	 * Gets the server signature.
	 * 
	 * @return the server signature
	 */
	public SignatureData getServerSignature() {
		return null;
	}

	/**
	 * Gets the server software certificates.
	 * 
	 * @return the server software certificates
	 */
	public SignedSoftwareCertificate[] getServerSoftwareCertificates() {
		return new SignedSoftwareCertificate[0];
	}

	/**
	 * Sets the security cert adminsistration.
	 * 
	 * @param securityCertAdmin the new security cert adminsistration
	 */
	public void setSecurityCertAdminsistration(SecurityCertificateAdministration securityCertAdmin) {
		this.securityCertificateAdministration = securityCertAdmin;
	}

	/**
	 * Sets the product name from the XML file.
	 * 
	 * @param content the new product name
	 */
	private void setCertificateValidation(List<Content> content) {
		for (Content c : content) {
			this.certificateValidity = c.getValue();
		}
	}

	/**
	 * Gets the security cert administration.
	 * 
	 * @return the security cert administration
	 */
	public SecurityCertificateAdministration getSecurityCertAdministration() {
		return this.securityCertificateAdministration;
	}

	/**
	 * Gets the security policy.
	 * 
	 * @return the security policy
	 */
	public List<ServerSecurityPolicy> getSecurityPolicy() {
		return this.getServerConfiguration().getSecurityPolicies();
	}

	/**
	 * Gets the application uri.
	 * 
	 * @return the application uri
	 */
	public String getApplicationUri() {
		return this.applicationUri;
	}

	private void setApplicationName(List<Content> content) {
		for (Content c : content) {
			this.applicationName = c.getValue();
		}
	}

	public void setApplicationName(String name) {
		this.applicationName = name;
	}

	public void setApplicationUri(String uri) {
		this.applicationUri = uri;
	}

	public String getProductName() {
		return this.productName;
	}

	/**
	 * Sets the application uri from the XML file.
	 * 
	 * @param content the new application uri
	 */
	private void setApplicationUri(List<Content> content) {
		for (Content c : content) {
			this.applicationUri = c.getValue();
		}
	}

	public AllowNoneCertificateValidator getCertificateValidator() {
		return this.certificateValidator;
	}

	public ApplicationType getApplicationType() {
		ApplicationType type = null;
		try {
			type = ApplicationType.valueOf(this.applicationType);
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
		}
		return type;
	}

	public void setApplicationType(String applicationType) {
		String[] splitted = applicationType.split("_");
		String appType = applicationType;
		if (splitted != null && splitted.length > 1) {
			appType = splitted[0];
		}
		this.applicationType = appType;
	}

	public String getOperationTimeout() {
		return operationTimeout;
	}

	public void setOperationTimeout(String operationTimeout) {
		this.operationTimeout = operationTimeout;
	}

	public String getMaxStringLength() {
		return maxStringLength;
	}

	public void setMaxStringLength(String maxStringLength) {
		this.maxStringLength = maxStringLength;
	}

	public String getMaxByteStringLength() {
		return maxByteStringLength;
	}

	public void setMaxByteStringLength(String maxByteStringLength) {
		this.maxByteStringLength = maxByteStringLength;
	}

	public String getMaxArrayLength() {
		return maxArrayLength;
	}

	public void setMaxArrayLength(String maxArrayLength) {
		this.maxArrayLength = maxArrayLength;
	}

	public String getMaxMessageSize() {
		return maxMessageSize;
	}

	public void setMaxMessageSize(String maxMessageSize) {
		this.maxMessageSize = maxMessageSize;
	}

	public String getMaxBufferSize() {
		return maxBufferSize;
	}

	public void setMaxBufferSize(String maxBufferSize) {
		this.maxBufferSize = maxBufferSize;
	}

	public String getChannelLifetime() {
		return channelLifetime;
	}

	public void setChannelLifetime(String channelLifetime) {
		this.channelLifetime = channelLifetime;
	}

	public String getSecurityTokenLifetime() {
		return securityTokenLifetime;
	}

	public void setSecurityTokenLifetime(String securityTokenLifetime) {
		this.securityTokenLifetime = securityTokenLifetime;
	}

	public SecurityCertificateAdministration getSecurityCertificateAdministration() {
		return securityCertificateAdministration;
	}

	public void setSecurityCertificateAdministration(
			SecurityCertificateAdministration securityCertificateAdministration) {
		this.securityCertificateAdministration = securityCertificateAdministration;
	}

	/**
	 * Sets the product name from the XML file.
	 * 
	 * @param content the new product name
	 */
	private void setProductName(List<Content> content) {
		for (Content c : content) {
			this.productName = c.getValue();
		}
	}

	public void setProductName(String prodName) {
		this.productName = prodName;
	}

	/**
	 * Sets the product uri from the XML file.
	 * 
	 * @param content the new product uri
	 */
	private void setProductUri(List<Content> content) {
		for (Content c : content) {
			this.productUri = c.getValue();
		}
	}

	public void setProductUri(String uri) {
		this.productUri = uri;
	}

	public String getProductUri() {
		return this.productUri;
	}

	/**
	 * Read all security configuration tags from the file and fills in
	 * 
	 * @param content the new security configuration
	 */
	private void setSecurityConfiguration(List<Content> content) {
		for (Content c : content) {
			if (c instanceof Element) {
				if (((Element) c).getName().equals(ApplicationConfigurationTags.ApplicationCertificate.name())) {
					this.securityConfiguration.createAppCert(c);
				} else if (((Element) c).getName()
						.equals(ApplicationConfigurationTags.TrustedPeerCertificates.name())) {
					this.securityConfiguration.createTrustedPeerCert(c);
				}
			}
		}
	}

	public void setSecurityConfiguration(SecurityConfiguration securityConfiguration) {
		this.securityConfiguration = securityConfiguration;
	}

	public void setServerConfiguration(ServerConfiguration serverConfiguration) {
		this.serverConfiguration = serverConfiguration;
	}

	public void setCertificateValidator(AllowNoneCertificateValidator certificateValidator) {
		this.certificateValidator = certificateValidator;
	}

	public DateTime getBuildDate() {
		return buildDate;
	}

	private void setBuildDate(List<Content> content) {
		for (Content c : content) {
			this.buildDate = OPCDateFormat.parseDateTime(c.getValue());
		}
	}

	public void setBuildDate(DateTime date) {
		this.buildDate = date;
	}

	public String getBuildNumber() {
		return buildNumber;
	}

	public void setBuildNumber(List<Content> content) {
		for (Content c : content) {
			this.buildNumber = c.getValue();
		}
	}

	public void setBuildNumber(String number) {
		buildNumber = number;
	}

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(List<Content> content) {
		for (Content c : content) {
			this.manufacturerName = c.getValue();
		}
	}

	public void setManufacturerName(String name) {
		this.manufacturerName = name;
	}

	public String getCertificateValidity() {
		return this.certificateValidity;
	}

	public void setCertificateValidity(String certificateValidity) {
		this.certificateValidity = certificateValidity;
	}

	public String getSoftwareVersion() {
		return softwareVersion;
	}

	public void setSoftwareVersion(List<Content> content) {
		for (Content c : content) {
			this.softwareVersion = c.getValue();
		}
	}

	public void setSoftwareVersion(String version) {
		this.softwareVersion = version;
	}

	public String getApplicationName() {
		return this.applicationName;
	}

	public List<String> getServerInformationModels() {
		return this.informationModels;
	}

	public List<String> getServerNodeSets() {
		return this.nodeSets;
	}

	public CertificateValidator getUserAuthCertValidator() {
		return this.userAuthCertStore;
	}

	public void setUserAuthCertValidator(CertificateValidator userAuthCertValidator) {
		this.userAuthCertStore = userAuthCertValidator;
	}
}
