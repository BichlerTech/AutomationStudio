package opc.sdk.core.classes.ua.core;

public class AlarmConditionType extends AcknowledgeableConditionType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.AlarmConditionType;
	private PropertyType maxTimeShelved;
	private TwoStateVariableType suppressedState;
	private TwoStateVariableType enabledState;
	private ShelvedStateMachineType shelvingState;
	private PropertyType inputNode;
	private TwoStateVariableType activeState;
	private PropertyType suppressedOrShelved;

	public AlarmConditionType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getMaxTimeShelved() {
		return maxTimeShelved;
	}

	public void setMaxTimeShelved(PropertyType value) {
		maxTimeShelved = value;
	}

	public TwoStateVariableType getSuppressedState() {
		return suppressedState;
	}

	public void setSuppressedState(TwoStateVariableType value) {
		suppressedState = value;
	}

	@Override
	public TwoStateVariableType getEnabledState() {
		return enabledState;
	}

	@Override
	public void setEnabledState(TwoStateVariableType value) {
		enabledState = value;
	}

	public ShelvedStateMachineType getShelvingState() {
		return shelvingState;
	}

	public void setShelvingState(ShelvedStateMachineType value) {
		shelvingState = value;
	}

	public PropertyType getInputNode() {
		return inputNode;
	}

	public void setInputNode(PropertyType value) {
		inputNode = value;
	}

	public TwoStateVariableType getActiveState() {
		return activeState;
	}

	public void setActiveState(TwoStateVariableType value) {
		activeState = value;
	}

	public PropertyType getSuppressedOrShelved() {
		return suppressedOrShelved;
	}

	public void setSuppressedOrShelved(PropertyType value) {
		suppressedOrShelved = value;
	}

	@Override
	public String toString() {
		return "AlarmConditionType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
