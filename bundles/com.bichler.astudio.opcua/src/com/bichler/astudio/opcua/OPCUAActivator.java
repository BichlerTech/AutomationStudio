package com.bichler.astudio.opcua;

import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.osgi.framework.BundleContext;

import com.bichler.astudio.opcua.constants.OPCUAConstants;
import com.bichler.astudio.utils.activator.InternationalActivator;
import com.bichler.astudio.connections.ConnectionsHostManager;

/**
 * The activator class controls the plug-in life cycle
 */
public class OPCUAActivator extends InternationalActivator {
	public ResourceBundle RESOURCE_BUNDLE;
	// The plug-in ID
	public static final String PLUGIN_ID = "com.bichler.astudio.opcua"; //$NON-NLS-1$
	public static final String BUNDLE_NAME = "com.bichler.astudio.opcua.resource.custom";
	// The shared instance
	private static OPCUAActivator plugin;

	/**
	 * The constructor
	 */
	public OPCUAActivator() {
		/**
		 * File file = new File("/"); long totalSpace = file.getTotalSpace(); //total
		 * disk space in bytes. long usableSpace = file.getUsableSpace(); ///unallocated
		 * / free disk space in bytes. long freeSpace = file.getFreeSpace();
		 * //unallocated / free disk space in bytes.
		 * 
		 * System.out.println(" === Partition Detail ===");
		 * 
		 * System.out.println(" === bytes ==="); System.out.println("Total size : " +
		 * totalSpace + " bytes"); System.out.println("Space free : " + usableSpace + "
		 * bytes"); System.out.println("Space free : " + freeSpace + " bytes");
		 */
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
		Locale locale = Locale.getDefault();
		try {
			RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, locale);
		} catch (Exception e) {
			RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, Locale.ENGLISH);
		}
		setDefaultValues();
		// opc ua connections
		ConnectionsHostManager opcuaConnectionsManager = new ConnectionsHostManager();
		IPreferenceStore opcuastore = OPCUAActivator.getDefault().getPreferenceStore();
		String opcuaFolder = opcuastore.getString(OPCUAConstants.OPCUARuntime);
		String opcuaServers = opcuastore.getString(OPCUAConstants.ASOPCUAServersPath);
		opcuaConnectionsManager.importHostsFromRuntimeStructure(opcuaFolder, opcuaServers);
	}

	/**
	 * set default values
	 */
	private void setDefaultValues() {
		IPreferenceStore store = OPCUAActivator.getDefault().getPreferenceStore();
		String opcuaFolder = store.getString(OPCUAConstants.OPCUARuntime);
		String opcuaServers = store.getString(OPCUAConstants.ASOPCUAServersPath);
		String opcuaDriverFolder = store.getString(OPCUAConstants.ASOPCUADriversFolder);
		String opcuaDriverFile = store.getString(OPCUAConstants.ASOPCUADriverConfigFile);
		
		String opcuaModuleFolder = store.getString(OPCUAConstants.ASOPCUAModulesFolder);
		String opcuaModuleFile = store.getString(OPCUAConstants.ASOPCUAModuleConfigFile);
		/**
		 * if all values are empty
		 */
		if (opcuaFolder.isEmpty() && opcuaServers.isEmpty() && opcuaDriverFolder.isEmpty()
				&& opcuaDriverFile.isEmpty()) {
			store.setValue(OPCUAConstants.ASOPCUADriversFolder, "drivers");
			store.setValue(OPCUAConstants.ASOPCUADriverConfigFile, "driver.com");
			store.setValue(OPCUAConstants.ASOPCUAModulesFolder, "modules");
			store.setValue(OPCUAConstants.ASOPCUAModuleConfigFile, "module.com");
			store.setValue(OPCUAConstants.ASOPCUAServersPath, "servers");
			store.setValue(OPCUAConstants.ASOPCUARuntimePath, "runtime");
			store.setValue(OPCUAConstants.OPCUAConnectionsFile, "");
			store.setValue(OPCUAConstants.OPCUARuntime, "opc_ua_server");
			store.setValue(OPCUAConstants.OPCUASHA, false);
			store.setValue(OPCUAConstants.OPCUAOnlyCombox, false);
			store.setValue(OPCUAConstants.OPCUADoCompileJar, true);
			store.setValue(OPCUAConstants.OPCUADoCompileAnsiC, true);
		}

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
	public static OPCUAActivator getDefault() {
		return plugin;
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
		super.initializeImageRegistry(registry);
		// registry.put(OPCUASharedImages.ICON_ADD,
		// getImageDescriptor(OPCUASharedImages.ICON_ADD));
		// registry.put(OPCUASharedImages.ICON_DELETE,
		// getImageDescriptor(OPCUASharedImages.ICON_DELETE));
		registry.put(OPCUASharedImages.ICON_CHECKED_0, getImageDescriptor(OPCUASharedImages.ICON_CHECKED_0));
		registry.put(OPCUASharedImages.ICON_CHECKED_1, getImageDescriptor(OPCUASharedImages.ICON_CHECKED_1));
	}

	@Override
	public String getBundleName() {
		return BUNDLE_NAME;
	}
}
