package com.bichler.astudio.opcua.properties.handler;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.opcua.properties.view.ASPropertyView;

public class ShowPropertiesViewHandler extends AbstractHandler
{
  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException
  {
    try
    {
      HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().showView(ASPropertyView.ID);
    }
    catch (PartInitException e)
    {
      Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
    }
    return null;
  }
}
