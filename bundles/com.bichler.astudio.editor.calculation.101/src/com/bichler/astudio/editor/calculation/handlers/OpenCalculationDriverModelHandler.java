package com.bichler.astudio.editor.calculation.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.bichler.astudio.opcua.handlers.AbstractOPCOpenDriverModelHandler;
import com.bichler.astudio.view.drivermodel.handler.util.DriverBrowserUtil;

public class OpenCalculationDriverModelHandler extends AbstractOPCOpenDriverModelHandler {

	public static final String ID = "com.bichler.astudio.editor.opendrivermodel.calculation";
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
//		OpenDriverModelBrowserEvent trigger = (OpenDriverModelBrowserEvent) event
//				.getTrigger();
		DriverBrowserUtil.openEmptyDriverModelView();
		return null;
	}

}
