package com.bichler.astudio.log.view.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.log.view.Activator;
import com.bichler.astudio.log.view.preferences.ASLogPreferenceManager;
import com.bichler.astudio.log.view.viewer.ASLogView;

public class QuickSearchHandler extends AbstractHandler {

	public static final String ID = "com.hbsoft.comet.log4j.quicksearch";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IViewPart part = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage()
				.findView(ASLogView.ID);

		if (part != null) {
			ASLogPreferenceManager preferenceManager = Activator.getDefault().getPreferenceManager();
			boolean visible = !preferenceManager.isQuickSearchVisible();
			preferenceManager.storeQuickSearchState(visible);
		}
		return null;
	}

}
