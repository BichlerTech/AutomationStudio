package com.bichler.astudio.opcua.handlers;

import org.eclipse.swt.widgets.Event;

public class OpenOPCUAEcmaScriptEvent extends Event {

	private int interval = 0;

	private String filePath = "";

	public OpenOPCUAEcmaScriptEvent() {
		super();
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
