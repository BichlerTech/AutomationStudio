package com.bichler.astudio.opcua.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.navigation.views.NavigationView;
import com.bichler.astudio.opcua.nodes.OPCUARootModelNode;

public class RefreshMenuHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// find selected node (root model node)
		ISelection selection = HandlerUtil.getCurrentSelectionChecked(event);
		if (selection.isEmpty()) {
			return null;
		}

		if (!(selection instanceof IStructuredSelection)) {
			return null;
		}
		
		Object navigationNode = ((IStructuredSelection) selection).getFirstElement();
		if (!(navigationNode instanceof OPCUARootModelNode)) {
			return null;
		}
		// empty node children
		((OPCUARootModelNode) navigationNode).refresh();
		((OPCUARootModelNode) navigationNode).setChildren(null);
		((OPCUARootModelNode) navigationNode).getChildren();

		// refresh project navigation view
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		if (window == null) {
			return null;
		}

		IWorkbenchPage page = window.getActivePage();
		if (page == null) {
			return null;
		}

		IViewPart view = page.findView(NavigationView.ID);
		if (view == null) {
			return null;
		}

		((NavigationView) view).refresh((OPCUARootModelNode) navigationNode);
		
		return null;
	}

}
