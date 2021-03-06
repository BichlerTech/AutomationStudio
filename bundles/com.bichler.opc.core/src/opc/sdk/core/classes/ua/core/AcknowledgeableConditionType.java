package opc.sdk.core.classes.ua.core;

public class AcknowledgeableConditionType extends ConditionType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.AcknowledgeableConditionType;
	private TwoStateVariableType confirmedState;
	private opc.sdk.core.classes.ua.base.BaseMethodGen confirm;
	private TwoStateVariableType enabledState;
	private opc.sdk.core.classes.ua.base.BaseMethodGen acknowledge;
	private TwoStateVariableType ackedState;

	public AcknowledgeableConditionType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public TwoStateVariableType getConfirmedState() {
		return confirmedState;
	}

	public void setConfirmedState(TwoStateVariableType value) {
		confirmedState = value;
	}

	public opc.sdk.core.classes.ua.base.BaseMethodGen getConfirm() {
		return confirm;
	}

	public void setConfirm(opc.sdk.core.classes.ua.base.BaseMethodGen value) {
		confirm = value;
	}

	@Override
	public TwoStateVariableType getEnabledState() {
		return enabledState;
	}

	@Override
	public void setEnabledState(TwoStateVariableType value) {
		enabledState = value;
	}

	public opc.sdk.core.classes.ua.base.BaseMethodGen getAcknowledge() {
		return acknowledge;
	}

	public void setAcknowledge(opc.sdk.core.classes.ua.base.BaseMethodGen value) {
		acknowledge = value;
	}

	public TwoStateVariableType getAckedState() {
		return ackedState;
	}

	public void setAckedState(TwoStateVariableType value) {
		ackedState = value;
	}

	@Override
	public String toString() {
		return "AcknowledgeableConditionType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
