package opc.sdk.core.classes.ua.core;

public class SessionsDiagnosticsSummaryType extends BaseObjectType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.SessionsDiagnosticsSummaryType;
	private SessionSecurityDiagnosticsArrayType sessionSecurityDiagnosticsArray;
	private SessionDiagnosticsArrayType sessionDiagnosticsArray;

	public SessionsDiagnosticsSummaryType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public SessionSecurityDiagnosticsArrayType getSessionSecurityDiagnosticsArray() {
		return sessionSecurityDiagnosticsArray;
	}

	public void setSessionSecurityDiagnosticsArray(SessionSecurityDiagnosticsArrayType value) {
		sessionSecurityDiagnosticsArray = value;
	}

	public SessionDiagnosticsArrayType getSessionDiagnosticsArray() {
		return sessionDiagnosticsArray;
	}

	public void setSessionDiagnosticsArray(SessionDiagnosticsArrayType value) {
		sessionDiagnosticsArray = value;
	}

	@Override
	public String toString() {
		return "SessionsDiagnosticsSummaryType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
