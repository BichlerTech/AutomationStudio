package com.bichler.astudio.editor.pubsub.wizard.page;

import com.bichler.astudio.editor.pubsub.wizard.PubSubConnectionWizard;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperKeyValuePair;

public class DetailConnectionPropertiesPage extends DetailPropertiesPage {
	
	public DetailConnectionPropertiesPage() {
		super();
		setTitle("Connection properties");
		setDescription("Properties of a connection");
	}

	@Override
	public PubSubConnectionWizard getWizard() {
		return (PubSubConnectionWizard) super.getWizard();
	}

	@Override
	WrapperKeyValuePair[] getKeyValuePair() {
		return getWizard().getElement().getConnectionProperties();
	}
}
