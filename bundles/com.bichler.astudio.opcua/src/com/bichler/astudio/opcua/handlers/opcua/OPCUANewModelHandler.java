package com.bichler.astudio.opcua.handlers.opcua;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.opcua.navigation.views.OPCNavigationView;
import com.bichler.astudio.opcua.nodes.OPCUAServerModelNode;

public class OPCUANewModelHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);

		OPCNavigationView view = (OPCNavigationView) window.getActivePage().findView(OPCNavigationView.ID);

		if (view == null) {
			return null;
		}

		TreeViewer viewer = view.getViewer();
		if (viewer.getInput() == null) {
			return null;
		}
		OPCUAServerModelNode input = (OPCUAServerModelNode) viewer.getInput();

		OPCUAUtil.resetModel(window, view, input);

		return null;
	}
}
