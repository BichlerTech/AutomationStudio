package com.bichler.astudio.filesystem.ui;

import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.BundleContext;

import com.bichler.astudio.utils.activator.InternationalActivator;

public class FilesystemUIActivator extends InternationalActivator {
	
	public static final String PLUGIN_ID = "com.bichler.astudio.filesystem";
	public static final String BUNDLE_NAME = "com.bichler.astudio.filesystem.resource.custom";
	// The shared instance
	private static FilesystemUIActivator plugin;

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		plugin = this;
		
		registerP2Policy(bundleContext);
	}

	private void registerP2Policy(BundleContext context) {
		// policyRegistration = context.registerService(Policy.class.getName(), new CloudPolicy(), null);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		plugin = null;
		super.stop(bundleContext);
	}

	
	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static FilesystemUIActivator getDefault() {
		return plugin;
	}
	
	@Override
	protected void initializeImageRegistry(ImageRegistry registry) {
		registry.put(FileSystemSharedImages.ICON_PALETTE_FOLDER, getImageDescriptor(FileSystemSharedImages.ICON_PALETTE_FOLDER));
	}
	
	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public Image getImage(String key) {
	  ImageRegistry reg = getImageRegistry();
    return reg.get(key);
  }
	
	@Override
	public void setLanguageResourceBundle(Locale locale) {
		Locale.setDefault(locale);
		try {
			RESOURCE_BUNDLE = ResourceBundle.getBundle(getBundleName(),
					locale);
		} catch (Exception e) {
			RESOURCE_BUNDLE = ResourceBundle.getBundle(getBundleName(),
					Locale.ENGLISH);
		}
	}

	@Override
	public String getBundleName() {
		return BUNDLE_NAME;
	}
}
