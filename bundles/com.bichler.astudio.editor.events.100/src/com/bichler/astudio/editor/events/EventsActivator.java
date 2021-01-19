package com.bichler.astudio.editor.events;

import org.osgi.framework.BundleContext;

import com.bichler.astudio.opcua.OPCUADriverRegistry;
import com.bichler.astudio.utils.activator.InternationalActivator;

/**
 * The activator class controls the plug-in life cycle
 */
public class EventsActivator extends InternationalActivator
{
  // The plug-in ID
  public static final String PLUGIN_ID = "com.bichler.astudio.editor.events.1.0.0";
  public static final String BUNDLE_NAME = "com.bichler.astudio.editor.events.resource.custom"; //$NON-NLS-1$
  // The shared instance
  private static EventsActivator plugin;

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.
   * BundleContext)
   */
  @Override
  public void start(BundleContext context) throws Exception
  {
    super.start(context);
    setPlugin(this);
    OPCUADriverRegistry.drivers.put(context.getBundle().getSymbolicName(), this);
  }

  private static synchronized void setPlugin(EventsActivator pl)
  {
    plugin = pl;
  }

  /**
   * Returns the shared instance
   *
   * @return the shared instance
   */
  public static EventsActivator getDefault()
  {
    return plugin;
  }

  @Override
  public String getBundleName()
  {
    return BUNDLE_NAME;
  }
  
  
}
