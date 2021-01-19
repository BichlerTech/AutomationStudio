package com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters;

import com.richclientgui.toolbox.validation.converter.IContentsStringConverter;

public class Int16StringConverter implements IContentsStringConverter<Short> {
	@Override
	public Short convertFromString(String unsignedInteger) {
		try {
			Short uInt = new Short(unsignedInteger);
			return uInt;
		} catch (IllegalArgumentException iae) {
			return null;
		}
	}

	@Override
	public String convertToString(Short unsignedInteger) {
		return unsignedInteger.toString();
	}
}
