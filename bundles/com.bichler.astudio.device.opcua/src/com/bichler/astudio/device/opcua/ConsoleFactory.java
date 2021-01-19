package com.bichler.astudio.device.opcua;

import java.io.PrintStream;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleFactory;
import org.eclipse.ui.console.IConsoleManager;

public class ConsoleFactory implements IConsoleFactory
{
  @Override
  public void openConsole()
  {
    IConsoleManager consoleManager = ConsolePlugin.getDefault().getConsoleManager();
    for(IConsole cons : consoleManager.getConsoles()) {
      if(cons.getName().compareTo("Automation Studio Console") == 0) {
        consoleManager.showConsoleView(cons);
        return;
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
  }
}
