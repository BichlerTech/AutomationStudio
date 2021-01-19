package com.bichler.astudio.editor.xml_da;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.bichler.astudio.editor.xml_da.xml.XMLDAModelNode;

public class XML_DA_ModelContentProvider implements ITreeContentProvider
{
  @Override
  public void dispose()
  {
    // TODO Auto-generated method stub
  }

  @Override
  public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
  {
    // TODO Auto-generated method stub
  }

  @SuppressWarnings("unchecked")
  @Override
  public Object[] getElements(Object inputElement)
  {
    if (inputElement != null)
    {
      return ((ArrayList<XMLDAModelNode>) inputElement).toArray();
    }
    return new Object[] { new XMLDAModelNode() };
  }

  @Override
  public Object[] getChildren(Object parentElement)
  {
    if (parentElement instanceof XMLDAModelNode)
    {
      return ((XMLDAModelNode) parentElement).getChildren().toArray();
    }
    return new Object[0];
  }

  @Override
  public Object getParent(Object element)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean hasChildren(Object element)
  {
    return this.getChildren(element).length > 0;
  }
}
