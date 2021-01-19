package com.bichler.astudio.opcua.opcmodeler.editor.node;

import opc.sdk.core.node.Node;

import org.eclipse.swt.custom.ScrolledComposite;

public class NodeEditorViewContentProvider extends NodeEditorContentProvider {
	public NodeEditorViewContentProvider(NodeEditorPart nodeEditorPart) {
		super(nodeEditorPart);
	}

	@Override
	protected void createContent(Node node, ScrolledComposite parent) {
	}

	@Override
	protected void setNodeInput(Node node) {
		setBaseNodeInput(node);
		setReferenceTableNodeInput(node);
	}

	@Override
	public void doSave(Node node) {
		// save the default node attributes
		doSaveNodeBaseAttributes(node);
	}
}
