package com.bichler.astudio.opcua.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.bichler.astudio.opcua.navigation.views.OPCNavigationView;
import com.bichler.astudio.opcua.editor.input.OPCUAServerSecurityEditorInput;
import com.bichler.astudio.opcua.editor.security.OPCUASecurityConfigEditor;
import com.bichler.astudio.opcua.nodes.security.OPCUAServerSecurityModelNode;

public class OpenOPCUASecurityConfigurationHandler extends AbstractHandler {

	public static final String ID = "com.bichler.astudio.commands.openopcuasecurityconfig";

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

		OPCUAServerSecurityModelNode selectedNode = (OPCUAServerSecurityModelNode) selection.getFirstElement();

		// if(event.getTrigger() instanceof CometOPCUAEcmaScriptOpenEvent) {

		try {
			OPCUAServerSecurityEditorInput input = new OPCUAServerSecurityEditorInput();
			input.setNode(selectedNode);
			input.init();
			page.openEditor(input, OPCUASecurityConfigEditor.ID);

			// ((CompilationUnitEditor)editor).setInput(input);

		} catch (PartInitException e) {
			e.printStackTrace();
		}
		return null;
	}

}
