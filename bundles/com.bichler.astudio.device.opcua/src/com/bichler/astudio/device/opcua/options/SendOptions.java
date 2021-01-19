package com.bichler.astudio.device.opcua.options;

import com.bichler.astudio.filesystem.IFileSystem;

public abstract class SendOptions {

	private boolean isComboxUpload = false;
	private OS_Startup os = OS_Startup.UNDEFINED;
	private String javaPath;
	private IFileSystem filesystem;

	public SendOptions() {
	}

	public boolean isHBDatahubUpload() {
		return isComboxUpload;
	}

	public OS_Startup getOs() {
		return os;
	}

	public String getJavaPath() {
		return javaPath;
	}

	public IFileSystem getFilesystem() {
		return this.filesystem;
	}

	public void setComboxUpload(boolean isComboxUpload) {
		this.isComboxUpload = isComboxUpload;
	}

	public void setFilesystem(IFileSystem filesystem) {
		this.filesystem = filesystem;
	}

	public void setOs(OS_Startup os) {
		this.os = os;
	}

	public void setJavaPath(String javaPath) {
		this.javaPath = javaPath;
	}

	public abstract String buildStartup(String path, String servername);

	public abstract String nameScriptFile(String startupScriptPath);

}
