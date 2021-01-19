package com.bichler.astudio.opcua.opcmodeler.commands.handler.extern;

import opc.sdk.server.core.UAServerApplicationInstance;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.ModelBrowserView;

/**
 * Do not use in Studio!
 * 
 * @author Kofi-Eagle
 *
 */
public class StartDesignerHandler extends AbstractHandler {
	public static final String ID = "com.xcontrol.modeler.opc.startDesigner";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		UAServerApplicationInstance instance = ServerInstance.startServer("");
		ModelBrowserView mbv = (ModelBrowserView) HandlerUtil.getActiveWorkbenchWindow(event).getActivePage()
				.findView(ModelBrowserView.ID);
		if (mbv != null) {
			mbv.startView();
		}
		return instance;
	}
}
