package com.bichler.astudio.images.common;

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

public class CommonImagesActivator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "com.bichler.astudio.images.common";
	private static CommonImagesActivator plugin;

	private static final String IMG_PATH = "icons";
	private static final IPath IMG_LAUNCH = new Path("astudio").append("launch");
	private static final String IMG_COMMON = "common";
	/**
	 * SIZE FOLDER
	 */
	public static final String IMG_16 = "16";
	public static final String IMG_24 = "24";
	public static final String IMG_32 = "32";
	public static final String IMG_64 = "64";
	public static final String IMG_128 = "128";
	/**
	 * COMMON
	 */
	public static final String IMPORT = "import.png";
	public static final String ADD = "add.png";
	public static final String EDIT = "edit.png";
	public static final String DELETE = "delete.png";
	public static final String PROJECT = "project.png";
	public static final String SERVER_STANDBY = "server_standby.png";
	public static final String SERVER_RUNNING = "server_running.png";
	public static final String SERVER_DISCOVERY = "server_discovery.png";
	public static final String SERVER_ENDPOINT = "endpoint.png";
	public static final String VISUSERVER = "visuserver.png";

	public static final String DATACHANGE = "datachange.png";
	public static final String DATAACCESS = "dataaccess.png";
	public static final String EVENTS = "event.png";
	public static final String ALARMCONDITION = "alarm.png";
	public static final String HISTORY = "historical.png";
	public static final String PROGRAM = "program.png";

	public static final String SECURITY_NONE = "security_none.png";
	public static final String SECURITY_SECURE = "security_secure.png";
	public static final String SECURITY_CERTIFICATION = "security_certificate.png";
	public static final String SECURITY_KEY = "security_key.png";
	public static final String SECURITY_CONFIGURATION = "security_configuration.png";

	public static final String LOG = "log.png";
	public static final String USER = "user.png";

	public static final String MISC = "misc.png";
	public static final String CERTIFICATE = "certificate.png";
	public static final String KEY = "key.png";

	public static final String X_DATAPOINTS = "datapoints_X.png";
	public static final String X_USER = "user_X.png";
	
	public static final String OPEN_FOLDER = "openFolder.png";

	public CommonImagesActivator() {

	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static Image getImage(String size, String name) {
		return CommonImagesActivator.getDefault().getRegisteredImage(size, name);
	}
	
	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static CommonImagesActivator getDefault() {
		return plugin;
	}

	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		Bundle bundle = Platform.getBundle(PLUGIN_ID);

		Path root = new Path(IMG_PATH);


		
		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_16, IMPORT);
		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_24, IMPORT);
		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_16, ADD);
		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_24, ADD);
		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_32, EDIT);
		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_16, DELETE);
		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_24, DELETE);
		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_16, PROJECT);
		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_16,
				VISUSERVER);

		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_16,
				DATAACCESS);
		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_24,
				DATAACCESS);
		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_32,
				DATAACCESS);

		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_16,
				DATACHANGE);
		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_24,
				DATACHANGE);
		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_32,
				DATACHANGE);

		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_16, EVENTS);
		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_24, EVENTS);
		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_32, EVENTS);

		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_16,
				ALARMCONDITION);
		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_24,
				ALARMCONDITION);
		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_32,
				ALARMCONDITION);

		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_16, HISTORY);
		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_24, HISTORY);
		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_32, HISTORY);

		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_16, PROGRAM);
		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_24, PROGRAM);
		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_32, PROGRAM);

		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_16,
				SERVER_STANDBY);
		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_24,
				SERVER_STANDBY);
		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_32,
				SERVER_STANDBY);

		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_16,
				SERVER_RUNNING);
		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_32,
				SERVER_RUNNING);

		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_16,
				SERVER_DISCOVERY);
		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_32,
				SERVER_DISCOVERY);

		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_16,
				SERVER_ENDPOINT);

		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_16,
				SECURITY_NONE);
		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_32,
				SECURITY_NONE);

		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_16,
				SECURITY_SECURE);
		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_32,
				SECURITY_SECURE);

		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_16,
				SECURITY_CERTIFICATION);
		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_16,
				SECURITY_CONFIGURATION);
		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_16,
				SECURITY_KEY);

		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_16, MISC);

		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_16,
				CERTIFICATE);
		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_16, KEY);
		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_16, LOG);
		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_24, LOG);
		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_16, USER);
		
		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_16, X_DATAPOINTS);
		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_16, X_USER);
		
		initializeIcons(bundle, reg, root.append(IMG_COMMON), IMG_16, OPEN_FOLDER);
	}

	private void initializeIcons(Bundle bundle, ImageRegistry reg, IPath path,
			String size, String icon) {

		IPath id = new Path(size).append(icon);
		IPath imgpath = path.append(id);

		URL url = FileLocator.find(bundle, imgpath, null);
		reg.put(id.toOSString(), ImageDescriptor.createFromURL(url));
	}

	/**
	 * 
	 * @param size
	 *            FOLDER_16, FOLDER_32
	 * @param name
	 *            ICON CONSTANT
	 * @return
	 */
	public Image getRegisteredImage(String size, String name) {
		return getImageRegistry().get(new Path(size).append(name).toOSString());
	}
}
