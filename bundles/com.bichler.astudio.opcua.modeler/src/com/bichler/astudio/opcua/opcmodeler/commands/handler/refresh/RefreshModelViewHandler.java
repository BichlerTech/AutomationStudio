package com.bichler.astudio.opcua.opcmodeler.commands.handler.refresh;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.ModelBrowserView;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;

public class RefreshModelViewHandler extends AbstractHandler {
	public static String ID = "commands.designer.modelview.refresh";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
		IViewReference[] viewRefs = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getViewReferences();
		if (viewRefs != null) {
			for (IViewReference page : viewRefs) {
				if (page.getId().equals(ModelBrowserView.ID)) {
					ModelBrowserView viewRef = ((ModelBrowserView) page.getPart(true));
					if (selection != null && !selection.isEmpty()
							&& selection.getFirstElement() instanceof BrowserModelNode) {
						viewRef.refresh((BrowserModelNode) selection.getFirstElement());
					}
				}
			}
		}
		return null;
	}
}
