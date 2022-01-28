package com.bichler.astudio.editor.allenbradley;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.bichler.astudio.editor.allenbradley.model.AbstractAllenBradleyNode;

public class AllenBradleyModelTreeContentProvider implements ITreeContentProvider
{
  @Override
  public void dispose()
  {
  }

  @Override
  public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
  {
  }

  @Override
  public Object[] getElements(Object inputElement)
  {
    return getChildren(inputElement);
  }

  @Override
  public Object[] getChildren(Object parentElement)
  {
    if (parentElement instanceof AbstractAllenBradleyNode)
    {
      return ((AbstractAllenBradleyNode) parentElement).getMembers();
    }
    return new Object[0];
  }

  @Override
  public Object getParent(Object element)
  {
    return null;
  }

  @Override
  public boolean hasChildren(Object element)
  {
    return this.getChildren(element).length > 0;
  }
}
