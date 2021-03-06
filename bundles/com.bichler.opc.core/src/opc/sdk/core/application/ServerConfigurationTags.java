package opc.sdk.core.application;

/**
 * Enum of the server configuration tags.
 * 
 * @author Thomas Z&ouml;bauer
 *
 */
public enum ServerConfigurationTags {
	BaseAddresses, SecurityPolicies, UserTokenPolicies, TokenType, DiagnosticsEnabled, MaxSessionCount,
	MinSessionTimeout, MaxSessionTimeout, MaxBrowseContinuationPoints, MaxQueryContinuationPoints,
	MaxHistoryContinuationPoints, MaxRequestAge, MinPublishingInterval, MaxPublishingInterval, PublishingResolution,
	MaxSubscriptionLifetime, MinSubscriptionLifetime, MaxMessageQueueSize, MaxNotificationQueueSize,
	MaxNotificationsPerPublish, MinMetadataSamplingInterval, AvailableSamplingRates, MaxRegistrationInterval,
	NodeManagerSaveFile, SecurityMode, SecurityPolicyUri, SecurityLevel, SamplingRateGroup, Start, Increment, Count,
	MaxSubscriptionCount, MaxPublishRequests, UseCertificateStore;

	public String getName() {
		return this.toString();
	}
}
