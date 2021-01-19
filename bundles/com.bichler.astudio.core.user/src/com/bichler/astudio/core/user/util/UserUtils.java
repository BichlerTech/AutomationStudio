package com.bichler.astudio.core.user.util;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;

import com.bichler.astudio.core.user.UserActivator;
import com.bichler.astudio.core.user.type.AbstractStudioUser;
import com.bichler.astudio.core.user.wizard.LoginUserConfirmWizard;

public class UserUtils {

	public static boolean testUserRights(int field) {
		AbstractStudioUser user = UserActivator.getDefault().getUser();
		return user.hasRights(field);
	}

	public static boolean askPreferenceDialog(int field) {
		boolean allow = testUserRights(field);
		if (!allow) {
			LoginUserConfirmWizard wizard = new LoginUserConfirmWizard();
			WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					wizard);
			
			int open = dialog.open();
			return WizardDialog.OK == open;
		}

		return true;
	}
}
