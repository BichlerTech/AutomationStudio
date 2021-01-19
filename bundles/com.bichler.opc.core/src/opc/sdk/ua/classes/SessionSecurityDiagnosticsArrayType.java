package opc.sdk.ua.classes;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.SessionSecurityDiagnosticsDataType;

import opc.sdk.core.enums.ValueRanks;

public class SessionSecurityDiagnosticsArrayType extends BaseDataVariableType<SessionSecurityDiagnosticsDataType[]> {
	public SessionSecurityDiagnosticsArrayType(BaseNode parent) {
		super(parent);
	}

	/**
	 * Returns the id of the default type definition node for the instance.
	 * 
	 * @return TypeDefinition Id
	 */
	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.SessionSecurityDiagnosticsArrayType;
	}

	/**
	 * Returns the id of the default data type node for the instance.
	 * 
	 * @return DataType Id
	 */
	@Override
	protected NodeId getDefaultDataTypeId() {
		return Identifiers.SessionSecurityDiagnosticsDataType;
	}

	/**
	 * Returns the id of the default value rank for the instance.
	 * 
	 * @return ValueRank
	 */
	@Override
	protected int getDefaultValueRank() {
		return ValueRanks.OneDimension.getValue();
	}
}
