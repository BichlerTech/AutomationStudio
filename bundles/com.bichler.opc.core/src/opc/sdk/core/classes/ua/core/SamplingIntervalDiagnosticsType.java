package opc.sdk.core.classes.ua.core;

public class SamplingIntervalDiagnosticsType extends BaseDataVariableType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.SamplingIntervalDiagnosticsType;
	private BaseDataVariableType monitoredItemCount;
	private BaseDataVariableType samplingInterval;
	private BaseDataVariableType disabledMonitoredItemCount;
	private BaseDataVariableType maxMonitoredItemCount;

	public SamplingIntervalDiagnosticsType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.core.SamplingIntervalDiagnosticsDataType getValue() {
		return getVariant() != null
				? (org.opcfoundation.ua.core.SamplingIntervalDiagnosticsDataType) getVariant().getValue()
				: null;
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public BaseDataVariableType getMonitoredItemCount() {
		return monitoredItemCount;
	}

	public void setMonitoredItemCount(BaseDataVariableType value) {
		monitoredItemCount = value;
	}

	public BaseDataVariableType getSamplingInterval() {
		return samplingInterval;
	}

	public void setSamplingInterval(BaseDataVariableType value) {
		samplingInterval = value;
	}

	public BaseDataVariableType getDisabledMonitoredItemCount() {
		return disabledMonitoredItemCount;
	}

	public void setDisabledMonitoredItemCount(BaseDataVariableType value) {
		disabledMonitoredItemCount = value;
	}

	public BaseDataVariableType getMaxMonitoredItemCount() {
		return maxMonitoredItemCount;
	}

	public void setMaxMonitoredItemCount(BaseDataVariableType value) {
		maxMonitoredItemCount = value;
	}

	@Override
	public String toString() {
		return "SamplingIntervalDiagnosticsType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
