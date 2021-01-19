package com.bichler.astudio.opcua.handlers.opcua.resource;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.IHandlerService;

import com.bichler.astudio.opcua.navigation.views.OPCNavigationView;
import com.bichler.astudio.opcua.driver.OPCDriverUtil;
import com.bichler.astudio.opcua.handlers.events.OPCUAValidationDriverParameter;
import com.bichler.astudio.opcua.handlers.events.OPCUAValidationUserAccessParameter;
import com.bichler.astudio.opcua.nodes.OPCUAServerDriverModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerModelNode;
import com.bichler.astudio.opcua.nosql.userauthority.handler.ValidiateUserAccessHandler;

public class OPCUAValidationHandler extends AbstractHandler {
	/** Command handler ID */
	public static final String ID = "com.bichler.astudio.opcua.validate";
	/** Command parameter ID */
	public static final String PARAMETER_ID = "opcuavalidation";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		if (window == null) {
			return null;
		}
		IWorkbenchPage page = window.getActivePage();
		if (page == null) {
			return null;
		}
		OPCNavigationView view = (OPCNavigationView) page.findView(OPCNavigationView.ID);
		if (view == null) {
			return null;
		}
		// prepare eclipse command framework
		// NamespaceTableChangeEvent trigger = (NamespaceTableChangeEvent) event
		// .getTrigger();
		/* call select command */
		ICommandService commandService = (ICommandService) page.getWorkbenchWindow().getService(ICommandService.class);
		IHandlerService handlerService = (IHandlerService) page.getWorkbenchWindow().getService(IHandlerService.class);
		// server navigation item
		OPCUAServerModelNode selectedElement = (OPCUAServerModelNode) view.getViewer().getInput();
		/**
		 * update user access db
		 */
		{
			String command = ValidiateUserAccessHandler.ID;
			Command updateUserAccess = commandService.getCommand(command);
			// Parameterization[] parameterizations = new Parameterization[1];
			// IParameter parameter;
			// new Parameterization(parameter, value);
			// new ParameterizedCommand(command, parameterizations );
			OPCUAValidationUserAccessParameter evt = new OPCUAValidationUserAccessParameter();
			evt.setFilesystem(selectedElement.getFilesystem());
			// From a view you get the site which allow to get the service
			ExecutionEvent executionUpdateUserAccess = handlerService.createExecutionEvent(updateUserAccess, null);
			IEvaluationContext ac = (IEvaluationContext) event.getApplicationContext();
			ac.addVariable(PARAMETER_ID, evt);
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
				OPCUAServerDriverModelNode node = ((OPCUAServerDriverModelNode) driver);
				String type = node.getDriverType();
				String version = node.getDriverVersion();
				String drvName = node.getDriverName();
				String command = "com.bichler.astudio.editor." + type + "." + version + ".validate";
				Command openDriverDPCmd = commandService.getCommand(command);
				// update all children properties
				Object[] children = node.getChildren();
				for (Object child : children) {
					OPCUAValidationDriverParameter evt = new OPCUAValidationDriverParameter();
					evt.setDrvName(drvName);
					evt.setFilesystem(selectedElement.getFilesystem());
					evt.setModelNode(((OPCUAServerDriverModelNode) child));
					// From a view you get the site which allow to get the
					// service
					ExecutionEvent executionOpenDriverDPEvent = handlerService.createExecutionEvent(openDriverDPCmd,
							null);
					IEvaluationContext ac = (IEvaluationContext) event.getApplicationContext();
					ac.addVariable(PARAMETER_ID, evt);
					try {
						openDriverDPCmd.executeWithChecks(executionOpenDriverDPEvent);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}
		// refresh open editors
		IEditorReference[] editors = page.getEditorReferences();
		for (IEditorReference editor : editors) {
			// if (editor instanceof INamespaceTableChange) {
			// ((INamespaceTableChange) editor).onNamespaceChange(trigger);
			// }
		}
		return null;
	}

	public static void validateOPCPerspective() {
	}
}
