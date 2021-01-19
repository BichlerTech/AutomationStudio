package com.bichler.astudio.editor.allenbradley.handlers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bichler.astudio.editor.allenbradley.model.AbstractAllenBradleyNode;

public class DisactivateChildrenHandler extends AbstractHandler
{
  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException
  {
    IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getActiveMenuSelection(event);
    if (selection == null)
    {
      return null;
    }
    Object[] items = ((StructuredSelection) selection).toArray();
    List<Object> items2update = new ArrayList<>();
    for (Object item : items)
    {
      if (item instanceof AbstractAllenBradleyNode)
      {
        boolean isActive = ((AbstractAllenBradleyNode) item).isActive();
        if (isActive)
        {
          ((AbstractAllenBradleyNode) item).setActiveAll(false);
          items2update.add(item);
        }
      }
    }
    // update items
    List<TreeViewer> viewers = new ArrayList<>();
    for (Object item : items2update)
    {
      if (item instanceof AbstractAllenBradleyNode)
      {
        // TreeViewer viewer = ((AbstractallenbradleyNode) item).getViewer();
        // if (viewer != null && !viewers.contains(viewer)) {
        // viewers.add(viewer);
        // }
      }
    }
    for (TreeViewer viewer : viewers)
    {
      viewer.refresh(true);
    }
    return null;
  }
}
