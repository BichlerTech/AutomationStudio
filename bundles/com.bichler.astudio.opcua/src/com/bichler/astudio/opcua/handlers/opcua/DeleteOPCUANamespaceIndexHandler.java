package com.bichler.astudio.opcua.handlers.opcua;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.DeleteNodesItem;

import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.ModelBrowserView;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.opcua.nodes.OPCUAServerInfoModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerInfoModelsNode;
import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.navigation.views.OPCNavigationView;

public class DeleteOPCUANamespaceIndexHandler extends AbstractHandler {
	public static final String ID = "com.bichler.astudio.commands.deletenamespaceindex";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		// add namespace index to server uri
		final NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();

		String description = CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				"handler.message.deletenamespace.confirm");

		IStructuredSelection selection = (IStructuredSelection) window.getActivePage().getSelection();

		OPCUAServerInfoModelNode nsIndex = (OPCUAServerInfoModelNode) selection.getFirstElement();
		String name = nsIndex.getInfModelName();

		int index = nsTable.getIndex(name);
		if (index < 0) {
			return null;
		}

		NodeId[] nodes = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager().getAllNodeIds(index);

		if (nodes.length > 0) {
			description += " " + CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
					"handler.message.deletenamespace.extend");
		}

		boolean confirm = MessageDialog.openConfirm(HandlerUtil.getActiveShell(event),
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "handler.message.deletenamespace"),
				description);

		if (confirm) {
			// remove referenced nodes for the namespace index
			if (nodes.length > 0) {
				DeleteNodesItem[] nodesToDelete = new DeleteNodesItem[nodes.length];
				for (int i = 0; i < nodes.length; i++) {
					nodesToDelete[i] = new DeleteNodesItem();
					nodesToDelete[i].setDeleteTargetReferences(true);
					nodesToDelete[i].setNodeId(nodes[i]);
				}

				try {
					ServerInstance.getInstance().getServerInstance().getMaster().deleteNodes(nodesToDelete, null);
					// refresh opc navigation view
					ModelBrowserView view = (ModelBrowserView) window.getActivePage().findView(ModelBrowserView.ID);

					if (view == null) {
						return null;
					}
					view.setDirty(true);
					view.refresh();

				} catch (ServiceResultException e) {
					e.printStackTrace();
				}
			}
			// remove namespace index
			nsTable.remove(index);
		}

		// refresh opc navigation view
		OPCNavigationView view = (OPCNavigationView) window.getActivePage().findView(OPCNavigationView.ID);

		if (view == null) {
			return null;
		}
		view.refresh(OPCUAServerInfoModelsNode.class, false);

		return null;
	}
}
