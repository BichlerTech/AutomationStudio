package com.bichler.astudio.opcua.nosql.userauthority.wizard;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import com.bichler.astudio.opcua.nosql.userauthority.wizard.page.AbstractOPCUAUserRoleAuthorityPage;

public class UserRoleWizardDialog extends WizardDialog {

	public UserRoleWizardDialog(Shell parentShell, IWizard newWizard) {
		super(parentShell, newWizard);
	}

	@Override
	public void showPage(IWizardPage page) {
		saveExpansionState(getCurrentPage());
		super.showPage(page);
	}

	protected void saveExpansionState(IWizardPage page) {
		if (page instanceof AbstractOPCUAUserRoleAuthorityPage) {
			((AbstractOPCUAUserRoleAuthorityPage) page).saveExpansionState();
		}
	}

	@Override
	protected void update() {
		super.update();

		IWizardPage currentpage = getCurrentPage();
		if (currentpage instanceof AbstractOPCUAUserRoleAuthorityPage) {
			((AbstractOPCUAUserRoleAuthorityPage) currentpage).updateExpansionState();
		}
	}

}
