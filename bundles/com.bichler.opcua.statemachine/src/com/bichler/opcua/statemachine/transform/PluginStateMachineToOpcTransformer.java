package com.bichler.opcua.statemachine.transform;

import java.io.File;

import com.bichler.opcua.statemachine.BaseStatemachineActivator;

public class PluginStateMachineToOpcTransformer extends AbstractStateMachineToOpcTransformer {

	public PluginStateMachineToOpcTransformer(boolean symbolicIds) {
		super(symbolicIds);
	}

	@Override
	public File[] loadDefaultOpcUaModelFiles() {
		return BaseStatemachineActivator.getDefault().getResourceDefaultOpcUaModelFiles();
	}

	@Override
	public File[] loadUMLDefaultOpcUaClassFiles() {
		return BaseStatemachineActivator.getDefault().getUMLResourceDefaultOpcUaClassFiles();
	}

}
