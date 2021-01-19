package com.bichler.astudio.opcua.login.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

public class LoginPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	public static final String AUTO_LOGIN = "prefs_auto_login";
	private ScopedPreferenceStore preferences = null;

	public LoginPreferencePage() {
		// super(GRID);
	}

	public LoginPreferencePage(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}

	public LoginPreferencePage(String title, ImageDescriptor image) {
		super(title, image);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub
	}

	@Override
	protected Control createContents(Composite parent) {
		// TODO Auto-generated method stub
		return null;
	}
}
