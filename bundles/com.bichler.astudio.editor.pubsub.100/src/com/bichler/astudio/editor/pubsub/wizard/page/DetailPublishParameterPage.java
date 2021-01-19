package com.bichler.astudio.editor.pubsub.wizard.page;

import com.bichler.astudio.editor.pubsub.nodes.DataSetVariable;
import com.bichler.astudio.editor.pubsub.nodes.PubSubDataSetField;
import com.bichler.astudio.editor.pubsub.wizard.PubSubDataSetFieldWizard;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperPublishedVariableParameter;

public class DetailPublishParameterPage extends AbstractPublishedVariableDataTypePage {

	public DetailPublishParameterPage() {
		super();
	}

	@Override
	public PubSubDataSetFieldWizard getWizard() {
		return (PubSubDataSetFieldWizard) super.getWizard();
	}

	@Override
	WrapperPublishedVariableParameter getElement() {
		PubSubDataSetField element = getWizard().getElement();
		if (element == null) {
			return null;
		}
		DataSetVariable field = element.getField();
		if (field == null) {
			return null;
		}
		return field.getPublishParameters();
	}

}
