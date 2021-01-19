package com.bichler.astudio.opcua.handlers.opcua;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.opcua.components.ui.Studio_ResourceManager;

public class OPCUAProjectBackHandler extends AbstractHandler {

	public static final String ID = "com.bichler.astudio.opcserver.back";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);

		IWorkbenchPage page = window.getActivePage();
		Boolean notCanceled = page.saveAllEditors(true);

		if (notCanceled) {
			// close OPC ModelDesigner
			Studio_ResourceManager.setInfoModellerResource(null);
			Studio_ResourceManager.setInfoModellerDokuResource(null);
			Studio_ResourceManager.setServerName(null);
			// switch perspective
			OPCUAUtil.closePerspective(window, null);
		}
		return null;
	}

}
