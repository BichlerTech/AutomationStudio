package com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields;

import org.opcfoundation.ua.builtintypes.UnsignedShort;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.richclientgui.toolbox.validation.validator.IFieldValidator;

public class UInt16FieldValidator implements IFieldValidator<UnsignedShort> {
	@Override
	public String getErrorMessage() {
		return CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.uint16") + " "
				+ UnsignedShort.MIN_VALUE + " "
				+ CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.to") + " "
				+ UnsignedShort.MAX_VALUE;
	}

	@Override
	public String getWarningMessage() {
		return null;
	}

	@Override
	public boolean isValid(UnsignedShort value) {
		if (value == null) {
			return false;
		}
		return true;
	}

	@Override
	public boolean warningExist(UnsignedShort value) {
		return false;
	}
}
