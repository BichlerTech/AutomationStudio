package com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields;

import org.opcfoundation.ua.builtintypes.UnsignedLong;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.richclientgui.toolbox.validation.validator.IFieldValidator;

public class UInt64FieldValidator implements IFieldValidator<UnsignedLong> {
	@Override
	public String getErrorMessage() {
		return CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.uint64") + " "
				+ UnsignedLong.MIN_VALUE + " "
				+ CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.to") + " "
				+ UnsignedLong.MAX_VALUE;
	}

	@Override
	public String getWarningMessage() {
		return null;
	}

	@Override
	public boolean isValid(UnsignedLong value) {
		if (value == null) {
			return false;
		}
		return true;
	}

	@Override
	public boolean warningExist(UnsignedLong value) {
		return false;
	}
}
