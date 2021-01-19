package com.bichler.astudio.editor.allenbradley.adapter;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;

import com.bichler.astudio.editor.allenbradley.properties.AllenBradleyPropertySource;
import com.bichler.astudio.editor.allenbradley.xml.AllenBradleyEntryModelNode;

public class AllenBradleyPropertyAdapter implements IAdapterFactory
{
  @SuppressWarnings("rawtypes")
  @Override
  public Object getAdapter(Object adaptableObject, Class adapterType)
  {
    // allenbradley datapoint node
    if (adapterType == IPropertySource.class && adaptableObject != null
        && adaptableObject.getClass() == AllenBradleyEntryModelNode.class)
    {
      return new AllenBradleyPropertySource((AllenBradleyEntryModelNode) adaptableObject);
    }
    return null;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public Class[] getAdapterList()
  {
    return new Class[] { IPropertySource.class };
  }
}
