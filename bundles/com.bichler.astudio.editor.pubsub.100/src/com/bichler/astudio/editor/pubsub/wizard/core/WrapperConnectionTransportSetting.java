package com.bichler.astudio.editor.pubsub.wizard.core;

import org.opcfoundation.ua.builtintypes.ExtensionObject;

public class WrapperConnectionTransportSetting implements IWrapper{

	private TransportSettingType type = null;
	private String ResourceUri = null;
	private String AuthenticationProfileUri = null;
	private ExtensionObject DiscoveryAddress = null;
	
	public WrapperConnectionTransportSetting() {
		
	}
	
	public TransportSettingType getType() {
		return this.type;
	}
	
	public void setType(TransportSettingType type) {
		this.type = type;
	}

	public String getResourceUri() {
		return ResourceUri;
	}

	public void setResourceUri(String resourceUri) {
		ResourceUri = resourceUri;
	}

	public String getAuthenticationProfileUri() {
		return AuthenticationProfileUri;
	}

	public void setAuthenticationProfileUri(String authenticationProfileUri) {
		AuthenticationProfileUri = authenticationProfileUri;
	}

	public ExtensionObject getDiscoveryAddress() {
		return DiscoveryAddress;
	}

	public void setDiscoveryAddress(ExtensionObject discoveryAddress) {
		DiscoveryAddress = discoveryAddress;
	}

	@Override
	public WrapperConnectionTransportSetting clone() {
		WrapperConnectionTransportSetting clone = new WrapperConnectionTransportSetting();
		clone.type = this.type;
		clone.AuthenticationProfileUri = this.AuthenticationProfileUri;
		clone.ResourceUri = this.ResourceUri;
		
		clone.DiscoveryAddress = this.DiscoveryAddress;
		
		return clone;
	}

	@Override
	public void reset() {
		this.type = null;
		this.ResourceUri = null;
		this.AuthenticationProfileUri = null;
		this.DiscoveryAddress = null;
	}
	
	
}
