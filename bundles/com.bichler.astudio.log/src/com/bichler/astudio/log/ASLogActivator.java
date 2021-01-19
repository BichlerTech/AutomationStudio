package com.bichler.astudio.log;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class ASLogActivator extends AbstractUIPlugin
{
  // The plug-in ID
  public static final String PLUGIN_ID = "com.bichler.astudio.log";
  // The shared instance
  private static ASLogActivator plugin;
  private static final Logger logger = Logger.getLogger(ASLogActivator.class.getName());
  private static final LogManager logManager = LogManager.getLogManager();

  public static final String LOGVIEW_ID = "com.hbsoft.comet.log4j.view";
  
  /**
   * The constructor
   */
  public ASLogActivator()
  {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.
   * BundleContext)
   */
  public void start(BundleContext context) throws Exception
  {
    super.start(context);
    plugin = this;
    // IPath path4j = Path.ROOT.append("log4j.properties");
//    IPath path = Path.ROOT.append("log.properties");
    // URL logproperty4j = FileLocator.find(ASLogActivator.getDefault().getBundle(),
    // path4j, null);
//    URL logproperty = FileLocator.find(ASLogActivator.getDefault().getBundle(), path, null);
    try (final InputStream is = getClass().getResourceAsStream("/log.properties"))
    {
      // URL url4j = FileLocator.toFileURL(logproperty4j);
      // PropertyConfigurator.configure(url4j);
      // URL url = FileLocator.toFileURL(logproperty);
      logManager.readConfiguration(is);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public Logger getLogger()
  {
    return logger;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.
   * BundleContext)
   */
  public void stop(BundleContext context) throws Exception
  {
    plugin = null;
    super.stop(context);
  }

  /**
   * Returns the shared instance
   *
   * @return the shared instance
   */
  public static ASLogActivator getDefault()
  {
    return plugin;
  }
}
