package com.bichler.astudio.opcua.addressspace.xml.exporter;

import com.bichler.astudio.opcua.addressspace.xml.exporter.factory.IModelFactory;
import com.bichler.astudio.opcua.addressspace.xml.exporter.factory.XMLModelV2Factory;

import opc.sdk.server.core.OPCInternalServer;

public class ModelV2Exporter extends AbstractModelExporter {

	public ModelV2Exporter(OPCInternalServer sInstance) {
		super(sInstance);

	}

	@Override
	protected IModelFactory createModelFactory() {
		return new XMLModelV2Factory(progressMonitor, this.serverInstance);
	}
}
