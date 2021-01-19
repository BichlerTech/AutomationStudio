package com.bichler.opcua.statemachine.addressspace;

import org.opcfoundation.ua.common.NamespaceTable;

import opc.sdk.core.context.StringTable;

public class ModelV2Exporter extends AbstractModelExporter{


	public ModelV2Exporter(NamespaceTable nsTable, StringTable serverUri) {
		super(nsTable, serverUri);
	}

	@Override
	protected IModelFactory createModelFactory() {
		return new XMLModelV2Factory(progressMonitor, this.serverNSTable);
	}
}
