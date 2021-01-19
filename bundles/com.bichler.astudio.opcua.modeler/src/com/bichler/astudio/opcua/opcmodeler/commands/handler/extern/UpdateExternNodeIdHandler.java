package com.bichler.astudio.opcua.opcmodeler.commands.handler.extern;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.IHandlerService;

import com.bichler.astudio.opcua.opcmodeler.commands.OPCUAUpdateNodeIdEvent;

public class UpdateExternNodeIdHandler extends AbstractHandler {
	public static final String ID = "com.xcontrol.modeler.opc.extern.updatenodeid";
	public static final String PARAMETER_ID = "updateexternnodeid";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		if (window == null) {
			return null;
		}
		OPCUAUpdateNodeIdEvent trigger = getCommandParameter(event);
		IHandlerService handlerService = (IHandlerService) window.getService(IHandlerService.class);
		ICommandService cmdService = (ICommandService) window.getService(ICommandService.class);
		Command command = cmdService.getCommand("com.bichler.astudio.opcua.update.nodeid");
		ExecutionEvent updateNodeIdEvent = handlerService.createExecutionEvent(command, null);
		IEvaluationContext evalCtx = (IEvaluationContext) event.getApplicationContext();
		evalCtx.addVariable(PARAMETER_ID, trigger);
		try {
			command.executeWithChecks(updateNodeIdEvent);
			// handlerService.executeCommand(
			// "com.bichler.astudio.opcua.update.nodeid", trigger);
		} catch (NotDefinedException e) {
			e.printStackTrace();
		} catch (NotEnabledException e) {
			e.printStackTrace();
		} catch (NotHandledException e) {
			e.printStackTrace();
		}
		return null;
	}

	private OPCUAUpdateNodeIdEvent getCommandParameter(ExecutionEvent event) {
		IEvaluationContext evalCtx = (IEvaluationContext) event.getApplicationContext();
		return (OPCUAUpdateNodeIdEvent) evalCtx.getVariable(PARAMETER_ID);
	}
}
