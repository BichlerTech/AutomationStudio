package opc.sdk.core.classes.ua.core;

public class FiniteStateMachineType extends StateMachineType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.FiniteStateMachineType;
	private FiniteStateVariableType currentState;
	private FiniteTransitionVariableType lastTransition;

	public FiniteStateMachineType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	@Override
	public FiniteStateVariableType getCurrentState() {
		return currentState;
	}

	public void setCurrentState(FiniteStateVariableType value) {
		currentState = value;
	}

	@Override
	public FiniteTransitionVariableType getLastTransition() {
		return lastTransition;
	}

	public void setLastTransition(FiniteTransitionVariableType value) {
		lastTransition = value;
	}

	@Override
	public String toString() {
		return "FiniteStateMachineType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
