package com.bichler.astudio.opcua.opcmodeler.commands.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.opcua.opcmodeler.views.namespacebrowser.NamespaceModelView;
import com.bichler.astudio.opcua.opcmodeler.wizards.search.FindAllNamespaceNodesWizard;

public class FindAllNamespaceNodesHandler extends AbstractHandler {
	public static final String ID = "com.hbsoft.comet.opcmodeler.findallnamespacenodes";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPart part = HandlerUtil.getActivePart(event);
		if (!(part instanceof NamespaceModelView)) {
			return null;
		}
		FindAllNamespaceNodesWizard wizard = new FindAllNamespaceNodesWizard();
		WizardDialog dialog = new WizardDialog(HandlerUtil.getActiveShell(event), wizard);
		int open = dialog.open();
		if (WizardDialog.OK == open) {
			String namespace = wizard.getNamespace();
			((NamespaceModelView) part).setNamespaceUris(namespace);
		}
		return null;
	}
}
