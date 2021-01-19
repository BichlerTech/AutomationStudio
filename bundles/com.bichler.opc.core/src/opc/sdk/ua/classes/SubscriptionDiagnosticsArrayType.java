package opc.sdk.ua.classes;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.SubscriptionDiagnosticsDataType;

import opc.sdk.core.enums.ValueRanks;

public class SubscriptionDiagnosticsArrayType extends BaseDataVariableType<SubscriptionDiagnosticsDataType[]> {
	public SubscriptionDiagnosticsArrayType(BaseNode parent) {
		super(parent);
	}

	/**
	 * Returns the id of the default type definition node for the instance.
	 * 
	 * @return DefaultTypeDefinitionId
	 */
	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.SubscriptionDiagnosticsArrayType;
	}

	/**
	 * Returns the id of the default data type node for the instance.
	 * 
	 * @return DefaultDataTypeId
	 */
	@Override
	protected NodeId getDefaultDataTypeId() {
		return Identifiers.SubscriptionDiagnosticsDataType;
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
