package com.bichler.astudio.editor.events.adapter;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;

import com.bichler.astudio.editor.events.properties.EventsPropertySource;
import com.bichler.astudio.editor.events.xml.EventEntryModelNode;

public class EventsPropertyAdapter implements IAdapterFactory
{
  @SuppressWarnings("unchecked")
  @Override
  public EventsPropertySource getAdapter(Object adaptableObject, @SuppressWarnings("rawtypes") Class adapterType)
  {
    // rockwell datapoint node
    if (adapterType == IPropertySource.class && adaptableObject != null
        && adaptableObject.getClass() == EventEntryModelNode.class)
    {
      return new EventsPropertySource(((EventEntryModelNode) adaptableObject));
    }
    return null;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  public Class[] getAdapterList()
  {
    return new Class[] { IPropertySource.class };
  }
}
