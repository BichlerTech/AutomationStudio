package opc.client.application.listener;

import opc.client.service.MonitoredItem;

import org.opcfoundation.ua.builtintypes.DataValue;

/**
 * Listener on a MonitoredItem to register to receive notifications of changes
 * to a DataChange.
 * 
 * @author Thomas Z&ouml;chbauer
 * @since 23.05.2012, HB-Softsolution e.U.
 * 
 */
public interface DataChangeMonitorListener extends MonitorListener {
	void monitorDataChange(MonitoredItem item, DataValue newValue, DataValue oldValue);
}
