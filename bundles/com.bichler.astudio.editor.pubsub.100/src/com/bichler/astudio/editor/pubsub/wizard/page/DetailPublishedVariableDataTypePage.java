package com.bichler.astudio.editor.pubsub.wizard.page;

import com.bichler.astudio.editor.pubsub.nodes.PubSubPublishedDataItemsTemplate;
import com.bichler.astudio.editor.pubsub.nodes.PubSubPublishedDataSet;
import com.bichler.astudio.editor.pubsub.wizard.PubSubPublishedDataSetWizard;

public class DetailPublishedVariableDataTypePage extends AbstractPublishedVariableDataTypePage {

	public DetailPublishedVariableDataTypePage() {
		super();
	}

	@Override
	public PubSubPublishedDataSetWizard getWizard() {
		return (PubSubPublishedDataSetWizard) super.getWizard();
	}

	@Override
	Object getElement() {
		PubSubPublishedDataSet element = getWizard().getElement();
		if (element == null) {
			return null;
		}
		Object config = element.getConfig();
		if (config instanceof PubSubPublishedDataItemsTemplate) {
			return ((PubSubPublishedDataItemsTemplate)config).getVariablesToAdd();
		}
		return null;
	}

}
