package com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters;

import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;

import com.richclientgui.toolbox.validation.converter.IContentsStringConverter;

public class ByteStringConverter implements IContentsStringConverter<UnsignedByte> {
	@Override
	public UnsignedByte convertFromString(String byte_) {
		try {
			UnsignedByte uByte = new UnsignedByte(byte_);
			return uByte;
		} catch (IllegalArgumentException iae) {
			return null;
		}
	}

	@Override
	public String convertToString(UnsignedByte byte_) {
		return byte_.toString();
	}
}
