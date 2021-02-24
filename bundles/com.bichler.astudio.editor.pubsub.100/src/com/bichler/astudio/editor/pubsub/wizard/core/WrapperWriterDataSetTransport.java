package com.bichler.astudio.editor.pubsub.wizard.core;

import org.opcfoundation.ua.core.BrokerTransportQualityOfService;

public class WrapperWriterDataSetTransport implements IWrapper {

	private TransportSettingType type;
	
	private String QueueName;
	private String ResourceUri;
	private String AuthenticationProfileUri;
	private BrokerTransportQualityOfService RequestedDeliveryGuarantee;
	private String MetaDataQueueName;
	private Double MetaDataUpdateTime;

	public WrapperWriterDataSetTransport() {

	}

	public String getQueueName() {
		return QueueName;
	}

	public String getResourceUri() {
		return ResourceUri;
	}

	public String getAuthenticationProfileUri() {
		return AuthenticationProfileUri;
	}

	public BrokerTransportQualityOfService getRequestedDeliveryGuarantee() {
		return RequestedDeliveryGuarantee;
	}

	public String getMetaDataQueueName() {
		return MetaDataQueueName;
	}

	public Double getMetaDataUpdateTime() {
		return MetaDataUpdateTime;
	}

	public void setQueueName(String queueName) {
		QueueName = queueName;
	}

	public void setResourceUri(String resourceUri) {
		ResourceUri = resourceUri;
	}

	public void setAuthenticationProfileUri(String authenticationProfileUri) {
		AuthenticationProfileUri = authenticationProfileUri;
	}

	public void setRequestedDeliveryGuarantee(BrokerTransportQualityOfService requestedDeliveryGuarantee) {
		RequestedDeliveryGuarantee = requestedDeliveryGuarantee;
	}

	public void setMetaDataQueueName(String metaDataQueueName) {
		MetaDataQueueName = metaDataQueueName;
	}

	public void setMetaDataUpdateTime(Double metaDataUpdateTime) {
		MetaDataUpdateTime = metaDataUpdateTime;
	}

	@Override
	public WrapperWriterDataSetTransport clone() {
		WrapperWriterDataSetTransport clone = new WrapperWriterDataSetTransport();
		clone.QueueName = this.QueueName;
		clone.ResourceUri = this.ResourceUri;
		clone.AuthenticationProfileUri = this.AuthenticationProfileUri;
		clone.RequestedDeliveryGuarantee = this.RequestedDeliveryGuarantee;
		clone.MetaDataQueueName = this.MetaDataQueueName;
		clone.MetaDataUpdateTime = this.MetaDataUpdateTime;
		return clone;
	}

	@Override
	public void reset() {
		this.QueueName = null;
		this.ResourceUri = null;
		this.AuthenticationProfileUri = null;
		this.RequestedDeliveryGuarantee = null;
		this.MetaDataQueueName = null;
		this.MetaDataUpdateTime = null;
	}
}
