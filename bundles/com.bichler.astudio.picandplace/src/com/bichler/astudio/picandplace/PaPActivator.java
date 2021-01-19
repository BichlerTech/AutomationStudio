package com.bichler.astudio.picandplace;

import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageRegistry;
import org.osgi.framework.BundleContext;
import com.bichler.astudio.connections.ConnectionsHostManager;
import com.bichler.astudio.utils.activator.InternationalActivator;

/**
 * The activator class controls the plug-in life cycle
 */
public class PaPActivator extends InternationalActivator
{
  public ResourceBundle RESOURCE_BUNDLE;
  // The plug-in ID
  public static final String PLUGIN_ID = "com.bichler.astudio.picandplace";
  public static final String BUNDLE_NAME = "com.bichler.astudio.picandplace.resource.custom";
  // The shared instance
  private static PaPActivator plugin;

  /**
   * The constructor
   */
  public PaPActivator()
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
    // tsn connections
//    ConnectionsHostManager tsnConnectionsManager = new ConnectionsHostManager();
//    IPreferenceStore tsnstore = PaPActivator.getDefault().getPreferenceStore();
//    String tsnFolder = tsnstore.getString(TSNConstants.ASTSNRuntime);
//    String tsnServers = tsnstore.getString(TSNConstants.ASTSNProjectsPath);
//    tsnConnectionsManager.importHostsFromRuntimeStructure(tsnFolder, tsnServers);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
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
  public static PaPActivator getDefault()
  {
    return plugin;
  }

  @Override
  protected void initializeImageRegistry(ImageRegistry registry)
  {
    super.initializeImageRegistry(registry);
    // registry.put(OPCUASharedImages.ICON_ADD,
    // getImageDescriptor(OPCUASharedImages.ICON_ADD));
    // registry.put(OPCUASharedImages.ICON_DELETE,
    // getImageDescriptor(OPCUASharedImages.ICON_DELETE));
    // registry.put(OPCUASharedImages.ICON_CHECKED_0,
    // getImageDescriptor(OPCUASharedImages.ICON_CHECKED_0));
    // registry.put(OPCUASharedImages.ICON_CHECKED_1,
    // getImageDescriptor(OPCUASharedImages.ICON_CHECKED_1));
  }

  @Override
  public String getBundleName()
  {
    return BUNDLE_NAME;
  }

  @Override
  public void setLanguageResourceBundle(Locale locale)
  {
    Locale.setDefault(locale);
    try
    {
      RESOURCE_BUNDLE = ResourceBundle.getBundle(getBundleName(), locale);
    }
    catch (Exception e)
    {
      RESOURCE_BUNDLE = ResourceBundle.getBundle(getBundleName(), Locale.ENGLISH);
    }
  }
}
