package com.bichler.astudio.editor.pubsub.wizard;

import com.bichler.astudio.editor.pubsub.nodes.PubSubPublishedDataSet;
import com.bichler.astudio.editor.pubsub.wizard.page.DetailFieldMetaDataListPage;
import com.bichler.astudio.editor.pubsub.wizard.page.DetailItemsTemplatePropertiesPage;
import com.bichler.astudio.editor.pubsub.wizard.page.DetailPublishedConfigPage;
import com.bichler.astudio.editor.pubsub.wizard.page.DetailPublishedDataSetVersionPage;
import com.bichler.astudio.editor.pubsub.wizard.page.DetailPublishedMetadataFieldPage;
import com.bichler.astudio.editor.pubsub.wizard.page.DetailPublishedVariableDataTypePage;
import com.bichler.astudio.editor.pubsub.wizard.page.DetailVersionPage;
import com.bichler.astudio.editor.pubsub.wizard.page.PubSubPublishedDataSetPage;

public class PubSubPublishedDataSetWizard extends AbstractPubSubWizard {

	private PubSubPublishedDataSetPage pageOne;
	public DetailPublishedConfigPage pageTwo;
	// pageSeven -> pageThree
	public DetailPublishedMetadataFieldPage pageThree;
	public DetailVersionPage pageFour;
	public DetailItemsTemplatePropertiesPage pageFive;
	public DetailPublishedVariableDataTypePage pageSix;
	public DetailFieldMetaDataListPage pageSeven;

	public PubSubPublishedDataSetWizard(PubSubPublishedDataSet element) {
		super(element);
		setWindowTitle("PubSubPublishedDataSet element");
	}

	@Override
	public void addPages() {
		this.pageOne = new PubSubPublishedDataSetPage();
		addPage(this.pageOne);

		this.pageTwo = new DetailPublishedConfigPage();
		addPage(this.pageTwo);
		
		this.pageThree = new DetailPublishedMetadataFieldPage();
		addPage(this.pageThree);
		
		this.pageFour = new DetailPublishedDataSetVersionPage();
		addPage(this.pageFour);
		
		this.pageFive = new DetailItemsTemplatePropertiesPage();
		addPage(this.pageFive);
		
		this.pageSix = new DetailPublishedVariableDataTypePage();
		addPage(this.pageSix);
		
		this.pageSeven = new DetailFieldMetaDataListPage();
		addPage(this.pageSeven);
	}

	@Override
	public boolean performFinish() {
		setIfDirty(getElement().getName(), this.pageOne.getElementName());
		getElement().setName(this.pageOne.getElementName());

		setIfDirty(getElement().getPublishedDataSetType(), this.pageOne.getPublishedDataSetFieldType());
		getElement().setPublishedDataSetType(this.pageOne.getPublishedDataSetFieldType());

		setIfDirty(getElement().getConfigurationFrozen(), this.pageOne.isConfigurationFrozen());
		getElement().setConfigurationFrozen(this.pageOne.isConfigurationFrozen());

		setIfDirty(getElement().getConfig(), this.pageTwo.getConfig());
		getElement().setConfig(this.pageTwo.getConfig());

		return true;
	}

	@Override
	public PubSubPublishedDataSet getElement() {
		return (PubSubPublishedDataSet) super.getElement();
	}

}
