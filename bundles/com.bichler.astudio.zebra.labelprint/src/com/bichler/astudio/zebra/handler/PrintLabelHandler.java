package com.bichler.astudio.zebra.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.bichler.astudio.zebra.labelprint.ZebraPrintBTech;

public class PrintLabelHandler extends AbstractHandler
{
  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException
  {
    if (event.getTrigger() instanceof PrintEvent)
    {
      PrintEvent pe = (PrintEvent) event.getTrigger();
      ZebraPrintBTech.printLabel(pe.getSn(), pe.getX1(), pe.getX2(), pe.getDate(), pe.getMonth());
    }
    return null;
  }
}
