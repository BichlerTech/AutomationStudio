package com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.richclientgui.toolbox.validation.validator.IFieldValidator;

public class FloatFieldValidator implements IFieldValidator<Float> {
	@Override
	public String getErrorMessage() {
		return CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.float") + " "
				+ Float.MIN_VALUE + " "
				+ CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.to") + " "
				+ Float.MAX_VALUE;
	}

	@Override
	public String getWarningMessage() {
		return CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.arraydim.notvalidinput");
	}

	@Override
	public boolean isValid(Float value) {
		if (value == null) {
			return false;
		}
		return true;
	}

	@Override
	public boolean warningExist(Float value) {
		return false;
	}
}
