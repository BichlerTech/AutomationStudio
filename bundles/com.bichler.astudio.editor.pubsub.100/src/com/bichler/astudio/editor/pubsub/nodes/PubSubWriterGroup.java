package com.bichler.astudio.editor.pubsub.nodes;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.core.MessageSecurityMode;
import org.opcfoundation.ua.core.WriterGroupMessageDataType;
import org.opcfoundation.ua.core.WriterGroupTransportDataType;

import com.bichler.astudio.editor.pubsub.wizard.core.WrapperKeyValuePair;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperWriterGroupMessage;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperWriterGroupTransport;
import com.bichler.astudio.editor.pubsub.xml.PUBSUBENTRYTYPE;

public class PubSubWriterGroup extends PubSubEntryModelNode {

	private static final java.lang.String TYPE_FIELD_NAME = "type";

	private String name;
	private Boolean enabled;
	private int writerGroupId;

	private Double publishingInterval;
	private Double keepAliveTime;
	private Byte priority;
	private MessageSecurityMode securityMode;
	private WrapperWriterGroupTransport transportSettings;
	private WrapperWriterGroupMessage messageSettings;
	
	private WrapperKeyValuePair[] groupProperties;
	
	private PubSubEncodingType encodingMimeType;

	/*
	 * non std. confi g parameter. maximum count of embedded DataSetMessage in one
	 * NetworkMessage
	 */
	private int maxEncapsulatedDataSetMessageCount;
	/* This flag is 'read only' and is set internally based on the PubSub state. */
	private Boolean configurationFrozen;
	/* non std. field */
	private PubSubRTLevel rtLevel;
	private List<PubSubDataSetWriter> children = null;

	//private int groupPropertiesSize;

	public PubSubWriterGroup() {
		super();
		this.children = new ArrayList<PubSubDataSetWriter>();
		this.groupProperties = new WrapperKeyValuePair[0];
		this.type = PUBSUBENTRYTYPE.WRITERGROUP;
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

	public Double getPublishingInterval() {
		return publishingInterval;
	}

	public void setPublishingInterval(Double publishingInterval) {
		this.publishingInterval = publishingInterval;
	}

	public Double getKeepAliveTime() {
		return keepAliveTime;
	}

	public void setKeepAliveTime(Double keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
	}

	public Byte getPriority() {
		return priority;
	}

	public void setPriority(Byte priority) {
		this.priority = priority;
	}

	public MessageSecurityMode getSecurityMode() {
		return securityMode;
	}

	public void setSecurityMode(MessageSecurityMode securityMode) {
		this.securityMode = securityMode;
	}

	public WrapperWriterGroupTransport getTransportSettings() {
		return transportSettings;
	}

	public void setTransportSettings(WrapperWriterGroupTransport transportSettings) {
		this.transportSettings = transportSettings;
	}

	public WrapperWriterGroupMessage getMessageSettings() {
		return messageSettings;
	}

	public void setMessageSettings(WrapperWriterGroupMessage messageSettings) {
		this.messageSettings = messageSettings;
	}

	public WrapperKeyValuePair[] getGroupProperties() {
		return groupProperties;
	}

	public void setGroupProperties(WrapperKeyValuePair[] groupProperties) {
		this.groupProperties = groupProperties;
	}

	public PubSubEncodingType getEncodingMimeType() {
		return encodingMimeType;
	}

	public void setEncodingMimeType(PubSubEncodingType encodingMimeType) {
		this.encodingMimeType = encodingMimeType;
	}

	public int getMaxEncapsulatedDataSetMessageCount() {
		return maxEncapsulatedDataSetMessageCount;
	}

	public void setMaxEncapsulatedDataSetMessageCount(int maxEncapsulatedDataSetMessageCount) {
		this.maxEncapsulatedDataSetMessageCount = maxEncapsulatedDataSetMessageCount;
	}

	public Boolean getConfigurationFrozen() {
		return configurationFrozen;
	}

	public void setConfigurationFrozen(Boolean configurationFrozen) {
		this.configurationFrozen = configurationFrozen;
	}

	public PubSubRTLevel getRtLevel() {
		return rtLevel;
	}

	public void setRtLevel(PubSubRTLevel rtLevel) {
		this.rtLevel = rtLevel;
	}

	public int getWriterGroupId() {
		return writerGroupId;
	}

	public void setWriterGroupId(int writerGroupId) {
		this.writerGroupId = writerGroupId;
	}

	public void addChild(PubSubDataSetWriter child) {
//	    child.setParent(this);
		this.children.add(child);
	}

	public List<PubSubDataSetWriter> getChildren() {
		return this.children;
	}
}
