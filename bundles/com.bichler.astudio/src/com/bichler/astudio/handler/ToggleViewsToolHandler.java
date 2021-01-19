package com.bichler.astudio.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.internal.WorkbenchPage;
import org.eclipse.ui.internal.WorkbenchWindow;

public class ToggleViewsToolHandler extends AbstractHandler
{
  public static final String ID = "com.bichler.astudio.command.toggleviewstool";

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException
  {
    Command command = event.getCommand();
    boolean oldValue = HandlerUtil.toggleCommandState(command);
    // try {
    // IHandlerService service = (IHandlerService) PlatformUI
    // .getWorkbench().getActiveWorkbenchWindow()
    // .getService(IHandlerService.class);
    // if (service != null)
    // service.executeCommand("org.eclipse.ui.ToggleCoolbarAction",
    // null);
    // } catch (Exception e) {
    // //handle error
    // }
    IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    IContributionItem[] items = ((WorkbenchWindow) workbenchWindow).getCoolBarManager2().getItems();
    // MenuBarManager().getItems();
    for (IContributionItem item : items)
    {
      if (item.getId().compareTo("com.bichler.astudio.main.toolbar.views") == 0)
      {
        item.setVisible(oldValue);
      }
    }
    ((WorkbenchPage) workbenchWindow.getActivePage()).removeHiddenItems("com.bichler.astudio.main.toolbar.views");
    //;
    //((WorkbenchWindow) workbenchWindow).getCoolBarManager().update(true);
    IWorkbench wb = PlatformUI.getWorkbench();
    IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
    IWorkbenchPage page = win.getActivePage();
    IPerspectiveDescriptor perspective = page.getPerspective();
    System.out.println(perspective.getId());
    // Perspective persp = null;
    // persp.updateActionBars();
    // ((WorkbenchWindow)workbenchWindow).getMenuBarManager().setVisible(oldValue);
    return null;
  }
}
