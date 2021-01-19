package com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters;

import com.richclientgui.toolbox.validation.converter.IContentsStringConverter;

public class SByteStringConverter implements IContentsStringConverter<Byte> {
	@Override
	public Byte convertFromString(String byte_) {
		try {
			Byte uByte = new Byte(byte_);
			return uByte;
		} catch (IllegalArgumentException iae) {
			return null;
		}
	}

	@Override
	public String convertToString(Byte byte_) {
		return byte_.toString();
	}
}
