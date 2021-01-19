package com.bichler.astudio.editor.xml_da.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.bichler.astudio.opcua.handlers.AbstractOPCOpenDriverModelHandler;
import com.bichler.astudio.view.drivermodel.handler.util.DriverBrowserUtil;

public class OpenXMLDADriverModelHandler extends AbstractOPCOpenDriverModelHandler
{
  public static final String ID = "com.bichler.astudio.editor.opendrivermodel.xml_da";

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException
  {
    // OpenDriverModelBrowserEvent trigger = (OpenDriverModelBrowserEvent)
    // event
    // .getTrigger();
    DriverBrowserUtil.openEmptyDriverModelView();
    return null;
  }
}
