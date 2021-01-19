package com.bichler.astudio.opcua.opcmodeler.wizards.search;

import org.eclipse.jface.wizard.Wizard;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class FindAllNamespaceNodesWizard extends Wizard {
	private FindAllNamespaceNodesWizardPage pageOne;
	private String namespace;

	public FindAllNamespaceNodesWizard() {
		super();
		setWindowTitle(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.namespace.lookup.title"));
	}

	@Override
	public void addPages() {
		this.pageOne = new FindAllNamespaceNodesWizardPage();
		addPage(this.pageOne);
	}

	@Override
	public boolean performFinish() {
		this.namespace = this.pageOne.getNamespace();
		return true;
	}

	public String getNamespace() {
		return this.namespace;
	}
}
