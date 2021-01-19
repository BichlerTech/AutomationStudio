package com.bichler.astudio.device.core;

import java.io.PrintStream;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.osgi.framework.BundleContext;

import com.bichler.astudio.utils.activator.InternationalActivator;

public class DevCoreActivator extends InternationalActivator {
	// The plug-in ID
	public static final String PLUGIN_ID = "com.bichler.astudio.device.core"; //$NON-NLS-1$
	public static final String BUNDLE_NAME = "com.bichler.astudio.device.core.resource.custom"; //$NON-NLS-1$
	// The shared instance
	private static DevCoreActivator plugin;

	/**
	 * The constructor
	 */
	public DevCoreActivator() {
	}

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
		DevCoreActivator.setPlugin(this);
		// remove all not required Consoles
		IConsole[] consoles = ConsolePlugin.getDefault().getConsoleManager().getConsoles();
		ConsolePlugin.getDefault().getConsoleManager().removeConsoles(consoles);
		final ASConsole console = new ASConsole("Automation Studio Console", null);
		ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] { console });
		PrintStream printStream = new PrintStream(console.newMessageStream());
		// Link standard output stream to the console System.setOut(printStream);
		// Link error output stream to the console
		System.setErr(printStream);
		 
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.
	 * BundleContext )
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		DevCoreActivator.setPlugin(null);
		super.stop(context);
	}

	public static void setPlugin(DevCoreActivator plugin) {
		DevCoreActivator.plugin = plugin;
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static DevCoreActivator getDefault() {
		return plugin;
	}

	@Override
	public String getBundleName() {
		return BUNDLE_NAME;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in relative
	 * path
	 * 
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	@Override
	protected void initializeImageRegistry(ImageRegistry registry) {
		registry.put(DevCoreSharedImages.ICON_SERVER_START, getImageDescriptor(DevCoreSharedImages.ICON_SERVER_START));
		registry.put(DevCoreSharedImages.ICON_SERVER_STOP, getImageDescriptor(DevCoreSharedImages.ICON_SERVER_STOP));
	}

}
