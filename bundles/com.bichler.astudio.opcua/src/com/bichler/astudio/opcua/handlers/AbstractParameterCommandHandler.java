package com.bichler.astudio.opcua.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.expressions.IEvaluationContext;

public abstract class AbstractParameterCommandHandler extends AbstractHandler {

	protected Object getCommandParameter(ExecutionEvent event, String id) {
		IEvaluationContext evalCxt = (IEvaluationContext) event.getApplicationContext();
		return evalCxt.getVariable(id);
	}
}
