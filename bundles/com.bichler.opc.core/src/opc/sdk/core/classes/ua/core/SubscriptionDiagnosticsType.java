package opc.sdk.core.classes.ua.core;

public class SubscriptionDiagnosticsType extends BaseDataVariableType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.SubscriptionDiagnosticsType;
	private BaseDataVariableType republishRequestCount;
	private BaseDataVariableType publishingEnabled;
	private BaseDataVariableType maxKeepAliveCount;
	private BaseDataVariableType disableCount;
	private BaseDataVariableType transferredToSameClientCount;
	private BaseDataVariableType sessionId;
	private BaseDataVariableType latePublishRequestCount;
	private BaseDataVariableType subscriptionId;
	private BaseDataVariableType currentKeepAliveCount;
	private BaseDataVariableType eventNotificationsCount;
	private BaseDataVariableType eventQueueOverFlowCount;
	private BaseDataVariableType notificationsCount;
	private BaseDataVariableType priority;
	private BaseDataVariableType dataChangeNotificationsCount;
	private BaseDataVariableType republishMessageCount;
	private BaseDataVariableType republishMessageRequestCount;
	private BaseDataVariableType maxNotificationsPerPublish;
	private BaseDataVariableType transferredToAltClientCount;
	private BaseDataVariableType nextSequenceNumber;
	private BaseDataVariableType publishingInterval;
	private BaseDataVariableType discardedMessageCount;
	private BaseDataVariableType monitoringQueueOverflowCount;
	private BaseDataVariableType unacknowledgedMessageCount;
	private BaseDataVariableType enableCount;
	private BaseDataVariableType modifyCount;
	private BaseDataVariableType monitoredItemCount;
	private BaseDataVariableType transferRequestCount;
	private BaseDataVariableType maxLifetimeCount;
	private BaseDataVariableType disabledMonitoredItemCount;
	private BaseDataVariableType currentLifetimeCount;
	private BaseDataVariableType publishRequestCount;

	public SubscriptionDiagnosticsType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.core.SubscriptionDiagnosticsDataType getValue() {
		return getVariant() != null
				? (org.opcfoundation.ua.core.SubscriptionDiagnosticsDataType) getVariant().getValue()
				: null;
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public BaseDataVariableType getRepublishRequestCount() {
		return republishRequestCount;
	}

	public void setRepublishRequestCount(BaseDataVariableType value) {
		republishRequestCount = value;
	}

	public BaseDataVariableType getPublishingEnabled() {
		return publishingEnabled;
	}

	public void setPublishingEnabled(BaseDataVariableType value) {
		publishingEnabled = value;
	}

	public BaseDataVariableType getMaxKeepAliveCount() {
		return maxKeepAliveCount;
	}

	public void setMaxKeepAliveCount(BaseDataVariableType value) {
		maxKeepAliveCount = value;
	}

	public BaseDataVariableType getDisableCount() {
		return disableCount;
	}

	public void setDisableCount(BaseDataVariableType value) {
		disableCount = value;
	}

	public BaseDataVariableType getTransferredToSameClientCount() {
		return transferredToSameClientCount;
	}

	public void setTransferredToSameClientCount(BaseDataVariableType value) {
		transferredToSameClientCount = value;
	}

	public BaseDataVariableType getSessionId() {
		return sessionId;
	}

	public void setSessionId(BaseDataVariableType value) {
		sessionId = value;
	}

	public BaseDataVariableType getLatePublishRequestCount() {
		return latePublishRequestCount;
	}

	public void setLatePublishRequestCount(BaseDataVariableType value) {
		latePublishRequestCount = value;
	}

	public BaseDataVariableType getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(BaseDataVariableType value) {
		subscriptionId = value;
	}

	public BaseDataVariableType getCurrentKeepAliveCount() {
		return currentKeepAliveCount;
	}

	public void setCurrentKeepAliveCount(BaseDataVariableType value) {
		currentKeepAliveCount = value;
	}

	public BaseDataVariableType getEventNotificationsCount() {
		return eventNotificationsCount;
	}

	public void setEventNotificationsCount(BaseDataVariableType value) {
		eventNotificationsCount = value;
	}

	public BaseDataVariableType getEventQueueOverFlowCount() {
		return eventQueueOverFlowCount;
	}

	public void setEventQueueOverFlowCount(BaseDataVariableType value) {
		eventQueueOverFlowCount = value;
	}

	public BaseDataVariableType getNotificationsCount() {
		return notificationsCount;
	}

	public void setNotificationsCount(BaseDataVariableType value) {
		notificationsCount = value;
	}

	public BaseDataVariableType getPriority() {
		return priority;
	}

	public void setPriority(BaseDataVariableType value) {
		priority = value;
	}

	public BaseDataVariableType getDataChangeNotificationsCount() {
		return dataChangeNotificationsCount;
	}

	public void setDataChangeNotificationsCount(BaseDataVariableType value) {
		dataChangeNotificationsCount = value;
	}

	public BaseDataVariableType getRepublishMessageCount() {
		return republishMessageCount;
	}

	public void setRepublishMessageCount(BaseDataVariableType value) {
		republishMessageCount = value;
	}

	public BaseDataVariableType getRepublishMessageRequestCount() {
		return republishMessageRequestCount;
	}

	public void setRepublishMessageRequestCount(BaseDataVariableType value) {
		republishMessageRequestCount = value;
	}

	public BaseDataVariableType getMaxNotificationsPerPublish() {
		return maxNotificationsPerPublish;
	}

	public void setMaxNotificationsPerPublish(BaseDataVariableType value) {
		maxNotificationsPerPublish = value;
	}

	public BaseDataVariableType getTransferredToAltClientCount() {
		return transferredToAltClientCount;
	}

	public void setTransferredToAltClientCount(BaseDataVariableType value) {
		transferredToAltClientCount = value;
	}

	public BaseDataVariableType getNextSequenceNumber() {
		return nextSequenceNumber;
	}

	public void setNextSequenceNumber(BaseDataVariableType value) {
		nextSequenceNumber = value;
	}

	public BaseDataVariableType getPublishingInterval() {
		return publishingInterval;
	}

	public void setPublishingInterval(BaseDataVariableType value) {
		publishingInterval = value;
	}

	public BaseDataVariableType getDiscardedMessageCount() {
		return discardedMessageCount;
	}

	public void setDiscardedMessageCount(BaseDataVariableType value) {
		discardedMessageCount = value;
	}

	public BaseDataVariableType getMonitoringQueueOverflowCount() {
		return monitoringQueueOverflowCount;
	}

	public void setMonitoringQueueOverflowCount(BaseDataVariableType value) {
		monitoringQueueOverflowCount = value;
	}

	public BaseDataVariableType getUnacknowledgedMessageCount() {
		return unacknowledgedMessageCount;
	}

	public void setUnacknowledgedMessageCount(BaseDataVariableType value) {
		unacknowledgedMessageCount = value;
	}

	public BaseDataVariableType getEnableCount() {
		return enableCount;
	}

	public void setEnableCount(BaseDataVariableType value) {
		enableCount = value;
	}

	public BaseDataVariableType getModifyCount() {
		return modifyCount;
	}

	public void setModifyCount(BaseDataVariableType value) {
		modifyCount = value;
	}

	public BaseDataVariableType getMonitoredItemCount() {
		return monitoredItemCount;
	}

	public void setMonitoredItemCount(BaseDataVariableType value) {
		monitoredItemCount = value;
	}

	public BaseDataVariableType getTransferRequestCount() {
		return transferRequestCount;
	}

	public void setTransferRequestCount(BaseDataVariableType value) {
		transferRequestCount = value;
	}

	public BaseDataVariableType getMaxLifetimeCount() {
		return maxLifetimeCount;
	}

	public void setMaxLifetimeCount(BaseDataVariableType value) {
		maxLifetimeCount = value;
	}

	public BaseDataVariableType getDisabledMonitoredItemCount() {
		return disabledMonitoredItemCount;
	}

	public void setDisabledMonitoredItemCount(BaseDataVariableType value) {
		disabledMonitoredItemCount = value;
	}

	public BaseDataVariableType getCurrentLifetimeCount() {
		return currentLifetimeCount;
	}

	public void setCurrentLifetimeCount(BaseDataVariableType value) {
		currentLifetimeCount = value;
	}

	public BaseDataVariableType getPublishRequestCount() {
		return publishRequestCount;
	}

	public void setPublishRequestCount(BaseDataVariableType value) {
		publishRequestCount = value;
	}

	@Override
	public String toString() {
		return "SubscriptionDiagnosticsType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
