package com.bichler.astudio.device.core;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.console.MessageConsole;

public class ASConsole extends MessageConsole
{
  public ASConsole(String name, ImageDescriptor imageDescriptor)
  {
    super(name, imageDescriptor);
  }

  @Override
  public String getName()
  {
    return "Automation Studio Console";
  }
}
