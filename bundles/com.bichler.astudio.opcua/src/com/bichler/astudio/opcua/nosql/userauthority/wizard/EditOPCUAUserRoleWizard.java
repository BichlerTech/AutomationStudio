package com.bichler.astudio.opcua.nosql.userauthority.wizard;

import org.eclipse.jface.wizard.Wizard;

import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class EditOPCUAUserRoleWizard extends Wizard {

	public EditOPCUAUserRoleWizard() {
		setWindowTitle(
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.edituserrole.title"));
	}

	@Override
	public void addPages() {

	}

	@Override
	public boolean performFinish() {
		return false;
	}

}
