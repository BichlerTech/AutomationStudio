package opc.sdk.core.classes.ua.core;

public class StateMachineType extends BaseObjectType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.StateMachineType;
	private StateVariableType currentState;
	private TransitionVariableType lastTransition;

	public StateMachineType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public StateVariableType getCurrentState() {
		return currentState;
	}

	public void setCurrentState(StateVariableType value) {
		currentState = value;
	}

	public TransitionVariableType getLastTransition() {
		return lastTransition;
	}

	public void setLastTransition(TransitionVariableType value) {
		lastTransition = value;
	}

	@Override
	public String toString() {
		return "StateMachineType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
