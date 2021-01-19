package com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.richclientgui.toolbox.validation.validator.IFieldValidator;

public class Int16FieldValidator implements IFieldValidator<Short> {
	@Override
	public String getErrorMessage() {
		return CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.int16") + " "
				+ Short.MIN_VALUE + " "
				+ CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.to") + " "
				+ Short.MAX_VALUE;
	}

	@Override
	public String getWarningMessage() {
		return CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.arraydim.notvalidinput");
	}

	@Override
	public boolean isValid(Short value) {
		if (value == null) {
			return false;
		}
		return true;
	}

	@Override
	public boolean warningExist(Short value) {
		return false;
	}
}
