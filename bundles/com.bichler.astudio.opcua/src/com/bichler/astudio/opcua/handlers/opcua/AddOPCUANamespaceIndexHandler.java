package com.bichler.astudio.opcua.handlers.opcua;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.opcfoundation.ua.common.NamespaceTable;

import com.bichler.astudio.opcua.nodes.OPCUAServerInfoModelsNode;
import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.opcua.navigation.views.OPCNavigationView;

public class AddOPCUANamespaceIndexHandler extends AbstractHandler {

	public static final String ID = "com.bichler.astudio.commands.addnamespaceindex";

	// TODO: SHOW Wizard or thisthing

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// add namespace index to server uri
		final NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();

		InputDialog dialog = new InputDialog(HandlerUtil.getActiveShell(event),
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "handler.message.addnamespace"),
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
						"handler.message.addnamespace.input"),
				"http://", new IInputValidator() {

					@Override
					public String isValid(String newText) {
						if (newText == null) {
							return CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
									"handler.message.namespace.empty");
						}

						int index = nsTable.getIndex(newText);
						if (index > -1) {
							return CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
									"handler.message.namespace.exist");
						}

						return null;
					}
				});

		int open = dialog.open();
		if (Dialog.OK == open) {
			String newNamespaceIndex = dialog.getValue();
			nsTable.add(newNamespaceIndex);
		}

		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);

		// refresh opc navigation view
		OPCNavigationView view = (OPCNavigationView) window.getActivePage().findView(OPCNavigationView.ID);

		if (view == null) {
			return null;
		}

		view.refresh(OPCUAServerInfoModelsNode.class, false);

		return null;
	}
}
