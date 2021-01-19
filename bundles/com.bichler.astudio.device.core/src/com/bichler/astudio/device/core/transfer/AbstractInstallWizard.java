package com.bichler.astudio.device.core.transfer;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.wizard.Wizard;

import com.bichler.astudio.device.core.DevCoreActivator;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.utils.internationalization.CustomString;


public abstract class AbstractInstallWizard extends Wizard {

	protected TargetExecutor targets = null;
	private int selectedVersion;
	
	public AbstractInstallWizard() {
		super();
	}
	
	@Override
	public boolean performFinish(){
	    this.selectedVersion = getVersionIndex();
		File files = getRootFolderToUpload();
		String[] versions = DeviceTargetUtil.readTargetVersion(files);
		if( this.selectedVersion >= versions.length) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
			        "com.bichler.astudio.device.core.error.readversionfile"));
			return true;
		}
		
	    this.targets = readTargets(files, versions[this.selectedVersion]);
	    return true;
	}

	protected TargetExecutor readTargets(File parent, String version) {
		return DeviceTargetUtil.readTargetFile(parent, version);
	}
	
	public TargetExecutor getTargets() {
	  return this.targets;
	}
	
	public int getSelectedVersion() {
		return this.selectedVersion;
	}
	
	protected IFileSystem getTargetFilesystem() {
		return null;
	}
	
	protected abstract File getRootFolderToUpload();
	protected abstract int getVersionIndex();
}
