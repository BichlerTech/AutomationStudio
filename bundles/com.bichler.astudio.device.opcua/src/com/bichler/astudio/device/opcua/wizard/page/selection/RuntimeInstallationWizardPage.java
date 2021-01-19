package com.bichler.astudio.device.opcua.wizard.page.selection;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.bichler.astudio.device.opcua.DeviceActivator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class RuntimeInstallationWizardPage extends WizardPage {

	protected String externalPath;

	/**
	 * Create the wizard.
	 */
	public RuntimeInstallationWizardPage() {
		super("wizardPage");
		setTitle(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.target.install.monitor.title"));
		setDescription(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.wizard.deviceinstall.page.message"));
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(1, false));

		createPart(container);

		setHandler();

	}

	private void createPart(Composite container) {

	}

	private void setHandler() {

	}

	
	public String getExternalPath() {
		return this.externalPath;
	}

}
