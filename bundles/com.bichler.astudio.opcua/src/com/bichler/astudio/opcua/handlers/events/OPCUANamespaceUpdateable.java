package com.bichler.astudio.opcua.handlers.events;

import com.bichler.astudio.components.file.ASUpdateable;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.components.ui.handler.update.IOPCUAUpdateable;
import com.bichler.astudio.opcua.opcmodeler.commands.NamespaceTableChangeParameter;

public class OPCUANamespaceUpdateable extends ASUpdateable {

	private OPCUAUpdateNamespaceTableDriverParameter trigger;

	public OPCUANamespaceUpdateable(IFileSystem filesystem, IOPCUAUpdateable trigger) {
		super(filesystem);

		if (!(trigger instanceof OPCUAUpdateNamespaceTableDriverParameter)) {
			throw new IllegalArgumentException("wrong trigger caller");
		}

		this.trigger = (OPCUAUpdateNamespaceTableDriverParameter) trigger;
	}

	@Override
	public NamespaceTableChangeParameter getTrigger() {
		return this.trigger;
	}

}
