package com.bichler.astudio.opcua.opcmodeler.commands.handler.clipboard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.ModelBrowserView;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;

import opc.sdk.core.node.Node;

public abstract class AbstractClipboardHandler extends AbstractHandler {
	/**
	 * 
	 * @param isCopy True is copy, False is cut!
	 * @return
	 */
	Object copy(IStructuredSelection selection, boolean isCopy) {
		List<BrowserModelNode> nodeList = getSelectedNodes(selection);
		Clipboard clipboard = new Clipboard(Display.getDefault());
		TextTransfer transfer = TextTransfer.getInstance();
		StringBuilder builder = new StringBuilder();
		for (BrowserModelNode node : nodeList) {
			builder.append(nodeToString(node, isCopy));
		}
		clipboard.setContents(new Object[] { builder.toString() }, new Transfer[] { transfer });
		return null;
	}

	IStructuredSelection getSelectionFromView() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		IViewPart view = page.findView(ModelBrowserView.ID);
		return (IStructuredSelection) view.getSite().getSelectionProvider().getSelection();
	}

	private List<BrowserModelNode> getSelectedNodes(IStructuredSelection selection) {
		List<BrowserModelNode> nodeList = new ArrayList<BrowserModelNode>();
		for (Iterator<BrowserModelNode> iterator = selection.iterator(); iterator.hasNext();) {
			BrowserModelNode viewNode = iterator.next();
			nodeList.add(viewNode);
		}
		return nodeList;
	}

	/**
	 * 
	 * @param modelNode
	 * @param isCopy    True if it is copy, False if it is cut.
	 * @return
	 */
	private String nodeToString(BrowserModelNode modelNode, boolean isCopy) {
		Node node = modelNode.getNode();
		StringBuilder builder = new StringBuilder();
		builder.append(node.getNodeId().toString());
		builder.append("\t");
		builder.append(node.getNodeClass().getValue());
		builder.append("\t");
		builder.append(isCopy);
		builder.append("\n");
		return builder.toString();
	}
}
