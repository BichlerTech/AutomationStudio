package com.bichler.astudio.opcua.wizard;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.opcua.wizard.page.NewOPCUADriverStatusPage;
import com.bichler.astudio.opcua.wizard.page.NewOPCUAServerModuleWizardPage;
import com.bichler.astudio.opcua.wizard.util.OPCWizardUtil;

public class NewOPCUAServerModuleWizard extends Wizard implements INewWizard {
	public static final String copyright = "(c) Bichler Technologies GmbH 2020.";
	// the workbench instance
	protected IWorkbench workbench;
	private NewOPCUAServerModuleWizardPage modulePage = null;
	// workbench selection when the wizard was started
	protected IStructuredSelection selection;
	private String modName = "";
	private String modType = "";
	private NewOPCUADriverStatusPage moduleStatusPage;
	private String modVersion;

	@Override
	public void addPages() {
		modulePage = new NewOPCUAServerModuleWizardPage(workbench, selection);
		addPage(modulePage);
		this.moduleStatusPage = new NewOPCUADriverStatusPage();
		addPage(this.moduleStatusPage);
	}

	public NewOPCUAServerModuleWizard() {
		this.setWindowTitle("Automation Studio");
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
	}

	@Override
	public boolean performFinish() {
		this.modName = modulePage.getDrvName();
		this.modType = modulePage.getDrvType();
		this.modVersion = modulePage.getDrvVersion();
		OPCWizardUtil.newOPCUAModule(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), getDrvName(),
				getModType(), getModVersion());
		if (this.moduleStatusPage.isStatus()) {
			NodeId startId = this.moduleStatusPage.getRoot();
			OPCWizardUtil.newOPCUAModuleModel(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(),
					startId, this.modName, 1);
		}
		return true;
	}

	public String getDrvName() {
		return modName;
	}

	public void setModName(String modName) {
		this.modName = modName;
	}

	public String getModType() {
		return modType;
	}

	public String getModVersion() {
		return modVersion;
	}

	public void setModType(String modType) {
		this.modType = modType;
	}
}
