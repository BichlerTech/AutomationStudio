package com.bichler.astudio.zebra.labelprint;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class ZebraActivator extends AbstractUIPlugin
{
  private BundleContext context = null;

  private static ZebraActivator plugin;
  @Override
  public void start(BundleContext context) throws Exception
  {
    super.start(context);
    plugin = this;
    
    // TODO Auto-generated method stub
    this.setContext(context);
  }

  @Override
  public void stop(BundleContext context) throws Exception
  {
    // TODO Auto-generated method stub
  }

  public BundleContext getContext()
  {
    return context;
  }

  public void setContext(BundleContext context)
  {
    this.context = context;
  }
  
  /**
   * Returns the shared instance
   *
   * @return the shared instance
   */
  public static ZebraActivator getDefault()
  {
    return plugin;
  }
  
  public String getResourceFile(String path)
  {
    URL url = FileLocator.find(getDefault().getBundle(), Path.ROOT.append(path), null);
    if (url != null)
    {
      try
      {
        URL url2 = FileLocator.toFileURL(url);
        // remove '/' beginning of path if possible
        return new File(url2.getFile()).getPath();
      }
      catch (IOException e)
      {
        Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
      }
    }
    return null;
  }
}
