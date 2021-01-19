package opc.sdk.core.node;

public enum NodeIdMode {
	/**
	 * Fill nodeids from beginning
	 */
	FILL,
	/**
	 * Add nodeids at end
	 */
	APPEND,
	/**
	 * Continue parent nodeid
	 */
	CONTINUE;

	public static NodeIdMode getValue(int value) {
		switch (value) {
		case 0:
			return FILL;
		case 1:
			return APPEND;
		case 2:
			return CONTINUE;
		default:
			throw new IllegalArgumentException();
		}
	}
}
