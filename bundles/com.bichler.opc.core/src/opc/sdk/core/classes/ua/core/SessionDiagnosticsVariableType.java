package opc.sdk.core.classes.ua.core;

public class SessionDiagnosticsVariableType extends BaseDataVariableType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.SessionDiagnosticsVariableType;
	private BaseDataVariableType queryNextCount;
	private BaseDataVariableType callCount;
	private BaseDataVariableType sessionId;
	private BaseDataVariableType unauthorizedRequestCount;
	private BaseDataVariableType clientDescription;
	private BaseDataVariableType readCount;
	private BaseDataVariableType addNodesCount;
	private BaseDataVariableType setPublishingModeCount;
	private BaseDataVariableType createSubscriptionCount;
	private BaseDataVariableType transferSubscriptionsCount;
	private BaseDataVariableType currentSubscriptionsCount;
	private BaseDataVariableType deleteNodesCount;
	private BaseDataVariableType browseNextCount;
	private BaseDataVariableType deleteSubscriptionsCount;
	private BaseDataVariableType deleteMonitoredItemsCount;
	private BaseDataVariableType localeIds;
	private BaseDataVariableType currentMonitoredItemsCount;
	private BaseDataVariableType clientLastContactTime;
	private BaseDataVariableType endpointUrl;
	private BaseDataVariableType writeCount;
	private BaseDataVariableType createMonitoredItemsCount;
	private BaseDataVariableType historyUpdateCount;
	private BaseDataVariableType maxResponseMessageSize;
	private BaseDataVariableType setMonitoringModeCount;
	private BaseDataVariableType setTriggeringCount;
	private BaseDataVariableType deleteReferencesCount;
	private BaseDataVariableType historyReadCount;
	private BaseDataVariableType modifySubscriptionCount;
	private BaseDataVariableType actualSessionTimeout;
	private BaseDataVariableType addReferencesCount;
	private BaseDataVariableType republishCount;
	private BaseDataVariableType totalRequestCount;
	private BaseDataVariableType clientConnectionTime;
	private BaseDataVariableType publishCount;
	private BaseDataVariableType currentPublishRequestsInQueue;
	private BaseDataVariableType translateBrowsePathsToNodeIdsCount;
	private BaseDataVariableType modifyMonitoredItemsCount;
	private BaseDataVariableType queryFirstCount;
	private BaseDataVariableType unregisterNodesCount;
	private BaseDataVariableType sessionName;
	private BaseDataVariableType browseCount;
	private BaseDataVariableType registerNodesCount;
	private BaseDataVariableType serverUri;

	public SessionDiagnosticsVariableType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.core.SessionDiagnosticsDataType getValue() {
		return getVariant() != null ? (org.opcfoundation.ua.core.SessionDiagnosticsDataType) getVariant().getValue()
				: null;
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public BaseDataVariableType getQueryNextCount() {
		return queryNextCount;
	}

	public void setQueryNextCount(BaseDataVariableType value) {
		queryNextCount = value;
	}

	public BaseDataVariableType getCallCount() {
		return callCount;
	}

	public void setCallCount(BaseDataVariableType value) {
		callCount = value;
	}

	public BaseDataVariableType getSessionId() {
		return sessionId;
	}

	public void setSessionId(BaseDataVariableType value) {
		sessionId = value;
	}

	public BaseDataVariableType getUnauthorizedRequestCount() {
		return unauthorizedRequestCount;
	}

	public void setUnauthorizedRequestCount(BaseDataVariableType value) {
		unauthorizedRequestCount = value;
	}

	public BaseDataVariableType getClientDescription() {
		return clientDescription;
	}

	public void setClientDescription(BaseDataVariableType value) {
		clientDescription = value;
	}

	public BaseDataVariableType getReadCount() {
		return readCount;
	}

	public void setReadCount(BaseDataVariableType value) {
		readCount = value;
	}

	public BaseDataVariableType getAddNodesCount() {
		return addNodesCount;
	}

	public void setAddNodesCount(BaseDataVariableType value) {
		addNodesCount = value;
	}

	public BaseDataVariableType getSetPublishingModeCount() {
		return setPublishingModeCount;
	}

	public void setSetPublishingModeCount(BaseDataVariableType value) {
		setPublishingModeCount = value;
	}

	public BaseDataVariableType getCreateSubscriptionCount() {
		return createSubscriptionCount;
	}

	public void setCreateSubscriptionCount(BaseDataVariableType value) {
		createSubscriptionCount = value;
	}

	public BaseDataVariableType getTransferSubscriptionsCount() {
		return transferSubscriptionsCount;
	}

	public void setTransferSubscriptionsCount(BaseDataVariableType value) {
		transferSubscriptionsCount = value;
	}

	public BaseDataVariableType getCurrentSubscriptionsCount() {
		return currentSubscriptionsCount;
	}

	public void setCurrentSubscriptionsCount(BaseDataVariableType value) {
		currentSubscriptionsCount = value;
	}

	public BaseDataVariableType getDeleteNodesCount() {
		return deleteNodesCount;
	}

	public void setDeleteNodesCount(BaseDataVariableType value) {
		deleteNodesCount = value;
	}

	public BaseDataVariableType getBrowseNextCount() {
		return browseNextCount;
	}

	public void setBrowseNextCount(BaseDataVariableType value) {
		browseNextCount = value;
	}

	public BaseDataVariableType getDeleteSubscriptionsCount() {
		return deleteSubscriptionsCount;
	}

	public void setDeleteSubscriptionsCount(BaseDataVariableType value) {
		deleteSubscriptionsCount = value;
	}

	public BaseDataVariableType getDeleteMonitoredItemsCount() {
		return deleteMonitoredItemsCount;
	}

	public void setDeleteMonitoredItemsCount(BaseDataVariableType value) {
		deleteMonitoredItemsCount = value;
	}

	public BaseDataVariableType getLocaleIds() {
		return localeIds;
	}

	public void setLocaleIds(BaseDataVariableType value) {
		localeIds = value;
	}

	public BaseDataVariableType getCurrentMonitoredItemsCount() {
		return currentMonitoredItemsCount;
	}

	public void setCurrentMonitoredItemsCount(BaseDataVariableType value) {
		currentMonitoredItemsCount = value;
	}

	public BaseDataVariableType getClientLastContactTime() {
		return clientLastContactTime;
	}

	public void setClientLastContactTime(BaseDataVariableType value) {
		clientLastContactTime = value;
	}

	public BaseDataVariableType getEndpointUrl() {
		return endpointUrl;
	}

	public void setEndpointUrl(BaseDataVariableType value) {
		endpointUrl = value;
	}

	public BaseDataVariableType getWriteCount() {
		return writeCount;
	}

	public void setWriteCount(BaseDataVariableType value) {
		writeCount = value;
	}

	public BaseDataVariableType getCreateMonitoredItemsCount() {
		return createMonitoredItemsCount;
	}

	public void setCreateMonitoredItemsCount(BaseDataVariableType value) {
		createMonitoredItemsCount = value;
	}

	public BaseDataVariableType getHistoryUpdateCount() {
		return historyUpdateCount;
	}

	public void setHistoryUpdateCount(BaseDataVariableType value) {
		historyUpdateCount = value;
	}

	public BaseDataVariableType getMaxResponseMessageSize() {
		return maxResponseMessageSize;
	}

	public void setMaxResponseMessageSize(BaseDataVariableType value) {
		maxResponseMessageSize = value;
	}

	public BaseDataVariableType getSetMonitoringModeCount() {
		return setMonitoringModeCount;
	}

	public void setSetMonitoringModeCount(BaseDataVariableType value) {
		setMonitoringModeCount = value;
	}

	public BaseDataVariableType getSetTriggeringCount() {
		return setTriggeringCount;
	}

	public void setSetTriggeringCount(BaseDataVariableType value) {
		setTriggeringCount = value;
	}

	public BaseDataVariableType getDeleteReferencesCount() {
		return deleteReferencesCount;
	}

	public void setDeleteReferencesCount(BaseDataVariableType value) {
		deleteReferencesCount = value;
	}

	public BaseDataVariableType getHistoryReadCount() {
		return historyReadCount;
	}

	public void setHistoryReadCount(BaseDataVariableType value) {
		historyReadCount = value;
	}

	public BaseDataVariableType getModifySubscriptionCount() {
		return modifySubscriptionCount;
	}

	public void setModifySubscriptionCount(BaseDataVariableType value) {
		modifySubscriptionCount = value;
	}

	public BaseDataVariableType getActualSessionTimeout() {
		return actualSessionTimeout;
	}

	public void setActualSessionTimeout(BaseDataVariableType value) {
		actualSessionTimeout = value;
	}

	public BaseDataVariableType getAddReferencesCount() {
		return addReferencesCount;
	}

	public void setAddReferencesCount(BaseDataVariableType value) {
		addReferencesCount = value;
	}

	public BaseDataVariableType getRepublishCount() {
		return republishCount;
	}

	public void setRepublishCount(BaseDataVariableType value) {
		republishCount = value;
	}

	public BaseDataVariableType getTotalRequestCount() {
		return totalRequestCount;
	}

	public void setTotalRequestCount(BaseDataVariableType value) {
		totalRequestCount = value;
	}

	public BaseDataVariableType getClientConnectionTime() {
		return clientConnectionTime;
	}

	public void setClientConnectionTime(BaseDataVariableType value) {
		clientConnectionTime = value;
	}

	public BaseDataVariableType getPublishCount() {
		return publishCount;
	}

	public void setPublishCount(BaseDataVariableType value) {
		publishCount = value;
	}

	public BaseDataVariableType getCurrentPublishRequestsInQueue() {
		return currentPublishRequestsInQueue;
	}

	public void setCurrentPublishRequestsInQueue(BaseDataVariableType value) {
		currentPublishRequestsInQueue = value;
	}

	public BaseDataVariableType getTranslateBrowsePathsToNodeIdsCount() {
		return translateBrowsePathsToNodeIdsCount;
	}

	public void setTranslateBrowsePathsToNodeIdsCount(BaseDataVariableType value) {
		translateBrowsePathsToNodeIdsCount = value;
	}

	public BaseDataVariableType getModifyMonitoredItemsCount() {
		return modifyMonitoredItemsCount;
	}

	public void setModifyMonitoredItemsCount(BaseDataVariableType value) {
		modifyMonitoredItemsCount = value;
	}

	public BaseDataVariableType getQueryFirstCount() {
		return queryFirstCount;
	}

	public void setQueryFirstCount(BaseDataVariableType value) {
		queryFirstCount = value;
	}

	public BaseDataVariableType getUnregisterNodesCount() {
		return unregisterNodesCount;
	}

	public void setUnregisterNodesCount(BaseDataVariableType value) {
		unregisterNodesCount = value;
	}

	public BaseDataVariableType getSessionName() {
		return sessionName;
	}

	public void setSessionName(BaseDataVariableType value) {
		sessionName = value;
	}

	public BaseDataVariableType getBrowseCount() {
		return browseCount;
	}

	public void setBrowseCount(BaseDataVariableType value) {
		browseCount = value;
	}

	public BaseDataVariableType getRegisterNodesCount() {
		return registerNodesCount;
	}

	public void setRegisterNodesCount(BaseDataVariableType value) {
		registerNodesCount = value;
	}

	public BaseDataVariableType getServerUri() {
		return serverUri;
	}

	public void setServerUri(BaseDataVariableType value) {
		serverUri = value;
	}

	@Override
	public String toString() {
		return "SessionDiagnosticsVariableType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
