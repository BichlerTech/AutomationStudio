package com.bichler.astudio.log.view;

import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.osgi.framework.BundleContext;

import com.bichler.astudio.log.view.preferences.ASLogPreferenceManager;
import com.bichler.astudio.utils.activator.InternationalActivator;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends InternationalActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.bichler.astudio.log.view"; //$NON-NLS-1$
	public static final String BUNDLE_NAME = "com.bichler.astudio.log.view.resource.custom";
	// The shared instance
	private static Activator plugin;

	private ASLogPreferenceManager preferenceManager;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		this.preferenceManager = new ASLogPreferenceManager(getPreferenceStore());
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
	public static Activator getDefault() {
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
	
	public ASLogPreferenceManager getPreferenceManager(){
		return this.preferenceManager;
	}

	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		reg.put(HBLog4jSharedImages.DEBUG, getImageDescriptor(HBLog4jSharedImages.DEBUG));
		reg.put(HBLog4jSharedImages.ERROR, getImageDescriptor(HBLog4jSharedImages.ERROR));
		reg.put(HBLog4jSharedImages.FATAL, getImageDescriptor(HBLog4jSharedImages.FATAL));
		reg.put(HBLog4jSharedImages.INFO, getImageDescriptor(HBLog4jSharedImages.INFO));
		reg.put(HBLog4jSharedImages.WARN, getImageDescriptor(HBLog4jSharedImages.WARN));
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
