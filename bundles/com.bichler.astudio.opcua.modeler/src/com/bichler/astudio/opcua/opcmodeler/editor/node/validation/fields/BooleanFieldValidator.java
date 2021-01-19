package com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.richclientgui.toolbox.validation.validator.IFieldValidator;

public class BooleanFieldValidator implements IFieldValidator<Boolean> {
	@Override
	public String getErrorMessage() {
		return CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.boolean");
	}

	@Override
	public String getWarningMessage() {
		return null;
	}

	@Override
	public boolean isValid(Boolean value) {
		if (value == null) {
			return false;
		}
		return true;
	}

	@Override
	public boolean warningExist(Boolean value) {
		return false;
	}
}
