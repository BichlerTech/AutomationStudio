package com.bichler.astudio.opcua.handlers.opcua;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.opcua.components.ui.Studio_ResourceManager;

public class OPCUASaveModelHandler extends AbstractHandler {

	public static final String ID = "com.bichler.astudio.opcserver.save";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		String path = Studio_ResourceManager.getInfoModellerResource();

		OPCUAUtil.saveModel(window, path);

		return null;
	}

}
