package com.hbsoft.studio.editor.events.xml;

import org.opcfoundation.ua.builtintypes.NodeId;

public abstract class AbstractEventModel {

	private NodeId sourceId = NodeId.NULL;
	private String displayname = "";
	protected String browsepath = "";

	public AbstractEventModel() {

	}

	public NodeId getSourceId() {
		return sourceId;
	}

	public String getDisplayname() {
		return displayname;
	}

	public void setBrowsepath(String browsepath) {
		this.browsepath = browsepath;
	}

	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}

	public void setSourceId(NodeId sourceId) {
		this.sourceId = sourceId;
	}
}
