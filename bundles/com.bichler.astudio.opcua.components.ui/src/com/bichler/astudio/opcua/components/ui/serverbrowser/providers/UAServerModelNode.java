package com.bichler.astudio.opcua.components.ui.serverbrowser.providers;

import org.eclipse.jface.viewers.TreePath;
import org.eclipse.ui.part.EditorPart;
import org.opcfoundation.ua.common.ServiceResultException;

import com.bichler.astudio.opcua.components.ui.serverbrowser.OPCUAServerModelNode;
import com.bichler.opc.comdrv.OPCUADevice;
import opc.client.service.ClientSession;

public class UAServerModelNode extends OPCUAServerModelNode {

	private Object[] expandedElements = null;
	private TreePath[] expandedTreePaths = null;
	private EditorPart nodeEditor = null;

	private OPCUADevice device = null;
	private ClientSession session = null;

	public UAServerModelNode() {
	}

	UAServerModelNode(EditorPart editor) {
		this.nodeEditor = editor;
	}

	public EditorPart getEditor() {
		return this.nodeEditor;
	}

	public void setReopenedEditor(EditorPart reopenedEditor) {
		this.nodeEditor = reopenedEditor;
	}

	public Object[] getExpandedElements() {
		return expandedElements;
	}

	public void setExpandedElements(Object[] expandedElements) {
		this.expandedElements = expandedElements;
	}

	public TreePath[] getExpandedTreePaths() {
		return expandedTreePaths;
	}

	public void setExpandedTreePaths(TreePath[] expandedTreePaths) {
		this.expandedTreePaths = expandedTreePaths;
	}

	public OPCUADevice getDevice() {
		return device;
	}

	public void setDevice(OPCUADevice device) {
		this.device = device;
	}

	public void dispose() {
		if (this.device != null) {
			if (this.device.getUaclient() != null) {
				try {
					this.device.getUaclient().closeSession(null, true);
				} catch (ServiceResultException e) {
					e.printStackTrace();
				}
			}
			this.device.disconnect();
		}
	}

	public void setUaSession(ClientSession session) {
		this.session = session;
	}

	public ClientSession getUaSession() {
		return this.session;
	}
}