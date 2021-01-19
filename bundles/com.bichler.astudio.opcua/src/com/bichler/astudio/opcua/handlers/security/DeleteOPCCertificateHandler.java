package com.bichler.astudio.opcua.handlers.security;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.nodes.security.OPCUACertificateModelNode;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.navigation.views.OPCNavigationView;

public class DeleteOPCCertificateHandler extends AbstractHandler {

	public static final String ID = "com.bichler.astudio.commands.certificate.delete";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);

		if (selection == null) {
			return null;
		}

		OPCUACertificateModelNode node = (OPCUACertificateModelNode) selection.getFirstElement();
		String path = node.getCertPath();
		IFileSystem filesystem = node.getFilesystem();

		if (!filesystem.isFile(path)) {
			MessageDialog.openWarning(HandlerUtil.getActiveShell(event),
					CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "handler.message.filenotexist"),
					CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
							"handler.message.filenotexist.description") + path);
			return null;
		}

		// remove file
		try {
			filesystem.removeFile(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		if (window == null) {
			return null;
		}
		IWorkbenchPage page = window.getActivePage();
		if (page == null) {
			return null;
		}
		OPCNavigationView navigation = (OPCNavigationView) page.findView(OPCNavigationView.ID);

		if (navigation == null) {
			return null;
		}

		navigation.refresh(node.getParent());

		return null;
	}

}
