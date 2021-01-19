package com.bichler.astudio.editor.pubsub.wizard.core;

import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.core.BrokerTransportQualityOfService;

public class WrapperWriterGroupTransport implements IWrapper {

	private TransportSettingType type = null;

	private String QueueName;
	private String ResourceUri;
	private String AuthenticationProfileUri;
	private BrokerTransportQualityOfService RequestedDeliveryGuarantee;

	private UnsignedByte MessageRepeatCount;
	private Double MessageRepeatDelay;

	public TransportSettingType getType() {
		return this.type;
	}

	public String getQueueName() {
		return this.QueueName;
	}

	public String getResourceUri() {
		return this.ResourceUri;
	}

	public String getAuthenticationProfileUri() {
		return this.AuthenticationProfileUri;
	}

	public BrokerTransportQualityOfService getRequestedDeliveryGuarantee() {
		return this.RequestedDeliveryGuarantee;
	}

	public UnsignedByte getMessageRepeatCount() {
		return this.MessageRepeatCount;
	}

	public Double getMessageRepeatDelay() {
		return this.MessageRepeatDelay;
	}
	
	public void setType(TransportSettingType type) {
		this.type = type;
	}

	public void setQueueName(String queueName) {
		this.QueueName = queueName;
	}

	public void setResourceUri(String resourceUri) {
		this.ResourceUri = resourceUri;
	}

	public void setAuthenticationProfileUri(String authenticationProfileUri) {
		this.AuthenticationProfileUri = authenticationProfileUri;
	}

	public void setRequestedDeliveryGuarantee(BrokerTransportQualityOfService requestedDeliveryGuarantee) {
		this.RequestedDeliveryGuarantee = requestedDeliveryGuarantee;
	}

	public void setMessageRepeatCount(UnsignedByte messageRepeatCount) {
		this.MessageRepeatCount = messageRepeatCount;
	}

	public void setMessageRepeatDelay(Double messageRepeatDelay) {
		this.MessageRepeatDelay = messageRepeatDelay;
	}

	@Override
	public WrapperWriterGroupTransport clone() {
		WrapperWriterGroupTransport obj = new WrapperWriterGroupTransport();
		obj.AuthenticationProfileUri = this.AuthenticationProfileUri;
		obj.QueueName = this.AuthenticationProfileUri;
		obj.RequestedDeliveryGuarantee = this.RequestedDeliveryGuarantee;
		obj.ResourceUri = this.ResourceUri;

		obj.MessageRepeatCount = this.MessageRepeatCount;
		obj.MessageRepeatDelay = this.MessageRepeatDelay;

		return obj;
	}

}
