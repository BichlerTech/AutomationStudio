package opc.sdk.ua.classes;

import org.opcfoundation.ua.core.SessionDiagnosticsDataType;

public class SessionDiagnosticsVariableValue extends BaseVariableValue {
	protected SessionDiagnosticsDataType value = null;
	protected SessionDiagnosticsVariableType variable = null;

	public SessionDiagnosticsVariableValue(SessionDiagnosticsDataType value, Object dataLock) {
		super(dataLock);
		this.value = value;
		if (this.value == null) {
			this.value = new SessionDiagnosticsDataType();
		}
	}

	public SessionDiagnosticsDataType getValue() {
		return this.value;
	}

	public void setValue(SessionDiagnosticsDataType value) {
		this.value = value;
	}

	public SessionDiagnosticsVariableType getVariable() {
		return this.variable;
	}
}
