package com.bichler.astudio.opcua.wizard;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.bichler.astudio.opcua.nodes.OPCUAServersModelNode;
import com.bichler.astudio.opcua.wizard.page.NewOPCUAServerProjectWizardPage;

public class NewOPCUAServerProjectWizard extends Wizard implements INewWizard {
	public static final String copyright = "(c) Bichler Technologies GmbH 2018.";
	private OPCUAServersModelNode servers = null;
	// the workbench instance
	protected IWorkbench workbench;
	private NewOPCUAServerProjectWizardPage newPage1 = null;
	// workbench selection when the wizard was started
	protected IStructuredSelection selection;
	private String newServerName = "";
	private String history = "";
	private String version;
	private String externalModel;

	public String getNewServerName() {
		return this.newServerName;
	}

	public String getExternalModel() {
		return externalModel;
	}

	@Override
	public void addPages() {
		newPage1 = new NewOPCUAServerProjectWizardPage(workbench, selection);
//    newPage1.setFilesystem(filesystem);
		addPage(newPage1);
		// newPage2 = new NewOPCUAServerDriverWizardPage1(workbench, selection);
		// addPage(newPage2);
	}

	public NewOPCUAServerProjectWizard() {
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
	}

	@Override
	public boolean performFinish() {
		/**
		 * create a new opc ua server
		 */
		this.newServerName = this.newPage1.getNewServerName();
		this.setVersion(this.newPage1.getNewServerVersion());
		this.history = this.newPage1.getHistory();
		this.externalModel = this.newPage1.getExternModel();
		// this.driverType = this.newPage2.getDrvType();
		// this.driverName = this.newPage2.getDrvName();
		return true;
	}

	public OPCUAServersModelNode getServers() {
		return servers;
	}

	public void setServers(OPCUAServersModelNode servers) {
		this.servers = servers;
	}

	public String getHistory() {
		return history;
	}

	public void setHistory(String history) {
		this.history = history;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
