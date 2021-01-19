package com.bichler.astudio.components.ui.file.filter;

import java.io.File;
import java.io.FilenameFilter;

import org.eclipse.core.runtime.Path;

public class XMLFileFilter implements FilenameFilter {

	@Override
	public boolean accept(File parent, String name) {
		Path file = new Path(name);
		String extension = file.getFileExtension();

		if ("xml".equals(extension)) {
			return true;
		}

		return false;
	}
}
