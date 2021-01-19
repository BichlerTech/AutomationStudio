package com.bichler.astudio.licensemanagement;

import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.BundleContext;

// import com.bichler.astudio.connections.ConnectionsHostManager;
// import com.bichler.astudio.connections.utils.ConnectionsSharedImages;
import com.bichler.astudio.licensemanagement.manager.LicenseManager;
import com.bichler.astudio.utils.activator.InternationalActivator;

/**
 * The activator class controls the plug-in life cycle
 */
public class LicManActivator extends InternationalActivator
{
  // public static ConnectionsHostManager hostManager = null;
  // The plug-in ID
  public static final String PLUGIN_ID = "com.bichler.astudio.licensemanagement"; //$NON-NLS-1$
  public static final String BUNDLE_NAME = "com.bichler.astudio.licensemanagement.resource.custom"; //$NON-NLS-1$
  // The shared instance
  private static LicManActivator plugin;
  private LicenseManager licenseManager = null;

  /**
   * The constructor
   */
  public LicManActivator()
  {
  }

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
    plugin = this;
    this.licenseManager = new LicenseManager(false);
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
    this.licenseManager.stop();
  }

  /**
   * Returns the shared instance
   *
   * @return the shared instance
   */
  public static LicManActivator getDefault()
  {
    return plugin;
  }

  /**
   * Returns an image descriptor for the image file at the given plug-in relative
   * path
   *
   * @param path
   *          the path
   * @return the image descriptor
   */
  public static ImageDescriptor getImageDescriptor(String path)
  {
    return imageDescriptorFromPlugin(PLUGIN_ID, path);
  }

  @Override
  public String getBundleName()
  {
    return BUNDLE_NAME;
  }

  public LicenseManager getLicenseManager()
  {
    return this.licenseManager;
  }
}
