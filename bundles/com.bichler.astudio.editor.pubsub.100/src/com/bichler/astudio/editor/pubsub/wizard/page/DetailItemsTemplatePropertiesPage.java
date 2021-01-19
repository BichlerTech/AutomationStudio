package com.bichler.astudio.editor.pubsub.wizard.page;

import com.bichler.astudio.editor.pubsub.wizard.PubSubPublishedDataSetWizard;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperFieldMetaData;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperKeyValuePair;

public class DetailItemsTemplatePropertiesPage extends DetailPropertiesPage {
	
	public DetailItemsTemplatePropertiesPage() {
		super();
		setTitle("Detail items template properties");
		setDescription("Properties of a publish item template");
	}

	@Override
	public PubSubPublishedDataSetWizard getWizard() {
		return (PubSubPublishedDataSetWizard) super.getWizard();
	}

	@Override
	WrapperKeyValuePair[] getKeyValuePair() {
		return new WrapperKeyValuePair[0];
	}

	public void setPropertiesFromElement(WrapperFieldMetaData metaData) {
		setModelValue(metaData.getProperties());
	}
}
