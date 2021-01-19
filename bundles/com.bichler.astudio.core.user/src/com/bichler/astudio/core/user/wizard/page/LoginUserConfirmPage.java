package com.bichler.astudio.core.user.wizard.page;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;

public class LoginUserConfirmPage extends WizardPage {
	private Text txtPassword;

	public LoginUserConfirmPage() {
		super("confirmloginwizardpage");

		setTitle("Confirm Login");
		setDescription(
				"Enter a password");
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);

		setControl(container);
		container.setLayout(new GridLayout(2, false));

		Label lblPassword = new Label(container, SWT.NONE);
		lblPassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPassword.setText("Password");

		txtPassword = new Text(container, SWT.PASSWORD | SWT.BORDER);
		txtPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		setHandler();
	}

	@Override
	public boolean isPageComplete() {
		if(txtPassword.getText().isEmpty()) {
			return false;
		}
		return true;
	}
	
	public String getPassword() {
		return this.txtPassword.getText();
	}

	private void setHandler() {
		txtPassword.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});
	}
}
