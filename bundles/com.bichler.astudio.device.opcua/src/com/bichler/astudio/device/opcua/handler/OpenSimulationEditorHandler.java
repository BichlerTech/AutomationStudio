package com.bichler.astudio.device.opcua.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.device.opcua.editor.SimulationEditor;
import com.bichler.astudio.device.opcua.editor.SimulationEditorInput;

public class OpenSimulationEditorHandler extends AbstractHandler {

	public static final String ID = "com.bichler.astudio.device.opcua.simulation.openSimulation";

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

		SimulationEditorInput input = new SimulationEditorInput();
		try {
			page.openEditor(input, SimulationEditor.ID);
		} catch (PartInitException e) {
			e.printStackTrace();
		}

		return null;
	}

}
