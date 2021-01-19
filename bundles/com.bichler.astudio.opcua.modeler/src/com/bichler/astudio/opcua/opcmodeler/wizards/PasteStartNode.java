package com.bichler.astudio.opcua.opcmodeler.wizards;

import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;

public class PasteStartNode {
	private boolean init = false;
	private NodeId nodeId;
	private QualifiedName browsename;
	private LocalizedText description;
	private LocalizedText displayname;
	private boolean useCopyId = false;
	private boolean copyIds;

	public PasteStartNode(QualifiedName browsename, LocalizedText description, LocalizedText displayname,
			NodeId nodeId) {
		this.nodeId = nodeId;
		this.browsename = browsename;
		this.description = description;
		this.displayname = displayname;
	}

	public NodeId getNodeId() {
		return nodeId;
	}

	public QualifiedName getBrowsename() {
		return browsename;
	}

	public LocalizedText getDescription() {
		return description;
	}

	public LocalizedText getDisplayname() {
		return displayname;
	}

	public boolean isInit() {
		return this.init;
	}

	public void setInit() {
		this.init = true;
	}

	public void setCopyIds(boolean copyIds) {
		this.copyIds = copyIds;
	}

	public boolean isCopyIds() {
		return this.copyIds;
	}
}
