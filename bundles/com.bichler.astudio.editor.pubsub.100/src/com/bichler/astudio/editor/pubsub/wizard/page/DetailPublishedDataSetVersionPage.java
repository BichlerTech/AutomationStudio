package com.bichler.astudio.editor.pubsub.wizard.page;

import com.bichler.astudio.editor.pubsub.nodes.PubSubPublishedDataItemsTemplate;
import com.bichler.astudio.editor.pubsub.nodes.PubSubPublishedDataSet;
import com.bichler.astudio.editor.pubsub.wizard.PubSubPublishedDataSetWizard;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperConfigurationVersion;

public class DetailPublishedDataSetVersionPage extends DetailVersionPage {

	public DetailPublishedDataSetVersionPage() {
		super();
	}

	@Override
	public PubSubPublishedDataSetWizard getWizard() {
		return (PubSubPublishedDataSetWizard) super.getWizard();
	}

	void setDefaultValue() {
		PubSubPublishedDataSet element = getWizard().getElement();

		if (element.getConfig() != null) {
			Object config = element.getConfig();
			if (config instanceof PubSubPublishedDataItemsTemplate) {
				WrapperConfigurationVersion version = ((PubSubPublishedDataItemsTemplate) config).getMetaData()
						.getConfigurationVersion();

				if (version != null) {
					if (version.getMinorVersion() != null) {
						txt_minVersion.setText(version.getMinorVersion().toString());
					}
					if (version.getMajorVersion() != null) {
						txt_maxVersion.setText(version.getMajorVersion().toString());
					}
				}
				this.model = version.clone();
			}
		}
	}
}
