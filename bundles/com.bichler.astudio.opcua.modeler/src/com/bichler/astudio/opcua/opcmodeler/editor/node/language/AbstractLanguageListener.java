package com.bichler.astudio.opcua.opcmodeler.editor.node.language;

import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Text;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.opcua.opcmodeler.editor.node.NodeEditorPart;

public abstract class AbstractLanguageListener implements SelectionListener {
	/** NodeId of the referenced opc ua node */
	private Text nodeId = null;
	/** default name */
	private Text name;
	/** editor used to call back if changes happens */
	private NodeEditorPart editor;

	public AbstractLanguageListener(Text nodeId, Text name, NodeEditorPart editor) {
		this.nodeId = nodeId;
		this.name = name;
		this.editor = editor;
	}

	NodeId getNodeId() {
		return (NodeId) this.nodeId.getData();
	}

	String getName() {
		return this.name.getText();
	}

	NodeEditorPart getEditor() {
		return this.editor;
	}
}
