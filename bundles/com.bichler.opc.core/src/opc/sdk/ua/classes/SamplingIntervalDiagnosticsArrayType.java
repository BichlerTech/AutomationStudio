package opc.sdk.ua.classes;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.SamplingIntervalDiagnosticsDataType;

import opc.sdk.core.enums.ValueRanks;

public class SamplingIntervalDiagnosticsArrayType extends BaseDataVariableType<SamplingIntervalDiagnosticsDataType[]> {
	/**
	 * Initializes the type with its default attribute values.
	 * 
	 * @param parent
	 */
	public SamplingIntervalDiagnosticsArrayType(BaseNode parent) {
		super(parent);
	}

	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.SamplingIntervalDiagnosticsArrayType;
	}

	@Override
	protected NodeId getDefaultDataTypeId() {
		return Identifiers.SamplingIntervalDiagnosticsDataType;
	}

	@Override
	protected int getDefaultValueRank() {
		return ValueRanks.OneDimension.getValue();
	}
}
