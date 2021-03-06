package opc.sdk.ua.classes;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Identifiers;

public class FiniteStateMachineType extends StateMachineType {
	public FiniteStateMachineType(BaseNode parent) {
		super(parent);
	}

	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.FiniteStateMachineType;
	}
}
