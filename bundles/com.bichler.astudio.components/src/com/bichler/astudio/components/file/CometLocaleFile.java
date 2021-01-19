package com.bichler.astudio.components.file;

import java.io.File;

import org.eclipse.core.internal.filesystem.local.LocalFile;

@SuppressWarnings("restriction")
public class CometLocaleFile extends LocalFile {

	public CometLocaleFile(File file) {
		super(file);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return super.getName().replace(".es", "");
	}

	public File getFile() {
		return this.file;
	}
}
