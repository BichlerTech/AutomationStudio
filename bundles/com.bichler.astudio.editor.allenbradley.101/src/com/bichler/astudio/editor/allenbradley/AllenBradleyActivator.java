package com.bichler.astudio.editor.allenbradley;

import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.osgi.framework.BundleContext;

import com.bichler.astudio.opcua.OPCUADriverRegistry;
import com.bichler.astudio.utils.activator.InternationalActivator;

/**
 * The activator class controls the plug-in life cycle
 */
public class AllenBradleyActivator extends InternationalActivator
{
  // The plug-in ID
  private static final String PLUGIN_ID = "com.bichler.astudio.editor.allenbradley.1.0.1"; //$NON-NLS-1$
  public static final String BUNDLE_NAME = "com.bichler.astudio.editor.allenbradley.resource.custom"; //$NON-NLS-1$
  // The shared instance
  private static AllenBradleyActivator plugin;

  /**
   * The constructor
   */
  public AllenBradleyActivator()
  {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.
   * BundleContext )
   */
  @Override
  public void start(BundleContext context) throws Exception
  {
    super.start(context);
    setPlugin(this);
    OPCUADriverRegistry.drivers.put(context.getBundle().getSymbolicName(), this);
  }

  private static synchronized void setPlugin(AllenBradleyActivator activator)
  {
    plugin = activator;
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
    setPlugin(null);
    super.stop(context);
  }

  /**
   * Returns the shared instance
   * 
   * @return the shared instance
   */
  public static AllenBradleyActivator getDefault()
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

  @Override
  protected void initializeImageRegistry(ImageRegistry registry)
  {
    registry.put(AllenBradleySharedImages.ICON_CHECKED_0, getImageDescriptor(AllenBradleySharedImages.ICON_CHECKED_0));
    registry.put(AllenBradleySharedImages.ICON_CHECKED_0_GRAY,
        getImageDescriptor(AllenBradleySharedImages.ICON_CHECKED_0_GRAY));
    registry.put(AllenBradleySharedImages.ICON_CHECKED_1, getImageDescriptor(AllenBradleySharedImages.ICON_CHECKED_1));
    registry.put(AllenBradleySharedImages.ICON_CHECKED_1_GRAY,
        getImageDescriptor(AllenBradleySharedImages.ICON_CHECKED_1_GRAY));
    registry.put(AllenBradleySharedImages.ICON_DATEPOINT, getImageDescriptor(AllenBradleySharedImages.ICON_DATEPOINT));
    registry.put(AllenBradleySharedImages.ICON_DATEPOINTLEAF,
        getImageDescriptor(AllenBradleySharedImages.ICON_DATEPOINTLEAF));
    registry.put(AllenBradleySharedImages.ICON_DELETE, getImageDescriptor(AllenBradleySharedImages.ICON_DELETE));
    registry.put(AllenBradleySharedImages.ICON_IMPORT, getImageDescriptor(AllenBradleySharedImages.ICON_IMPORT));
    registry.put(AllenBradleySharedImages.ICON_LOADDP, getImageDescriptor(AllenBradleySharedImages.ICON_LOADDP));
    registry.put(AllenBradleySharedImages.ICON_VALIDATEDP,
        getImageDescriptor(AllenBradleySharedImages.ICON_VALIDATEDP));
    registry.put(AllenBradleySharedImages.ICON_GEARS, getImageDescriptor(AllenBradleySharedImages.ICON_GEARS));
    registry.put(AllenBradleySharedImages.ICON_TEST, getImageDescriptor(AllenBradleySharedImages.ICON_TEST));
  }
}
