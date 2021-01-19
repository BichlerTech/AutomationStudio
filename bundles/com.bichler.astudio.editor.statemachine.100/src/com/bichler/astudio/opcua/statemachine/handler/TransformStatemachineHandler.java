package com.bichler.astudio.opcua.statemachine.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.opcua.statemachine.wizard.CreateStateMachineWizard;

public class TransformStatemachineHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		CreateStateMachineWizard wizard = new CreateStateMachineWizard();
		WizardDialog dialog = new WizardDialog(HandlerUtil.getActiveShell(event), wizard);
		int open = dialog.open();

		if (WizardDialog.OK == open) {
//			MessageDialog.openInformation(HandlerUtil.getActiveShell(event), "Transform UML Statemachine",
//					"Successful, statemachine has been exported!");
		}

		return null;
	}

}
