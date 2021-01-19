package com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields;

import java.util.UUID;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.richclientgui.toolbox.validation.validator.IFieldValidator;

public class GuidFieldValidator implements IFieldValidator<UUID> {
	@Override
	public String getErrorMessage() {
		return CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.guid") + " "
				+ UUID.randomUUID();
	}

	@Override
	public String getWarningMessage() {
		return null;
	}

	@Override
	public boolean isValid(UUID value) {
		if (value == null) {
			return false;
		}
		return true;
	}

	@Override
	public boolean warningExist(UUID value) {
		return false;
	}
}
