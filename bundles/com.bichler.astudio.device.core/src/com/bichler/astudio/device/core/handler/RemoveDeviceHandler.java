package com.bichler.astudio.device.core.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.osgi.service.prefs.Preferences;

import com.bichler.astudio.device.core.DevCoreActivator;
import com.bichler.astudio.device.core.view.OPCUADeviceView;
import com.bichler.astudio.utils.internationalization.CustomString;

public class RemoveDeviceHandler extends AbstractHandler {

	public static final String ID = "com.bichler.astudio.device.core.remove";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		Preferences obj = (Preferences) ((IStructuredSelection) selection).getFirstElement();

		String deviceViewId = HandlerUtil.getActivePartId(event);
		OPCUADeviceView deviceView = (OPCUADeviceView) HandlerUtil.getActiveWorkbenchWindow(event).getActivePage()
				.findView(deviceViewId);

		boolean isYes = MessageDialog.openConfirm(HandlerUtil.getActiveShell(event),
				CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.device.opcua.handler.target.remove.dialog.title"),
				CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.device.opcua.handler.target.remove.dialog.message"));
		if (isYes) {
			deviceView.removeDevice(obj);
		}
		return null;
	}

}
