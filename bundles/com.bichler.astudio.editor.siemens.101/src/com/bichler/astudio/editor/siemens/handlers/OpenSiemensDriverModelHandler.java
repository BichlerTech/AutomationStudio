package com.bichler.astudio.editor.siemens.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.bichler.astudio.editor.siemens.datenbaustein.SiemensDBResourceManager;
import com.bichler.astudio.editor.siemens.datenbaustein.SiemensResources;
import com.bichler.astudio.editor.siemens.driver.SiemensDriverUtil;
import com.bichler.astudio.opcua.events.OpenDriverModelBrowserParameter;
import com.bichler.astudio.opcua.handlers.AbstractOPCOpenDriverModelHandler;

public class OpenSiemensDriverModelHandler extends AbstractOPCOpenDriverModelHandler {
	public static final String ID = "com.bichler.astudio.editor.opendrivermodel.siemens";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		OpenDriverModelBrowserParameter trigger = getCommandParameter(event);
		// (OpenDriverModelBrowserParameter) event
		// .getTrigger();
		SiemensDBResourceManager structManager = SiemensResources.getInstance()
				.getDBResourceManager(trigger.getDrivername());
		SiemensDriverUtil.openDriverView(trigger.getFilesystem(), trigger.getDriverConfigPath(), structManager);
		return null;
	}
}
