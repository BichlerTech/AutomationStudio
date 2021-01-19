package com.bichler.astudio.log.view.utils;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.services.IServiceLocator;

import com.bichler.astudio.log.view.handler.StartServerHandler;

public class LogViewUtils {

	public static void startServer() {
		// Obtain IServiceLocator implementer, e.g. from
		// PlatformUI.getWorkbench():
		IServiceLocator serviceLocator = PlatformUI.getWorkbench();
		// or a site from within a editor or view:
		// IServiceLocator serviceLocator = getSite();

		ICommandService commandService = (ICommandService) serviceLocator
				.getService(ICommandService.class);

		try {
			// Lookup commmand with its ID
			Command command = commandService.getCommand(StartServerHandler.ID);

			// Optionally pass a ExecutionEvent instance, default no-param arg
			// creates blank event
			command.executeWithChecks(new ExecutionEvent());

		} catch (ExecutionException | NotDefinedException | NotEnabledException
				| NotHandledException e) {
			// Replace with real-world exception handling
			e.printStackTrace();
		}
	}
	
	public static void stopServer() {
		// Obtain IServiceLocator implementer, e.g. from
		// PlatformUI.getWorkbench():
		IServiceLocator serviceLocator = PlatformUI.getWorkbench();
		// or a site from within a editor or view:
		// IServiceLocator serviceLocator = getSite();

		ICommandService commandService = (ICommandService) serviceLocator
				.getService(ICommandService.class);

		try {
			// Lookup commmand with its ID
			Command command = commandService.getCommand(StartServerHandler.ID);

			// Optionally pass a ExecutionEvent instance, default no-param arg
			// creates blank event
			command.executeWithChecks(new ExecutionEvent());

		} catch (ExecutionException | NotDefinedException | NotEnabledException
				| NotHandledException e) {
			// Replace with real-world exception handling
			e.printStackTrace();
		}
	}
}
