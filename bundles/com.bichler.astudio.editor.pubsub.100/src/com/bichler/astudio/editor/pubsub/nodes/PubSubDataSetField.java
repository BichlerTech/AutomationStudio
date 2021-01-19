package com.bichler.astudio.editor.pubsub.nodes;

import com.bichler.astudio.editor.pubsub.xml.PUBSUBENTRYTYPE;

public class PubSubDataSetField extends PubSubEntryModelNode {
	private DataSetFieldType dataSetFieldType;
	private DataSetVariable field;
//    union {
//        /* events need other config later */
//        UA_DataSetVariableConfig variable;
//    } field;
	/* This flag is 'read only' and is set internally based on the PubSub state. */
	private Boolean configurationFrozen;

	public PubSubDataSetField() {
		super();
		this.type = PUBSUBENTRYTYPE.DATASETFIELD;
	}

	public DataSetFieldType getDataSetFieldType() {
		return dataSetFieldType;
	}

	public void setDataSetFieldType(DataSetFieldType dataSetFieldType) {
		this.dataSetFieldType = dataSetFieldType;
	}

	public DataSetVariable getField() {
		return field;
	}

	public void setField(DataSetVariable field) {
		this.field = field;
	}

	public Boolean getConfigurationFrozen() {
		return configurationFrozen;
	}

	public void setConfigurationFrozen(Boolean configurationFrozen) {
		this.configurationFrozen = configurationFrozen;
	}
}
