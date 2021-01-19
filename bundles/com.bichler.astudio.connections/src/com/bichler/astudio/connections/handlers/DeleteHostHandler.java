package com.bichler.astudio.connections.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.connections.ConnectionsActivator;
import com.bichler.astudio.connections.nodes.HostConnectionModelNode;
import com.bichler.astudio.connections.nodes.HostConnectionsModelNode;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.navigation.views.NavigationView;
import com.bichler.astudio.utils.internationalization.CustomString;

public class DeleteHostHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();

		TreeSelection selection = (TreeSelection) HandlerUtil.getActiveWorkbenchWindow(event).getSelectionService()
				.getSelection(NavigationView.ID);

		MessageDialog dia = new MessageDialog(page.getActivePart().getSite().getShell(),
				CustomString.getString(ConnectionsActivator.getDefault().RESOURCE_BUNDLE,
						"DeleteHost.Monitor.Delete.Title"),
				null, CustomString.getString(ConnectionsActivator.getDefault().RESOURCE_BUNDLE,
						"DeleteHost.Monitor.Delete.Message"), MessageDialog.QUESTION,
				new String[] { CustomString.getString(ConnectionsActivator.getDefault().RESOURCE_BUNDLE,
						"Default.Yes"), CustomString.getString(ConnectionsActivator.getDefault().RESOURCE_BUNDLE,
								"Default.No") }, 0);

		int result = dia.open();
		if (result == Dialog.OK) {

			StudioModelNode node = (StudioModelNode) selection.getFirstElement();

			if (node instanceof HostConnectionModelNode) {
				StudioModelNode parent = node.getParent();
				((HostConnectionsModelNode) parent).getConnectionsManager().getStudioConnections()
						.removeConnection(((HostConnectionModelNode) node).getFilesystem().getConnectionName());

				// if (node instanceof OPCUARootModelNode) {

				// }
				((HostConnectionsModelNode) ((HostConnectionModelNode) node).getParent()).getConnectionsManager()
						.exportHosts();
			}
			// NavigationView view = (NavigationView) page
			// .findView(NavigationView.ID);
		}
		return null;
	}

}
