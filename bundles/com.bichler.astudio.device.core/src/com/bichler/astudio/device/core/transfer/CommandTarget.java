package com.bichler.astudio.device.core.transfer;

import java.io.IOException;

import com.bichler.astudio.filesystem.IFileSystem;

public class CommandTarget extends AbstractTarget{

	
	public CommandTarget(String execution) {
		super(execution);
	}

	@Override
	public boolean execute(TargetExecutor executor, IFileSystem filesystem) throws IOException {
		return filesystem.execCommand(this.execution);
	}

}
