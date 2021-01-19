package com.bichler.astudio.editor.siemens;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.osgi.framework.BundleContext;

import com.bichler.astudio.opcua.OPCUADriverRegistry;
import com.bichler.astudio.utils.activator.InternationalActivator;

/**
 * The activator class controls the plug-in life cycle
 */
public class SiemensActivator extends InternationalActivator {
	// The plug-in ID
	private static final String PLUGIN_ID = "com.bichler.astudio.editor.siemens.1.0.0"; //$NON-NLS-1$
	public static final String BUNDLE_NAME = "com.bichler.astudio.editor.siemens.resource.custom"; //$NON-NLS-1$
	// The shared instance
	private static SiemensActivator plugin;

	/**
	 * The constructor
	 */
	public SiemensActivator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.
	 * BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		OPCUADriverRegistry.drivers.put(context.getBundle().getSymbolicName(), this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.
	 * BundleContext)
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
	public static SiemensActivator getDefault() {
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
		registry.put(SiemensSharedImages.ICON_CHECKED_0, getImageDescriptor(SiemensSharedImages.ICON_CHECKED_0));
		registry.put(SiemensSharedImages.ICON_CHECKED_0_GRAY,
				getImageDescriptor(SiemensSharedImages.ICON_CHECKED_0_GRAY));
		registry.put(SiemensSharedImages.ICON_CHECKED_1, getImageDescriptor(SiemensSharedImages.ICON_CHECKED_1));
		registry.put(SiemensSharedImages.ICON_CHECKED_1_GRAY,
				getImageDescriptor(SiemensSharedImages.ICON_CHECKED_1_GRAY));
		registry.put(SiemensSharedImages.ICON_DATEPOINT, getImageDescriptor(SiemensSharedImages.ICON_DATEPOINT));
		registry.put(SiemensSharedImages.ICON_DATEPOINTLEAF,
				getImageDescriptor(SiemensSharedImages.ICON_DATEPOINTLEAF));
		registry.put(SiemensSharedImages.ICON_DELETE, getImageDescriptor(SiemensSharedImages.ICON_DELETE));
		registry.put(SiemensSharedImages.ICON_ADD, getImageDescriptor(SiemensSharedImages.ICON_ADD));
		registry.put(SiemensSharedImages.ICON_IMPORT, getImageDescriptor(SiemensSharedImages.ICON_IMPORT));
		registry.put(SiemensSharedImages.ICON_GEARS, getImageDescriptor(SiemensSharedImages.ICON_GEARS));
		registry.put(SiemensSharedImages.ICON_LOADDP, getImageDescriptor(SiemensSharedImages.ICON_LOADDP));
		registry.put(SiemensSharedImages.ICON_EXPORT, getImageDescriptor(SiemensSharedImages.ICON_EXPORT));
		registry.put(SiemensSharedImages.ICON_SEARCH, getImageDescriptor(SiemensSharedImages.ICON_SEARCH));
		registry.put(SiemensSharedImages.ICON_SELECTALL, getImageDescriptor(SiemensSharedImages.ICON_SELECTALL));
		registry.put(SiemensSharedImages.ICON_SELECTNONE, getImageDescriptor(SiemensSharedImages.ICON_SELECTNONE));
		registry.put(SiemensSharedImages.ICON_VALIDATEDP, getImageDescriptor(SiemensSharedImages.ICON_VALIDATEDP));
	}

	@Override
	public String getBundleName() {
		return BUNDLE_NAME;
	}
}
