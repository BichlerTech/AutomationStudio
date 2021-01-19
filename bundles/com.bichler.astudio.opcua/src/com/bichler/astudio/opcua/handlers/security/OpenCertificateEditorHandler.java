package com.bichler.astudio.opcua.handlers.security;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.bichler.astudio.opcua.editor.input.OPCUACertificateEditorInput;
import com.bichler.astudio.opcua.editor.security.OPCUACertificateEditor;
import com.bichler.astudio.opcua.navigation.views.OPCNavigationView;
import com.bichler.astudio.opcua.nodes.security.AbstractOPCUACertificateModelNode;

public class OpenCertificateEditorHandler extends AbstractHandler {

	public static final String ID = "com.bichler.astudio.commands.openopcuacertificate";

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

		AbstractOPCUACertificateModelNode selectedNode = (AbstractOPCUACertificateModelNode) selection
				.getFirstElement();

		// if(event.getTrigger() instanceof CometOPCUAEcmaScriptOpenEvent) {

		try {
			OPCUACertificateEditorInput input = new OPCUACertificateEditorInput();
			input.setNode(selectedNode);
			page.openEditor(input, OPCUACertificateEditor.ID);

			// ((CompilationUnitEditor)editor).setInput(input);

		} catch (PartInitException e) {
			e.printStackTrace();
		}
		return null;
	}

}
