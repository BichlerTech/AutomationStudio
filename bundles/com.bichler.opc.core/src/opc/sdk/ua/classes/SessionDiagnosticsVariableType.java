package opc.sdk.ua.classes;

import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.ServiceCounterDataType;
import org.opcfoundation.ua.core.SessionDiagnosticsDataType;

import opc.sdk.core.enums.ValueRanks;
import opc.sdk.ua.constants.NodeStateChangeMasks;

public class SessionDiagnosticsVariableType extends BaseDataVariableType<SessionDiagnosticsDataType> {
	protected BaseDataVariableType<NodeId> sessionId = null;
	protected BaseDataVariableType<String> sessionName = null;
	protected BaseDataVariableType<ApplicationDescription> clientDescription = null;
	protected BaseDataVariableType<String> serverUri = null;
	protected BaseDataVariableType<String> endpointUrl = null;
	protected BaseDataVariableType<String[]> localeIds = null;
	protected BaseDataVariableType<Double> actualSessionTimeout = null;
	protected BaseDataVariableType<UnsignedInteger> maxResponseMessageSize = null;
	protected BaseDataVariableType<DateTime> clientConnectionTime = null;
	protected BaseDataVariableType<DateTime> clientLastContactTime = null;
	protected BaseDataVariableType<UnsignedInteger> currentSubscriptionsCount = null;
	protected BaseDataVariableType<UnsignedInteger> currentMonitoredItemsCount = null;
	protected BaseDataVariableType<UnsignedInteger> currentPublishRequestsInQueue = null;
	protected BaseDataVariableType<ServiceCounterDataType> totalRequestCount = null;
	protected BaseDataVariableType<UnsignedInteger> unauthorizedRequestCount = null;
	protected BaseDataVariableType<ServiceCounterDataType> readCount = null;
	protected BaseDataVariableType<ServiceCounterDataType> historyReadCount = null;
	protected BaseDataVariableType<ServiceCounterDataType> writeCount = null;
	protected BaseDataVariableType<ServiceCounterDataType> historyUpdateCount = null;
	protected BaseDataVariableType<ServiceCounterDataType> callCount = null;
	protected BaseDataVariableType<ServiceCounterDataType> createMonitoredItemsCount = null;
	protected BaseDataVariableType<ServiceCounterDataType> modifyMonitoredItemsCount = null;
	protected BaseDataVariableType<ServiceCounterDataType> setMonitoringModeCount = null;
	protected BaseDataVariableType<ServiceCounterDataType> setTriggeringCount = null;
	protected BaseDataVariableType<ServiceCounterDataType> deleteMonitoredItemsCount = null;
	protected BaseDataVariableType<ServiceCounterDataType> createSubscriptionCount = null;
	protected BaseDataVariableType<ServiceCounterDataType> modifySubscriptionCount = null;
	protected BaseDataVariableType<ServiceCounterDataType> setPublishingModeCount = null;
	protected BaseDataVariableType<ServiceCounterDataType> publishCount = null;
	protected BaseDataVariableType<ServiceCounterDataType> republishCount = null;
	protected BaseDataVariableType<ServiceCounterDataType> transferSubscriptionsCount = null;
	protected BaseDataVariableType<ServiceCounterDataType> deleteSubscriptionsCount = null;
	protected BaseDataVariableType<ServiceCounterDataType> addNodesCount = null;
	protected BaseDataVariableType<ServiceCounterDataType> addReferencesCount = null;
	protected BaseDataVariableType<ServiceCounterDataType> deleteNodesCount = null;
	protected BaseDataVariableType<ServiceCounterDataType> deleteReferencesCount = null;
	protected BaseDataVariableType<ServiceCounterDataType> browseCount = null;
	protected BaseDataVariableType<ServiceCounterDataType> browseNextCount = null;
	protected BaseDataVariableType<ServiceCounterDataType> translateBrowsePathsToNodeIdsCount = null;
	protected BaseDataVariableType<ServiceCounterDataType> queryFirstCount = null;
	protected BaseDataVariableType<ServiceCounterDataType> queryNextCount = null;
	protected BaseDataVariableType<ServiceCounterDataType> registerNodesCount = null;
	protected BaseDataVariableType<ServiceCounterDataType> unregisterNodesCount = null;

	/**
	 * Initializes the type with its default attribute values.
	 * 
	 * @param parent
	 */
	public SessionDiagnosticsVariableType(BaseNode parent) {
		super(parent);
	}

	/**
	 * Returns the id of the default type definition node for the instance.
	 */
	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.SessionDiagnosticsVariableType;
	}

	/**
	 * SessionDiagnosticsDataType Id
	 */
	@Override
	protected NodeId getDefaultDataTypeId() {
		return Identifiers.SessionDiagnosticsDataType;
	}

	@Override
	protected int getDefaultValueRank() {
		return ValueRanks.Scalar.getValue();
	}

	/**
	 * Get a description for the SessionId Variable.
	 * 
	 * @return SessionId
	 */
	public BaseDataVariableType<NodeId> getSessionId() {
		return this.sessionId;
	}

	/**
	 * Set a description for the SessionId Variable.
	 * 
	 * @param Value SessionId variable
	 */
	public void setSessionId(BaseDataVariableType<NodeId> value) {
		if (this.sessionId != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.sessionId = value;
	}

	/**
	 * Get a description for the SessionName Variable.
	 * 
	 * @return SessionName
	 */
	public BaseDataVariableType<String> getSessionName() {
		return this.sessionName;
	}

	/**
	 * Set a description for the SessionName Variable.
	 * 
	 * @param Value SessionName value.
	 */
	public void setSessionName(BaseDataVariableType<String> value) {
		if (this.sessionName != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.sessionName = value;
	}

	/**
	 * Get the description for the ClientDescription Variable.
	 * 
	 * @return ClientDescription
	 */
	public BaseDataVariableType<ApplicationDescription> getClientDescription() {
		return this.clientDescription;
	}

	/**
	 * Set the description for the ClientDescription value.
	 * 
	 * @param Value ClientDescription Value.
	 */
	public void setClientDescription(BaseDataVariableType<ApplicationDescription> value) {
		if (this.clientDescription != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.clientDescription = value;
	}

	/**
	 * Get the description for the ClientDescription Variable.
	 * 
	 * @return ClientDescription
	 */
	public BaseDataVariableType<String> getServerUri() {
		return this.serverUri;
	}

	/**
	 * Set the description for the ServerUri value.
	 * 
	 * @param Value ServerUri Value.
	 */
	public void setServerUri(BaseDataVariableType<String> value) {
		if (this.serverUri != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.serverUri = value;
	}

	/**
	 * Get the description for the EndpointUrl Variable.
	 * 
	 * @return EndpointUrl
	 */
	public BaseDataVariableType<String> getEndpointUrl() {
		return this.endpointUrl;
	}

	/**
	 * Set the description for the Endpoint value.
	 * 
	 * @param Value EndpointUri Value.
	 */
	public void setEndpointUrl(BaseDataVariableType<String> value) {
		if (this.endpointUrl != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.endpointUrl = value;
	}

	/**
	 * Get the description for the LocaleIds Variable.
	 * 
	 * @return LocaleIds
	 */
	public BaseDataVariableType<String[]> getLocaleIds() {
		return this.localeIds;
	}

	/**
	 * Set the description for the LocaleIds value.
	 * 
	 * @param Value LocaleIds Value.
	 */
	public void setLocaleIds(BaseDataVariableType<String[]> value) {
		if (this.localeIds != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.localeIds = value;
	}

	/**
	 * Get the description for the ActualSessionTimeout Variable.
	 * 
	 * @return ActualSessionTimeout
	 */
	public BaseDataVariableType<Double> getActualSessionTimeout() {
		return this.actualSessionTimeout;
	}

	/**
	 * Set the description for the ActualSessionTimeout value.
	 * 
	 * @param Value ActualSessionTimeout Value.
	 */
	public void setActualSessionTimeout(BaseDataVariableType<Double> value) {
		if (this.actualSessionTimeout != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.actualSessionTimeout = value;
	}

	/**
	 * Get the description for the MaxResponseMessageSize Variable.
	 * 
	 * @return MaxResponseMessageSize
	 */
	public BaseDataVariableType<UnsignedInteger> getMaxResponseMessageSize() {
		return this.maxResponseMessageSize;
	}

	/**
	 * Set the description for the MaxResponseMessageSize value.
	 * 
	 * @param Value MaxResponseMessageSize Value.
	 */
	public void setMaxResponseMessageSize(BaseDataVariableType<UnsignedInteger> value) {
		if (this.maxResponseMessageSize != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.maxResponseMessageSize = value;
	}

	/**
	 * Get the description for the ClientConnectionTime Variable.
	 * 
	 * @return ClientConnectionTime
	 */
	public BaseDataVariableType<DateTime> getClientConnectionTime() {
		return this.clientConnectionTime;
	}

	/**
	 * Set the description for the ClientConnectionTime value.
	 * 
	 * @param Value ClientConnectionTime Value.
	 */
	public void setClientConnectionTime(BaseDataVariableType<DateTime> value) {
		if (this.clientConnectionTime != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.clientConnectionTime = value;
	}

	/**
	 * Get the description for the ClientLastContactTime Variable.
	 * 
	 * @return ClientLastContactTime
	 */
	public BaseDataVariableType<DateTime> getClientLastContactTime() {
		return this.clientLastContactTime;
	}

	/**
	 * Set the description for the ClientLastContactTime value.
	 * 
	 * @param Value ClientLastContactTime Value.
	 */
	public void setClientLastContactTime(BaseDataVariableType<DateTime> value) {
		if (this.clientLastContactTime != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.clientLastContactTime = value;
	}

	/**
	 * Get the description for the CurrentMonitoredItems Variable.
	 * 
	 * @return CurrentMonitoredItems
	 */
	public BaseDataVariableType<UnsignedInteger> getCurrentMonitoredItemsCount() {
		return this.currentMonitoredItemsCount;
	}

	/**
	 * Set the description for the CurrentMonitoredItems value.
	 * 
	 * @param Value CurrentMonitoredItems Value.
	 */
	public void setCurrentMonitoredItemsCount(BaseDataVariableType<UnsignedInteger> value) {
		if (this.currentMonitoredItemsCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.currentMonitoredItemsCount = value;
	}

	/**
	 * Get the description for the TotalRequestCount Variable.
	 * 
	 * @return TotalRequestCount
	 */
	public BaseDataVariableType<ServiceCounterDataType> getTotalRequestCount() {
		return this.totalRequestCount;
	}

	/**
	 * Set the description for the TotalRequestCount value.
	 * 
	 * @param Value TotalRequestCount Value.
	 */
	public void setTotalRequestCount(BaseDataVariableType<ServiceCounterDataType> value) {
		if (this.totalRequestCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.totalRequestCount = value;
	}

	/**
	 * Get the description for the CurrentSubscriptionsCount.
	 * 
	 * @return CurrentSubscriptionsCount
	 */
	public BaseDataVariableType<UnsignedInteger> getCurrentSubscriptionsCount() {
		return currentSubscriptionsCount;
	}

	/**
	 * Set a description for the CurrentSubscriptionsCount Variable.
	 * 
	 * @param Value CurrentSubscriptionsCount variable
	 */
	public void setCurrentSubscriptionsCount(BaseDataVariableType<UnsignedInteger> value) {
		if (this.currentSubscriptionsCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.currentSubscriptionsCount = value;
	}

	/**
	 * Get the description for the CurrentSubscriptionsCount.
	 * 
	 * @return CurrentSubscriptionsCount
	 */
	public BaseDataVariableType<UnsignedInteger> getCurrentPublishRequestsInQueue() {
		return currentPublishRequestsInQueue;
	}

	/**
	 * Set a description for the CurrentPublishRequestsInQueue Variable.
	 * 
	 * @param Value CurrentPublishRequetssInQueue variable
	 */
	public void setCurrentPublishRequestsInQueue(BaseDataVariableType<UnsignedInteger> value) {
		if (this.currentPublishRequestsInQueue != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.currentPublishRequestsInQueue = value;
	}

	/**
	 * Get the description for the UnauthorizedREquestCount.
	 * 
	 * @return UnauthorizedREquestCount
	 */
	public BaseDataVariableType<UnsignedInteger> getUnauthorizedRequestCount() {
		return unauthorizedRequestCount;
	}

	/**
	 * Set a description for the UnauthorizedRequestCount Variable.
	 * 
	 * @param Value UnauthorizedRequestCount variable
	 */
	public void setUnauthorizedRequestCount(BaseDataVariableType<UnsignedInteger> value) {
		if (this.unauthorizedRequestCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.unauthorizedRequestCount = value;
	}

	/**
	 * Get the description for the ReadCount.
	 * 
	 * @return ReadCount
	 */
	public BaseDataVariableType<ServiceCounterDataType> getReadCount() {
		return readCount;
	}

	/**
	 * Set a description for the ReadCount Variable.
	 * 
	 * @param Value ReadCount variable
	 */
	public void setReadCount(BaseDataVariableType<ServiceCounterDataType> value) {
		if (this.readCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.readCount = value;
	}

	/**
	 * Get the description for the HistoryReadCount.
	 * 
	 * @return HistoryReadCount
	 */
	public BaseDataVariableType<ServiceCounterDataType> getHistoryReadCount() {
		return historyReadCount;
	}

	/**
	 * Set a description for the HistoryReadCount Variable.
	 * 
	 * @param Value HistoryReadCount variable
	 */
	public void setHistoryReadCount(BaseDataVariableType<ServiceCounterDataType> value) {
		if (this.historyReadCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.historyReadCount = value;
	}

	/**
	 * Get the description for the WriteCount.
	 * 
	 * @return WriteCount
	 */
	public BaseDataVariableType<ServiceCounterDataType> getWriteCount() {
		return writeCount;
	}

	/**
	 * Set a description for the WriteCount Variable.
	 * 
	 * @param Value WriteCount variable
	 */
	public void setWriteCount(BaseDataVariableType<ServiceCounterDataType> value) {
		if (this.writeCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.writeCount = value;
	}

	/**
	 * Get the description for the HistoryUpdateCount.
	 * 
	 * @return HistoryUpdateCount
	 */
	public BaseDataVariableType<ServiceCounterDataType> getHistoryUpdateCount() {
		return historyUpdateCount;
	}

	/**
	 * Set a description for the HistoryUpdateCount Variable.
	 * 
	 * @param Value HistoryUpdateCount variable
	 */
	public void setHistoryUpdateCount(BaseDataVariableType<ServiceCounterDataType> value) {
		if (this.historyUpdateCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.historyUpdateCount = value;
	}

	/**
	 * Get the description for the CallCount.
	 * 
	 * @return CallCount
	 */
	public BaseDataVariableType<ServiceCounterDataType> getCallCount() {
		return callCount;
	}

	/**
	 * Set a description for the CallCount Variable.
	 * 
	 * @param Value CallCount variable
	 */
	public void setCallCount(BaseDataVariableType<ServiceCounterDataType> value) {
		if (this.callCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.callCount = value;
	}

	/**
	 * Get the description for the CreateMonitoredItemsCount.
	 * 
	 * @return CreateMonitoredItemsCount
	 */
	public BaseDataVariableType<ServiceCounterDataType> getCreateMonitoredItemsCount() {
		return createMonitoredItemsCount;
	}

	/**
	 * Set a description for the CreateMonitoredItemsCount Variable.
	 * 
	 * @param Value CreateMonitoredItemsCount variable
	 */
	public void setCreateMonitoredItemsCount(BaseDataVariableType<ServiceCounterDataType> value) {
		if (this.createMonitoredItemsCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.createMonitoredItemsCount = value;
	}

	/**
	 * Get the description for the ModifyMonitoredItemsCount.
	 * 
	 * @return ModifyMonitoredItemsCount
	 */
	public BaseDataVariableType<ServiceCounterDataType> getModifyMonitoredItemsCount() {
		return modifyMonitoredItemsCount;
	}

	/**
	 * Set a description for the ModifyMonitoredItemsCount Variable.
	 * 
	 * @param Value ModifyMonitoredItemsCount variable
	 */
	public void setModifyMonitoredItemsCount(BaseDataVariableType<ServiceCounterDataType> value) {
		if (this.modifyMonitoredItemsCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.modifyMonitoredItemsCount = value;
	}

	/**
	 * Get the description for the MonitoringModeCount.
	 * 
	 * @return MonitoringModeCount
	 */
	public BaseDataVariableType<ServiceCounterDataType> getSetMonitoringModeCount() {
		return setMonitoringModeCount;
	}

	/**
	 * Set a description for the SetMonitoringModeCount Variable.
	 * 
	 * @param Value SetMonitoringModeCount variable
	 */
	public void setSetMonitoringModeCount(BaseDataVariableType<ServiceCounterDataType> value) {
		if (this.setMonitoringModeCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.setMonitoringModeCount = value;
	}

	/**
	 * Get the description for the SetTriggeringCount.
	 * 
	 * @return SetTriggeringCount
	 */
	public BaseDataVariableType<ServiceCounterDataType> getSetTriggeringCount() {
		return setTriggeringCount;
	}

	/**
	 * Set a description for the SetTriggeringCount Variable.
	 * 
	 * @param Value SetTriggeringCount variable
	 */
	public void setSetTriggeringCount(BaseDataVariableType<ServiceCounterDataType> value) {
		if (this.setTriggeringCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.setTriggeringCount = value;
	}

	/**
	 * Get the description for the DeleteMonitoredItemsCount.
	 * 
	 * @return DeleteMonitoredItemsCount
	 */
	public BaseDataVariableType<ServiceCounterDataType> getDeleteMonitoredItemsCount() {
		return deleteMonitoredItemsCount;
	}

	/**
	 * Set a description for the DeleteMonitoredItemsCount Variable.
	 * 
	 * @param Value DeleteMonitoredItemsCount variable
	 */
	public void setDeleteMonitoredItemsCount(BaseDataVariableType<ServiceCounterDataType> value) {
		if (this.deleteMonitoredItemsCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.deleteMonitoredItemsCount = value;
	}

	/**
	 * Get the description for the CreateSubscriptionCount.
	 * 
	 * @return CreateSubscriptionCount
	 */
	public BaseDataVariableType<ServiceCounterDataType> getCreateSubscriptionCount() {
		return createSubscriptionCount;
	}

	/**
	 * Set a description for the CreateSubscriptionCount Variable.
	 * 
	 * @param Value CreateSubscriptionCount variable
	 */
	public void setCreateSubscriptionCount(BaseDataVariableType<ServiceCounterDataType> value) {
		if (this.createSubscriptionCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.createSubscriptionCount = value;
	}

	/**
	 * Get the description for the ModifySubscriptionCount.
	 * 
	 * @return CreateSubscriptionCount
	 */
	public BaseDataVariableType<ServiceCounterDataType> getModifySubscriptionCount() {
		return modifySubscriptionCount;
	}

	/**
	 * Set a description for the ModifySubscriptionCount Variable.
	 * 
	 * @param Value ModifySubscriptionCount variable
	 */
	public void setModifySubscriptionCount(BaseDataVariableType<ServiceCounterDataType> value) {
		if (this.modifySubscriptionCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.modifySubscriptionCount = value;
	}

	/**
	 * Get the description for the SetPublishingModeCount.
	 * 
	 * @return SetPublishingModeCount
	 */
	public BaseDataVariableType<ServiceCounterDataType> getSetPublishingModeCount() {
		return setPublishingModeCount;
	}

	/**
	 * Set a description for the SetPublishingModeCount Variable.
	 * 
	 * @param Value SetPublishingModeCount variable
	 */
	public void setSetPublishingModeCount(BaseDataVariableType<ServiceCounterDataType> value) {
		if (this.setPublishingModeCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.setPublishingModeCount = value;
	}

	/**
	 * Get the description for the PublishCount.
	 * 
	 * @return PublishCount
	 */
	public BaseDataVariableType<ServiceCounterDataType> getPublishCount() {
		return publishCount;
	}

	/**
	 * Set a description for the PublishCount Variable.
	 * 
	 * @param Value PublishCount variable
	 */
	public void setPublishCount(BaseDataVariableType<ServiceCounterDataType> value) {
		if (this.publishCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.publishCount = value;
	}

	/**
	 * Get the description for the RepublishCount.
	 * 
	 * @return RepublishCount
	 */
	public BaseDataVariableType<ServiceCounterDataType> getRepublishCount() {
		return republishCount;
	}

	/**
	 * Set a description for the RepublishCount Variable.
	 * 
	 * @param Value RepublishCount variable
	 */
	public void setRepublishCount(BaseDataVariableType<ServiceCounterDataType> value) {
		if (this.republishCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.republishCount = value;
	}

	/**
	 * Get the description for the TransferSubscriptionsCount.
	 * 
	 * @return TransferSubscriptionsCount
	 */
	public BaseDataVariableType<ServiceCounterDataType> getTransferSubscriptionsCount() {
		return transferSubscriptionsCount;
	}

	/**
	 * Set a description for the TransferSubscriptionsCount Variable.
	 * 
	 * @param Value TransferSubscriptionsCount variable
	 */
	public void setTransferSubscriptionsCount(BaseDataVariableType<ServiceCounterDataType> value) {
		if (this.transferSubscriptionsCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.transferSubscriptionsCount = value;
	}

	/**
	 * Get the description for the DeleteSubscriptionsCount.
	 * 
	 * @return DeleteSubscriptionsCount
	 */
	public BaseDataVariableType<ServiceCounterDataType> getDeleteSubscriptionsCount() {
		return deleteSubscriptionsCount;
	}

	/**
	 * Set a description for the DeleteSubscriptionsCount Variable.
	 * 
	 * @param Value DeleteSubscriptionsCount variable
	 */
	public void setDeleteSubscriptionsCount(BaseDataVariableType<ServiceCounterDataType> value) {
		if (this.deleteSubscriptionsCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.deleteSubscriptionsCount = value;
	}

	/**
	 * Get the description for the AddNodesCount.
	 * 
	 * @return AddNodesCount
	 */
	public BaseDataVariableType<ServiceCounterDataType> getAddNodesCount() {
		return addNodesCount;
	}

	/**
	 * Set a description for the AddNodesCount Variable.
	 * 
	 * @param Value AddNodesCount variable
	 */
	public void setAddNodesCount(BaseDataVariableType<ServiceCounterDataType> value) {
		if (this.addNodesCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.addNodesCount = value;
	}

	/**
	 * Get the description for the AddReferencesCount.
	 * 
	 * @return AddReferencesCount
	 */
	public BaseDataVariableType<ServiceCounterDataType> getAddReferencesCount() {
		return addReferencesCount;
	}

	/**
	 * Set a description for the AddReferencesCount Variable.
	 * 
	 * @param Value AddReferencesCount variable
	 */
	public void setAddReferencesCount(BaseDataVariableType<ServiceCounterDataType> value) {
		if (this.addReferencesCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.addReferencesCount = value;
	}

	/**
	 * Get the description for the DeleteNodesCount.
	 * 
	 * @return DeleteNodesCount
	 */
	public BaseDataVariableType<ServiceCounterDataType> getDeleteNodesCount() {
		return deleteNodesCount;
	}

	/**
	 * Set a description for the DeleteNodesCount Variable.
	 * 
	 * @param Value DeleteNodesCount variable
	 */
	public void setDeleteNodesCount(BaseDataVariableType<ServiceCounterDataType> value) {
		if (this.deleteNodesCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.deleteNodesCount = value;
	}

	/**
	 * Get the description for the DeleteReferencesCount.
	 * 
	 * @return DeleteReferencesCount
	 */
	public BaseDataVariableType<ServiceCounterDataType> getDeleteReferencesCount() {
		return deleteReferencesCount;
	}

	/**
	 * Set a description for the DeleteReferencesCount Variable.
	 * 
	 * @param Value DeleteReferencesCount variable
	 */
	public void setDeleteReferencesCount(BaseDataVariableType<ServiceCounterDataType> value) {
		if (this.deleteReferencesCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.deleteReferencesCount = value;
	}

	/**
	 * Get the description for the BrowseCount.
	 * 
	 * @return BrowseCount
	 */
	public BaseDataVariableType<ServiceCounterDataType> getBrowseCount() {
		return browseCount;
	}

	/**
	 * Set a description for the BrowseCount Variable.
	 * 
	 * @param Value BrowseCount variable
	 */
	public void setBrowseCount(BaseDataVariableType<ServiceCounterDataType> value) {
		if (this.browseCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.browseCount = value;
	}

	/**
	 * Get the description for the BrowseNextCount.
	 * 
	 * @return BrowseNextCount
	 */
	public BaseDataVariableType<ServiceCounterDataType> getBrowseNextCount() {
		return browseNextCount;
	}

	/**
	 * Set a description for the BrowseNextCount Variable.
	 * 
	 * @param Value BrowseNextCount variable
	 */
	public void setBrowseNextCount(BaseDataVariableType<ServiceCounterDataType> value) {
		if (this.browseNextCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.browseNextCount = value;
	}

	/**
	 * Get the description for the TranslateBrowsePathsToNodeIdsCount.
	 * 
	 * @return TranslateBrowsePathsToNodeIdsCount
	 */
	public BaseDataVariableType<ServiceCounterDataType> getTranslateBrowsePathsToNodeIdsCount() {
		return translateBrowsePathsToNodeIdsCount;
	}

	/**
	 * Set a description for the TranslateBrowsePathsToNodeIdsCount Variable.
	 * 
	 * @param Value TranslateBrowsePathsToNodeIdsCount variable
	 */
	public void setTranslateBrowsePathsToNodeIdsCount(BaseDataVariableType<ServiceCounterDataType> value) {
		if (this.translateBrowsePathsToNodeIdsCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.translateBrowsePathsToNodeIdsCount = value;
	}

	/**
	 * Get the description for the QueryFirstCount.
	 * 
	 * @return QueryFirstCount
	 */
	public BaseDataVariableType<ServiceCounterDataType> getQueryFirstCount() {
		return queryFirstCount;
	}

	/**
	 * Set a description for the QueryFirstCount Variable.
	 * 
	 * @param Value QueryFirstCount
	 */
	public void setQueryFirstCount(BaseDataVariableType<ServiceCounterDataType> value) {
		if (this.queryFirstCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.queryFirstCount = value;
	}

	/**
	 * Get the description for the QueryNextCount.
	 * 
	 * @return QueryNextCount
	 */
	public BaseDataVariableType<ServiceCounterDataType> getQueryNextCount() {
		return queryNextCount;
	}

	/**
	 * Set a description for the QueryNextCount Variable.
	 * 
	 * @param Value QueryNextCount variable
	 */
	public void setQueryNextCount(BaseDataVariableType<ServiceCounterDataType> value) {
		if (this.queryNextCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.queryNextCount = value;
	}

	/**
	 * Get the description for the RegisterNodesCount.
	 * 
	 * @return RegisterNodesCount
	 */
	public BaseDataVariableType<ServiceCounterDataType> getRegisterNodesCount() {
		return registerNodesCount;
	}

	/**
	 * Set a description for the RegisterNodesCount Variable.
	 * 
	 * @param Value RegisterNodesCount variable
	 */
	public void setRegisterNodesCount(BaseDataVariableType<ServiceCounterDataType> value) {
		if (this.registerNodesCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.registerNodesCount = value;
	}

	/**
	 * Get the description for the UnregisterNodesCount.
	 * 
	 * @return UnregisterNodesCount
	 */
	public BaseDataVariableType<ServiceCounterDataType> getUnregisterNodesCount() {
		return unregisterNodesCount;
	}

	/**
	 * Set a description for the UnregisterNodesCount Variable.
	 * 
	 * @param Value UnregisterNodesCount variable
	 */
	public void setUnregisterNodesCount(BaseDataVariableType<ServiceCounterDataType> value) {
		if (this.unregisterNodesCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.unregisterNodesCount = value;
	}
}
