package com.bichler.astudio.opcua.components.ui;

import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;

public class BrowsePathElement {
	private NodeId id = null;
	private LocalizedText displayname = null;
	private QualifiedName browsename = null;
	public NodeId getId() {
		return id;
	}
	public void setId(NodeId id) {
		this.id = id;
	}
	public LocalizedText getDisplayname() {
		return displayname;
	}
	public void setDisplayname(LocalizedText displayname) {
		this.displayname = displayname;
	}
	public QualifiedName getBrowsename() {
		return browsename;
	}
	public void setBrowsename(QualifiedName qualifiedName) {
		this.browsename = qualifiedName;
	}
}
