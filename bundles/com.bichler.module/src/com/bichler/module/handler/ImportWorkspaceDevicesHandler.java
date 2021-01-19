package com.bichler.module.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.device.core.view.OPCUADeviceView;
import com.bichler.module.wizard.ImportWorkspaceDevicesWizard;

public class ImportWorkspaceDevicesHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ImportWorkspaceDevicesWizard wizard = new ImportWorkspaceDevicesWizard();
		WizardDialog dialog = new WizardDialog(HandlerUtil.getActiveShell(event), wizard);
		int open = dialog.open();
		if (WizardDialog.OK != open) {
			return null;
		}

		OPCUADeviceView deviceView = (OPCUADeviceView) HandlerUtil.getActiveWorkbenchWindow(event).getActivePage()
				.findView(OPCUADeviceView.ID);
		deviceView.refreshViewer();

		return null;
	}

}
