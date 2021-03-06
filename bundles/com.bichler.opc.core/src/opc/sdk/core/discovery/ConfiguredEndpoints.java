package opc.sdk.core.discovery;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.core.ApplicationType;
import org.opcfoundation.ua.core.EndpointConfiguration;
import org.opcfoundation.ua.core.EndpointDescription;

import opc.sdk.core.application.ApplicationConfiguration;
import opc.sdk.core.utils.Utils;

/**
 * Stores a list of cached enpoints.
 * 
 * @author Thomas Z&ouml;bauer
 * 
 */
public class ConfiguredEndpoints {
	private String[] discoveryUrls = null;
	private ArrayList<String> knownHosts = null;
	private EndpointConfiguration defaultConfiguration = null;
	private ArrayList<ConfiguredEndpoint> endpoints = null;

	/**
	 * The default constructor
	 */
	public ConfiguredEndpoints() {
		initialize();
	}

	/**
	 * Initializes the object from an application configuration.
	 * 
	 * @param applicationConfiguration
	 */
	public ConfiguredEndpoints(ApplicationConfiguration applicationConfiguration) {
		initialize();
	}

	/**
	 * Sets private members to default values
	 */
	private void initialize() {
		this.setKnownHosts(new ArrayList<String>());
		this.setDiscoveryUrls(Utils.DISCOVERYURLS);
		this.endpoints = new ArrayList<ConfiguredEndpoint>();
		this.defaultConfiguration = EndpointConfiguration.defaults();
	}

	/**
	 * Add the endpoint description to the cache
	 * 
	 * @param endpoint
	 */
	public ConfiguredEndpoint add(EndpointDescription endpoint) {
		return add(endpoint, null);
	}

	private ConfiguredEndpoint add(EndpointDescription endpoint, EndpointConfiguration configuration) {
		validateEndpoint(endpoint);
		for (ConfiguredEndpoint item : this.endpoints) {
			if (item.getDescription() == endpoint) {
				throw new IllegalArgumentException("Endpoint already exists in the collection!");
			}
		}
		ConfiguredEndpoint configuredEndpoint = new ConfiguredEndpoint(this, endpoint, configuration);
		this.endpoints.add(configuredEndpoint);
		return configuredEndpoint;
	}

	/**
	 * Throws exceptions if the endpoint is not valid
	 * 
	 * @param endpoint
	 */
	private void validateEndpoint(EndpointDescription endpoint) {
		if (endpoint == null) {
			throw new IllegalArgumentException("Endpoint is null");
		}
		if (endpoint.getEndpointUrl() == null || endpoint.getEndpointUrl().isEmpty()) {
			throw new IllegalArgumentException("EndpointURL is null");
		}
		if (endpoint.getServer() == null) {
			endpoint.setServer(new ApplicationDescription());
			endpoint.getServer().setApplicationType(ApplicationType.Server);
		}
		if (endpoint.getServer().getApplicationUri() == null || endpoint.getServer().getApplicationUri().isEmpty()) {
			endpoint.getServer().setApplicationUri(endpoint.getEndpointUrl());
		}
	}

	public EndpointConfiguration getDefaultConfiguration() {
		return this.defaultConfiguration;
	}

	public List<ConfiguredEndpoint> getEndpoints() {
		return this.endpoints;
	}

	public String[] getDiscoveryUrls() {
		return discoveryUrls;
	}

	public void setDiscoveryUrls(String[] discoveryUrls) {
		this.discoveryUrls = discoveryUrls;
	}

	public ArrayList<String> getKnownHosts() {
		return knownHosts;
	}

	public void setKnownHosts(ArrayList<String> knownHosts) {
		this.knownHosts = knownHosts;
	}
}
