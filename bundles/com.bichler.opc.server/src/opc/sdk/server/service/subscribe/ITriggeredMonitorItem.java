package opc.sdk.server.service.subscribe;

import org.opcfoundation.ua.builtintypes.UnsignedInteger;

public interface ITriggeredMonitorItem {
	/**
	 * The identifier for the item that is unique within the server.
	 * 
	 * @return
	 */
	public UnsignedInteger getItemId();

	/**
	 * Flags the monitored item as triggered.
	 * 
	 * @return True if there is something to publish.
	 */
	public boolean isTriggered();
}
