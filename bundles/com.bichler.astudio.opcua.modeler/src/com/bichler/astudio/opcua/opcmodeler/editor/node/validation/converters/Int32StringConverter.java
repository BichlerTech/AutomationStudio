package com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters;

import com.richclientgui.toolbox.validation.converter.IContentsStringConverter;

public class Int32StringConverter implements IContentsStringConverter<Integer> {
	@Override
	public Integer convertFromString(String unsignedInteger) {
		try {
			Integer uInt = new Integer(unsignedInteger);
			return uInt;
		} catch (IllegalArgumentException iae) {
			return null;
		}
	}

	@Override
	public String convertToString(Integer unsignedInteger) {
		return unsignedInteger.toString();
	}
}
