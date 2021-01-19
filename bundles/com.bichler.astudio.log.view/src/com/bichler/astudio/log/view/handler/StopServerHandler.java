package com.bichler.astudio.log.view.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.log.view.viewer.ASLogView;

public class StopServerHandler extends AbstractHandler {

	public static final String ID = "com.hbsoft.comet.log4j.stopServer";
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// get the window (which is a IServiceLocator)
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);

		IViewPart part = window.getActivePage().findView(ASLogView.ID);

		if (part != null) {
			ASLogView view = (ASLogView) part;
			view.getController().stopServer();
		}
		return null;
	}

}
