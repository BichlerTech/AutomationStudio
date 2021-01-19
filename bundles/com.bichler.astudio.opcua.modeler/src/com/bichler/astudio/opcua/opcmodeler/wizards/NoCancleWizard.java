package com.bichler.astudio.opcua.opcmodeler.wizards;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class NoCancleWizard extends WizardDialog {
	public NoCancleWizard(Shell parentShell, IWizard newWizard) {
		super(parentShell, newWizard);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		Button cancle = getButton(IDialogConstants.CANCEL_ID);
		cancle.dispose();
		parent.layout();
	}
}
