package com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields;

import org.opcfoundation.ua.builtintypes.UnsignedByte;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.richclientgui.toolbox.validation.validator.IFieldValidator;

public class ByteFieldValidator implements IFieldValidator<UnsignedByte> {
	@Override
	public String getErrorMessage() {
		return CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.byte") + " "
				+ UnsignedByte.MIN_VALUE + " "
				+ CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.to") + " "
				+ UnsignedByte.MAX_VALUE;
	}

	@Override
	public String getWarningMessage() {
		return null;
	}

	@Override
	public boolean isValid(UnsignedByte value) {
		if (value == null) {
			return false;
		}
		return true;
	}

	@Override
	public boolean warningExist(UnsignedByte value) {
		return false;
	}
}
