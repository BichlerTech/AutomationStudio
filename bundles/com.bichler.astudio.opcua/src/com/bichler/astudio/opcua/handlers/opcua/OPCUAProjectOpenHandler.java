package com.bichler.astudio.opcua.handlers.opcua;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.opcua.nodes.OPCUAServerModelNode;

public class OPCUAProjectOpenHandler extends AbstractHandler {

	public static final String ID = "com.bichler.astudio.commands.OpenOPCUAServerProject";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
		if (selection == null) {
			return null;
		}
		final OPCUAServerModelNode node = (OPCUAServerModelNode) selection.getFirstElement();

		if (node == null) {
			return null;
		}
		final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

		OPCUAUtil.openProjectPerspective(window, node);

		return null;
	}

}
