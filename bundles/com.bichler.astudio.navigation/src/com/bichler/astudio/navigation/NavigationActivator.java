package com.bichler.astudio.navigation;

import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.osgi.framework.BundleContext;

import com.bichler.astudio.utils.activator.InternationalActivator;

/**
 * The activator class controls the plug-in life cycle
 */
public class NavigationActivator extends InternationalActivator {

	//public static ConnectionsHostManager hostManager = null;
	
	/**
	 * For default navigation plugin we don't need to load any image.
	 */
	@Override
	protected void initializeImageRegistry(ImageRegistry registry) {
		registry.put(NavigationSharedImages.ICON_COMET, getImageDescriptor(NavigationSharedImages.ICON_COMET));
		// registry.put(ConnectionsSharedImages.ICON_HOSTCONNECTION_GREEN, getImageDescriptor(ConnectionsSharedImages.ICON_HOSTCONNECTION_GREEN));
	}

	// The plug-in ID
	public static final String PLUGIN_ID = "com.bichler.astudio.navigation"; //$NON-NLS-1$
	public static final String BUNDLE_NAME = "com.bichler.astudio.navigation.resource.custom";
	
	// The shared instance
	private static NavigationActivator plugin;
	
	/**
	 * The constructor
	 */
	public NavigationActivator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
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
	public static NavigationActivator getDefault() {
		return plugin;
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

	@Override
	public String getBundleName() {
		return BUNDLE_NAME;
	}
	
	@Override
	public void setLanguageResourceBundle(Locale locale){
		Locale.setDefault(locale);
		try {
			RESOURCE_BUNDLE = ResourceBundle.getBundle(getBundleName(),
					locale);
		} catch (Exception e) {
			RESOURCE_BUNDLE = ResourceBundle.getBundle(getBundleName(),
					Locale.ENGLISH);
		}
	}
}
