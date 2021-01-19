package com.bichler.astudio.device.core.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.osgi.service.prefs.Preferences;

import com.bichler.astudio.device.core.dialog.UABoxStatusDialog;
import com.bichler.astudio.device.core.preference.DevicePreferenceManager;
import com.bichler.astudio.device.core.view.OPCUADeviceView;
import com.bichler.astudio.filesystem.IFileSystem;

public class OpenUABoxControlHandler extends AbstractHandler {

	public static final String ID = "com.bichler.astudio.core.box.openControl";

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

		OPCUADeviceView devView = (OPCUADeviceView) page.findView(OPCUADeviceView.ID);
		IStructuredSelection selection = (IStructuredSelection) devView.getSite().getSelectionProvider().getSelection();
		Preferences bo = (Preferences) selection.getFirstElement();

		DevicePreferenceManager prefManager = new DevicePreferenceManager();
		IFileSystem filesystem = prefManager.getFilesystem(bo.name());

		UABoxStatusDialog status = new UABoxStatusDialog(page.getActivePart().getSite().getShell(), filesystem,
				SWT.NONE);
		status.open();

		/**
		 * Local Filesystem
		 */
		// OPCNavigationView view = (OPCNavigationView)
		// page.findView(OPCNavigationView.ID);
		// if(view == null){
		// return null;
		// }
		// Object root = view.getViewer().getInput();
		/**
		 * TODO: Target Device!
		 */

		// SimulationEditorInput input = new SimulationEditorInput();
		// try {
		// page.openEditor(input, SimulationEditorPart.ID);
		// } catch (PartInitException e) {
		// e.printStackTrace();
		// }

		return null;
	}

}
