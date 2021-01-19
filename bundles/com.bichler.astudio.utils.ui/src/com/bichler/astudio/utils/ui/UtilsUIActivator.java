package com.bichler.astudio.utils.ui;

import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.BundleContext;

import com.bichler.astudio.utils.activator.InternationalActivator;
import com.bichler.astudio.utils.ui.images.SharedImages;

public class UtilsUIActivator extends InternationalActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.bichler.astudio.utils.ui"; //$NON-NLS-1$

	public static final String BUNDLE_NAME = "com.bichler.astudio.utils.ui.resource.custom";

	// The shared instance
	private static UtilsUIActivator plugin;

	/**
	 * The constructor
	 */
	public UtilsUIActivator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.
	 * BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.
	 * BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}
	
	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static UtilsUIActivator getDefault() {
		return plugin;
	}
	
	@Override
	public void setLanguageResourceBundle(Locale locale) {
		Locale.setDefault(locale);
		try {
			RESOURCE_BUNDLE = ResourceBundle.getBundle(getBundleName(), locale);
		} catch (Exception e) {
			RESOURCE_BUNDLE = ResourceBundle.getBundle(getBundleName(), Locale.ENGLISH);
		}
	}

	@Override
	public String getBundleName() {
		return BUNDLE_NAME;
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
			registry.put(SharedImages.ICON_LOOK, getImageDescriptor(SharedImages.ICON_LOOK));
		}
	
}
