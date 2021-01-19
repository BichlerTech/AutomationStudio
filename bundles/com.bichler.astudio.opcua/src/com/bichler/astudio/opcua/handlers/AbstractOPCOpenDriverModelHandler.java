package com.bichler.astudio.opcua.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.expressions.IEvaluationContext;

import com.bichler.astudio.opcua.events.OpenDriverModelBrowserParameter;

public abstract class AbstractOPCOpenDriverModelHandler extends AbstractHandler {

	public static final String PARAMETER_ID = "opendrivermodelbrowser";

	protected OpenDriverModelBrowserParameter getCommandParameter(ExecutionEvent event) {
		IEvaluationContext evalCxt = (IEvaluationContext) event.getApplicationContext();
		OpenDriverModelBrowserParameter trigger = (OpenDriverModelBrowserParameter) evalCxt.getVariable(PARAMETER_ID);
		return trigger;
	}
}
