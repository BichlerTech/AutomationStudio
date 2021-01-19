package opc.sdk.ua.classes;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.core.Identifiers;

import opc.sdk.ua.constants.BrowseNames;

public class AlarmConditionType extends AcknowledgeableConditionType {
	protected TwoStateVariableType activeState = null;
	protected PropertyVariableType<NodeId> inputNode = null;
	protected TwoStateVariableType suppressedState = null;
	protected ShelvedStateMachineType shelvingState = null;
	protected PropertyVariableType<Boolean> suppressedOrShelved = null;
	protected PropertyVariableType<Double> maxTimeShelved = null;

	public AlarmConditionType(BaseNode parent) {
		super(parent);
	}

	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.AlarmConditionType;
	}

	private BaseInstance setActiveState(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.activeState == null)
			if (replacement == null)
				setActiveState(new TwoStateVariableType(this));
			else
				setActiveState((TwoStateVariableType) replacement);
		return this.activeState;
	}

	@SuppressWarnings("unchecked")
	private BaseInstance setInputNode(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.inputNode == null)
			if (replacement == null)
				setInputNode(new PropertyVariableType<NodeId>(this));
			else
				setInputNode((PropertyVariableType<NodeId>) replacement);
		return this.activeState;
	}

	private BaseInstance setSuppressedState(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.suppressedState == null)
			if (replacement == null)
				setSuppressedState(new TwoStateVariableType(this));
			else
				setSuppressedState((TwoStateVariableType) replacement);
		return this.suppressedState;
	}

	private BaseInstance setShelvingState(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.shelvingState == null)
			if (replacement == null)
				setShelvingState(new ShelvedStateMachineType(this));
			else
				setShelvingState((ShelvedStateMachineType) replacement);
		return this.shelvingState;
	}

	@SuppressWarnings("unchecked")
	private BaseInstance setSuppressedOrShelved(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.suppressedOrShelved == null)
			if (replacement == null)
				setSuppressedOrShelved(new PropertyVariableType<Boolean>(this));
			else
				setSuppressedOrShelved((PropertyVariableType<Boolean>) replacement);
		return this.suppressedOrShelved;
	}

	@SuppressWarnings("unchecked")
	private BaseInstance setMaxTimeShelved(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.maxTimeShelved == null)
			if (replacement == null)
				setMaxTimeShelved(new PropertyVariableType<Double>(this));
			else
				setMaxTimeShelved((PropertyVariableType<Double>) replacement);
		return this.maxTimeShelved;
	}

	@Override
	protected BaseInstance findChild(QualifiedName browseName, boolean createOrReplace, BaseInstance replacement) {
		BaseInstance instance = null;
		switch (browseName.getName()) {
		case BrowseNames.ACTIVESTATE:
			instance = setActiveState(createOrReplace, replacement);
			break;
		case BrowseNames.INPUTNODE:
			instance = setInputNode(createOrReplace, replacement);
			break;
		case BrowseNames.SUPPRESSEDSTATE:
			instance = setSuppressedState(createOrReplace, replacement);
			break;
		case BrowseNames.SHELVINGSTATE:
			instance = setShelvingState(createOrReplace, replacement);
			break;
		case BrowseNames.SUPPRESSEDORSHELVED:
			instance = setSuppressedOrShelved(createOrReplace, replacement);
			break;
		case BrowseNames.MAXTIMESHELVED:
			instance = setMaxTimeShelved(createOrReplace, replacement);
			break;
		default:
			break;
		}
		if (instance != null) {
			return instance;
		}
		return super.findChild(browseName, createOrReplace, replacement);
	}

	@Override
	public List<BaseInstance> getChildren() {
		List<BaseInstance> children = new ArrayList<>();
		children.addAll(super.getChildren());
		return children;
	}

	public TwoStateVariableType getActiveState() {
		return activeState;
	}

	public PropertyVariableType<NodeId> getInputNode() {
		return inputNode;
	}

	public TwoStateVariableType getSuppressedState() {
		return suppressedState;
	}

	public ShelvedStateMachineType getShelvingState() {
		return shelvingState;
	}

	public PropertyVariableType<Boolean> getSuppressedOrShelved() {
		return suppressedOrShelved;
	}

	public PropertyVariableType<Double> getMaxTimeShelved() {
		return maxTimeShelved;
	}

	public void setActiveState(TwoStateVariableType activeState) {
		this.activeState = activeState;
	}

	public void setInputNode(PropertyVariableType<NodeId> inputNode) {
		this.inputNode = inputNode;
	}

	public void setSuppressedState(TwoStateVariableType suppressedState) {
		this.suppressedState = suppressedState;
	}

	public void setShelvingState(ShelvedStateMachineType shelvingState) {
		this.shelvingState = shelvingState;
	}

	public void setSuppressedOrShelved(PropertyVariableType<Boolean> suppressedOrShelved) {
		this.suppressedOrShelved = suppressedOrShelved;
	}

	public void setMaxTimeShelved(PropertyVariableType<Double> maxTimeShelved) {
		this.maxTimeShelved = maxTimeShelved;
	}
}
