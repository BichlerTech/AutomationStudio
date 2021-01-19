package com.bichler.astudio.opcua.addressspace.xml;

import org.osgi.framework.BundleContext;

import com.bichler.astudio.utils.activator.InternationalActivator;

/**
 * The activator class controls the plug-in life cycle
 */
public class XMLActivator extends InternationalActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.bichler.astudio.opcua.addressspace.xml"; //$NON-NLS-1$
	public static final String BUNDLE_NAME = "com.bichler.astudio.opcua.addressspace.xml.resource.custom";
	
	// The shared instance
	private static XMLActivator plugin;
	
	/**
	 * The constructor
	 */
	public XMLActivator() {
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
	public static XMLActivator getDefault() {
		return plugin;
	}
	
	public static void setPlugin(XMLActivator plugin) {
		XMLActivator.plugin = plugin;
	}
	
	@Override
	public String getBundleName() {
		return BUNDLE_NAME;
	}

}
