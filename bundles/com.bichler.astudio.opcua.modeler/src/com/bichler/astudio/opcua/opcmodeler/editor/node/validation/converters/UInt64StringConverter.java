package com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters;

import org.opcfoundation.ua.builtintypes.UnsignedLong;

import com.richclientgui.toolbox.validation.converter.IContentsStringConverter;

public class UInt64StringConverter implements IContentsStringConverter<UnsignedLong> {
	@Override
	public UnsignedLong convertFromString(String unsignedLong) {
		try {
			UnsignedLong uInt = new UnsignedLong(unsignedLong);
			return uInt;
		} catch (IllegalArgumentException iae) {
			return null;
		}
	}

	@Override
	public String convertToString(UnsignedLong unsignedLong) {
		return unsignedLong.toString();
	}
}
