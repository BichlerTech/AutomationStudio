package com.bichler.astudio.editor.pubsub.adapter;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;

import com.bichler.astudio.editor.pubsub.nodes.PubSubEntryModelNode;
import com.bichler.astudio.editor.pubsub.properties.PubSubPropertySource;

public class PubSubPropertyAdapter implements IAdapterFactory
{
  @SuppressWarnings("unchecked")
  @Override
  public PubSubPropertySource getAdapter(Object adaptableObject, @SuppressWarnings("rawtypes") Class adapterType)
  {
    // rockwell datapoint node
    if (adapterType == IPropertySource.class && adaptableObject != null
        && adaptableObject.getClass() == PubSubEntryModelNode.class)
    {
      return new PubSubPropertySource(((PubSubEntryModelNode) adaptableObject));
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
