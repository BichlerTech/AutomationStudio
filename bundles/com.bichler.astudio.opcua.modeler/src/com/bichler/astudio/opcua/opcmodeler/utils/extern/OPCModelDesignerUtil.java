package com.bichler.astudio.opcua.opcmodeler.utils.extern;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.IHandlerService;

import com.bichler.astudio.opcua.opcmodeler.commands.handler.extern.StartDesignerHandler;
import com.bichler.astudio.opcua.opcmodeler.commands.handler.opc.ExportNamespaceModelHandler;

public class OPCModelDesignerUtil {
	// public static MessageConsoleStream getMessageConsole() {
	//
	// if (msgConsoleStream == null) {
	// MessageConsole outputConsole = new MessageConsole("CometDesigner",
	// null);
	//
	// ConsolePlugin.getDefault().getConsoleManager()
	// .addConsoles(new IConsole[] { outputConsole });
	//
	// msgConsoleStream = outputConsole.newMessageStream();
	// }
	//
	// return msgConsoleStream;
	// }
	//
	// private static MessageConsoleStream msgConsoleStream;
	public static void startDesigner(IWorkbenchWindow workbenchWindow) {
		IHandlerService handlerService = (IHandlerService) workbenchWindow.getService(IHandlerService.class);
		try {
			handlerService.executeCommand(StartDesignerHandler.ID, null);
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (NotDefinedException e) {
			e.printStackTrace();
		} catch (NotEnabledException e) {
			e.printStackTrace();
		} catch (NotHandledException e) {
			e.printStackTrace();
		}
	}

	public static void doSaveAsInformationModel(IWorkbenchWindow window) {
		final IHandlerService service = (IHandlerService) window.getService(IHandlerService.class);
		try {
			service.executeCommand(ExportNamespaceModelHandler.ID, null);
		} catch (NotDefinedException e) {
			e.printStackTrace();
		} catch (NotEnabledException e) {
			e.printStackTrace();
		} catch (NotHandledException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
}
