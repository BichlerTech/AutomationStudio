package com.bichler.astudio.opcua.login;

import java.util.Locale;

import org.eclipse.swt.graphics.Image;

public class LanguageItem {
	private String language = "";
	private Image image = null;
	private Locale locale = Locale.ENGLISH;

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
}
