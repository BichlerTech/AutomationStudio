package com.bichler.astudio.opcua.statemachine.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.handlers.HandlerUtil;
import com.bichler.astudio.opcua.statemachine.wizard.ReverseEngineModelNamespaceWizard;

public class UMLReverseEngineeringNamespaceHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ReverseEngineModelNamespaceWizard wizard = new ReverseEngineModelNamespaceWizard();
		WizardDialog dialog = new WizardDialog(HandlerUtil.getActiveShell(event), wizard);

		int open = dialog.open();
		if (WizardDialog.OK == open) {
//			MessageDialog.openInformation(HandlerUtil.getActiveShell(event), "Reverse engine UML Statemachine",
//					"Successful, OPC UA UML type model has been exported!");
		}

		return null;
	}

}
