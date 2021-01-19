package com.bichler.astudio.opcua.opcmodeler.commands.handler.extern;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;

public class ParametrizedNewModelHandler extends AbstractHandler {
	public static final String ID = "com.xcontrol.modeler.opc.extern.resetServer";
	public static String PARAMETER_PROJECT_PATH = "com.xcontrol.modeler.opc.extern.resetmodel.path";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String projectRootPath = event.getParameter(PARAMETER_PROJECT_PATH);
		ServerInstance.resetServer(projectRootPath);
		return null;
	}
}
