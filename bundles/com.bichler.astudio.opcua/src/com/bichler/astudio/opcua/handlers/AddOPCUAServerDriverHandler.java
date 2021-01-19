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

import com.bichler.astudio.opcua.wizard.NewOPCUAServerDriverWizard;

public class AddOPCUAServerDriverHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();

		TreeSelection selection = (TreeSelection) HandlerUtil.getCurrentSelection(event);

		NewOPCUAServerDriverWizard wizard = new NewOPCUAServerDriverWizard();
		if ((selection instanceof IStructuredSelection) || (selection == null))
			wizard.init(page.getWorkbenchWindow().getWorkbench(), (IStructuredSelection) selection);

		// Instantiates the wizard container with the wizard and opens it
		WizardDialog dialog = new WizardDialog(page.getActivePart().getSite().getShell(), wizard);
		dialog.create();

		if (dialog.open() == Dialog.OK) {
		}

		return null;
	}

}
