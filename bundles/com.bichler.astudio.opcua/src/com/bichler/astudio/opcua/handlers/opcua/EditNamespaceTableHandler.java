package com.bichler.astudio.opcua.handlers.opcua;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.opcua.nodes.OPCUAServerInfoModelsNode;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.navigation.views.OPCNavigationView;

public class EditNamespaceTableHandler extends AbstractHandler {

	public static final String ID = "com.bichler.astudio.opcua.editnamespaces";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);

		if (window == null) {
			return null;
		}

		IWorkbenchPage page = window.getActivePage();

		if (page == null) {
			return null;
		}

		// save all editors and opc ua information model

		boolean confirm = true;
		// check if something is dirty
		if (OPCUAUtil.isOPCDirty(page)) {
			confirm = MessageDialog.openConfirm(HandlerUtil.getActiveShell(event),
					CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "handler.message.refactor"),
					CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
							"handler.message.refactor.message"));
		}

		if (confirm) {
			OPCUAUtil.doSaveAll(page);
			OPCUAUtil.editNamespaceTable(window);
			OPCNavigationView view = (OPCNavigationView) window.getActivePage().findView(OPCNavigationView.ID);

			if (view == null) {
				return null;
			}
			view.refresh(OPCUAServerInfoModelsNode.class, false);
			// do not autosave namespace changes
			// OPCUAUtil.doSaveAll(page);
		}
		return null;
	}

}
