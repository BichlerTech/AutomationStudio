package com.bichler.astudio.opcua.handlers;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.navigation.views.IFileSystemNavigator;
import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.navigation.views.OPCNavigationView;
import com.bichler.astudio.opcua.nodes.OPCUAServerVersionModelNode;
import com.bichler.astudio.utils.internationalization.CustomString;

public class DeleteOPCUAServerVersionHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
		MessageDialog dia = new MessageDialog(page.getActivePart().getSite().getShell(),
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "handler.message.deleteversion"),
				null,
				CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
						"handler.message.deleteversion.confirm"),
				MessageDialog.QUESTION,
				new String[] { CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.yes"),
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "editor.form.no") },
				0);
		int result = dia.open();
		if (result != Dialog.OK) {
			return null;
		}
		TreeSelection selection = (TreeSelection) HandlerUtil.getCurrentSelection(event);
		Object selectedNode = selection.getFirstElement();
		if (selectedNode instanceof OPCUAServerVersionModelNode) {
			OPCUAServerVersionModelNode node = (OPCUAServerVersionModelNode) selectedNode;
			String versionName = node.getVersionName();
			String filepath = new Path(node.getFilesystem().getRootPath()).append("versions").append(versionName)
					.toOSString();
			if (node.getFilesystem().isFile(filepath)) {
				try {
					node.getFilesystem().removeFile(filepath);
					IFileSystemNavigator view = (IFileSystemNavigator) page.findView(OPCNavigationView.ID);
					view.refresh((StudioModelNode) node.getParent());
				} catch (IOException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
				}
			}
		}
		return null;
	}
}
