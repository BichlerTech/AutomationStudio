package com.bichler.astudio.opcua.addressspace.xml.exporter.factory;

import opc.sdk.core.application.operation.ICancleOperation;

public abstract class AbstractXMLModelFactory implements IModelFactory {

	private ICancleOperation progressMonitor = null;
	
	public AbstractXMLModelFactory(ICancleOperation progressMonitor) {
		this. progressMonitor = progressMonitor;
	}
	
	boolean checkProgressMonitorCancled() {
		if (this.progressMonitor != null && this.progressMonitor.isCanceled()) {
			return true;
		}
		return false;
	}
}
