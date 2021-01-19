package com.bichler.astudio.device.core.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.device.core.view.OPCUADeviceView;

public class AddDeviceHandler extends AbstractHandler {

	public static final String ID = "com.bichler.astudio.device.core.add";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String deviceViewId = HandlerUtil.getActivePartId(event);
		OPCUADeviceView deviceView = (OPCUADeviceView) HandlerUtil
				.getActiveWorkbenchWindow(event).getActivePage()
				.findView(deviceViewId);

		deviceView.addNewDevice(false);

		return null;
	}

}
