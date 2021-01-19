package com.bichler.astudio.device.module.wizard;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.wizard.IWizardPage;

import com.bichler.astudio.device.core.transfer.AbstractInstallWizard;
import com.bichler.astudio.device.core.transfer.DeviceTargetUtil;
import com.bichler.astudio.device.core.transfer.TargetExecutor;
import com.bichler.astudio.device.module.ModuleActivator;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.utils.internationalization.CustomString;

public class ModuleInstallWizard extends AbstractInstallWizard {
	private ModuleWizardPage pageOne;
	private IFileSystem targetFileSystem;

	public ModuleInstallWizard(IFileSystem filesystem) {
		setWindowTitle(CustomString.getString(ModuleActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.zip.wizard.title"));
		this.targetFileSystem = filesystem;
	}

	@Override
	public boolean performFinish(){
	   // this.selectedVersion = getVersionIndex();
		File zipFile = getRootFolderToUpload();
		/**String[] versions = DeviceTargetUtil.readTargetVersion(files);
		if( this.selectedVersion >= versions.length) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
			        "com.bichler.astudio.device.core.error.readversionfile"));
			return true;
		}
		
	    this.targets = readTargets(files, versions[this.selectedVersion]);
	    */
		
		this.targets = readTargets(zipFile, null);
	    return true;
	}
	
	@Override
	public void addPages() {
		this.pageOne = new ModuleWizardPage();
		addPage(pageOne);
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		return super.getNextPage(page);
	}

	@Override
	protected int getVersionIndex() {
		// no version needed
		return this.pageOne.getZipVersionIndex();
	}

	@Override
	protected File getRootFolderToUpload() {
		return this.pageOne.getZipfile();
	}

	@Override
	protected TargetExecutor readTargets(File parent, String version) {
		/**
		 * return DeviceTargetUtil.readTargetFileZip(this.targetFileSystem, parent, version);
		 */
		
		return DeviceTargetUtil.readTargetFileZip(this.targetFileSystem, parent);
	}

	@Override
	protected IFileSystem getTargetFilesystem() {
		return this.targetFileSystem;
	}
	
	protected void setTargetFilesystem(IFileSystem filesystem) {
		this.targetFileSystem = filesystem;
	}

}
