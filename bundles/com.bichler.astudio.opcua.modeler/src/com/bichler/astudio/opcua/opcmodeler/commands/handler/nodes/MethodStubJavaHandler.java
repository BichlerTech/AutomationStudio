package com.bichler.astudio.opcua.opcmodeler.commands.handler.nodes;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.ModelBrowserView;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.UAMethodNode;

public class MethodStubJavaHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		// get the selection (parent node)
		final TreeSelection selection = (TreeSelection) HandlerUtil.getActiveWorkbenchWindow(event)
				.getSelectionService().getSelection(ModelBrowserView.ID);
		Node selectedNode = ((BrowserModelNode) selection.getFirstElement()).getNode();
		if (selectedNode instanceof UAMethodNode) {
			System.out.println("// register method to internal drv connection interface ");
			System.out.println("RemoveParameterByUri rnode = new RemoveParameterByUri(null);");
			System.out.println("rnode.setNodeId(nid);");
			System.out.println("manager.getBaseManager().registerNodeState(rnode);");
			System.out.println();
			System.out.println("public void " + selectedNode.getBrowseName().getName() + "() {");
			System.out.println("\tto hier was;");
			System.out.println("}");
		}
		return null;
	}
}
