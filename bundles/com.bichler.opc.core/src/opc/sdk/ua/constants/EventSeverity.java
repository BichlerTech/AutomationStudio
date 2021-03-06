package opc.sdk.ua.constants;

import org.opcfoundation.ua.builtintypes.UnsignedShort;

/**
 * Enum of different event serverities.
 * 
 * @author Thomas Z&ouml;bauer
 *
 */
public enum EventSeverity {
	/**
	 * The highest possible severity.
	 */
	MAX(1000),
	/**
	 * The event has high severity.
	 */
	HIGH(900),
	/**
	 * The event has medium high severity.
	 */
	MEDIUMHIGH(700),
	/**
	 * The event has medium severity.
	 */
	MEDIUM(500),
	/**
	 * The event has medium-low severity.
	 */
	MEDIUMLOW(300),
	/**
	 * The event has low severity.
	 */
	LOW(100),
	/**
	 * The lowest possible severity.
	 */
	MIN(1);

	private int severity;

	private EventSeverity(int severity) {
		this.severity = severity;
	}

	public UnsignedShort severity() {
		return new UnsignedShort(this.severity);
	}
}
