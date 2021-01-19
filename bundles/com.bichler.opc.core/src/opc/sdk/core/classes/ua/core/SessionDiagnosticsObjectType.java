package opc.sdk.core.classes.ua.core;

public class SessionDiagnosticsObjectType extends BaseObjectType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.SessionDiagnosticsObjectType;
	private SessionDiagnosticsVariableType sessionDiagnostics;
	private SessionSecurityDiagnosticsType sessionSecurityDiagnostics;
	private SubscriptionDiagnosticsArrayType subscriptionDiagnosticsArray;

	public SessionDiagnosticsObjectType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public SessionDiagnosticsVariableType getSessionDiagnostics() {
		return sessionDiagnostics;
	}

	public void setSessionDiagnostics(SessionDiagnosticsVariableType value) {
		sessionDiagnostics = value;
	}

	public SessionSecurityDiagnosticsType getSessionSecurityDiagnostics() {
		return sessionSecurityDiagnostics;
	}

	public void setSessionSecurityDiagnostics(SessionSecurityDiagnosticsType value) {
		sessionSecurityDiagnostics = value;
	}

	public SubscriptionDiagnosticsArrayType getSubscriptionDiagnosticsArray() {
		return subscriptionDiagnosticsArray;
	}

	public void setSubscriptionDiagnosticsArray(SubscriptionDiagnosticsArrayType value) {
		subscriptionDiagnosticsArray = value;
	}

	@Override
	public String toString() {
		return "SessionDiagnosticsObjectType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
