package opc.client.application.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.opcfoundation.ua.core.ApplicationType;

/**
 * Parser for a client application configuration File.
 * 
 * @author Thomas Z&ouml;chbauer
 * @since 23.05.2012, HB-Softsolution e.U.
 */
public class ApplicationConfigurationReader {
	/** Root Element of the Application Configuration XML - File */
	private Element rootElement = null;

	/**
	 * Parser for a Client-Application File
	 * 
	 * @param ConfigFile Configuration File for the Parser.
	 * @throws JDOMException
	 */
	public ApplicationConfigurationReader(InputStream configFile) throws JDOMException {
		this.rootElement = initialize(configFile);
		if (!"ApplicationConfiguration".equals(this.rootElement.getName())) {
			throw new IllegalArgumentException("Bad_ConfigFile_RootInvalid");
		}
	}

	/**
	 * DOM initialization of the Application File
	 * 
	 * @param ConfigFile Configuration File for the Parser to initialize.
	 * @return Root Element of the Configuration File.
	 */
	private Element initialize(InputStream configFile) throws JDOMException {
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		try {
			doc = builder.build(configFile);
		} catch (JDOMException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			throw e;
		} catch (IOException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
		}
		if (doc == null) {
			throw new NullPointerException("Document is null, can't get root element");
		}
		return doc.getRootElement();
	}

	/**
	 * Returns the Application Name, defined by the Configuration File.
	 * 
	 * @return ApplicationName
	 */
	public String getApplicationName() {
		Element applicationName = findTag(this.rootElement, "ApplicationName");
		return applicationName.getValue();
	}

	/**
	 * Returns the Application Uri, defined by the Configuration File.
	 * 
	 * @return ApplicationUri
	 */
	public String getApplicationUri() {
		Element applicationUri = findTag(this.rootElement, "ApplicationUri");
		return applicationUri.getValue();
	}

	/**
	 * Returns the Product Uri, defined by the Configuration File.
	 * 
	 * @return ProductUri
	 */
	public String getProductUri() {
		Element productUri = findTag(this.rootElement, "ProductUri");
		return productUri.getValue();
	}

	/**
	 * Returns the Application Type, defined by the Configuration File.
	 * 
	 * @return {@link ApplicationType}
	 */
	public String getApplicationType() {
		Element applicationType = findTag(this.rootElement, "ApplicationType");
		return applicationType.getValue();
	}

	/**
	 * Returns the Security Configuration.
	 * 
	 * @return {@link ClientSecurityConfiguration}
	 */
	public ClientSecurityConfiguration getSecurityConfiguration() {
		Element securityConfiguration = findTag(this.rootElement, "SecurityConfiguration");
		Element applicationCertificate = findTag(securityConfiguration, "ApplicationCertificate");
		Element storePath = findTag(applicationCertificate, "StorePath");
		Element subjectName = findTag(applicationCertificate, "SubjectName");
		Element privateKeyPassword = findTag(applicationCertificate, "PrivateKeyPassword");
		Element nonceLength = findTag(securityConfiguration, "NonceLength");
		return new ClientSecurityConfiguration(storePath.getValue(), subjectName.getValue(),
				privateKeyPassword.getValue(), nonceLength.getValue());
	}

	/**
	 * Returns the Transport Configuration.
	 * 
	 * @return Transport Configuration
	 */
	public String getTransportConfiguration() {
		Element transportConfiguration = findTag(this.rootElement, "TransportConfiguration");
		return transportConfiguration.getValue();
	}

	/**
	 * Returns the Transport Quotas.
	 * 
	 * @return Transport Quotas
	 */
	public TransportQuotas getTransportQuotas() {
		Element transportQuotasTag = findTag(this.rootElement, "TransportQuotas");
		Element operationTimeout = findTag(transportQuotasTag, "OperationTimeout");
		Element maxStringLength = findTag(transportQuotasTag, "MaxStringLength");
		Element maxByteStringLength = findTag(transportQuotasTag, "MaxByteStringLength");
		Element maxArrayLength = findTag(transportQuotasTag, "MaxArrayLength");
		Element maxMessageSize = findTag(transportQuotasTag, "MaxMessageSize");
		Element maxBufferSize = findTag(transportQuotasTag, "MaxBufferSize");
		Element channelLifetime = findTag(transportQuotasTag, "ChannelLifetime");
		Element securityTokenLifeTime = findTag(transportQuotasTag, "SecurityTokenLifetime");
		return new TransportQuotas(operationTimeout.getValue(), maxStringLength.getValue(),
				maxByteStringLength.getValue(), maxArrayLength.getValue(), maxMessageSize.getValue(),
				maxBufferSize.getValue(), channelLifetime.getValue(), securityTokenLifeTime.getValue());
	}

	/**
	 * Returns the Client Application Configuration.
	 * 
	 * @return {@link ClientApplicationConfiguration}
	 */
	public ClientApplicationConfiguration getClientConfiguration() {
		Element clientConfigurationTag = findTag(this.rootElement, "ClientConfiguration");
		Element defaultSessionTimeout = findTag(clientConfigurationTag, "DefaultSessionTimeout");
		Element wellknownDiscoveryServers = findTag(clientConfigurationTag, "DiscoveryServers");
		Element minSubscriptionsLifetime = findTag(clientConfigurationTag, "MinSubscriptionLifetime");
		// remove first and last space
		String discoveryServer = wellknownDiscoveryServers.getValue().trim();
		// get the array
		String[] discoveryServers = discoveryServer.split("\\n");
		for (int ii = 0; ii < discoveryServers.length; ii++) {
			discoveryServers[ii] = discoveryServers[ii].trim();
		}
		return new ClientApplicationConfiguration(defaultSessionTimeout.getValue(), discoveryServers,
				minSubscriptionsLifetime.getValue());
	}

	/**
	 * Returns all Extensions, defined by the Configuration File.
	 * 
	 * @return Extensions
	 */
	public String getExtensions() {
		Element extensions = findTag(this.rootElement, "Extensions");
		return extensions.getValue();
	}

	/**
	 * Returns the Trace Configuration, defined by the Configuration File.
	 * 
	 * @return {@link TraceConfiguration}
	 */
	public TraceConfiguration getTraceConfiguration() {
		Element traceConfigurationTag = findTag(this.rootElement, "TraceConfiguration");
		Element outputFilePath = findTag(traceConfigurationTag, "OutputFilePath");
		Element deleteOnLoad = findTag(traceConfigurationTag, "DeleteOnLoad");
		Element traceMasks = findTag(traceConfigurationTag, "TraceMasks");
		return new TraceConfiguration(outputFilePath.getValue(), deleteOnLoad.getValue(), traceMasks.getValue());
	}

	/**
	 * It is not required, because the Java DateTime should be a hi-res-clock
	 * 
	 * @return
	 */
	public Boolean getDisableHiResClock() {
		Element applicationUri = findTag(this.rootElement, "DisableHiResClock");
		return new Boolean(applicationUri.getValue());
	}

	/**
	 * Method to read a Tag from an Application Configuration,
	 * 
	 * @param Parent  Parent Tag from the Tag to find.
	 * @param TagName Tag Name from the Tag to find.
	 * @return a found Tag Element, or NULL.
	 */
	private Element findTag(Element parent, String tagName) {
		if (tagName == null) {
			throw new IllegalArgumentException("Bad_Tagname is null");
		}
		Iterator<?> iterator = parent.getChildren().iterator();
		while (iterator.hasNext()) {
			Element element = (Element) iterator.next();
			if (tagName.equals(element.getName())) {
				return element;
			}
		}
		throw new IllegalArgumentException("Bad_Tagname not found");
	}
}
