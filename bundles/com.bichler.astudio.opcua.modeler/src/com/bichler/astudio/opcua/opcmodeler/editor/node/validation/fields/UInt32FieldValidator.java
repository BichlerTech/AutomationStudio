package com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields;

import org.opcfoundation.ua.builtintypes.UnsignedInteger;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.richclientgui.toolbox.validation.validator.IFieldValidator;

public class UInt32FieldValidator implements IFieldValidator<UnsignedInteger> {
	@Override
	public String getErrorMessage() {
		return CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.uint32") + " "
				+ UnsignedInteger.MIN_VALUE + " "
				+ CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.to") + " "
				+ UnsignedInteger.MAX_VALUE;
	}

	@Override
	public String getWarningMessage() {
		return null;
	}

	@Override
	public boolean isValid(UnsignedInteger value) {
		if (value == null) {
			return false;
		}
		return true;
	}

	@Override
	public boolean warningExist(UnsignedInteger value) {
		return false;
	}
}
