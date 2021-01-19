package com.bichler.module.handler;

import java.util.logging.Logger;

import org.eclipse.core.commands.ExecutionEvent;

import com.bichler.astudio.device.core.transfer.AbstractInstallWizard;
import com.bichler.astudio.device.core.transfer.AbstractUploadHandler;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.module.wizard.InstallModuleToDeviceWizard;

public class InstallModuleToDeviceHandler extends AbstractUploadHandler {

	public static final String ID = "com.bichler.module.install";
	private Logger logger = Logger.getLogger(getClass().getName());
	
	@Override
	protected AbstractInstallWizard createWizard(IFileSystem filesystem) {
		return new InstallModuleToDeviceWizard(filesystem);
	}

	@Override
	protected IFileSystem findFileSystem(ExecutionEvent event) {
		return null;
	}
	
	@Override
	protected String titleJob() {
		return "com.bichler.astudio.device.core.monitor.upload.title";
	}
	
	@Override
	protected String descriptionJob() {
		return "com.bichler.astudio.device.website.install.monitor.upload";
	}
}
