package com.bichler.astudio.language;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.BundleContext;

import com.bichler.astudio.utils.activator.InternationalActivator;
import com.bichler.astudio.utils.ui.images.SharedImages;

/**
 * The activator class controls the plug-in life cycle
 */
public class LanguageActivator extends InternationalActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.bichler.astudio.language"; //$NON-NLS-1$
	public static final String BUNDLE_NAME = "com.bichler.astudio.language.resource.custom";

	// The shared instance
	private static LanguageActivator plugin;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.
	 * BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		LanguageActivator.setPlugin(this);
	}
	
	private static void setPlugin(LanguageActivator plugin) {
	  LanguageActivator.plugin = plugin;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.
	 * BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		LanguageActivator.setPlugin(plugin);
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static LanguageActivator getDefault() {
		return plugin;
	}
	
	/**
   * Returns an image from the image registry.
   * 
   * @param imgId
   * @return
   */
  public Image getImage(String imgId) {
    ImageRegistry reg = getImageRegistry();
    return reg.get(imgId);
  }


  /**
   * Returns an image descriptor for the image file at the given plug-in
   * relative path
   * 
   * @param path
   *            the path
   * @return the image descriptor
   */
  public static ImageDescriptor getImageDescriptor(String path) {
    return imageDescriptorFromPlugin(PLUGIN_ID, path);
  }

  @Override
  protected void initializeImageRegistry(ImageRegistry registry) {
    registry.put(SharedImages.ICON_CHECKED_0, getImageDescriptor(SharedImages.ICON_CHECKED_0));
    registry.put(SharedImages.ICON_CHECKED_0_GRAY, getImageDescriptor(SharedImages.ICON_CHECKED_0_GRAY));
    registry.put(SharedImages.ICON_CHECKED_1, getImageDescriptor(SharedImages.ICON_CHECKED_1));
    registry.put(SharedImages.ICON_CHECKED_1_GRAY, getImageDescriptor(SharedImages.ICON_CHECKED_1_GRAY));

  }


  @Override
  public String getBundleName() {
    return BUNDLE_NAME;
  }

}
