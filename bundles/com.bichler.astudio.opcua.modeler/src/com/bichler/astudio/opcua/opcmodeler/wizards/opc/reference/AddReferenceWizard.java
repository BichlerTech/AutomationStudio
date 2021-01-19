package com.bichler.astudio.opcua.opcmodeler.wizards.opc.reference;

import org.eclipse.jface.wizard.Wizard;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AddReferencesItem;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.reference.model.ReferenceModel;
import com.bichler.astudio.utils.internationalization.CustomString;

public class AddReferenceWizard extends Wizard {
	private AddReferenceTypeWizardPage pageOne;
	private AddReferenceTargetWizardPage pageTwo;
	private NodeId sourceNodeId;
	private ExpandedNodeId targetId;

	public AddReferenceWizard() {
		setWindowTitle(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.addref"));
	}

	@Override
	public void addPages() {
		this.pageOne = new AddReferenceTypeWizardPage();
		addPage(this.pageOne);
		this.pageTwo = new AddReferenceTargetWizardPage();
		addPage(this.pageTwo);
	}

	@Override
	public boolean performFinish() {
		ReferenceModel type = this.pageOne.getType();
		ReferenceModel target = this.pageTwo.getTarget();
		setTargetId(target.getId());
		boolean isInverse = this.pageOne.getIsInverse();
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		AddReferencesItem referenceToAdd = new AddReferencesItem();
		referenceToAdd.setIsForward(!isInverse);
		referenceToAdd.setReferenceTypeId(type.getId(nsTable));
		referenceToAdd.setSourceNodeId(this.sourceNodeId);
		referenceToAdd.setTargetNodeId(target.getId());
		referenceToAdd.setTargetNodeClass(target.getNodeClass());
		try {
			StatusCode[] result = ServerInstance.addReferences(new AddReferencesItem[] { referenceToAdd });
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
		return true;
	}

	private void setTargetId(ExpandedNodeId id) {
		this.targetId = id;
	}

	public void setSourceId(NodeId sourceNodeId) {
		this.sourceNodeId = sourceNodeId;
	}

	public ExpandedNodeId getTargetId() {
		return this.targetId;
	}
}
