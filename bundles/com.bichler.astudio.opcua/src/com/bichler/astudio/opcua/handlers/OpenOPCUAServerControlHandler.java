package com.bichler.astudio.opcua.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.navigation.views.NavigationView;
import com.bichler.astudio.opcua.editor.OPCUAServerControlEditor;
import com.bichler.astudio.opcua.editor.input.OPCUAServerControlEditorInput;
import com.bichler.astudio.opcua.nodes.OPCUAServerModelNode;

public class OpenOPCUAServerControlHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

		if (page != null) {
			TreeSelection selection = (TreeSelection) HandlerUtil.getActiveWorkbenchWindow(event).getSelectionService()
					.getSelection(NavigationView.ID);

			OPCUAServerModelNode selectedNode = (OPCUAServerModelNode) selection.getFirstElement();

			// if(event.getTrigger() instanceof CometOPCUAEcmaScriptOpenEvent) {

			try {
				OPCUAServerControlEditorInput input = new OPCUAServerControlEditorInput();
				input.setNode(selectedNode);
				input.setFilesystem(selectedNode.getFilesystem());

				page.openEditor(input, OPCUAServerControlEditor.ID);

				// ((CompilationUnitEditor)editor).setInput(input);

			} catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// }

		}
		return null;
	}

}
