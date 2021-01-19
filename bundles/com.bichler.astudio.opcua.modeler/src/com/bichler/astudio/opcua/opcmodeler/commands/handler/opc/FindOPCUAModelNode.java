package com.bichler.astudio.opcua.opcmodeler.commands.handler.opc;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.opcua.opcmodeler.wizards.search.SearchOPCUANodeWizard;

public class FindOPCUAModelNode extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		SearchOPCUANodeWizard wizard = new SearchOPCUANodeWizard();
		WizardDialog dialog = new WizardDialog(HandlerUtil.getActiveShell(event), wizard);
		int open = dialog.open();
		return null;
	}
}
