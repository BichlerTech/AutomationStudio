package com.bichler.module.wizard;

import com.bichler.astudio.device.core.wizard.pages.DeviceConnectionWizardPage;
import com.bichler.astudio.device.core.wizard.pages.ScanForDeviceWizardPage;
import com.bichler.astudio.device.module.wizard.ModuleInstallWizard;
import com.bichler.astudio.filesystem.IFileSystem;

public class InstallModuleToDeviceWizard extends ModuleInstallWizard{

	private ScanForDeviceWizardPage deviceScanPage;
	private DeviceConnectionWizardPage deviceConnectionPage;

	public InstallModuleToDeviceWizard(IFileSystem filesystem) {
		super(filesystem);
	}

	@Override
	public void addPages() {
		this.deviceScanPage = new ScanForDeviceWizardPage(true);
		addPage(deviceScanPage);
		this.deviceConnectionPage = new DeviceConnectionWizardPage(true, true, deviceScanPage);
		addPage(deviceConnectionPage);
		super.addPages();
	}

	@Override
	public boolean performFinish() {
		setTargetFilesystem(this.deviceConnectionPage.getFilesystem());
		return super.performFinish();
	}
	
	

}
