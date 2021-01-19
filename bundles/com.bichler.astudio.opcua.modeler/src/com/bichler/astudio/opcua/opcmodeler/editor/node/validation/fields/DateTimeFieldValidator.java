package com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields;

import org.opcfoundation.ua.builtintypes.DateTime;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.richclientgui.toolbox.validation.validator.IFieldValidator;

public class DateTimeFieldValidator implements IFieldValidator<DateTime> {
	@Override
	public String getErrorMessage() {
		return CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.datetime") + " "
				+ DateTime.currentTime();
	}

	@Override
	public String getWarningMessage() {
		return null;
	}

	@Override
	public boolean isValid(DateTime value) {
		if (value == null) {
			return false;
		}
		return true;
	}

	@Override
	public boolean warningExist(DateTime value) {
		return false;
	}
}
