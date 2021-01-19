package com.bichler.astudio.opcua.statemachine;

import org.osgi.framework.BundleContext;

import com.bichler.astudio.utils.activator.InternationalActivator;

public class StatemachineActivator extends InternationalActivator {

	public static String PLUGIN_ID = "com.bichler.astudio.opcua.statemachine";
	public static final String BUNDLE_NAME = "com.bichler.astudio.opcua.statemachine.resource.custom"; //$NON-NLS-1$
	
	
	private static StatemachineActivator plugin;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.
	 * BundleContext )
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
//	    plugin = this;
		StatemachineActivator.setPlugin(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.
	 * BundleContext )
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		StatemachineActivator.setPlugin(null);
		super.stop(context);
	}

	public static void setPlugin(StatemachineActivator plugin) {
		StatemachineActivator.plugin = plugin;
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static StatemachineActivator getDefault() {
		return plugin;
	}
	@Override
	public String getBundleName() {
		return BUNDLE_NAME;
	}

}
