package com.bichler.astudio.editor.aggregated.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.bichler.astudio.editor.aggregated.driver.AggregatedDriverUtil;
import com.bichler.astudio.opcua.events.OpenDriverModelBrowserParameter;
import com.bichler.astudio.opcua.handlers.AbstractOPCOpenDriverModelHandler;

public class OpenAggregatedDriverModelHandler extends
		AbstractOPCOpenDriverModelHandler {

	public static final String ID = "com.bichler.astudio.editor.opendrivermodel.aggregated";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		OpenDriverModelBrowserParameter trigger = getCommandParameter(event);

		trigger = (OpenDriverModelBrowserParameter) event.getTrigger();
		AggregatedDriverUtil.openDriverView(trigger.getFilesystem(),
				trigger.getDriverConfigPath(), trigger.getDriverPath());
		return null;
	}

}
