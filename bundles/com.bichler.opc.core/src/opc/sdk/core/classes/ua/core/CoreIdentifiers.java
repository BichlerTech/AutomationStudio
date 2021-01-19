package opc.sdk.core.classes.ua.core;

import org.opcfoundation.ua.builtintypes.NodeId;
import opc.sdk.core.node.NodeIdUtil;

public class CoreIdentifiers {
	public static final NodeId ProgramTransitionAuditEventType = init(0, 3806);
	public static final NodeId StateVariableType = init(0, 2755);
	public static final NodeId AuditHistoryUpdateEventType = init(0, 2104);
	public static final NodeId AuditHistoryAtTimeDeleteEventType = init(0, 3019);
	public static final NodeId DiscreteAlarmType = init(0, 10523);
	public static final NodeId SessionDiagnosticsArrayType = init(0, 2196);
	public static final NodeId SessionDiagnosticsVariableType = init(0, 2197);
	public static final NodeId AuditHistoryEventDeleteEventType = init(0, 3022);
	public static final NodeId TransitionVariableType = init(0, 2762);
	public static final NodeId FiniteStateVariableType = init(0, 2760);
	public static final NodeId AuditUpdateEventType = init(0, 2099);
	public static final NodeId NonExclusiveRateOfChangeAlarmType = init(0, 10214);
	public static final NodeId AuditHistoryRawModifyDeleteEventType = init(0, 3014);
	public static final NodeId ServerType = init(0, 2004);
	public static final NodeId AuditWriteUpdateEventType = init(0, 2100);
	public static final NodeId AggregateConfigurationType = init(0, 11187);
	public static final NodeId FiniteTransitionVariableType = init(0, 2767);
	public static final NodeId AuditDeleteReferencesEventType = init(0, 2097);
	public static final NodeId TwoStateVariableType = init(0, 8995);
	public static final NodeId ExclusiveRateOfChangeAlarmType = init(0, 9623);
	public static final NodeId ShelvedStateMachineType = init(0, 2929);
	public static final NodeId AuditAddReferencesEventType = init(0, 2095);
	public static final NodeId ExclusiveLevelAlarmType = init(0, 9482);
	public static final NodeId AuditAddNodesEventType = init(0, 2091);
	public static final NodeId DataItemType = init(0, 2365);
	public static final NodeId AuditHistoryDeleteEventType = init(0, 3012);
	public static final NodeId FiniteStateMachineType = init(0, 2771);
	public static final NodeId ExclusiveLimitStateMachineType = init(0, 9318);
	public static final NodeId ConditionVariableType = init(0, 9002);
	public static final NodeId AuditDeleteNodesEventType = init(0, 2093);
	public static final NodeId GeneralModelChangeEventType = init(0, 2133);
	public static final NodeId BaseModelChangeEventType = init(0, 2132);
	public static final NodeId TwoStateDiscreteType = init(0, 2373);
	public static final NodeId DeviceFailureEventType = init(0, 2131);
	public static final NodeId DiscreteItemType = init(0, 2372);
	public static final NodeId AuditConditionConfirmEventType = init(0, 8961);
	public static final NodeId SystemEventType = init(0, 2130);
	public static final NodeId AnalogItemType = init(0, 2368);
	public static final NodeId AuditHistoryValueUpdateEventType = init(0, 3006);
	public static final NodeId AlarmConditionType = init(0, 2915);
	public static final NodeId AuditHistoryEventUpdateEventType = init(0, 2999);
	public static final NodeId AuditUpdateMethodEventType = init(0, 2127);
	public static final NodeId MultiStateDiscreteType = init(0, 2376);
	public static final NodeId SemanticChangeEventType = init(0, 2738);
	public static final NodeId ProgramTransitionEventType = init(0, 2378);
	public static final NodeId NonExclusiveLevelAlarmType = init(0, 10060);
	public static final NodeId StateMachineType = init(0, 2299);
	public static final NodeId ProgramDiagnosticType = init(0, 2380);
	public static final NodeId AuditConditionCommentEventType = init(0, 2829);
	public static final NodeId AuditUrlMismatchEventType = init(0, 2748);
	public static final NodeId DialogConditionType = init(0, 2830);
	public static final NodeId ExclusiveLimitAlarmType = init(0, 9341);
	public static final NodeId VendorServerInfoType = init(0, 2033);
	public static final NodeId ServerRedundancyType = init(0, 2034);
	public static final NodeId MultiStateValueDiscreteType = init(0, 11238);
	public static final NodeId InitialStateType = init(0, 2309);
	public static final NodeId AuditConditionAcknowledgeEventType = init(0, 8944);
	public static final NodeId AuditSessionEventType = init(0, 2069);
	public static final NodeId StateType = init(0, 2307);
	public static final NodeId TransparentRedundancyType = init(0, 2036);
	public static final NodeId AuditChannelEventType = init(0, 2059);
	public static final NodeId ServerDiagnosticsSummaryType = init(0, 2150);
	public static final NodeId OffNormalAlarmType = init(0, 10637);
	public static final NodeId TransitionEventType = init(0, 2311);
	public static final NodeId TransitionType = init(0, 2310);
	public static final NodeId SessionDiagnosticsObjectType = init(0, 2029);
	public static final NodeId AuditOpenSecureChannelEventType = init(0, 2060);
	public static final NodeId AuditConditionEnableEventType = init(0, 2803);
	public static final NodeId AcknowledgeableConditionType = init(0, 2881);
	public static final NodeId AuditUpdateStateEventType = init(0, 2315);
	public static final NodeId BaseObjectType = init(0, 58);
	public static final NodeId ServerVendorCapabilityType = init(0, 2137);
	public static final NodeId ExclusiveDeviationAlarmType = init(0, 9764);
	public static final NodeId ServerStatusType = init(0, 2138);
	public static final NodeId AuditSecurityEventType = init(0, 2058);
	public static final NodeId HistoricalDataConfigurationType = init(0, 2318);
	public static final NodeId BaseVariableType = init(0, 62);
	public static final NodeId NonTransparentRedundancyType = init(0, 2039);
	public static final NodeId BaseDataVariableType = init(0, 63);
	public static final NodeId NonExclusiveLimitAlarmType = init(0, 9906);
	public static final NodeId BuildInfoType = init(0, 3051);
	public static final NodeId BaseEventType = init(0, 2041);
	public static final NodeId FolderType = init(0, 61);
	public static final NodeId AuditEventType = init(0, 2052);
	public static final NodeId ServerCapabilitiesType = init(0, 2013);
	public static final NodeId PropertyType = init(0, 68);
	public static final NodeId DataTypeDescriptionType = init(0, 69);
	public static final NodeId AuditConditionRespondEventType = init(0, 8927);
	public static final NodeId AuditCertificateUntrustedEventType = init(0, 2087);
	public static final NodeId AuditCertificateInvalidEventType = init(0, 2086);
	public static final NodeId HistoricalEventConfigurationType = init(0, 2329);
	public static final NodeId AuditNodeManagementEventType = init(0, 2090);
	public static final NodeId LimitAlarmType = init(0, 2955);
	public static final NodeId HistoryServerCapabilitiesType = init(0, 2330);
	public static final NodeId AuditCertificateMismatchEventType = init(0, 2089);
	public static final NodeId AuditCertificateRevokedEventType = init(0, 2088);
	public static final NodeId ConditionType = init(0, 2782);
	public static final NodeId SubscriptionDiagnosticsType = init(0, 2172);
	public static final NodeId AuditCertificateDataMismatchEventType = init(0, 2082);
	public static final NodeId LockType = init(0, 8921);
	public static final NodeId DataTypeDictionaryType = init(0, 72);
	public static final NodeId SubscriptionDiagnosticsArrayType = init(0, 2171);
	public static final NodeId AuditCertificateEventType = init(0, 2080);
	public static final NodeId SessionSecurityDiagnosticsArrayType = init(0, 2243);
	public static final NodeId AuditCertificateExpiredEventType = init(0, 2085);
	public static final NodeId SessionSecurityDiagnosticsType = init(0, 2244);
	public static final NodeId TripAlarmType = init(0, 10751);
	public static final NodeId RefreshStartEventType = init(0, 2787);
	public static final NodeId RefreshEndEventType = init(0, 2788);
	public static final NodeId AuditActivateSessionEventType = init(0, 2075);
	public static final NodeId RefreshRequiredEventType = init(0, 2789);
	public static final NodeId ModellingRuleType = init(0, 77);
	public static final NodeId SessionsDiagnosticsSummaryType = init(0, 2026);
	public static final NodeId AuditConditionShelvingEventType = init(0, 11093);
	public static final NodeId DataTypeEncodingType = init(0, 76);
	public static final NodeId NonExclusiveDeviationAlarmType = init(0, 10368);
	public static final NodeId AggregateFunctionType = init(0, 2340);
	public static final NodeId EventQueueOverflowEventType = init(0, 3035);
	public static final NodeId DataTypeSystemType = init(0, 75);
	public static final NodeId AuditCancelEventType = init(0, 2078);
	public static final NodeId AuditCreateSessionEventType = init(0, 2071);
	public static final NodeId BaseConditionClassType = init(0, 11163);
	public static final NodeId ProcessConditionClassType = init(0, 11164);
	public static final NodeId MaintenanceConditionClassType = init(0, 11165);
	public static final NodeId ServerDiagnosticsType = init(0, 2020);
	public static final NodeId SamplingIntervalDiagnosticsType = init(0, 2165);
	public static final NodeId SystemConditionClassType = init(0, 11166);
	public static final NodeId AuditConditionEventType = init(0, 2790);
	public static final NodeId SamplingIntervalDiagnosticsArrayType = init(0, 2164);
	public static final NodeId ProgramStateMachineType = init(0, 2391);
	public static final NodeId AggregateFunctionsType = init(0, 11167);

	private CoreIdentifiers() {
	}

	static NodeId init(Integer index, Object value) {
		return NodeIdUtil.createNodeId(index, value);
	}
}
