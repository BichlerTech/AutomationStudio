package com.bichler.astudio.editor.pubsub.nodes;

import com.bichler.astudio.editor.pubsub.xml.PUBSUBENTRYTYPE;

public class PubSubReaderGroup extends PubSubEntryModelNode {
	private String name;
    private PubSubSecurityParameters securityParameters;
    
    public PubSubReaderGroup() {
		super();
    	this.type = PUBSUBENTRYTYPE.READERGROUP;
    }
    
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public PubSubSecurityParameters getSecurityParameters() {
		return this.securityParameters;
	}

	public void setSecurityParameters(PubSubSecurityParameters securityParamters) {
		this.securityParameters = securityParamters;;
	}
}
