package opc.sdk.core.application;

/**
 * Enum for an element type.
 * 
 * @author Thomas Z&ouml;chbauer
 * 
 */
public enum ElementType {
	ANYHIERACHICAL(0x01), ANYCOMPONENT(0x02), FORWARDREFERENCE(0x03), INVERSEREFERENCE(0x04);

	private Integer value = null;

	ElementType(int value) {
		this.setValue(value);
	}

	public Integer getValue() {
		return value;
	}

	private void setValue(Integer value) {
		this.value = value;
	}
}
