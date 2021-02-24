package com.bichler.astudio.editor.pubsub.wizard.core;

public class WrapperConnectionAddress implements IWrapper{
	
	private String networkInterface = null;
	private String url = null;
	
	public WrapperConnectionAddress() {
		
	}
	
	public String getNetworkInterface() {
		return networkInterface;
	}
	public void setNetworkInterface(String networkInterface) {
		this.networkInterface = networkInterface;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public WrapperConnectionAddress clone() {
		WrapperConnectionAddress clone = new WrapperConnectionAddress();
		clone.networkInterface = this.networkInterface;
		clone.url = this.url;
		return clone;
	}

	@Override
	public void reset() {
		this.networkInterface = null;
		this.url = null;
	}
	
	
}
