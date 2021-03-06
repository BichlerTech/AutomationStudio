package opc.sdk.core.newApplication;

/**
 * Enum for server configuration tags.
 * 
 * @author Thomas Z&ouml;bauer
 *
 */
public enum ServerConfigurationTags {
	BaseAddresses, SecurityPolicies, UserTokenPolicies, DiagnosticsEnabled, MaxSessionCount, MinSessionTimeout,
	MaxSessionTimeout, MaxBrowseContinuationPoints, MaxQueryContinuationPoints, MaxHistoryContinuationPoints,
	MaxRequestAge, MinPublishingInterval, MaxPublishingInterval, PublishingResolution, MaxSubscriptionLifetime,
	MaxMessageQueueSize, MaxNotificationQueueSize, MaxNotificationsPerPublish, MinMetadataSamplingInterval,
	AvailableSamplingRates, MaxRegistrationInterval, NodeManagerSaveFile, SecurityMode, SecurityPolicyUri,
	SecurityLevel, SamplingRateGroup, Start, Increment, Count;

	public String getName() {
		return this.toString();
	}
}
