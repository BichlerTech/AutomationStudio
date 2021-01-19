package opc.sdk.core.enums;

/**
 * Enum for monitoreditem types
 * 
 * @author Thomas Z&ouml;bauer
 * 
 */
public enum MonitoredItemTypeMask {
	/**
	 * Defines constants for the monitored item type.
	 */
	/**
	 * The monitored item subscribes to data changes.
	 */
	DataChange(0x1),
	/**
	 * The monitored item subscribes to events.
	 */
	Events(0x2),
	/**
	 * The monitored item subscribes to all events produced by the server.
	 */
	AllEvents(0x4),
	/**
	 * Undefined, eg. Non-Attribte Value
	 */
	Undefined(0x8);

	private Integer mask = null;

	private MonitoredItemTypeMask(int value) {
		this.mask = value;
	}

	public int getValue() {
		return this.mask;
	}
}
