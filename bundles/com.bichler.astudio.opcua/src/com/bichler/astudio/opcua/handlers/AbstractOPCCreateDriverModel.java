package com.bichler.astudio.opcua.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.expressions.IEvaluationContext;

import com.bichler.astudio.opcua.events.CreateOPCUADriverParameter;

public abstract class AbstractOPCCreateDriverModel extends AbstractHandler {
	public static final String PARAMETER_ID = "opcuacreatedriver";

	protected CreateOPCUADriverParameter getCommandParameter(ExecutionEvent event) {
		IEvaluationContext evalCxt = (IEvaluationContext) event.getApplicationContext();
		return (CreateOPCUADriverParameter) evalCxt.getVariable(PARAMETER_ID);
	}
}
