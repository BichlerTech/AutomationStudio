package com.bichler.astudio.opcua.opcmodeler.wizards.opc.nodeid;

import opc.sdk.core.node.Node;

import org.eclipse.jface.wizard.Wizard;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class NodeIdWizard extends Wizard {
	private NodeIdTypeWizardPage pageOne;
	// private NodeIdValueWizardPage pageTwo;
	// private IdType nodeIdTyp;
	private NodeId initId;
	private Node node;
	private NodeId newNodeId;

	public NodeIdWizard(Node node, NodeId nodeId) {
		setWindowTitle(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "CreateVariableDialog.lbl_nodeId.text"));
		this.node = node;
		this.initId = nodeId;
	}

	@Override
	public void addPages() {
		this.pageOne = new NodeIdTypeWizardPage(this.node, this.initId);
		addPage(this.pageOne);
	}

	@Override
	public boolean performFinish() {
		this.newNodeId = this.pageOne.getNewNodeId();
		return true;
	}

	public NodeId getNewNodeId() {
		return this.newNodeId;
	}

	public NodeId getInitId() {
		return this.initId;
	}
}
