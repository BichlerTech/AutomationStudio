package com.bichler.astudio.connections;

import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.osgi.framework.BundleContext;

import com.bichler.astudio.connections.utils.ConnectionsSharedImages;
import com.bichler.astudio.utils.activator.InternationalActivator;

/**
 * The activator class controls the plug-in life cycle
 */
public class ConnectionsActivator extends InternationalActivator {
	// The plug-in ID
	public static final String PLUGIN_ID = "com.bichler.astudio.connections"; //$NON-NLS-1$

	public static final String BUNDLE_NAME = "com.bichler.astudio.connections.resource.custom";

	// The shared instance
	private static ConnectionsActivator plugin;

//	public boolean freeze = false;
	/**
	 * The constructor
	 */
	public ConnectionsActivator() {
		try {
			RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME,
					Locale.getDefault());
		} catch (Exception e) {
			RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME,
					Locale.ENGLISH);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
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
	public static ConnectionsActivator getDefault() {
		return plugin;
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
		registry.put(ConnectionsSharedImages.ICON_HOSTCONNECTION,
				getImageDescriptor(ConnectionsSharedImages.ICON_HOSTCONNECTION));
		registry.put(
				ConnectionsSharedImages.ICON_HOSTCONNECTION_GREEN,
				getImageDescriptor(ConnectionsSharedImages.ICON_HOSTCONNECTION_GREEN));
		registry.put(
				ConnectionsSharedImages.ICON_WIZARD_PROJECT_ADD,
				getImageDescriptor(ConnectionsSharedImages.ICON_WIZARD_PROJECT_ADD));
	}

	@Override
	public String getBundleName() {
		return BUNDLE_NAME;
	}

	@Override
	public void setLanguageResourceBundle(Locale locale) {
		Locale.setDefault(locale);
		try {
			RESOURCE_BUNDLE = ResourceBundle.getBundle(getBundleName(), locale);
		} catch (Exception e) {
			RESOURCE_BUNDLE = ResourceBundle.getBundle(getBundleName(),
					Locale.ENGLISH);
		}
	}
}
