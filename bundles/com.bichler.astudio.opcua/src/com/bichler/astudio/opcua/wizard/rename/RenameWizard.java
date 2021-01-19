package com.bichler.astudio.opcua.wizard.rename;

import org.eclipse.jface.wizard.Wizard;

import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class RenameWizard extends Wizard {
	/** rename page */
	private RenameWizardPage pageRename;
	/** rename */
	private String rename;
	private String originname;

	public RenameWizard(String name) {
		setWindowTitle(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.rename.title"));
		this.originname = name;
	}

	@Override
	public void addPages() {
		this.pageRename = new RenameWizardPage(this.originname);
		addPage(this.pageRename);
	}

	@Override
	public boolean performFinish() {
		this.rename = this.pageRename.getRename();
		return true;
	}

	public String getRename() {
		return this.rename;
	}
}
