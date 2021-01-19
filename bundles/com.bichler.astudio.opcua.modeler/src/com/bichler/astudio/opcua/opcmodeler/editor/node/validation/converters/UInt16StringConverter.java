package com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters;

import org.opcfoundation.ua.builtintypes.UnsignedShort;

import com.richclientgui.toolbox.validation.converter.IContentsStringConverter;

public class UInt16StringConverter implements IContentsStringConverter<UnsignedShort> {
	@Override
	public UnsignedShort convertFromString(String unsignedInteger) {
		try {
			UnsignedShort uInt = new UnsignedShort(unsignedInteger);
			return uInt;
		} catch (IllegalArgumentException iae) {
			return null;
		}
	}

	@Override
	public String convertToString(UnsignedShort unsignedInteger) {
		return unsignedInteger.toString();
	}
}
