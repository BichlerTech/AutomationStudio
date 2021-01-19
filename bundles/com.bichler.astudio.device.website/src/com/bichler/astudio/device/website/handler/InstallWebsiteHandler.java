package com.bichler.astudio.device.website.handler;

import java.util.logging.Logger;

import com.bichler.astudio.device.core.transfer.AbstractInstallWizard;
import com.bichler.astudio.device.core.transfer.AbstractUploadHandler;
import com.bichler.astudio.device.website.wizard.WebsiteInstallWizard;
import com.bichler.astudio.filesystem.IFileSystem;


/**
 * 
 * 
 * @author Thomas
 *
 */
public class InstallWebsiteHandler extends AbstractUploadHandler {

	public static final String ID = "com.bichler.astudio.device.installwebsite";
	private Logger logger = Logger.getLogger(getClass().getName());
	
	@Override
	protected AbstractInstallWizard createWizard(IFileSystem filesystem) {
		return new WebsiteInstallWizard();
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
