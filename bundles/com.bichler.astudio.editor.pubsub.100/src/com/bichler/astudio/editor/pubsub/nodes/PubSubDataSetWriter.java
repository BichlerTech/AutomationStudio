package com.bichler.astudio.editor.pubsub.nodes;

import com.bichler.astudio.editor.pubsub.wizard.core.WrapperDataSetMetaData;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperKeyValuePair;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperWriterDataSetMessage;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperWriterDataSetTransport;
import com.bichler.astudio.editor.pubsub.xml.PUBSUBENTRYTYPE;

public class PubSubDataSetWriter extends PubSubEntryModelNode {
	
	private String name;
    private int dataSetWriterId;
    
    private DataSetFieldContentMask dataSetFieldContentMask;
    private int keyFrameCount;
    private WrapperWriterDataSetMessage messageSettings;
    private WrapperWriterDataSetTransport transportSettings;
    private String dataSetName;

    private int dataSetWriterPropertiesSize;
    WrapperKeyValuePair[] dataSetWriterProperties;
    /* This flag is 'read only' and is set internally based on the PubSub state. */
    private Boolean configurationFrozen;
    
    public PubSubDataSetWriter() {
		super();
    	this.type = PUBSUBENTRYTYPE.DATASETWRITER;
    }
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDataSetWriterId() {
		return dataSetWriterId;
	}
	public void setDataSetWriterId(int dataSetWriterId) {
		this.dataSetWriterId = dataSetWriterId;
	}
	public DataSetFieldContentMask getDataSetFieldContentMask() {
		return dataSetFieldContentMask;
	}
	public void setDataSetFieldContentMask(DataSetFieldContentMask dataSetFieldContentMask) {
		this.dataSetFieldContentMask = dataSetFieldContentMask;
	}
	public int getKeyFrameCount() {
		return keyFrameCount;
	}
	public void setKeyFrameCount(int keyFrameCount) {
		this.keyFrameCount = keyFrameCount;
	}
	public WrapperWriterDataSetMessage getMessageSettings() {
		return messageSettings;
	}
	public int getDataSetPropertiesSize() {
		return dataSetWriterPropertiesSize;
	}
	public void setMessageSettings(WrapperWriterDataSetMessage messageSettings) {
		this.messageSettings = messageSettings;
	}
	public WrapperWriterDataSetTransport getTransportSettings() {
		return transportSettings;
	}
	public void setTransportSettings(WrapperWriterDataSetTransport transportSettings) {
		this.transportSettings = transportSettings;
	}
	public String getDataSetName() {
		return dataSetName;
	}
	public void setDataSetName(String dataSetName) {
		this.dataSetName = dataSetName;
	}
	public WrapperKeyValuePair[] getDataSetWriterProperties() {
		return dataSetWriterProperties;
	}
	public void setDataSetWriterProperties(WrapperKeyValuePair[] dataSetWriterProperties) {
		this.dataSetWriterProperties = dataSetWriterProperties;
	}
	public Boolean getConfigurationFrozen() {
		return configurationFrozen;
	}
	public void setConfigurationFrozen(Boolean configurationFrozen) {
		this.configurationFrozen = configurationFrozen;
	}
	public void setDataSetWriterPropertiesSize(int propertiesSize) {
		this.dataSetWriterPropertiesSize = propertiesSize;
	}
}
