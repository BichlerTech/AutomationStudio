package com.bichler.astudio.opcua.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.bichler.astudio.opcua.navigation.views.OPCNavigationView;
import com.bichler.astudio.opcua.editor.OPCUAServerUsersEditor;
import com.bichler.astudio.opcua.editor.input.OPCUAServerUsersEditorInput;
import com.bichler.astudio.opcua.nodes.OPCUAServerUsersModelNode;

public class OpenOPCUAServerUsersHandler extends AbstractHandler {

	public static final String ID = "com.bichler.astudio.commands.openopcuaserverusers";

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

		if (!(selection.getFirstElement() instanceof OPCUAServerUsersModelNode)) {
			return null;
		}

		OPCUAServerUsersModelNode selectedNode = (OPCUAServerUsersModelNode) selection.getFirstElement();

		// if(event.getTrigger() instanceof CometOPCUAEcmaScriptOpenEvent) {

		try {
			OPCUAServerUsersEditorInput input = new OPCUAServerUsersEditorInput();
			input.setNode(selectedNode);
			input.init();
			page.openEditor(input, OPCUAServerUsersEditor.ID);

			// ((CompilationUnitEditor)editor).setInput(input);
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// }

		return null;

	}

}
