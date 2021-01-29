package com.bichler.astudio.device.opcua;

import java.io.File;
import java.io.PrintStream;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.osgi.framework.BundleContext;

import com.bichler.astudio.utils.activator.InternationalActivator;

/**
 * The activator class controls the plug-in life cycle
 */
public class DeviceActivator extends InternationalActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.bichler.astudio.device.opcua"; //$NON-NLS-1$
	public static final String BUNDLE_NAME = "com.bichler.astudio.device.opcua.resource.custom"; //$NON-NLS-1$
	// The shared instance
	private static DeviceActivator plugin;

	private static final String RUNTIME = "runtime";
	private static final String OPCUA = RUNTIME + "_opcua";
	private static final String DRIVERS = "drivers";
	private static final String LICENSE = "license";
	private static final String WEBSITE = "website";
	private static final String FIRMWARE = "firmware";
	private static final String NODEJSVISUALIZATION = "dataHUB-Xi";
	private static final String TOOLCHAIN = "toolchain";
	private static final String WAKEONLAN = "wakeonlan";
	private static final String DOKU = "docu.pdf";

	/**
	 * The constructor
	 */
	public DeviceActivator() {
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
		DeviceActivator.setPlugin(this);
		// remove all not required Consoles
		IConsole[] consoles = ConsolePlugin.getDefault().getConsoleManager().getConsoles();
		ConsolePlugin.getDefault().getConsoleManager().removeConsoles(consoles);
		final ASConsole console = new ASConsole("Automation Studio Console", null);
		ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] { console });
		PrintStream printStream = new PrintStream(console.newMessageStream());
		// Link standard output stream to the console
		System.setOut(printStream);
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
		DeviceActivator.setPlugin(null);
		super.stop(context);
	}

	public static void setPlugin(DeviceActivator plugin) {
		DeviceActivator.plugin = plugin;
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static DeviceActivator getDefault() {
		return plugin;
	}

	public File getLicenseFile(String license) {
		return getFile(getDefault().getBundle(), Path.ROOT.append(LICENSE).append(license));
	}

	public File getOPCRuntimeFile() {
		return getFile(getDefault().getBundle(), Path.ROOT.append(OPCUA));
	}

	public File getWebsiteFile() {
		return getFile(getDefault().getBundle(), Path.ROOT.append(WEBSITE));
	}

	public File getNodeJsVisualizationFile() {
		return getFile(getDefault().getBundle(), Path.ROOT.append(NODEJSVISUALIZATION));
	}

	public File getFirmwareFile() {
		return getFile(getDefault().getBundle(), Path.ROOT.append(FIRMWARE));
	}

	public File getWakeOnLanFile() {
		return getFile(getDefault().getBundle(), Path.ROOT.append(WAKEONLAN));
	}

	public File getWakeOnLanNodeFile() {
		return getFile(getDefault().getBundle(), Path.ROOT.append("node.sh"));
	}

	public File getWebsiteDocuFile(String version) {
		return getFile(getDefault().getBundle(), Path.ROOT.append(WEBSITE).append(version).append(DOKU));
	}

	public File getWakeOnLanDocuFile(String version) {
		return getFile(getDefault().getBundle(), Path.ROOT.append(WAKEONLAN).append(version).append(DOKU));
	}

	public File getDriverFile() {
		return getFile(getDefault().getBundle(), Path.ROOT.append(OPCUA).append(RUNTIME).append(DRIVERS));
	}

	public File getToolchain() {
		return getFile(getDefault().getBundle(), Path.ROOT.append(TOOLCHAIN));
	}

	public boolean isToolchainInstalled() {
		boolean exist = false;

		File file = getFile(getDefault().getBundle(), Path.ROOT.append(TOOLCHAIN).append("btech_src"));
		if (file != null) {
			exist = file.exists();
		}
		return exist;
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
		registry.put(DeviceSharedImages.ICON_SERVER_START, getImageDescriptor(DeviceSharedImages.ICON_SERVER_START));
		registry.put(DeviceSharedImages.ICON_SERVER_STOP, getImageDescriptor(DeviceSharedImages.ICON_SERVER_STOP));
	}

}
