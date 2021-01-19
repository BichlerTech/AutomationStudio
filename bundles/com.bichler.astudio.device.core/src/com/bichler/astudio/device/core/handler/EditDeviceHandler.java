package com.bichler.astudio.device.core.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.osgi.service.prefs.Preferences;

import com.bichler.astudio.device.core.view.OPCUADeviceView;

public class EditDeviceHandler extends AbstractHandler {

	public static final String ID = "com.bichler.astudio.device.core.edit";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		Preferences obj = (Preferences) ((IStructuredSelection) selection)
				.getFirstElement();

		String deviceViewId = HandlerUtil.getActivePartId(event);
		OPCUADeviceView deviceView = (OPCUADeviceView) HandlerUtil
				.getActiveWorkbenchWindow(event).getActivePage()
				.findView(deviceViewId);
		deviceView.editDevice(obj);
		
		return null;
	}

}
