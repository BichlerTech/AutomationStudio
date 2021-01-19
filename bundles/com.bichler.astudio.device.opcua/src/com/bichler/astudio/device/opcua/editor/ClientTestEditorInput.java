package com.bichler.astudio.device.opcua.editor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.bichler.astudio.device.opcua.DeviceActivator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class ClientTestEditorInput implements IEditorInput
{
  public ClientTestEditorInput()
  {
  }

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
    if (obj == null || getClass() != obj.getClass())
      return false;
    return true;
  }
}
