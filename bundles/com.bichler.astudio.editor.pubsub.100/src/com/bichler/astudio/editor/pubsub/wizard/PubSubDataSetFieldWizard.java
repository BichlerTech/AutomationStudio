package com.bichler.astudio.editor.pubsub.wizard;

import com.bichler.astudio.editor.pubsub.nodes.PubSubDataSetField;
import com.bichler.astudio.editor.pubsub.wizard.page.DetailDataSetFieldVersionPage;
import com.bichler.astudio.editor.pubsub.wizard.page.DetailDataSetPage;
import com.bichler.astudio.editor.pubsub.wizard.page.DetailPublishParameterPage;
import com.bichler.astudio.editor.pubsub.wizard.page.DetailVersionPage;
import com.bichler.astudio.editor.pubsub.wizard.page.PubSubDataSetFieldPage;

public class PubSubDataSetFieldWizard extends AbstractPubSubWizard {

	private PubSubDataSetFieldPage pageOne;
	public DetailDataSetPage pageTwo;
	public DetailVersionPage pageThree;
	public DetailPublishParameterPage pageFour;

	public PubSubDataSetFieldWizard(PubSubDataSetField element) {
		super(element);
		setWindowTitle("PubSubDataSetField element");
	}

	@Override
	public void addPages() {
		this.pageOne = new PubSubDataSetFieldPage();
		addPage(this.pageOne);

		this.pageTwo = new DetailDataSetPage();
		addPage(this.pageTwo);

		this.pageThree = new DetailDataSetFieldVersionPage();
		addPage(this.pageThree);

		this.pageFour = new DetailPublishParameterPage();
		addPage(this.pageFour);
	}

	@Override
	public boolean performFinish() {
		setIfDirty(getElement().getDataSetFieldType(), this.pageOne.getDataSetFieldType());
		getElement().setDataSetFieldType(this.pageOne.getDataSetFieldType());

		setIfDirty(getElement().getConfigurationFrozen(), this.pageOne.isConfigurationFrozen());
		getElement().setConfigurationFrozen(this.pageOne.isConfigurationFrozen());
		
		setIfDirty(getElement().getField(), this.pageTwo.getField());

		return true;
	}

	@Override
	public PubSubDataSetField getElement() {
		return (PubSubDataSetField) super.getElement();
	}
	
	
}
