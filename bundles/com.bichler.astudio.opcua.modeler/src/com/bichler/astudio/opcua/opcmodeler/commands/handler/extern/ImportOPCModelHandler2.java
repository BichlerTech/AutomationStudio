package com.bichler.astudio.opcua.opcmodeler.commands.handler.extern;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.opcua.opcmodeler.utils.extern.DesignerUtils;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.load.ImportGeneratedIec2OpcWizard;

public class ImportOPCModelHandler2 extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ImportGeneratedIec2OpcWizard wizard = new ImportGeneratedIec2OpcWizard();
		Shell shell = HandlerUtil.getActiveShell(event);
		WizardDialog wizardDialog = new WizardDialog(shell, wizard);
		wizardDialog.create();
		Point defaultSize = wizardDialog.getShell().getSize();
		wizardDialog.getShell().setSize(Math.max(400, defaultSize.x), Math.max(600, defaultSize.y));
		wizardDialog.setBlockOnOpen(true);
		int ok = wizardDialog.open();
		if (ok == WizardDialog.OK) {
			DesignerUtils.refreshBrowserAll();
		}
		return null;
	}
}
