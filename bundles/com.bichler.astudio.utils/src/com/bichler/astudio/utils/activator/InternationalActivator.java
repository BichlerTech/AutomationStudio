package com.bichler.astudio.utils.activator;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;

public abstract class InternationalActivator extends AbstractUIPlugin {

	public ResourceBundle RESOURCE_BUNDLE;

	public void setLanguageResourceBundle(Locale locale) {
		Locale.setDefault(locale);
		try {
			RESOURCE_BUNDLE = ResourceBundle.getBundle(getBundleName(), locale, this.getClass().getClassLoader());
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			RESOURCE_BUNDLE = ResourceBundle.getBundle(getBundleName(), Locale.ENGLISH,
					this.getClass().getClassLoader());
		}
	}

	public abstract String getBundleName();

	public File getFile(Bundle bundle, IPath path) {
		URL url = FileLocator.find(bundle, path, null);
		if (url != null) {
			try {
				URL url2 = FileLocator.toFileURL(url);
				// remove '/' beginning of path if possible
				return new File(url2.getFile());
			} catch (IOException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
				RESOURCE_BUNDLE = ResourceBundle.getBundle(getBundleName(), Locale.ENGLISH);
			}
		}
		return null;
	}
}
