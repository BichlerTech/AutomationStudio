package com.bichler.astudio.opcua.handlers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.licensemanagement.LicManActivator;
import com.bichler.astudio.licensemanagement.exception.ASStudioLicenseException;
import com.bichler.astudio.navigation.views.NavigationView;
import com.bichler.astudio.opcua.nodes.OPCUAServerModelNode;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.opcua.OPCUAActivator;

public class DeleteOPCUAServerHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
		TreeSelection selection = (TreeSelection) HandlerUtil.getCurrentSelection(event);
		final Object selectedNode = selection.getFirstElement();
		if (selectedNode instanceof OPCUAServerModelNode) {
			// final IPreferenceStore store = OPCUAActivator.getDefault()
			// .getPreferenceStore();
			final IFileSystem filesystem = ((OPCUAServerModelNode) selectedNode).getFilesystem();
			// final OPCUAServerModelNode server = (OPCUAServerModelNode)
			// selectedNode;

			// Do you want to delete the opc ua server and all its
			// configuration?
			MessageDialog dia = new MessageDialog(page.getActivePart().getSite().getShell(),
					CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "handler.message.deleteserver"),
					null,
					CustomString.getString(
							OPCUAActivator.getDefault().RESOURCE_BUNDLE, "handler.message.deleteserver.confirm"),
					MessageDialog.QUESTION,
					new String[] {
							CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.yes"),
							CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.no") },
					0);

			int result = dia.open();
			if (result == Dialog.OK) {
				ProgressMonitorDialog dialog = new ProgressMonitorDialog(page.getActivePart().getSite().getShell());

				// ProgressMonitorDialog dialog = new ProgressMonitorDialog(page
				// .getActivePart().getSite().getShell());

//				try {
//					LicManActivator.getDefault().getLicenseManager().getLicense().validateDeleteOpcuaServer(1,false);
				try {
					dialog.run(true, true, new IRunnableWithProgress() {
						public void run(IProgressMonitor monitor) {
							monitor.beginTask(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
									"handler.message.deleteserver") + "...", IProgressMonitor.UNKNOWN);
							try {
								String serverDir = filesystem.getRootPath();
								if (filesystem.isDir(serverDir)) {
									try {
										filesystem.removeDir(serverDir, true);
									} catch (FileNotFoundException e) {
										e.printStackTrace();
									} catch (IOException e) {
										e.printStackTrace();
									}

									final NavigationView view = (NavigationView) page.findView(NavigationView.ID);

									HandlerUtil.getActiveShell(event).getDisplay().asyncExec(new Runnable() {

										@Override
										public void run() {
											((OPCUAServerModelNode) selectedNode).getParent().setChildren(null);

											view.refresh(((OPCUAServerModelNode) selectedNode).getParent());

										}
									});
								}
							} finally {
								monitor.done();
							}
						}
					});
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
//				} catch (HBStudioLicenseException e1) {
//					// should never happen
//					e1.printStackTrace();
//				}
			}
		}
		return null;
	}
}
