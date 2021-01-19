package com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields;

import java.util.Locale;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.richclientgui.toolbox.validation.validator.IFieldValidator;

public class LocaleFieldValidator implements IFieldValidator<Locale> {
	@Override
	public String getErrorMessage() {
		return null;
	}

	@Override
	public String getWarningMessage() {
		return CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.locale");
	}

	@Override
	public boolean isValid(Locale locale) {
		return true;
	}

	@Override
	public boolean warningExist(Locale locale) {
		if (locale == null) {
			return true;
		}
		return false;
	}
}
