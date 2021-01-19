package opc.sdk.core.classes.ua.core;

public class ServerDiagnosticsType extends BaseObjectType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.ServerDiagnosticsType;
	private SubscriptionDiagnosticsArrayType subscriptionDiagnosticsArray;
	private PropertyType enabledFlag;
	private SamplingIntervalDiagnosticsArrayType samplingIntervalDiagnosticsArray;
	private SessionsDiagnosticsSummaryType sessionsDiagnosticsSummary;
	private ServerDiagnosticsSummaryType serverDiagnosticsSummary;

	public ServerDiagnosticsType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public SubscriptionDiagnosticsArrayType getSubscriptionDiagnosticsArray() {
		return subscriptionDiagnosticsArray;
	}

	public void setSubscriptionDiagnosticsArray(SubscriptionDiagnosticsArrayType value) {
		subscriptionDiagnosticsArray = value;
	}

	public PropertyType getEnabledFlag() {
		return enabledFlag;
	}

	public void setEnabledFlag(PropertyType value) {
		enabledFlag = value;
	}

	public SamplingIntervalDiagnosticsArrayType getSamplingIntervalDiagnosticsArray() {
		return samplingIntervalDiagnosticsArray;
	}

	public void setSamplingIntervalDiagnosticsArray(SamplingIntervalDiagnosticsArrayType value) {
		samplingIntervalDiagnosticsArray = value;
	}

	public SessionsDiagnosticsSummaryType getSessionsDiagnosticsSummary() {
		return sessionsDiagnosticsSummary;
	}

	public void setSessionsDiagnosticsSummary(SessionsDiagnosticsSummaryType value) {
		sessionsDiagnosticsSummary = value;
	}

	public ServerDiagnosticsSummaryType getServerDiagnosticsSummary() {
		return serverDiagnosticsSummary;
	}

	public void setServerDiagnosticsSummary(ServerDiagnosticsSummaryType value) {
		serverDiagnosticsSummary = value;
	}

	@Override
	public String toString() {
		return "ServerDiagnosticsType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
