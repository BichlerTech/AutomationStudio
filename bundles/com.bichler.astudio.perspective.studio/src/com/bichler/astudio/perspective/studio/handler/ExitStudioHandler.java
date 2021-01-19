package com.bichler.astudio.perspective.studio.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.handlers.HandlerUtil;

public class ExitStudioHandler extends AbstractHandler {

	public static final String ID = "com.bichler.astudio.command.exit";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		MessageDialog.openConfirm(HandlerUtil.getActiveShell(event), "Exit application",
				"Do you really want to exit application");

		IEvaluationContext context = (IEvaluationContext) event.getApplicationContext();
		IWorkbench workbench = (IWorkbench) context.getVariable(IWorkbench.class.getName());
		workbench.close();
		return null;
	}
}
