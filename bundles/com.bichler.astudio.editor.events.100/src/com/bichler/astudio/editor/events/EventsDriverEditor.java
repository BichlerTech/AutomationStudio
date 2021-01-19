package com.bichler.astudio.editor.events;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

import com.bichler.astudio.opcua.AbstractOPCDriverConfigSelectionEditorPart;
import com.bichler.astudio.opcua.editor.input.OPCUADriverEditorInput;
import com.bichler.astudio.opcua.widget.NodeToTrigger;
import com.bichler.astudio.view.drivermodel.handler.util.DriverBrowserUtil;

public class EventsDriverEditor extends AbstractOPCDriverConfigSelectionEditorPart
{
  private boolean dirty;
  public static final String ID = "com.bichler.astudio.editor.events.EventsDriverEditor";

  @Override
  public void doSave(IProgressMonitor monitor)
  {
  }

  @Override
  public void doSaveAs()
  {
  }

  @Override
  public void init(IEditorSite site, IEditorInput input) throws PartInitException
  {
    setSite(site);
    setInput(input);
    setPartName(getEditorInput().getServerName() + " - " + getEditorInput().getDriverName() + " - Treiber");
  }

  @Override
  public boolean isDirty()
  {
    return this.dirty;
  }

  @Override
  public boolean isSaveAsAllowed()
  {
    return false;
  }

  @Override
  public void createPartControl(Composite parent)
  {
  }

  @Override
  public void setFocus()
  {
    super.setFocus();
  }

  @Override
  public OPCUADriverEditorInput getEditorInput()
  {
    return (OPCUADriverEditorInput) super.getEditorInput();
  }

  @Override
  public void addSelectionChangedListener(ISelectionChangedListener listener)
  {
    this.listener = listener;
  }

  @Override
  public ISelection getSelection()
  {
    if (this.currentSelection != null)
    {
      return new StructuredSelection(this.currentSelection);
    }
    return StructuredSelection.EMPTY;
  }

  @Override
  public void removeSelectionChangedListener(ISelectionChangedListener listener)
  {
    this.listener = null;
  }

  @Override
  public void setSelection(ISelection selection)
  {
  }

  @Override
  public void setDirty(boolean dirty)
  {
    this.dirty = dirty;
    firePropertyChange(IEditorPart.PROP_DIRTY);
  }

  @Override
  public void computeSection()
  {
  }

  @Override
  public boolean isTriggerNodeValid(NodeToTrigger obj)
  {
    return false;
  }

  @Override
  public List<NodeToTrigger> getPossibleTriggerNodes()
  {
    return null;
  }

  @Override
  public void setPossibleTriggerNodes(List<NodeToTrigger> possibleTriggerNodes)
  {
  }

  @Override
  public void refreshDatapoints()
  {
  }

  @Override
  public void onFocusRemoteView()
  {
    DriverBrowserUtil.openEmptyDriverModelView();
  }
}
