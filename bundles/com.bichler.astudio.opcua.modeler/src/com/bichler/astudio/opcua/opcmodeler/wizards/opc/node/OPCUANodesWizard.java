package com.bichler.astudio.opcua.opcmodeler.wizards.opc.node;

import java.util.List;

import opc.sdk.core.node.Node;

import org.eclipse.jface.wizard.Wizard;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class OPCUANodesWizard extends Wizard {
	private OPCUANodesWizardPage pageNodes;
	private List<Node[]> nodes;

	public OPCUANodesWizard(List<Node[]> nodes) {
		setWindowTitle(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.opcnodes"));
		this.nodes = nodes;
	}

	@Override
	public void addPages() {
		this.pageNodes = new OPCUANodesWizardPage();
		this.pageNodes.setNodes(this.nodes);
		addPage(this.pageNodes);
	}

	@Override
	public boolean performFinish() {
		return true;
	}
}
