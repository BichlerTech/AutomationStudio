package opc.sdk.core.classes.ua.core;

public class TransitionEventType extends BaseEventType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.TransitionEventType;
	private TransitionVariableType transition;
	private StateVariableType fromState;
	private StateVariableType toState;

	public TransitionEventType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public TransitionVariableType getTransition() {
		return transition;
	}

	public void setTransition(TransitionVariableType value) {
		transition = value;
	}

	public StateVariableType getFromState() {
		return fromState;
	}

	public void setFromState(StateVariableType value) {
		fromState = value;
	}

	public StateVariableType getToState() {
		return toState;
	}

	public void setToState(StateVariableType value) {
		toState = value;
	}

	@Override
	public String toString() {
		return "TransitionEventType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
