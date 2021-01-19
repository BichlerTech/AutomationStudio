package com.bichler.opcua.statemachine.reverse;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class ReverseEngineActivator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		ReverseEngineActivator.context = bundleContext;
	}

	public void stop(BundleContext bundleContext) throws Exception {
		ReverseEngineActivator.context = null;
	}

}
