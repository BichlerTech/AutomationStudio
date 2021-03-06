package opc.client.application.listener;

import opc.client.service.EventElement;
import opc.client.service.MonitoredItem;
import opc.client.service.Subscription;

import org.opcfoundation.ua.builtintypes.DataValue;

/**
 * Listener on a Subscription to register to recieve Notifications of
 * MonitoredItems to DataChanges or Events.
 * 
 * @author Thomas Z&ouml;chbauer
 * @since 23.05.2012, HB-Softsolution e.U.
 */
public interface SubscriptionNotificationListener {
	/**
	 * Gives notification that there was a DataChange or Event.
	 * 
	 * @param Subscription  Subscription that has been notified.
	 * @param MonitoredItem MonitoredItem that has changed.
	 * @param Value         Value that has been changed.
	 */
	void receiveNotificationMessage(Subscription subscription, MonitoredItem monitoredItem, DataValue value);

	void receiveNotificationMessage(Subscription subscription, EventElement[] events);
}
