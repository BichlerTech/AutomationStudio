package com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters;

import java.util.Locale;

import com.richclientgui.toolbox.validation.converter.IContentsStringConverter;

public class LocaleStringConverter implements IContentsStringConverter<Locale> {
	@Override
	public Locale convertFromString(String stringValue) {
		Locale toConvert = null;
		for (Locale locale : Locale.getAvailableLocales()) {
			if (locale.toString().equals(stringValue)) {
				toConvert = locale;
				break;
			}
		}
		return toConvert;
	}

	@Override
	public String convertToString(Locale locale) {
		return locale.toString();
	}
}
