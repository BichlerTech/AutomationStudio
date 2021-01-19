package com.bichler.astudio.editor.pubsub.wizard;

import com.bichler.astudio.editor.pubsub.nodes.PubSubWriterGroup;
import com.bichler.astudio.editor.pubsub.wizard.page.DetailWriterGroupMessageSettingsPage;
import com.bichler.astudio.editor.pubsub.wizard.page.DetailWriterGroupPropertiesPage;
import com.bichler.astudio.editor.pubsub.wizard.page.DetailWriterGroupTransportSettingsPage;
import com.bichler.astudio.editor.pubsub.wizard.page.PubSubWriterGroupPage;

public class PubSubWriterGroupWizard extends AbstractPubSubWizard {

	private PubSubWriterGroupPage pageOne;
	public DetailWriterGroupMessageSettingsPage pageTwo;
	public DetailWriterGroupTransportSettingsPage pageThree;
	public DetailWriterGroupPropertiesPage pageFour;

	public PubSubWriterGroupWizard(PubSubWriterGroup element) {
		super(element);
		setWindowTitle("PubSubWriterGroup element");
	}

	@Override
	public void addPages() {
		this.pageOne = new PubSubWriterGroupPage();
		addPage(this.pageOne);

		this.pageTwo = new DetailWriterGroupMessageSettingsPage();
		addPage(this.pageTwo);

		this.pageThree = new DetailWriterGroupTransportSettingsPage();
		addPage(this.pageThree);

		this.pageFour = new DetailWriterGroupPropertiesPage();
		addPage(this.pageFour);
	}

	@Override
	public boolean performFinish() {
		setIfDirty(getElement().getName(), this.pageOne.getElementName());
		getElement().setName(this.pageOne.getElementName());

		setIfDirty(getElement().getEnabled(), this.pageOne.isEnabled());
		getElement().setEnabled(this.pageOne.isEnabled());

		setIfDirty(getElement().getWriterGroupId(), this.pageOne.getWriterGroupId());
		getElement().setWriterGroupId(this.pageOne.getWriterGroupId());

		setIfDirty(getElement().getPublishingInterval(), this.pageOne.getPublishingInterval());
		getElement().setPublishingInterval(this.pageOne.getPublishingInterval());

		setIfDirty(getElement().getKeepAliveTime(), this.pageOne.getKeepAlivetime());
		getElement().setKeepAliveTime(this.pageOne.getKeepAlivetime());

		setIfDirty(getElement().getPriority(), this.pageOne.getPriority());
		getElement().setPriority(this.pageOne.getPriority());

		setIfDirty(getElement().getSecurityMode(), this.pageOne.getSecurityMode());
		getElement().setSecurityMode(this.pageOne.getSecurityMode());

		setIfDirty(getElement().getTransportSettings(), this.pageThree.getTransportSettings());
		getElement().setTransportSettings(this.pageThree.getTransportSettings());

		setIfDirty(getElement().getGroupProperties(), this.pageOne.getGroupProperties());
		getElement().setGroupProperties(getElement().getGroupProperties());

		setIfDirty(getElement().getEncodingMimeType(), this.pageOne.getEncodingType());
		getElement().setEncodingMimeType(this.pageOne.getEncodingType());

		setIfDirty(getElement().getMaxEncapsulatedDataSetMessageCount(),
				this.pageOne.getMaxEncapsulationDataSetMessageCount());
		getElement().setMaxEncapsulatedDataSetMessageCount(this.pageOne.getMaxEncapsulationDataSetMessageCount());

		setIfDirty(getElement().getRtLevel(), this.pageOne.getRTLevel());
		getElement().setRtLevel(this.pageOne.getRTLevel());

		setIfDirty(getElement().getConfigurationFrozen(), this.pageOne.isConfigurationFrozen());
		getElement().setConfigurationFrozen(this.pageOne.isConfigurationFrozen());

		setIfDirty(getElement().getMessageSettings(), this.pageTwo.getWriterGroupMessage());
		getElement().setMessageSettings(this.pageTwo.getWriterGroupMessage());

		setIfDirty(getElement().getTransportSettings(), this.pageThree.getTransportSettings());
		getElement().setTransportSettings(this.pageThree.getTransportSettings());

		setIfDirty(getElement().getGroupProperties(), this.pageFour.getProperties());
		getElement().setGroupProperties(this.pageFour.getProperties());

		return true;
	}

	@Override
	public PubSubWriterGroup getElement() {
		return (PubSubWriterGroup) super.getElement();
	}

}
