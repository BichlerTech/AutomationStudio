package opc.sdk.core.classes.ua.core;

public class DialogConditionType extends ConditionType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.DialogConditionType;
	private PropertyType okResponse;
	private opc.sdk.core.classes.ua.base.BaseMethodGen respond;
	private PropertyType prompt;
	private PropertyType responseOptionSet;
	private PropertyType defaultResponse;
	private PropertyType cancelResponse;
	private TwoStateVariableType dialogState;
	private TwoStateVariableType enabledState;
	private PropertyType lastResponse;

	public DialogConditionType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public PropertyType getOkResponse() {
		return okResponse;
	}

	public void setOkResponse(PropertyType value) {
		okResponse = value;
	}

	public opc.sdk.core.classes.ua.base.BaseMethodGen getRespond() {
		return respond;
	}

	public void setRespond(opc.sdk.core.classes.ua.base.BaseMethodGen value) {
		respond = value;
	}

	public PropertyType getPrompt() {
		return prompt;
	}

	public void setPrompt(PropertyType value) {
		prompt = value;
	}

	public PropertyType getResponseOptionSet() {
		return responseOptionSet;
	}

	public void setResponseOptionSet(PropertyType value) {
		responseOptionSet = value;
	}

	public PropertyType getDefaultResponse() {
		return defaultResponse;
	}

	public void setDefaultResponse(PropertyType value) {
		defaultResponse = value;
	}

	public PropertyType getCancelResponse() {
		return cancelResponse;
	}

	public void setCancelResponse(PropertyType value) {
		cancelResponse = value;
	}

	public TwoStateVariableType getDialogState() {
		return dialogState;
	}

	public void setDialogState(TwoStateVariableType value) {
		dialogState = value;
	}

	@Override
	public TwoStateVariableType getEnabledState() {
		return enabledState;
	}

	@Override
	public void setEnabledState(TwoStateVariableType value) {
		enabledState = value;
	}

	public PropertyType getLastResponse() {
		return lastResponse;
	}

	public void setLastResponse(PropertyType value) {
		lastResponse = value;
	}

	@Override
	public String toString() {
		return "DialogConditionType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
