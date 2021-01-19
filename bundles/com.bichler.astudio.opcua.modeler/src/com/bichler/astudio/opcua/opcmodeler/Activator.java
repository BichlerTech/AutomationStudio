package com.bichler.astudio.opcua.opcmodeler;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.osgi.framework.BundleContext;

import com.bichler.astudio.opcua.opcmodeler.utils.SharedImages;
import com.bichler.astudio.utils.activator.InternationalActivator;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends InternationalActivator {
	// The plug-in ID
	public static final String PLUGIN_ID = "com.bichler.astudio.opcua.modeler"; //$NON-NLS-1$
	public static final String BUNDLE_NAME = "com.bichler.astudio.opcua.opcmodeler.resource.custom"; //$NON-NLS-1$
	// The shared instance
	private static Activator plugin;

	private static final String CONFIGURATION = "configuration";
	private static final String NODESET2 = "Opc.Ua.NodeSet2.xml";
	
	/**
	 * The constructor
	 */
	public Activator() {
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
		// Locale l = Locale.GERMAN;
		// String lang = l.getLanguage();
		// String varLang = l.getVariant();
		// OPCDesignerString.setBundle(l);
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
	public static Activator getDefault() {
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
	
	public File getNodeset2File() {
		return getFile(getDefault().getBundle(), Path.ROOT.append(CONFIGURATION).append(NODESET2));
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
	
	protected void initializeImageRegistry(ImageRegistry registry) {
		registry.put(SharedImages.DESC_PREV_EVENT, createImageDescriptor(SharedImages.DESC_PREV_EVENT));
		registry.put(SharedImages.ICON_VISU_SERVER_16, createImageDescriptor(SharedImages.ICON_VISU_SERVER_16));
		registry.put(SharedImages.ICON_VISU_PROJECT_16, createImageDescriptor(SharedImages.ICON_VISU_PROJECT_16));
	}

	private ImageDescriptor createImageDescriptor(String id) {
		return imageDescriptorFromPlugin(PLUGIN_ID, id);
	}
}
