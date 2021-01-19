package com.bichler.astudio.opcua.opcmodeler.editor.node;

import opc.sdk.core.node.Node;

import org.eclipse.swt.custom.ScrolledComposite;

public class NodeEditorUnspecifiedContentProvider extends NodeEditorContentProvider {
	public NodeEditorUnspecifiedContentProvider(NodeEditorPart nodeEditorPart) {
		super(nodeEditorPart);
	}

	@Override
	protected void createContent(Node node, ScrolledComposite parent) {
		// System.out.println("Unspecified");
	}

	@Override
	protected void setNodeInput(Node node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void doSave(Node node) {
		// TODO Auto-generated method stub
	}
}
