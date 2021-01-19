package com.bichler.astudio.perspective.studio;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class ASPerspectiveActivator extends AbstractUIPlugin
{
  // The plug-in ID
  public static final String PLUGIN_ID = "com.bichler.astudio.perspective.studio"; //$NON-NLS-1$
  // The shared instance
  private static ASPerspectiveActivator plugin;

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
  public static ASPerspectiveActivator getDefault()
  {
    return plugin;
  }
  
  /**
	 * Returns an image descriptor for the image file at the given plug-in relative
	 * path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		super.initializeImageRegistry(reg);

		registerImage(reg, StudioImages.ICON_ABOUT, StudioImages.ICON_ABOUT);
	}
	
	public static Image getImage(String key) {
		return getDefault().getImageRegistry().get(key);
	}

	private void registerImage(ImageRegistry reg, String key, String path) {
		Bundle bundle = Platform.getBundle(PLUGIN_ID);
		ImageDescriptor myImage = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path(path), null));
		reg.put(key, myImage);
	}
}
