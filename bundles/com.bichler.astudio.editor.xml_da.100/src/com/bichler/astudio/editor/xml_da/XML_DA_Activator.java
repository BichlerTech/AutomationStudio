package com.bichler.astudio.editor.xml_da;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.osgi.framework.BundleContext;

import com.bichler.astudio.opcua.OPCUADriverRegistry;
import com.bichler.astudio.utils.activator.InternationalActivator;

/**
 * The activator class controls the plug-in life cycle
 */
public class XML_DA_Activator extends InternationalActivator
{
  // The plug-in ID
  private static final String PLUGIN_ID = "com.bichler.astudio.editor.xml_da.1.0.0"; //$NON-NLS-1$
  public static final String BUNDLE_NAME = "com.bichler.astudio.editor.xml_da.resource.custom"; //$NON-NLS-1$
  // The shared instance
  private static XML_DA_Activator plugin;

  /**
   * The constructor
   */
  public XML_DA_Activator()
  {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.
   * BundleContext )
   */
  public void start(BundleContext context) throws Exception
  {
    super.start(context);
    XML_DA_Activator.plugin = this; // Noncompliant
    OPCUADriverRegistry.drivers.put(context.getBundle().getSymbolicName(), this);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.
   * BundleContext )
   */
  @Override
  public void stop(BundleContext context) throws Exception
  {
    XML_DA_Activator.plugin = null; // Noncompliant
    super.stop(context);
  }

  /**
   * Returns the shared instance
   * 
   * @return the shared instance
   */
  public static XML_DA_Activator getDefault()
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
  protected void initializeImageRegistry(ImageRegistry registry)
  {
    registry.put(XML_DA_SharedImages.ICON_CHECKED_0, getImageDescriptor(XML_DA_SharedImages.ICON_CHECKED_0));
    registry.put(XML_DA_SharedImages.ICON_CHECKED_0_GRAY, getImageDescriptor(XML_DA_SharedImages.ICON_CHECKED_0_GRAY));
    registry.put(XML_DA_SharedImages.ICON_CHECKED_1, getImageDescriptor(XML_DA_SharedImages.ICON_CHECKED_1));
    registry.put(XML_DA_SharedImages.ICON_CHECKED_1_GRAY, getImageDescriptor(XML_DA_SharedImages.ICON_CHECKED_1_GRAY));
    registry.put(XML_DA_SharedImages.ICON_DATEPOINT, getImageDescriptor(XML_DA_SharedImages.ICON_DATEPOINT));
    registry.put(XML_DA_SharedImages.ICON_DATEPOINTLEAF, getImageDescriptor(XML_DA_SharedImages.ICON_DATEPOINTLEAF));
    registry.put(XML_DA_SharedImages.ICON_ADD, getImageDescriptor(XML_DA_SharedImages.ICON_ADD));
    registry.put(XML_DA_SharedImages.ICON_DELETE, getImageDescriptor(XML_DA_SharedImages.ICON_DELETE));
    registry.put(XML_DA_SharedImages.ICON_IMPORT, getImageDescriptor(XML_DA_SharedImages.ICON_IMPORT));
    registry.put(XML_DA_SharedImages.ICON_TEST, getImageDescriptor(XML_DA_SharedImages.ICON_TEST));
  }

  @Override
  public String getBundleName()
  {
    return BUNDLE_NAME;
  }
}
