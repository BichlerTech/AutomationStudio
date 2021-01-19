package com.bichler.astudio.device.opcua.wizard;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

import com.bichler.astudio.core.user.util.UserUtils;
import com.bichler.astudio.device.opcua.options.EthernetUploadOption;
import com.bichler.astudio.device.opcua.options.SendOptions;
import com.bichler.astudio.device.opcua.wizard.page.connection.DeviceEthernetWizardPage;
import com.bichler.astudio.device.opcua.wizard.page.connection.ServerCertificateWizardPage;
import com.bichler.astudio.device.opcua.wizard.page.selection.DeviceSelectionWizardPage;
import com.bichler.astudio.device.opcua.wizard.page.selection.DriverSelectionWizardPage;
import com.bichler.astudio.device.opcua.wizard.page.selection.ModuleSelectionWizardPage;
import com.bichler.astudio.device.opcua.wizard.page.selection.NamespaceModelWizardPage;
import com.bichler.astudio.device.opcua.wizard.page.selection.StartupWizardPage;
import com.bichler.astudio.device.opcua.wizard.page.selection.UploadOrCompileWizardPage;
import com.bichler.astudio.device.opcua.wizard.page.selection.UploadTypeWizardPage;
import com.bichler.astudio.filesystem.DataHubFileSystem;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.filesystem.SimpleFileSystem;
import com.bichler.astudio.filesystem.SshFileSystem;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerModelNode;

public class DeviceTargetWizard extends Wizard {

	@Override
	public boolean canFinish() {
		// TODO Auto-generated method stub
		return super.canFinish();
	}

	private IFileSystem targetFileSysteme;
	private UploadOrCompileWizardPage wizardPageUploadOrCompileType;
	private DeviceSelectionWizardPage wizardPageDevices;
	private NamespaceModelWizardPage wizardPageInfoModel;
	private DriverSelectionWizardPage wizardPageDrivers;
	private ModuleSelectionWizardPage wizardPageModules;
	private StartupWizardPage wizardPageStartup;
	private IFileSystem serverFileSystem;
	private StudioModelNode serverNode;
	private Object[] targetDrivers;
	private Object[] targetModules;
	private Object[] namespaces2export;
	private boolean fullNsTable2export;
	private SendOptions uploadOptions;
	private boolean isComBoxUpload;
	private boolean isCustomUpload;
	private DeviceEthernetWizardPage wizardPageEthernet;
	private EthernetUploadOption ethernetOptions;
	private boolean isDatahub = false;
	private boolean isUploadNewCertificate;
	private ServerCertificateWizardPage wizardCertificatePage;

	public DeviceTargetWizard(IFileSystem serverFileSystem, StudioModelNode serverNode, String title) {
		setWindowTitle(title);
		this.serverFileSystem = serverFileSystem;
		this.serverNode = serverNode;
	}

	@Override
	public void addPages() {
		boolean isAdmin = UserUtils.testUserRights(1);

		this.wizardPageDevices = new DeviceSelectionWizardPage();
		addPage(this.wizardPageDevices);
		this.wizardPageEthernet = new DeviceEthernetWizardPage();
		addPage(this.wizardPageEthernet);
		this.wizardCertificatePage = new ServerCertificateWizardPage();
		addPage(this.wizardCertificatePage);

		if (this.serverNode instanceof OPCUAServerModelNode) {
			this.wizardPageUploadOrCompileType = new UploadOrCompileWizardPage();
			addPage(this.wizardPageUploadOrCompileType);

			this.wizardPageStartup = new StartupWizardPage(this.serverFileSystem);
			if (isAdmin) {
				addPage(this.wizardPageStartup);
			}

			this.wizardPageInfoModel = new NamespaceModelWizardPage(
					((OPCUAServerModelNode) this.serverNode).getServerName());
			addPage(this.wizardPageInfoModel);

			this.wizardPageDrivers = new DriverSelectionWizardPage(this);
			this.wizardPageDrivers.setServerFileSystem(this.serverFileSystem);
			this.wizardPageDrivers.setServerNode(this.serverNode);
			addPage(this.wizardPageDrivers);

			this.wizardPageModules = new ModuleSelectionWizardPage(this);
			this.wizardPageModules.setServerFileSystem(this.serverFileSystem);
			this.wizardPageModules.setServerNode(this.serverNode);
			if (isAdmin) {
				addPage(this.wizardPageModules);
			}
		}

	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page instanceof DeviceSelectionWizardPage) {
			IFileSystem device = ((DeviceSelectionWizardPage) page).getTargetFileSystem();
			// update driver view with target version
			wizardPageDrivers.setTargetFileSystem(device);
			wizardPageDrivers.initialize();

			wizardPageModules.setTargetFileSystem(device);
			wizardPageModules.initialize();
			if (device instanceof DataHubFileSystem) {
				this.isDatahub = true;
			} else {
				this.isDatahub = false;
				return this.wizardCertificatePage;
			}
		} else if (page instanceof UploadTypeWizardPage) {
			isComBoxUpload = ((UploadTypeWizardPage) page).isComboxUpload();
			this.wizardPageStartup.doChangeOptions(isComBoxUpload);
		} else if (page instanceof UploadOrCompileWizardPage) {
			isCustomUpload = ((UploadOrCompileWizardPage) page).isCustomUpload();
			this.wizardPageStartup.doChangeOptions(isComBoxUpload);
			if (isComBoxUpload) {
				return wizardPageInfoModel;
			}
		} else if (page instanceof DriverSelectionWizardPage) {
			this.wizardPageDrivers.setValidateRequ(true);
			this.wizardPageDrivers.getTableViewer().refresh();
		} else if (page instanceof ModuleSelectionWizardPage) {
			this.wizardPageModules.setValidateRequ(true);
			this.wizardPageModules.getTableViewer().refresh();
		}
		return super.getNextPage(page);
	}

	@Override
	public IWizardPage getPreviousPage(IWizardPage page) {
		if (page instanceof DriverSelectionWizardPage) {
			this.wizardPageDrivers.setValidateRequ(false);
		}
		return super.getPreviousPage(page);
	}

	@Override
	public boolean performFinish() {
		this.targetFileSysteme = this.wizardPageDevices.getTargetFileSystem();
		if (this.targetFileSysteme instanceof SimpleFileSystem) {
			this.wizardPageStartup.doChangeOptions(false);
		} else if (this.targetFileSysteme instanceof SshFileSystem) {
			this.wizardPageStartup.doChangeOptions(true);
		}
		this.isUploadNewCertificate = this.wizardCertificatePage.getUploadNewCertificate();
		if (this.isCustomUpload) {
			this.fullNsTable2export = this.wizardPageInfoModel.isFullNsExport();
			this.namespaces2export = this.wizardPageInfoModel.getCheckedElements();
			this.targetDrivers = this.wizardPageDrivers.getSelectedDrivers();
			this.targetModules = this.wizardPageModules.getSelectedModules();

		} else {
			this.namespaces2export = this.wizardPageInfoModel.getAllNamespaceElements();
			this.targetDrivers = this.wizardPageDrivers.getAllDrivers();
			this.targetModules = this.wizardPageModules.getSelectedModules();
		}
		this.uploadOptions = this.wizardPageStartup.getSendOptions();
		this.ethernetOptions = this.wizardPageEthernet.getEthernetOptions();
		
		return true;
	}

	public boolean isFullNsTable() {
		return this.fullNsTable2export;
	}

	public boolean isUploadNewCertificate() {
		return this.isUploadNewCertificate;
	}

	public IFileSystem getTargetFileSystem() {
		return this.targetFileSysteme;
	}

	public Object[] getNamespaces2Export() {
		return this.namespaces2export;
	}

	public Object[] getDriver2export() {
		return this.targetDrivers;
	}

	public Object[] getModules2export() {
		return this.targetModules;
	}

	public SendOptions getSendOptions() {
		return this.uploadOptions;
	}

	public EthernetUploadOption getEthernetOptions() {
		return this.ethernetOptions;
	}
}
