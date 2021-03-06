package opc.client.application.listener;

import opc.client.service.EventElement;
import opc.client.service.MonitoredItem;

import org.opcfoundation.ua.builtintypes.NodeId;

/**
 * Listener on a MonitoredItem to register to receive notifications of an Event.
 * 
 * @author Thomas Z&ouml;chbauer
 * @since 23.05.2012, HB-Softsolution e.U.
 * 
 */
public interface EventMonitorListener extends MonitorListener {
	void monitorEvent(MonitoredItem monitoredItem, NodeId eventId, EventElement eventElement);
}
