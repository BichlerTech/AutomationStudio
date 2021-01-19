package com.bichler.astudio.opcua.libs;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class OPCLibsActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.bichler.astudio.opcua.libs"; //$NON-NLS-1$

	private static final String FOLDER_LIBS = "libs";

	public static final String JAR_STACK = "opc-ua-stack-1.4.1.1.jar";
	public static final String JAR_CORE = "opccore.jar";
	public static final String JAR_SERVER = "opcserver.jar";
	public static final String JAR_CLIENT = "opcclient.jar";
//	public static final String JAR_LICENSE = "com.hbsoft.license.jar";

	public static final String JAR_COMDRV = "ComDRV.jar";

	public static final String JAR_DRVBASE = "driver_base.jar";
//	public static final String PREFERENCE_OPCUA_DO_COMPILEL = "modeldocompile";

	// The shared instance
	private static OPCLibsActivator plugin;

	/**
	 * The constructor
	 */
	public OPCLibsActivator() {
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
	public static OPCLibsActivator getDefault() {
		return plugin;
	}

	public static String getLibraryPath(String library) {
		URL url = FileLocator.find(getDefault().getBundle(),
				Path.ROOT.append(FOLDER_LIBS).append(library), null);

		if (url != null) {
			try {
				URL url2 = FileLocator.toFileURL(url);
				// remove '/' beginning of path if possible
				return new File(url2.getFile()).getPath();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
