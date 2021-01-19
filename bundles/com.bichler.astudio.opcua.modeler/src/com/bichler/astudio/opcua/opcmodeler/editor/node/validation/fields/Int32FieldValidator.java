package com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.richclientgui.toolbox.validation.validator.IntegerFieldValidator;

public class Int32FieldValidator extends IntegerFieldValidator {
	@Override
	public String getErrorMessage() {
		return CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "Int32FieldValidator.error");
	}

	@Override
	public String getWarningMessage() {
		return CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.arraydim.notvalidinput");
	}

	@Override
	public boolean warningExist(Integer value) {
		return false;
	}

	@Override
	public boolean isValid(Integer value) {
		return super.isValid(value);
	}
}
