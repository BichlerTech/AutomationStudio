package com.bichler.astudio.editor.pubsub.wizard;

import com.bichler.astudio.editor.pubsub.nodes.PubSubConnection;
import com.bichler.astudio.editor.pubsub.wizard.page.DetailConnectionAddressPage;
import com.bichler.astudio.editor.pubsub.wizard.page.DetailConnectionPropertiesPage;
import com.bichler.astudio.editor.pubsub.wizard.page.DetailConnectionTransportSettingsPage;
import com.bichler.astudio.editor.pubsub.wizard.page.DetailPropertiesPage;
import com.bichler.astudio.editor.pubsub.wizard.page.PubSubConnectionPage;

public class PubSubConnectionWizard extends AbstractPubSubWizard {

	private PubSubConnectionPage pageOne;
	public DetailConnectionAddressPage pageTwo;
	public DetailConnectionTransportSettingsPage pageThree;
	public DetailPropertiesPage pageFour;

	public PubSubConnectionWizard(PubSubConnection element) {
		super(element);
		setWindowTitle("PubSubConnection element");
	}

	@Override
	public void addPages() {
		this.pageOne = new PubSubConnectionPage();
		addPage(this.pageOne);

		this.pageTwo = new DetailConnectionAddressPage();
		addPage(this.pageTwo);

		this.pageThree = new DetailConnectionTransportSettingsPage();
		addPage(this.pageThree);

		this.pageFour = new DetailConnectionPropertiesPage();
		addPage(this.pageFour);
	}

	@Override
	public boolean performFinish() {
		setIfDirty(getElement().getName(), this.pageOne.getElementName());
		getElement().setName(this.pageOne.getElementName());

		setIfDirty(getElement().getEnabled(), this.pageOne.isEnabled());
		getElement().setEnabled(this.pageOne.isEnabled());

		setIfDirty(getElement().getPublisherIdType(), this.pageOne.getPublisherIdType());
		getElement().setPublisherIdType(this.pageOne.getPublisherIdType());

		setIfDirty(getElement().getPublisherId(), this.pageOne.getPublisherId());
		getElement().setPublisherId(this.pageOne.getPublisherId());

		setIfDirty(getElement().getTransportProfileUri(), this.pageOne.getTransportProfileUri());
		getElement().setTransportProfileUri(this.pageOne.getTransportProfileUri());

		setIfDirty(getElement().getConfigurationFrozen(), this.pageOne.isConfigurationFrozen());
		getElement().setConfigurationFrozen(this.pageOne.isConfigurationFrozen());

		setIfDirty(getElement().getAddress(), this.pageTwo.getAddress());
		getElement().setAddress(this.pageTwo.getAddress());

		setIfDirty(getElement().getConnectionTransportSettings(), this.pageThree.getConnectionTransportSettings());
		getElement().setConnectionTransportSettings(this.pageThree.getConnectionTransportSettings());
		
		setIfDirty(getElement().getConnectionProperties(), this.pageFour.getProperties());
		getElement().setConnectionProperties(this.pageFour.getProperties());
		
		return true;
	}

	@Override
	public PubSubConnection getElement() {
		return (PubSubConnection) super.getElement();
	}

}
