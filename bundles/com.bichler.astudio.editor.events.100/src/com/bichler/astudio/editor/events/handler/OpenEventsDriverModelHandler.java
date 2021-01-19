package com.bichler.astudio.editor.events.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.bichler.astudio.opcua.handlers.AbstractOPCOpenDriverModelHandler;
import com.bichler.astudio.view.drivermodel.handler.util.DriverBrowserUtil;


public class OpenEventsDriverModelHandler extends AbstractOPCOpenDriverModelHandler
{
  public static final String ID = "com.bichler.astudio.editor.opendrivermodel.events";

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException
  {
    // OpenDriverModelBrowserParameter trigger =
    // (OpenDriverModelBrowserParameter) event
    // .getTrigger();
    DriverBrowserUtil.openEmptyDriverModelView();
    return null;
  }
}
