package com.bichler.astudio.editor.pubsub.wizard.page;

import com.bichler.astudio.editor.pubsub.nodes.DataSetVariable;
import com.bichler.astudio.editor.pubsub.nodes.PubSubDataSetField;
import com.bichler.astudio.editor.pubsub.wizard.PubSubDataSetFieldWizard;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperConfigurationVersion;

public class DetailDataSetFieldVersionPage extends DetailVersionPage {

	public DetailDataSetFieldVersionPage() {
		super();
	}

	@Override
	public PubSubDataSetFieldWizard getWizard() {
		return (PubSubDataSetFieldWizard) super.getWizard();
	}

	@Override
	void setDefaultValue() {
		PubSubDataSetField element = getWizard().getElement();

		if (element.getField() != null) {
			DataSetVariable field = element.getField();
			WrapperConfigurationVersion version = field.getConfigurationVersion();
			
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
