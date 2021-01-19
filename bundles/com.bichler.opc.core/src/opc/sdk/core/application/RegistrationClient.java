package opc.sdk.core.application;

import opc.sdk.core.discovery.ConfiguredEndpoint;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.application.Client;
import org.opcfoundation.ua.application.SessionChannel;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.core.RegisterServerRequest;
import org.opcfoundation.ua.core.RegisterServerResponse;
import org.opcfoundation.ua.core.RegisteredServer;
import org.opcfoundation.ua.core.RequestHeader;
import org.opcfoundation.ua.transport.security.KeyPair;

/**
 * An object used by clients to access a UA discovery service
 * 
 * @author Thomas Z&ouml;bauer
 * 
 */
public class RegistrationClient {
	private Client clientInstance = null;
	private ConfiguredEndpoint endpoint = null;
	private SecurityCertificateAdministration securityCert = null;
	private ApplicationDescription applicationConfiguration = null;

	RegistrationClient() {
	}

	/**
	 * Local client on the server
	 * 
	 * @param endpoint
	 * @param securityServer
	 * @param applicationConfiguration
	 */
	public RegistrationClient(ConfiguredEndpoint endpoint, SecurityCertificateAdministration securityServer,
			ApplicationDescription applicationConfiguration) {
		this();
		this.endpoint = endpoint;
		this.securityCert = securityServer;
		this.applicationConfiguration = applicationConfiguration;
		this.clientInstance = Client
				.createClientApplication(new KeyPair(securityServer.getCert(), securityServer.getPrivKey()));
	}

	public RegisterServerResponse registerServer(RegisteredServer registrationInfo) {
		SessionChannel chan = null;
		try {
			chan = this.clientInstance.createSessionChannel(this.applicationConfiguration);
		} catch (ServiceResultException e1) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e1);
		}
		RequestHeader requestHeader = new RequestHeader();
		requestHeader.setTimestamp(DateTime.currentTime());
		RegisterServerResponse response = null;
		try {
			RegisterServerRequest request = new RegisterServerRequest();
			request.setServer(registrationInfo);
			request.setRequestHeader(requestHeader);
			if (chan == null)
				throw new NullPointerException("Session Channel is null");
			response = chan.RegisterServer(request);
		} catch (NullPointerException | ServiceResultException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (chan != null)
					chan.close();
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			}
			if (chan != null)
				chan.dispose();
		}
		return response;
	}

	public Client getClientInstance() {
		return clientInstance;
	}

	public void setClientInstance(Client clientInstance) {
		this.clientInstance = clientInstance;
	}

	public ConfiguredEndpoint getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(ConfiguredEndpoint endpoint) {
		this.endpoint = endpoint;
	}

	public SecurityCertificateAdministration getSecurityCert() {
		return securityCert;
	}

	public void setSecurityCert(SecurityCertificateAdministration securityCert) {
		this.securityCert = securityCert;
	}

	public ApplicationDescription getApplicationConfiguration() {
		return applicationConfiguration;
	}

	public void setApplicationConfiguration(ApplicationDescription applicationConfiguration) {
		this.applicationConfiguration = applicationConfiguration;
	}
}
