package com.bichler.astudio.opcua.datadictionary.modeler;

import org.osgi.framework.BundleContext;

import com.bichler.astudio.utils.activator.InternationalActivator;

public class DataDictionaryModelerActivator extends InternationalActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.bichler.astudio.opcua.datadictionary.modeler"; //$NON-NLS-1$
	public static final String BUNDLE_NAME = "com.bichler.astudio.opcua.datadictionary.modeler.resource.custom";
	// The shared instance
	private static DataDictionaryModelerActivator plugin;
	
	/**
	 * The constructor
	 */
	public DataDictionaryModelerActivator() {
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
	public static DataDictionaryModelerActivator getDefault() {
		return plugin;
	}

	@Override
	public String getBundleName() {
		return BUNDLE_NAME;
	}

}
