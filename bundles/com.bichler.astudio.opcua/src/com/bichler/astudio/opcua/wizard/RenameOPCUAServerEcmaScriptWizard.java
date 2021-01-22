
package com.bichler.astudio.opcua.wizard;

import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.bichler.astudio.opcua.wizard.page.RenameOPCUAServerEcmaScriptWizardPage1;

public class RenameOPCUAServerEcmaScriptWizard extends Wizard implements INewWizard {

	public static final String copyright = "(c) Bichler Technologies GmbH 2018.";

	// the workbench instance
	protected IWorkbench workbench;

	private RenameOPCUAServerEcmaScriptWizardPage1 newPage1 = null;

	// workbench selection when the wizard was started
	protected IStructuredSelection selection;

	private String scriptName = "";

	@Override
	public void addPages() {
		newPage1 = new RenameOPCUAServerEcmaScriptWizardPage1(workbench, selection);
		newPage1.setScriptName(scriptName);
		addPage(newPage1);
	}

	public RenameOPCUAServerEcmaScriptWizard() {
		// TODO Auto-generated constructor stub
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

		this.setScriptName(this.newPage1.getScriptName());
		return true;
	}

	public String getScriptName() {
		return scriptName;
	}

	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}
}