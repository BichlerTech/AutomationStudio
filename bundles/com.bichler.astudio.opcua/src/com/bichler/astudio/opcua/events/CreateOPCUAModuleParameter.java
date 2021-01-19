package com.bichler.astudio.opcua.events;

import org.eclipse.swt.widgets.Shell;

import com.bichler.astudio.filesystem.IFileSystem;

public class CreateOPCUAModuleParameter {
	private String modulePath = "";
	private IFileSystem filesystem = null;
//  private String drvType;
	private Shell shell;
//  private String drvVersion;


	public IFileSystem getFilesystem() {
		return filesystem;
	}

	public void setFilesystem(IFileSystem filesystem) {
		this.filesystem = filesystem;
	}
	// public String getDrvConfigPath() {
	// return drvConfigPath;
	// }

	// public void setDrvConfigPath(String drvConfigPath) {
	// this.drvConfigPath = drvConfigPath;
	// }
//  public void setDrvType(String drvType)
//  {
//    this.drvType = drvType;
//  }
//
//  public void setDrvVersion(String drvVersion)
//  {
//    this.drvVersion = drvVersion;
//  }

//  public String getDrvType()
//  {
//    return this.drvType;
//  }
//
//  public String getDrvVersion()
//  {
//    return this.drvVersion;
//  }

	public Shell getShell() {
		return shell;
	}

	public void setShell(Shell shell) {
		this.shell = shell;
	}

	public String getModulePath() {
		return modulePath;
	}

	public void setModulePath(String modulePath) {
		this.modulePath = modulePath;
	}
}
