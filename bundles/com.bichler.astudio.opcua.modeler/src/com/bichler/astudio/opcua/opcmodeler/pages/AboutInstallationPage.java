package com.bichler.astudio.opcua.opcmodeler.pages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.about.InstallationPage;

public class AboutInstallationPage extends InstallationPage {
	@Override
	public void createControl(Composite parent) {
		Label l = new Label(parent, SWT.BORDER);
		l.setText("Hallo");
	}
}
