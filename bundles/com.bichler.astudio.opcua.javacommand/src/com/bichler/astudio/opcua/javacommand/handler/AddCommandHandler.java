package com.bichler.astudio.opcua.javacommand.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.opcua.javacommand.preferences.JavaCommandPreferenceManager;
import com.bichler.astudio.opcua.javacommand.view.JavaCommandView;
import com.bichler.astudio.opcua.javacommand.wizard.AddCommandWizard;

public class AddCommandHandler extends AbstractHandler {

	public static final String ID = "com.bichler.astudio.opcua.javacommand.add";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		AddCommandWizard wizard = new AddCommandWizard();

		if (!(HandlerUtil.getActivePart(event) instanceof JavaCommandView)) {
			return null;
		}

		JavaCommandView view = (JavaCommandView) HandlerUtil
				.getActivePart(event);

		WizardDialog dialog = new WizardDialog(
				HandlerUtil.getActiveShell(event), wizard);

		int open = dialog.open();
		if (WizardDialog.OK == open) {
			String name = wizard.getSkriptname();
			String description = wizard.getDescription();
			String skript = wizard.getSkript();

			// Preferences node =
			JavaCommandPreferenceManager.addCommand(name, description, skript);
			view.refresh();
		}

		return null;
	}

}
