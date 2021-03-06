package opc.sdk.ua.classes;

import org.opcfoundation.ua.core.SubscriptionDiagnosticsDataType;

public class SubscriptionDiagnosticsValue extends BaseVariableValue {
	protected SubscriptionDiagnosticsDataType value = null;
	protected SubscriptionDiagnosticsType variable = null;

	/**
	 * Initializes the instance with its defalt attribute values.
	 * 
	 * @param variable
	 * @param value
	 * @param dataLock
	 */
	public SubscriptionDiagnosticsValue(SubscriptionDiagnosticsType variable, SubscriptionDiagnosticsDataType value,
			Object dataLock) {
		super(dataLock);
		this.value = value;
		if (this.value == null) {
			this.value = new SubscriptionDiagnosticsDataType();
		}
		initialize(variable);
	}

	/**
	 * The variable that the value belongs to.
	 * 
	 * @return Variable
	 */
	public SubscriptionDiagnosticsType getVariable() {
		return this.variable;
	}

	/**
	 * Get the value of the variable.
	 * 
	 * @return Value
	 */
	public SubscriptionDiagnosticsDataType getValue() {
		return this.value;
	}

	/**
	 * Initializes the object
	 * 
	 * @param variable
	 */
	private void initialize(SubscriptionDiagnosticsType variable) {
		synchronized (this.getLock()) {
			this.variable = variable;
			variable.setValue(this.value);
		}
	}
}
