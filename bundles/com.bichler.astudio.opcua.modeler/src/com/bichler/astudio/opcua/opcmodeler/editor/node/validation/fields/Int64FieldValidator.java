package com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.richclientgui.toolbox.validation.validator.IFieldValidator;
import com.richclientgui.toolbox.validation.validator.IntegerFieldValidator;

public class Int64FieldValidator implements IFieldValidator<Long> {
	@Override
	public String getErrorMessage() {
		return CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.int64") + " "
				+ Long.MIN_VALUE + " "
				+ CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.to") + " "
				+ Long.MAX_VALUE;
	}

	@Override
	public String getWarningMessage() {
		return CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.arraydim.notvalidinput");
	}

	@Override
	public boolean warningExist(Long value) {
		return false;
	}

	@Override
	public boolean isValid(Long value) {
		if (value == null) {
			return false;
		}
		return true;
	}
}
