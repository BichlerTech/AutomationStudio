package opc.sdk.ua.classes;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Identifiers;

public class ExclusiveLimitStateMachineType extends FiniteStateMachineType {
	public ExclusiveLimitStateMachineType(BaseNode parent) {
		super(parent);
	}

	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.ExclusiveLimitStateMachineType;
	}
}
