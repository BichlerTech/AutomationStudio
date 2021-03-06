package opc.sdk.ua.classes;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.SessionDiagnosticsDataType;

import opc.sdk.core.enums.ValueRanks;

public class SessionDiagnosticsArrayType extends BaseDataVariableType<SessionDiagnosticsDataType[]> {
	protected SessionDiagnosticsArrayType(BaseNode parent) {
		super(parent);
	}

	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.SessionDiagnosticsArrayType;
	}

	@Override
	protected NodeId getDefaultDataTypeId() {
		return Identifiers.SessionDiagnosticsDataType;
	}

	@Override
	protected int getDefaultValueRank() {
		return ValueRanks.OneDimension.getValue();
	}
}
