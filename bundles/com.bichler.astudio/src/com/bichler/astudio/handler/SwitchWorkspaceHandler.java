package com.bichler.astudio.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.bichler.astudio.ASActivator;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.utils.ui.dialogs.PickWorkspaceDialog;


public class SwitchWorkspaceHandler extends AbstractHandler {

	public static final String ID = "com.bichler.astudio.command.switchworkspace";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		PickWorkspaceDialog pwd = new PickWorkspaceDialog(true, null);
		int pick = pwd.open();
		if (pick == Dialog.CANCEL) {
			return null;
		}
		if (pwd.hasChanged()) {
			MessageDialog.openInformation(Display.getDefault().getActiveShell(),
					CustomString.getString(ASActivator.getDefault().RESOURCE_BUNDLE,
							"com.bichler.astudio.dialog.pickworkspace.title.switch"),
					CustomString.getString(ASActivator.getDefault().RESOURCE_BUNDLE,
							"com.bichler.astudio.dialog.switchworkspace.description"));

			// restart client
			PlatformUI.getWorkbench().restart();
		}
		return pick;
	}

}
