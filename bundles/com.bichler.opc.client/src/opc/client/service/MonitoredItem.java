package opc.client.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.DeleteMonitoredItemsRequest;
import org.opcfoundation.ua.core.EventFilter;
import org.opcfoundation.ua.core.EventFilterResult;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.MonitoredItemCreateResult;
import org.opcfoundation.ua.core.MonitoredItemModifyResult;
import org.opcfoundation.ua.core.MonitoredItemNotification;
import org.opcfoundation.ua.core.MonitoringFilter;
import org.opcfoundation.ua.core.MonitoringMode;
import org.opcfoundation.ua.core.MonitoringParameters;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.encoding.DecodingException;
import org.opcfoundation.ua.encoding.EncoderContext;
import org.opcfoundation.ua.encoding.EncodingException;

import opc.client.application.listener.DataChangeMonitorListener;
import opc.client.application.listener.EventMonitorListener;
import opc.client.application.listener.MonitorListener;
import opc.client.util.AlarmConditionUtil;
import opc.sdk.core.enums.RequestType;
import opc.sdk.ua.constants.EventSeverity;

/**
 * Monitored Item of an UA Client to recieve DataChange or Event notifications.
 * 
 * @author Thomas Z&ouml;chbauer
 * @since 23.05.2012, HB-Softsolution e.U.
 */
public class MonitoredItem {
	private static final String SUBSCRIPTIONTEXT = "Subscription ";
	/** NodeId of the Node to monitor */
	private NodeId startNodeId = null;
	/** Relative path of the Node to Monitor */
	private String relativePath = null;
	/** Unique Client Handle of the Monitored Item */
	private UnsignedInteger clientHandle = null;
	/** Attribute to monitor (Value or EventNotifier) */
	private UnsignedInteger attributeId = null;
	/** IndexRange */
	private String indexRange = null;
	/** Encoding */
	private QualifiedName enconding = null;
	/** Filter to use */
	private MonitoringFilter filter = null;
	/** Are discarding oldest values */
	private Boolean discardOldest = null;
	/** Displayname of the Monitored Item. The same as its monitoring Node */
	private String displayName = null;
	/** Interval to sample the value changes (0 for events) */
	private Double samplingInterval = null;
	/** MonitoringMode of the MonitoredItem */
	private MonitoringMode monitoringMode = null;
	private Object monitoringModeLock = new Object();
	/**
	 * MonitoringItemQueue size, to store multiple values if there are more changes
	 * then publishes
	 */
	private UnsignedInteger queueSize = null;
	/** Unique MonitoredItem Id used in the Subscription */
	private UnsignedInteger id = null;
	/** Internal lock */
	private Object lock = new Object();
	/** Subscription which is containing this MonitoredItem */
	private Subscription subscription = null;
	/** Last Notification */
	private Object lastNotification = null;
	/** MonitoredItem ValueChange cache, used to store datachanges */
	private MonitoredItemDataCache dataCache = null;
	/** MonitoredItem Event cache, used to store events */
	private MonitoredItemEventCache eventCache = null;
	/** NodeClass of the Node to monitor */
	private NodeClass nodeClass = null;
	/** Registered Listeners */
	private List<MonitorListener> listeners = null;
	/** create result */
	private StatusCode result = null;
	/** filter result */
	private EventFilterResult filterResult = null;
	/** items to trigger */
	private List<UnsignedInteger> triggerItems = null;
	private String key = null;

	/**
	 * Default monitored item
	 */
	public MonitoredItem() {
		this.startNodeId = null;
		this.relativePath = null;
		this.clientHandle = null;
		this.attributeId = Attributes.Value;
		this.indexRange = null;
		this.enconding = null;
		this.filter = null;
		this.queueSize = null;
		this.discardOldest = true;
		// this.status =
		this.displayName = "MonitoredItem";
		this.samplingInterval = -1.0;
		this.monitoringMode = MonitoringMode.Reporting;
		// this ensures the state is consistent with the node class.
		this.listeners = new ArrayList<>();
		this.triggerItems = new ArrayList<>();
		this.setKey(null);
	}

	/**
	 * Add DataChange- or Event MonitorListeners.
	 * 
	 * @param Listeners Listeners to add.
	 */
	public void addMonitorListener(MonitorListener... listeners) {
		if (listeners == null) {
			return;
		}
		synchronized (this.lock) {
			for (MonitorListener l : listeners)
				this.listeners.add(l);
		}
	}

	/**
	 * Remove DataChange- or Event MonitorListeners.
	 * 
	 * @param Listeners Listeners to remvoe.
	 */
	public void removeMonitorListener(MonitorListener... listeners) {
		if (listeners == null) {
			return;
		}
		synchronized (this.lock) {
			for (MonitorListener l : listeners) {
				this.listeners.remove(l);
			}
		}
	}

	/**
	 * Returns the displayname of the monitoreditem.
	 * 
	 * @return Displayname
	 */
	public String getDisplayName() {
		return this.displayName;
	}

	/**
	 * Returns the id of the monitoreditem.
	 * 
	 * @return MonitoredItem Id
	 */
	public UnsignedInteger getMonitoredItemId() {
		return this.id;
	}

	/**
	 * Returns the id of the subscription, which the monitoreditem is stored.
	 * 
	 * @return Subscription Id
	 */
	public UnsignedInteger getSubscriptionId() {
		return this.subscription.getSubscriptionId();
	}

	/**
	 * Returns the clienthandle id identifying the monitoreditem in the
	 * subscription.
	 * 
	 * @return Clienthandle
	 */
	public UnsignedInteger getClientHandle() {
		return this.clientHandle;
	}

	/**
	 * Returns the nodeclass of the node to monitor.
	 * 
	 * @return NodeClass
	 */
	public NodeClass getNodeClass() {
		return this.nodeClass;
	}

	/**
	 * Returns the datachange value cache of the monitoreditem.
	 * 
	 * @return Datavalue cache
	 */
	public MonitoredItemDataCache getDataCache() {
		return this.dataCache;
	}

	/**
	 * Returns the flag if the oldest value should be discarded.
	 * 
	 * @return DiscardOldest
	 */
	public boolean getDiscardOldest() {
		return this.discardOldest;
	}

	/**
	 * Returns the event cache.
	 * 
	 * @return Event cache
	 */
	public MonitoredItemEventCache getEventCache() {
		return this.eventCache;
	}

	/**
	 * Returns the nodeid of the node to monitor.
	 * 
	 * @return NodeId
	 */
	public NodeId getNodeId() {
		return this.startNodeId;
	}

	/**
	 * Returns the monitoring filter.
	 * 
	 * @return Filter
	 */
	public ExtensionObject getFilter() {
		if (this.filter != null) {
			try {
				return ExtensionObject.binaryEncode(this.filter, EncoderContext.getDefaultInstance());
			} catch (EncodingException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				return null;
			}
		}
		return null;
	}

	/**
	 * Returns the queuesize.
	 * 
	 * @return Queuesize
	 */
	public UnsignedInteger getQueueSize() {
		return this.queueSize;
	}

	/**
	 * Returns the sampling interval.
	 * 
	 * @return SamplingInterval
	 */
	public Double getSamplingInterval() {
		return this.samplingInterval;
	}

	/**
	 * Returns the relative browse path of the monitored item.
	 * 
	 * @return RelativePath
	 */
	public String getRelativePath() {
		return this.relativePath;
	}

	/**
	 * Returns the attribute id of the node to monitor. (EventNotifier- or Value
	 * attribute)
	 * 
	 * @return AttributeId
	 */
	public UnsignedInteger getAttributeId() {
		return this.attributeId;
	}

	/**
	 * Return the indexrange.
	 * 
	 * @return IndexRange
	 */
	public String getIndexRange() {
		return this.indexRange;
	}

	/**
	 * Returns the encoding.
	 * 
	 * @return Encoding
	 */
	public QualifiedName getEnconding() {
		return this.enconding;
	}

	/**
	 * Returns the monitoring mode.
	 * 
	 * @return MonitoringMode
	 */
	public MonitoringMode getMonitoringMode() {
		return this.monitoringMode;
	}

	/**
	 * Returns the last datachange notification.
	 * 
	 * @return Last datachange notification
	 */
	public Object getLastNotification() {
		return this.lastNotification;
	}

	/**
	 * Returns the subscription, which the monitoreditem is stored
	 * 
	 * @return
	 */
	public Subscription getSubscription() {
		return this.subscription;
	}

	/**
	 * Returns all monitor listeners.
	 * 
	 * @return MonitorListener
	 */
	public MonitorListener[] getListeners() {
		synchronized (this.lock) {
			return this.listeners.toArray(new MonitorListener[0]);
		}
	}

	/**
	 * Returns the create result.
	 * 
	 * @return Create result
	 */
	public StatusCode getCreateResult() {
		return this.result;
	}

	/**
	 * Returns the ids of the monitoreditems to trigger.
	 * 
	 * @return TriggerItems
	 */
	public UnsignedInteger[] getTriggeringItems() {
		return this.triggerItems.toArray(new UnsignedInteger[0]);
	}

	/**
	 * Returns the event filter result.
	 * 
	 * @return EventFilterResult
	 */
	public EventFilterResult getEventFilterResult() {
		return this.filterResult;
	}

	/**
	 * Sets the displayname.
	 * 
	 * @param DisplayName
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Creates a default monitored item
	 * 
	 * @param logger
	 */
	protected void create(NodeId nodeId, UnsignedInteger attributeId, QualifiedName dataEncoding, String indexRange,
			MonitoringParameters monitoringParameters, String key) {
		this.startNodeId = nodeId;
		this.attributeId = attributeId;
		this.enconding = dataEncoding;
		this.indexRange = indexRange;
		this.clientHandle = monitoringParameters.getClientHandle();
		this.discardOldest = monitoringParameters.getDiscardOldest();
		if (monitoringParameters.getFilter() != null)
			try {
				this.filter = monitoringParameters.getFilter().decode(EncoderContext.getDefaultInstance());
			} catch (DecodingException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			}
		this.queueSize = monitoringParameters.getQueueSize();
		this.samplingInterval = monitoringParameters.getSamplingInterval();
		// set a nodeclass for the item (datachange or event)
		if (Attributes.Value.equals(this.attributeId)) {
			setNodeClass(NodeClass.Variable);
		} else if (Attributes.EventNotifier.equals(this.attributeId)) {
			setNodeClass(NodeClass.Object);
		}
		this.setKey(key);
	}

	/**
	 * 
	 * @param NewValue new received value
	 * @throws ServiceResultException Service failed drastically!
	 */
	protected void saveValueInCache(Object newValue) {
		synchronized (this.lock) {
			this.lastNotification = newValue;
			if (this.dataCache != null && newValue instanceof MonitoredItemNotification) {
				this.dataCache.onNotification(this, (MonitoredItemNotification) newValue);
			}
			if (this.eventCache != null && newValue instanceof EventElement) {
				this.eventCache.onNotification(this, (EventElement) newValue);
			}
		}
	}

	protected void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}

	protected void modify(MonitoredItemModifyResult result, MonitoringParameters monitoringParameters) {
		synchronized (this.lock) {
			setFilter(monitoringParameters.getFilter());
			setFilterResult(result.getFilterResult());
			setQueueSize(result.getRevisedQueueSize(), monitoringParameters.getQueueSize());
			setSamplingInterval(result.getRevisedSamplingInterval(), monitoringParameters.getSamplingInterval());
			setDiscardOldest(monitoringParameters.getDiscardOldest());
		}
	}

	protected boolean setResult(MonitoredItemCreateResult createResult, MonitoringParameters requestParameters,
			int index) {
		synchronized (this.lock) {
			if (UnsignedInteger.ZERO.equals(createResult.getMonitoredItemId()) && createResult.getStatusCode() != null
					&& createResult.getStatusCode().isGood()) {
				this.result = new StatusCode(StatusCodes.Bad_MonitoredItemIdInvalid);
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"{0} MonitoredItemId of the {1}. item is invalid (ZERO)!",
						new String[] { RequestType.CreateMonitoredItems.name(), Integer.toString(index) });
				return false;
			}
			this.setMonitoredItemId(createResult.getMonitoredItemId());
			this.setQueueSize(createResult.getRevisedQueueSize(), requestParameters.getQueueSize());
			if (createResult.getFilterResult() != null) {
				this.setFilterResult(createResult.getFilterResult());
			}
			this.setSamplingInterval(createResult.getRevisedSamplingInterval(),
					requestParameters.getSamplingInterval());
			this.result = createResult.getStatusCode();
			if (this.result == null || this.result.isBad()) {
				String description = "Description is Null";
				if (this.result != null)
					description = this.result.getDescription();
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"{0} Error result creating the monitored item with the problematic node {1}! {2}",
						new String[] { RequestType.CreateMonitoredItems.name(),
								this.startNodeId == null ? NodeId.NULL.toString() : this.startNodeId.toString(),
								description });
				this.monitoringMode = MonitoringMode.Disabled;
			}
		}
		return true;
	}

	protected void clear() {
		synchronized (this.lock) {
			if (this.dataCache != null) {
				this.dataCache.clear();
			}
			if (this.eventCache != null) {
				this.eventCache.clear();
			}
			this.listeners.clear();
			this.triggerItems.clear();
		}
	}

	protected void setMonitoringMode(MonitoringMode monitoringMode) {
		if (this.monitoringMode.compareTo(monitoringMode) == 0) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING, "{0} Monitoring mode {1} has not changed!",
					new String[] { RequestType.SetMonitoringMode.name(), this.monitoringMode.name() });
			return;
		}
		synchronized (this.monitoringModeLock) {
			this.monitoringMode = monitoringMode;
		}
	}

	protected void addTrigger(UnsignedInteger id) {
		this.triggerItems.add(id);
	}

	protected void removeTrigger(UnsignedInteger id) {
		this.triggerItems.remove(id);
	}

	private void setFilterResult(ExtensionObject filterResult) {
		if (filterResult != null) {
			try {
				this.filterResult = filterResult.decode(EncoderContext.getDefaultInstance());
			} catch (DecodingException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			}
		}
	}

	private void setFilter(ExtensionObject filter) {
		if (filter != null) {
			try {
				this.filter = filter.decode(EncoderContext.getDefaultInstance());
			} catch (DecodingException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			}
		}
	}

	private void setDiscardOldest(Boolean discardOldest) {
		if (discardOldest != null) {
			this.discardOldest = discardOldest;
		}
	}

	private void setMonitoredItemId(UnsignedInteger monitoredItemId) {
		synchronized (this.lock) {
			this.id = monitoredItemId;
		}
	}

	/**
	 * The node class of the node being monitored (affects the type of filter
	 * available)
	 * 
	 * @param variable
	 */
	private void setNodeClass(NodeClass nodeClass) {
		if (nodeClass == null) {
			throw new IllegalArgumentException("NodeClass");
		}
		if (!nodeClass.equals(this.nodeClass)) {
			setNodeClassFilter(nodeClass);
		}
		this.nodeClass = nodeClass;
	}

	private void setNodeClassFilter(NodeClass nodeClass) {
		if ((nodeClass.getValue() & (NodeClass.Object.getValue() | NodeClass.View.getValue())) != 0) {
			// ensure a valid event filter.
			if (!(this.filter instanceof EventFilter)) {
				useDefaultEventFilter();
			}
			// set the queue size to the default for events.
			if (this.queueSize.intValue() <= 1) {
				this.queueSize = UnsignedInteger.MAX_VALUE;
			}
			this.eventCache = new MonitoredItemEventCache(100);
			this.attributeId = Attributes.EventNotifier;
		} else {
			// clear the filter if it is only for events
			if (this.filter instanceof EventFilter) {
				this.filter = null;
			}
			// set the queue size to the default for data changes
			if (this.queueSize.intValue() == UnsignedInteger.L_MAX_VALUE) {
				this.queueSize = new UnsignedInteger(1);
			}
			this.dataCache = new MonitoredItemDataCache(1);
		}
	}

	private void useDefaultEventFilter() {
		try {
			this.filter = AlarmConditionUtil.createEventFilter(this.subscription.getSession(), EventSeverity.MIN, true,
					new NodeId[] { Identifiers.BaseEventType });
		} catch (ServiceResultException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
		}
	}

	private void setQueueSize(UnsignedInteger queueSize, UnsignedInteger requestedQueueSize) {
		synchronized (this.lock) {
			if (queueSize == null || (queueSize.compareTo(requestedQueueSize)) != 0) {
				String destination = (this.getSubscription() != null)
						? SUBSCRIPTIONTEXT + this.subscription.getSubscriptionId() + " "
						: "";
				if (queueSize == null)
					throw new NullPointerException("Queue size is null");
				Logger.getLogger(getClass().getName()).log(Level.INFO,
						"{0} MonitoredItem {1} {2} changed queuesize from origin: {3} to {4}.",
						new Object[] { RequestType.ModifyMonitoredItems.name(), this.id, destination, this.queueSize,
								queueSize });
			} else if (queueSize.longValue() > 0) {
				this.queueSize = queueSize;
				if (this.dataCache != null) {
					this.dataCache.setQueueSize(queueSize.intValue());
				}
			}
			// CTT 10.2-013
			else {
				String destination = (this.getSubscription() != null)
						? SUBSCRIPTIONTEXT + this.subscription.getSubscriptionId() + " "
						: "";
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"{0} MonitoredItem {1} {2} queuesize is invalid {3}!",
						new Object[] { RequestType.ModifyMonitoredItems.name(), this.id, destination, queueSize });
			}
		}
	}

	private void setSamplingInterval(Double samplingInterval, Double requestedSamplingInterval) {
		synchronized (this.lock) {
			if (samplingInterval == null || (samplingInterval.compareTo(requestedSamplingInterval)) != 0) {
				String destination = (this.getSubscription() != null)
						? SUBSCRIPTIONTEXT + this.subscription.getSubscriptionId() + " "
						: "";
				Logger.getLogger(getClass().getName()).log(Level.WARNING,
						"{0} MonitoredItem {1} {2} changed sampling interval from origin: {3} to {4}",
						new Object[] { RequestType.ModifyMonitoredItems.name(), this.id, destination,
								Double.toString(requestedSamplingInterval), Double.toString(samplingInterval) });
			}
			if ((samplingInterval > 0 && samplingInterval != Double.NaN)
					|| (samplingInterval == 0 && this.filter != null)) {
				this.samplingInterval = samplingInterval;
			} else {
				String sampint = "null";
				if (samplingInterval != null)
					sampint = samplingInterval.toString();
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"{0} MonitoredItem {1} has has not a valid sampling interval {2}!",
						new Object[] { RequestType.ModifyMonitoredItems.name(), this.id, sampint });
			}
		}
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public class MonitoredItemDataCache {
		private int queueSize = -1;
		private Queue<DataValue> values = null;
		private DataValue lastValue = null;

		public MonitoredItemDataCache(int capacity) {
			this.queueSize = capacity;
			this.values = new ConcurrentLinkedQueue<>();
		}

		/**
		 * The size of the queue to maintain
		 * 
		 * @return
		 */
		public int getQueueSize() {
			return this.queueSize;
		}

		/**
		 * The last value received from the server
		 * 
		 * @return
		 */
		public DataValue getCurrentValue() {
			return this.lastValue;
		}

		/**
		 * The values received from the server
		 * 
		 * @return
		 */
		public Queue<DataValue> getValues() {
			return this.values;
		}

		/**
		 * Returns all values in the queue
		 * 
		 * @return
		 */
		public List<DataValue> publish() {
			List<DataValue> tmpValues = new ArrayList<>(this.values.size());
			DataValue value = this.values.poll();
			while (value != null) {
				tmpValues.add(value);
				value = this.values.poll();
			}
			return tmpValues;
		}

		/**
		 * Saves a notification in the data change cache.
		 * 
		 * @param monitoredItem
		 * 
		 * @param newNotification
		 * @param listener
		 * @throws ServiceResultException
		 */
		private void onNotification(final MonitoredItem monitoredItem, MonitoredItemNotification newNotification) {
			synchronized (lock) {
				// memory old value
				final DataValue oldValue = this.lastValue;
				this.lastValue = newNotification.getValue();
				// structure changed?
				if (this.lastValue != null && this.lastValue.getStatusCode() != null
						&& this.lastValue.getStatusCode().isStructureChanged()) {
					// CTT 11.4-012
					Logger.getLogger(getClass().getName()).log(Level.FINE,
							"{0} Receiving a structure change bit for node {1}! Removing monitored item {2} from subscription {3}!",
							new Object[] { RequestType.Publish.name(), MonitoredItem.this.startNodeId, id,
									subscription.getSubscriptionId() });
					DeleteMonitoredItemsRequest request = new DeleteMonitoredItemsRequest();
					request.setMonitoredItemIds(new UnsignedInteger[] { id });
					request.setSubscriptionId(MonitoredItem.this.subscription.getSubscriptionId());
					try {
						MonitoredItem.this.subscription.getSession().getSubscriptionManager()
								.deleteMonitoredItems(MonitoredItem.this.subscription.getSession(), request);
					} catch (ServiceResultException e) {
						Logger.getLogger(getClass().getName()).log(Level.FINE, null, e);
					}
				}
				// add
				else {
					this.values.add(this.lastValue);
					while (this.values.size() > this.queueSize) {
						this.values.poll();
					}
				}
				for (MonitorListener valueChange : listeners) {
					if (valueChange instanceof DataChangeMonitorListener) {
						((DataChangeMonitorListener) valueChange).monitorDataChange(monitoredItem, lastValue, oldValue);
					}
				}
			}
		}

		/**
		 * Changes the size of the queue
		 * 
		 * @param queueSize
		 */
		public void setQueueSize(int queueSize) {
			int tmpQueueSize = queueSize;
			if (tmpQueueSize == this.queueSize) {
				return;
			}
			if (tmpQueueSize < 1) {
				tmpQueueSize = 1;
			}
			this.queueSize = tmpQueueSize;
			while (this.values.size() > this.queueSize) {
				this.values.poll();
			}
		}

		private void clear() {
			this.lastValue = null;
			this.values.clear();
		}
	}

	public class MonitoredItemEventCache {
		private int queueSize = -1;
		private Queue<EventElement> events = null;
		boolean isCondition = false;

		public MonitoredItemEventCache(int capacity) {
			this.queueSize = capacity;
			this.events = new ConcurrentLinkedQueue<>();
		}

		private void add(EventElement ee) {
			boolean found = false;
			// unique
			for (EventElement element : this.events) {
				if (ee.equals(element)) {
					element.setEvent(ee.getEvent());
					found = true;
					break;
				}
			}
			// new event to add
			if (!found) {
				this.events.add(ee);
			}
		}

		public void onNotification(final MonitoredItem monitoredItem, final EventElement newevent) {
			add(newevent);
			/** this.lastEvent = newevent; */
			while (this.events.size() > this.queueSize) {
				this.events.poll();
			}
			for (MonitorListener event : listeners) {
				if (event instanceof EventMonitorListener) {
					((EventMonitorListener) event).monitorEvent(monitoredItem, newevent.getElementId(), newevent);
				}
			}
		}

		public void refresh() {
			this.events.clear();
		}

		public Queue<EventElement> getEvents() {
			return new LinkedList<>(this.events);
		}

		private void clear() {
			this.events.clear();
		}
	}
}
