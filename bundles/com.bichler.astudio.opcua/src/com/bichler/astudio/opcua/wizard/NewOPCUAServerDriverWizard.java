package com.bichler.astudio.opcua.wizard;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.opcua.wizard.page.NewOPCUADriverStatusPage;
import com.bichler.astudio.opcua.wizard.page.NewOPCUAServerDriverWizardPage;
import com.bichler.astudio.opcua.wizard.util.OPCWizardUtil;

public class NewOPCUAServerDriverWizard extends Wizard implements INewWizard {
	public static final String copyright = "(c) Bichler Technologies GmbH 2018.";
	// the workbench instance
	protected IWorkbench workbench;
	private NewOPCUAServerDriverWizardPage driverPage = null;
	// workbench selection when the wizard was started
	protected IStructuredSelection selection;
	private String drvName = "";
	private String drvType = "";
	private NewOPCUADriverStatusPage driverStatusPage;
	private String drvVersion;

	public NewOPCUAServerDriverWizard() {
		this.setWindowTitle("Automation Studio");
	}
	
	@Override
	public void addPages() {
		driverPage = new NewOPCUAServerDriverWizardPage(workbench, selection);
		addPage(driverPage);
		this.driverStatusPage = new NewOPCUADriverStatusPage();
		addPage(this.driverStatusPage);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
	}

	@Override
	public boolean performFinish() {
		this.drvName = driverPage.getDrvName();
		this.drvType = driverPage.getDrvType();
		this.drvVersion = driverPage.getDrvVersion();
		OPCWizardUtil.newOPCUADriver(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), getDrvName(),
				getDrvType(), getDrvVersion());
		if (this.driverStatusPage.isStatus()) {
			NodeId startId = this.driverStatusPage.getRoot();
			OPCWizardUtil.newOPCUADriverModel(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(),
					startId, this.drvName, 1);
		}
		return true;
	}

	public String getDrvName() {
		return drvName;
	}

	public void setDrvName(String drvName) {
		this.drvName = drvName;
	}

	public String getDrvType() {
		return drvType;
	}

	public String getDrvVersion() {
		return drvVersion;
	}

	public void setDrvType(String drvType) {
		this.drvType = drvType;
	}
}
