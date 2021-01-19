package com.bichler.astudio.editor.xml_da.adapter;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;

import com.bichler.astudio.editor.xml_da.properties.XMLDAPropertySource;
import com.bichler.astudio.editor.xml_da.xml.XMLDAModelNode;

public class XMLDAPropertyAdapter implements IAdapterFactory
{
  @Override
  public Object getAdapter(Object adaptableObject, Class adapterType)
  {
    // rockwell datapoint node
    if (adapterType == IPropertySource.class && adaptableObject != null
        && adaptableObject.getClass() == XMLDAModelNode.class)
    {
      return new XMLDAPropertySource((XMLDAModelNode) adaptableObject);
    }
    return null;
  }

  @Override
  public Class[] getAdapterList()
  {
    return new Class[] { IPropertySource.class };
  }
}
