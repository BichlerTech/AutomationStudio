package com.bichler.astudio.editor.allenbradley.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.bichler.astudio.editor.allenbradley.datenbaustein.AllenBradleyDBResourceManager;
import com.bichler.astudio.editor.allenbradley.datenbaustein.AllenBradleyResources;
import com.bichler.astudio.editor.allenbradley.driver.AllenBradleyDriverUtil;
import com.bichler.astudio.opcua.events.OpenDriverModelBrowserParameter;
import com.bichler.astudio.opcua.handlers.AbstractOPCOpenDriverModelHandler;

public class OpenAllenBradleyDriverModelHandler extends AbstractOPCOpenDriverModelHandler
{
  public static final String ID = "com.bichler.astudio.editor.opendrivermodel.allenbradley";

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException
  {
    OpenDriverModelBrowserParameter trigger = getCommandParameter(event);
    // trigger = (OpenDriverModelBrowserParameter) event.getTrigger();
    AllenBradleyDBResourceManager structManager = AllenBradleyResources.getInstance()
        .getDBResourceManager(trigger.getDrivername());
    AllenBradleyDriverUtil.openDriverView(trigger.getFilesystem(), trigger.getDriverConfigPath(), structManager);
    return null;
  }
}
