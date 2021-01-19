package com.bichler.astudio.editor.pubsub.wizard.page;

import com.bichler.astudio.editor.pubsub.wizard.PubSubWriterGroupWizard;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperKeyValuePair;

public class DetailWriterGroupPropertiesPage extends DetailPropertiesPage {

	public DetailWriterGroupPropertiesPage() {
		super();
		setTitle("WriterGroup properties");
		setDescription("Properties of a writer group");
	}

	@Override
	public PubSubWriterGroupWizard getWizard() {
		return (PubSubWriterGroupWizard) super.getWizard();
	}

	@Override
	WrapperKeyValuePair[] getKeyValuePair() {
		return getWizard().getElement().getGroupProperties();
	}
}
