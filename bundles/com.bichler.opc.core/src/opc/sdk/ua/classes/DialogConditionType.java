package opc.sdk.ua.classes;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.core.Identifiers;

import opc.sdk.ua.constants.BrowseNames;

public class DialogConditionType extends ConditionType {
	protected TwoStateVariableType dialogState = null;
	protected PropertyVariableType<LocalizedText> prompt = null;
	protected PropertyVariableType<LocalizedText[]> responseOptionSet = null;
	protected PropertyVariableType<Integer> defaultResponse = null;
	protected PropertyVariableType<Integer> okResponse = null;
	protected PropertyVariableType<Integer> cancelResponse = null;
	protected PropertyVariableType<Integer> lastResponse = null;
	protected BaseMethod respondMethod = null;

	public DialogConditionType(BaseNode parent) {
		super(parent);
	}

	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.DialogConditionType;
	}

	private TwoStateVariableType createDialogState(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.dialogState == null) {
			if (replacement == null) {
				setDialogState(new TwoStateVariableType(this));
			} else {
				setDialogState((TwoStateVariableType) replacement);
			}
		}
		return this.dialogState;
	}

	private PropertyVariableType<LocalizedText> createPrompt(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.prompt == null) {
			if (replacement == null) {
				setPrompt(new PropertyVariableType<LocalizedText>(this));
			} else {
				setPrompt((PropertyVariableType<LocalizedText>) replacement);
			}
		}
		return this.prompt;
	}

	private PropertyVariableType<LocalizedText[]> createResponseOptionSet(boolean createOrReplace) {
		if (createOrReplace && this.responseOptionSet == null) {
			setResponseOptionSet(new PropertyVariableType<LocalizedText[]>(this));
		}
		return this.responseOptionSet;
	}

	private PropertyVariableType<Integer> createDefaultResponse(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.defaultResponse == null) {
			if (replacement == null) {
				setDefaultResponse(new PropertyVariableType<Integer>(this));
			} else {
				setDefaultResponse((PropertyVariableType<Integer>) replacement);
			}
		}
		return this.defaultResponse;
	}

	private PropertyVariableType<Integer> createOKResponse(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.okResponse == null) {
			if (replacement == null) {
				setOkResponse(new PropertyVariableType<Integer>(this));
			} else {
				setOkResponse((PropertyVariableType<Integer>) replacement);
			}
		}
		return this.okResponse;
	}

	private PropertyVariableType<Integer> createCancelResponse(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.cancelResponse == null) {
			if (replacement == null) {
				setCancelResponse(new PropertyVariableType<Integer>(this));
			} else {
				setCancelResponse((PropertyVariableType<Integer>) replacement);
			}
		}
		return this.cancelResponse;
	}

	private PropertyVariableType<Integer> createLastResponse(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.lastResponse == null) {
			if (replacement == null) {
				setLastResponse(new PropertyVariableType<Integer>(this));
			} else {
				setLastResponse((PropertyVariableType<Integer>) replacement);
			}
		}
		return this.lastResponse;
	}

	private BaseMethod createRespond(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.respondMethod == null) {
			if (replacement == null) {
				setRespond(new BaseMethod(this));
			} else {
				setRespond((BaseMethod) replacement);
			}
		}
		return this.respondMethod;
	}

	@Override
	protected BaseInstance findChild(QualifiedName browseName, boolean createOrReplace, BaseInstance replacement) {
		if (QualifiedName.isNull(browseName)) {
			return null;
		}
		BaseInstance instance = null;
		switch (browseName.getName()) {
		case BrowseNames.DIALOGSTATE:
			instance = createDialogState(createOrReplace, replacement);
			break;
		case BrowseNames.PROMPT:
			instance = createPrompt(createOrReplace, replacement);
			break;
		case BrowseNames.RESPONSEOPTIONSET:
			instance = createResponseOptionSet(createOrReplace);
			break;
		case BrowseNames.DEFAULTRESPONSE:
			instance = this.createDefaultResponse(createOrReplace, replacement);
			break;
		case BrowseNames.OKRESPONSE:
			instance = createOKResponse(createOrReplace, replacement);
			break;
		case BrowseNames.CANCELRESPONSE:
			instance = createCancelResponse(createOrReplace, replacement);
			break;
		case BrowseNames.LASTRESPONSE:
			instance = createLastResponse(createOrReplace, replacement);
			break;
		case BrowseNames.RESPOND:
			instance = createRespond(createOrReplace, replacement);
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
		if (this.dialogState != null) {
			children.add(this.dialogState);
		}
		if (this.prompt != null) {
			children.add(this.prompt);
		}
		if (this.responseOptionSet != null) {
			children.add(this.responseOptionSet);
		}
		if (this.defaultResponse != null) {
			children.add(this.defaultResponse);
		}
		if (this.okResponse != null) {
			children.add(this.okResponse);
		}
		if (this.cancelResponse != null) {
			children.add(this.cancelResponse);
		}
		if (this.lastResponse != null) {
			children.add(this.lastResponse);
		}
		if (this.respondMethod != null) {
			children.add(this.respondMethod);
		}
		children.addAll(super.getChildren());
		return children;
	}

	public TwoStateVariableType getDialogState() {
		return dialogState;
	}

	public PropertyVariableType<LocalizedText> getPrompt() {
		return prompt;
	}

	public PropertyVariableType<LocalizedText[]> getResponseOptionSet() {
		return responseOptionSet;
	}

	public PropertyVariableType<Integer> getDefaultResponse() {
		return defaultResponse;
	}

	public PropertyVariableType<Integer> getOkResponse() {
		return okResponse;
	}

	public PropertyVariableType<Integer> getCancelResponse() {
		return cancelResponse;
	}

	public PropertyVariableType<Integer> getLastResponse() {
		return lastResponse;
	}

	public BaseMethod getRespond() {
		return respondMethod;
	}

	public void setDialogState(TwoStateVariableType dialogState) {
		this.dialogState = dialogState;
	}

	public void setPrompt(PropertyVariableType<LocalizedText> prompt) {
		this.prompt = prompt;
	}

	public void setResponseOptionSet(PropertyVariableType<LocalizedText[]> responseOptionSet) {
		this.responseOptionSet = responseOptionSet;
	}

	public void setDefaultResponse(PropertyVariableType<Integer> defaultResponse) {
		this.defaultResponse = defaultResponse;
	}

	public void setOkResponse(PropertyVariableType<Integer> okResponse) {
		this.okResponse = okResponse;
	}

	public void setCancelResponse(PropertyVariableType<Integer> cancelResponse) {
		this.cancelResponse = cancelResponse;
	}

	public void setLastResponse(PropertyVariableType<Integer> lastResponse) {
		this.lastResponse = lastResponse;
	}

	public void setRespond(BaseMethod respondMethod) {
		this.respondMethod = respondMethod;
	}
}
