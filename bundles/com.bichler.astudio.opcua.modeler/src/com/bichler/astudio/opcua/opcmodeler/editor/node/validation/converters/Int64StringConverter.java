package com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters;

import com.richclientgui.toolbox.validation.converter.IContentsStringConverter;

public class Int64StringConverter implements IContentsStringConverter<Long> {
	@Override
	public Long convertFromString(String long_) {
		try {
			Long _long = new Long(long_);
			return _long;
		} catch (IllegalArgumentException iae) {
			return null;
		}
	}

	@Override
	public String convertToString(Long long_) {
		return long_.toString();
	}
}
