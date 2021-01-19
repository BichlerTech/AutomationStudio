package com.bichler.astudio.device.opcua.editor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.bichler.astudio.device.opcua.DeviceActivator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class SimulationEditorInput implements IEditorInput
{
  // private UAServerApplicationInstance opcServer;
  public SimulationEditorInput()
  {
  }

  // public UAServerApplicationInstance getServerInstance(){
  // return this.opcServer;
  // }
  @Override
  public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean exists()
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public ImageDescriptor getImageDescriptor()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getName()
  {
    return CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.simulation.editorinput.name");
  }

  @Override
  public IPersistableElement getPersistable()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getToolTipText()
  {
    return CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.simulation.editorinput.tooltip");
  }

  @Override
  public boolean equals(Object obj)
  {
    // if (this == obj)
    // return true;
    if (obj == null || getClass() != obj.getClass())
      return false;
    return true;
    // return this.opcServer == ((SimulationEditorInput)
    // obj).getServerInstance();
  }
}
