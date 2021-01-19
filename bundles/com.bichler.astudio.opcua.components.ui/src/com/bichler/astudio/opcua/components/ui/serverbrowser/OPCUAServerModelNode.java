package com.bichler.astudio.opcua.components.ui.serverbrowser;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.part.EditorPart;

public class OPCUAServerModelNode {

	private EditorPart nodeEditor = null;
	//protected ClientSession session = null;
	boolean isConnected = false;
	TreeViewer tree = null;
	/** The display name. */
	private String displayName;
	boolean isReconnecting = false;
	private String serverUrl = null;
	private Integer serverIndex = null;
	
	public OPCUAServerModelNode() {
	}

	OPCUAServerModelNode(EditorPart editor) {
		this.nodeEditor = editor;
	}

	public EditorPart getEditor() {
		return this.nodeEditor;
	}

	public void setReopenedEditor(EditorPart reopenedEditor) {
		this.nodeEditor = reopenedEditor;
	}

	/**
	 * Gets the display name.
	 * 
	 * @return the display name
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Sets the display name.
	 * 
	 * @param dpName
	 *            the new display name
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public boolean isConnected() {
		return this.isConnected;
	}

	public void setConnected(boolean clientConnected) {
		this.isConnected = clientConnected;
	}

	public TreeViewer getTree() {
		return this.tree;
	}

	public void setTree(TreeViewer tree) {
		this.tree = tree;
	}

	public void setReconnection(boolean isReconnecting) {
		this.isReconnecting = isReconnecting;
	}

	public boolean isReconnecting() {
		return this.isReconnecting;
	}

	public void setServerUrl(String serverurl) {
		this.serverUrl = serverurl;
	}
	
	public String getServerUrl(){
		return this.serverUrl;
	}
	
	public void setServerIndex(int index) {
		this.serverIndex = index;
	}

	public Integer getServerIndex() {
		return this.serverIndex;
	}

	/*
	public ClientSession getSession() {
		return this.session;
	}*/
}