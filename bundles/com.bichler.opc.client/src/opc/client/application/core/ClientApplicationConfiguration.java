package opc.client.application.core;

/**
 * A client application configuration.
 * 
 * @author Thomas Z&ouml;chbauer
 * @since 23.05.2012, HB-Softsolution e.U.
 */
public class ClientApplicationConfiguration {
	/** Default Session Timeout */
	private String defaultSessionTimeout = null;
	/** Known Discovery Servers */
	private String[] discoveryServers = null;
	/** Minimum Subscription Lifetime Count */
	private String minSubscriptionLifetime = null;

	/*
	 * default empty constructor to create a client application configuration from
	 * scratch
	 */
	public ClientApplicationConfiguration() {
		// do nothing
	}

	/**
	 * A Client Application Configuration.
	 * 
	 * @param DefaultSessionTimeout
	 * @param WellKnownDiscorveryUrls
	 * @param DiscoveryServers
	 * @param EndpointCacheFilePath
	 * @param MinSubscriptionLifetime
	 */
	public ClientApplicationConfiguration(String defaultSessionTimeout, String[] discoveryServers,
			String minSubscriptionLifetime) {
		this.defaultSessionTimeout = defaultSessionTimeout;
		this.discoveryServers = discoveryServers;
		this.minSubscriptionLifetime = minSubscriptionLifetime;
	}

	/**
	 * Returns the Default Session Timeout.
	 * 
	 * @return DefaultSessionTimeout
	 */
	public Double getDefaultSessionTimeout() {
		return Double.parseDouble(this.defaultSessionTimeout);
	}

	public String[] getDiscoveryServer() {
		return this.discoveryServers;
	}

	public int getMinSubscriptionLifetime() {
		return Integer.parseInt(this.minSubscriptionLifetime);
	}

	public void setDefaultSessionTimeout(double timeout) {
		this.defaultSessionTimeout = Double.toString(timeout);
	}
}
