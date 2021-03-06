package opc.sdk.server.service.subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.ServiceResult;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.DataChangeFilter;
import org.opcfoundation.ua.core.DataChangeTrigger;
import org.opcfoundation.ua.core.DeadbandType;
import org.opcfoundation.ua.core.EventFieldList;
import org.opcfoundation.ua.core.EventFilter;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.MonitoredItemCreateRequest;
import org.opcfoundation.ua.core.MonitoredItemModifyResult;
import org.opcfoundation.ua.core.MonitoredItemNotification;
import org.opcfoundation.ua.core.MonitoringFilter;
import org.opcfoundation.ua.core.MonitoringMode;
import org.opcfoundation.ua.core.MonitoringParameters;
import org.opcfoundation.ua.core.SimpleAttributeOperand;
import org.opcfoundation.ua.core.TimestampsToReturn;
import org.opcfoundation.ua.encoding.DecodingException;
import org.opcfoundation.ua.encoding.EncoderContext;
import org.opcfoundation.ua.encoding.EncodingException;
import org.opcfoundation.ua.utils.MultiDimensionArrayUtils;

import com.bichler.opc.comdrv.ComDRVManager;

import opc.sdk.core.enums.MonitoredItemTypeMask;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.VariableNode;
import opc.sdk.core.subscription.IMonitoredItem;
import opc.sdk.core.types.TypeTable;
import opc.sdk.core.utils.NumericRange;
import opc.sdk.server.core.managers.OPCValidationFramework;
import opc.sdk.server.service.session.OPCServerSession;
import opc.sdk.ua.classes.BaseEventType;
import opc.sdk.ua.classes.BaseInstance;
import opc.sdk.ua.classes.BaseVariableType;
import opc.sdk.ua.classes.ConditionType;

public class OPCMonitoredItem implements IMonitoredItem, ITriggeredMonitorItem {
	// private Object lock2 = new Object();
	private Object lock = new Object();
	private Node node;
	private UnsignedInteger subscriptionId;
	private UnsignedInteger attributeId;
	private long monitoredItemId;
	private OPCServerSession session;
	private String indexRange;
	private TimestampsToReturn timestampsToReturn;
	private MonitoringMode monitoringMode;
	private UnsignedInteger clientHandle;
	private Double samplingInterval;
	private UnsignedInteger queueSize;
	private Boolean discardOldest;
	private MonitoringFilter filter;
	private MonitoredItemTypeMask typeMask = null;
	/** nano seconds */
	private long nextSamplingTime;
	// private long samplingIntervalNano = 0;
	private ExtensionObject filterResult;
	/** publishing state */
	private boolean readyToPublish;
	// private MonitoredItemTypeMask monitoredItemType =
	// MonitoredItemTypeMask.DataChange;
	private OPCMonitoredItemQueue queue;
	private List<EventFieldList> events;
	private boolean semanticsChanged;
	private boolean structureChanged;
	// private boolean overflow;
	private QualifiedName dataEncoding;
	/**
	 * Gets or Sets a value indicating whether the item is ready to trigger in case
	 * it has some linked items.
	 */
	private boolean readyToTrigger = false;
	private boolean triggered = false;

	/**
	 * Initializes a monitored item.
	 * 
	 * @param Node               OPC UA address space node
	 * @param SubscriptionId     Identifier of the subscription, which the item
	 *                           belongs
	 * @param ItemToCreate       Request item
	 * @param TimestampsToReturn Timestamp filter to the published values
	 * @param TypeMask           Type of the monitored item (DataChange, Event,
	 *                           Undefined (Non-Attribute, No-Variable changes),...
	 */
	public OPCMonitoredItem(Node node, UnsignedInteger subscriptionId, MonitoredItemCreateRequest itemToCreate,
			TimestampsToReturn timestampsToReturn, MonitoredItemTypeMask typeMask) {
		// initialize
		this.node = node;
		this.subscriptionId = subscriptionId;
		this.attributeId = itemToCreate.getItemToMonitor().getAttributeId();
		this.typeMask = typeMask;
		this.indexRange = itemToCreate.getItemToMonitor().getIndexRange();
		this.dataEncoding = itemToCreate.getItemToMonitor().getDataEncoding();
		this.timestampsToReturn = timestampsToReturn;
		this.monitoringMode = itemToCreate.getMonitoringMode();
		this.clientHandle = itemToCreate.getRequestedParameters().getClientHandle();
		this.samplingInterval = itemToCreate.getRequestedParameters().getSamplingInterval();
		this.queueSize = itemToCreate.getRequestedParameters().getQueueSize();
		this.discardOldest = itemToCreate.getRequestedParameters().getDiscardOldest();
		this.monitoredItemId = -1;
		// monitor item filter
		try {
			ExtensionObject filterToUse = itemToCreate.getRequestedParameters().getFilter();
			if (filterToUse != null) {
				this.filter = filterToUse.decode(EncoderContext.getDefaultInstance());
			}
		} catch (DecodingException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
		}
		if (this.filter instanceof EventFilter) {
			this.typeMask = MonitoredItemTypeMask.Events;
			if (Identifiers.Server.equals(itemToCreate.getItemToMonitor().getNodeId())) {
				this.typeMask = MonitoredItemTypeMask.AllEvents;
			}
		}
		initializeQueue();
		// this.alwaysReportUpdates = false;
		this.nextSamplingTime = System.nanoTime();
	}

	public void modifyMonitoredItem(MonitoringParameters params, TimestampsToReturn timestampsToReturn,
			MonitoredItemModifyResult result, TypeTable typeTable, UnsignedInteger maxQueueSize) {
		synchronized (this.lock) {
			// decode filter
			MonitoringFilter filterToUse = null;
			if (params.getFilter() != null) {
				try {
					filterToUse = params.getFilter().decode(EncoderContext.getDefaultInstance());
				} catch (DecodingException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
				}
			}
			// validate modify request
			ServiceResult error = OPCValidationFramework.validateFilter(this.attributeId, this.node, filterToUse,
					typeTable);
			if (error != null) {
				result.setStatusCode(error.getCode());
				return;
			}
			// calculate monitoring parameters
			Double revisedSamplingInterval = calculateSamplingInterval(params.getSamplingInterval());
			UnsignedInteger revisedQueueSize = calculateQueueSize(params.getQueueSize(), maxQueueSize);
			// set parameter modifications
			setSamplingInterval(revisedSamplingInterval);
			setQueueSize(revisedQueueSize, params.getDiscardOldest());
			this.timestampsToReturn = timestampsToReturn;
			setFilter(filterToUse);
			// fill result
			result.setRevisedQueueSize(revisedQueueSize);
			result.setRevisedSamplingInterval(revisedSamplingInterval);
			try {
				if (filterToUse != null) {
					result.setFilterResult(
							ExtensionObject.binaryEncode(filterToUse, EncoderContext.getDefaultInstance()));
				}
			} catch (EncodingException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			}
			result.setStatusCode(StatusCode.GOOD);
		}
	}

	public boolean publishEvent(List<EventFieldList> events) {
		synchronized (this.lock) {
			// checks if the item reports datachanges
			switch (this.typeMask) {
			case DataChange:
			case Undefined:
				return false;
			default:
			}
			// long timestamp = System.nanoTime() ;
			// only publish if reporting
			// if (!this.isReadyToPublish(timestamp)) {
			// return false;
			// }
			// go to the next sampling interval
			// incrementSampleTime(timestamp);
			// publish all queued values
			if (this.events != null && this.events.size() > 0) {
				events.addAll(this.events);
				this.events.clear();
			}
			// if (this.queue != null && this.queue.getQueueSize() > 0) {
			// // reference
			// DataValue dequeuedValue = new DataValue();
			//
			// while (this.queue.dequeue(dequeuedValue)) {
			// // new instance
			// DataValue existingValue = new DataValue(
			// dequeuedValue.getValue(),
			// dequeuedValue.getStatusCode(),
			// dequeuedValue.getSourceTimestamp(),
			// dequeuedValue.getServerPicoseconds(),
			// dequeuedValue.getServerTimestamp(),
			// dequeuedValue.getServerPicoseconds());
			//
			// publish(notifications, existingValue);
			// }
			// }
			// /** publish last value if there is no queuing */
			// else {
			// publish(notifications, this.queue.getLastValue());
			// }
			// this.overflow = false;
			this.readyToPublish = false;
			this.triggered = false;
			this.readyToTrigger = false;
			return true;
		}
	}

	/**
	 * Dequeues values for data change notification message
	 * 
	 * @param notifications
	 * @return
	 */
	public boolean publishDataChange(List<MonitoredItemNotification> notifications) {
		synchronized (this.lock) {
			// checks if the item reports datachanges
			switch (this.typeMask) {
			case Events:
			case AllEvents:
				return false;
			default:
			}
			long timestamp = System.nanoTime();
			// check data change publish is allowed
			if (!this.isReadyToPublish(timestamp)) {
				return false;
			}
			// go to the next sampling interval
			incrementSampleTime(timestamp);
			// publish all queued values
			if (this.queue != null && this.queue.getQueueSize() > 0) {
				// reference
				DataValue dequeuedValue = new DataValue();
				while (this.queue.dequeue(dequeuedValue)) {
					// new instance
					DataValue existingValue = new DataValue(dequeuedValue.getValue(), dequeuedValue.getStatusCode(),
							dequeuedValue.getSourceTimestamp(), dequeuedValue.getServerPicoseconds(),
							dequeuedValue.getServerTimestamp(), dequeuedValue.getServerPicoseconds());
					publish(notifications, existingValue);
				}
			}
			/** publish last value if there is no queuing */
			else {
				if (queue != null)
					publish(notifications, this.queue.getLastValue());
			}
			// this.overflow = false;
			this.readyToPublish = false;
			this.readyToTrigger = false;
			this.triggered = false;
			return true;
		}
	}

	@Override
	public boolean queueEvent(BaseEventType state) {
		synchronized (this.lock) {
			if (MonitoringMode.Disabled == this.monitoringMode) {
				return false;
			}
			return addEventToQueue(state);
		}
	}

	private boolean addEventToQueue(BaseEventType state) {
		boolean isAdd = false;
		// TODO: event list should not be null!
		if (this.events != null) {
			SimpleAttributeOperand[] select = null;
			if (this.filter instanceof EventFilter) {
				select = ((EventFilter) this.filter).getSelectClauses();
			}
			List<Variant> fields = new ArrayList<>();
			if (select != null) {
				boolean allowed = ComDRVManager.getDRVManager().filterEventFromDriver(getNode().getNodeId(), state,
						(EventFilter) this.filter);
				if (!allowed) {
					return false;
				}
				for (SimpleAttributeOperand operand : select) {
					QualifiedName[] path = operand.getBrowsePath();
					if (path == null) {
						continue;
					}
					BaseInstance bis = state;
					// operands attributeid is noded
					UnsignedInteger attrId = operand.getAttributeId();
					// is for conditions
					if (state instanceof ConditionType && UnsignedInteger.ONE.equals(attrId)) {
						fields.add(new Variant(new NodeId(6, 73)));
						continue;
					} else if (UnsignedInteger.ONE.equals(attrId)) {
						// TODO: add underlying method id
						fields.add(Variant.NULL);
						continue;
					}
					for (QualifiedName qn : path) {
						bis = bis.findChild(qn);
						if (bis == null) {
							// cannot find child
							// fields.add(new Variant(1));
							fields.add(Variant.NULL);
							break;
						}
					}
					if (!(bis instanceof BaseVariableType)) {
						continue;
					}
					// TODO change parameterized
					Object value = ((BaseVariableType<?>) bis).getValue();
					fields.add(new Variant(value));
				}
			} else {
				fields.add(new Variant(1));
			}
			EventFieldList evt = new EventFieldList();
			evt.setClientHandle(this.clientHandle);
			evt.setEventFields(fields.toArray(new Variant[0]));
			isAdd = this.events.add(evt);
			this.readyToPublish = true;
		}
		// save last value recieved
		// if (!this.isReadyToPublish(System.nanoTime()) || this.discardOldest)
		// {
		// // this.queue.setLastValue(value);
		// this.readyToPublish = true;
		// }
		return isAdd;
	}

	@Override
	public boolean queueValueChange(DataValue value, DataValue lastValue) {
		synchronized (this.lock) {
			// check monitoring mode
			if (MonitoringMode.Disabled == this.monitoringMode) {
				return false;
			}
			// apply data change filter to incoming item
			if (!applyFilter(value, lastValue)) {
				return false;
			}
			// add the value to the queue
			addValueToQueue(value);
			return true;
		}
	}

	public boolean queueValueChange(DataValue value, DataValue lastValue, boolean isInit) {
		synchronized (this.lock) {
			boolean hasChanged = queueValueChange(value, lastValue);
			if (MonitoringMode.Sampling == this.monitoringMode) {
				this.readyToTrigger = false;
			}
			return hasChanged;
		}
	}

	public StatusCode setMonitoringMode(MonitoringMode monitoringMode) {
		// if (monitoringMode.getValue() < MonitoringMode.Disabled.getValue()
		// || monitoringMode.getValue() > MonitoringMode.Reporting
		// .getValue()) {
		// return new StatusCode(StatusCodes.Bad_MonitoringModeInvalid);
		// }
		// this.monitoringMode = monitoringMode;
		// return StatusCode.GOOD;
		synchronized (this.lock) {
			MonitoringMode previousMode = this.monitoringMode;
			// change on different mode
			if (previousMode != monitoringMode) {
				// init next sampling time
				if (previousMode == MonitoringMode.Disabled) {
					this.nextSamplingTime = System.nanoTime();
					if (this.queue != null) {
						this.queue.setLastValue(null);
					}
				}
				// change mode
				this.monitoringMode = monitoringMode;
				// disable ready to publish flag
				if (this.monitoringMode == MonitoringMode.Disabled) {
					this.readyToPublish = false;
					this.readyToTrigger = false;
					this.triggered = false;
				}
				// save last value of queue
				DataValue lastValue = null;
				if (this.queue != null) {
					lastValue = this.queue.getLastValue();
				}
				// re-initialze queue
				initializeQueue();
				switch (this.typeMask) {
				// events
				case Events:
				case AllEvents:
					break;
				// data change
				default:
					// publish initial value if previous mode was disabled
					if (this.monitoringMode == MonitoringMode.Reporting && previousMode == MonitoringMode.Disabled) {
						// read current value from node
						DataValue value = new DataValue();
						this.node.readAttributeValue(this.attributeId, value, null);
						queueValueChange(value, lastValue);
					}
					break;
				}
			}
			return StatusCode.GOOD;
		}
	}

	public void setQueueSize(UnsignedInteger queueSize, boolean discardOldest) {
		synchronized (this.lock) {
			if (queueSize.longValue() < 0) {
				return;
			}
			if (this.queueSize.longValue() != queueSize.longValue()) {
				this.queueSize = queueSize;
				this.discardOldest = discardOldest;
				initializeQueue();
			}
		}
	}

	/**
	 * Registers monitored item to OPC UA address space node
	 * 
	 * @return
	 */
	boolean register() {
		return this.node.register(this);
	}

	/**
	 * Unregisters monitored item to OPC UA address space node
	 * 
	 * @return
	 */
	boolean unRegister() {
		return this.node.unregister(this);
	}

	public NumericRange getIndexRange() {
		try {
			if (this.indexRange == null || this.indexRange.isEmpty()) {
				return NumericRange.getEmpty();
			}
			return NumericRange.parse(this.indexRange);
		} catch (ServiceResultException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Applies the filter to value to determine if the new value should be kept.
	 * 
	 * @param lastValue
	 * 
	 * @param Value     Value to use.
	 * 
	 * @return true if the value has changed and the filter exceeds on the new
	 *         value, otherwise false
	 * @throws ServiceResultException
	 */
	protected boolean applyFilter(DataValue value, DataValue lastValue) {
		if (value == null) {
			return false;
		}
		boolean changed = valueChanged(value, lastValue);
		return changed;
	}

	protected Double calculateSamplingInterval(Double samplingInterval) {
		Double minimumSamplingInterval = 0.0;
		if (node != null && node instanceof VariableNode) {
			minimumSamplingInterval = ((VariableNode) node).getMinimumSamplingInterval();
		}
		/** determine the sampling interval. */
		if (samplingInterval < 0) {
			samplingInterval = this.samplingInterval;
		}
		if (samplingInterval - minimumSamplingInterval < 0) {
			samplingInterval = minimumSamplingInterval;
		}
		/** put a large upper limit on sampling */
		if (samplingInterval >= Double.MAX_VALUE) {
			samplingInterval = 365 * 24 * 3600 * 1000.0;
		}
		return samplingInterval;
	}

	protected UnsignedInteger calculateQueueSize(UnsignedInteger queueSize, UnsignedInteger maxQueueSize) {
		UnsignedInteger newQueueSize = queueSize;
		if (newQueueSize.longValue() <= 0) {
			newQueueSize = UnsignedInteger.ONE;
		}
		if (newQueueSize.longValue() > maxQueueSize.longValue()) {
			newQueueSize = UnsignedInteger.valueOf(maxQueueSize.longValue());
		}
		return newQueueSize;
	}

	// protected ReadValueId getReadValueId() {
	// new ReadValueId(this.node.getNodeId(), this.attributeId, this.indexRange,
	// this.dataencoding);
	// return this.readValueId;
	// }
	/**
	 * Whether the item has notifications that are ready to publish
	 * 
	 * @return TRUE if the monitored item is ready to publish, otherwise FALSE.
	 */
	protected boolean isReadyToPublish(long timestamp) {
		// check if not ready to pulbish
		if (!this.readyToPublish) {
			return false;
		}
		// check if monitoring is disabled
		if (MonitoringMode.Disabled == this.monitoringMode) {
			return false;
		}
		// check if it has been triggered
		// if (MonitoringMode.Disabled != this.monitoringMode && this.triggered)
		// {
		// return true;
		// }
		if (this.triggered) {
			return true;
		}
		// check if monitoring was turned off (sampling)
		if (MonitoringMode.Reporting != this.monitoringMode) {
			return false;
		}
		// long samplingTime = timestamp.getTimeInMillis();
		if (this.nextSamplingTime - timestamp > 0) {
			return false;
		}
		return true;
	}

	protected void setSamplingInterval(Double samplingInterval) {
		synchronized (this.lock) {
			if (samplingInterval == null || samplingInterval < 0) {
				return;
			}
			this.samplingInterval = samplingInterval;
		}
	}

	private void addValueToQueue(DataValue value) {
		// boolean isAdd = false;
		if (this.queue != null) {
			// isAdd =
			this.queue.queueValue(value);
		}
		// save last value received
		if (!this.isReadyToPublish(System.nanoTime()) || this.discardOldest) {
			// this.queue.setLastValue(value);
			// if (this.monitoringMode == MonitoringMode.Reporting) {
			this.readyToPublish = true;
			// }
			this.readyToTrigger = true;
		}
		return;
	}

	private void initializeQueue() {
		switch (this.monitoringMode) {
		case Disabled:
			this.queue = null;
			this.events = null;
			break;
		case Reporting:
		case Sampling:
			// checks if queueing is disabled
			// if (this.queueSize.longValue() <= 1) {
			// this.queue = null;
			// this.events = null;
			// }
			switch (this.typeMask) {
			// initialize event queue
			case Events:
			case AllEvents:
				if (this.events == null) {
					this.events = new ArrayList<EventFieldList>();
				}
				// check if existing queue entries must be discarded
				if (this.events.size() > this.queueSize.longValue()) {
					int queueSize = this.queueSize.intValue();
					if (this.discardOldest) {
						for (int index = 0; index < this.events.size() - queueSize; index++) {
							this.events.remove(index);
						}
					} else {
						for (int index = queueSize; index < this.events.size() - queueSize; index++) {
							this.events.remove(index);
						}
					}
				}
				break;
			// initialize data change queue
			default:
				if (this.queue == null) {
					this.queue = new OPCMonitoredItemQueue(this.discardOldest);
				}
				this.queue.setQueueSize(this.queueSize, this.discardOldest);
				break;
			}
			break;
		default:
			break;
		}
	}

	private void incrementSampleTime(long timestamp) {
		if (this.nextSamplingTime > 0) {
			while (timestamp - this.nextSamplingTime > 0) {
				this.nextSamplingTime += (this.samplingInterval.longValue() * 1000000);
			}
		} else {
			this.nextSamplingTime = timestamp + (this.samplingInterval.longValue() * 1000000);
		}
	}

	private void publish(List<MonitoredItemNotification> notifications, DataValue existingValue) {
		if (this.semanticsChanged) {
			/**
			 * TODO:SETSEMANTICCHANGED if(value != null){
			 * value.getStatusCode().isSemanticsChanged(); } if(error != null){
			 * error.getCode().isSemanticsChanged(); }
			 */
			this.semanticsChanged = false;
		}
		if (this.structureChanged) {
			/**
			 * TODO:SETSTRUCTURECHANGED if(value != null){
			 * value.getStatusCode().isStructureChanged()(); } if(error != null){
			 * error.getCode().isStructureChanged()(); }
			 */
			this.structureChanged = false;
		}
		// copy data value
		MonitoredItemNotification item = new MonitoredItemNotification();
		item.setClientHandle(this.clientHandle);
		item.setValue(existingValue);
		// apply timestamp filter
		switch (this.timestampsToReturn) {
		case Both:
			item.getValue().setServerTimestamp(DateTime.currentTime());
			break;
		case Server:
			item.getValue().setSourceTimestamp(DateTime.MIN_VALUE);
			break;
		case Source:
			item.getValue().setServerTimestamp(DateTime.MIN_VALUE);
			break;
		case Neither:
			item.getValue().setServerTimestamp(DateTime.MIN_VALUE);
			item.getValue().setSourceTimestamp(DateTime.MIN_VALUE);
			break;
		case Invalid:
			break;
		default:
			break;
		}
		notifications.add(item);
	}

	/**
	 * Checks if two variant values are equal.
	 * 
	 * @param Value1       Current Value of this MonitoredItem.
	 * @param NewValue     New Value of this MonitoredItem to set.
	 * @param DeadBandType The DeadbandFilter Type, None, Absolute or Percent.
	 * @param DeadBand     The Deadband Value.
	 * @return TRUE if the current value is the same object or value as the newValue
	 *         argument; FALSE otherwise.
	 */
	private boolean equals(Object value1, Object value2, DeadbandType deadBandType, double deadBand) {
		// check if reference to same object
		if (value1 == value2) {
			return true;
		}
		// check for invalid values
		if (value1 == null || value2 == null) {
			return value1 == value2;
		}
		// check for type changes
		if (value1.getClass() != value2.getClass()) {
			return false;
		}
		// check if value are equal
		if (value1.equals(value2)) {
			return true;
		}
		// check for array
		Variant array1 = new Variant(value1);
		Variant array2 = new Variant(value2);
		boolean isArray1 = value1.getClass().isArray();
		boolean isArray2 = value2.getClass().isArray();
		if (!isArray1 || !isArray2) {
			// nothing to do if no deadband
			if (deadBandType == DeadbandType.None) {
				return false;
			}
			// check deadband
			return !exceedsDeadBand(value1, value2, deadBandType, deadBand);
		}
		// compare dimension of both arrays
		if (array1.getDimension() != array2.getDimension()) {
			return false;
		}
		// compare each element
		boolean isVariant = array1.getCompositeClass() == Variant.class;
		int[] arraydim = array1.getArrayDimensions();
		arraydim = MultiDimensionArrayUtils.getArrayLengths(array1.getValue());
		// run through the dimensions because they are equal
		for (int i = 0; i < arraydim.length; i++) {
			// run through the array values
			int dims1 = MultiDimensionArrayUtils.getArrayLengths(array1.getValue())[i];
			int dims2 = MultiDimensionArrayUtils.getArrayLengths(array2.getValue())[i];
			if (dims1 == dims2) {
				for (int j = 0; j < arraydim[i]; j++) {
					Object e1 = null;
					Object e2 = null;
					if (array1.getValue() instanceof byte[] && array2.getValue() instanceof byte[]) {
						e1 = ((byte[]) array1.getValue())[j];
						e2 = ((byte[]) array2.getValue())[j];
					} else {
						e1 = ((Object[]) array1.getValue())[j];
						e2 = ((Object[]) array2.getValue())[j];
					}
					if (isVariant) {
						e1 = ((Variant) e1).getValue();
						e2 = ((Variant) e2).getValue();
					}
					if (!equals(e1, e2, deadBandType, deadBand)) {
						return false;
					}
				}
			} else {
				return false;
			}
		}
		// must be equal
		return true;
	}

	/**
	 * Checks the Deadband if it has been exceeded.
	 * 
	 * @param Value1       Current Value of this MonitoredItem.
	 * @param Value2       New Value of this MonitoredItem to set.
	 * @param DeadBandType The DeadbandFilter Type, None, Absolute or Percent.
	 * @param DeadBand     The Deadband Value.
	 * @return TRUE if the Deadband was exceeded, FALSE otherwise.
	 */
	private boolean exceedsDeadBand(Object value1, Object value2, DeadbandType deadBandType, double deadband) {
		// cannont convert doubles safe to to decimals
		double baseline = 1.0;
		try {
			Number number1 = (Number) value1;
			Number number2 = (Number) value2;
			if (DeadbandType.Percent.equals(deadBandType)) {
				baseline = deadband / 100;
				double dif = number1.doubleValue() * baseline;
				if (Math.abs(number1.doubleValue() - number2.doubleValue()) >= dif) {
					return true;
				}
			} else if (baseline > 0) {
				if ((Math.abs(number1.doubleValue() - number2.doubleValue()) / baseline) <= deadband) {
					return false;
				}
			}
		} catch (ClassCastException c) {
			// treat all conversion errors as evidence thath the deadband was
			// exceeded
		}
		return true;
	}

	/**
	 * Checks if the value has changed since the last write on a monitored item.
	 * 
	 * @param lastValue2
	 * 
	 * @param Value      New value to be checked
	 * @return TRUE if the value has changed, otherwise FALSE.
	 */
	private boolean valueChanged(DataValue value, DataValue lastValue) {
		// apply datachange filter
		double deadBand = 0.0;
		DeadbandType deadbandType = DeadbandType.None;
		DataChangeTrigger trigger = DataChangeTrigger.StatusValue;
		if (this.filter instanceof DataChangeFilter) {
			trigger = ((DataChangeFilter) this.filter).getTrigger();
			deadbandType = DeadbandType.valueOf(((DataChangeFilter) this.filter).getDeadbandType().intValue());
			deadBand = ((DataChangeFilter) this.filter).getDeadbandValue();
			// last statuscode
			StatusCode lastStatus = null;
			// when deadband is used and the trigger is StatusValueTimestamp,
			// then it should behave as if trigger is StatusValue.
			if ((deadbandType != DeadbandType.None) && (trigger == DataChangeTrigger.StatusValueTimestamp)) {
				trigger = DataChangeTrigger.StatusValue;
			}
			// current status
			StatusCode status = value.getStatusCode();
			if (lastValue != null) {
				lastStatus = lastValue.getStatusCode();
			} else {
				lastStatus = StatusCode.GOOD;
			}
			// value changed if any status change occures
			if (status.getValueAsIntBits() != lastStatus.getValueAsIntBits()) {
				return true;
			}
			// check if timestamp has changed
			if (DataChangeTrigger.StatusValueTimestamp == trigger) {
				if (lastValue == null) {
					return true;
				}
				if (lastValue.getSourceTimestamp() != null && value.getSourceTimestamp() != null && (lastValue
						.getSourceTimestamp().getTimeInMillis() != value.getSourceTimestamp().getTimeInMillis())) {
					return true;
				}
			}
			// check if value changes are ignored
			if (DataChangeTrigger.Status == trigger) {
				if (lastValue == null) {
					return true;
				}
				if (lastValue.getStatusCode() != null && value.getStatusCode() != null
						&& lastValue.getStatusCode().getValueAsIntBits() != value.getStatusCode().getValueAsIntBits()) {
					return true;
				}
				return false;
			}
		}
		// value changed if only one is null
		if (value == null || lastValue == null) {
			return lastValue != null || value != null;
		}
		// equal values do not change
		if (value.getStatusCode().equals(lastValue.getStatusCode())
				&& equals(lastValue.getValue().getValue(), value.getValue().getValue(), deadbandType, deadBand)) {
			return false;
		}
		return true;
	}

	public Node getNode() {
		return node;
	}

	public long getSubscriptionId() {
		return this.subscriptionId.longValue();
	}

	@Override
	public UnsignedInteger getAttributeId() {
		return attributeId;
	}

	@Override
	public long getMonitoredItemId() {
		return monitoredItemId;
	}

	public OPCServerSession getSession() {
		return session;
	}

	public String getIndexRangeAsString() {
		return this.indexRange;
	}

	public TimestampsToReturn getTimestampsToReturn() {
		return timestampsToReturn;
	}

	public MonitoringMode getMonitoringMode() {
		return monitoringMode;
	}

	@Override
	public UnsignedInteger getClientHandle() {
		return clientHandle;
	}

	public Double getSamplingInterval() {
		return samplingInterval;
	}

	public UnsignedInteger getQueueSize() {
		return queueSize;
	}

	protected Boolean getDiscardOldest() {
		return discardOldest;
	}

	protected MonitoringFilter getFilter() {
		return filter;
	}

	public MonitoredItemTypeMask getTypeMask() {
		return typeMask;
	}

	// public long getNextSamplingTime() {
	// return nextSamplingTime;
	// }
	public void setSubscriptionId(UnsignedInteger subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public void setAttributeId(UnsignedInteger attributeId) {
		this.attributeId = attributeId;
	}

	public void setMonitoredItemId(long monitoredItemId) {
		this.monitoredItemId = monitoredItemId;
	}

	public void setSession(OPCServerSession session) {
		this.session = session;
	}

	// public void setClientHandle(UnsignedInteger clientHandle) {
	// this.clientHandle = clientHandle;
	// }
	public void setFilter(MonitoringFilter filter) {
		this.filter = filter;
	}

	public void setTypeMask(MonitoredItemTypeMask typeMask) {
		this.typeMask = typeMask;
	}

	public void setFilterResult(ExtensionObject filterResult) {
		this.filterResult = filterResult;
	}

	public ExtensionObject getFilterResult() {
		return this.filterResult;
	}

	// public MonitoredItemTypeMask getMonitoredItemType() {
	// return this.monitoredItemType;
	// }
	public QualifiedName getDataEncoding() {
		return this.dataEncoding;
	}

	public OPCMonitoredItemQueue getQueue() {
		return this.queue;
	}

	public boolean getReadyToTrigger() {
		synchronized (this.lock) {
			// only allow to trigger if sampling or reporting.
			if (this.monitoringMode == MonitoringMode.Disabled) {
				return false;
			}
			return this.readyToTrigger;
		}
	}

	public void setReadyToTrigger(boolean readyToTrigger) {
		synchronized (this.lock) {
			this.readyToTrigger = readyToTrigger;
		}
	}

	@Override
	public UnsignedInteger getItemId() {
		return new UnsignedInteger(getMonitoredItemId());
	}

	/**
	 * Sets a flag indicating that the item has been triggered and should publish.
	 * 
	 * @return
	 */
	@Override
	public boolean isTriggered() {
		synchronized (this.lock) {
			if (this.readyToPublish) {
				// Utils.Trace("SetTriggered[{0}]", m_id);
				this.triggered = true;
				return true;
			}
		}
		return false;
	}

	protected boolean getReadyToPublish() {
		return this.readyToPublish;
	}

	protected boolean getTriggered() {
		return this.triggered;
	}

	protected long getNextSamplingTime() {
		return this.nextSamplingTime;
	}
	// public void setTriggered(boolean triggered) {
	// this.triggered = triggered;
	// }
	//
	// public boolean getTriggered() {
	// return this.triggered;
	// }
}
