package com.bichler.astudio.editor.pubsub.wizard.page;

import org.eclipse.jface.wizard.WizardPage;

import com.bichler.astudio.editor.pubsub.nodes.PubSubEntryModelNode;

public abstract class AbstractMainWizardPage extends WizardPage {

	protected AbstractMainWizardPage(String pageName) {
		super(pageName);
	}

	@Override
	public boolean canFlipToNextPage() {
		return false;
	}

}
