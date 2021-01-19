package com.bichler.astudio.opcua.statemachine.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.opcua.statemachine.wizard.ReverseEngineModelWizard;

public class UMLReverseEngineeringHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ReverseEngineModelWizard wizard = new ReverseEngineModelWizard();
		WizardDialog dialog = new WizardDialog(HandlerUtil.getActiveShell(event), wizard);

		int open = dialog.open();
		if (WizardDialog.OK == open) {
			MessageDialog.openInformation(HandlerUtil.getActiveShell(event), "Reverse engine UML Statemachine",
					"Successful, OPC UA UML type model has been exported!");
		}

		return null;
	}

}
