package com.bichler.astudio.opcua.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.opcua.navigation.views.OPCNavigationView;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerDriverModelNode;
import com.bichler.astudio.opcua.wizard.rename.RenameWizard;

public class RenameHandler extends AbstractHandler {

	public static final String ID = "com.bichler.astudio.opcua.rename";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		if (window == null) {
			return null;
		}
		IWorkbenchPage page = window.getActivePage();
		if (page == null) {
			return null;
		}

		IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelectionChecked(event);
		Object fe = selection.getFirstElement();

		rename(page, fe, OPCUAServerDriverModelNode.class);
		return null;
	}

	private void rename(IWorkbenchPage page, Object fe, Class<OPCUAServerDriverModelNode> class1) {

		if (fe instanceof OPCUAServerDriverModelNode) {
			// perform change
			RenameWizard wizard = new RenameWizard(((OPCUAServerDriverModelNode) fe).getDriverName());
			WizardDialog dialog = new WizardDialog(page.getActivePart().getSite().getShell(), wizard);
			if (WizardDialog.OK == dialog.open()) {
				String newName = wizard.getRename();

				OPCNavigationView view = (OPCNavigationView) page.findView(OPCNavigationView.ID);
				((OPCUAServerDriverModelNode) fe).rename(newName);
				((OPCUAServerDriverModelNode) fe).setDriverName(newName);
				for (Object obj : ((OPCUAServerDriverModelNode) fe).getChildren()) {
					if (obj instanceof OPCUAServerDriverModelNode) {
						((OPCUAServerDriverModelNode) obj).setDriverName(newName);
					}
				}
				view.update((StudioModelNode) fe);
			}
		}
	}
}
