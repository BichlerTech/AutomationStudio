package com.bichler.astudio.opcua.opcmodeler.commands.handler.extern;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.ModelBrowserView;

public class CloseDesignerHandler extends AbstractHandler {
	public static final String ID = "com.xcontrol.modeler.opc.closeDesigner";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ServerInstance.closeServer();
		ModelBrowserView mbv = (ModelBrowserView) HandlerUtil.getActiveWorkbenchWindow(event).getActivePage()
				.findView(ModelBrowserView.ID);
		if (mbv != null) {
			mbv.closeView();
		}
		return null;
	}
}
