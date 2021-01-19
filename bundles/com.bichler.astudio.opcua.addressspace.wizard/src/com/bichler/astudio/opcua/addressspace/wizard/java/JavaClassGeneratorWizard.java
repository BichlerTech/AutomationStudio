package com.bichler.astudio.opcua.addressspace.wizard.java;

import org.eclipse.jface.wizard.Wizard;

import com.bichler.astudio.opcua.addressspace.wizard.Activator;
import com.bichler.astudio.opcua.addressspace.wizard.java.page.DefaultConfigurationPage;
import com.bichler.astudio.utils.internationalization.CustomString;

public class JavaClassGeneratorWizard extends Wizard {

	private DefaultConfigurationPage general;

	public JavaClassGeneratorWizard() {
		super();
		setWindowTitle(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.title"));
	}

	@Override
	public void addPages() {
		this.general = new DefaultConfigurationPage();
		addPage(this.general);
	}

	@Override
	public boolean performFinish() {
		return true;
	}

	public String getDestination() {
		return this.general.getDestination();
	}

	public String getConstantsName() {
		return this.general.getConstantName();
	}

	public String getPackageName() {
		return this.general.getPackageName();
	}

}
