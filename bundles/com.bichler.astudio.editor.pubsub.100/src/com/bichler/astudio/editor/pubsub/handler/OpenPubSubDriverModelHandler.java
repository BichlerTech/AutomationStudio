package com.bichler.astudio.editor.pubsub.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.bichler.astudio.opcua.handlers.AbstractOPCOpenDriverModelHandler;
import com.bichler.astudio.view.drivermodel.handler.util.DriverBrowserUtil;


public class OpenPubSubDriverModelHandler extends AbstractOPCOpenDriverModelHandler
{
  public static final String ID = "com.bichler.astudio.editor.opendrivermodel.pubsub";

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
