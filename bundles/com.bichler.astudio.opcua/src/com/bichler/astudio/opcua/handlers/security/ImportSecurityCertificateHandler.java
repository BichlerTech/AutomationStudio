package com.bichler.astudio.opcua.handlers.security;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.opcua.navigation.views.OPCNavigationView;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.nodes.security.AbstractOPCUACertificateStoreModelNode;

public class ImportSecurityCertificateHandler extends AbstractHandler {

	public static final String ID = "com.bichler.astudio.commands.certificate.import";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
		if (selection == null) {
			return null;
		}

		AbstractOPCUACertificateStoreModelNode node = (AbstractOPCUACertificateStoreModelNode) selection
				.getFirstElement();
		IFileSystem filesystem = node.getFilesystem();

		SecurityUtil.importCertificateToCertificateStore(HandlerUtil.getActiveShell(event), filesystem,
				node.getCertStorePath());

		// refresh viewer
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

		navigation.refresh(node);

		return null;
	}

}
