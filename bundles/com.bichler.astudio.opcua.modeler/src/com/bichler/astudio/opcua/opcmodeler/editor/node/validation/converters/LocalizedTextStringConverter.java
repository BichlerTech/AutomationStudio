package com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters;

import java.util.Locale;

import org.opcfoundation.ua.builtintypes.LocalizedText;

import com.richclientgui.toolbox.validation.converter.IContentsStringConverter;

public class LocalizedTextStringConverter implements IContentsStringConverter<LocalizedText> {
	@Override
	public LocalizedText convertFromString(String value) {
		try {
			int startLocale = value.indexOf("(");
			int endLocale = value.indexOf(")");
			String locale = null;
			String txt = null;
			if (startLocale >= 0 || endLocale > 0) {
				locale = value.substring(startLocale + 1, endLocale);
				txt = value.substring(endLocale + 2);
			} else {
				txt = value;
			}
			Locale l = null;
			if (locale != null && !locale.isEmpty()) {
				l = new Locale(locale);
			}
			LocalizedText text = new LocalizedText(txt, l);
			return text;
		} catch (IllegalArgumentException iae) {
			return null;
		}
	}

	@Override
	public String convertToString(LocalizedText value) {
		return value.toString();
	}
}
