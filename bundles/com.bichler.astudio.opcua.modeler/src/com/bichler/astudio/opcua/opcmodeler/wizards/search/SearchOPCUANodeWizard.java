package com.bichler.astudio.opcua.opcmodeler.wizards.search;

import org.eclipse.jface.wizard.Wizard;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class SearchOPCUANodeWizard extends Wizard {
	private SearchWizardPage searchPage;

	public SearchOPCUANodeWizard() {
		super();
		setWindowTitle(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.node.lookup.title"));
	}

	@Override
	public void addPages() {
		this.searchPage = new SearchWizardPage();
		addPage(this.searchPage);
	}

	@Override
	public boolean performFinish() {
		return true;
	}
}
