package com.bichler.opcua.statemachine.reverse.engineering;

import java.io.File;

import com.bichler.opcua.statemachine.BaseStatemachineActivator;
import com.bichler.opcua.statemachine.exception.StatemachineException;

public class PluginReverseStatemachineEngine extends AbstractInternalReverseEngine {

	public PluginReverseStatemachineEngine() throws StatemachineException {
		super();
	}

	@Override
	public File[] loadDefaultOpcUaModelFiles() {
		return BaseStatemachineActivator.getDefault().getResourceDefaultOpcUaModelFiles();
	}

	@Override
	public File[] loadUMLDefaultOpcUaClassFiles() {
		return BaseStatemachineActivator.getDefault().getUMLResourceDefaultOpcUaClassFiles();
	}

	@Override
	public File[] getUMLBaseTypeFiles() {
		return BaseStatemachineActivator.getDefault().getUMLBaseTypeFiles();
	}

}
