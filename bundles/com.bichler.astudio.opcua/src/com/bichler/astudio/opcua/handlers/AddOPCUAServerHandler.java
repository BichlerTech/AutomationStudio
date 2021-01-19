package com.bichler.astudio.opcua.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.opcua.nodes.OPCUARootModelNode;
import com.bichler.astudio.opcua.wizard.NewOPCUAServerProjectWizard;
import com.bichler.astudio.opcua.wizard.util.OPCWizardUtil;

public class AddOPCUAServerHandler extends AbstractHandler {
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
		TreeSelection selection = (TreeSelection) HandlerUtil.getCurrentSelection(event);
		if (selection == null) {
			return null;
		}
		final Object selectedNode = selection.getFirstElement();
		if (selectedNode == null) {
			return null;
		}
		if (!(selectedNode instanceof OPCUARootModelNode)) {
			return null;
		}
		final NewOPCUAServerProjectWizard wizard = new NewOPCUAServerProjectWizard();
		if ((selection instanceof IStructuredSelection) || (selection == null))
			wizard.init(page.getWorkbenchWindow().getWorkbench(), (IStructuredSelection) selection);
		// Instantiates the wizard container with the wizard and opens it
		WizardDialog dialog = new WizardDialog(page.getActivePart().getSite().getShell(), wizard);
		dialog.create();
		if (dialog.open() == Dialog.OK) {
			final String serverName = wizard.getNewServerName();
			final String history = wizard.getHistory();
			final String version = wizard.getVersion();
			final String externalModel = wizard.getExternalModel();
			OPCWizardUtil.newOPCServerProject(page, serverName, version, history, externalModel);
		}
		return null;
	}
}
