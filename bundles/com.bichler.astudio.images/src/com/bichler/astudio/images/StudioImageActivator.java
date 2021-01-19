package com.bichler.astudio.images;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class StudioImageActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.bichler.astudio.images"; //$NON-NLS-1$

	// The shared instance
	private static StudioImageActivator plugin;

	/**
	 * The constructor
	 */
	public StudioImageActivator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.
	 * BundleContext )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.
	 * BundleContext )
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
	public static StudioImageActivator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in relative
	 * path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		super.initializeImageRegistry(reg);

		registerImage(reg, StudioImages.ICON_HOSTCONNECTION, StudioImages.ICON_HOSTCONNECTION);

		registerImage(reg, StudioImages.ICON_HOSTCONNECTION_GREEN, StudioImages.ICON_HOSTCONNECTION_GREEN);
		registerImage(reg, StudioImages.ICON_DATASERVER, StudioImages.ICON_DATASERVER);
		registerImage(reg, StudioImages.ICON_SERVER, StudioImages.ICON_SERVER);
		registerImage(reg, StudioImages.ICON_OPCUADRIVERS, StudioImages.ICON_OPCUADRIVERS);
		registerImage(reg, StudioImages.ICON_OPCUACONFIGURATION, StudioImages.ICON_OPCUACONFIGURATION);
		registerImage(reg, StudioImages.ICON_OPCUAHISTORY, StudioImages.ICON_OPCUAHISTORY);
		registerImage(reg, StudioImages.ICON_OPCUAVERSION, StudioImages.ICON_OPCUAVERSION);
		registerImage(reg, StudioImages.ICON_OPCUADRIVERDPS, StudioImages.ICON_OPCUADRIVERDPS);
		registerImage(reg, StudioImages.ICON_OPCUACERTIFICATE, StudioImages.ICON_OPCUACERTIFICATE);
		registerImage(reg, StudioImages.ICON_SCRIPTS, StudioImages.ICON_SCRIPTS);
		registerImage(reg, StudioImages.ICON_OPCUAINFORMATIONMODEL, StudioImages.ICON_OPCUAINFORMATIONMODEL);
		registerImage(reg, StudioImages.ICON_CHECKED_1, StudioImages.ICON_CHECKED_1);
		registerImage(reg, StudioImages.ICON_CHECKED_0, StudioImages.ICON_CHECKED_0);
		registerImage(reg, StudioImages.ICON_WIZARD_OPC_UA_SERVER_ADD, StudioImages.ICON_WIZARD_OPC_UA_SERVER_ADD);
		registerImage(reg, StudioImages.ICON_WIZARD_DRIVER_ADD, StudioImages.ICON_WIZARD_DRIVER_ADD);
		registerImage(reg, StudioImages.ICON_WIZARD_CERTIFICATE_ADD, StudioImages.ICON_WIZARD_DRIVER_ADD);
		registerImage(reg, StudioImages.ICON_WIZARD_SCRIPT_ADD, StudioImages.ICON_WIZARD_SCRIPT_ADD);
		registerImage(reg, StudioImages.ICON_COMET_16, StudioImages.ICON_COMET_16);
		registerImage(reg, StudioImages.ICON_HMI, StudioImages.ICON_HMI);
		registerImage(reg, StudioImages.ICON_IECPROJECT, StudioImages.ICON_IECPROJECT);
		registerImage(reg, StudioImages.ICON_IECHARDWARE, StudioImages.ICON_IECHARDWARE);
		registerImage(reg, StudioImages.ICON_IECDATATYPE, StudioImages.ICON_IECDATATYPE);
		registerImage(reg, StudioImages.ICON_IECDT, StudioImages.ICON_IECDT);
		registerImage(reg, StudioImages.ICON_IECGLOBALVAR, StudioImages.ICON_IECGLOBALVAR);
		registerImage(reg, StudioImages.ICON_IECTIMEBAR, StudioImages.ICON_IECTIMEBAR);
		registerImage(reg, StudioImages.ICON_IECCONTENT_PROPOSAL, StudioImages.ICON_IECCONTENT_PROPOSAL);
		registerImage(reg, StudioImages.ICON_IECPICTURE, StudioImages.ICON_IECPICTURE);

		registerImage(reg, StudioImages.ICON_DELETE, StudioImages.ICON_DELETE);
		registerImage(reg, StudioImages.ICON_ADD, StudioImages.ICON_ADD);
		registerImage(reg, StudioImages.ICON_IMPORT, StudioImages.ICON_IMPORT);
		registerImage(reg, StudioImages.ICON_TRUST, StudioImages.ICON_TRUST);
		registerImage(reg, StudioImages.ICON_ARROW_DOWN, StudioImages.ICON_ARROW_DOWN);
		registerImage(reg, StudioImages.ICON_ARROW_UP, StudioImages.ICON_ARROW_UP);
		registerImage(reg, StudioImages.ICON_SELECT_NONE, StudioImages.ICON_SELECT_NONE);
		registerImage(reg, StudioImages.ICON_SELECT_ALL, StudioImages.ICON_SELECT_ALL);
		registerImage(reg, StudioImages.ICON_SEARCH, StudioImages.ICON_SEARCH);
		registerImage(reg, StudioImages.ICON_16_FUNCTIONBLOCK, StudioImages.ICON_16_FUNCTIONBLOCK);
		registerImage(reg, StudioImages.ICON_16_LADDER, StudioImages.ICON_16_LADDER);
		registerImage(reg, StudioImages.ICON_16_ST, StudioImages.ICON_16_ST);

		registerImage(reg, StudioImages.ICON_32_FUNCTIONBLOCK, StudioImages.ICON_32_FUNCTIONBLOCK);
		registerImage(reg, StudioImages.ICON_32_LADDER, StudioImages.ICON_32_LADDER);
		registerImage(reg, StudioImages.ICON_32_ST, StudioImages.ICON_32_ST);
	}

	public static Image getImage(String key) {
		return getDefault().getImageRegistry().get(key);
	}

	private void registerImage(ImageRegistry reg, String key, String path) {
		Bundle bundle = Platform.getBundle(PLUGIN_ID);
		ImageDescriptor myImage = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path(path), null));
		reg.put(key, myImage);
	}

}
