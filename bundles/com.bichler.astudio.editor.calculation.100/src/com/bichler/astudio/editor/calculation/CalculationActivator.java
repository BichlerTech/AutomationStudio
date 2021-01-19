package com.bichler.astudio.editor.calculation;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.osgi.framework.BundleContext;

import com.bichler.astudio.opcua.OPCUADriverRegistry;
import com.bichler.astudio.utils.activator.InternationalActivator;

/**
 * The activator class controls the plug-in life cycle
 */
public class CalculationActivator extends InternationalActivator
{
  // The plug-in ID
  private static final String PLUGIN_ID = "com.bichler.astudio.editor.calculation.1.0.0"; //$NON-NLS-1$
  public static final String BUNDLE_NAME = "com.bichler.astudio.editor.calculation.resource.custom"; //$NON-NLS-1$
  // The shared instance
  private static CalculationActivator plugin;

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

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.
   * BundleContext)
   */
  @Override
  public void stop(BundleContext context) throws Exception
  {
    setPlugin(null);
    super.stop(context);
  }

  public static synchronized void setPlugin(CalculationActivator plugin)
  {
    CalculationActivator.plugin = plugin;
  }

  /**
   * Returns the shared instance
   *
   * @return the shared instance
   */
  public static CalculationActivator getDefault()
  {
    return plugin;
  }

  @Override
  protected void initializeImageRegistry(ImageRegistry registry)
  {
    registry.put(CalculationSharedImages.ICON_CHECKED_0, getImageDescriptor(CalculationSharedImages.ICON_CHECKED_0));
    registry.put(CalculationSharedImages.ICON_CHECKED_0_GRAY,
        getImageDescriptor(CalculationSharedImages.ICON_CHECKED_0_GRAY));
    registry.put(CalculationSharedImages.ICON_CHECKED_1, getImageDescriptor(CalculationSharedImages.ICON_CHECKED_1));
    registry.put(CalculationSharedImages.ICON_CHECKED_1_GRAY,
        getImageDescriptor(CalculationSharedImages.ICON_CHECKED_1_GRAY));
    registry.put(CalculationSharedImages.ICON_DATEPOINT, getImageDescriptor(CalculationSharedImages.ICON_DATEPOINT));
    registry.put(CalculationSharedImages.ICON_DATEPOINTLEAF,
        getImageDescriptor(CalculationSharedImages.ICON_DATEPOINTLEAF));
    registry.put(CalculationSharedImages.ICON_DELETE, getImageDescriptor(CalculationSharedImages.ICON_DELETE));
    registry.put(CalculationSharedImages.ICON_ADD, getImageDescriptor(CalculationSharedImages.ICON_ADD));
    registry.put(CalculationSharedImages.ICON_IMPORT, getImageDescriptor(CalculationSharedImages.ICON_IMPORT));
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

//  @Override
//  public void setLanguageResourceBundle(Locale locale)
//  {
//    Locale.setDefault(locale);
//    try
//    {
//      RESOURCE_BUNDLE = ResourceBundle.getBundle(getBundleName(), locale);
//    }
//    catch (Exception e)
//    {
//      RESOURCE_BUNDLE = ResourceBundle.getBundle(getBundleName(), Locale.ENGLISH);
//    }
//  }
}
