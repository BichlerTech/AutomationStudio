package opc.sdk.core.classes.ua.core;

public class SamplingIntervalDiagnosticsArrayType extends BaseDataVariableType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.SamplingIntervalDiagnosticsArrayType;

	public SamplingIntervalDiagnosticsArrayType() {
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

	@Override
	public String toString() {
		return "SamplingIntervalDiagnosticsArrayType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
