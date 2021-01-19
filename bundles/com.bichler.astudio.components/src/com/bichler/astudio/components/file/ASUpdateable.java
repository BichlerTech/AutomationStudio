package com.bichler.astudio.components.file;

import com.bichler.astudio.filesystem.IFileSystem;

public abstract class ASUpdateable {

	private IFileSystem filesystem = null;

	public ASUpdateable(IFileSystem filesystem) {
		this.filesystem = filesystem;
	}

	public IFileSystem getFilesystem() {
		return this.filesystem;
	}
	
	public abstract Object getTrigger();
}
