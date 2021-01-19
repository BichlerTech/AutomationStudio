package com.bichler.astudio.log.view.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.bichler.astudio.log.view.viewer.ASLogView;

public class StartServerHandler extends AbstractHandler {

	public static final String ID = "com.hbsoft.comet.log4j.startServer";
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// get the window (which is a IServiceLocator)
		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow awb = wb.getActiveWorkbenchWindow();
		IWorkbenchPage page = awb.getActivePage();
//		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);

		IViewPart part = page.findView(ASLogView.ID);
		if (part != null) {
			ASLogView view = (ASLogView) part;
			view.getController().startServer();
		}
		
		
		// Logger logger = Logger.getLogger(StartServerHandler.class);
		// logger.error("test");
		
		return null;
	}

}
