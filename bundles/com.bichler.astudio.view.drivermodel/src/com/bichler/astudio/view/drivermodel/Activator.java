package com.bichler.astudio.view.drivermodel;

import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import com.bichler.astudio.utils.activator.InternationalActivator;


/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends InternationalActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.bichler.astudio.view.drivermodel"; //$NON-NLS-1$

	private static final String BUNDLE_NAME = "com.bichler.astudio.view.drivermodel.resource.custom";

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {
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
	public static Activator getDefault() {
		return plugin;
	}

	@Override
	public void setLanguageResourceBundle(Locale locale) {
		Locale.setDefault(locale);
		try {
			RESOURCE_BUNDLE = ResourceBundle.getBundle(getBundleName(), locale);
		} catch (Exception e) {
			RESOURCE_BUNDLE = ResourceBundle.getBundle(getBundleName(),
					Locale.ENGLISH);
		}
	}

	@Override
	public String getBundleName() {
		return BUNDLE_NAME;
	}

	public static final String ICON_ARRAY = "array.png";
	public static final String ICON_STRUCTUR = "structur.png";
	public static final String ICON_VARIABLEN = "variablen.png";

	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		super.initializeImageRegistry(reg);

		registerImage(reg, ICON_ARRAY, "icons/"+ICON_ARRAY);
		registerImage(reg, ICON_STRUCTUR, "icons/"+ICON_STRUCTUR);
		registerImage(reg, ICON_VARIABLEN, "icons/"+ICON_VARIABLEN);
	}

	private void registerImage(ImageRegistry reg, String key, String path) {
		Bundle bundle = Platform.getBundle(PLUGIN_ID);
		ImageDescriptor myImage = ImageDescriptor.createFromURL(FileLocator
				.find(bundle, new Path(path), null));
		reg.put(key, myImage);
	}
	
	public static Image getImage(String key){
		return getDefault().getImageRegistry().get(key);
	}

}
