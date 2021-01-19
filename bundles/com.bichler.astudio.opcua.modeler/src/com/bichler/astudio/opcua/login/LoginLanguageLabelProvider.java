package com.bichler.astudio.opcua.login;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class LoginLanguageLabelProvider extends LabelProvider {
	@Override
	public Image getImage(Object element) {
		return ((LanguageItem) element).getImage();
	}

	@Override
	public String getText(Object element) {
		return ((LanguageItem) element).getLanguage();
	}
}
