package com.bichler.astudio.editor.pubsub.nodes;

import com.bichler.astudio.editor.pubsub.xml.PUBSUBENTRYTYPE;

public class PubSubEntryModelNode // implements IDriverNode
{
	//protected PubSubEntryModelNode parent = null;
	protected PUBSUBENTRYTYPE type = PUBSUBENTRYTYPE.CONNECTION;

	PubSubEntryModelNode() {

	}

	void setType(PUBSUBENTRYTYPE type) {
		this.type = type;
	}

	public PUBSUBENTRYTYPE getType() {
		return this.type;
	}

}
