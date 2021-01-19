package com.bichler.astudio.opcua.opcmodeler.wizards.opc.reference;

import java.util.List;

import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.DeleteReferencesItem;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.ReferenceNode;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.utils.ReferenceRule;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.reference.model.ReferenceModel;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.reference.model.ReferenceNodeValidation;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.reference.model.ReferenceUtil;
import com.bichler.astudio.utils.internationalization.CustomString;

import opc.sdk.core.node.Node;

public class DeleteReferenceWizard extends AbstractReferenceWizard {
	private DeleteReferenceWizardPage pageOne;

	public DeleteReferenceWizard() {
		setWindowTitle(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.deleteref"));
	}

	@Override
	public void addPages() {
		ReferenceModel model = ReferenceUtil.initializeReferenceTypeTree(Identifiers.References, Identifiers.HasSubtype,
				false);
		ReferenceNode targetRefNode = findTargetReference();
		Node targetNode = ServerInstance.getNode(this.refNode2Edit.getTargetId());
		List<ReferenceRule> sourceRules = ReferenceNodeValidation.validate(model, this.source.getNode(),
				(ReferenceNode) this.refNode2Edit);
		List<ReferenceRule> targetRules = ReferenceNodeValidation.validate(model, targetNode, targetRefNode);
		this.pageOne = new DeleteReferenceWizardPage();
		this.pageOne.setSourceNode(this.source);
		this.pageOne.setSourceReference(this.refNode2Edit);
		this.pageOne.setTargetReference(targetRefNode);
		this.pageOne.setSourceRules(sourceRules);
		this.pageOne.setTargetRules(targetRules);
		addPage(this.pageOne);
	}

	@Override
	public boolean performFinish() {
		ReferenceNode source = this.pageOne.getSourceReferenceNode();
		ReferenceNode target = this.pageOne.getTargetReferenceNode();
		DeleteReferencesItem[] delete = new DeleteReferencesItem[1];
		DeleteReferencesItem references2delete = new DeleteReferencesItem();
		if (target != null) {
			references2delete.setDeleteBidirectional(true);
		} else {
			references2delete.setDeleteBidirectional(false);
		}
		references2delete.setIsForward(!source.getIsInverse());
		references2delete.setReferenceTypeId(source.getReferenceTypeId());
		references2delete.setSourceNodeId(this.source.getNode().getNodeId());
		references2delete.setTargetNodeId(source.getTargetId());
		delete[0] = references2delete;
		try {
			ServerInstance.deleteReference(delete);
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
		return true;
	}
}
