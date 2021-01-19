package com.bichler.astudio.opcua.opcmodeler.wizards.create;

import opc.sdk.core.node.Node;

import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.MethodAttributes;
import org.opcfoundation.ua.core.NodeAttributes;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.wizards.AbstractCreateWizard;
import com.bichler.astudio.opcua.opcmodeler.wizards.create.page.DetailObjectTypePage;
import com.bichler.astudio.opcua.opcmodeler.wizards.create.page.DetailVariablePage;
import com.bichler.astudio.opcua.opcmodeler.wizards.create.page.DetailVariableTypePage;
import com.bichler.astudio.utils.internationalization.CustomString;

public class CreateVariableTypeWizard extends AbstractCreateWizard {
	private DetailVariableTypePage detailPage;

	public CreateVariableTypeWizard(Node selectedParent) {
		super(selectedParent);
		setWindowTitle(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "opc.message.extendvariabletype"));
	}

	@Override
	public void addPages() {
		super.addPages();
		this.detailPage = new DetailVariableTypePage();
		addPage(this.detailPage);
	}

	@Override
	public boolean performFinish() {
		QualifiedName browsename = getGeneralPage().getBrowseName();
		LocalizedText description = getGeneralPage().getDescriptionText();
		LocalizedText displayname = getGeneralPage().getDisplayname();
		NodeId nodeid = getGeneralPage().getNodeId();
		UnsignedInteger userwritemask = getGeneralPage().getUserWriteMask();
		UnsignedInteger writemask = getGeneralPage().getWriteMask();
		boolean executeable = this.detailPage.getExecuteable();
		boolean userexecuteable = this.detailPage.getUserExecuteable();
		NodeAttributes attributes = new MethodAttributes(null, displayname, description, writemask, userwritemask,
				executeable, userexecuteable);
		return true;
	}

	@Override
	protected String getTitle() {
		return "";
	}

	@Override
	protected String getDescription() {
		return "";
	}
}
