package com.bichler.astudio.editor.pubsub.wizard;

import com.bichler.astudio.editor.pubsub.nodes.PubSubEntryModelNode;
import com.bichler.astudio.editor.pubsub.nodes.PubSubReaderGroup;
import com.bichler.astudio.editor.pubsub.wizard.page.PubSubReaderGroupPage;

public class PubSubReaderGroupWizard extends AbstractPubSubWizard {

	private PubSubReaderGroupPage pageOne;

	public PubSubReaderGroupWizard(PubSubReaderGroup element) {
		super(element);
		setWindowTitle("PubSubReaderGroup element");
	}

	@Override
	public void addPages() {
		this.pageOne = new PubSubReaderGroupPage();
		addPage(this.pageOne);
	}

	@Override
	public boolean performFinish() {
		setIfDirty(getElement().getName(), this.pageOne.getElementName());
		getElement().setName(this.pageOne.getElementName());

		setIfDirty(getElement().getSecurityParameters(), this.pageOne.getSecurityParameters());
		getElement().setSecurityParameters(this.pageOne.getSecurityParameters());

		return true;
	}

	@Override
	public PubSubReaderGroup getElement() {
		return (PubSubReaderGroup) super.getElement();
	}
	
	
}
