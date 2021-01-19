package com.bichler.astudio.navigation.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.components.ui.preferences.General_Path_Selection;
import com.bichler.astudio.core.user.util.UserUtils;

public class OpenGeneralPreferenceHandler extends AbstractHandler {

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
					General_Path_Selection.ID, null, null); 
			dialog.open();
		}
		return null;
	}

}
