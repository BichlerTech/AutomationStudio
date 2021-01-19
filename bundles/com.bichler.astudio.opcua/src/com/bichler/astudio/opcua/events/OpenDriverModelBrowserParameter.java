package com.bichler.astudio.opcua.events;

import com.bichler.astudio.filesystem.IFileSystem;

public class OpenDriverModelBrowserParameter {

	private IFileSystem filesystem;
	private String driverPath;
	private String driverConfigPath;
	private String drivername;

	public void setFilesystem(IFileSystem filesystem) {
		this.filesystem = filesystem;
	}

	public IFileSystem getFilesystem() {
		return this.filesystem;
	}

	public String getDriverPath() {
		return this.driverPath;
	}

	public void setDriverPath(String driverPath) {
		this.driverPath = driverPath;
	}

	public void setDriverConfigPath(String driverConfigFile) {
		this.driverConfigPath = driverConfigFile;
	}

	public String getDriverConfigPath() {
		return this.driverConfigPath;
	}

	public void setDriverName(String drivername) {
		this.drivername = drivername;
	}

	public String getDrivername() {
		return this.drivername;
	}

}
