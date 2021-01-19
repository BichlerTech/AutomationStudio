package com.bichler.astudio.view.drivermodel.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.view.drivermodel.browser.DriverModelBrowserView;

public class RefreshDriverModelHandler extends AbstractHandler {

	public static final String ID = "com.bichler.astudio.view.drivermodel.openDrivermodelView";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		if (window == null) {
			return null;
		}

		IWorkbenchPage page = window.getActivePage();
		if (page == null) {
			return null;
		}

		DriverModelBrowserView view = (DriverModelBrowserView) page
				.findView(DriverModelBrowserView.ID);
		if (view == null) {
			return null;
		}
		view.refresh();

		
		return null;
	}

}
