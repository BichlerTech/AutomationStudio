package com.bichler.astudio.p2;

import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.bichler.astudio.utils.activator.InternationalActivator;

/**
 * The activator class controls the plug-in life cycle
 */
public class P2Activator extends InternationalActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.bichler.astudio.p2"; //$NON-NLS-1$
	public static final String BUNDLE_NAME = "com.bichler.astudio.p2.resource.custom";

	// The shared instance
	private static P2Activator plugin;
	
	/**
	 * The constructor
	 */
	public P2Activator() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static P2Activator getDefault() {
		return plugin;
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
			System.setProperty("osgi.nl", locale.toString());
		} catch (Exception e) {
			RESOURCE_BUNDLE = ResourceBundle.getBundle(getBundleName(), Locale.ENGLISH);
		}
	}


}
