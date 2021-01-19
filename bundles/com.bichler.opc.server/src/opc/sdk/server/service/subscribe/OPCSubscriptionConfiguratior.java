package opc.sdk.server.service.subscribe;

import opc.sdk.core.application.ApplicationConfiguration;
import opc.sdk.core.application.ServerConfiguration;

import org.opcfoundation.ua.builtintypes.UnsignedInteger;

public class OPCSubscriptionConfiguratior {
	/**
	 * Minimum publish interval for all subscriptions, defined by the server
	 * configuration
	 */
	private Double minPublishingInterval;
	/**
	 * Minimum subscription lifetime for all subscriptions, defined by the server
	 * configuration
	 */
	private UnsignedInteger minSubscriptionLifetime;
	/**
	 * Maximum message count for all subscriptions, defined by the server
	 * configuration
	 */
	private int maxMessageCount = -1;
	/**
	 * Maximum notifications per publish for all subscriptions, defined by the
	 * server configuration
	 */
	private UnsignedInteger maxNotificationPerPublish = null;
	/**
	 * Maximum publish interval for all subscriptions, defined by the server
	 * configuration
	 */
	private Double maxPublishingInterval = null;
	/**
	 * Maximum publish request for all subscriptions, defined by the server
	 * configuration
	 */
	private UnsignedInteger maxPublishRequest = null;
	/**
	 * Maximum subscription lifetime for all subscriptions, defined by the server
	 * configuration
	 */
	private UnsignedInteger maxSubscriptionLifetime = null;
	/**
	 * Publish resolution for the subscription publish thread to execute, defined by
	 * the server configuration
	 */
	private long publishingResolution;
	private Integer maxSubscriptionsCount;

	public OPCSubscriptionConfiguratior() {
		initDefaults();
	}

	private void initDefaults() {
		this.minPublishingInterval = 100.0;
		this.maxPublishingInterval = 100000.0;
		this.publishingResolution = 50;
		this.maxSubscriptionLifetime = new UnsignedInteger(60000);
		this.minSubscriptionLifetime = new UnsignedInteger(500);
		this.maxMessageCount = 250;
		this.maxNotificationPerPublish = new UnsignedInteger(250);
		this.maxSubscriptionsCount = 1000;
		this.maxPublishRequest = new UnsignedInteger(50);
	}

	public void setConfiguration(ApplicationConfiguration configuration) {
		ServerConfiguration serverConfig = configuration.getServerConfiguration();
		this.minPublishingInterval = serverConfig.getMinPublishingInterval();
		this.maxPublishingInterval = serverConfig.getMaxPublishingInterval();
		this.publishingResolution = serverConfig.getPublishResolution();
		this.maxSubscriptionLifetime = serverConfig.getMaxSubscriptionLifetime();
		this.minSubscriptionLifetime = serverConfig.getMinSubscriptionLifetime();
		this.maxMessageCount = serverConfig.getMaxMessageCount();
		this.maxNotificationPerPublish = serverConfig.getMaxNotificationPerPublish();
		this.maxSubscriptionsCount = serverConfig.getMaxSubscriptionCount();
		this.maxPublishRequest = serverConfig.getMaxPublishRequests();
	}

	public Double getMinPublishingInterval() {
		return minPublishingInterval;
	}

	public void setMinPublishingInterval(Double minPublishingInterval) {
		this.minPublishingInterval = minPublishingInterval;
	}

	public UnsignedInteger getMinSubscriptionLifetime() {
		return minSubscriptionLifetime;
	}

	public void setMinSubscriptionLifetime(UnsignedInteger minSubscriptionLifetime) {
		this.minSubscriptionLifetime = minSubscriptionLifetime;
	}

	public int getMaxMessageCount() {
		return maxMessageCount;
	}

	public void setMaxMessageCount(int maxMessageCount) {
		this.maxMessageCount = maxMessageCount;
	}

	public UnsignedInteger getMaxNotificationPerPublish() {
		return maxNotificationPerPublish;
	}

	public void setMaxNotificationPerPublish(UnsignedInteger maxNotificationPerPublish) {
		this.maxNotificationPerPublish = maxNotificationPerPublish;
	}

	public Double getMaxPublishingInterval() {
		return maxPublishingInterval;
	}

	public void setMaxPublishingInterval(Double maxPublishingInterval) {
		this.maxPublishingInterval = maxPublishingInterval;
	}

	public UnsignedInteger getMaxPublishRequest() {
		return maxPublishRequest;
	}

	public void setMaxPublishRequest(UnsignedInteger maxPublishRequest) {
		this.maxPublishRequest = maxPublishRequest;
	}

	public UnsignedInteger getMaxSubscriptionLifetime() {
		return maxSubscriptionLifetime;
	}

	public void setMaxSubscriptionLifetime(UnsignedInteger maxSubscriptionLifetime) {
		this.maxSubscriptionLifetime = maxSubscriptionLifetime;
	}

	public long getPublishingResolution() {
		return publishingResolution;
	}

	public void setPublishingResolution(Integer publishingResolution) {
		this.publishingResolution = publishingResolution;
	}

	public Integer getMaxSubscriptionsCount() {
		return maxSubscriptionsCount;
	}

	public void setMaxSubscriptionsCount(Integer maxSubscriptionsCount) {
		this.maxSubscriptionsCount = maxSubscriptionsCount;
	}
}
