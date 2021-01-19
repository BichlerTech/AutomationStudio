package com.bichler.astudio.opcua.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.navigation.views.NavigationView;
import com.bichler.astudio.opcua.wizard.NewOPCUAServerCertificateWizard;

public class AddOPCUAServerCertificateHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();

		TreeSelection selection = (TreeSelection) HandlerUtil.getActiveWorkbenchWindow(event).getSelectionService()
				.getSelection(NavigationView.ID);

		NewOPCUAServerCertificateWizard wizard = new NewOPCUAServerCertificateWizard();
		if ((selection instanceof IStructuredSelection) || (selection == null)) {
			wizard.init(page.getWorkbenchWindow().getWorkbench(), (IStructuredSelection) selection);
		}

		// Instantiates the wizard container with the wizard and opens it
		WizardDialog dialog = new WizardDialog(page.getActivePart().getSite().getShell(), wizard);
		dialog.create();
		dialog.open();

		return null;
	}

}
