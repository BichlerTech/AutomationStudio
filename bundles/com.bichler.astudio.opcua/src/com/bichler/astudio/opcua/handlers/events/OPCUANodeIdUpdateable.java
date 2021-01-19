package com.bichler.astudio.opcua.handlers.events;

import com.bichler.astudio.components.file.ASUpdateable;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.components.ui.handler.update.IOPCUAUpdateable;
import com.bichler.astudio.opcua.opcmodeler.commands.OPCUAUpdateNodeIdEvent;

public class OPCUANodeIdUpdateable extends ASUpdateable {

	private OPCUAUpdateNodeIdEvent trigger;

	public OPCUANodeIdUpdateable(IFileSystem filesystem, IOPCUAUpdateable trigger) {
		super(filesystem);

		if (!(trigger instanceof OPCUAUpdateNodeIdEvent)) {
			throw new IllegalArgumentException("wrong trigger caller");
		}

		this.trigger = (OPCUAUpdateNodeIdEvent) trigger;
	}

	@Override
	public OPCUAUpdateNodeIdEvent getTrigger() {
		return this.trigger;
	}

}
