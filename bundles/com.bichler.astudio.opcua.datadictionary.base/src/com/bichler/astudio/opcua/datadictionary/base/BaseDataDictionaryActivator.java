package com.bichler.astudio.opcua.datadictionary.base;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;


/**
 * The activator class controls the plug-in life cycle
 */
public class BaseDataDictionaryActivator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		BaseDataDictionaryActivator.context = bundleContext;
	}

	public void stop(BundleContext bundleContext) throws Exception {
		BaseDataDictionaryActivator.context = null;
	}
}
