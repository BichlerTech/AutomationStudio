package opc.sdk.core.application;

/**
 * Enum for the binary encoding support.
 * 
 * @author Thomas Z&ouml;chbauer
 * 
 */
public enum BinaryEncodingSupport {
	/**
	 * The UA binary encoding may be used
	 */
	OPTIONAL,
	/**
	 * The UA binary encoding must be used
	 */
	REQUIRED,
	/**
	 * The UA binary encoding may not be used
	 */
	NONE;
}
