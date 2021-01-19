package com.bichler.astudio.device.wakeonlan.handler;

import java.util.logging.Logger;

import com.bichler.astudio.device.core.transfer.AbstractInstallWizard;
import com.bichler.astudio.device.core.transfer.AbstractUploadHandler;
import com.bichler.astudio.device.wakeonlan.wizard.WakeonlanInstallWizard;
import com.bichler.astudio.filesystem.IFileSystem;


public class InstallWakeonlanHandler extends AbstractUploadHandler {
	public static final String ID = "com.bichler.astudio.device.installwakeonlan";
	private Logger logger = Logger.getLogger(getClass().getName());

	@Override
	protected AbstractInstallWizard createWizard(IFileSystem filesystem) {
		return new WakeonlanInstallWizard();
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