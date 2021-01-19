package opc.sdk.core.classes.ua.core;

public class ExclusiveLimitStateMachineType extends FiniteStateMachineType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.ExclusiveLimitStateMachineType;
	private TransitionType lowLowToLow;
	private StateType highHigh;
	private StateType lowLow;
	private StateType low;
	private TransitionType highToHighHigh;
	private TransitionType lowToLowLow;
	private StateType high;
	private TransitionType highHighToHigh;

	public ExclusiveLimitStateMachineType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public TransitionType getLowLowToLow() {
		return lowLowToLow;
	}

	public void setLowLowToLow(TransitionType value) {
		lowLowToLow = value;
	}

	public StateType getHighHigh() {
		return highHigh;
	}

	public void setHighHigh(StateType value) {
		highHigh = value;
	}

	public StateType getLowLow() {
		return lowLow;
	}

	public void setLowLow(StateType value) {
		lowLow = value;
	}

	public StateType getLow() {
		return low;
	}

	public void setLow(StateType value) {
		low = value;
	}

	public TransitionType getHighToHighHigh() {
		return highToHighHigh;
	}

	public void setHighToHighHigh(TransitionType value) {
		highToHighHigh = value;
	}

	public TransitionType getLowToLowLow() {
		return lowToLowLow;
	}

	public void setLowToLowLow(TransitionType value) {
		lowToLowLow = value;
	}

	public StateType getHigh() {
		return high;
	}

	public void setHigh(StateType value) {
		high = value;
	}

	public TransitionType getHighHighToHigh() {
		return highHighToHigh;
	}

	public void setHighHighToHigh(TransitionType value) {
		highHighToHigh = value;
	}

	@Override
	public String toString() {
		return "ExclusiveLimitStateMachineType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
