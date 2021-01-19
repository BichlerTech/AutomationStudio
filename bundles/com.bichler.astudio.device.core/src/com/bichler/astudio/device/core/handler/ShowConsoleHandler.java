package com.bichler.astudio.device.core.handler;

import java.io.PrintStream;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;

import com.bichler.astudio.device.core.ASConsole;


public class ShowConsoleHandler extends AbstractHandler
{
  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException
  {
    IConsoleManager consoleManager = ConsolePlugin.getDefault().getConsoleManager();
    for(IConsole cons : consoleManager.getConsoles()) {
      if(cons.getName().compareTo("Automation Studio Console") == 0) {
        consoleManager.showConsoleView(cons);
        return null;
      }
    }
    ASConsole console = new ASConsole("Automation Studio Console", null);
    consoleManager.addConsoles(new IConsole[] { console });
    PrintStream printStream = new PrintStream(console.newMessageStream());
    // Link standard output stream to the console
    System.setOut(printStream);
    // Link error output stream to the console
    System.setErr(printStream);
    consoleManager.showConsoleView(console);
    return null;
  }
}
