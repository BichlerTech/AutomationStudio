package com.bichler.astudio.core.user.wizard;

import org.eclipse.jface.wizard.Wizard;

import com.bichler.astudio.core.user.UserActivator;
import com.bichler.astudio.core.user.wizard.page.LoginUserConfirmPage;

public class LoginUserConfirmWizard extends Wizard {

	private LoginUserConfirmPage pageLoginUserConfirm;

	public LoginUserConfirmWizard() {
		setWindowTitle("Confirm Login");
	}

	@Override
	public void addPages() {
		this.pageLoginUserConfirm = new LoginUserConfirmPage();
		addPage(this.pageLoginUserConfirm);
	}

	@Override
	public boolean performFinish() {
		String password = this.pageLoginUserConfirm.getPassword();
		if(!UserActivator.PASSWORD.equals(password)) {
			this.pageLoginUserConfirm.setErrorMessage("Password is not valid!");
			return false;
		}
		return true;
	}

}
