package com.bichler.astudio.opcua.opcmodeler.wizards.opc.reference;

import java.util.List;

import opc.sdk.core.node.Node;

import org.eclipse.jface.dialogs.MessageDialog;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.ReferenceNode;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.utils.ReferenceRule;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.reference.model.ReferenceModel;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.reference.model.ReferenceNodeValidation;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.reference.model.ReferenceUtil;
import com.bichler.astudio.utils.internationalization.CustomString;

public class EditReferenceWizard extends AbstractReferenceWizard {
	private EditReferenceWizardPage pageOne;
	private EditReferenceTypeWizardPage pageTwo;

	public EditReferenceWizard() {
		setWindowTitle(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "AddReferenceDialog.changeref.caption"));
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
		this.pageOne = new EditReferenceWizardPage();
		this.pageOne.setSourceNode(this.source);
		this.pageOne.setSourceReference(this.refNode2Edit);
		this.pageOne.setTargetReference(targetRefNode);
		addPage(this.pageOne);
		this.pageTwo = new EditReferenceTypeWizardPage();
		this.pageTwo.setReferenceType(this.refNode2Edit.getReferenceTypeId());
		this.pageTwo.setModel(model);
		this.pageTwo.setSourceRules(sourceRules);
		this.pageTwo.setTargetRules(targetRules);
		addPage(this.pageTwo);
	}

	@Override
	public boolean performFinish() {
		ReferenceNode source = this.pageOne.getSourceReferenceNode();
		ReferenceNode target = this.pageOne.getTargetReferenceNode();
		ReferenceModel newReferenceType = this.pageTwo.getNewReferenceType();
		// nothing changed
		if (newReferenceType == null) {
			MessageDialog.openInformation(getShell(),
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
							"CreateVariableDialog.lbl_references.text"),
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.editref.noref"));
			return true;
		}
		// change everything
		NodeId newRefId = newReferenceType.getId(ServerInstance.getInstance().getServerInstance().getNamespaceUris());
		if (source != null) {
			source.setReferenceTypeId(newRefId);
		}
		if (target != null) {
			target.setReferenceTypeId(newRefId);
		}
		return true;
	}
}
