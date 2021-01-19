package com.bichler.astudio.opcua.javacommand;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
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
public class JavaCommandActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.bichler.astudio.opcua.javacommand"; //$NON-NLS-1$
	// The shared instance
	private static JavaCommandActivator plugin;

	private static final String IMG_PATH = "icons";
	public static final String IMG_STARTCMD = "startcmd.png";

	/**
	 * The constructor
	 */
	public JavaCommandActivator() {
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
	public static JavaCommandActivator getDefault() {
		return plugin;
	}

	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		Bundle bundle = Platform.getBundle(PLUGIN_ID);

		Path root = new Path(IMG_PATH);

		initializeIcons(bundle, reg, root, IMG_STARTCMD);
	}

	private void initializeIcons(Bundle bundle, ImageRegistry reg, IPath path,
			String icon) {

		IPath imgpath =path.append(icon);
		URL url = FileLocator.find(bundle, imgpath, null);
		reg.put(imgpath.toOSString(), ImageDescriptor.createFromURL(url));
	}

	/**
	 * 
	 * @param size
	 *            FOLDER_16, FOLDER_32
	 * @param name
	 *            ICON CONSTANT
	 * @return
	 */
	public Image getRegisteredImage(String name) {
		return getImageRegistry().get(new Path(IMG_PATH).append(name).toOSString());
	}

	
}
