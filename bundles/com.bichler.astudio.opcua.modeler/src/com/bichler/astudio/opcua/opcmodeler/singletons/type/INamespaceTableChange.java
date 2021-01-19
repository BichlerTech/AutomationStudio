package com.bichler.astudio.opcua.opcmodeler.singletons.type;

import com.bichler.astudio.opcua.opcmodeler.commands.NamespaceTableChangeParameter;

public interface INamespaceTableChange {
	public void onNamespaceChange(NamespaceTableChangeParameter trigger);
}
