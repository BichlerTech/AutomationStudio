package opc.sdk.core.classes.ua.core;

public class ShelvedStateMachineType extends FiniteStateMachineType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.ShelvedStateMachineType;
	private StateType unshelved;
	private PropertyType unshelveTime;
	private TransitionType oneShotShelvedToUnshelved;
	private opc.sdk.core.classes.ua.base.BaseMethodGen timedShelve;
	private opc.sdk.core.classes.ua.base.BaseMethodGen oneShotShelve;
	private TransitionType unshelvedToTimedShelved;
	private TransitionType unshelvedToOneShotShelved;
	private opc.sdk.core.classes.ua.base.BaseMethodGen unshelve;
	private StateType timedShelved;
	private TransitionType oneShotShelvedToTimedShelved;
	private TransitionType timedShelvedToUnshelved;
	private StateType oneShotShelved;
	private TransitionType timedShelvedToOneShotShelved;

	public ShelvedStateMachineType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public StateType getUnshelved() {
		return unshelved;
	}

	public void setUnshelved(StateType value) {
		unshelved = value;
	}

	public PropertyType getUnshelveTime() {
		return unshelveTime;
	}

	public void setUnshelveTime(PropertyType value) {
		unshelveTime = value;
	}

	public TransitionType getOneShotShelvedToUnshelved() {
		return oneShotShelvedToUnshelved;
	}

	public void setOneShotShelvedToUnshelved(TransitionType value) {
		oneShotShelvedToUnshelved = value;
	}

	public opc.sdk.core.classes.ua.base.BaseMethodGen getTimedShelve() {
		return timedShelve;
	}

	public void setTimedShelve(opc.sdk.core.classes.ua.base.BaseMethodGen value) {
		timedShelve = value;
	}

	public opc.sdk.core.classes.ua.base.BaseMethodGen getOneShotShelve() {
		return oneShotShelve;
	}

	public void setOneShotShelve(opc.sdk.core.classes.ua.base.BaseMethodGen value) {
		oneShotShelve = value;
	}

	public TransitionType getUnshelvedToTimedShelved() {
		return unshelvedToTimedShelved;
	}

	public void setUnshelvedToTimedShelved(TransitionType value) {
		unshelvedToTimedShelved = value;
	}

	public TransitionType getUnshelvedToOneShotShelved() {
		return unshelvedToOneShotShelved;
	}

	public void setUnshelvedToOneShotShelved(TransitionType value) {
		unshelvedToOneShotShelved = value;
	}

	public opc.sdk.core.classes.ua.base.BaseMethodGen getUnshelve() {
		return unshelve;
	}

	public void setUnshelve(opc.sdk.core.classes.ua.base.BaseMethodGen value) {
		unshelve = value;
	}

	public StateType getTimedShelved() {
		return timedShelved;
	}

	public void setTimedShelved(StateType value) {
		timedShelved = value;
	}

	public TransitionType getOneShotShelvedToTimedShelved() {
		return oneShotShelvedToTimedShelved;
	}

	public void setOneShotShelvedToTimedShelved(TransitionType value) {
		oneShotShelvedToTimedShelved = value;
	}

	public TransitionType getTimedShelvedToUnshelved() {
		return timedShelvedToUnshelved;
	}

	public void setTimedShelvedToUnshelved(TransitionType value) {
		timedShelvedToUnshelved = value;
	}

	public StateType getOneShotShelved() {
		return oneShotShelved;
	}

	public void setOneShotShelved(StateType value) {
		oneShotShelved = value;
	}

	public TransitionType getTimedShelvedToOneShotShelved() {
		return timedShelvedToOneShotShelved;
	}

	public void setTimedShelvedToOneShotShelved(TransitionType value) {
		timedShelvedToOneShotShelved = value;
	}

	@Override
	public String toString() {
		return "ShelvedStateMachineType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
