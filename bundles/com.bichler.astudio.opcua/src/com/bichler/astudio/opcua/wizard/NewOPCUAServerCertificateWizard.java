package com.bichler.astudio.opcua.wizard;

import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.bichler.astudio.opcua.wizard.page.NewOPCUAServerCertificateWizardPage1;

public class NewOPCUAServerCertificateWizard extends Wizard implements INewWizard {
	public static final String copyright = "(c) Bichler Technologies GmbH 2018.";
	// the workbench instance
	protected IWorkbench workbench;
	private NewOPCUAServerCertificateWizardPage1 newPage1 = null;
	// workbench selection when the wizard was started
	protected IStructuredSelection selection;

	@Override
	public void addPages() {
		newPage1 = new NewOPCUAServerCertificateWizardPage1(workbench, selection);
		addPage(newPage1);
	}

	public NewOPCUAServerCertificateWizard() {
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
		if (selection != null && !selection.isEmpty()) {
			Object obj = selection.getFirstElement();
			if (obj instanceof IFolder) {
				IFolder folder = (IFolder) obj;
				if (folder.getName().equals("Discounts")) {
				}
			}
		}
	}

	@Override
	public boolean performFinish() {
		return false;
	}
}
