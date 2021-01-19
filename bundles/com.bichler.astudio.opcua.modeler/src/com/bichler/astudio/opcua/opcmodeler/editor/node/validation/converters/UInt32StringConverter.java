package com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters;

import org.opcfoundation.ua.builtintypes.UnsignedInteger;

import com.richclientgui.toolbox.validation.converter.IContentsStringConverter;

public class UInt32StringConverter implements IContentsStringConverter<UnsignedInteger> {
	@Override
	public UnsignedInteger convertFromString(String unsignedInteger) {
		try {
			UnsignedInteger uInt = new UnsignedInteger(unsignedInteger);
			return uInt;
		} catch (IllegalArgumentException iae) {
			return null;
		}
	}

	@Override
	public String convertToString(UnsignedInteger unsignedInteger) {
		return unsignedInteger.toString();
	}
}
