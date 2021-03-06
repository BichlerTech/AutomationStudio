package opc.sdk.ua.constants;

/**
 * Enum for variable copy.
 * 
 * @author Thomas Z&ouml;bauer
 *
 */
public enum VariableCopyPolicy {
	/**
	 * The value is copied when is is read.
	 */
	COPYONREAD(0x1),
	/**
	 * The value is copied before it is written.
	 * 
	 */
	COPYONWRITE(0x2),
	/**
	 * The value is never copied (only useful for value types that do not contain
	 * reference types).
	 */
	NEVER(0x0),
	/**
	 * Data is copied when it is written and when it is read.
	 */
	ALWAYS(0x3);

	private Integer value = null;

	VariableCopyPolicy(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}
}
