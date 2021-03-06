package opc.sdk.core.classes.ua.core;

public class ServerDiagnosticsSummaryType extends BaseDataVariableType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.ServerDiagnosticsSummaryType;
	private BaseDataVariableType sessionAbortCount;
	private BaseDataVariableType serverViewCount;
	private BaseDataVariableType rejectedRequestsCount;
	private BaseDataVariableType cumulatedSubscriptionCount;
	private BaseDataVariableType securityRejectedSessionCount;
	private BaseDataVariableType rejectedSessionCount;
	private BaseDataVariableType currentSessionCount;
	private BaseDataVariableType currentSubscriptionCount;
	private BaseDataVariableType cumulatedSessionCount;
	private BaseDataVariableType sessionTimeoutCount;
	private BaseDataVariableType securityRejectedRequestsCount;
	private BaseDataVariableType publishingIntervalCount;

	public ServerDiagnosticsSummaryType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.core.ServerDiagnosticsSummaryDataType getValue() {
		return getVariant() != null
				? (org.opcfoundation.ua.core.ServerDiagnosticsSummaryDataType) getVariant().getValue()
				: null;
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public BaseDataVariableType getSessionAbortCount() {
		return sessionAbortCount;
	}

	public void setSessionAbortCount(BaseDataVariableType value) {
		sessionAbortCount = value;
	}

	public BaseDataVariableType getServerViewCount() {
		return serverViewCount;
	}

	public void setServerViewCount(BaseDataVariableType value) {
		serverViewCount = value;
	}

	public BaseDataVariableType getRejectedRequestsCount() {
		return rejectedRequestsCount;
	}

	public void setRejectedRequestsCount(BaseDataVariableType value) {
		rejectedRequestsCount = value;
	}

	public BaseDataVariableType getCumulatedSubscriptionCount() {
		return cumulatedSubscriptionCount;
	}

	public void setCumulatedSubscriptionCount(BaseDataVariableType value) {
		cumulatedSubscriptionCount = value;
	}

	public BaseDataVariableType getSecurityRejectedSessionCount() {
		return securityRejectedSessionCount;
	}

	public void setSecurityRejectedSessionCount(BaseDataVariableType value) {
		securityRejectedSessionCount = value;
	}

	public BaseDataVariableType getRejectedSessionCount() {
		return rejectedSessionCount;
	}

	public void setRejectedSessionCount(BaseDataVariableType value) {
		rejectedSessionCount = value;
	}

	public BaseDataVariableType getCurrentSessionCount() {
		return currentSessionCount;
	}

	public void setCurrentSessionCount(BaseDataVariableType value) {
		currentSessionCount = value;
	}

	public BaseDataVariableType getCurrentSubscriptionCount() {
		return currentSubscriptionCount;
	}

	public void setCurrentSubscriptionCount(BaseDataVariableType value) {
		currentSubscriptionCount = value;
	}

	public BaseDataVariableType getCumulatedSessionCount() {
		return cumulatedSessionCount;
	}

	public void setCumulatedSessionCount(BaseDataVariableType value) {
		cumulatedSessionCount = value;
	}

	public BaseDataVariableType getSessionTimeoutCount() {
		return sessionTimeoutCount;
	}

	public void setSessionTimeoutCount(BaseDataVariableType value) {
		sessionTimeoutCount = value;
	}

	public BaseDataVariableType getSecurityRejectedRequestsCount() {
		return securityRejectedRequestsCount;
	}

	public void setSecurityRejectedRequestsCount(BaseDataVariableType value) {
		securityRejectedRequestsCount = value;
	}

	public BaseDataVariableType getPublishingIntervalCount() {
		return publishingIntervalCount;
	}

	public void setPublishingIntervalCount(BaseDataVariableType value) {
		publishingIntervalCount = value;
	}

	@Override
	public String toString() {
		return "ServerDiagnosticsSummaryType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
