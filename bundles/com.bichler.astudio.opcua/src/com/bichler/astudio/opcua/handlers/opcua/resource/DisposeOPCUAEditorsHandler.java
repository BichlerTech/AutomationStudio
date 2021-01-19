package com.bichler.astudio.opcua.handlers.opcua.resource;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.IHandlerService;

import com.bichler.astudio.opcua.navigation.views.OPCNavigationView;
import com.bichler.astudio.opcua.nodes.OPCUAServerModelNode;

public class DisposeOPCUAEditorsHandler extends AbstractHandler {

	public static final String ID = "command.dispose.opcua.editor.driver.view";

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
		// OPCUAUpdateNodeIdEvent trigger = (OPCUAUpdateNodeIdEvent) event
		// .getTrigger();
		/* call select command */
		// ICommandService commandService = (ICommandService) page
		// .getWorkbenchWindow().getService(ICommandService.class);
		IHandlerService handlerService = (IHandlerService) page.getWorkbenchWindow().getService(IHandlerService.class);
		// server navigation item
		OPCUAServerModelNode selectedElement = (OPCUAServerModelNode) view.getViewer().getInput();
		/**
		 * update driver
		 */
		// Object[] drivers = OPCDriverUtil
		// .getAllDriverNamesFromOPCProject(selectedElement);
		// if (drivers.length > 0) {
		// for (Object driver : drivers) {
		// String type = ((OPCUAServerDriverModelNode) driver)
		// .getDriverType();
		String command = "com.bichler.astudio.view.drivermodel.opcua.dispose";

		try {
			handlerService.executeCommand(command, null);
		} catch (NotDefinedException e) {
			e.printStackTrace();
		} catch (NotEnabledException e) {
			e.printStackTrace();
		} catch (NotHandledException e) {
			e.printStackTrace();
		}

		// Command openDriverDPCmd = commandService.getCommand(command);
		//
		// OPCUAUpdateNodeIdDriverEvent driverTrigger = new
		// OPCUAUpdateNodeIdDriverEvent(
		// trigger, (OPCUAServerDriverModelNode) driver);
		//
		// // From a view you get the site which allow to get the
		// service
		// ExecutionEvent executionOpenDriverDPEvent = handlerService
		// .createExecutionEvent(openDriverDPCmd, driverTrigger);
		// try {
		// openDriverDPCmd
		// .executeWithChecks(executionOpenDriverDPEvent);
		// } catch (Exception ex) {
		// ex.printStackTrace();
		// }
		// }
		// }

		return null;
	}

}
