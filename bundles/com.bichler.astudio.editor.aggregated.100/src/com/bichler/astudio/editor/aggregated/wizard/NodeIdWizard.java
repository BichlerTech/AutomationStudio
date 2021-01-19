package com.bichler.astudio.editor.aggregated.wizard;

import org.eclipse.jface.wizard.Wizard;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.editor.aggregated.AggregatedActivator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class NodeIdWizard extends Wizard {

	private NodeIdWizardPage pageNodeId = null;
	private NodeId initId;
	private NodeId newId;

	public NodeIdWizard(NodeId initId) {
		setWindowTitle( CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.aggregated.propertyview.wizard.nodeid"));
		this.initId = initId;
	}

	@Override
	public void addPages() {
		this.pageNodeId = new NodeIdWizardPage(this.initId);
		addPage(this.pageNodeId );
	}

	@Override
	public boolean performFinish() {
		this.newId = this.pageNodeId.getNewNodeId();
		return true;
	}

	public NodeId getNewNodeId() {
		return this.newId;
	}

}
