package com.bichler.astudio.opcua.handlers.opcua;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.core.user.util.UserUtils;
import com.bichler.astudio.opcua.preferences.OPCUA_Path_Selection;

public class OpenOPCUAPreferenceHandler extends AbstractHandler {

	public static final String ID = "com.bichler.astudio.opcua.openPreferences";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow workbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);

		if (workbenchWindow == null) {
			// action has been dispose
			return null;
		}
		boolean allow = UserUtils.askPreferenceDialog(1);

		if (allow) {
			PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(HandlerUtil.getActiveShell(event),
					OPCUA_Path_Selection.ID, null, null);
			int open = dialog.open();
		}
		return null;
	}

}
