package opc.client.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.ServiceFaultException;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.DataChangeNotification;
import org.opcfoundation.ua.core.EventFieldList;
import org.opcfoundation.ua.core.EventNotificationList;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.MonitoredItemNotification;
import org.opcfoundation.ua.core.NotificationMessage;
import org.opcfoundation.ua.core.SubscriptionAcknowledgement;
import org.opcfoundation.ua.core.TimestampsToReturn;
import org.opcfoundation.ua.encoding.DecodingException;
import org.opcfoundation.ua.encoding.EncoderContext;
import org.opcfoundation.ua.encoding.IEncodeable;

import opc.client.application.listener.SubscriptionDeleteListener;
import opc.client.application.listener.SubscriptionNotificationListener;
import opc.sdk.core.enums.RequestType;

/**
 * Subscription of an UA Client to store Monitored Items.
 *
 * @author Thomas Z&ouml;chbauer
 * @since 23.05.2012, HB-Softsolution e.U.
 */
public class Subscription {
	/** id of the subscription */
	private UnsignedInteger subscriptionId = null;
	/** custom name */
	private String displayName = null;
	/** interval to publish monitored item notifications */
	private Double publishingInterval = null;
	/** counter without sending messages when interval has expired */
	private UnsignedInteger keepAliveCount = null;
	/** counter to remove subscription when no messages has been sent */
	private UnsignedInteger lifetimeCount = null;
	/** number of maximum notifications to publish */
	private UnsignedInteger maxNotificationsPerPublish = null;
	/** flag to indicate publishing */
	private Boolean publishEnabled = null;
	/** Kind of timestamps to return */
	private TimestampsToReturn timestampsToReturn = null;
	/** number of maximum messages within a notification */
	private Integer maxMessageCount = null;
	/** datachange value cache */
	private LinkedList<NotificationMessage> messageCache = null;
	/** stored monitored items */
	private Map<UnsignedInteger, MonitoredItem> monitoredItems = null;
	/** subscription priority */
	private UnsignedByte priority = null;
	/** thread to handle keep alive tasks */
	private Timer keepAliveTimer = null;
	/** subscription lock */
	private Object lock = new Object();
	/** datetime from last received notifications */
	private DateTime lastNotificationTime = DateTime.MIN_VALUE;
	/** current publishing interval */
	private Double currentPublishingInterval = null;
	/** exceeded publish late count */
	private Integer publishLateCount = null;
	/** current keep alive count */
	private UnsignedInteger currentKeepAliveCount = null;
	/** containing client session */
	private ClientSession session = null;
	/** id of notification messages which are available to receive */
	private UnsignedInteger[] availableSequenceNumbers = null;
	/** publish service message queue */
	private LinkedList<IncomingMessage> incomingMessages = null;
	/** flag to prevent storing values inside monitored item value caches */
	private boolean disableMonitoredItemCache = false;
	/** listeners to notifiy on incoming messages */
	private List<SubscriptionNotificationListener> notificationListeners = null;
	/** keep alive task */
	private SubscriptionKeepAliveTask keepAliveTask = null;
	/** listener to notify subscription state changes (Delete) */
	private Queue<SubscriptionDeleteListener> deleteListeners = null;
	/** last number of publish message */
	private UnsignedInteger lastPublishMessageSeqNr;
	/** flag to indicate if only values with valid timestamps should accepted */
	private boolean onlyValidPublishTimestampValues = true;
	/** flag to indicate if there was a last message */
	private boolean wasLastMessage = false;
	private long maxPastTimeout = 60000;
	private long maxFutureTimeout = 1000;

	/**
	 * Subscriptions are used to report notifications. Initializes a default
	 * Subscription.
	 *
	 * @param Session Session where the subscription is stored.
	 */
	Subscription(ClientSession session) {
		this.session = session;
		this.subscriptionId = new UnsignedInteger();
		this.displayName = "Subscription";
		this.publishingInterval = 1000.0;
		this.keepAliveCount = new UnsignedInteger(10);
		this.lifetimeCount = new UnsignedInteger(10);
		this.maxNotificationsPerPublish = new UnsignedInteger(1000);
		this.publishEnabled = false;
		this.timestampsToReturn = TimestampsToReturn.Both;
		this.maxMessageCount = 25;
		this.messageCache = new LinkedList<>();
		this.monitoredItems = new ConcurrentHashMap<>();
		this.priority = new UnsignedByte(0);
		this.publishLateCount = 0;
		this.notificationListeners = new ArrayList<>();
		this.incomingMessages = new LinkedList<>();
		this.deleteListeners = new ConcurrentLinkedQueue<>();
		this.lastPublishMessageSeqNr = UnsignedInteger.ZERO;
	}

	/**
	 * Subscriptions are used to report notifications.
	 *
	 * @param Session                   Session where the subscription is stored.
	 * @param SubscriptionId            Unique id to identify subscription.
	 * @param KeepAliveCount            Number of consiquent publish cycles without
	 *                                  changes.
	 * @param PublishingInterval        Interval of an publish cycle.
	 * @param MaxNotificationPerPublish Number of maximum notifications to send at
	 *                                  once.
	 * @param Priority                  Priority of the subscription.
	 * @param PublishingEnabled         Flag to enable/disable publishing.
	 * @param LifetimeCount             Number of maximum publish cycles without
	 *                                  messages to delete subscription.
	 */
	public Subscription(ClientSession session, UnsignedInteger subscriptionId, UnsignedInteger keepAliveCount,
			Double publishingInterval, UnsignedInteger maxNotificationPerPublish, UnsignedByte priority,
			Boolean publishingEnabled, UnsignedInteger lifetimeCount) {
		this(session);
		this.subscriptionId = subscriptionId;
		this.keepAliveCount = keepAliveCount;
		this.currentKeepAliveCount = keepAliveCount;
		this.lifetimeCount = lifetimeCount;
		this.publishingInterval = publishingInterval;
		this.currentPublishingInterval = publishingInterval;
		if (maxNotificationPerPublish != null) {
			this.maxNotificationsPerPublish = maxNotificationPerPublish;
		}
		if (priority != null) {
			this.priority = priority;
		}
		if (publishingEnabled != null) {
			this.publishEnabled = publishingEnabled;
		}
		this.keepAliveTimer = new Timer("UA Subscription keep alive Timer - " + subscriptionId.intValue());
	}

	/**
	 * Adds a listener to notify subscription changes.
	 *
	 * @param Listeners Listeners to add
	 */
	public void addSubscriptionNotificationListener(SubscriptionNotificationListener... listeners) {
		synchronized (this.lock) {
			for (SubscriptionNotificationListener l : listeners) {
				this.notificationListeners.add(l);
			}
		}
	}

	/**
	 * Adds a listener to notify subscription state changes.
	 *
	 * @param Listeners Listeners to add
	 */
	public void addSubscriptionDeleteListener(SubscriptionDeleteListener... listeners) {
		synchronized (this.lock) {
			for (SubscriptionDeleteListener l : listeners) {
				this.deleteListeners.add(l);
			}
		}
	}

	/**
	 * Returns the id of the subscription
	 *
	 * @return SubscriptionId
	 */
	public UnsignedInteger getSubscriptionId() {
		return this.subscriptionId;
	}

	/**
	 * Remove subscription notification listeners.
	 *
	 * @param Listeners Listeners to remove.
	 */
	public void removeSubscriptionNotificationListener(SubscriptionNotificationListener... listeners) {
		synchronized (this.lock) {
			for (SubscriptionNotificationListener l : listeners) {
				this.notificationListeners.remove(l);
			}
		}
	}

	/**
	 * Remove subscription delete listeners.
	 *
	 * @param Listeners Listeners to remove.
	 */
	public void removeSubscriptionDeleteListener(SubscriptionDeleteListener... listeners) {
		synchronized (this.lock) {
			for (SubscriptionDeleteListener l : listeners) {
				this.deleteListeners.remove(l);
			}
		}
	}

	/**
	 * Returns all subscription state listeners.
	 *
	 * @return SubscriptionStateListeners
	 */
	public SubscriptionDeleteListener[] getSubscriptionDeleteListeners() {
		return this.deleteListeners.toArray(new SubscriptionDeleteListener[0]);
	}

	/**
	 * Returns the publishing interval.
	 *
	 * @return PublishingInterval
	 */
	public Double getPublishingInterval() {
		return this.currentPublishingInterval;
	}

	/**
	 * Returns the keep alive count.
	 *
	 * @return KeepAliveCount
	 */
	public UnsignedInteger getKeepAliveCount() {
		return this.keepAliveCount;
	}

	/**
	 * Returns the priority.
	 *
	 * @return Priority
	 */
	public UnsignedByte getPriority() {
		return this.priority;
	}

	/**
	 * Returns the lifetime count.
	 *
	 * @return LifetimeCount
	 */
	public UnsignedInteger getLifetimeCount() {
		return this.lifetimeCount;
	}

	/**
	 * Returns the Publishing interval.
	 *
	 * @return PublishingInterval
	 */
	public Double getPublishInterval() {
		return this.publishingInterval;
	}

	/**
	 * clears all monitoreditems from outside
	 */
	public void clearMonitoredItems() {
		this.monitoredItems.clear();
	}

	/**
	 * Return all monitored items.
	 *
	 * @return MonitoredItems
	 */
	public MonitoredItem[] getMonitoredItems() {
		synchronized (this.lock) {
			return this.monitoredItems.values().toArray(new MonitoredItem[0]);
		}
	}

	/**
	 * Returns a monitoreditem by its clienthandle.
	 *
	 * @param ClientHandle Handle of the monitored item
	 * @return MonitoredItem
	 */
	public MonitoredItem getMonitoredItemByClientHandle(UnsignedInteger clientHandle) {
		synchronized (this.lock) {
			return this.monitoredItems.get(clientHandle);
		}
	}

	/**
	 * Returns a monitoreditem by its id.
	 *
	 * @param MonitoredItemId Id of the monitoreditem
	 * @return MonitoredItem
	 */
	public MonitoredItem getMonitoredItemById(UnsignedInteger monitoredItemId) {
		for (MonitoredItem item : this.monitoredItems.values()) {
			if (monitoredItemId.equals(item.getMonitoredItemId())) {
				return item;
			}
		}
		return null;
	}

	/**
	 * Returns a displayname.
	 *
	 * @return Displayname
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Set a displayname.
	 *
	 * @param DisplayName Displayname to use.
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Sets a flag to accept only values with valid timestamps are stored.
	 *
	 * @param OnlyValid Flag to enable/disable.
	 */
	public void setOnlyValidTimestampPublishValuesAllowed(boolean onlyValid) {
		this.onlyValidPublishTimestampValues = onlyValid;
	}

	/**
	 * Flag which shows if value timestamps should be ignored if they are not valid
	 * (value timestamp > current time +/-1000 sec)!
	 *
	 * @return isTimestampPublishingAllowed
	 */
	public boolean isOnlyValidTimestampPublishValuesAllowed() {
		return this.onlyValidPublishTimestampValues;
	}

	/**
	 * Returns the number of maximum notifcations per publish sequence.
	 *
	 * @return MaxNotificationsPerPublish
	 */
	public UnsignedInteger getMaxNotificationsPerPublish() {
		return maxNotificationsPerPublish;
	}

	/**
	 * Enable/disable flag for the subscription to publish.
	 *
	 * @return TRUE means publishing is enabled, otherwise FALSE.
	 */
	public Boolean getPublishEnabled() {
		return publishEnabled;
	}

	/**
	 * Return which timestamps should recieved.
	 *
	 * @return TimestampsToReturn
	 */
	public TimestampsToReturn getTimestampsToReturn() {
		return timestampsToReturn;
	}

	/**
	 * Returns number of maximum messages within a notification.
	 *
	 * @return MaxMessageCount
	 */
	public Integer getMaxMessageCount() {
		return maxMessageCount;
	}

	/**
	 * Returns a timestamp from the last received notification.
	 *
	 * @return LastNotificationTime
	 */
	public DateTime getLastNotificationTime() {
		return lastNotificationTime;
	}

	/**
	 * Returns the pbulish late count.
	 *
	 * @return PublishLateCount
	 */
	public Integer getPublishLateCount() {
		return publishLateCount;
	}

	/**
	 * Sets the pbulish late count.
	 *
	 * @param PublishLateCount Number of allowed late publishes.
	 */
	public void setPublishLateCount(Integer publishLateCount) {
		this.publishLateCount = publishLateCount;
	}

	/**
	 * Returns the current keep alive count.
	 *
	 * @return CurrentKeepAliveCount
	 */
	public UnsignedInteger getCurrentKeepAliveCount() {
		return currentKeepAliveCount;
	}

	/**
	 * Returns the subscription's containing session.
	 *
	 * @return Session
	 */
	public ClientSession getSession() {
		return session;
	}

	/**
	 * Returns all subscription notification listeners.
	 *
	 * @return SubscriptionNotificationListeners
	 */
	public SubscriptionNotificationListener[] getNotificationListeners() {
		synchronized (this.lock) {
			return this.notificationListeners.toArray(new SubscriptionNotificationListener[0]);
		}
	}

	/**
	 * Adds an item to the subscription
	 *
	 * @param MonitoredItem with its clientHandle?
	 */
	protected boolean addItem(MonitoredItem monitoredItem) {
		if (monitoredItem == null) {
			throw new IllegalArgumentException("MonitoredItem");
		}
		synchronized (this.lock) {
			// we need not to check if the client handle exists, because of reconnect,
			// we replace the old monitored item
			/**
			 * if (this.monitoredItems.containsKey(monitoredItem.getClientHandle())) {
			 * Logger.getLogger(getClass().getName()).log(Level.SEVERE, "{0} Cannot add a
			 * monitored item with the same handle {1}!", new String[] {
			 * RequestType.CreateMonitoredItems.name(),
			 * monitoredItem.getClientHandle().toString() }); return false; }
			 */
			/**
			 * for (MonitoredItem item : this.monitoredItems.values()) { if
			 * (item.getMonitoredItemId().equals(monitoredItem.getMonitoredItemId())) {
			 * Logger.getLogger(getClass().getName()).log(Level.SEVERE, "{0} Cannot add a
			 * monitored item with the same id {1}!", new String[] {
			 * RequestType.CreateMonitoredItems.name(), item.getMonitoredItemId().toString()
			 * }); return false; } }
			 */
			this.monitoredItems.put(monitoredItem.getClientHandle(), monitoredItem);
			monitoredItem.setSubscription(this);
			return true;
		}
	}

	/**
	 * Add items to the subscription
	 *
	 * @param monitoredItemsToCreate
	 */
	protected void addItems(MonitoredItem[] monitoredItemsToCreate) {
		if (monitoredItemsToCreate == null || monitoredItemsToCreate.length <= 0) {
			throw new IllegalArgumentException("MonitoredItems");
		}
		for (MonitoredItem monitoredItemToAdd : monitoredItemsToCreate) {
			addItem(monitoredItemToAdd);
		}
	}

	protected void deleteMonitoredItem(UnsignedInteger monitoredItemId) {
		synchronized (this.lock) {
			MonitoredItem item = getMonitoredItemById(monitoredItemId);
			if (item != null) {
				item.clear();
				this.monitoredItems.remove(item.getClientHandle());
			}
		}
	}

	/**
	 * Adds the notification message to internal cache
	 *
	 * @param availableSequenceNumbers
	 * @param notificationMessage
	 * @param stringTable
	 * @param results
	 * @param isMoreNotifications
	 * @param publishExecutor
	 * @param notificationListeners
	 * @param listener
	 */
	protected boolean saveMessageInCache(RequestType requestType, UnsignedInteger[] availableSequenceNumbers,
			NotificationMessage message, boolean isRepublish, boolean isMoreNotifications,
			ExecutorService publishExecutor) {
		synchronized (this.lock) {
			// checks if an acknowledged sqnr is re-used
			if (availableSequenceNumbers != null) {
				// new available sequence numbers
				Set<UnsignedInteger> avsqnr = null;
				if (this.availableSequenceNumbers != null) {
					for (int i = 0; i < availableSequenceNumbers.length; i++) {
						UnsignedInteger uint = availableSequenceNumbers[i];
						boolean found = false;
						for (UnsignedInteger seqNr : this.availableSequenceNumbers) {
							if (seqNr.compareTo(uint) == 0) {
								found = true;
								break;
							}
						}
						// CTT 11.4- 006
						if (this.lastPublishMessageSeqNr.inc().compareTo(uint) < 0) {
							Logger.getLogger(getClass().getName()).log(Level.FINE,
									"{0} A invalid (bogus) acknowledged sequencenumber {1} for the subscription {2}!",
									new Object[] { requestType.name(), uint, this.subscriptionId });
							continue;
						} else if (this.lastPublishMessageSeqNr.getValue() > uint.getValue()) {
							Logger.getLogger(getClass().getName()).log(Level.FINE,
									"{0} A invalid previous acknowledged sequencenumber {1} for the subscription {2} has been sent again!",
									new Object[] { requestType.name(), uint, this.subscriptionId });
						}
						// init list
						if (avsqnr == null) {
							avsqnr = new HashSet<>();
						}
						avsqnr.add(uint);
						// CTT 11.4- 004-005
						if (found) {
							Logger.getLogger(getClass().getName()).log(Level.FINE,
									"{0} A previously acknowledged sequencenumber {1} for the subscription {2} are showing as unacknowledged again!",
									new Object[] { requestType.name(), uint, this.subscriptionId });
						}
					}
				}
				// acknowledges
				if (avsqnr != null) {
					this.availableSequenceNumbers = avsqnr.toArray(new UnsignedInteger[0]);
				}
				// first
				else {
					this.availableSequenceNumbers = availableSequenceNumbers;
				}
				// re-acknowledging
				if (this.availableSequenceNumbers.length > 0) {
					List<SubscriptionAcknowledgement> acks2add = null;
					for (UnsignedInteger acknowledge : this.availableSequenceNumbers) {
						for (SubscriptionAcknowledgement acks : this.session.getAcknowledgementsToSend()) {
							if (acks.getSequenceNumber().getValue() != acknowledge.getValue()) {
								if (acks2add == null) {
									acks2add = new ArrayList<>();
								}
								SubscriptionAcknowledgement ack2add = new SubscriptionAcknowledgement();
								ack2add.setSequenceNumber(acknowledge);
								ack2add.setSubscriptionId(this.subscriptionId);
								acks2add.add(ack2add);
							}
						}
					}
					if (acks2add != null) {
						this.session.getAcknowledgementsToSend().addAll(acks2add);
						acks2add.clear();
					}
				}
			}
			if (message == null) {
				return true;
			}
			// checks the seqnr of the message. Skips datachanges for
			// republished notifications.
			if (this.lastPublishMessageSeqNr.getValue() > 0 && message.getSequenceNumber() != null
					&& this.lastPublishMessageSeqNr.getValue() >= message.getSequenceNumber().getValue() && !isRepublish
					&& message.getNotificationData() != null && message.getNotificationData().length > 0) {
				if (isMoreNotifications) { // CTT 11.4-014
					Logger.getLogger(getClass().getName()).log(Level.SEVERE,
							"{0} The previous message has shown more notifications available for this message {1}, subscription {2} but nothing available! The notification data is dismissed!",
							new Object[] { requestType.name(), message.getSequenceNumber(), subscriptionId });
					return false;
				}
				// only if last was a message with notifications (not a keep
				// alive)
				if (this.wasLastMessage) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE,
							"{0} The previous message has the same sequence number {1} than the new message!",
							new Object[] { requestType.name(), message.getSequenceNumber() });
					return true;
				}
			}
			// checks the publish time that it is not in the future or past
			if ((DateTime.currentTime().getTimeInMillis() + maxFutureTimeout < message.getPublishTime()
					.getTimeInMillis())
					|| (DateTime.currentTime().getTimeInMillis() > message.getPublishTime().getTimeInMillis()
							+ maxPastTimeout)) {
				if (maxFutureTimeout >= 0 && maxPastTimeout >= 0) {
					Logger.getLogger(getClass().getName()).log(Level.INFO,
							"{0} The message {1} has an invalid publish timestamp {2} and the values are {3}! Current timestamp: {4}!",
							new Object[] { requestType.name(), message.getSequenceNumber(), message.getPublishTime(),
									(!this.onlyValidPublishTimestampValues) ? "allowed" : "rejected",
									DateTime.currentTime() });
				}
				// skips if values have an invalid timestamp
				if (this.onlyValidPublishTimestampValues) {
					if (maxFutureTimeout >= 0 && maxPastTimeout >= 0) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE,
								"{0} The message {1} has an invalid publish timestamp {2} and the values are {3}! Current timestamp: {4}!",
								new Object[] { requestType.name(), message.getSequenceNumber(),
										message.getPublishTime(),
										(!this.onlyValidPublishTimestampValues) ? "allowed" : "rejected",
										DateTime.currentTime() });
					}
					// remember last received message sequence number
					if (message.getSequenceNumber() != null
							&& message.getSequenceNumber().compareTo(this.lastPublishMessageSeqNr) > 0) {
						this.lastPublishMessageSeqNr = message.getSequenceNumber();
					}
					return true;
				}
			}
			this.lastNotificationTime = DateTime.currentTime();
			// save the string table that came with notification
			// create the queue for the first time
			if (this.incomingMessages == null) {
				this.incomingMessages = new LinkedList<>();
			}
			// find or create an entry for the incoming sequence number
			IncomingMessage entry = null;
			ListIterator<IncomingMessage> iterator = this.incomingMessages.listIterator(this.incomingMessages.size());
			while (iterator.hasPrevious()) {
				entry = iterator.previous();
				if (entry.getSequenceNumber().compareTo(message.getSequenceNumber()) == 0) {
					entry.setTimestamp(DateTime.currentTime());
					break;
				}
				if (entry.getSequenceNumber().compareTo(message.getSequenceNumber()) < 0) {
					entry = new IncomingMessage();
					entry.setSequenceNumber(message.getSequenceNumber());
					entry.setTimestamp(DateTime.currentTime());
					iterator.next();
					iterator.add(entry);
					break;
				}
				entry = null;
			}
			if (entry == null) {
				entry = new IncomingMessage();
				entry.setSequenceNumber(message.getSequenceNumber());
				entry.setTimestamp(DateTime.currentTime());
				this.incomingMessages.addLast(entry);
			}
			/** check for keep alive */
			if (message.getNotificationData() != null && message.getNotificationData().length > 0) {
				this.wasLastMessage = true;
				// monitored items in datachangenotification are null then
				// republish them
				// wegen CTT 11.7-1
				boolean hasMessage2republish = false;
				for (int i = 0; i < message.getNotificationData().length; i++) {
					try {
						IEncodeable data = message.getNotificationData()[i].decode(EncoderContext.getDefaultInstance());
						if (data instanceof DataChangeNotification) {
							if (((DataChangeNotification) data).getMonitoredItems() == null
									|| ((DataChangeNotification) data).getMonitoredItems().length == 0) {
								hasMessage2republish = true;
								break;
							} else if (this.maxNotificationsPerPublish
									.getValue() < ((DataChangeNotification) data).getMonitoredItems().length) {
								Logger.getLogger(getClass().getName()).log(Level.SEVERE,
										"{0} More notifications for subscription {1} in message {2} received ({3}) than expected ({4}) (server fault)!",
										new Object[] { requestType.name(), this.subscriptionId,
												message.getSequenceNumber(),
												Integer.toString(
														((DataChangeNotification) data).getMonitoredItems().length),
												this.maxNotificationsPerPublish });
							}
						}
					} catch (DecodingException e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
					}
				}
				if (!hasMessage2republish) {
					entry.setMessage(message);
				} else {
					if (isRepublish) {
						Logger.getLogger(getClass().getName()).log(Level.WARNING,
								"{0} Could not successful republish message for subscription {1} with the sequence number {2}(server fault)!",
								new Object[] { RequestType.Republish.name(), this.subscriptionId,
										message.getSequenceNumber() });
						entry.setMessage(message);
					}
					Logger.getLogger(getClass().getName()).log(Level.SEVERE,
							"{0} There are no notifications for monitored items available!",
							new Object[] { RequestType.Publish.name() });
				}
				entry.setProcessed(false);
			}
			/** check for response without data (wrong keepalive) */
			else if ((message.getNotificationData() == null || message.getNotificationData().length == 0)
					&& this.lastPublishMessageSeqNr.getValue() > 0) {
				if (message.getSequenceNumber().getValue() > this.lastPublishMessageSeqNr.getValue()
						&& !this.wasLastMessage) {
					// CTT 11.4-011
					Logger.getLogger(getClass().getName()).log(Level.SEVERE,
							"{0} A problematic publish notification with sequence number {1} on subscription {2} was received! The sequence number of the response get increased, but there are no messages (KeepAlive?)!",
							new Object[] { requestType.name(), message.getSequenceNumber(), this.subscriptionId });
				}
				this.wasLastMessage = false;
				entry.setProcessed(true);
			}
			// remember last received message sequence number
			if (message.getSequenceNumber() != null
					&& message.getSequenceNumber().compareTo(this.lastPublishMessageSeqNr) > 0) {
				this.lastPublishMessageSeqNr = message.getSequenceNumber();
			}
			// fill in any gaps in the queue
			iterator = this.incomingMessages.listIterator();
			try {
				entry = iterator.next();
			} catch (NoSuchElementException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				entry = null;
			}
			IncomingMessage next;
			while (iterator.hasNext()) {
				try {
					next = iterator.next();
				} catch (NoSuchElementException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
					next = null;
				}
				if (entry == null)
					throw new NullPointerException("entry is null");
				if (next != null && next.getSequenceNumber().longValue() > entry.getSequenceNumber().longValue() + 1) {
					IncomingMessage placeholder = new IncomingMessage();
					placeholder.setSequenceNumber(new UnsignedInteger(entry.getSequenceNumber().longValue() + 1));
					placeholder.setTimestamp(DateTime.currentTime());
					iterator.previous();
					iterator.add(placeholder);
					entry = placeholder;
					continue;
				}
				entry = next;
			}
			// clean out processed values
			iterator = this.incomingMessages.listIterator();
			while (iterator.hasNext()) {
				entry = iterator.next();
				// can only pull off processed or expired messages
				if (!entry.isProcessed() && !(entry.isRepublished()
						&& entry.getTimestamp().getTimeInMillis() < DateTime.currentTime().getTimeInMillis())) {
					break;
				}
				if (iterator.hasNext()) {
					iterator.remove();
				}
			}
			// process messages
			publishExecutor.execute(new Runnable() {
				@Override
				public void run() {
					onMessageRecieved();
				}
			});
		}
		return true;
	}

	/**
	 * Starts a timer to ensure publish requests are sent frequently enough to
	 * detect network interruptions
	 *
	 * @throws ServiceResultException
	 * @throws IllegalArgumentException
	 */
	protected void startKeepAliveTimer(boolean republish) throws ServiceResultException {
		synchronized (this.lock) {
			if (this.keepAliveTask != null) {
				this.keepAliveTask.cancel();
				this.keepAliveTask = null;
			}
			int keepAliveInterval = this.currentPublishingInterval.intValue() * this.keepAliveCount.intValue();
			this.lastNotificationTime = DateTime.currentTime();
			this.keepAliveTask = new SubscriptionKeepAliveTask(keepAliveInterval);
			this.keepAliveTimer.scheduleAtFixedRate(this.keepAliveTask, 0, keepAliveInterval);
		}
		// sent initial publish
		if (republish) {
			this.session.beginRepublish();
		}
		// else
		this.session.beginPublish();
	}

	protected void setKeepAliveCount(UnsignedInteger maxKeepAliveCount) {
		synchronized (this.lock) {
			if (maxKeepAliveCount != null) {
				this.keepAliveCount = maxKeepAliveCount;
			}
		}
	}

	protected void setPublishingInterval(Double publishingInterval, boolean republish) {
		synchronized (this.lock) {
			if (publishingInterval != null) {
				this.publishingInterval = publishingInterval;
				setCurrentPublishingInterval(publishingInterval);
			}
			try {
				startKeepAliveTimer(republish);
			} catch (ServiceResultException e) {
				// error in initialize publish
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			}
		}
	}

	protected void setPriority(UnsignedByte priority) {
		synchronized (this.lock) {
			if (priority != null) {
				this.priority = priority;
			}
		}
	}

	protected void setMaxNotificationPerPublish(UnsignedInteger maxNotificationsPerPublish) {
		synchronized (this.lock) {
			if (maxNotificationsPerPublish != null) {
				this.maxNotificationsPerPublish = maxNotificationsPerPublish;
			}
		}
	}

	protected void clear() {
		synchronized (this.lock) {
			this.availableSequenceNumbers = null;
			this.incomingMessages.clear();
			this.notificationListeners.clear();
			this.messageCache.clear();
			this.deleteListeners.clear();
			if (this.keepAliveTask != null) {
				this.keepAliveTask.cancel();
				this.keepAliveTask = null;
			}
		}
	}

	protected void setPublishEnabled(Boolean publishEnabled) {
		synchronized (this.lock) {
			this.publishEnabled = publishEnabled;
		}
	}

	protected void setTimestampsToReturn(TimestampsToReturn timestampsToReturn) {
		this.timestampsToReturn = timestampsToReturn;
	}

	protected Object getLock() {
		return lock;
	}

	protected void setCurrentKeepAliveCount(UnsignedInteger currentKeepAliveCount) {
		this.currentKeepAliveCount = currentKeepAliveCount;
	}

	protected void setSubscriptionId(UnsignedInteger subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	protected void setLifetimeCount(UnsignedInteger lifetimeCount) {
		synchronized (this.lock) {
			if (lifetimeCount != null) {
				this.lifetimeCount = lifetimeCount;
			}
		}
	}

	// protected void setKeepAliveTimer(Timer timer)
	// {
	// this.keepAliveTimer = timer;
	// }
	public void stopKeepAliveTimer() {
		if (this.keepAliveTimer != null) {
			this.keepAliveTimer.cancel();
			this.keepAliveTimer.purge();
			this.keepAliveTimer = null;
		}
	}

	protected void onDelete(final ClientSession session, final Subscription s) {
		for (SubscriptionDeleteListener listener : this.deleteListeners) {
			listener.onDeleted(session, s);
		}
		for (UnsignedInteger item : monitoredItems.keySet()) {
			deleteMonitoredItem(item);
		}
	}

	void reInitialize(ClientSession session) {
		this.session = session;
		this.lastPublishMessageSeqNr = UnsignedInteger.ZERO;
		this.availableSequenceNumbers = null;
		this.publishLateCount = 0;
		this.incomingMessages.clear();
	}

	/**
	 * Processes the incoming messages
	 *
	 * @throws ServiceResultException
	 * @throws ServiceFaultException
	 * @throws IllegalArgumentException
	 */
	private void onMessageRecieved()/** throws ServiceResultException */
	{
		ClientSession tmpSession;
		UnsignedInteger tmpSubscriptionId;
		// get list of new messages to process
		List<NotificationMessage> messagesToProcess = null;
		// get list of new messages to republish
		List<IncomingMessage> messagesToRepublish = null;
		synchronized (this.lock) {
			ListIterator<IncomingMessage> iterator = this.incomingMessages.listIterator();
			while (iterator.hasNext()) {
				IncomingMessage message = iterator.next();
				// update monitord items with unprocessed messages
				if (message.getMessage() != null && !message.isProcessed()) {
					if (messagesToProcess == null) {
						messagesToProcess = new ArrayList<>();
					}
					messagesToProcess.add(message.getMessage());
					// remove oldest item
					while (this.messageCache.size() > this.maxMessageCount) {
						this.messageCache.removeFirst();
					}
					this.messageCache.addLast(message.getMessage());
					message.setProcessed(true);
				}
				// check for missing messages
				if (iterator.hasNext() && message.getMessage() == null && !message.isProcessed()
						&& !message.isRepublished()
						&& message.getTimestamp().getTimeInMillis() + 2000 < DateTime.currentTime().getTimeInMillis()) {
					if (messagesToRepublish == null) {
						messagesToRepublish = new ArrayList<>();
					}
					messagesToRepublish.add(message);
					message.setRepublished(true);
				}
			}
			tmpSession = this.session;
			tmpSubscriptionId = this.subscriptionId;
		}
		// process new messages
		if (messagesToProcess != null) {
			for (int i = 0; i < messagesToProcess.size(); i++) {
				NotificationMessage message = messagesToProcess.get(i);
				for (int j = 0; j < message.getNotificationData().length; j++) {
					Object data = null;
					try {
						data = message.getNotificationData()[j].decode(EncoderContext.getDefaultInstance());
					} catch (DecodingException e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
						continue;
					}
					/** Data Change */
					if (data instanceof DataChangeNotification) {
						// nothing to do, skip
						if (((DataChangeNotification) data).getMonitoredItems() == null
								|| ((DataChangeNotification) data).getMonitoredItems().length == 0) {
							continue;
						}
						if (!this.disableMonitoredItemCache) {
							saveDataChange((DataChangeNotification) data);
						}
					}
					/** Event */
					else if (data instanceof EventNotificationList && !this.disableMonitoredItemCache) {
						saveEvents((EventNotificationList) data);
					}
				}
			}
		}
		// do any republishes
		if (messagesToRepublish != null && tmpSession != null && tmpSubscriptionId.longValue() != 0) {
			for (int ii = 0; ii < messagesToRepublish.size(); ii++) {
				try {
					if (!tmpSession.beginRepublish(tmpSubscriptionId, messagesToRepublish.get(ii).getSequenceNumber(),
							Identifiers.RepublishRequest)) {
						messagesToRepublish.get(ii).setRepublished(false);
					}
				} catch (ServiceResultException ex) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage());
				}
			}
		}
	}

	/**
	 * Save an event in the monitored item cache
	 *
	 * @param Message NotificationMessage of the Event
	 * @param Data    EventNotificationList of with the EventDataStructure.
	 */
	private void saveEvents(final EventNotificationList notifications) {
		final EventElement[] events = new EventElement[notifications.getEvents().length];
		for (int i = 0; i < notifications.getEvents().length; i++) {
			EventFieldList eventFields = notifications.getEvents()[i];
			MonitoredItem monitoredItem;
			// lookup monitored item
			synchronized (this.lock) {
				if ((monitoredItem = this.monitoredItems.get(eventFields.getClientHandle())) == null) {
					continue;
				}
			}
			EventElement event = new EventElement(monitoredItem, eventFields);
			monitoredItem.saveValueInCache(event);
			events[i] = event;
		}
		if (this.notificationListeners != null && !this.notificationListeners.isEmpty()) {
			final Subscription subscription = this;
			for (SubscriptionNotificationListener listener : notificationListeners) {
				listener.receiveNotificationMessage(subscription, events);
			}
		}
	}

	/**
	 * Save a data change in the monitored item cache
	 *
	 * @param message
	 * @param listener
	 * @param data
	 * @return
	 */
	private void saveDataChange(DataChangeNotification notifications) {
		for (int ii = 0; ii < notifications.getMonitoredItems().length; ii++) {
			final MonitoredItemNotification notification = notifications.getMonitoredItems()[ii];
			// lookup monitored item
			MonitoredItem monitoredItem;
			synchronized (this.lock) {
				if ((monitoredItem = this.monitoredItems.get(notification.getClientHandle())) == null) {
					// do not log info, this can happen if a monitoreditem was deleted and this is
					// the last callback
					continue;
				}
			}
			// save in cache
			monitoredItem.saveValueInCache(notification);
			final Subscription s = this;
			final MonitoredItem m = monitoredItem;
			for (SubscriptionNotificationListener listener : getNotificationListeners()) {
				listener.receiveNotificationMessage(s, m, notification.getValue());
			}
		}
	}

	private void setCurrentPublishingInterval(Double interval) {
		this.currentPublishingInterval = interval;
	}

	public long getMaxPastTimeout() {
		return maxPastTimeout;
	}

	public void setMaxPastTimeout(long maxPastTimeout) {
		this.maxPastTimeout = maxPastTimeout;
	}

	public long getMaxFutureTimeout() {
		return maxFutureTimeout;
	}

	public void setMaxFutureTimeout(long maxFutureTimeout) {
		this.maxFutureTimeout = maxFutureTimeout;
	}

	class SubscriptionKeepAliveTask extends TimerTask {
		private int keepAliveInterval = -1;

		public SubscriptionKeepAliveTask(int keepAliveInterval) {
			this.keepAliveInterval = keepAliveInterval;
		}

		@Override
		public void run() {
			synchronized (lock) {
				if (!publishingStopped()) {
					return;
				}
				publishLateCount++;
			}
		}

		/**
		 * Returns true if the subscription is not receiving publishes
		 *
		 * @return
		 */
		private boolean publishingStopped() {
			synchronized (lock) {
				int tmpKeepAliveInterval = (int) (currentPublishingInterval * currentKeepAliveCount.intValue());
				if (lastNotificationTime.getTimeInMillis() + (tmpKeepAliveInterval + 500) < DateTime.currentTime()
						.getTimeInMillis()) {
					return true;
				}
			}
			return false;
		}

		public int getKeepAliveInterval() {
			return keepAliveInterval;
		}

		public void setKeepAliveInterval(int keepAliveInterval) {
			this.keepAliveInterval = keepAliveInterval;
		}
	}

	/**
	 * A message received from the server cached until is processed or discarded.
	 *
	 * @author Arbeit
	 */
	class IncomingMessage {
		private UnsignedInteger sequenceNumber = null;
		private DateTime timestamp = null;
		private NotificationMessage message = null;
		private boolean processed = false;
		private boolean republished = false;

		public IncomingMessage() {
			// default empty implementation of incomingMessage
		}

		public IncomingMessage(NotificationMessage message, UnsignedInteger sequenceNumber, DateTime timestamp,
				boolean processed, boolean republished) {
			this.sequenceNumber = sequenceNumber;
			this.timestamp = timestamp;
			this.message = message;
			this.processed = processed;
			this.republished = republished;
		}

		public UnsignedInteger getSequenceNumber() {
			return sequenceNumber;
		}

		public void setSequenceNumber(UnsignedInteger sequenceNumber) {
			this.sequenceNumber = sequenceNumber;
		}

		public DateTime getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(DateTime timestamp) {
			this.timestamp = timestamp;
		}

		public NotificationMessage getMessage() {
			return message;
		}

		public void setMessage(NotificationMessage message) {
			this.message = message;
		}

		public boolean isProcessed() {
			return processed;
		}

		public void setProcessed(boolean processed) {
			this.processed = processed;
		}

		public boolean isRepublished() {
			return republished;
		}

		public void setRepublished(boolean republished) {
			this.republished = republished;
		}
	}
}
