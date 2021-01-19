
package com.bichler.astudio.opcua.wizard;

import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.bichler.astudio.opcua.wizard.page.NewOPCUAServerShellScriptWizardPage1;

public class NewOPCUAServerShellScriptWizard extends Wizard implements INewWizard {

	public static final String copyright = "(c) Bichler Technologies GmbH 2018.";

	// the workbench instance
	protected IWorkbench workbench;

	private NewOPCUAServerShellScriptWizardPage1 pageOne = null;

	// workbench selection when the wizard was started
	protected IStructuredSelection selection;

	private String scriptname = "";
	private int scripttype = 0;

	@Override
	public void addPages() {
		pageOne = new NewOPCUAServerShellScriptWizardPage1(workbench, selection);
		addPage(pageOne);
	}

	public NewOPCUAServerShellScriptWizard() {

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
				// model.discounted = true;
			}
		}
	}

	@Override
	public boolean performFinish() {
		this.scriptname = this.pageOne.getScriptName();
		this.scripttype = this.pageOne.getScriptType();

		return true;
	}

	public String getScriptname() {
		return this.scriptname;
	}

	public int getScripttype() {
		return this.scripttype;
	}

}
