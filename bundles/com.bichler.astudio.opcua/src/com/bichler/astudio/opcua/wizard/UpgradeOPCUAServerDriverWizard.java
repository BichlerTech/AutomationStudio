package com.bichler.astudio.opcua.wizard;

import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.opcua.wizard.page.NewOPCUADriverStatusPage;
import com.bichler.astudio.opcua.wizard.page.NewOPCUAServerDriverWizardPage;
import com.bichler.astudio.opcua.wizard.page.UpgradeOPCUAServerDriverWizardPage;
import com.bichler.astudio.opcua.wizard.util.OPCWizardUtil;

public class UpgradeOPCUAServerDriverWizard extends Wizard implements INewWizard {
	public static final String copyright = "(c) Bichler Technologies GmbH 2018.";
	// the workbench instance
	protected IWorkbench workbench;
	private UpgradeOPCUAServerDriverWizardPage driverPage = null;
	// workbench selection when the wizard was started
	protected IStructuredSelection selection;
	private String drvName = "";
	private String drvType = "";
	private String drvVersion;

	public UpgradeOPCUAServerDriverWizard(String drvName, String drvType, String drvVersion) {
		this.setWindowTitle("Automation Studio");
		this.drvName = drvName;
		this.drvType = drvType;
		this.drvVersion = drvVersion;
	}
	
	@Override
	public void addPages() {
		driverPage = new UpgradeOPCUAServerDriverWizardPage(workbench, selection);
		driverPage.setDRVType(drvType);
		driverPage.setDRVName(drvName);
		driverPage.setOldDRVVersion(drvVersion);
		addPage(driverPage);
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
//    StudioModelNode node = (StudioModelNode) this.selection.getFirstElement();
	}

	@Override
	public boolean performFinish() {
		this.drvName = driverPage.getDrvName();
		this.drvType = driverPage.getDrvType();
		this.drvVersion = driverPage.getDrvVersion();
		OPCWizardUtil.newOPCUADriver(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), getDrvName(),
				getDrvType(), getDrvVersion());
//    if (this.driverStatusPage.isStatus())
//    {
//      NodeId startId = this.driverStatusPage.getRoot();
//      OPCWizardUtil.newOPCUADriverModel(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), startId,
//          this.drvName, 1);
//    }
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
