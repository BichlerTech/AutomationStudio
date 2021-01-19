package com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.richclientgui.toolbox.validation.validator.IFieldValidator;

public class DoubleFieldValidator implements IFieldValidator<Double> {
	@Override
	public String getErrorMessage() {
		return CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "DoubleFieldValidator.error");
	}

	@Override
	public String getWarningMessage() {
		return CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.arraydim.notvalidinput");
	}

	@Override
	public boolean isValid(Double value) {
		if (value == null) {
			return false;
		}
		return true;
	}

	@Override
	public boolean warningExist(Double value) {
		return false;
	}
}
