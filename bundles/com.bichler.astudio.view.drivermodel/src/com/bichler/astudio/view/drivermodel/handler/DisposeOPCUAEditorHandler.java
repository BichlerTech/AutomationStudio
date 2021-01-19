package com.bichler.astudio.view.drivermodel.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.bichler.astudio.view.drivermodel.handler.util.DriverBrowserUtil;

public class DisposeOPCUAEditorHandler extends AbstractHandler {

	public static final String ID = "com.bichler.astudio.view.drivermodel.opcua.dispose";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();

		if (window != null) {
			IWorkbenchPage page = window.getActivePage();
			if (page != null) {
				IEditorReference[] references = page.getEditorReferences();
				if (references == null || references.length == 0) {
					DriverBrowserUtil.openEmptyDriverModelView();
				}
			}
		}
		return null;
	}

}
