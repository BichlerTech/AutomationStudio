package com.bichler.astudio.device.core.transfer;

import java.io.IOException;

import com.bichler.astudio.filesystem.IFileSystem;

public interface ITarget {

	/**
	 * Executes a target
	 * 
	 * @param filesystem
	 * 			target file system
	 * @param command
	 * 			command to execute
	 * @return
	 */
	public boolean execute(TargetExecutor executor, IFileSystem filesystem) throws IOException;
	
	public String getExecutionString();
}
