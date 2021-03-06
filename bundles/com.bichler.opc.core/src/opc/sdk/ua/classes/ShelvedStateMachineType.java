package opc.sdk.ua.classes;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Identifiers;

public class ShelvedStateMachineType extends FiniteStateMachineType {
	public ShelvedStateMachineType(BaseNode parent) {
		super(parent);
	}

	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.ShelvedStateMachineType;
	}
}
