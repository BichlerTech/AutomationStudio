package com.bichler.astudio.editor.aggregated;

import com.bichler.astudio.opcua.OPCUADriverRegistry;
import com.bichler.astudio.utils.activator.InternationalActivator;

import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.osgi.framework.BundleContext;


/**
 * The activator class controls the plug-in life cycle
 */
public class AggregatedActivator extends InternationalActivator {

	// The plug-in ID
	private static final String PLUGIN_ID = "com.bichler.astudio.editor.aggregated.1.0.0"; //$NON-NLS-1$
	public static final String BUNDLE_NAME = "com.bichler.astudio.editor.aggregated.resource.custom"; //$NON-NLS-1$
	// The shared instance
	private static AggregatedActivator plugin;

	/**
	 * The constructor
	 */
	public AggregatedActivator() {
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
		OPCUADriverRegistry.drivers.put(context.getBundle().getSymbolicName(), this);
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
	public static AggregatedActivator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	@Override
	public String getBundleName() {
		return BUNDLE_NAME;
	}

	@Override
	public void setLanguageResourceBundle(Locale locale) {
		Locale.setDefault(locale);
		try {
			RESOURCE_BUNDLE = ResourceBundle.getBundle(getBundleName(), locale);
		} catch (Exception e) {
			RESOURCE_BUNDLE = ResourceBundle.getBundle(getBundleName(), Locale.ENGLISH);
		}
	}

	@Override
	protected void initializeImageRegistry(ImageRegistry registry) {
		registry.put(AggregatedSharedImages.ICON_CHECKED_0, getImageDescriptor(AggregatedSharedImages.ICON_CHECKED_0));
		registry.put(AggregatedSharedImages.ICON_CHECKED_0_GRAY,
				getImageDescriptor(AggregatedSharedImages.ICON_CHECKED_0_GRAY));
		registry.put(AggregatedSharedImages.ICON_CHECKED_1, getImageDescriptor(AggregatedSharedImages.ICON_CHECKED_1));
		registry.put(AggregatedSharedImages.ICON_CHECKED_1_GRAY,
				getImageDescriptor(AggregatedSharedImages.ICON_CHECKED_1_GRAY));

		// registry.put(AggregatedSharedImages.ICON_ADD,
		// getImageDescriptor(AggregatedSharedImages.ICON_ADD));
		registry.put(AggregatedSharedImages.ICON_DELETE, getImageDescriptor(AggregatedSharedImages.ICON_DELETE));
		registry.put(AggregatedSharedImages.ICON_IMPORT, getImageDescriptor(AggregatedSharedImages.ICON_IMPORT));

		registry.put(AggregatedSharedImages.ICONS_DATATYPE_IMG,
				getImageDescriptor(AggregatedSharedImages.ICONS_DATATYPE_IMG));
		registry.put(AggregatedSharedImages.ICONS_METHOD_IMG,
				getImageDescriptor(AggregatedSharedImages.ICONS_METHOD_IMG));
		registry.put(AggregatedSharedImages.ICONS_OBJECT_IMG,
				getImageDescriptor(AggregatedSharedImages.ICONS_OBJECT_IMG));
		registry.put(AggregatedSharedImages.ICONS_FOLDER_IMG,
				getImageDescriptor(AggregatedSharedImages.ICONS_FOLDER_IMG));
		registry.put(AggregatedSharedImages.ICONS_OBJECTTYPE_IMG,
				getImageDescriptor(AggregatedSharedImages.ICONS_OBJECTTYPE_IMG));
		registry.put(AggregatedSharedImages.ICONS_REFERENCETYPE_IMG,
				getImageDescriptor(AggregatedSharedImages.ICONS_REFERENCETYPE_IMG));
		registry.put(AggregatedSharedImages.ICONS_VARIABLETYPE_IMG,
				getImageDescriptor(AggregatedSharedImages.ICONS_VARIABLETYPE_IMG));
		registry.put(AggregatedSharedImages.ICONS_VARIABLE_IMG,
				getImageDescriptor(AggregatedSharedImages.ICONS_VARIABLE_IMG));
		registry.put(AggregatedSharedImages.ICONS_VIEW_IMG, getImageDescriptor(AggregatedSharedImages.ICONS_VIEW_IMG));
		registry.put(AggregatedSharedImages.ICONS_EVENT_IMG,
				getImageDescriptor(AggregatedSharedImages.ICONS_EVENT_IMG));
		registry.put(AggregatedSharedImages.ICONS_ALARM_IMG,
				getImageDescriptor(AggregatedSharedImages.ICONS_ALARM_IMG));

		registry.put(AggregatedSharedImages.ICON_BUTTON_SERVER_STOPPED,
				getImageDescriptor(AggregatedSharedImages.ICON_BUTTON_SERVER_STOPPED));
		registry.put(AggregatedSharedImages.ICON_BUTTON_SERVER_RUNNING,
				getImageDescriptor(AggregatedSharedImages.ICON_BUTTON_SERVER_RUNNING));

		registry.put(AggregatedSharedImages.ICON_BUTTON_SERVER_START,
				getImageDescriptor(AggregatedSharedImages.ICON_BUTTON_SERVER_START));
		registry.put(AggregatedSharedImages.ICON_BUTTON_SERVER_START_SELECTED,
				getImageDescriptor(AggregatedSharedImages.ICON_BUTTON_SERVER_START_SELECTED));
		registry.put(AggregatedSharedImages.ICON_BUTTON_SERVER_START_DISABLED,
				getImageDescriptor(AggregatedSharedImages.ICON_BUTTON_SERVER_START_DISABLED));

		registry.put(AggregatedSharedImages.ICON_BUTTON_SERVER_STOP,
				getImageDescriptor(AggregatedSharedImages.ICON_BUTTON_SERVER_STOP));
		registry.put(AggregatedSharedImages.ICON_BUTTON_SERVER_STOP_SELECTED,
				getImageDescriptor(AggregatedSharedImages.ICON_BUTTON_SERVER_STOP_SELECTED));
		registry.put(AggregatedSharedImages.ICON_BUTTON_SERVER_STOP_DISABLED,
				getImageDescriptor(AggregatedSharedImages.ICON_BUTTON_SERVER_STOP_DISABLED));

		registry.put(AggregatedSharedImages.ICON_BROWSE, getImageDescriptor(AggregatedSharedImages.ICON_BROWSE));
		registry.put(AggregatedSharedImages.ICON_CONNECT, getImageDescriptor(AggregatedSharedImages.ICON_CONNECT));
		registry.put(AggregatedSharedImages.ICON_CHECK, getImageDescriptor(AggregatedSharedImages.ICON_CHECK));
		registry.put(AggregatedSharedImages.ICON_OPEN, getImageDescriptor(AggregatedSharedImages.ICON_OPEN));
		registry.put(AggregatedSharedImages.ICON_CLOSE, getImageDescriptor(AggregatedSharedImages.ICON_CLOSE));
	}
}
