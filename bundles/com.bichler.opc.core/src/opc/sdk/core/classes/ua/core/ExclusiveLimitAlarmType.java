package opc.sdk.core.classes.ua.core;

public class ExclusiveLimitAlarmType extends LimitAlarmType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.ExclusiveLimitAlarmType;
	private ExclusiveLimitStateMachineType limitState;
	private TwoStateVariableType activeState;

	public ExclusiveLimitAlarmType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public ExclusiveLimitStateMachineType getLimitState() {
		return limitState;
	}

	public void setLimitState(ExclusiveLimitStateMachineType value) {
		limitState = value;
	}

	@Override
	public TwoStateVariableType getActiveState() {
		return activeState;
	}

	@Override
	public void setActiveState(TwoStateVariableType value) {
		activeState = value;
	}

	@Override
	public String toString() {
		return "ExclusiveLimitAlarmType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
