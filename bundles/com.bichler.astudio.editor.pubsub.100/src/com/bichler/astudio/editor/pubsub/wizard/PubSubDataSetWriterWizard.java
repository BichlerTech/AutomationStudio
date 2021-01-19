package com.bichler.astudio.editor.pubsub.wizard;

import com.bichler.astudio.editor.pubsub.nodes.PubSubDataSetWriter;
import com.bichler.astudio.editor.pubsub.wizard.page.DetailDataSetWriterPropertiesPage;
import com.bichler.astudio.editor.pubsub.wizard.page.DetailDataSetWriterMessageSettingsPage;
import com.bichler.astudio.editor.pubsub.wizard.page.DetailPropertiesPage;
import com.bichler.astudio.editor.pubsub.wizard.page.DetailDataSetWriterTransportSettingsPage;
import com.bichler.astudio.editor.pubsub.wizard.page.PubSubDataSetWriterPage;

public class PubSubDataSetWriterWizard extends AbstractPubSubWizard {

	private PubSubDataSetWriterPage pageOne;
	public DetailDataSetWriterMessageSettingsPage pageTwo;
	public DetailDataSetWriterTransportSettingsPage pageThree;
	public DetailPropertiesPage pageFour;

	public PubSubDataSetWriterWizard(PubSubDataSetWriter element) {
		super(element);
		setWindowTitle("PubSubDataSetWriter element");
	}

	@Override
	public void addPages() {
		this.pageOne = new PubSubDataSetWriterPage();
		addPage(this.pageOne);

		this.pageTwo = new DetailDataSetWriterMessageSettingsPage();
		addPage(this.pageTwo);

		this.pageThree = new DetailDataSetWriterTransportSettingsPage();
		addPage(this.pageThree);

		this.pageFour = new DetailDataSetWriterPropertiesPage();
		addPage(this.pageFour);
	}

	@Override
	public boolean performFinish() {
		setIfDirty(getElement().getName(), this.pageOne.getElementName());
		getElement().setName(this.pageOne.getElementName());

		setIfDirty(getElement().getDataSetWriterId(), this.pageOne.getDataSetWriterId());
		getElement().setDataSetWriterId(this.pageOne.getDataSetWriterId());

		setIfDirty(getElement().getDataSetFieldContentMask(), this.pageOne.getDataSetFieldContentMask());
		getElement().setDataSetFieldContentMask(this.pageOne.getDataSetFieldContentMask());

		setIfDirty(getElement().getKeyFrameCount(), this.pageOne.getKeyFrameCount());
		getElement().setKeyFrameCount(this.pageOne.getKeyFrameCount());

		setIfDirty(getElement().getDataSetName(), this.pageOne.getDataSetName());
		getElement().setDataSetName(this.pageOne.getDataSetName());

		setIfDirty(getElement().getConfigurationFrozen(), this.pageOne.isConfigurationFrozen());
		getElement().setConfigurationFrozen(this.pageOne.isConfigurationFrozen());

		//getElement().setDataSetWriterPropertiesSize(this.pageOne.getDataSetWriterPropertiesSize());

		setIfDirty(getElement().getMessageSettings(), this.pageTwo.getMessageSettings());
		getElement().setMessageSettings(this.pageTwo.getMessageSettings());

		setIfDirty(getElement().getTransportSettings(), this.pageThree.getTransportSettings());
		getElement().setTransportSettings(this.pageThree.getTransportSettings());

		setIfDirty(getElement().getDataSetWriterProperties(), this.pageFour.getProperties());
		getElement().setDataSetWriterProperties(this.pageFour.getProperties());

		return true;
	}

	@Override
	public PubSubDataSetWriter getElement() {
		return (PubSubDataSetWriter) super.getElement();
	}

}
