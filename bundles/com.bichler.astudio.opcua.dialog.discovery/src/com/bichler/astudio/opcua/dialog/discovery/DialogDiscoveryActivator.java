package com.bichler.astudio.opcua.dialog.discovery;

import java.util.Locale;
import java.util.ResourceBundle;

import org.osgi.framework.BundleContext;

import com.bichler.astudio.utils.activator.InternationalActivator;

/**
 * The activator class controls the plug-in life cycle
 */
public class DialogDiscoveryActivator extends InternationalActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.bichler.astudio.opcua.dialog.discovery"; //$NON-NLS-1$
	public static final String BUNDLE_NAME = "com.bichler.astudio.opcua.dialog.discovery.resource.custom";

	// The shared instance
	private static DialogDiscoveryActivator plugin;

	/**
	 * The constructor
	 */
	public DialogDiscoveryActivator() {

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
	public static DialogDiscoveryActivator getDefault() {
		return plugin;
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
