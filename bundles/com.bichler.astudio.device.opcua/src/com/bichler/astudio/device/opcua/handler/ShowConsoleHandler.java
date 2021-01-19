package com.bichler.astudio.device.opcua.handler;

import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.device.core.view.OPCUADeviceView;
import com.bichler.astudio.device.opcua.ASConsole;

public class ShowConsoleHandler extends AbstractHandler {
	private static final String CONSOLE_NAME = "Automation Studio Console";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IConsoleManager consoleManager = ConsolePlugin.getDefault().getConsoleManager();

		Command command = event.getCommand();
		boolean oldValue = HandlerUtil.toggleCommandState(command);
		if (!oldValue) {
			// try open console
			for (IConsole cons : consoleManager.getConsoles()) {
				if (cons.getName().compareTo(CONSOLE_NAME) == 0) {
					consoleManager.showConsoleView(cons);
					return null;
				}
			}
			ASConsole console = new ASConsole(CONSOLE_NAME, null);
			consoleManager.addConsoles(new IConsole[] { console });
			PrintStream printStream = new PrintStream(console.newMessageStream());
			// Link standard output stream to the console
			System.setOut(printStream);
			// Link error output stream to the console
			System.setErr(printStream);
			consoleManager.showConsoleView(console);
		} else {
			IViewPart viewpart = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage()
					.findView("org.eclipse.ui.console.ConsoleView");
			if (viewpart != null) {
				HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().hideView(viewpart);
			}

		}

		return null;
	}
}
