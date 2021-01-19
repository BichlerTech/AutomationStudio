package com.bichler.astudio.opcua.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.bichler.astudio.opcua.navigation.views.OPCNavigationView;
import com.bichler.astudio.opcua.editor.OPCUAServerLoggingEditor;
import com.bichler.astudio.opcua.editor.input.OPCUAServerLoggingEditorInput;
import com.bichler.astudio.opcua.nodes.OPCUAServerLoggingModelNode;

public class OpenOPCUAServerLogHandler extends AbstractHandler {

	public static final String ID = "com.bichler.astudio.commands.openopcuaserverlog";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

		if (page == null) {
			return null;
		}

		TreeSelection selection = (TreeSelection) page.getSelection(OPCNavigationView.ID);

		if (selection == null) {
			return null;
		}

		if (!(selection.getFirstElement() instanceof OPCUAServerLoggingModelNode)) {
			return null;
		}

		OPCUAServerLoggingModelNode selectedNode = (OPCUAServerLoggingModelNode) selection.getFirstElement();

		try {
			OPCUAServerLoggingEditorInput input = new OPCUAServerLoggingEditorInput();
			input.setNode(selectedNode);
			input.init();
			page.openEditor(input, OPCUAServerLoggingEditor.ID);
		} catch (PartInitException e) {
			e.printStackTrace();
		}

		return null;
	}

}
