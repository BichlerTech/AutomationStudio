package com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields;

import org.opcfoundation.ua.builtintypes.LocalizedText;

import com.richclientgui.toolbox.validation.validator.IFieldValidator;

public class LocalizedTextFieldValidator implements IFieldValidator<LocalizedText> {
	@Override
	public String getErrorMessage() {
		return null;
	}

	@Override
	public String getWarningMessage() {
		return null;
	}

	@Override
	public boolean isValid(LocalizedText text) {
		return true;
	}

	@Override
	public boolean warningExist(LocalizedText text) {
		if (text == null) {
			return true;
		} else if (text.getLocale() == null) {
			return true;
		}
		return false;
	}
}
