package com.bichler.astudio.opcua.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.expressions.IEvaluationContext;

import com.bichler.astudio.opcua.events.CreateOPCUAModuleParameter;

public abstract class AbstractOPCCreateModuleModel extends AbstractHandler {
	public static final String PARAMETER_ID = "opcuacreatemodule";

	protected CreateOPCUAModuleParameter getCommandParameter(ExecutionEvent event) {
		IEvaluationContext evalCxt = (IEvaluationContext) event.getApplicationContext();
		return (CreateOPCUAModuleParameter) evalCxt.getVariable(PARAMETER_ID);
	}
}
