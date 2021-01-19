package com.bichler.astudio.opcua.properties.view;

import org.eclipse.ui.views.properties.IPropertySheetEntry;
import org.eclipse.ui.views.properties.PropertySheetSorter;

public class ASProperySheetSorter extends PropertySheetSorter
{

  @Override
  public int compare(IPropertySheetEntry entryA, IPropertySheetEntry entryB)
  {
//    entryA.getDisplayName()
    return getCollator().compare(entryA.getDisplayName(),
        entryB.getDisplayName());
  }
}
