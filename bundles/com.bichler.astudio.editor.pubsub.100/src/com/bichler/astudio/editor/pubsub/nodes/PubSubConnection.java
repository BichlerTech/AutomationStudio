package com.bichler.astudio.editor.pubsub.nodes;

import java.util.ArrayList;
import java.util.List;

import com.bichler.astudio.editor.pubsub.wizard.core.WrapperConnectionAddress;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperConnectionTransportSetting;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperKeyValuePair;
import com.bichler.astudio.editor.pubsub.xml.PUBSUBENTRYTYPE;

public class PubSubConnection extends PubSubEntryModelNode {

	private String name;
	private Boolean enabled;
	private PubSubPublisherIdType publisherIdType;
	private Object publisherId;
	//    union { /* std: valid types UInt or String */
	//        UA_UInt32 numeric;
	//        UA_String string;
	//    } publisherId;
	private String transportProfileUri = "testuri";

	private WrapperConnectionAddress address = null;
	// private Variant address;

	// private int connectionPropertiesSize;
	private WrapperKeyValuePair[] connectionProperties;

	private WrapperConnectionTransportSetting connectionTransportSettings;
	// private Variant connectionTransportSettings;

	private List<PubSubWriterGroup> children = null;

	/* This flag is 'read only' and is set internally based on the PubSub state. */
	private Boolean configurationFrozen;

	public PubSubConnection() {
		super();
		this.setChildren(new ArrayList<PubSubWriterGroup>());
//		this.connectionProperties = new ArrayList<>();
		this.type = PUBSUBENTRYTYPE.CONNECTION;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public PubSubPublisherIdType getPublisherIdType() {
		return publisherIdType;
	}

	public void setPublisherIdType(PubSubPublisherIdType publisherIdType) {
		this.publisherIdType = publisherIdType;
	}

	public String getTransportProfileUri() {
		return transportProfileUri;
	}

	public void setTransportProfileUri(String transportProfileUri) {
		this.transportProfileUri = transportProfileUri;
	}

//	public int getConnectionPropertiesSize() {
//		return connectionPropertiesSize;
//	}
//
//	public void setConnectionPropertiesSize(int connectionPropertiesSize) {
//		this.connectionPropertiesSize = connectionPropertiesSize;
//	}

	public Boolean getConfigurationFrozen() {
		return configurationFrozen;
	}

	public void setConfigurationFrozen(Boolean configurationFrozen) {
		this.configurationFrozen = configurationFrozen;
	}

	public WrapperConnectionAddress getAddress() {
		return address;
	}

	public void setAddress(WrapperConnectionAddress address) {
		this.address = address;
	}

	public WrapperConnectionTransportSetting getConnectionTransportSettings() {
		return connectionTransportSettings;
	}

	public void setConnectionTransportSettings(WrapperConnectionTransportSetting connectionTransportSettings) {
		this.connectionTransportSettings = connectionTransportSettings;
	}

	public Object getPublisherId() {
		return publisherId;
	}

	public void setPublisherId(Object publisherId) {
		this.publisherId = publisherId;
	}

	public WrapperKeyValuePair[] getConnectionProperties() {
		return this.connectionProperties;
	}

	public void setConnectionProperties(WrapperKeyValuePair[] connectionProperties) {
		this.connectionProperties = connectionProperties;
	}

	public List<PubSubWriterGroup> getChildren() {
		return children;
	}

	public void setChildren(List<PubSubWriterGroup> children) {
		this.children = children;
	}
}
