package com.bichler.astudio.language.handler;

import java.util.Locale;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.language.LanguageActivator;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.utils.ui.dialogs.PickWorkspaceDialog;

public class ChangeLanguageHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Locale locale = LanguageUtil.openLanguageDialog();
		if (locale == null) {
			return null;
		}
		Locale productlanguage = PickWorkspaceDialog.getProductLanguage();
		if (productlanguage.equals(locale)) {
			MessageDialog.openError(HandlerUtil.getActiveShell(event),
					CustomString.getString(LanguageActivator.getDefault().RESOURCE_BUNDLE,
							"com.bichler.astudio.dialog.language.handler.error.title"),
					CustomString.getString(LanguageActivator.getDefault().RESOURCE_BUNDLE,
							"com.bichler.astudio.dialog.language.handler.error.nochange"));
			return null;
		}

		MessageDialog.openInformation(HandlerUtil.getActiveShell(event),
				CustomString.getString(LanguageActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.dialog.language.handler.error.title"),
				CustomString.getString(LanguageActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.dialog.language.handler.info.close"));

		LanguageUtil.setProductLanguage(locale);

		PlatformUI.getWorkbench().restart();

		return null;
	}

}
