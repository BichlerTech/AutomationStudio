package com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters;

import com.richclientgui.toolbox.validation.converter.IContentsStringConverter;

public class BooleanStringConverter implements IContentsStringConverter<Boolean> {
	@Override
	public Boolean convertFromString(String short_) {
		try {
			Boolean uByte = new Boolean(short_);
			return uByte;
		} catch (IllegalArgumentException iae) {
			return null;
		}
	}

	@Override
	public String convertToString(Boolean short_) {
		return short_.toString();
	}
}
