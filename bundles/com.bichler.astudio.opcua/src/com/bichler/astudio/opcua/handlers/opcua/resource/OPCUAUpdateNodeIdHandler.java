package com.bichler.astudio.opcua.handlers.opcua.resource;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.IHandlerService;

import com.bichler.astudio.opcua.opcmodeler.commands.OPCUAUpdateNodeIdEvent;
import com.bichler.astudio.opcua.opcmodeler.commands.handler.extern.UpdateExternNodeIdHandler;
import com.bichler.astudio.opcua.navigation.views.OPCNavigationView;
import com.bichler.astudio.opcua.driver.OPCDriverUtil;
import com.bichler.astudio.opcua.events.OPCUAUpdateNodeIdDriverParameter;
import com.bichler.astudio.opcua.handlers.opcua.OPCUAUtil;
import com.bichler.astudio.opcua.opcmodeler.singletons.type.INamespaceTableChange;
import com.bichler.astudio.opcua.nodes.OPCUAServerDriverModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerModelNode;
import com.bichler.astudio.opcua.nosql.userauthority.handler.UpdateNodeIdUserAccessHandler;

public class OPCUAUpdateNodeIdHandler extends AbstractHandler {
	public static final String ID = "com.bichler.astudio.opcua.update.nodeid";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
		if (page == null) {
			return null;
		}
		OPCNavigationView view = (OPCNavigationView) page.findView(OPCNavigationView.ID);
		if (view == null) {
			return null;
		}
		// prepare eclipse command framework
		OPCUAUpdateNodeIdEvent trigger = getCommandParameter(event);
		// (OPCUAUpdateNodeIdEvent) event .getTrigger();
		/* call select command */
		ICommandService commandService = (ICommandService) page.getWorkbenchWindow().getService(ICommandService.class);
		IHandlerService handlerService = (IHandlerService) page.getWorkbenchWindow().getService(IHandlerService.class);
		// server navigation item
		OPCUAServerModelNode selectedElement = (OPCUAServerModelNode) view.getViewer().getInput();
		/**
		 * update user access db
		 */
		{
			String command = UpdateNodeIdUserAccessHandler.ID;
			Command updateUserAccess = commandService.getCommand(command);
			// From a view you get the site which allow to get the service
			ExecutionEvent executionUpdateUserAccess = handlerService.createExecutionEvent(updateUserAccess, null);
			IEvaluationContext evalCtx = (IEvaluationContext) event.getApplicationContext();
			evalCtx.addVariable(UpdateExternNodeIdHandler.PARAMETER_ID, trigger);
			try {
				updateUserAccess.executeWithChecks(executionUpdateUserAccess);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		/**
		 * update driver
		 */
		Object[] drivers = OPCDriverUtil.getAllDriverNamesFromOPCProject(selectedElement);
		if (drivers.length > 0) {
			for (Object driver : drivers) {
				String type = ((OPCUAServerDriverModelNode) driver).getDriverType();
				String version = ((OPCUAServerDriverModelNode) driver).getDriverVersion();
				String command = "command.update.opcua.nodeid.driver." + type + "." + version;
				Command openDriverDPCmd = commandService.getCommand(command);
				OPCUAUpdateNodeIdDriverParameter driverTrigger = new OPCUAUpdateNodeIdDriverParameter(trigger,
						(OPCUAServerDriverModelNode) driver);
				// From a view you get the site which allow to get the service
				ExecutionEvent executionOpenDriverDPEvent = handlerService.createExecutionEvent(openDriverDPCmd, null);
				IEvaluationContext evalCtx = (IEvaluationContext) event.getApplicationContext();
				evalCtx.addVariable(OPCUAUpdateNodeIdDriverParameter.PARAMETER_ID, driverTrigger);
				try {
					openDriverDPCmd.executeWithChecks(executionOpenDriverDPEvent);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		// TODO:
		// refresh open editors
		// IEditorReference[] editors = page.getEditorReferences();
		// for (IEditorReference editor : editors) {
		// if (editor instanceof INamespaceTableChange) {
		// ((INamespaceTableChange) editor).onNamespaceChange(trigger);
		// }
		// }
		// }
		OPCUAUtil.validateOPCUAPerspective();
		return null;
	}

	protected OPCUAUpdateNodeIdEvent getCommandParameter(ExecutionEvent event) {
		IEvaluationContext evalCxt = (IEvaluationContext) event.getApplicationContext();
		OPCUAUpdateNodeIdEvent trigger = (OPCUAUpdateNodeIdEvent) evalCxt
				.getVariable(UpdateExternNodeIdHandler.PARAMETER_ID);
		return trigger;
	}
}
