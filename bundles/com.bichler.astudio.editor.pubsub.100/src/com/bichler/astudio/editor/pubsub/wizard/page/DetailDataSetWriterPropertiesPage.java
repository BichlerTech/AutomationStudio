package com.bichler.astudio.editor.pubsub.wizard.page;

import com.bichler.astudio.editor.pubsub.wizard.PubSubDataSetWriterWizard;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperKeyValuePair;

public class DetailDataSetWriterPropertiesPage extends DetailPropertiesPage {

	public DetailDataSetWriterPropertiesPage() {
		super();
		setTitle("Dataset properties");
		setDescription("Properties of a dataset");
	}
	
	@Override
	public PubSubDataSetWriterWizard getWizard() {
		return (PubSubDataSetWriterWizard) super.getWizard();
	}

	@Override
	WrapperKeyValuePair[] getKeyValuePair() {
		return getWizard().getElement().getDataSetWriterProperties();
	}
}
