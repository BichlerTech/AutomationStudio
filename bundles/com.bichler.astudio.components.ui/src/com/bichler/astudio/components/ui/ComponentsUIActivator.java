package com.bichler.astudio.components.ui;

import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.osgi.framework.BundleContext;

import com.bichler.astudio.utils.activator.InternationalActivator;
import com.bichler.astudio.utils.constants.StudioConstants;

/**
 * The activator class controls the plug-in life cycle
 */
public class ComponentsUIActivator extends InternationalActivator {
	
	// The plug-in ID
	public static final String PLUGIN_ID = "com.bichler.astudio.components.ui"; //$NON-NLS-1$

	public static final String BUNDLE_NAME = "com.bichler.astudio.components.ui.resource.custom";
	// The shared instance
	private static ComponentsUIActivator plugin;
	
	/**
	 * The constructor
	 */
	public ComponentsUIActivator() {		
		try {
			RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault());
		} catch (Exception e) {
			RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, Locale.ENGLISH);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		setDefaultValues();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
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
	public static ComponentsUIActivator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
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
	public void setLanguageResourceBundle(Locale locale){
		Locale.setDefault(locale);
		try {
			RESOURCE_BUNDLE = ResourceBundle.getBundle(getBundleName(),
					locale);
		} catch (Exception e) {
			RESOURCE_BUNDLE = ResourceBundle.getBundle(getBundleName(),
					Locale.ENGLISH);
		}
	}
	
	@Override
	protected void initializeImageRegistry(ImageRegistry registry) {
		registry.put(ComponentsSharedImages.ICON_DATATYPE, getImageDescriptor(ComponentsSharedImages.ICON_DATATYPE));
		registry.put(ComponentsSharedImages.ICON_FOLDER, getImageDescriptor(ComponentsSharedImages.ICON_FOLDER));
		registry.put(ComponentsSharedImages.ICON_METHOD_M, getImageDescriptor(ComponentsSharedImages.ICON_METHOD_M));
		registry.put(ComponentsSharedImages.ICON_METHOD_O, getImageDescriptor(ComponentsSharedImages.ICON_METHOD_O));
		
		registry.put(ComponentsSharedImages.ICON_METHOD, getImageDescriptor(ComponentsSharedImages.ICON_METHOD));
		registry.put(ComponentsSharedImages.ICON_OBJECT_M, getImageDescriptor(ComponentsSharedImages.ICON_OBJECT_M));
		
		registry.put(ComponentsSharedImages.ICON_OBJECT_O, getImageDescriptor(ComponentsSharedImages.ICON_OBJECT_O));
		registry.put(ComponentsSharedImages.ICON_OBJECT, getImageDescriptor(ComponentsSharedImages.ICON_OBJECT));
		registry.put(ComponentsSharedImages.ICON_OBJECTTYPE, getImageDescriptor(ComponentsSharedImages.ICON_OBJECTTYPE));
		
		registry.put(ComponentsSharedImages.ICON_REFERENCETYPE, getImageDescriptor(ComponentsSharedImages.ICON_OBJECTTYPE));
		registry.put(ComponentsSharedImages.ICON_VARIABLE_M, getImageDescriptor(ComponentsSharedImages.ICON_OBJECTTYPE));
		registry.put(ComponentsSharedImages.ICON_VARIABLE_O, getImageDescriptor(ComponentsSharedImages.ICON_OBJECTTYPE));
		registry.put(ComponentsSharedImages.ICON_VARIABLE, getImageDescriptor(ComponentsSharedImages.ICON_OBJECTTYPE));
		registry.put(ComponentsSharedImages.ICON_VARIABLETYPE, getImageDescriptor(ComponentsSharedImages.ICON_OBJECTTYPE));
		registry.put(ComponentsSharedImages.ICON_VIEW, getImageDescriptor(ComponentsSharedImages.ICON_OBJECTTYPE));
		registry.put(ComponentsSharedImages.ICON_EVENT, getImageDescriptor(ComponentsSharedImages.ICON_EVENT));
		registry.put(ComponentsSharedImages.ICON_HOSTCONNECTION, getImageDescriptor(ComponentsSharedImages.ICON_HOSTCONNECTION));
		registry.put(ComponentsSharedImages.ICON_HOSTCONNECTION_GREEN, getImageDescriptor(ComponentsSharedImages.ICON_HOSTCONNECTION_GREEN));
		registry.put(ComponentsSharedImages.ICONS_SECURITY_CERT, getImageDescriptor(ComponentsSharedImages.ICONS_SECURITY_CERT));
		
		
		registry.put(ComponentsSharedImages.ICON_SERVER_UA, getImageDescriptor(ComponentsSharedImages.ICON_SERVER_UA));
		registry.put(ComponentsSharedImages.ICON_DISCOVERY_UA, getImageDescriptor(ComponentsSharedImages.ICON_DISCOVERY_UA));
		registry.put(ComponentsSharedImages.ICON_ENDPOINT_UA, getImageDescriptor(ComponentsSharedImages.ICON_ENDPOINT_UA));
		registry.put(ComponentsSharedImages.ICON_SECURITY_UA, getImageDescriptor(ComponentsSharedImages.ICON_SECURITY_UA));
		registry.put(ComponentsSharedImages.ICON_SECURITY_NONE, getImageDescriptor(ComponentsSharedImages.ICON_SECURITY_NONE));
	}
	
	/**
	   * set default values
	   */
	  private void setDefaultValues()
	  {
	    IPreferenceStore store = ComponentsUIActivator.getDefault().getPreferenceStore();
	    String binCommand = store.getString(StudioConstants.BinCommand);
	    /**
	     * if all values are empty
	     */
	    if (binCommand.isEmpty() )
	    {
	      store.setValue(StudioConstants.BinCommand, "hbin");
	    }
	    
	  }
}
