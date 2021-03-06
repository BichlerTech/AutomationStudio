package opc.sdk.ua.classes;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.SubscriptionDiagnosticsDataType;

import opc.sdk.core.enums.ValueRanks;
import opc.sdk.ua.constants.BrowseNames;
import opc.sdk.ua.constants.NodeStateChangeMasks;

public class SubscriptionDiagnosticsType extends BaseDataVariableType<SubscriptionDiagnosticsDataType> {
	protected BaseDataVariableType<NodeId> sessionId = null;
	protected BaseDataVariableType<UnsignedInteger> subscriptionId = null;
	protected BaseDataVariableType<Byte> priority = null;
	protected BaseDataVariableType<Double> publishingInterval = null;
	protected BaseDataVariableType<UnsignedInteger> maxKeepAliveCount = null;
	protected BaseDataVariableType<UnsignedInteger> maxLifetimeCount = null;
	protected BaseDataVariableType<UnsignedInteger> maxNotificationsPerPublish = null;
	protected BaseDataVariableType<Boolean> publishingEnabled = null;
	protected BaseDataVariableType<UnsignedInteger> modifyCount = null;
	protected BaseDataVariableType<UnsignedInteger> enableCount = null;
	protected BaseDataVariableType<UnsignedInteger> disableCount = null;
	protected BaseDataVariableType<UnsignedInteger> republishRequestCount = null;
	protected BaseDataVariableType<UnsignedInteger> republishMessageRequestCount = null;
	protected BaseDataVariableType<UnsignedInteger> republishMessageCount = null;
	protected BaseDataVariableType<UnsignedInteger> transferRequestCount = null;
	protected BaseDataVariableType<UnsignedInteger> transferredToAltClientCount = null;
	protected BaseDataVariableType<UnsignedInteger> transferredToSameClientCount = null;
	protected BaseDataVariableType<UnsignedInteger> publishRequestCount = null;
	protected BaseDataVariableType<UnsignedInteger> dataChangeNotificationsCount = null;
	protected BaseDataVariableType<UnsignedInteger> eventNotificationsCount = null;
	protected BaseDataVariableType<UnsignedInteger> notificationsCount = null;
	protected BaseDataVariableType<UnsignedInteger> latePublishRequestCount = null;
	protected BaseDataVariableType<UnsignedInteger> currentKeepAliveCount = null;
	protected BaseDataVariableType<UnsignedInteger> currentLifetimeCount = null;
	protected BaseDataVariableType<UnsignedInteger> unacknowledgedMessageCount = null;
	protected BaseDataVariableType<UnsignedInteger> discardedMessageCount = null;
	protected BaseDataVariableType<UnsignedInteger> monitoredItemCount = null;
	protected BaseDataVariableType<UnsignedInteger> disabledMonitoredItemCount = null;
	protected BaseDataVariableType<UnsignedInteger> monitoringQueueOverflowCount = null;
	protected BaseDataVariableType<UnsignedInteger> nextSequenceNumber = null;
	protected BaseDataVariableType<UnsignedInteger> eventQueueOverFlowCount = null;

	/**
	 * Initializes the type with its default attribute values.
	 * 
	 * @param parent
	 */
	protected SubscriptionDiagnosticsType(BaseNode parent) {
		super(parent);
	}

	/**
	 * Returns the id of the default type definition node for the instance.
	 */
	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.SubscriptionDiagnosticsType;
	}

	/**
	 * Returns the id of the default data type node for the instance.
	 */
	@Override
	protected NodeId getDefaultDataTypeId() {
		return Identifiers.SubscriptionDiagnosticsDataType;
	}

	/**
	 * Returns the id of the default value rank for the instance.
	 */
	@Override
	protected int getDefaultValueRank() {
		return ValueRanks.Scalar.getValue();
	}

	/**
	 * Get the description for the SessionId Variable.
	 * 
	 * @return SessionId
	 */
	public BaseDataVariableType<NodeId> getSessionId() {
		return sessionId;
	}

	/**
	 * Set the description for the SessionId Variable
	 * 
	 * @param Value SessionId
	 */
	public void setSessionId(BaseDataVariableType<NodeId> value) {
		if (this.sessionId != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.sessionId = value;
	}

	/**
	 * Get the description for the SubscriptionId Variable.
	 * 
	 * @return SubscriptionId
	 */
	public BaseDataVariableType<UnsignedInteger> getSubscriptionId() {
		return subscriptionId;
	}

	/**
	 * Set the description for the SubscriptionId Variable.
	 * 
	 * @param Value SubscriptionId
	 */
	public void setSubscriptionId(BaseDataVariableType<UnsignedInteger> value) {
		if (this.subscriptionId != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.subscriptionId = value;
	}

	/**
	 * Get the description for the Priority Variable.
	 * 
	 * @return Priority
	 */
	public BaseDataVariableType<Byte> getPriority() {
		return priority;
	}

	/**
	 * Set the description for the Priority Variable.
	 * 
	 * @param Value Priority
	 */
	public void setPriority(BaseDataVariableType<Byte> value) {
		if (this.priority != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.priority = value;
	}

	/**
	 * Get the description for the PublishingInterval Variable.
	 * 
	 * @return PublishingInterval
	 */
	public BaseDataVariableType<Double> getPublishingInterval() {
		return publishingInterval;
	}

	/**
	 * Set the description for the PublishingInterval Variable.
	 * 
	 * @param Value PublishingInterval
	 */
	public void setPublishingInterval(BaseDataVariableType<Double> value) {
		if (this.publishingInterval != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.publishingInterval = value;
	}

	/**
	 * Get the description for the MaxKeepAliveCount Variable.
	 * 
	 * @return MaxKeepAliveCount
	 */
	public BaseDataVariableType<UnsignedInteger> getMaxKeepAliveCount() {
		return maxKeepAliveCount;
	}

	/**
	 * Set the description for the MaxKeepAliveCount Variable.
	 * 
	 * @param Value MaxKeepAliveCount
	 */
	public void setMaxKeepAliveCount(BaseDataVariableType<UnsignedInteger> value) {
		if (this.maxKeepAliveCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.maxKeepAliveCount = value;
	}

	/**
	 * Get the description for the MaxLifetimeCount Variable.
	 * 
	 * @return MaxLifetimeCount
	 */
	public BaseDataVariableType<UnsignedInteger> getMaxLifetimeCount() {
		return maxLifetimeCount;
	}

	/**
	 * Set the description for the MaxLifetimeCount Variable.
	 * 
	 * @param Value MaxLifetimeCount
	 */
	public void setMaxLifetimeCount(BaseDataVariableType<UnsignedInteger> value) {
		if (this.maxLifetimeCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.maxLifetimeCount = value;
	}

	/**
	 * Get the description for the MaxNotificationsPerPublish Variable.
	 * 
	 * @return MaxNotificationsPerPublish
	 */
	public BaseDataVariableType<UnsignedInteger> getMaxNotificationsPerPublish() {
		return maxNotificationsPerPublish;
	}

	/**
	 * Set the description for the MaxNotificationsPerPublish Variable.
	 * 
	 * @param Value MaxNotificationsPerPublish
	 */
	public void setMaxNotificationsPerPublish(BaseDataVariableType<UnsignedInteger> value) {
		if (this.maxNotificationsPerPublish != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.maxNotificationsPerPublish = value;
	}

	/**
	 * Get the description for the PublishingEnabled Variable.
	 * 
	 * @return PublishingEnabled
	 */
	public BaseDataVariableType<Boolean> getPublishingEnabled() {
		return publishingEnabled;
	}

	/**
	 * Set the description for the PublishingEnabled Variable.
	 * 
	 * @param Value PublishingEnabled
	 */
	public void setPublishingEnabled(BaseDataVariableType<Boolean> value) {
		if (this.publishingEnabled != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.publishingEnabled = value;
	}

	/**
	 * Get the description for the ModifyCount Variable.
	 * 
	 * @return ModifyCount
	 */
	public BaseDataVariableType<UnsignedInteger> getModifyCount() {
		return modifyCount;
	}

	/**
	 * Set the description for the ModifyCount Variable.
	 * 
	 * @param Value ModifyCount
	 */
	public void setModifyCount(BaseDataVariableType<UnsignedInteger> value) {
		if (this.modifyCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.modifyCount = value;
	}

	/**
	 * Get the description for the EnableCount Variable.
	 * 
	 * @return EnableCount
	 */
	public BaseDataVariableType<UnsignedInteger> getEnableCount() {
		return enableCount;
	}

	/**
	 * Set the description for the EnableCount Variable.
	 * 
	 * @param Value EnableCount
	 */
	public void setEnableCount(BaseDataVariableType<UnsignedInteger> value) {
		if (this.enableCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.enableCount = value;
	}

	/**
	 * Get the description for the DisableCount Variable.
	 * 
	 * @return DisableCount
	 */
	public BaseDataVariableType<UnsignedInteger> getDisableCount() {
		return disableCount;
	}

	/**
	 * Set the description for the DisableCount Variable.
	 * 
	 * @param Value DisableCount
	 */
	public void setDisableCount(BaseDataVariableType<UnsignedInteger> value) {
		if (this.disableCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.disableCount = value;
	}

	/**
	 * Get the description for the RepublishRequestCount Variable.
	 * 
	 * @return RepublishRequestCount
	 */
	public BaseDataVariableType<UnsignedInteger> getRepublishRequestCount() {
		return republishRequestCount;
	}

	/**
	 * Set the description for the RepublishRequestCount Variable.
	 * 
	 * @param Value RepubishRequestCount
	 */
	public void setRepublishRequestCount(BaseDataVariableType<UnsignedInteger> value) {
		if (this.republishRequestCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.republishRequestCount = value;
	}

	/**
	 * Get the description for the RepublishMessageRequestCount Variable.
	 * 
	 * @return RepublsihMessageRequestCount
	 */
	public BaseDataVariableType<UnsignedInteger> getRepublishMessageRequestCount() {
		return republishMessageRequestCount;
	}

	/**
	 * Set the description for the RepublishMessageRequestCount Variable.
	 * 
	 * @param Value RepublishMessageRequestCount
	 */
	public void setRepublishMessageRequestCount(BaseDataVariableType<UnsignedInteger> value) {
		if (this.republishMessageRequestCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.republishMessageRequestCount = value;
	}

	/**
	 * Get the description for the RepublishMessageCount Variable.
	 * 
	 * @return RepublishMessageCount
	 */
	public BaseDataVariableType<UnsignedInteger> getRepublishMessageCount() {
		return republishMessageCount;
	}

	/**
	 * Set the description for the RepublishMessageCount Variable.
	 * 
	 * @param Value RepublishMessageCount
	 */
	public void setRepublishMessageCount(BaseDataVariableType<UnsignedInteger> value) {
		if (this.republishMessageCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.republishMessageCount = value;
	}

	/**
	 * Get the description for the TransferRequestCount Variable.
	 * 
	 * @return TransferRequestCount
	 */
	public BaseDataVariableType<UnsignedInteger> getTransferRequestCount() {
		return transferRequestCount;
	}

	/**
	 * Set the description for the TransferRequestCount Variable.
	 * 
	 * @param Value TransferRequestCount
	 */
	public void setTransferRequestCount(BaseDataVariableType<UnsignedInteger> value) {
		if (this.transferRequestCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.transferRequestCount = value;
	}

	/**
	 * Get the description for the TransferredToAltClientCount Variable.
	 * 
	 * @return TransferredToAltClientCount
	 */
	public BaseDataVariableType<UnsignedInteger> getTransferredToAltClientCount() {
		return transferredToAltClientCount;
	}

	/**
	 * Set the description for the TransferredToAltClientCount Variable.
	 * 
	 * @param Value TransferredToAltClientCount
	 */
	public void setTransferredToAltClientCount(BaseDataVariableType<UnsignedInteger> value) {
		if (this.transferredToAltClientCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.transferredToAltClientCount = value;
	}

	/**
	 * Get the description for the TransferredToSameClientCount Variable.
	 * 
	 * @return TransferredToSameClientCount
	 */
	public BaseDataVariableType<UnsignedInteger> getTransferredToSameClientCount() {
		return transferredToSameClientCount;
	}

	/**
	 * Set the description for the TransferredToSameClientCount Variable.
	 * 
	 * @param Value TransferredToSameClientCount
	 */
	public void setTransferredToSameClientCount(BaseDataVariableType<UnsignedInteger> value) {
		if (this.transferredToSameClientCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.transferredToSameClientCount = value;
	}

	/**
	 * Get the description for the PublishRequestCount Variable.
	 * 
	 * @return PublishRequestCount
	 */
	public BaseDataVariableType<UnsignedInteger> getPublishRequestCount() {
		return publishRequestCount;
	}

	/**
	 * Set the description for the PublishRequestCount Variable.
	 * 
	 * @param Value PublishRequestCount
	 */
	public void setPublishRequestCount(BaseDataVariableType<UnsignedInteger> value) {
		if (this.publishRequestCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.publishRequestCount = value;
	}

	/**
	 * Get the description for the DataChangeNotificationsCount Variable.
	 * 
	 * @return DataChangeNotificationsCount
	 */
	public BaseDataVariableType<UnsignedInteger> getDataChangeNotificationsCount() {
		return dataChangeNotificationsCount;
	}

	/**
	 * Set the description for the DataChangeNotificationsCount Variable.
	 * 
	 * @param Value DataChangeNotificationsCount
	 */
	public void setDataChangeNotificationsCount(BaseDataVariableType<UnsignedInteger> value) {
		if (this.dataChangeNotificationsCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.dataChangeNotificationsCount = value;
	}

	/**
	 * Get the description for the EventNotificationsCount Variable.
	 * 
	 * @return EventNotificationsCount
	 */
	public BaseDataVariableType<UnsignedInteger> getEventNotificationsCount() {
		return eventNotificationsCount;
	}

	/**
	 * Set the description for the EventNotificationsCount Variable.
	 * 
	 * @param Value EventNotificationsCount
	 */
	public void setEventNotificationsCount(BaseDataVariableType<UnsignedInteger> value) {
		if (this.eventNotificationsCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.eventNotificationsCount = value;
	}

	/**
	 * Get the description for the NotificationsCount Variable.
	 * 
	 * @return NotificationsCount
	 */
	public BaseDataVariableType<UnsignedInteger> getNotificationsCount() {
		return notificationsCount;
	}

	/**
	 * Set the description for the NotificationsCount Variable.
	 * 
	 * @param Value NotificationsCount
	 */
	public void setNotificationsCount(BaseDataVariableType<UnsignedInteger> value) {
		if (this.notificationsCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.notificationsCount = value;
	}

	/**
	 * Get the description for the LatePublishRequestCount Variable.
	 * 
	 * @return LatePublishRequestCount
	 */
	public BaseDataVariableType<UnsignedInteger> getLatePublishRequestCount() {
		return latePublishRequestCount;
	}

	/**
	 * Set the description for the LatePublishRequestCount Variable.
	 * 
	 * @param Value LatePublishRequestCount
	 */
	public void setLatePublishRequestCount(BaseDataVariableType<UnsignedInteger> value) {
		if (this.latePublishRequestCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.latePublishRequestCount = value;
	}

	/**
	 * Get the description for the CurrentKeepAliveCount Variable.
	 * 
	 * @return CurrentKeepAliveCount
	 */
	public BaseDataVariableType<UnsignedInteger> getCurrentKeepAliveCount() {
		return currentKeepAliveCount;
	}

	/**
	 * Set the description for the CurrentKeepAliveCount Variable.
	 * 
	 * @param Value CurrentKeepAliveCount
	 */
	public void setCurrentKeepAliveCount(BaseDataVariableType<UnsignedInteger> value) {
		if (this.currentKeepAliveCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.currentKeepAliveCount = value;
	}

	/**
	 * Get the description for the CurrentLifetimeCount Variable.
	 * 
	 * @return CurrentLifetimeCount
	 */
	public BaseDataVariableType<UnsignedInteger> getCurrentLifetimeCount() {
		return currentLifetimeCount;
	}

	/**
	 * Set the description for the CurrentLifetimeCount Variable.
	 * 
	 * @param Value CurrentLifetimeCount
	 */
	public void setCurrentLifetimeCount(BaseDataVariableType<UnsignedInteger> value) {
		if (this.currentLifetimeCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.currentLifetimeCount = value;
	}

	/**
	 * Get the description for the UnacknowledgedMessageCount Variable.
	 * 
	 * @return UnacknowledgedMessageCount
	 */
	public BaseDataVariableType<UnsignedInteger> getUnacknowledgedMessageCount() {
		return unacknowledgedMessageCount;
	}

	/**
	 * Set the description for the UnacknowledgedMessageCount Variable.
	 * 
	 * @param Value UnacknowledgedMessageCount
	 */
	public void setUnacknowledgedMessageCount(BaseDataVariableType<UnsignedInteger> value) {
		if (this.unacknowledgedMessageCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.unacknowledgedMessageCount = value;
	}

	/**
	 * Get the description for the DiscardedMessageCount Variable.
	 * 
	 * @return DiscardedMessageCount
	 */
	public BaseDataVariableType<UnsignedInteger> getDiscardedMessageCount() {
		return discardedMessageCount;
	}

	/**
	 * Set the description for the DiscardedMessageCount Variable.
	 * 
	 * @param Value DiscardedMessageCount
	 */
	public void setDiscardedMessageCount(BaseDataVariableType<UnsignedInteger> value) {
		if (this.discardedMessageCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.discardedMessageCount = value;
	}

	/**
	 * Get the description for the MonitoredItemCount Variable.
	 * 
	 * @return MonitoredItemCount
	 */
	public BaseDataVariableType<UnsignedInteger> getMonitoredItemCount() {
		return monitoredItemCount;
	}

	/**
	 * Set the description for the MonitoredItemCount Variable.
	 * 
	 * @param Value MonitoredItemCount
	 */
	public void setMonitoredItemCount(BaseDataVariableType<UnsignedInteger> value) {
		if (this.monitoredItemCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.monitoredItemCount = value;
	}

	/**
	 * Get the description for the DisabledMonitoredItemCount Variable.
	 * 
	 * @return DisabledMonitoredItemCount
	 */
	public BaseDataVariableType<UnsignedInteger> getDisabledMonitoredItemCount() {
		return disabledMonitoredItemCount;
	}

	/**
	 * Set the description for the DisabledMonitoredItemCount Variable.
	 * 
	 * @param Value DisabledMonitoredItemCount
	 */
	public void setDisabledMonitoredItemCount(BaseDataVariableType<UnsignedInteger> value) {
		if (this.disabledMonitoredItemCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.disabledMonitoredItemCount = value;
	}

	/**
	 * Get the description for the MonitoringQueueOverflowCount Variable.
	 * 
	 * @return MonitoringQueueOverflowCount
	 */
	public BaseDataVariableType<UnsignedInteger> getMonitoringQueueOverflowCount() {
		return monitoringQueueOverflowCount;
	}

	/**
	 * Set the description for the MonitoringQueueOverflowCount Variable.
	 * 
	 * @param Value MonitoringQueueOverflowCount
	 */
	public void setMonitoringQueueOverflowCount(BaseDataVariableType<UnsignedInteger> value) {
		if (this.monitoringQueueOverflowCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.monitoringQueueOverflowCount = value;
	}

	/**
	 * Get the description for the NextSequenceNumber Variable.
	 * 
	 * @return NextSequenceNumber
	 */
	public BaseDataVariableType<UnsignedInteger> getNextSequenceNumber() {
		return nextSequenceNumber;
	}

	/**
	 * Set the description for the NextSequenceNumber Variable.
	 * 
	 * @param Value NextSequenceNumber
	 */
	public void setNextSequenceNumber(BaseDataVariableType<UnsignedInteger> value) {
		if (this.nextSequenceNumber != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.nextSequenceNumber = value;
	}

	/**
	 * Get the description for the EventQueueOverFlowCount Variable.
	 * 
	 * @return EventQueueOverFlowCount
	 */
	public BaseDataVariableType<UnsignedInteger> getEventQueueOverFlowCount() {
		return eventQueueOverFlowCount;
	}

	/**
	 * Set the description for the EventQueueOverFlowCount Variable.
	 * 
	 * @param Value EventQueueOverFlowCount
	 */
	public void setEventQueueOverFlowCount(BaseDataVariableType<UnsignedInteger> value) {
		if (this.eventQueueOverFlowCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.eventQueueOverFlowCount = value;
	}

	/**
	 * Populates a list with the children that belong to the node.
	 */
	@Override
	public List<BaseInstance> getChildren() {
		List<BaseInstance> children = new ArrayList<>();
		if (this.sessionId != null)
			children.add(this.sessionId);
		if (this.subscriptionId != null)
			children.add(this.subscriptionId);
		if (this.priority != null)
			children.add(this.priority);
		if (this.publishingInterval != null)
			children.add(this.publishingInterval);
		if (this.maxKeepAliveCount != null) {
			children.add(this.maxKeepAliveCount);
		}
		if (this.maxLifetimeCount != null) {
			children.add(this.maxLifetimeCount);
		}
		if (this.maxNotificationsPerPublish != null) {
			children.add(this.maxNotificationsPerPublish);
		}
		if (this.publishingEnabled != null) {
			children.add(this.publishingEnabled);
		}
		if (this.modifyCount != null) {
			children.add(this.modifyCount);
		}
		if (this.enableCount != null) {
			children.add(this.enableCount);
		}
		if (this.disableCount != null) {
			children.add(this.disableCount);
		}
		if (this.republishRequestCount != null) {
			children.add(this.republishRequestCount);
		}
		if (this.republishMessageRequestCount != null) {
			children.add(this.republishMessageRequestCount);
		}
		if (this.republishMessageCount != null) {
			children.add(this.republishMessageCount);
		}
		if (this.transferRequestCount != null) {
			children.add(this.transferRequestCount);
		}
		if (this.publishRequestCount != null) {
			children.add(this.publishRequestCount);
		}
		if (this.dataChangeNotificationsCount != null) {
			children.add(this.dataChangeNotificationsCount);
		}
		if (this.eventNotificationsCount != null) {
			children.add(this.eventNotificationsCount);
		}
		if (this.notificationsCount != null) {
			children.add(this.notificationsCount);
		}
		if (this.latePublishRequestCount != null) {
			children.add(this.latePublishRequestCount);
		}
		if (this.currentKeepAliveCount != null) {
			children.add(this.currentKeepAliveCount);
		}
		if (this.currentLifetimeCount != null) {
			children.add(this.currentLifetimeCount);
		}
		if (this.unacknowledgedMessageCount != null) {
			children.add(this.unacknowledgedMessageCount);
		}
		if (this.discardedMessageCount != null) {
			children.add(this.discardedMessageCount);
		}
		if (this.monitoredItemCount != null) {
			children.add(this.monitoredItemCount);
		}
		if (this.disabledMonitoredItemCount != null) {
			children.add(this.disabledMonitoredItemCount);
		}
		if (this.monitoringQueueOverflowCount != null) {
			children.add(this.monitoringQueueOverflowCount);
		}
		if (this.nextSequenceNumber != null) {
			children.add(this.nextSequenceNumber);
		}
		if (this.eventQueueOverFlowCount != null) {
			children.add(this.eventQueueOverFlowCount);
		}
		children.addAll(super.getChildren());
		return children;
	}

	private BaseDataVariableType<NodeId> creatSessionId(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.sessionId == null) {
			if (replacement == null) {
				this.sessionId = new BaseDataVariableType<>(this);
			} else {
				this.sessionId = (BaseDataVariableType<NodeId>) replacement;
			}
		}
		return this.sessionId;
	}

	private BaseDataVariableType<UnsignedInteger> creatSubscriptionId(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.subscriptionId == null) {
			if (replacement == null) {
				this.subscriptionId = new BaseDataVariableType<>(this);
			} else {
				this.subscriptionId = (BaseDataVariableType<UnsignedInteger>) replacement;
			}
		}
		return this.subscriptionId;
	}

	private BaseDataVariableType<Byte> creatPriority(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.priority == null) {
			if (replacement == null) {
				this.priority = new BaseDataVariableType<>(this);
			} else {
				this.priority = (BaseDataVariableType<Byte>) replacement;
			}
		}
		return this.priority;
	}

	private BaseDataVariableType<Double> creatPublishingInterval(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.publishingInterval == null) {
			if (replacement == null) {
				this.publishingInterval = new BaseDataVariableType<>(this);
			} else {
				this.publishingInterval = (BaseDataVariableType<Double>) replacement;
			}
		}
		return this.publishingInterval;
	}

	private BaseDataVariableType<UnsignedInteger> creatMaxKeepAliveCount(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.maxKeepAliveCount == null) {
			if (replacement == null) {
				this.maxKeepAliveCount = new BaseDataVariableType<>(this);
			} else {
				this.maxKeepAliveCount = (BaseDataVariableType<UnsignedInteger>) replacement;
			}
		}
		return this.maxKeepAliveCount;
	}

	private BaseDataVariableType<UnsignedInteger> creatMaxLiveTimeCount(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.maxLifetimeCount == null) {
			if (replacement == null) {
				this.maxLifetimeCount = new BaseDataVariableType<>(this);
			} else {
				this.maxLifetimeCount = (BaseDataVariableType<UnsignedInteger>) replacement;
			}
		}
		return this.maxLifetimeCount;
	}

	private BaseDataVariableType<UnsignedInteger> creatMaxNotificationPerPublish(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.maxNotificationsPerPublish == null) {
			if (replacement == null) {
				this.maxNotificationsPerPublish = new BaseDataVariableType<>(this);
			} else {
				this.maxNotificationsPerPublish = (BaseDataVariableType<UnsignedInteger>) replacement;
			}
		}
		return this.maxNotificationsPerPublish;
	}

	private BaseDataVariableType<Boolean> creatPublishingEnabled(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.publishingEnabled == null) {
			if (replacement == null) {
				this.publishingEnabled = new BaseDataVariableType<>(this);
			} else {
				this.publishingEnabled = (BaseDataVariableType<Boolean>) replacement;
			}
		}
		return this.publishingEnabled;
	}

	private BaseDataVariableType<UnsignedInteger> creatModifyCount(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.modifyCount == null) {
			if (replacement == null) {
				this.modifyCount = new BaseDataVariableType<>(this);
			} else {
				this.modifyCount = (BaseDataVariableType<UnsignedInteger>) replacement;
			}
		}
		return this.modifyCount;
	}

	private BaseDataVariableType<UnsignedInteger> createEnableCount(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.enableCount == null) {
			if (replacement == null) {
				this.enableCount = new BaseDataVariableType<>(this);
			} else {
				this.enableCount = (BaseDataVariableType<UnsignedInteger>) replacement;
			}
		}
		return this.enableCount;
	}

	private BaseDataVariableType<UnsignedInteger> createDisableCount(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.disableCount == null) {
			if (replacement == null) {
				this.disableCount = new BaseDataVariableType<>(this);
			} else {
				this.disableCount = (BaseDataVariableType<UnsignedInteger>) replacement;
			}
		}
		return this.disableCount;
	}

	private BaseDataVariableType<UnsignedInteger> createRepublishRequestCount(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.republishRequestCount == null) {
			if (replacement == null) {
				this.republishRequestCount = new BaseDataVariableType<>(this);
			} else {
				this.republishRequestCount = (BaseDataVariableType<UnsignedInteger>) replacement;
			}
		}
		return this.republishRequestCount;
	}

	private BaseDataVariableType<UnsignedInteger> createRepublishMessageRequestCount(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.republishMessageRequestCount == null) {
			if (replacement == null) {
				this.republishMessageRequestCount = new BaseDataVariableType<>(this);
			} else {
				this.republishMessageRequestCount = (BaseDataVariableType<UnsignedInteger>) replacement;
			}
		}
		return this.republishMessageRequestCount;
	}

	private BaseDataVariableType<UnsignedInteger> createRepublishMessageCount(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.republishMessageCount == null) {
			if (replacement == null) {
				this.republishMessageCount = new BaseDataVariableType<>(this);
			} else {
				this.republishMessageCount = (BaseDataVariableType<UnsignedInteger>) replacement;
			}
		}
		return this.republishMessageCount;
	}

	private BaseDataVariableType<UnsignedInteger> createTransferRequestCount(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.transferRequestCount == null) {
			if (replacement == null) {
				this.transferRequestCount = new BaseDataVariableType<>(this);
			} else {
				this.transferRequestCount = (BaseDataVariableType<UnsignedInteger>) replacement;
			}
		}
		return this.transferRequestCount;
	}

	private BaseDataVariableType<UnsignedInteger> createTransferredToAltClientCount(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.transferredToAltClientCount == null) {
			if (replacement == null) {
				this.transferredToAltClientCount = new BaseDataVariableType<>(this);
			} else {
				this.transferredToAltClientCount = (BaseDataVariableType<UnsignedInteger>) replacement;
			}
		}
		return this.transferredToAltClientCount;
	}

	private BaseDataVariableType<UnsignedInteger> createTransferredToSampleClientCount(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.transferredToSameClientCount == null) {
			if (replacement == null) {
				this.transferredToSameClientCount = new BaseDataVariableType<>(this);
			} else {
				this.transferredToSameClientCount = (BaseDataVariableType<UnsignedInteger>) replacement;
			}
		}
		return this.transferredToSameClientCount;
	}

	private BaseDataVariableType<UnsignedInteger> createPublishRequestCount(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.publishRequestCount == null) {
			if (replacement == null) {
				this.publishRequestCount = new BaseDataVariableType<>(this);
			} else {
				this.publishRequestCount = (BaseDataVariableType<UnsignedInteger>) replacement;
			}
		}
		return this.publishRequestCount;
	}

	private BaseDataVariableType<UnsignedInteger> createDataChangeNotificationsCount(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.dataChangeNotificationsCount == null) {
			if (replacement == null) {
				this.dataChangeNotificationsCount = new BaseDataVariableType<>(this);
			} else {
				this.dataChangeNotificationsCount = (BaseDataVariableType<UnsignedInteger>) replacement;
			}
		}
		return this.dataChangeNotificationsCount;
	}

	private BaseDataVariableType<UnsignedInteger> createEventNotificationsCount(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.eventNotificationsCount == null) {
			if (replacement == null) {
				this.eventNotificationsCount = new BaseDataVariableType<>(this);
			} else {
				this.eventNotificationsCount = (BaseDataVariableType<UnsignedInteger>) replacement;
			}
		}
		return this.eventNotificationsCount;
	}

	private BaseDataVariableType<UnsignedInteger> createNotificationsCount(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.notificationsCount == null) {
			if (replacement == null) {
				this.notificationsCount = new BaseDataVariableType<>(this);
			} else {
				this.notificationsCount = (BaseDataVariableType<UnsignedInteger>) replacement;
			}
		}
		return this.notificationsCount;
	}

	private BaseDataVariableType<UnsignedInteger> createLatePublishRequestCount(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.latePublishRequestCount == null) {
			if (replacement == null) {
				this.latePublishRequestCount = new BaseDataVariableType<>(this);
			} else {
				this.latePublishRequestCount = (BaseDataVariableType<UnsignedInteger>) replacement;
			}
		}
		return this.latePublishRequestCount;
	}

	private BaseDataVariableType<UnsignedInteger> createCurrentKeepAliveCount(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.currentKeepAliveCount == null) {
			if (replacement == null) {
				this.currentKeepAliveCount = new BaseDataVariableType<>(this);
			} else {
				this.currentKeepAliveCount = (BaseDataVariableType<UnsignedInteger>) replacement;
			}
		}
		return this.currentKeepAliveCount;
	}

	private BaseDataVariableType<UnsignedInteger> createCurrentLifetimeCount(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.currentLifetimeCount == null) {
			if (replacement == null) {
				this.currentLifetimeCount = new BaseDataVariableType<>(this);
			} else {
				this.currentLifetimeCount = (BaseDataVariableType<UnsignedInteger>) replacement;
			}
		}
		return this.currentLifetimeCount;
	}

	private BaseDataVariableType<UnsignedInteger> createUnacknowledgedMessageCount(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.unacknowledgedMessageCount == null) {
			if (replacement == null) {
				this.unacknowledgedMessageCount = new BaseDataVariableType<>(this);
			} else {
				this.unacknowledgedMessageCount = (BaseDataVariableType<UnsignedInteger>) replacement;
			}
		}
		return this.unacknowledgedMessageCount;
	}

	private BaseDataVariableType<UnsignedInteger> createDiscardedMessageCount(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.discardedMessageCount == null) {
			if (replacement == null) {
				this.discardedMessageCount = new BaseDataVariableType<>(this);
			} else {
				this.discardedMessageCount = (BaseDataVariableType<UnsignedInteger>) replacement;
			}
		}
		return this.discardedMessageCount;
	}

	private BaseDataVariableType<UnsignedInteger> createMonitoredItemCount(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.monitoredItemCount == null) {
			if (replacement == null) {
				this.monitoredItemCount = new BaseDataVariableType<>(this);
			} else {
				this.monitoredItemCount = (BaseDataVariableType<UnsignedInteger>) replacement;
			}
		}
		return this.monitoredItemCount;
	}

	private BaseDataVariableType<UnsignedInteger> createDisabledMonitoredItemCount(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.disabledMonitoredItemCount == null) {
			if (replacement == null) {
				this.disabledMonitoredItemCount = new BaseDataVariableType<>(this);
			} else {
				this.disabledMonitoredItemCount = (BaseDataVariableType<UnsignedInteger>) replacement;
			}
		}
		return this.disabledMonitoredItemCount;
	}

	private BaseDataVariableType<UnsignedInteger> createMonitoringQueueOverflowCount(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.monitoringQueueOverflowCount == null) {
			if (replacement == null) {
				this.monitoringQueueOverflowCount = new BaseDataVariableType<>(this);
			} else {
				this.monitoringQueueOverflowCount = (BaseDataVariableType<UnsignedInteger>) replacement;
			}
		}
		return this.monitoringQueueOverflowCount;
	}

	private BaseDataVariableType<UnsignedInteger> createNextSequenceNumber(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.nextSequenceNumber == null) {
			if (replacement == null) {
				this.nextSequenceNumber = new BaseDataVariableType<>(this);
			} else {
				this.nextSequenceNumber = (BaseDataVariableType<UnsignedInteger>) replacement;
			}
		}
		return this.nextSequenceNumber;
	}

	private BaseDataVariableType<UnsignedInteger> createEventQueueOverFlowCount(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.eventQueueOverFlowCount == null) {
			if (replacement == null) {
				this.eventQueueOverFlowCount = new BaseDataVariableType<>(this);
			} else {
				this.eventQueueOverFlowCount = (BaseDataVariableType<UnsignedInteger>) replacement;
			}
		}
		return this.eventQueueOverFlowCount;
	}

	/**
	 * Finds the child with the specified browse name.
	 */
	@Override
	protected BaseInstance findChild(QualifiedName browseName, boolean createOrReplace, BaseInstance replacement) {
		if (QualifiedName.isNull(browseName)) {
			return null;
		}
		BaseInstance instance = null;
		switch (browseName.getName()) {
		case BrowseNames.SESSIONID:
			instance = creatSessionId(createOrReplace, replacement);
			break;
		case BrowseNames.SUBSCRIPTIONID:
			instance = creatSubscriptionId(createOrReplace, replacement);
			break;
		case BrowseNames.PRIORITY:
			instance = creatPriority(createOrReplace, replacement);
			break;
		case BrowseNames.PUBLISHINGINTERVAL:
			instance = creatPublishingInterval(createOrReplace, replacement);
			break;
		case BrowseNames.MAXKEEPALIVECOUNT:
			instance = creatMaxKeepAliveCount(createOrReplace, replacement);
			break;
		case BrowseNames.MAXLIFETIMECOUNT:
			instance = creatMaxLiveTimeCount(createOrReplace, replacement);
			break;
		case BrowseNames.MAXNOTIFICATIONSPERPUBLISH:
			instance = creatMaxNotificationPerPublish(createOrReplace, replacement);
			break;
		case BrowseNames.PUBLISHINGENABLED:
			instance = creatPublishingEnabled(createOrReplace, replacement);
			break;
		case BrowseNames.MODIFYCOUNT:
			instance = creatModifyCount(createOrReplace, replacement);
			break;
		case BrowseNames.ENABLECOUNT:
			instance = createEnableCount(createOrReplace, replacement);
			break;
		case BrowseNames.DISABLECOUNT:
			instance = createDisableCount(createOrReplace, replacement);
			break;
		case BrowseNames.REPUBLISHREQUESTCOUNT:
			instance = createRepublishRequestCount(createOrReplace, replacement);
			break;
		case BrowseNames.REPUBLISHMESSAGEREQUESTCOUNT:
			instance = createRepublishMessageRequestCount(createOrReplace, replacement);
			break;
		case BrowseNames.REPUBLISHMESSAGECOUNT:
			instance = createRepublishMessageCount(createOrReplace, replacement);
			break;
		case BrowseNames.TRANSFERREQUESTCOUNT:
			instance = createTransferRequestCount(createOrReplace, replacement);
			break;
		case BrowseNames.TRANSFERREDTOALTCLIENTCOUNT:
			instance = createTransferredToAltClientCount(createOrReplace, replacement);
			break;
		case BrowseNames.TRANSFERREDTOSAMECLIENTCOUNT:
			instance = createTransferredToSampleClientCount(createOrReplace, replacement);
			break;
		case BrowseNames.PUBLISHREQUESTCOUNT:
			instance = createPublishRequestCount(createOrReplace, replacement);
			break;
		case BrowseNames.DATACHANGENOTIFICATIONSCOUNT:
			instance = createDataChangeNotificationsCount(createOrReplace, replacement);
			break;
		case BrowseNames.EVENTNOTIFICATIONSCOUNT:
			instance = createEventNotificationsCount(createOrReplace, replacement);
			break;
		case BrowseNames.NOTIFICATIONSCOUNT:
			instance = createNotificationsCount(createOrReplace, replacement);
			break;
		case BrowseNames.LATEPUBLISHREQUESTCOUNT:
			instance = createLatePublishRequestCount(createOrReplace, replacement);
			break;
		case BrowseNames.CURRENTKEEPALIVECOUNT:
			instance = createCurrentKeepAliveCount(createOrReplace, replacement);
			break;
		case BrowseNames.CURRENTLIFETIMECOUNT:
			instance = createCurrentLifetimeCount(createOrReplace, replacement);
			break;
		case BrowseNames.UNACKNOWLEDGEDMESSAGECOUNT:
			instance = createUnacknowledgedMessageCount(createOrReplace, replacement);
			break;
		case BrowseNames.DISCARDMESSAGECOUNT:
			instance = createDiscardedMessageCount(createOrReplace, replacement);
			break;
		case BrowseNames.MONITOREDITEMCOUNT:
			instance = createMonitoredItemCount(createOrReplace, replacement);
			break;
		case BrowseNames.DISABLEDMONITOREDITEMCOUNT:
			instance = createDisabledMonitoredItemCount(createOrReplace, replacement);
			break;
		case BrowseNames.MONITORINGQUEUEOVERFLOWCOUNT:
			instance = createMonitoringQueueOverflowCount(createOrReplace, replacement);
			break;
		case BrowseNames.NEXTSEQUENCENUMBER:
			instance = createNextSequenceNumber(createOrReplace, replacement);
			break;
		case BrowseNames.EVENTQUEUEOVERFLOWCOUNT:
			instance = createEventQueueOverFlowCount(createOrReplace, replacement);
			break;
		default:
			break;
		}
		if (instance != null) {
			return instance;
		}
		return super.findChild(browseName, createOrReplace, replacement);
	}
}
