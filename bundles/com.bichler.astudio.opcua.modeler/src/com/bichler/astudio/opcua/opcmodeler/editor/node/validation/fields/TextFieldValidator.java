package com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields;

import org.eclipse.swt.widgets.Control;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.richclientgui.toolbox.validation.validator.IFieldValidator;

public class TextFieldValidator<S> implements IFieldValidator<String> {
	protected String errorMessage = CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
			"message.error.value");
	protected String warningMessage = CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
			"message.error.value");

	public Control getParent() {
		return parent;
	}

	public void setParent(Control parent) {
		this.parent = parent;
	}

	private Control parent = null;

	public void setErrorMessage(String message) {
		this.errorMessage = message;
	}

	public void setWarningMessage(String message) {
		this.warningMessage = message;
	}

	@Override
	public String getErrorMessage() {
		return this.errorMessage;
	}

	@Override
	public String getWarningMessage() {
		return this.warningMessage;
	}

	@Override
	public boolean isValid(String content) {
		return true;
	}

	@Override
	public boolean warningExist(String content) {
		if (parent != null && parent.getEnabled()) {
			return content.isEmpty();
		}
		return false;
	}
}
