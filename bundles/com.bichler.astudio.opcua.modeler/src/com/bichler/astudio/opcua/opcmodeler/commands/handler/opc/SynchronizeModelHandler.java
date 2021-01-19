package com.bichler.astudio.opcua.opcmodeler.commands.handler.opc;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;

public class SynchronizeModelHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IStructuredSelection treeSelection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
		BrowserModelNode selection = (BrowserModelNode) treeSelection.getFirstElement();
		return null;
	}
}
