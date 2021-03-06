package opc.sdk.ua.classes;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.core.SessionSecurityDiagnosticsDataType;

public class SessionSecurityDiagnosticsValue extends BaseVariableValue {
	protected SessionSecurityDiagnosticsDataType value = null;
	protected SessionSecurityDiagnosticsType variable = null;

	public SessionSecurityDiagnosticsValue(SessionSecurityDiagnosticsType variable,
			SessionSecurityDiagnosticsDataType value, Object dataLock) {
		super(dataLock);
		this.value = value;
		if (this.value == null) {
			this.value = new SessionSecurityDiagnosticsDataType();
		}
		initialize(variable);
	}

	public SessionSecurityDiagnosticsType getVariable() {
		return this.variable;
	}

	public SessionSecurityDiagnosticsDataType getValue() {
		return this.value;
	}

	public void setValue(SessionSecurityDiagnosticsDataType value) {
		this.value = value;
	}

	private void initialize(SessionSecurityDiagnosticsType variable) {
		synchronized (getLock()) {
			this.variable = variable;
			variable.setValue(this.value);
			List<BaseVariableType<?>> updateList = new ArrayList<>();
			updateList.add(variable);
			this.variable.getSessionId();
		}
	}
}
