package com.bichler.astudio.device.module.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bichler.astudio.device.core.transfer.AbstractInstallWizard;
import com.bichler.astudio.device.core.transfer.AbstractUploadHandler;
import com.bichler.astudio.device.core.transfer.DeviceTargetUtil;
import com.bichler.astudio.device.module.wizard.ModuleInstallWizard;
import com.bichler.astudio.filesystem.DataHubFileSystem;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.filesystem.SshFileSystem;

/**
 * 
 * 
 * @author Thomas
 *
 */
public class InstallModuleHandler extends AbstractUploadHandler {

	public static final String ID = "com.bichler.astudio.device.installmodule";
	private Logger logger = Logger.getLogger(getClass().getName());

	@Override
	protected AbstractInstallWizard createWizard(IFileSystem filesystem) {
		return new ModuleInstallWizard(filesystem);
	}

	@Override
	protected void preInstall(IFileSystem filesystem, File file) throws IOException {
		// transfer zip from local created to target filesystem --> /modules.zip
		String targetPath = getTargetPath(filesystem);
		// remove file if exists
		if (filesystem.isFile(targetPath)) {
			filesystem.removeFile(targetPath);
		}
		// add file
		if (!filesystem.isFile(targetPath)) {
			filesystem.addFile(targetPath);
		}

		// write file
		try (FileInputStream fis = new FileInputStream(file); OutputStream out = filesystem.writeFile(targetPath);) {
			int read = -1;
			byte[] buffer = new byte[1024];
			while ((read = fis.read(buffer, 0, buffer.length)) != -1) {
				out.write(buffer, 0, read);
			}
		} catch (Exception e) {
			Logger.getLogger(DeviceTargetUtil.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}

		unzip(filesystem, targetPath);
	}

	private void unzip(IFileSystem filesystem, String zipPath) throws IOException {
		if (filesystem instanceof DataHubFileSystem) {
			if (filesystem.isDir(DeviceTargetUtil.FOLDER_SSH_UNZIP_MODULE)) {
				filesystem.execCommand("rm -r " + DeviceTargetUtil.FOLDER_SSH_UNZIP_MODULE);
			}

			// create folder /unzip_module/
			filesystem.addDir(DeviceTargetUtil.FOLDER_SSH_UNZIP_MODULE);

			// unzip module.zip to /unzip_module/
			filesystem.execCommand("unzip /module.zip -d /unzip_module");
		}
	}

	@Override
	protected void postInstall(IFileSystem filesystem, File root) throws IOException {
		String targetPath = getTargetPath(filesystem);
		// remove file if exists
		if (filesystem.isFile(targetPath)) {
			filesystem.removeFile(targetPath);
		}

		if (filesystem instanceof SshFileSystem) {
			filesystem.execCommand("rm -r " + DeviceTargetUtil.FOLDER_SSH_UNZIP_MODULE);
		}
	}

	private String getTargetPath(IFileSystem filesystem) throws IOException {
		String targetPath = null;
		if (filesystem instanceof DataHubFileSystem) {
			targetPath = "/";
		} else {
			// check for separator
			if (filesystem.getRootPath().endsWith(filesystem.getTargetFileSeparator())) {
				targetPath = filesystem.getRootPath();
			} else {
				targetPath = filesystem.getRootPath() + filesystem.getTargetFileSeparator();
			}
		}
		targetPath = targetPath + DeviceTargetUtil.FILE_MODULES;
		return targetPath;
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
