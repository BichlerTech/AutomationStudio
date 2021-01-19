package com.bichler.astudio.opcua.opcmodeler;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {
	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	protected void makeActions(IWorkbenchWindow window) {
		IWorkbenchAction saveAction = ActionFactory.SAVE.create(window);
		register(saveAction);
		IWorkbenchAction buildAllAction = org.eclipse.ui.ide.IDEActionFactory.BUILD_AUTOMATICALLY.create(window);
		register(buildAllAction);
		IWorkbenchAction copyAction = ActionFactory.COPY.create(window);
		register(copyAction);
		// IWorkbenchAction deleteAction = ActionFactory.DELETE.create(window);
		// register(deleteAction);
		IWorkbenchAction pasteAction = ActionFactory.PASTE.create(window);
		register(pasteAction);
	}

	protected void fillMenuBar(IMenuManager menuBar) {
	}
}
