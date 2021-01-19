package com.bichler.astudio.opcua.opcmodeler.wizards;

import opc.sdk.core.node.Node;

import org.eclipse.jface.wizard.Wizard;

import com.bichler.astudio.opcua.opcmodeler.wizards.create.page.GeneralCreatePage;

public abstract class AbstractCreateWizard extends Wizard {
	private GeneralCreatePage generalPage;
	private Node selectedParent;

	public AbstractCreateWizard(Node selectedParent) {
		this.selectedParent = selectedParent;
	}

	@Override
	public void addPages() {
		this.generalPage = new GeneralCreatePage(this.selectedParent);
		this.generalPage.setTitle(getTitle());
		this.generalPage.setDescription(getDescription());
		addPage(this.generalPage);
	}

	public GeneralCreatePage getGeneralPage() {
		return this.generalPage;
	}

	protected Node getParentNode() {
		return this.selectedParent;
	}

	protected abstract String getTitle();

	protected abstract String getDescription();
}
