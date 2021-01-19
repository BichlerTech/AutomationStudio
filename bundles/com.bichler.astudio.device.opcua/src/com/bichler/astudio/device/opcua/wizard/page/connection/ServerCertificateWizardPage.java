package com.bichler.astudio.device.opcua.wizard.page.connection;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.bichler.astudio.device.opcua.DeviceActivator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class ServerCertificateWizardPage extends WizardPage {

	private Button cb_newCert;

	public ServerCertificateWizardPage() {
		super("something");
		setTitle(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"wizard.certificate.title"));
		setDescription(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"wizard.certificate.description"));
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(2, false));

		this.cb_newCert = new Button(container, SWT.CHECK);
		cb_newCert.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		cb_newCert.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"wizard.certificate.newcertificate"));
		cb_newCert.setSelection(true);
	}

	public boolean getUploadNewCertificate() {
		return this.cb_newCert.getSelection();
	}

}
