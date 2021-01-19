package com.bichler.astudio.device.core.transfer;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bichler.astudio.filesystem.IFileSystem;

public class TargetExecutor {

	private Logger logger = Logger.getLogger(getClass().getName());
	
	private File root;
	private ITarget[] targets = new ITarget[0];
	private IFileSystem targetFileSystem; 

	public TargetExecutor(File source) {
		this.root = source;
		this.targetFileSystem = null;
	}
	
	protected File getRoot() {
		// local file
		return this.root;
	}
	
	protected void setTargetFileSystem(IFileSystem filesystem) {
		this.targetFileSystem = filesystem;
	}
	
	protected IFileSystem getTargetFileSystem() {
		return this.targetFileSystem;
	}
	
	public void setTargets(ITarget[] targets) {
		this.targets = targets;
	}
	
	/**
	 * Execute commands on a target platform
	 * 
	 * @param filesystem
	 * @param targets
	 * @throws IOException 
	 */
	public void execute(IFileSystem filesystem) throws IOException {	
		for(ITarget target : targets) {
			try {
				target.execute(this, filesystem);
			} catch (IOException e) {
				logger.log(Level.SEVERE, getClass().getName() + " Error executing upload "+target.getExecutionString()+"! " + e.getMessage());
				throw e;
			}
		}
	}
}
