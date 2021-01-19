package com.bichler.astudio.device.opcua.wizard;

import org.eclipse.jface.wizard.Wizard;

import com.bichler.astudio.device.opcua.DeviceActivator;
import com.bichler.astudio.device.opcua.wizard.page.selection.NamespaceModelWizardPage;
import com.bichler.astudio.opcua.nodes.OPCUAServerModelNode;
import com.bichler.astudio.utils.internationalization.CustomString;

public class BuildLocalProjectWizard extends Wizard {

	private NamespaceModelWizardPage namespaceWizardPage;
	private OPCUAServerModelNode serverNode;
	private Object[] namespaces2export;
	private boolean fullNsTableExport;

	public BuildLocalProjectWizard(/* ICometFileSystem serverFileSystem, */
			OPCUAServerModelNode serverNode) {

		setWindowTitle(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.build.wizard.title"));
		this.serverNode = serverNode;
	}

	@Override
	public void addPages() {
		this.namespaceWizardPage = new NamespaceModelWizardPage(
				((OPCUAServerModelNode) this.serverNode).getServerName());
		addPage(this.namespaceWizardPage);
	}

	@Override
	public boolean performFinish() {
		this.namespaces2export = this.namespaceWizardPage.getCheckedElements();
		this.fullNsTableExport = this.namespaceWizardPage.isFullNsExport();
		return true;
	}

	public Object[] getNamespaces2Export() {
		return this.namespaces2export;
	}
	
	public boolean isFullNsTableExport() {
		return this.fullNsTableExport;
	}

}
