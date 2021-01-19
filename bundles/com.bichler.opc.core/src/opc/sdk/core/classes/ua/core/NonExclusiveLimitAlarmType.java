package opc.sdk.core.classes.ua.core;

public class NonExclusiveLimitAlarmType extends LimitAlarmType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.NonExclusiveLimitAlarmType;
	private TwoStateVariableType lowState;
	private TwoStateVariableType highHighState;
	private TwoStateVariableType activeState;
	private TwoStateVariableType highState;
	private TwoStateVariableType lowLowState;

	public NonExclusiveLimitAlarmType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public TwoStateVariableType getLowState() {
		return lowState;
	}

	public void setLowState(TwoStateVariableType value) {
		lowState = value;
	}

	public TwoStateVariableType getHighHighState() {
		return highHighState;
	}

	public void setHighHighState(TwoStateVariableType value) {
		highHighState = value;
	}

	@Override
	public TwoStateVariableType getActiveState() {
		return activeState;
	}

	@Override
	public void setActiveState(TwoStateVariableType value) {
		activeState = value;
	}

	public TwoStateVariableType getHighState() {
		return highState;
	}

	public void setHighState(TwoStateVariableType value) {
		highState = value;
	}

	public TwoStateVariableType getLowLowState() {
		return lowLowState;
	}

	public void setLowLowState(TwoStateVariableType value) {
		lowLowState = value;
	}

	@Override
	public String toString() {
		return "NonExclusiveLimitAlarmType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
