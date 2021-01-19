package com.bichler.astudio.editor.siemens.model;

import opc.sdk.core.newApplication.NamespaceItem;

public class SiemensNamespaceItem extends NamespaceItem {
	private boolean serverEntry = true;

	public SiemensNamespaceItem(String ns) {
		super(ns);
	}

	public SiemensNamespaceItem(String ns, boolean serverEntry) {
		super(ns);
		this.serverEntry = serverEntry;
	}

	public boolean isServerEntry() {
		return this.serverEntry;
	}
}
