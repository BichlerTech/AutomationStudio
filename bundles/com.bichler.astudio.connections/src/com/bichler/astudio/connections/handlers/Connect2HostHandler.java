package com.bichler.astudio.connections.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.filesystem.SimpleFileSystem;
import com.bichler.astudio.connections.ConnectionsActivator;
import com.bichler.astudio.connections.nodes.HostConnectionModelNode;
import com.bichler.astudio.navigation.views.NavigationView;
import com.bichler.astudio.utils.internationalization.CustomString;

public class Connect2HostHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();

		TreeSelection selection = (TreeSelection) HandlerUtil.getActiveWorkbenchWindow(event).getSelectionService()
				.getSelection(NavigationView.ID);

		if (selection != null) {
			Object selectedNode = selection.getFirstElement();

			if (selectedNode != null && selectedNode instanceof HostConnectionModelNode) {

				final HostConnectionModelNode node = (HostConnectionModelNode) selectedNode;

				if (node.getFilesystem() instanceof SimpleFileSystem) {
					// we do nothing because we need not to connect to a host
					return null;
				}
				// final Display display = Display.getCurrent();
				ProgressMonitorDialog dialog = new ProgressMonitorDialog(HandlerUtil.getActiveShell(event));
				// ProgressMonitorDialog dialog = new ProgressMonitorDialog(
				// HandlerUtil.getActiveShell(event));
				try {
					dialog.run(true, true, new IRunnableWithProgress() {
						public void run(final IProgressMonitor monitor) {

							monitor.setTaskName(CustomString.getString(
									ConnectionsActivator.getDefault().RESOURCE_BUNDLE, "ConnectHost.Monitor.Connect"));
							monitor.beginTask(node.getFilesystem().getConnectionName() + " ("
									+ node.getFilesystem().getHostName() + ")", IProgressMonitor.UNKNOWN);

							if (node.getFilesystem().connect()) {
								node.setChildren(null);

								HandlerUtil.getActiveShell(event).getDisplay().asyncExec(new Runnable() {

									@Override
									public void run() {
										NavigationView view = (NavigationView) page.findView(NavigationView.ID);

										view.getViewer().refresh(node);
										monitor.done();
									}
								});
							}
						}

					});
				} catch (Exception ex) {
					// dialog.setReady(true);
				}
			}
		}
		return null;
	}

}
