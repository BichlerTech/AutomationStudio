package com.bichler.astudio.opcua.properties.view;

import org.eclipse.ui.views.properties.PropertySheetEntry;

public class ASPropertySheetEntry extends PropertySheetEntry
{
  public ASPropertySheetEntry()
  {
    super();
  }

  @Override
  public String getDisplayName()
  {
    String displayName = super.getDisplayName();
    int colon = displayName.lastIndexOf(':');
    if (colon != -1)
      displayName = displayName.substring(colon + 1);
    return displayName;
  }
  
  
  public String getFulllDisplayName()
  {
    return super.getDisplayName();
  }
}
