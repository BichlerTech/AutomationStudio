package com.bichler.astudio.opcua.handlers.events;

import com.bichler.astudio.filesystem.IFileSystem;

public abstract class AbstractOPCUAValidationParameter {

	private IFileSystem filesystem = null;

	public AbstractOPCUAValidationParameter() {
		super();
	}

	public IFileSystem getFilesystem() {
		return filesystem;
	}

	public void setFilesystem(IFileSystem filesystem) {
		this.filesystem = filesystem;
	}

}
