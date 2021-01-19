package com.bichler.astudio.opcua.javacommand.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.opcua.javacommand.preferences.JavaCommandPreferenceManager;
import com.bichler.astudio.opcua.javacommand.view.JavaCommandView;

public class RemoveCommandHandler extends AbstractHandler {

	public static final String ID = "com.bichler.astudio.opcua.javacommand.remove";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// AddCommandWizard wizard = new AddCommandWizard();

		if (!(HandlerUtil.getActivePart(event) instanceof JavaCommandView)) {
			return null;
		}

		JavaCommandView view = (JavaCommandView) HandlerUtil
				.getActivePart(event);

		IStructuredSelection selection = (IStructuredSelection) HandlerUtil
				.getCurrentSelection(event);
		if (selection == null || selection.isEmpty()) {
			return null;
		}

		// WizardDialog dialog = new WizardDialog(
		// HandlerUtil.getActiveShell(event), wizard);

		boolean confirm = MessageDialog
				.openConfirm(HandlerUtil.getActiveShell(event),
						"OPC UA Serverstartskript",
						"Wollen sie das ausgewählte Serverstartskript wirklich löschen?");

		// int open = dialog.open();
		// if (WizardDialog.OK == open) {
		if (confirm) {
			// String name = wizard.getSkriptname();
			// String description = wizard.getDescription();
			// String skript = wizard.getSkript();
			String preference = (String) selection.getFirstElement();
			// Preferences node =
			JavaCommandPreferenceManager.removeCommand(preference);
			view.refresh();
		}

		return null;
	}

}
